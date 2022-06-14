package dev.woc.afterline.client.windows;

import dev.woc.afterline.client.Afterline;
import dev.woc.afterline.client.AppWindow;
import dev.woc.afterline.client.net.NetClient;
import dev.woc.katengine.KatEngine;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;

public class AboutWindow extends AppWindow {
    public static final AboutWindow INSTANCE = new AboutWindow();

    private AboutWindow() {
        super("About", ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoDocking);
    }

    @Override
    public void drawContents() {
        ImGui.text("Afterline Version " + Afterline.VERSION);
        ImGui.text("Built with KatEngine Version " + KatEngine.VERSION);
        if (Afterline.INSTANCE.isConnected()) {
            NetClient net = Afterline.INSTANCE.getNetClient();
            ImGui.text("Connected to " + net.getConnectionAddress() + ":" + net.getConnectionPort());
        } else {
            ImGui.text("Not Connected: Retrying in " + Afterline.INSTANCE.getReconnectCooldown() + " seconds.");
            ImGui.text("Retries: " + Afterline.INSTANCE.getReconnectAttempts());
        }
    }
}
