package dev.woc.afterline.client;

import dev.woc.afterline.client.windows.AboutWindow;
import dev.woc.katengine.Application;
import imgui.ImGui;
import imgui.ImGuiViewport;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class Afterline extends Application {
    public static final String VERSION = "0.1.0a";
    public static Afterline INSTANCE;

    private static int defaultWindowFlags = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoCollapse ;
    public static final Set<AppWindow> windows = new HashSet<>();

    private boolean firstF = true;

    public Afterline() {
        INSTANCE = this;
    }

    @Override
    public void create() {
        ImGui.styleColorsDark();
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

    }

    public void onConnectToServer() {

    }
}
