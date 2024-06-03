import java.awt.*;
import java.io.Serial;
import java.io.Serializable;

public class Circle extends ShapeEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 3L;

    public Circle(int x, int y, int width, int height, int depth, Color color) {
        super("Circle", x, y, width, height, depth, color);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(getColor());
        g.fillArc(x, y, width, height, 180, 180);
    }

    @Override
    public void drawTop(Graphics g) {
        g.setColor(getColor());
        g.fillOval(x, y, width, depth);
    }

    @Override
    public void drawUnder(Graphics g) {
        g.setColor(getColor());
        g.fillOval(x, y, width, depth);
    }

    @Override
    public double calculateVolume() {
        return 0.5 * 4/3 * Math.PI * width * height * depth;
    }

    @Override
    public double calculateArea() {
        return  4 * Math.PI * ((width * height + width * depth + height * depth) / 3 );
    }

}