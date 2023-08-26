package com.tatv.baseapp.utils.string;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tatv on 18/8/2021.
 */

public class StringUtils {
    public static String upperToCaseFirst(String string) {
        if (!string.isEmpty() && string.length() > 1) {
            String upperString = string.substring(0, 1).toUpperCase() + string.substring(1);
            return upperString;
        } else {
            return string;
        }
    }

    public static String lowerToCaseFirst(String string) {
        if (!string.isEmpty() && string.length() > 1) {
            String upperString = string.substring(0, 1).toLowerCase() + string.substring(1);
            return upperString;
        } else {
            return string;
        }
    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isNumericRegex(String s) {
        if (s.matches(".*\\d.*")) {
            return true;
        }
        return false;
    }

    public static boolean isAlphabets(String s) {
        Pattern pattern = Pattern.compile(".*[a-zA-Z]+.*");
        Matcher matcher = pattern.matcher(s);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    public static boolean isSpecialCharacter(String s) {
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(s);
        return m.find();
    }

    public static String fixedLengthString(String string, int length) {
        StringBuilder temp = new StringBuilder();
        if (string.length() < length) {
            for (int i = 0; i < length - string.length(); i++) {
                temp.append("0");
            }
        }
        temp.append(string);
        return temp.toString();
    }
    public static String fixedLengthString(int value, int length) {
        String string = String.valueOf(value);
        StringBuilder temp = new StringBuilder();
        if (string.length() < length) {
            for (int i = 0; i < length - string.length(); i++) {
                temp.append("0");
            }
        }
        temp.append(string);
        return temp.toString();
    }
}
