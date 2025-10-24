// Grid.java (REPLACE content)

import java.awt.Graphics;
import java.awt.Point;
import java.util.List;
import java.util.Optional;

public class Grid<T extends Cell> implements WeatherObserver { 
    
    private final int rows = 20; 
    private final int cols = 20; 
    private Cell[][] cells;
    
    private char colToLabel(int col) { 
        return (char) (col + Character.valueOf('A'));
    }

    public Grid() {
        this.cells = new Cell[rows][cols];
        
        for(int r = 0; r < rows; r++) {
            for(int c = 0; c < cols; c++) {
                cells[r][c] = new GrassCell(colToLabel(c), r, c, Cell.size * c, Cell.size * r);
            }
        }
    }
    
    public int rows() { return rows; }
    public int cols() { return cols; }

    public void setCell(int r, int c, Cell cell) {
        cells[r][c] = cell;
    }

    public Optional<Cell> getCell(int r, int c) {
        if (inBounds(r, c)) {
            return Optional.of(cells[r][c]);
        }
        return Optional.empty();
    }
    
    public boolean inBounds(int r, int c) {
        return r >= 0 && r < rows && c >= 0 && c < cols;
    }

    /**
     * FIX: Apply WindDecorator logic and simplify decorator removal.
     * * INTERPRETATION:
     * 1. Rain > 0.7: Apply SnowDecorator (heavy precipitation/slush).
     * 2. WindX or WindY > 0.8: Apply WindDecorator (high wind speed).
     * 3. If a condition is no longer met, revert the cell to its base type.
     */
    @Override
    public void updateWeather(List<WeatherDataPoint> weatherData) {
        // Clear all current weather decorators before applying new ones
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (cells[r][c] instanceof CellDecorator) {
                    // Revert to the base cell (essential for proper weather change)
                    cells[r][c] = ((CellDecorator) cells[r][c]).getDecoratedCell();
                }
            }
        }
        
        // Use streams to process the incoming data points (Lambda/Stream use case)
        weatherData.stream().forEach(data -> {
            // Simplified Mapping: Add 10 (center to corner) to x/y, then truncate to int
            int targetC = data.getX() + 10;
            int targetR = data.getY() + 10;
            
            if (inBounds(targetR, targetC)) {
                Cell currentCell = cells[targetR][targetC];
                
                // --- Apply New Decorator Logic ---
                if (data.getAttribute().equals("rain") && data.getValue() > 0.7f) {
                    // Check to prevent wrapping a decorator around another decorator 
                    // that was just applied in the same stream (though stream order is undefined)
                    if (!(currentCell instanceof SnowDecorator)) {
                        cells[targetR][targetC] = new SnowDecorator(currentCell);
                    }
                } 
                else if (data.getAttribute().equals("windx") || data.getAttribute().equals("windy")) {
                    // Check if *either* wind component is strong enough
                    if (data.getValue() > 0.8f) {
                        if (!(currentCell instanceof WindDecorator)) {
                            cells[targetR][targetC] = new WindDecorator(currentCell);
                        }
                    }
                }
            }
        });
    }

    public void paint(Graphics g, Point mousePos) {
        for(int r = 0; r < rows; r++) {
            for(int c = 0; c < cols; c++) {
                cells[r][c].paint(g, mousePos);
            }
        }
    }
}