import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class GrassCell extends Cell {

    public GrassCell(char inLabel, int inRow, int inCol, int x, int y) {
        super(inLabel, inRow, inCol, x, y); 
    }
    

    @Override
    public Color getColor() {
        return new Color(102, 179, 88); 
    }

    @Override
    public boolean isWalkable() {
        return true; 
    }
    
    @Override
    public int getMovementCost() {
        return 1; 
    }

    @Override
    public void paint(Graphics g, Point mousePos) {
        g.setColor(getColor());
        g.fillRect(x, y, size, size);
        
        g.setColor(Color.DARK_GRAY);
        g.drawRect(x, y, size, size);

        if (contains(mousePos)) {
            g.setColor(new Color(255, 255, 255, 80)); 
            g.fillRect(x, y, size, size);
        }
    }
}