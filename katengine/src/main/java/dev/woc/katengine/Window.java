package dev.woc.katengine;

import org.joml.Vector2i;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;


public class Window {

    private long window;

    public Window(int width, int height, String title) {
        glfwInit();
        KatEngine.LOGGER.info("Initialized GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6);

        window = glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        KatEngine.LOGGER.info("Created {}x{} window with title \"{}\"", width, height, title);

        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        KatEngine.LOGGER.info("Initialized OpenGL");



        glfwSwapInterval(1);
    }

    public boolean isOpen() {
        return !glfwWindowShouldClose(window);
    }

    public void swap() {
        glfwSwapBuffers(window);
    }

    public void poll() {
        glfwPollEvents();
    }

    public long getHandle() {
        return window;
    }

    public Vector2i getFramebufferSize() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer xb = stack.mallocInt(1);
            IntBuffer yb = stack.mallocInt(1);
            glfwGetFramebufferSize(window, xb.rewind(), yb.rewind());
            return new Vector2i(xb.rewind().get(), yb.rewind().get());
        }
    }
}
