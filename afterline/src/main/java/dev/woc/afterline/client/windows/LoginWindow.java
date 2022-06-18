package dev.woc.afterline.client.windows;

import dev.woc.afterline.client.Afterline;
import dev.woc.afterline.client.AppWindow;
import dev.woc.afterline.client.net.NetClient;
import dev.woc.afterline.common.net.message.FederatedLoginRequest;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;

public class LoginWindow extends AppWindow {
    public static LoginWindow INSTANCE = new LoginWindow();
    private ImString username = new ImString();
    private boolean showWarn = false;


    public LoginWindow() {
        super("Login", ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoDocking);
    }

    @Override
    protected void onHide() {
        showWarn = false;
    }

    @Override
    protected void onShow() {
        Afterline.LOGGER.info("Hello!");
    }

    @Override
    public void drawContents() {
        ImGui.text("Log in to afterline");
        ImGui.inputTextWithHint("Enter your username:", "username", username);

        if (showWarn) {
            ImGui.textColored(1.0f,0.0f,0.0f,0.0f, "You must enter a username to log in");
        }

        if (ImGui.button("Login with Google")) {
            if (username.isEmpty()) {
                showWarn = true;
            } else {
                NetClient.INSTANCE.postMessage(new FederatedLoginRequest(username.get()));
            }
        }

        if (Afterline.INSTANCE.getUsername() != null) {
            ImGui.text("Logged in as " + Afterline.INSTANCE.getUsername());
        }
    }
}
