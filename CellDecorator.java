import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public abstract class CellDecorator extends Cell {

    protected Cell decoratedCell;
    
    public CellDecorator(Cell decoratedCell) {
        super(decoratedCell.getLabel(), 
              decoratedCell.getR(), 
              decoratedCell.getC(), 
              decoratedCell.getCellX(), 
              decoratedCell.getCellY());
              
        this.decoratedCell = decoratedCell;
    }
    
    public Cell getDecoratedCell() {
        return decoratedCell;
    }
    
    @Override
    public boolean contains(Point p) { return decoratedCell.contains(p); }

    @Override
    public Color getColor() { return decoratedCell.getColor(); }

    @Override
    public boolean isWalkable() { return decoratedCell.isWalkable(); }

    @Override
    public int getMovementCost() { return decoratedCell.getMovementCost(); }
    
    @Override
    public void paint(Graphics g, Point mousePos) { decoratedCell.paint(g, mousePos); }
}