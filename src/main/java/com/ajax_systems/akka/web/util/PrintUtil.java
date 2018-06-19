package com.ajax_systems.akka.web.util;

import com.ajax_systems.akka.model.SomeTxtLevel;

public class PrintUtil {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";
    private static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    private static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    private static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    private static final String ANSI_GRAY_BACKGROUND = "\u001B[47m";
    private static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    private static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    private static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    private static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    private static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    private static final String formatOutData = "|" + ANSI_BLUE + " %1$-10s " + ANSI_RESET + "|" + ANSI_CYAN + " %2$-50s " + ANSI_RESET + "|" + ANSI_BLUE + " %3$-5s " + ANSI_RESET + "|\n";

    public static void print(SomeTxtLevel someTxtLevel) {
        String[] out = new String[]{someTxtLevel.getLevel(), someTxtLevel.getDescription(), String.valueOf(someTxtLevel.getSize())};
        System.out.format(formatOutData, out);
    }
}
