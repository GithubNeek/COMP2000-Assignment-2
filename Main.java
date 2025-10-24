// Main.java (FINAL FIX - Connects ALL Observers)

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Main {
    
    private static JFrame mainFrame;

    private static char ColToLabel(int col) { 
        return (char) ('A' + col);
    }
    
    public static void restartGame() {
        if (mainFrame != null) {
            mainFrame.dispose();
        }
        main(new String[]{});
    }

    public static void main(String[] args) {
        
        Grid<Cell> grid = new Grid<Cell>(); 
        
        int rows = grid.rows(), cols = grid.cols();
        Random rnd = new Random();

        // --- ASSIGNMENT 2 SYSTEM (Numerical Data) ---
        WeatherClient weatherClient = new WeatherClient();
        // Grid uses this for movement cost/decorator changes
        weatherClient.addObserver(grid);
        
        // --- RESTORED OLD SYSTEM (Random Events for Console/Tint) ---
        WeatherStream randomWeatherStream = new WeatherStream();
        
        // --- Grid Initialization ---
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int x = c * Cell.size;
                int y = r * Cell.size;
                int roll = rnd.nextInt(100); 

                if (roll < 15) {
                    grid.setCell(r, c, new WaterCell(ColToLabel(c), r, c, x, y));
                } else if (roll < 35) {
                    grid.setCell(r, c, new SandCell(ColToLabel(c), r, c, x, y));
                } else {
                    grid.setCell(r, c, new GrassCell(ColToLabel(c), r, c, x, y));
                }
            }
        }

        grid.setCell(0, 0, new GrassCell(ColToLabel(0), 0, 0, 0, 0));
        
        int lastRow = rows - 1;
        int lastCol = cols - 1;
        int lastX = lastCol * Cell.size;
        int lastY = lastRow * Cell.size;
        
        grid.setCell(lastRow, lastCol,
            new GrassCell(ColToLabel(lastCol), lastRow, lastCol, lastX, lastY));

        Player player = new Player(0, 0);

        JFrame f = new JFrame("Grid Game - Reach the Goal!");
        mainFrame = f; 
        
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setResizable(false);

        GamePanel panel = new GamePanel(grid, player);
        
        // FIX 1: Add GamePanel as observer to the new client (for general repaint)
        weatherClient.addObserver(panel); 
        
        // FIX 2: ADD GAMEPANEL AS OBSERVER TO THE RESTORED STREAM (for screen tint updates)
        randomWeatherStream.addObserver(panel);
        
        // Start BOTH threads
        new Thread(weatherClient).start(); 
        new Thread(randomWeatherStream).start(); 
        
        f.add(panel, BorderLayout.CENTER);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        panel.setFocusable(true);
        panel.requestFocusInWindow();
        panel.requestFocus();
    }
}