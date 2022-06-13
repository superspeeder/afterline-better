package dev.woc.afterline.client.windows;

import dev.woc.afterline.client.Afterline;
import dev.woc.afterline.client.AppWindow;
import dev.woc.katengine.KatEngine;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;

public class AboutWindow extends AppWindow {
    public static final AboutWindow INSTANCE = new AboutWindow();

    private AboutWindow() {
        super("About", AppWindow.DEFAULT_FLAGS | ImGuiWindowFlags.NoDocking);
    }

    @Override
    public void drawContents() {
        ImGui.text("Afterline Version " + Afterline.VERSION);
        ImGui.text("Built with KatEngine Version " + KatEngine.VERSION);
    }
}
