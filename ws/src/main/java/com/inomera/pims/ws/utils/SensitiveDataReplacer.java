package com.inomera.pims.ws.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SensitiveDataReplacer {

    private static final String JSON_PASSWORD_MASK_PATTERN = "(\"loginPassword\"\\s*:\\s*\")[^\"]*(\")";
    private static final Pattern multilineMaskPattern = Pattern.compile(JSON_PASSWORD_MASK_PATTERN);

    public static String maskPassword(String text) {
        final Matcher matcher = multilineMaskPattern.matcher(text);
        while (matcher.find()) {
            text = matcher.replaceAll("$1*masked*$2");
        }
        return text;
    }


}
