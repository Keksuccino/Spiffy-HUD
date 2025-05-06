package de.keksuccino.spiffyhud.util.rendering;

public class ItemRenderingUtils {

    private static float itemOpacity = 1.0F;

    public static void setItemOpacity(float opacity) {
        itemOpacity = opacity;
    }

    public static float getItemOpacity() {
        return itemOpacity;
    }

    public static void resetItemOpacity() {
        setItemOpacity(1.0F);
    }

}
