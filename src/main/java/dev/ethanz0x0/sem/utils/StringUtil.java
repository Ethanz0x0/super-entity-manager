package dev.ethanz0x0.sem.utils;

public class StringUtil {

    public static int countOccurrences(String source, String target) {
        if (target.isEmpty()) return 0;

        int count = 0;
        int index = 0;

        while ((index = source.indexOf(target, index)) != -1) {
            count++;
            index += target.length();
        }

        return count;
    }



}
