package com.school.campusexpress.util;

import java.util.Random;

public class PickupCodeUtil {

    private static final Random random = new Random();

    public static String generate() {
        return String.format("%06d", random.nextInt(1000000));
    }
}
