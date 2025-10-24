// GamePanel.java (REPLACE content - FINAL)

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Optional;
import javax.swing.*; 

public class GamePanel extends JPanel implements KeyListener, MouseMotionListener, WeatherObserver {
    Grid<Cell> grid;
    Player player;
    Point mousePos = new Point(0, 0);
    boolean won = false;
    boolean gameOver = false; 
    
    // FIX 1: New field to track the simple weather state for screen tint
    private WeatherEvent currentLegacyWeather = WeatherEvent.SUNNY; 

    public GamePanel(Grid<Cell> g, Player p) {
        grid = g;
        player = p;

        setPreferredSize(new Dimension(grid.cols() * Cell.size, grid.rows() * Cell.size));
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        addKeyListener(this);
        addMouseMotionListener(this);
    }
    
    // Assignment 2 Observer Method (Updates Grid/Repaints)
    @Override
    public void updateWeather(List<WeatherDataPoint> weatherData) {
        repaint(); 
    }

    // FIX 2: Legacy Observer Method (Updates the tint state)
    @Override
    public void updateLegacyWeather(WeatherEvent event) {
        this.currentLegacyWeather = event;
        repaint();
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        grid.paint(g, mousePos); 
        
        // --- HUD and Player/Goal Drawing (Kept the same) ---
        // ... (This section remains unchanged) ...

        // HUD TEXT (Top-Left)
        g.setFont(new Font("SansSerif", Font.BOLD, 14));
        g.setColor(Color.BLACK);
        g.drawString("Fuel: " + player.getFuel(), 8, 32); 

        // Draw GOAL and PLAYER
        int gr = grid.rows() - 1, gc = grid.cols() - 1;
        int gx = gc * Cell.size, gy = gr * Cell.size;
        g.setColor(Color.YELLOW);
        g.drawRect(gx + 2, gy + 2, Cell.size - 4, Cell.size - 4);
        g.drawRect(gx + 4, gy + 4, Cell.size - 8, Cell.size - 8);

        int px = player.c * Cell.size, py = player.r * Cell.size;
        int pad = 6;
        g.setColor(Color.RED);
        g.fillOval(px + pad, py + pad, Cell.size - 2 * pad, Cell.size - 2 * pad);
        g.setColor(Color.BLACK);
        g.drawOval(px + pad, py + pad, Cell.size - 2 * pad, Cell.size - 2 * pad);

        // INSTRUCTIONS (Bottom Center)
        String instructionText = "Move: W/A/S/D | Goal: Reach the yellow square!";
        int instructionY = grid.rows() * Cell.size - 8;
        Font instructionFont = new Font("SansSerif", Font.BOLD, 16);
        g.setFont(instructionFont);
        
        FontMetrics fm = g.getFontMetrics(instructionFont);
        int textWidth = fm.stringWidth(instructionText);
        int instructionX = (grid.cols() * Cell.size - textWidth) / 2;

        g.setColor(Color.BLACK);
        g.drawString(instructionText, instructionX + 1, instructionY + 1);
        
        g.setColor(Color.WHITE);
        g.drawString(instructionText, instructionX, instructionY);


        // --- FIX 3: Draw the Screen Tint Overlay ---
        Color overlayColor = null;
        int width = grid.cols() * Cell.size;
        int height = grid.rows() * Cell.size;

        if (currentLegacyWeather == WeatherEvent.SUNNY) {
            // Bright, warm tint: light yellow/red with low transparency (alpha=60)
            overlayColor = new Color(255, 180, 0, 60); 
        } else if (currentLegacyWeather == WeatherEvent.SNOW) {
            // Cold, white tint: pure white with medium transparency (alpha=90)
            overlayColor = new Color(255, 255, 255, 90);
        } else if (currentLegacyWeather == WeatherEvent.RAIN) {
            // Cool, gloomy tint: dark blue/gray with medium transparency (alpha=90)
            overlayColor = new Color(50, 50, 100, 90); 
        }

        if (overlayColor != null) {
            g.setColor(overlayColor);
            g.fillRect(0, 0, width, height);
        }
        // ---------------------------------------------

        // --- WIN/LOSE Status (Drawn LAST to appear on top of tint) ---
        if (won) {
            g.setFont(new Font("SansSerif", Font.BOLD, 24));
            g.setColor(new Color(0, 150, 0));
            g.drawString("You Win!", 20, 50);
        }
        
        if (gameOver) {
            g.setFont(new Font("SansSerif", Font.BOLD, 24));
            g.setColor(new Color(150, 0, 0));
            g.drawString("Game Over: Out of Fuel!", 20, 70);
        }
    }
    
    // ... (rest of checkGameOver, keyPressed, etc. remains unchanged) ...

    private void checkGameOver() {
        if (player.getFuel() <= 0 && !won) {
            gameOver = true;
            repaint();
            
            int choice = JOptionPane.showConfirmDialog(
                this, 
                "You ran out of fuel! The game is over. Try again?", 
                "Game Over", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE
            );

            if (choice == JOptionPane.YES_OPTION) {
                Main.restartGame(); 
            } else {
                System.exit(0);
            }
        }
    }

    public void keyPressed(KeyEvent e) {
        if (won || gameOver) return; 

        char ch = Character.toUpperCase(e.getKeyChar());
        int dr = 0, dc = 0;
        if (ch == 'W') dr = -1;
        else if (ch == 'S') dr = 1;
        else if (ch == 'A') dc = -1;
        else if (ch == 'D') dc = 1;
        else return;

        int nr = player.r + dr, nc = player.c + dc;
        if (!grid.inBounds(nr, nc)) return;

        Optional<Cell> targetCell = grid.getCell(nr, nc); 
        
        if (targetCell.isPresent()) {
            Cell target = targetCell.get();
            
            if (player.tryMoveTo(target)) {
                player.r = nr;
                player.c = nc;
                repaint();

                if (player.r == grid.rows() - 1 && player.c == grid.cols() - 1) {
                    won = true;
                    repaint();
                    JOptionPane.showMessageDialog(this, "You reached the goal!");
                }
                
                checkGameOver();
                
            } else {
                Toolkit.getDefaultToolkit().beep();
                checkGameOver(); 
            }
        }
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
    public void mouseMoved(MouseEvent e) { mousePos = e.getPoint(); repaint(); }
    public void mouseDragged(MouseEvent e) { mouseMoved(e); }
}