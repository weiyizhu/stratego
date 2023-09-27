package com.killerf1.backend;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

/**
 * This class provides information about the 13 types of units a Stratego game
 * has
 */
@Getter
public enum Unit {

    FLAG("Flag", 1, 0, new ZeroSpaceMoveStrategy()),
    SPY("Spy", 1, 1, new OneSpaceMoveStrategy()), // can beat marshal if initiates the move
    SCOUT("Scout", 8, 2, new InfiniteSpaceMoveStrategy()),
    MINER("Miner", 5, 3, new OneSpaceMoveStrategy()), // can beat bomb
    SERGEANT("Sergeant", 4, 4, new OneSpaceMoveStrategy()),
    LIEUTENANT("Lieutenant", 4, 5, new OneSpaceMoveStrategy()),
    CAPTAIN("Captain", 4, 6, new OneSpaceMoveStrategy()),
    MAJOR("Major", 3, 7, new OneSpaceMoveStrategy()),
    COLONEL("Colonel", 2, 8, new OneSpaceMoveStrategy()),
    GENERAL("General", 1, 9, new OneSpaceMoveStrategy()),
    MARSHAL("Marshal", 1, 10, new OneSpaceMoveStrategy()),
    BOMB("Bomb", 6, 11, new ZeroSpaceMoveStrategy()),
    RIVER("River", -1, 12, new ZeroSpaceMoveStrategy()),
    EMPTY("Empty", -1, 13, new ZeroSpaceMoveStrategy()),
    UNKNOWN_ENEMY("Unknown", -1, 14, new ZeroSpaceMoveStrategy());

    public final String name;
    public final int rank;
    public final MoveStrategy moveStrategy;
    public final int quantity;
    public final static Map<Integer, Integer> rankToQuantity = new HashMap<>() {
        {
            put(0, 1);
            put(1, 1);
            put(2, 8);
            put(3, 5);
            put(4, 4);
            put(5, 4);
            put(6, 4);
            put(7, 3);
            put(8, 2);
            put(9, 1);
            put(10, 1);
            put(11, 6);
        }
    };
    public final static Map<Integer, Unit> rankToUnit = new HashMap<>() {
        {
            put(0, Unit.FLAG);
            put(1, Unit.SPY);
            put(2, Unit.SCOUT);
            put(3, Unit.MINER);
            put(4, Unit.SERGEANT);
            put(5, Unit.LIEUTENANT);
            put(6, Unit.CAPTAIN);
            put(7, Unit.MAJOR);
            put(8, Unit.COLONEL);
            put(9, Unit.GENERAL);
            put(10, Unit.MARSHAL);
            put(11, Unit.BOMB);
        }
    };

    /**
     * The constructor of the Unit enum class
     * 
     * @param name         The name of the unit
     * @param quantity     The number of pieces one side has in a game
     * @param rank         A numerical value that is used as the comparator in most
     *                     cases to determine the winner between two units
     * @param moveStrategy The move strategy of the unit (i.e zero, one, infinite)
     */
    private Unit(String name, int quantity, int rank, MoveStrategy moveStrategy) {
        this.name = name;
        this.quantity = quantity;
        this.rank = rank;
        this.moveStrategy = moveStrategy;
    }

    /**
     * This static method determines the winning unit by comparing the ranks and
     * other special interactions
     * 
     * @return Thw winning unit
     */
    public static Unit decideWinningUnit(Unit unit1, Unit unit2) {
        return unit1;
    }
}
