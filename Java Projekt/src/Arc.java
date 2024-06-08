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
        g2d.setStroke(new BasicStroke(width/50));
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
        double circumference = Math.PI * Math.sqrt(2 * (width/2 * width/2 + height/2 * height/2));
        return 5/9 * Math.PI * circumference * depth/2 * width/50;
    }

    @Override
    public double calculateArea() {
        double circumference = Math.PI * Math.sqrt(2 * (width/2 * width/2 + height/2 * height/2));
        return 5/9 * circumference * Math.PI * (width/50 + depth/2);
    }

}