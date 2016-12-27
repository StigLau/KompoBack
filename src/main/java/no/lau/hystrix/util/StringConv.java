
package no.lau.hystrix.util;

import java.io.UnsupportedEncodingException;

public class StringConv {

    public static String UTF8(byte[] bytes) {
        try {
            if (bytes != null) {
                return new String(bytes, "UTF8");
            } else {
                return "";
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}