package dev.woc.afterline.server;

public class Main {
    public static void main(String[] args) {
        int port = NetServer.DEFAULT_SERVER_PORT;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
            if (port < 0 || port > 65536) {
                port = NetServer.DEFAULT_SERVER_PORT;
            }
        }

        new Afterline(port).run();

    }
}