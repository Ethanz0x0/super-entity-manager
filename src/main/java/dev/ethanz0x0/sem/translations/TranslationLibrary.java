package dev.ethanz0x0.sem.translations;

import dev.ethanz0x0.sem.Config;
import dev.ethanz0x0.sem.SuperEntityManager;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class TranslationLibrary {

    private static final SuperEntityManager plugin = SuperEntityManager.getInstance();
    private static final String GITHUB_API_URL = "https://api.github.com/repos/Ethanz0x0/super-entity-manager/contents/translations";
    private static final File TRANSLATIONS_DIR = new File(plugin.getDataFolder().toString() + "/translations");

    private static final Map<String, Properties> loadedTranslations = new HashMap<>();
    private static String defaultLanguage;

    public static Map<String, Properties> getLoadedTranslations() {
        return loadedTranslations;
    }

    public static String getDefaultLanguage() {
        return defaultLanguage;
    }

    public static Properties getLanguage(String lang) {
        return loadedTranslations.get(lang.toLowerCase());
    }

    public static boolean loadTranslations() {
        loadedTranslations.clear();
        downloadTranslations();
        defaultLanguage = Config.getConfig().getString("default_language");
        File[] files = TRANSLATIONS_DIR.listFiles((dir, name) -> name.endsWith(".properties"));
        if (files == null || files.length == 0) {
            plugin.getLogger().severe("No translation files found.");
            return false;
        }
        for (File file : files) {
            Properties properties = new Properties();
            try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8)) {
                properties.load(reader);
            } catch (IOException e) {
                plugin.getLogger().severe("Failed to load translation file: " + file.getName());
                continue;
            }
            loadedTranslations.put(file.getName().split("\\.")[0].toLowerCase(), properties);
        }
        if (loadedTranslations.isEmpty()) {
            plugin.getLogger().severe("No translation files successfully loaded.");
            return false;
        }
        plugin.getLogger().info("Loaded " + loadedTranslations.size() + " translations.");
        return true;
    }

    public static void downloadTranslations() {
        plugin.getLogger().info("Downloading translations from GitHub...");
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(GITHUB_API_URL).openConnection();
            connection.setRequestProperty("User-Agent", "JavaPlugin/1.0");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            if (connection.getResponseCode() != 200) {
                plugin.getLogger().severe("Failed to download translation files: cannot connect to GitHub API: " + connection.getResponseCode());
                return;
            }

            String json = readInputStream(connection.getInputStream());

            List<FileEntry> files = parseJsonForFiles(json);

            for (FileEntry file : files) {
                downloadFile(file.downloadUrl, TRANSLATIONS_DIR + "/" + file.name);
            }

            plugin.getLogger().info("Successfully downloaded translations from GitHub.");
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to download translation files: " + e.getMessage());
        }
    }

    private static void downloadFile(String fileUrl, String savePath) throws IOException {
        URL url = new URL(fileUrl);
        Path target = Paths.get(savePath);
        Files.createDirectories(target.getParent());

        if (Files.exists(target)) {
            return;
        }

        try (InputStream in = url.openStream()) {
            Files.copy(in, target);
        }
    }


    private static String readInputStream(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line.trim());
        }
        return builder.toString();
    }

    private static List<FileEntry> parseJsonForFiles(String json) {
        List<FileEntry> files = new ArrayList<>();
        String[] entries = json.split("},\\{");

        for (String entry : entries) {
            String name = extractJsonField(entry, "\"name\":\"", "\"");
            String downloadUrl = extractJsonField(entry, "\"download_url\":\"", "\"");
            if (name != null && downloadUrl != null) {
                files.add(new FileEntry(name, downloadUrl));
            }
        }

        return files;
    }

    private static String extractJsonField(String source, String prefix, String suffix) {
        int start = source.indexOf(prefix);
        if (start == -1) return null;
        start += prefix.length();
        int end = source.indexOf(suffix, start);
        if (end == -1) return null;
        return source.substring(start, end).replace("\\/", "/");
    }

    private static class FileEntry {
        String name;
        String downloadUrl;

        public FileEntry(String name, String downloadUrl) {
            this.name = name;
            this.downloadUrl = downloadUrl;
        }
    }
}
