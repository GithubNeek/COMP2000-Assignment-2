import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public abstract class Cell extends Rectangle {
    
    public static final int size = 40;
    protected char label;
    protected int r, c;
    protected int x, y;
    
    public Cell(char label, int r, int c, int x, int y) {
        super(x, y, size, size); 
        this.label = label;
        this.r = r;
        this.c = c;
        this.x = x;
        this.y = y;
    }

    public abstract void paint(Graphics g, Point mousePos);
    public abstract int getMovementCost();
    public abstract boolean isWalkable();
    public abstract Color getColor();

    public char getLabel() { return label; }
    public int getR() { return r; }
    public int getC() { return c; }
    
    public int getCellX() { return x; } 
    public int getCellY() { return y; }
}