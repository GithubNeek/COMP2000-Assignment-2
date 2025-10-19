import java.util.Random;

public class Grid<T extends Cell> { 
    private final int rows;
    private final int cols;
    private final T[][] cells;
    
    private int goalRow;
    private int goalCol;
    
    // Minimum distance from (0, 0) to ensure a challenging path
    private static final int MIN_GOAL_DISTANCE = 5; 

    public Grid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.cells = (T[][]) new Cell[rows][cols]; 
        
        this.goalRow = rows - 1;
        this.goalCol = cols - 1;
    }

    public int rows() { return rows; }
    public int cols() { return cols; }

    public T get(int r, int c) {
        return cells[r][c];
    }

    public void set(int r, int c, T cell) {
        cells[r][c] = cell;
    }

    public boolean inBounds(int r, int c) {
        return r >= 0 && r < rows && c >= 0 && c < cols;
    }

    public void setGoal(int r, int c) {
        this.goalRow = r;
        this.goalCol = c;
    }

    public int getGoalRow() { return goalRow; }
    public int getGoalCol() { return goalCol; }
    
    // Helper method to calculate Manhattan distance (steps)
    private int getDistance(int r1, int c1, int r2, int c2) {
        return Math.abs(r1 - r2) + Math.abs(c1 - c2);
    }
    
    public void randomizeGoal() {
        Random rand = new Random();
        T targetCell;
        
        // CRITICAL FIX: Loop until a valid, far-away goal cell is found
        do {
            this.goalRow = rand.nextInt(rows);
            this.goalCol = rand.nextInt(cols);
            
            // Get the cell object (it should be set thanks to the Main.java fix)
            targetCell = get(goalRow, goalCol);
            
        } while ((this.goalRow == 0 && this.goalCol == 0) || // 1. Must not be the start (0,0)
                 (targetCell == null) || // 2. Must be a defined cell
                 (targetCell instanceof WaterCell) || // 3. Must NOT be a WaterCell (impassable)
                 (getDistance(0, 0, this.goalRow, this.goalCol) < MIN_GOAL_DISTANCE)); // 4. Must be far enough away

    }

    public void handleWeatherUpdate(WeatherData data) {
        int r = data.getY(); 
        int c = data.getX();
        
        if (inBounds(r, c)) {
            T cell = get(r, c);
            cell.applyWeather(data);
        }
    }
}