package dev.woc.katengine;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.internal.ImGuiContext;
import imgui.lwjgl3.glfw.ImGuiImplGlfwNative;
import org.joml.Vector2i;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;

/**
 * Generic class for Applications. Extend this and call the {@link Application#run} function to start it.
 */
public abstract class Application {

    private double startTime, lastFrame, deltaTime = 1.0 / 60.0;
    private Window window;

    private Config config = new Config();
    private ImGuiContext imGuiContext;
    private ImGuiIO imGuiIO;
    private ImGuiImplGlfw imGuiImplGlfw;
    private ImGuiImplGl3 imGuiImplGl;
    private Color backgroundColor = Color.WHITE;

    public Application() {
    }

    /**
     * Called when the application resources can be created.
     */
    public abstract void create();

    /**
     * Called each frame for render
     *
     * @param deltaTime how much time has it been since the last frame, in seconds
     */
    public abstract void render(double deltaTime);


    /**
     * Called when the application should cleanup its resources
     */
    public abstract void destroy();

    /**
     * Call this to start the app
     */
    public final void run() {
        internalCreate();
        create();

        lastFrame = GLFW.glfwGetTime();

        while (isOpen()) {
            preRender();
            render(deltaTime);
            postRender();
        }

        destroy();
        internalDestroy();
        postPostDestroy();
        System.exit(0);
    }

    private void postRender() {
        renderImGui();

        window.swap();

        double now = GLFW.glfwGetTime();
        deltaTime = now - lastFrame;
        lastFrame = now;
    }

    private void preRender() {
        window.poll();

        RenderUtils.setViewport(window.getFramebufferSize());
        RenderUtils.clear(backgroundColor);

        imGuiImplGlfw.newFrame();
        ImGui.newFrame();
    }

    private boolean isOpen() {
        return window.isOpen();
    }

    private void internalCreate() {
        window = new Window(config.width, config.height, config.title);

        imGuiContext = ImGui.createContext();
        imGuiIO = ImGui.getIO();
        imGuiIO.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard | ImGuiConfigFlags.ViewportsEnable | ImGuiConfigFlags.DockingEnable);
        ImGui.styleColorsLight();
        imGuiImplGlfw = new ImGuiImplGlfw();
        imGuiImplGl = new ImGuiImplGl3();

        imGuiIO.getFonts().addFontFromFileTTF("DroidSans.ttf", 14);

        imGuiImplGlfw.init(window.getHandle(), true);
        imGuiImplGl.init("#version 430 core");
    }

    private void internalDestroy() {
        ImGui.destroyContext();
        Callbacks.glfwFreeCallbacks(window.getHandle());
        GLFW.glfwTerminate();
    }

    public void renderImGui() {
        ImGui.render();
        imGuiImplGl.renderDrawData(ImGui.getDrawData());


        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = GLFW.glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            GLFW.glfwMakeContextCurrent(backupWindowPtr);
        }
    }

    public Window getWindow() {
        return window;
    }

    public abstract void postPostDestroy();


    public static class Config {
        public int width = 800, height = 800;
        public String title = "Window";

        public int getWidth() {
            return width;
        }

        public Config setWidth(int width) {
            this.width = width;
            return this;
        }

        public int getHeight() {
            return height;
        }

        public Config setHeight(int height) {
            this.height = height;
            return this;
        }

        public String getTitle() {
            return title;
        }

        public Config setTitle(String title) {
            this.title = title;
            return this;
        }
    }
}
