package de.keksuccino.spiffyhud.util.player;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;

public class PlayerPositionObserver {

    protected static double currentPositionDeltaX = 0.0;
    protected static double currentPositionDeltaY = 0.0;
    protected static double currentPositionDeltaZ = 0.0;
    protected static double lastPositionX = 0.0;
    protected static double lastPositionY = 0.0;
    protected static double lastPositionZ = 0.0;

    public static void tick() {
        Entity e = Minecraft.getInstance().getCameraEntity();
        if (e != null) {
            // Retrieve current positions from the entity
            double currentX = e.getX();
            double currentY = e.getY();
            double currentZ = e.getZ();

            // Calculate the delta (difference) between the current and last positions
            currentPositionDeltaX = currentX - lastPositionX;
            currentPositionDeltaY = currentY - lastPositionY;
            currentPositionDeltaZ = currentZ - lastPositionZ;

            // Update the last recorded position values for the next tick
            lastPositionX = currentX;
            lastPositionY = currentY;
            lastPositionZ = currentZ;
        } else {
            // Reset values if no entity is available
            currentPositionDeltaX = 0.0;
            currentPositionDeltaY = 0.0;
            currentPositionDeltaZ = 0.0;
            lastPositionX = 0.0;
            lastPositionY = 0.0;
            lastPositionZ = 0.0;
        }
    }

    public static double getCurrentPositionDeltaX() {
        return currentPositionDeltaX;
    }

    public static double getCurrentPositionDeltaY() {
        return currentPositionDeltaY;
    }

    public static double getCurrentPositionDeltaZ() {
        return currentPositionDeltaZ;
    }

}
