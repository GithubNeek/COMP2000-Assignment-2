
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

    
    public void updateWeather(List<WeatherDataPoint> weatherData) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (cells[r][c] instanceof CellDecorator) {
                    cells[r][c] = ((CellDecorator) cells[r][c]).getDecoratedCell();
                }
            }
        }
        
        weatherData.stream().forEach(data -> {
            int targetC = data.getX() + 10;
            int targetR = data.getY() + 10;
            
            if (inBounds(targetR, targetC)) {
                Cell currentCell = cells[targetR][targetC];
                
                if (data.getAttribute().equals("rain") && data.getValue() > 0.7f) {
                    
                    if (!(currentCell instanceof SnowDecorator)) {
                        cells[targetR][targetC] = new SnowDecorator(currentCell);
                    }
                } 
                else if (data.getAttribute().equals("windx") || data.getAttribute().equals("windy")) {
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
