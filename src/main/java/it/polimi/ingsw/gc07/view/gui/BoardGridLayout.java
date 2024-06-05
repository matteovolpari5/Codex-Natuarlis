package it.polimi.ingsw.gc07.view.gui;

/**
 * Enumerated class containing relative coordinates for players' token colors on the board.
 */
public enum BoardGridLayout {
    RED_0(20, 1),
    GREEN_0(20,2),
    YELLOW_0(19, 1),
    BLUE_0(19,2),
    RED_1(20, 3),
    GREEN_1(20,4),
    YELLOW_1(19, 3),
    BLUE_1(19,4),
    RED_2(20, 5),
    GREEN_2(20,6),
    YELLOW_2(19, 3),
    BLUE_2(19,6),
    RED_3(18, 6),
    GREEN_3(18,7),
    YELLOW_3(17, 6),
    BLUE_3(17,7),
    RED_4(18, 4),
    GREEN_4(18,5),
    YELLOW_4(17, 4),
    BLUE_4(17,5),
    RED_5(18, 2),
    GREEN_5(18,3),
    YELLOW_5(17, 2),
    BLUE_5(17,3),
    RED_6(18, 0),
    GREEN_6(18,1),
    YELLOW_6(17, 0),
    BLUE_6(17,1),
    RED_7(15, 0),
    GREEN_7(15,1),
    YELLOW_7(14, 0),
    BLUE_7(14,1),
    RED_8(15, 2),
    GREEN_8(15,3),
    YELLOW_8(14, 2),
    BLUE_8(14,3),
    RED_9(15, 4),
    GREEN_9(15,5),
    YELLOW_9(14, 4),
    BLUE_9(14,5),
    RED_10(15, 6),
    GREEN_10(15,7),
    YELLOW_10(14, 6),
    BLUE_10(14,7),
    RED_11(13, 6),
    GREEN_11(13,7),
    YELLOW_11(12, 6),
    BLUE_11(12,7),
    RED_12(13, 4),
    GREEN_12(13,5),
    YELLOW_12(12, 4),
    BLUE_12(12,5),
    RED_13(13, 2),
    GREEN_13(13,3),
    YELLOW_13(12, 2),
    BLUE_13(12,3),
    RED_14(13, 0),
    GREEN_14(13,1),
    YELLOW_14(12, 0),
    BLUE_14(12,1),
    RED_15(11, 0),
    GREEN_15(11,1),
    YELLOW_15(10, 0),
    BLUE_15(10,1),
    RED_16(11, 2),
    GREEN_16(11,3),
    YELLOW_16(10, 2),
    BLUE_16(10,3),
    RED_17(11, 4),
    GREEN_17(11,5),
    YELLOW_17(10, 4),
    BLUE_17(10,5),
    RED_18(11, 6),
    GREEN_18(11,7),
    YELLOW_18(10, 6),
    BLUE_18(10,7),
    RED_19(8, 4),
    GREEN_19(8,5),
    YELLOW_19(7, 4),
    BLUE_19(7,5),
    RED_20(7, 3),
    GREEN_20(7,4),
    YELLOW_20(6, 3),
    BLUE_20(6,4),
    RED_21(8, 0),
    GREEN_21(8,1),
    YELLOW_21(7, 0),
    BLUE_21(7,1),
    RED_22(6, 0),
    GREEN_22(6,1),
    YELLOW_22(5, 0),
    BLUE_22(5,1),
    RED_23(4, 0),
    GREEN_23(4,1),
    YELLOW_23(3, 0),
    BLUE_23(3,1),
    RED_24(2, 1),
    GREEN_24(2,2),
    YELLOW_24(1, 1),
    BLUE_24(1,2),
    RED_25(1, 3),
    GREEN_25(1,4),
    YELLOW_25(0, 3),
    BLUE_25(0,4),
    RED_26(2, 5),
    GREEN_26(2,6),
    YELLOW_26(1, 5),
    BLUE_26(1,6),
    RED_27(4, 6),
    GREEN_27(4,7),
    YELLOW_27(3, 6),
    BLUE_27(3,7),
    RED_28(8, 6),
    GREEN_28(8,7),
    YELLOW_28(7, 6),
    BLUE_28(7,7),
    RED_29(4, 3),
    GREEN_29(4,4),
    YELLOW_29(3, 3),
    BLUE_29(3,4);

    /**
     * Integer value representing x coordinate in ScoreBoard grid.
     */
    private final int x;
    /**
     * Integer representing y coordinate in ScoreBoard grid.
     */
    private final int y;

    /**
     * Constructor of BoardGridLayout.
     * @param x x coordinate
     * @param y y coordinate
     */
    BoardGridLayout(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Getter for x coordinate.
     * @return value for x coordinate
     */
    public int getX() {
        return this.x;
    }

    /**
     * Getter for y coordinate.
     * @return value for y coordinate
     */
    public int getY(){
        return this.y;
    }
}
