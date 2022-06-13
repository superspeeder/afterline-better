package dev.woc.katengine;

public class Color {
    public static final Color WHITE = new Color(1.0f,1.0f,1.0f);
    public static final Color BLACK = new Color(0.0f,0.0f,0.0f);
    public static final Color RED = new Color(1.0f,0.0f,0.0f);
    public static final Color GREEN = new Color(0.0f,1.0f,1.0f);
    public static final Color BLUE = new Color(0.0f,0.0f,1.0f);
    public static final Color YELLOW = new Color(1.0f,1.0f,0.0f);
    public static final Color CYAN = new Color(0.0f,1.0f,1.0f);
    public static final Color MAGENTA = new Color(1.0f,0.0f,1.0f);

    public float r,g,b,a;

    public Color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Color(float r, float g, float b) {
        this(r,g,b,1.0f);
    }
}
