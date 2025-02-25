package config;

import geometry.IntCoordinates;
import gui.AppStateMachine.PlayingState;

public class MazeConfig {
    private static MazeLoad mazeLoad = new MazeLoad();

    public MazeConfig(Cell[][] grid, IntCoordinates pacManPos, IntCoordinates blinkyPos, IntCoordinates pinkyPos,
                      IntCoordinates inkyPos, IntCoordinates clydePos ,IntCoordinates exit_pos) {
        this.grid = new Cell[grid.length][grid[0].length];
        for (int i = 0; i < getHeight(); i++) {
            if (getWidth() >= 0)
                System.arraycopy(grid[i], 0, this.grid[i], 0, getHeight());
        }
        this.pacManPos = pacManPos;
        this.blinkyPos = blinkyPos;
        this.inkyPos = inkyPos;
        this.pinkyPos = pinkyPos;
        this.clydePos = clydePos;
        this.exit_pos = exit_pos;
    }

    private final Cell[][] grid;
    private final IntCoordinates pacManPos, blinkyPos, pinkyPos, inkyPos, clydePos, exit_pos;

    public IntCoordinates getPacManPos() {
        return pacManPos;
    }

    public IntCoordinates getBlinkyPos() {
        return blinkyPos;
    }

    public IntCoordinates getPinkyPos() {
        return pinkyPos;
    }

    public IntCoordinates getInkyPos() {
        return inkyPos;
    }

    public IntCoordinates getClydePos() {
        return clydePos;
    }

    public IntCoordinates getExit_pos() {
        return exit_pos;
    }

    public int getWidth() {
        return grid[0].length;
    }

    public int getHeight() {
        return grid.length;
    }

    public Cell getCell(IntCoordinates pos) {
        return grid[Math.floorMod(pos.y(), getHeight())][Math.floorMod(pos.x(), getWidth())];
    }

    public static MazeConfig makeMaze(int level) {
        switch (level) {
            case 1:
                return makeLevel1();
            case 2:
                return makeLevel1();
            case 3:
                return makeLevel1();
            case 4:
                return makeLevel1();
            case 5:
                return makeLevel1();
            case 6:
                return makeLevel1();
            case 7:
                return makeLevel1();
            case 8:
                return makeLevel1();
            case 9:
                return makeLevel1();
            case 10:
                return makeLevel1();
            default:
                return makeLevel1();
        }
    }

    public static MazeConfig makeLevel1() {
        return new MazeConfig(mazeLoad.make(1),
                new IntCoordinates(7, 7),
                new IntCoordinates(6, 6),
                new IntCoordinates(8, 5),
                new IntCoordinates(6, 5),
                new IntCoordinates(8, 6),
                new IntCoordinates(7, 4));
    }
}
