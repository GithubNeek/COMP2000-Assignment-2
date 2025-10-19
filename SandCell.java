import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class SandCell extends Cell {

    public SandCell(char inCol, int inRow, int x, int y) {
        super(inCol, inRow, x, y);
    }

    @Override
    public void paint(Graphics g, Point mousePos) {
        
        // 1. BASE COLOR: Standard Sand Yellow
        g.setColor(Color.YELLOW); 
        g.fillRect(x, y, size, size); 

        // 2. FLOOD CHECK: Draw semi-transparent blue overlay 
        if (this.getMovementCostMultiplier() > 1.0) {
            g.setColor(new Color(0, 0, 255, 128)); 
            g.fillRect(x, y, size, size);
        }

        // 3. DRAW OUTLINE
        g.setColor(Color.BLACK);
        g.drawRect(x, y, size, size);
    }
}