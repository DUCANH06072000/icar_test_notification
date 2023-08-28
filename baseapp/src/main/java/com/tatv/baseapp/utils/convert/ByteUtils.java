package com.tatv.baseapp.utils.convert;

import com.tatv.baseapp.utils.string.StringUtils;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class ByteUtils {
    public static String asciiToHex(String asciiStr) {
        char[] chars = asciiStr.toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char ch : chars) {
            hex.append(Integer.toHexString((int) ch));
        }

        return hex.toString();
    }

    public static String hexToAscii(String hexStr) {
        StringBuilder output = new StringBuilder("");

        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }

        return output.toString();
    }

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes, int lenght) {
        char[] hexChars = new char[lenght * 2];
        for (int j = 0; j < lenght; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length];
        for (int j = 0; j < bytes.length/2; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static int strHexToInt(String str) {
        if (str == null || str.length() < 1) {
            return 0;
        }
        int sum = 0;
        for (int i = 0; i < str.length(); i += 1) {
            int n;
            char c = str.charAt((str.length() - 1) - i);
            if ((c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F')) {
                n = Character.toUpperCase(c) - 55;
            } else if (c < '0' || c > '9') {
                throw new RuntimeException("\ufffd\u05b7\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\u03f7\ufffd");
            } else {
                n = c - 48;
            }
            sum += (1 << (i * 4)) * n;
        }
        return sum;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static byte[] intToByteArray(int a) {
        byte[] ret = new byte[4];
        ret[3] = (byte) (a & 0xFF);
        ret[2] = (byte) ((a >> 8) & 0xFF);
        ret[1] = (byte) ((a >> 16) & 0xFF);
        ret[0] = (byte) ((a >> 24) & 0xFF);
        return ret;
    }

    public static byte[] convertStringToByteArray(String str) {
        byte[] data;
        if (!str.equals("")) {
            int dataLength = (str.length() + 1) / 3;
            data = new byte[dataLength];
            for (int i = 0; i < dataLength; i++) {
                int value = Integer.parseInt(str.substring(i * 3, (i * 3) + 2), 16);
                data[i] = (byte) (value & 0xFF);
            }
            return data;
        }
        return new byte[0];
    }

    public static byte[] strToByteArray(String str) {
        try {
            String msg;
            byte[] data;
            StringBuilder sb = new StringBuilder();
            toHexString(sb, fromHexString(str));
            msg = sb.toString();
            data = fromHexString(msg);
            return data;
        } catch (Exception e) {
            return new byte[]{};
        }

    }

    public static byte[] fromHexString(final CharSequence s) {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        byte b = 0;
        int nibble = 0;
        for (int pos = 0; pos < s.length(); pos++) {
            if (nibble == 2) {
                buf.write(b);
                nibble = 0;
                b = 0;
            }
            int c = s.charAt(pos);
            if (c >= '0' && c <= '9') {
                nibble++;
                b *= 16;
                b += c - '0';
            }
            if (c >= 'A' && c <= 'F') {
                nibble++;
                b *= 16;
                b += c - 'A' + 10;
            }
            if (c >= 'a' && c <= 'f') {
                nibble++;
                b *= 16;
                b += c - 'a' + 10;
            }
        }
        if (nibble > 0)
            buf.write(b);
        return buf.toByteArray();
    }

    public static String toHexString(final byte[] buf) {
        return toHexString(buf, 0, buf.length);
    }

    static String toHexString(final byte[] buf, int begin, int end) {
        StringBuilder sb = new StringBuilder(3 * (end - begin));
        toHexString(sb, buf, begin, end);
        return sb.toString();
    }

    public static void toHexString(StringBuilder sb, final byte[] buf) {
        toHexString(sb, buf, 0, buf.length);
    }

    public static void toHexString(StringBuilder sb, final byte[] buf, int begin, int end) {
        for (int pos = begin; pos < end; pos++) {
            if (sb.length() > 0)
                sb.append(' ');
            int c;
            c = (buf[pos] & 0xff) / 16;
            if (c >= 10) c += 'A' - 10;
            else c += '0';
            sb.append((char) c);
            c = (buf[pos] & 0xff) % 16;
            if (c >= 10) c += 'A' - 10;
            else c += '0';
            sb.append((char) c);
        }
    }

    public static boolean checkSum(String msg) {
        try {
            int checksum = 0; //Checksum = (Type + Length + Data0 + Data1+…..+ Datan) ^ 0xFF & 0xFF
            int length = strHexToInt(msg.substring(2 * 3, 2 * 3 + 2));
            for (int i = 1; i <= length + 2; i++) {
                checksum += strHexToInt(msg.substring(i * 3, i * 3 + 2));
            }
            checksum = (checksum ^ 255) & 255;
            if (checksum == strHexToInt(msg.substring((length + 3) * 3, (length + 3) * 3 + 2))) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static int getCheckSumInt(String msg) {
        try {
            int checksum = 0; //Checksum = (Type + Length + Data0 + Data1+…..+ Datan) ^ 0xFF & 0xFF
            int length = strHexToInt(msg.substring(2 * 3, 2 * 3 + 2));
            for (int i = 1; i <= length + 2; i++) {
                checksum += strHexToInt(msg.substring(i * 3, i * 3 + 2));
            }
            checksum = (checksum ^ 255) & 255;

            return checksum;
        } catch (Exception e) {
            return -1;
        }
    }

    public static String getCheckSumHexString(String msg) {
        try {
            int checksum = 0; //Checksum = (Type ^ Length ^ Data0 ^ Data1 ^... ^ Datan)
            int length = msg.length();
            for (int i = 0; i < length - 1; i = i + 2) {
                checksum ^= strHexToInt(msg.substring(i, i + 2));
            }

            return StringUtils.fixedLengthString(Integer.toHexString(checksum), 2).toUpperCase();
        } catch (Exception e) {
            return "";
        }
    }

    public static int getCheckSumInt(List<Integer> data) {
        try {
            int checksum = 0;
            for (int i = 1; i <= data.get(2) + 2; i++) {
                checksum += data.get(i);
            }
            checksum = (checksum ^ 255) & 255;
            return checksum;
        } catch (Exception e) {
            return 0;
        }
    }


}
