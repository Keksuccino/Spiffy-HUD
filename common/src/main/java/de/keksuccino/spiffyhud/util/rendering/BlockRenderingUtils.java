package de.keksuccino.spiffyhud.util.rendering;

public class BlockRenderingUtils {

    private static float blockOpacity = 1.0F;

    public static void setBlockOpacity(float opacity) {
        blockOpacity = opacity;
    }

    public static float getBlockOpacity() {
        return blockOpacity;
    }

    public static void resetBlockOpacity() {
        setBlockOpacity(1.0F);
    }

}
