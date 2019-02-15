package byog.Core;

import java.util.ArrayList;

public class Room {

    private int xCenter;
    private int yCenter;
    private int width;
    private int height;
    private int[] position = new int[2];
    //the lines ArrayList is a list of rooms that the current room shares lines with
    private ArrayList<Room> lines;

    public Room(int xPos, int yPos, int height, int width) {
        position[0] = xPos;
        position[1] = yPos;
        this.width = width;
        this.height = height;
        xCenter = (xPos + xPos + width) / 2;
        yCenter = (yPos + yPos + height) / 2;
        lines = new ArrayList<>();

    }

    /**
     * Returns the distance between this room and the other room.
     * @param other the other room from which we're calculating the distance
     * @return a double representing the distance between the two rooms
     */
    public double calculateDistance(Room other) {
        return Math.sqrt((xCenter - other.getXCenter()) * (xCenter - other.getXCenter())
                + (yCenter - other.getYCenter()) * (yCenter - other.getYCenter()));
    }

    /**
     * Returns the x coordinate of the center of the room.
     * @return int representing the x coordinate of a room's center
     */
    public int getXCenter() {
        return xCenter;
    }

    /**
     * Returns the y coordinate of the center of the room.
     * @return int representing the y coordinate of a room's center
     */
    public int getYCenter() {
        return yCenter;
    }

    public ArrayList<Room> getLines() {
        return lines;
    }

    /**
     * Adds a single line from this room to the other room.
     * @param other the other room to which the line points
     */
    public void addLine(Room other) {
        lines.add(other);
    }

    /**
     * Deletes the line from this room to the other room.
     * @param other the other room to which the line points
     */
    public void deleteLine(Room other) {
        if (!lines.contains(other)) {
            return;
        }
        int index = lines.indexOf(other);
        lines.remove(index);
    }

}
