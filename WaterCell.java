import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class WaterCell extends Cell {

    public WaterCell(char inCol, int inRow, int x, int y) {
        super(inCol, inRow, x, y);
    }

    @Override
    public void paint(Graphics g, Point mousePos) {
        
        // 1. BASE COLOR: Dark Blue to represent deep, impassable water
        g.setColor(new Color(0, 51, 153)); 
        g.fillRect(x, y, size, size); 

        // 2. FLOOD CHECK: Still shows the flood effect on top of the dark blue
        if (this.getMovementCostMultiplier() > 1.0) {
            g.setColor(new Color(0, 0, 255, 128)); 
            g.fillRect(x, y, size, size);
        }

        // 3. DRAW OUTLINE
        g.setColor(Color.BLACK);
        g.drawRect(x, y, size, size);
    }
}