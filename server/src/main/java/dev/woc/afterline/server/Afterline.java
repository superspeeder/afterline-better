package dev.woc.afterline.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Afterline {
    public static Afterline INSTANCE;
    public static final Logger LOGGER = LogManager.getLogger("Main");

    private NetServer net;

    public Afterline(int port) {
        net = new NetServer(this, port);
    }


    public void onServerStart() {

    }

    public void run() {
        net.launchAsThread();
    }
}
