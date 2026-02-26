package net.alcaris.plugin.template;

public final class SharedUtil {
    public SharedUtil() {}

    public static void printStartupMessage(String platform) {
        System.out.println("[Common] Hello from SharedUtil! Running on " + platform);
    }
}