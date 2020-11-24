package org.ecs160.a1;

import java.security.SecureRandom;

public class Helper {
    private static final Helper INSTANCE = new Helper();
    private String DEFAULT_STR = "0.";
    private static int idCounter = 0;
    private final int PRECISION = 5;

    private Helper() {
        for (int i = 0; i < PRECISION; i++) {
            DEFAULT_STR = DEFAULT_STR.concat("0");
        }
    }
    public static Helper getInstance() {
        return INSTANCE;
    }

    public String getNewId(){
        String ret = Integer.toString(idCounter);
        idCounter += 1;
        return ret;
    }

    public void setIdCounter(int newVal) {
        idCounter = newVal;
    }

    public String fmt(double number) {
        String format = "%.".concat(Integer.toString(PRECISION)).concat("f");
        return String.format(format,number);
    }

    public String fmtShort(double number) {
        return String.format("%.3f",number);
    }

    public String getDefaultStr() {
        return DEFAULT_STR;
    }
}
