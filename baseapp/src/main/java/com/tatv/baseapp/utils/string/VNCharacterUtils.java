package com.tatv.baseapp.utils.string;

import java.util.Arrays;

/**
 * Created by tatv on 16/12/2022.
 */
public class VNCharacterUtils {

    private static final char[] SOURCE_CHARACTERS = {'À', 'Á', 'Â', 'Ã', 'È', 'É',
            'Ê', 'Ì', 'Í', 'Ò', 'Ó', 'Ô', 'Õ', 'Ù', 'Ú', 'Ý', 'à', 'á', 'â',
            'ã', 'è', 'é', 'ê', 'ì', 'í', 'ò', 'ó', 'ô', 'õ', 'ù', 'ú', 'ý',
            'Ă', 'ă', 'Đ', 'đ', 'Ĩ', 'ĩ', 'Ũ', 'ũ', 'Ơ', 'ơ', 'Ư', 'ư', 'Ạ',
            'ạ', 'Ả', 'ả', 'Ấ', 'ấ', 'Ầ', 'ầ', 'Ẩ', 'ẩ', 'Ẫ', 'ẫ', 'Ậ', 'ậ',
            'Ắ', 'ắ', 'Ằ', 'ằ', 'Ẳ', 'ẳ', 'Ẵ', 'ẵ', 'Ặ', 'ặ', 'Ẹ', 'ẹ', 'Ẻ',
            'ẻ', 'Ẽ', 'ẽ', 'Ế', 'ế', 'Ề', 'ề', 'Ể', 'ể', 'Ễ', 'ễ', 'Ệ', 'ệ',
            'Ỉ', 'ỉ', 'Ị', 'ị', 'Ọ', 'ọ', 'Ỏ', 'ỏ', 'Ố', 'ố', 'Ồ', 'ồ', 'Ổ',
            'ổ', 'Ỗ', 'ỗ', 'Ộ', 'ộ', 'Ớ', 'ớ', 'Ờ', 'ờ', 'Ở', 'ở', 'Ỡ', 'ỡ',
            'Ợ', 'ợ', 'Ụ', 'ụ', 'Ủ', 'ủ', 'Ứ', 'ứ', 'Ừ', 'ừ', 'Ử', 'ử', 'Ữ',
            'ữ', 'Ự', 'ự',};

    private static final char[] DESTINATION_CHARACTERS = {'A', 'A', 'A', 'A', 'E',
            'E', 'E', 'I', 'I', 'O', 'O', 'O', 'O', 'U', 'U', 'Y', 'a', 'a',
            'a', 'a', 'e', 'e', 'e', 'i', 'i', 'o', 'o', 'o', 'o', 'u', 'u',
            'y', 'A', 'a', 'D', 'd', 'I', 'i', 'U', 'u', 'O', 'o', 'U', 'u',
            'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A',
            'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'E', 'e',
            'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E',
            'e', 'I', 'i', 'I', 'i', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o',
            'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O',
            'o', 'O', 'o', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u',
            'U', 'u', 'U', 'u',};


    private static final String[] SOURCE_DIACRITICS = { "òa", "óa", "ỏa", "õa", "ọa", "òe", "óe", "ỏe", "õe", "ọe", "ùy", "úy", "ủy", "ũy", "ụy"};

    private static final String[] DESTINATION_SOURCE_DIACRITICS = {"oà", "oá", "oả", "oã", "oạ", "oè", "oé", "oẻ", "oẽ", "oẹ", "uỳ", "uý", "uỷ", "uỹ", "uỵ" };

    public static char removeAccent(char ch) {
        int index = Arrays.binarySearch(SOURCE_CHARACTERS, ch);
        if (index >= 0) {
            ch = DESTINATION_CHARACTERS[index];
        }
        return ch;
    }

    public static String removeAccent(String str) {
        StringBuilder sb = new StringBuilder(str);
        for (int i = 0; i < sb.length(); i++) {
            sb.setCharAt(i, removeAccent(sb.charAt(i)));
        }
        return sb.toString();
    }

    /*
    * So sánh không dấu
    * */
    public static boolean contains(String value, String key) {
        // make both strings lowercase and remove spaces
        if(key.matches("[a-zA-Z0-9 ]+"))
        {
            value = VNCharacterUtils.removeAccent(value);
            key = VNCharacterUtils.removeAccent(key);
        }
        value = value.toLowerCase().replace(" ", "");
        key = key.toLowerCase().replace(" ", "");

        // use a char array
        char[] letters = key.toCharArray();

        // loop through it
        for (char c : letters) {
            if (!value.contains(c + "")) {
                return false;   // stop searching if can't find at least one char
            }

            // remove the first part of the string, so that we're searching for
            // letters that exist in a string in order they appear in the key.
            value = value.substring(value.indexOf(c));
        }

        return true;
    }

    /*
     * So sánh có dấu
     * */
    public static boolean scontains(String value, String key){
        value = value.toLowerCase();
        key = key.toLowerCase();

        for(int i = 0; i < SOURCE_DIACRITICS.length; i++){
            if(value.contains(SOURCE_DIACRITICS[i])){
                value = value.replaceAll(SOURCE_DIACRITICS[i], DESTINATION_SOURCE_DIACRITICS[i]);
            }
        }

        for(int i = 0; i < SOURCE_DIACRITICS.length; i++){
            if(key.contains(SOURCE_DIACRITICS[i])){
                key = key.replaceAll(SOURCE_DIACRITICS[i], DESTINATION_SOURCE_DIACRITICS[i]);
            }
        }
        return value.contains(key);
    }

    /*
     * So sánh có dấu bỏ ký tự đặc biệt
     * */
    public static boolean sscontains(String value, String key){
        value = value.replaceAll("(\\s|-|\\.|_)+", " ").toLowerCase();
        key = key.replaceAll("(\\s|-|\\.|_)+", " ").toLowerCase();

        for(int i = 0; i < SOURCE_DIACRITICS.length; i++){
            if(value.contains(SOURCE_DIACRITICS[i])){
                value = value.replaceAll(SOURCE_DIACRITICS[i], DESTINATION_SOURCE_DIACRITICS[i]);
            }
        }

        for(int i = 0; i < SOURCE_DIACRITICS.length; i++){
            if(key.contains(SOURCE_DIACRITICS[i])){
                key = key.replaceAll(SOURCE_DIACRITICS[i], DESTINATION_SOURCE_DIACRITICS[i]);
            }
        }
        return value.contains(key);
    }


}
