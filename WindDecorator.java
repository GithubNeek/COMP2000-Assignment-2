// WindDecorator.java (NEW FILE)

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class WindDecorator extends CellDecorator {

    public WindDecorator(Cell decoratedCell) {
        super(decoratedCell); 
    }

    @Override
    public int getMovementCost() {
        // Wind adds 3 to the movement cost (more than snow)
        return decoratedCell.getMovementCost() + 3;
    }
    
    @Override
    public void paint(Graphics g, Point mousePos) {
        // First, let the base cell (Grass, Sand, etc.) paint itself
        decoratedCell.paint(g, mousePos);
        
        // Then, draw a visual indicator of wind (e.g., semi-transparent blue overlay)
        // Wind Effect: A transparent, fast-moving blue/gray
        g.setColor(new Color(100, 100, 255, 120)); 
        
        // Draw diagonal lines or a swirl to represent wind movement
        g.drawLine(x, y, x + size, y + size);
        g.drawLine(x + size, y, x, y + size);
        
        // Draw the semi-transparent overlay
        g.fillRect(x, y, size, size);
    }
}