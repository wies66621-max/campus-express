package com.school.campusexpress.util;

import java.util.Random;

public class PickupCodeUtil {

    private static final Random random = new Random();

    public static String generate() {
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}
