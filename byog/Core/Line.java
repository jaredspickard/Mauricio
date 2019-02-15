package byog.Core;

import java.io.Serializable;

public class Line implements Serializable {

    //private double length;
    private Room room1;
    private Room room2;
    private int xComponent;
    private int yComponent;
    private int xStart;
    private int yStart;

    public Line(Room one, Room two) {
        room1 = one;
        room2 = two;
        xComponent = room2.getXCenter() - room1.getXCenter();
        yComponent = room2.getYCenter() - room1.getYCenter();
        xStart = room1.getXCenter();
        yStart = room1.getYCenter();
    }

    /**
     * Returns whether or not two lines are the same.
     * @param other the other line to be compared
     * @return true if the lines are the same, false if they are not
     */
    public boolean equals(Line other) {
        return checkRooms(other.getRoom1(), other.getRoom2());
    }

    /**
     * Returns whether or not this line is equal to the line between two rooms.
     * @param one the first room
     * @param two the second room
     * @return true if the lines are the same, false if not
     */
    private boolean checkRooms(Room one, Room two) {
        return ((room1 == one || room1 == two) && (room2 == one || room1 == two));
    }

    /**
     * Returns room1.
     * @return room1
     */
    public Room getRoom1() {
        return room1;
    }

    /**
     * Returns room2.
     * @return room2.
     */
    public Room getRoom2() {
        return room2;
    }

    /**
     * Returns the x component of the line.
     * @return x component of the line
     */
    public int getXComponent() {
        return xComponent;
    }

    /**
     * Returns the y component of the line.
     * @return y component of the line
     */
    public int getYComponent() {
        return yComponent;
    }

    /**
     * Returns the x coordinate of the start location.
     * @return x coordinate of the start location
     */
    public int getXStart() {
        return xStart;
    }

    /**
     * Returns the y coordinate of the start location.
     * @return y coordinate of the start location
     */
    public int getYStart() {
        return yStart;
    }
}
