package dev.woc.afterline.client;

import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;

public abstract class AppWindow {
    protected static final int DEFAULT_FLAGS = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoCollapse;
    private String title;
    private int flags;
    private ImBoolean visible = new ImBoolean(false);
    private boolean wasVisibleBefore = false;

    public AppWindow(String title, int flags) {
        this.title = title;
        this.flags = flags;
        Afterline.windows.add(this);
    }

    public final void draw() {
        if (visible.get()) {
            if (!wasVisibleBefore) {
                this.onShow();
            }
            if (ImGui.begin(title, visible, flags)) {
                drawContents();
                ImGui.end();
            }
        } else {
            if (wasVisibleBefore) {
                this.onHide();
            }
        }
        wasVisibleBefore = visible.get();
    }

    protected abstract void onHide();

    protected abstract void onShow();

    public void show() {
        visible.set(true);
    }

    public void hide() {
        visible.set(false);
    }

    public abstract void drawContents();
}
