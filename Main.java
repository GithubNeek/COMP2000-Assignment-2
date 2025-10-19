import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 1. JFrame and container setup
            JFrame frame = new JFrame("Grid Game - Reach the Goal!");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            
            int rows = 10;
            int cols = 10;
            
            // 2. Grid Creation
            Grid<Cell> grid = new Grid<>(rows, cols);

            // 3. Grid Filling Loop (MUST HAPPEN FIRST)
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    
                    // Skip the starting cell (0, 0)
                    if (r == 0 && c == 0) {
                        grid.set(r, c, new GrassCell((char)('A' + c), r, c * Cell.size, r * Cell.size));
                        continue;
                    }

                    double type = Math.random();

                    // Maze obstacle creation (25% Sand, 20% Water, 55% Grass)
                    if (type < 0.25) { 
                        grid.set(r, c, new SandCell((char)('A' + c), r, c * Cell.size, r * Cell.size));
                    } else if (type < 0.45) { 
                        grid.set(r, c, new WaterCell((char)('A' + c), r, c * Cell.size, r * Cell.size));
                    } else { 
                        grid.set(r, c, new GrassCell((char)('A' + c), r, c * Cell.size, r * Cell.size));
                    }
                }
            }
            
            // 4. Randomize the goal AFTER the grid is filled.
            grid.randomizeGoal(); 
            
            // --- CRITICAL FIX: Add Wall around the Goal ---
            int goalR = grid.getGoalRow();
            int goalC = grid.getGoalCol();
            
            // Define the ONE open entrance: We'll force the cell directly above the goal to be the entrance
            // For example, if goal is at (8, 8), the entrance is forced to be (7, 8).
            int entranceR = goalR - 1;
            int entranceC = goalC; 
            
            // Check all 4 neighbours of the goal
            int[] dr = {-1, 1, 0, 0}; // Change in row (Up, Down, Left, Right)
            int[] dc = {0, 0, -1, 1}; // Change in col
            
            for (int i = 0; i < 4; i++) {
                int neighborR = goalR + dr[i];
                int neighborC = goalC + dc[i];
                
                // If the neighbor is in bounds and is NOT the designated entrance...
                if (grid.inBounds(neighborR, neighborC) && (neighborR != entranceR || neighborC != entranceC)) {
                    // ...force it to be an impassable WaterCell (if it's not the goal cell itself)
                    if (grid.get(neighborR, neighborC) != grid.get(goalR, goalC)) {
                         grid.set(neighborR, neighborC, new WaterCell((char)('A' + neighborC), neighborR, neighborC * Cell.size, neighborR * Cell.size));
                    }
                }
            }
            // ---------------------------------------------

            // 5. Player and GamePanel setup
            Player player = new Player(0, 0); 
            GamePanel gamePanel = new GamePanel(grid, player);
            
            frame.add(gamePanel);
            
            // 6. Start the weather feed
            Thread weatherFeed = new Thread(new WeatherFeedReader(grid));
            weatherFeed.start();
            
            // 7. Frame packing and visibility
            frame.pack();
            frame.setLocationRelativeTo(null); 
            frame.setVisible(true);
            
            gamePanel.requestFocusInWindow();
        });
    }
}