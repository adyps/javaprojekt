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
        return 0.5 * 3/4 * Math.PI * width/2 * height/2 * depth/2;
    }

    @Override
    public double calculateArea() {
        double a = width/2;
        double b = height/2;
        double c = depth/2;
        return  4 * Math.PI * Math.pow((( Math.pow((a * b), 1.6) + Math.pow((a * c), 1.6) + Math.pow((b * c), 1.6)) / 3 ), 1/1.6);
    }

}