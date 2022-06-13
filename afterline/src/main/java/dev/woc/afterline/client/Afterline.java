package dev.woc.afterline.client;

import dev.woc.afterline.client.net.NetClient;
import dev.woc.afterline.client.windows.AboutWindow;
import dev.woc.afterline.common.net.message.PingMessage;
import dev.woc.katengine.Application;
import imgui.ImGui;
import imgui.ImGuiViewport;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 */
public class Afterline extends Application {
    public static final String VERSION = "0.1.0a";
    public static final Logger LOGGER = LogManager.getLogger("Afterline");
    private static final int RECONNECT_COOLDOWN = 30;

    public static Afterline INSTANCE;

    private static int defaultWindowFlags = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoCollapse ;
    public static final Set<AppWindow> windows = new HashSet<>();

    private NetClient net;
    private Thread netThread;
    private boolean isConnected;
    private Timer reconnectTimer = new Timer();
    private Instant nextReconnect;
    private int connectionRetries = 0;
    private boolean continueTryingToConnect = true;

    public Afterline() {
        INSTANCE = this;
    }

    @Override
    public void create() {
        net = new NetClient(this);
        tryConnect();

        ImGui.styleColorsDark();
    }

    public void tryConnect() {
        LOGGER.info("Trying to connect");
        netThread = new Thread(net);
        netThread.start();
        connectionRetries += 1;

    }

    @Override
    public void render(double deltaTime) {
        drawMenubarDockspaceWindow();

        windows.forEach(AppWindow::draw);
    }

    private static final int fullwin_flags = ImGuiWindowFlags.NoCollapse |
            ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.MenuBar |
            ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoNavFocus |
            ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoDocking;

    private void drawMenubarDockspaceWindow() {
        ImGuiViewport vp = ImGui.getMainViewport();
        ImVec2 corner = vp.getPos();
        ImVec2 size = vp.getSize();
        ImGui.setNextWindowSize(size.x, size.y);
        ImGui.setNextWindowPos(corner.x, corner.y);
        ImGui.getStyle().setWindowRounding(0);

        ImGui.begin("fullwin", fullwin_flags);
        ImGui.dockSpace(ImGui.getID("mainwin-dockspace"));

        if (ImGui.beginMenuBar()) {
            if (ImGui.beginMenu("Afterline")) {
                if (ImGui.menuItem("About")) {
                    AboutWindow.INSTANCE.show();
                }
                ImGui.endMenu();
            }
            ImGui.endMenuBar();
        }

        ImGui.end();
    }

    @Override
    public void destroy() {
        continueTryingToConnect = false;
        netThread.interrupt();
        reconnectTimer.cancel();
    }

    public void onConnectToServer() {
        net.postMessage(new PingMessage());
        isConnected = true;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public NetClient getNetClient() {
        return net;
    }

    public long getReconnectCooldown() {
        return Duration.between(Instant.now(), nextReconnect).getSeconds();
    }

    public boolean isContinueTryingToConnect() {
        return continueTryingToConnect;
    }

    public void onDisconnected() {
        isConnected = false;
        if (continueTryingToConnect) {
            LOGGER.warn("Lost/Failed Connection, retrying in 30 seconds");
            reconnectTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    tryConnect();
                }
            }, 1000 * RECONNECT_COOLDOWN);
            nextReconnect = Instant.now().plus(Duration.of(30, ChronoUnit.SECONDS));
        }
    }
}
