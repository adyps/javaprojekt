import java.awt.*;
import java.io.Serial;

public class Rectangle extends ShapeEntity {
    @Serial
    private static final long serialVersionUID = 2L;

    public Rectangle(int x, int y, int width, int height, int depth, Color color) {
        super("Rectangle", x, y, width, height, depth, color);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(getColor());
        g.fillRect(x, y, width, height);
    }

    @Override
    public void drawTop(Graphics g) {
        g.setColor(getColor());
        g.drawRect(x, y, width, height);
    }

    @Override
    public void drawUnder(Graphics g) {
        g.setColor(getColor());
        g.fillOval(x, y, width, depth);
    }


    @Override
    public double calculateVolume() {
        return width * height * depth;
    }

    @Override
    public double calculateArea() {
        return (width*height) * 2 + (height*depth) * 2 + (depth*width) * 2;
    }
}
