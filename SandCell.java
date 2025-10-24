import java.awt.*;

public class SandCell extends Cell { 

    public SandCell(char inLabel, int inRow, int inCol, int x, int y) { 
        super(inLabel, inRow, inCol, x, y); 
    }


    @Override
    public Color getColor() {
        return new Color(230, 210, 140);
    }
    
    @Override
    public boolean isWalkable() {
        return true; 
    }
    
    @Override
    public int getMovementCost() { 
        return 2; 
    }

    @Override
    public void paint(Graphics g, Point mousePos) {
        
        g.setColor(getColor());
        g.fillRect(x, y, size, size);
        
        g.setColor(new Color(210, 190, 120));
        g.drawLine(x, y + size - 1, x + size - 1, y + size - 1); 
        g.setColor(Color.BLACK);
        g.drawRect(x, y, size, size);
    }
}