import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public abstract class Cell extends Rectangle {
    
    public static int size = 35;
    public final char col;
    public final int row;

    private ICellEffect currentEffect = new NormalEffect();
    
    private static long startTime = System.currentTimeMillis(); // For the pulse effect

    public Cell(char inCol, int inRow, int x, int y) {
        super(x, y, size, size);
        col = inCol;
        row = inRow;
    }

    protected float getPulseValue() {
        long elapsed = System.currentTimeMillis() - startTime;
        return (float) (Math.sin(elapsed / 500.0) + 1.0) / 2.0f; 
    }

    public void setEffect(ICellEffect newEffect) {
        this.currentEffect = newEffect;
    }

    public double getMovementCostMultiplier() {
        return this.currentEffect.getMovementCostMultiplier();
    }

    public void applyWeather(WeatherData data) {
        if (data.getAttribute().equals("rain")) {
            if (data.getValue() >= 0.7) {
                this.setEffect(new FloodingEffect());
            } else if (data.getValue() < 0.3) {
                this.setEffect(new NormalEffect());
            }
        }
    }
    
    // FINAL FIX: This abstract method must include all possible parameters.
    // SandCell and WaterCell will just ignore the last two.
    public abstract void paint(Graphics g, Point mousePos, int goalRow, int goalCol);

    // Overload for backwards compatibility (for non-Goal cells)
    public void paint(Graphics g, Point mousePos) {
        paint(g, mousePos, -1, -1);
    }
}