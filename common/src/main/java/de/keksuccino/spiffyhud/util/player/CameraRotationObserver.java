package de.keksuccino.spiffyhud.util.player;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;

public class CameraRotationObserver {

    protected static float currentRotationDeltaX = 0.0f;
    protected static float currentRotationDeltaY = 0.0f;
    protected static float lastRotationX = 0.0f;
    protected static float lastRotationY = 0.0f;

    public static void tick() {
        Entity e = Minecraft.getInstance().getCameraEntity();
        if (e != null) {
            // Get the current rotations from the camera's entity
            float currentXRot = e.getXRot(); // camera X rotation (pitch)
            float currentYRot = e.getYRot(); // camera Y rotation (yaw)

            // Calculate the delta (difference) between the current and last rotations
            currentRotationDeltaX = currentXRot - lastRotationX;
            currentRotationDeltaY = currentYRot - lastRotationY;

            // Update the last recorded rotation values
            lastRotationX = currentXRot;
            lastRotationY = currentYRot;
        } else {
            // Reset values if there is no camera entity available
            lastRotationX = 0.0f;
            lastRotationY = 0.0f;
            currentRotationDeltaX = 0.0f;
            currentRotationDeltaY = 0.0f;
        }
    }

    public static float getCurrentRotationDeltaX() {
        return currentRotationDeltaX;
    }

    public static float getCurrentRotationDeltaY() {
        return currentRotationDeltaY;
    }

}
