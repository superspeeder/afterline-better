package dev.woc.afterline.client;

import dev.woc.katengine.KatEngine;

public class Main {

    public static void main(String[] args) {
        KatEngine.LOGGER.info("Hello World!");
        KatEngine.LOGGER.info("Running KatEngine Version {}", KatEngine.VERSION);

        Afterline afterline = new Afterline();
        afterline.run();
    }
}
