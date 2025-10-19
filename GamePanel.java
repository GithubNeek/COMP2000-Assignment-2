import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;

public class GamePanel extends JPanel {
    private final Grid<Cell> grid;
    private final Player player;
    
    // NEW: Variable to track the pulse for the goal highlight
    private static long startTime = System.currentTimeMillis(); 

    public GamePanel(Grid<Cell> grid, Player player) {
        this.grid = grid;
        this.player = player;
        
        int width = grid.cols() * Cell.size;
        int height = grid.rows() * Cell.size;
        this.setPreferredSize(new Dimension(width, height));

        this.setFocusable(true); 

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                int dx = 0;
                int dy = 0;

                if (keyCode == KeyEvent.VK_W) { 
                    dy = -1;
                } else if (keyCode == KeyEvent.VK_S) { 
                    dy = 1;
                } else if (keyCode == KeyEvent.VK_A) { 
                    dx = -1;
                } else if (keyCode == KeyEvent.VK_D) { 
                    dx = 1;
                }
                
                if (dx != 0 || dy != 0) {
                    player.move(grid, dx, dy);
                    repaint(); 
                }
            }
        });
    }

    // Helper method to calculate the pulse value (0 to 1)
    private float getPulseValue() {
        long elapsed = System.currentTimeMillis() - startTime;
        return (float) (Math.sin(elapsed / 500.0) + 1.0) / 2.0f; 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        
        int goalR = grid.getGoalRow();
        int goalC = grid.getGoalCol();

        // 1. Draw all cells (Using the simpler 2-argument paint method now)
        for (int r = 0; r < grid.rows(); r++) {
            for (int c = 0; c < grid.cols(); c++) {
                grid.get(r, c).paint(g, null); 
            }
        }
        
        // 2. Draw the GOAL OVERLAY (Always visible, bright RED)
        float pulse = getPulseValue();
        int redBrightness = (int) (150 + 105 * pulse); 
        int x = goalC * Cell.size;
        int y = goalR * Cell.size;

        // Draw pulsating red fill
        g.setColor(new Color(redBrightness, 0, 0, 150)); // Semi-transparent Red
        g.fillRect(x, y, Cell.size, Cell.size);

        // Draw bold red border
        g.setColor(Color.RED);
        g.drawRect(x + 1, y + 1, Cell.size - 2, Cell.size - 2); 
        g.drawRect(x + 2, y + 2, Cell.size - 4, Cell.size - 4); 

        // 3. Draw the player
        player.paint(g);

        // 4. DRAW 'YOU WIN!' TEXT
        if (player.getCol() == goalC && player.getRow() == goalR) {
            g.setColor(new Color(0, 150, 0));
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.drawString("You Win!", 50, getHeight() / 2); 
        }

        // 5. DRAW INSTRUCTIONS (HUD)
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Goal: Reach the RED square (W/A/S/D)", 10, 20);
        
        // CRITICAL: Repaint must be called periodically for the pulse to work
        this.repaint();
    }
}