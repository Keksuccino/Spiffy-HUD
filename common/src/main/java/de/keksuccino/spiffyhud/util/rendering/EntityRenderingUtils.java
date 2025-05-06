package de.keksuccino.spiffyhud.util.rendering;

public class EntityRenderingUtils {

    private static float livingEntityOpacity = 1.0F;

    public static void setLivingEntityOpacity(float opacity) {
        livingEntityOpacity = opacity;
    }

    public static float getLivingEntityOpacity() {
        return livingEntityOpacity;
    }

    public static void resetLivingEntityOpacity() {
        setLivingEntityOpacity(1.0F);
    }

}
