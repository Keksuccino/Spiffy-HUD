package de.keksuccino.spiffyhud.util.rendering;

import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

public class ElementMobilizer {

    /**
     * Moves the render body to the given X and Y coordinates after correcting the body's position to X=0 and Y=0.
     *
     * @param graphics The {@link GuiGraphics} instance for rendering.
     * @param offsetCorrectTo0X The X offset to move the render body to X=0.
     * @param offsetCorrectTo0Y The Y offset to move the render body to Y=0.
     * @param posX The final X position of the render body.
     * @param posY The final Y position of the render body.
     * @param renderBody The render body.
     */
    public static void mobilize(@NotNull GuiGraphics graphics, int offsetCorrectTo0X, int offsetCorrectTo0Y, int posX, int posY, @NotNull Runnable renderBody) {
        graphics.pose().pushPose();
        graphics.pose().translate(offsetCorrectTo0X + posX, offsetCorrectTo0Y + posY, 0);
        renderBody.run();
        graphics.pose().popPose();
    }

}
