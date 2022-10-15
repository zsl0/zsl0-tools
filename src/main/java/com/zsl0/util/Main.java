package com.zsl0.util;

import java.util.UUID;

/**
 * @author zsl0
 * @date ${DATE} ${TIME}
 */
public class Main {
    public static void main(String[] args) {
        for (int i = 0; i < 10000; i++) {
            System.out.printf("===================== %d =================%n", i);
            uuid2long();
        }


    }

    private static void uuid2long() {
        String hex = UUID.randomUUID().toString().replace("-", "");
        String heightStr = hex.substring(0, 16);
        String lowStr = hex.substring(16);
        System.out.println(heightStr + ":" + lowStr);
        long heightInt = Long.parseUnsignedLong(heightStr, 16);
        long lowInt = Long.parseUnsignedLong(lowStr, 16);
        System.out.println(heightInt + ":" + lowInt);
    }
}