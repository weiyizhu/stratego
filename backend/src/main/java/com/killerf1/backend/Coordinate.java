package com.killerf1.backend;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Coordinate {
    public int row;
    public int col;

    public static boolean isValidCoordinate(Coordinate coordinate) {
        if (coordinate.row < 0 || coordinate.row > 9 || coordinate.col < 0 || coordinate.col > 9) {
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        Coordinate otherCoordinate = (Coordinate) o;
        if (this.row == otherCoordinate.row && this.col == otherCoordinate.col) {
            return true;
        }
        return false;
    }
}
