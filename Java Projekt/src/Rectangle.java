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
        return Math.PI * width/2 * height * depth/2;
    }

    @Override
    public double calculateArea() {
        return Math.PI * (width/2 + depth/2) * height + 2 * width/2 * depth/2 * Math.PI;
    }
}
