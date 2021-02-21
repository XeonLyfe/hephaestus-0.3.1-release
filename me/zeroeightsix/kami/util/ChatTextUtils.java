/*
 * Decompiled with CFR 0.151.
 */
package me.zeroeightsix.kami.util;

import java.util.Random;

public class ChatTextUtils {
    private static Random rand = new Random();

    public static String appendChatSuffix(String s) {
        s = ChatTextUtils.cropMaxLengthMessage(s, " \u23d0 \u041d\u03b5\u13ae\u043d\u15e9\u03b5\u0455\u01ad\u03c5\u0455".length());
        s = s + " \u23d0 \u041d\u03b5\u13ae\u043d\u15e9\u03b5\u0455\u01ad\u03c5\u0455";
        return s;
    }

    public static String generateRandomHexSuffix(int n) {
        StringBuffer sb = new StringBuffer();
        sb.append(" [");
        sb.append(Integer.toHexString((rand.nextInt() + 11) * rand.nextInt()).substring(0, n));
        sb.append(']');
        return sb.toString();
    }

    public static String cropMaxLengthMessage(String s, int i) {
        if (s.length() >= 256 - i) {
            s = s.substring(0, 256 - i);
        }
        return s;
    }
}

