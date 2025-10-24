import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class SnowDecorator extends CellDecorator {

    public SnowDecorator(Cell decoratedCell) {
        super(decoratedCell); 
    }

    @Override
    public int getMovementCost() {
        return decoratedCell.getMovementCost() + 1;
    }
    
    @Override
    public void paint(Graphics g, Point mousePos) {
        decoratedCell.paint(g, mousePos);
        
        g.setColor(new Color(255, 255, 255, 180));
        g.fillRect(x, y, size, size);
    }
}