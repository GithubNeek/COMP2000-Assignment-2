import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Player {
    private int col;
    private int row;

    // Use a non-static variable so the speed is set randomly for each new game
    private final long baseMoveDelayMs; 

    public Player(int col, int row) {
        this.col = col;
        this.row = row;
        
        // FINAL FIX: Randomize the base speed between 100ms (fast) and 600ms (slow)
        Random rand = new Random();
        this.baseMoveDelayMs = rand.nextInt(500) + 100; // Result is 100 to 599 ms
        
        System.out.println("New Game Speed Multiplier: " + this.baseMoveDelayMs);
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public void paint(Graphics g) {
        g.setColor(Color.RED);
        int x = col * Cell.size;
        int y = row * Cell.size;
        g.fillOval(x + 5, y + 5, Cell.size - 10, Cell.size - 10);
    }
    
    public void move(Grid<Cell> grid, int dx, int dy) {
        int newCol = col + dx;
        int newRow = row + dy;
        
        if (grid.inBounds(newRow, newCol)) {
            
            Cell targetCell = grid.get(newRow, newCol);
            
            // 1. IMPASSABLE CHECK: Block movement on WaterCell
            if (targetCell instanceof WaterCell) {
                System.out.println("Movement blocked: Water is impassable!");
                return; 
            }
            
            // 2. ASSIGNMENT 2 SLOWDOWN LOGIC
            double costMultiplier = targetCell.getMovementCostMultiplier();
            
            // Calculate delay using the randomized base speed
            long calculatedDelay = (long) (this.baseMoveDelayMs * costMultiplier);
            
            // Apply the delay using Thread.sleep()
            try {
                Thread.sleep(calculatedDelay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); 
            }
            
            // 3. Complete the move
            this.col = newCol;
            this.row = newRow;
        }
    }
}