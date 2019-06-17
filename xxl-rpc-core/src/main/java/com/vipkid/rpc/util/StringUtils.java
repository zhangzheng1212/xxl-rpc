package com.vipkid.rpc.util;

public class StringUtils {
    public static boolean isEmpty(String string) {
        if (string == null) {
            return false;
        }

        return string.length() != 0;
    }
}
