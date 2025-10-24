import java.awt.*;

public class WaterCell extends Cell { 

    public WaterCell(char inLabel, int inRow, int inCol, int x, int y) { 
        super(inLabel, inRow, inCol, x, y); 
    }


    @Override
    public Color getColor() {
        return new Color(90, 150, 220);
    }

    @Override
    public boolean isWalkable() {
        return false; 
    }

    @Override
    public int getMovementCost() { 
        return 999; 
    }

    @Override
    public void paint(Graphics g, Point mousePos) {
        
        g.setColor(getColor());
        g.fillRect(x, y, size, size);
        
        g.setColor(new Color(70, 130, 200));
        g.drawArc(x + 6, y + 8, size - 12, size - 18, 0, 180);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, size, size);
    }
}