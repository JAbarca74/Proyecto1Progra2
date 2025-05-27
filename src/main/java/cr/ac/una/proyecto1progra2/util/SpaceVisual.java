package cr.ac.una.proyecto1progra2.util;

import cr.ac.una.proyecto1progra2.DTO.SpacesDto;

public class SpaceVisual {

    private SpacesDto space;
    private int row;
    private int column;
    private int rowSpan;
    private int colSpan;

    public SpaceVisual(SpacesDto space, int row, int column, int rowSpan, int colSpan) {
        this.space = space;
        this.row = row;
        this.column = column;
        this.rowSpan = rowSpan;
        this.colSpan = colSpan;
    }

    public SpacesDto getSpace() {
        return space;
    }

    public void setSpace(SpacesDto space) {
        this.space = space;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRowSpan() {
        return rowSpan;
    }

    public void setRowSpan(int rowSpan) {
        this.rowSpan = rowSpan;
    }

    public int getColSpan() {
        return colSpan;
    }

    public void setColSpan(int colSpan) {
        this.colSpan = colSpan;
    }
}
