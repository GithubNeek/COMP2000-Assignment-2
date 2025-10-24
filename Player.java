
public class Player {
    public int r;
    public int c;

    private int fuel; 
    public static final int STARTING_FUEL = 50;

    public Player(int startR, int startC) {
        this.r = startR;
        this.c = startC;
        this.fuel = STARTING_FUEL;
    }
    public boolean tryMoveTo(Cell targetCell) {
        if (!targetCell.isWalkable()) {
            return false;
        }

        int cost = targetCell.getMovementCost();
        
        if (this.fuel >= cost) {
            this.fuel -= cost;
            System.out.println("Moved to (" + targetCell.getLabel() + ", " + targetCell.getR() + "). Fuel remaining: " + this.fuel);
            return true;
        } else {
            System.out.println("Insufficient fuel. Cost: " + cost + ", Current Fuel: " + this.fuel);
            return false;
        }
    }

    public int getFuel() {
        return fuel;
    }
}