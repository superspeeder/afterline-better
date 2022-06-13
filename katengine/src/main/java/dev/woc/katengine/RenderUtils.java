package dev.woc.katengine;

import org.joml.Vector2i;

import static org.lwjgl.opengl.GL46.*;

public class RenderUtils {
    public static void setViewport(Vector2i viewportSize) {
        glViewport(0, 0, viewportSize.x, viewportSize.y);
    }

    public static void setViewport(Vector2i corner, Vector2i viewportSize) {
        glViewport(corner.x, corner.y, viewportSize.x, viewportSize.y);
    }

    public static void clear(Color backgroundColor) {
        glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
        clear();
    }

    public static void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
    }
}
