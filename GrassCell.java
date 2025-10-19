import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class GrassCell extends Cell {

    public GrassCell(char inCol, int inRow, int x, int y) {
        super(inCol, inRow, x, y);
    }

    @Override
    public void paint(Graphics g, Point mousePos) {
        
        // 1. BASE COLOR
        g.setColor(new Color(152, 251, 152)); 
        g.fillRect(x, y, size, size); 

        // 2. FLOOD CHECK (Blue Overlay)
        if (this.getMovementCostMultiplier() > 1.0) {
            g.setColor(new Color(0, 0, 255, 128)); 
            g.fillRect(x, y, size, size);
        }

        // 3. DRAW OUTLINE
        g.setColor(Color.BLACK);
        g.drawRect(x, y, size, size);
        // NOTE: Goal is now drawn by GamePanel, not here.
    }
}