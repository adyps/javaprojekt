import java.awt.*;
import java.io.*;

public abstract class ShapeEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public String type;
    public int x;
    public int y;
    public int width;
    public int height;
    public int depth;
    public Color color;

    public ShapeEntity(String type, int x, int y, int width, int height, int depth, Color color) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public Color getColor() {
        return color;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getDepth() {
        return depth;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public abstract void draw(Graphics g);

    public abstract void drawTop(Graphics g);

    public abstract void drawUnder(Graphics g);

    public abstract double calculateVolume();

    public abstract double calculateArea();
}
