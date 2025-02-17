package de.keksuccino.spiffyhud.util;

@SuppressWarnings("unused")
public class SizeAndPositionRecorder {

    protected int startX = -10000000;
    protected int startY = -10000000;
    protected int endX = -10000000;
    protected int endY = -10000000;
    protected int widthModifier = 0;
    protected int heightModifier = 0;
    protected int xModifier = 0;
    protected int yModifier = 0;
    protected boolean updatedX = false;
    protected boolean updatedY = false;

    public void updateX(int x) {
        this.updateStartX(x);
        this.updateEndX(x);
        this.updatedX = true;
    }

    protected void updateStartX(int x) {
        if (startX == -10000000) {
            startX = x;
        } else if (x < startX) {
            startX = x;
        }
    }

    protected void updateEndX(int x) {
        if (endX == -10000000) {
            endX = x;
        } else if (x > endX) {
            endX = x;
        }
    }

    public void updateY(int y) {
        this.updateStartY(y);
        this.updateEndY(y);
        this.updatedY = true;
    }

    protected void updateStartY(int y) {
        if (startY == -10000000) {
            startY = y;
        } else if (y < startY) {
            startY = y;
        }
    }

    protected void updateEndY(int y) {
        if (endY == -10000000) {
            endY = y;
        } else if (y > endY) {
            endY = y;
        }
    }

    public boolean isUpdated() {
        return this.updatedX && this.updatedY;
    }

    public int getX() {
        if (!this.isUpdated()) return 0;
        return Math.min(this.startX, this.endX) + this.xModifier;
    }

    public int getY() {
        if (!this.isUpdated()) return 0;
        return Math.min(this.startY, this.endY) + this.yModifier;
    }

    public int getWidth() {
        if (!this.isUpdated()) return 0;
        return (Math.max(startX, endX) - Math.min(startX, endX)) + this.widthModifier;
    }

    public int getHeight() {
        if (!this.isUpdated()) return 0;
        return (Math.max(startY, endY) - Math.min(startY, endY)) + this.heightModifier;
    }

    /**
     * Modifies the final width and returns it.
     */
    public int setWidthOffset(int offset) {
        this.widthModifier = offset;
        return this.getWidth();
    }

    /**
     * Modifies the final height and returns it.
     */
    public int setHeightOffset(int offset) {
        this.heightModifier = offset;
        return this.getHeight();
    }

    /**
     * Modifies the final X coordinate and returns it.
     */
    public int setXOffset(int offset) {
        this.xModifier = offset;
        return this.getX();
    }

    /**
     * Modifies the final Y coordinate and returns it.
     */
    public int setYOffset(int offset) {
        this.yModifier = offset;
        return this.getY();
    }

}
