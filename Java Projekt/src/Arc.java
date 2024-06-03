import java.awt.*;
import java.io.Serial;
import java.io.Serializable;

public class Arc extends ShapeEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 4L;

    public Arc(int x, int y, int width, int height, int depth, Color color) {
        super("Arc", x, y, width, height, depth, color);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(getColor());
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(depth));
        g.drawArc(x, y, width, height, 260, 200);
    }

    @Override
    public void drawTop(Graphics g) {
        g.setColor(getColor());
        g.fillRect(x, y, width, depth);
    }

    @Override
    public void drawUnder(Graphics g) {
    }

    @Override
    public double calculateVolume() {
        return 2 * Math.PI * Math.PI * width * width * depth;
    }

    @Override
    public double calculateArea() {
        return 4 * Math.PI * Math.PI * width * depth;
    }

}