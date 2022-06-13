package dev.woc.afterline.client;

import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;

public abstract class AppWindow {
    protected static final int DEFAULT_FLAGS = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoCollapse;
    private String title;
    private int flags;
    private ImBoolean visible = new ImBoolean(false);

    public AppWindow(String title, int flags) {
        this.title = title;
        this.flags = flags;
        Afterline.windows.add(this);
    }

    public final void draw() {
        if (visible.get()) {
            if (ImGui.begin(title, visible, flags)) {
                drawContents();
                ImGui.end();
            }
        }
    }

    public void show() {
        visible.set(true);
    }

    public void hide() {
        visible.set(false);
    }

    public abstract void drawContents();
}
