package dev.ethanz0x0.sem.translations;

import dev.ethanz0x0.sem.utils.MessageUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class Translation {

    public static String translate(String lang, String translationKey, Object... replacers) {
        if (!TranslationLibrary.getLoadedTranslations().containsKey(lang)) {
            lang = TranslationLibrary.getDefaultLanguage();
        }
        String translation = TranslationLibrary.getLanguage(lang).getProperty(translationKey);
        if (translation == null) {
            return "&cNO_TRANSLATION(" + translationKey + ")";
        }
        int index = 0;
        for (Object replacer : replacers) {
            translation = translation.replace("{" + index + "}", replacer.toString());
            index ++;
        }
        return translation;
    }

    public static void sendMessage(CommandSender receiver, Level level, String translationKey, Object... replacers) {
        String lang = TranslationLibrary.getDefaultLanguage();
        if (receiver instanceof Player) {
            lang = ((Player) receiver).getLocale();
        }

        receiver.sendMessage(MessageUtil.parseColors("&8[&eSEM&7/" +
                getLevelText(level) + "&8] &f" + translate(lang, translationKey, replacers)));
    }

    public static String getLevelText(Level level) {
        if (level == Level.INFO) {
            return "&aINFO";
        } else if (level == Level.WARNING) {
            return "&6WARN";
        } else if (level == Level.SEVERE) {
            return "&cSEVERE";
        } else {
            return "&cUNKNOWN";
        }
    }
}
