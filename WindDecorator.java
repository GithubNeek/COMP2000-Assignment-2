
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class WindDecorator extends CellDecorator {

    public WindDecorator(Cell decoratedCell) {
        super(decoratedCell); 
    }

    @Override
    public int getMovementCost() {
        return decoratedCell.getMovementCost() + 3;
    }
    
    @Override
    public void paint(Graphics g, Point mousePos) {
        decoratedCell.paint(g, mousePos);
        
      
        g.setColor(new Color(100, 100, 255, 120)); 
        
        g.drawLine(x, y, x + size, y + size);
        g.drawLine(x + size, y, x, y + size);
        
        g.fillRect(x, y, size, size);
    }
}
