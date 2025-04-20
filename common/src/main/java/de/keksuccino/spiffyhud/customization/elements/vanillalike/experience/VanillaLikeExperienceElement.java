package de.keksuccino.spiffyhud.customization.elements.vanillalike.experience;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class VanillaLikeExperienceElement extends AbstractElement {

    private static final Logger LOGGER = LogManager.getLogger();

    // Sprite resources for the experience bar in 1.21.1
    private static final ResourceLocation EXPERIENCE_BAR_BACKGROUND_SPRITE = ResourceLocation.withDefaultNamespace("hud/experience_bar_background");
    private static final ResourceLocation EXPERIENCE_BAR_PROGRESS_SPRITE = ResourceLocation.withDefaultNamespace("hud/experience_bar_progress");

    private static final int BAR_WIDTH = 182;
    private static final int BAR_HEIGHT = 5;

    private final Minecraft minecraft = Minecraft.getInstance();

    public VanillaLikeExperienceElement(@NotNull ElementBuilder<?, ?> builder) {
        super(builder);
    }

    /**
     * Renders the experience bar within this element's bounds.
     *
     * @param graphics The graphics context.
     * @param mouseX   The current mouse X-coordinate.
     * @param mouseY   The current mouse Y-coordinate.
     * @param partial  Partial ticks.
     */
    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {

        // Do nothing if the player or level is missing.
        if (this.minecraft.player == null || this.minecraft.level == null) {
            return;
        }

        // Get the absolute position and dimensions of this element.
        int elementX = this.getAbsoluteX();
        int elementY = this.getAbsoluteY();
        int elementWidth = this.getAbsoluteWidth();
        int elementHeight = this.getAbsoluteHeight();

        // Prepare the render state.
        RenderSystem.enableBlend();
        RenderingUtils.resetShaderColor(graphics);

        // Render the experience bar using the element's own bounds.
        renderExperienceBar(graphics, elementX, elementY, elementWidth, elementHeight);

        RenderingUtils.resetShaderColor(graphics);

    }

    /**
     * Renders the background bar, the filled portion according to the player’s experience progress,
     * and overlays the player's current experience level as text.
     *
     * @param graphics The graphics context to draw on.
     * @param x        The x-coordinate (absolute) of the element.
     * @param y        The y-coordinate (absolute) of the element.
     * @param width    The width of the element (should be 182 pixels).
     * @param height   The height of the element (should be 5 pixels).
     */
    public void renderExperienceBar(GuiGraphics graphics, int x, int y, int width, int height) {

        var player = this.minecraft.player;
        if (player == null) return;

        // Enable blending and set the shader color with the desired opacity.
        RenderSystem.enableBlend();
        graphics.setColor(1.0f, 1.0f, 1.0f, this.opacity);

        // Only draw the bar if the player requires XP for the next level.
        int xpNeeded = player.getXpNeededForNextLevel();
        if ((xpNeeded > 0) || isEditor()) {
            // Calculate the width (in pixels) of the filled portion.
            // (The original calculation used a scale of 183.0f for a bar that is 182 pixels wide.)
            int filledBarWidth = (int) (player.experienceProgress * 183.0f);
            if (isEditor()) filledBarWidth = BAR_WIDTH / 2;

            // Draw the empty (background) experience bar.
            graphics.blitSprite(EXPERIENCE_BAR_BACKGROUND_SPRITE, x, y, width, height);

            // Draw the filled part of the bar if any XP has been gained.
            if (filledBarWidth > 0) {
                // In 1.21.1, we use the progress sprite with specified dimensions
                graphics.blitSprite(EXPERIENCE_BAR_PROGRESS_SPRITE, width, height, 0, 0, x, y, filledBarWidth, height);
            }
        }

        // Render the experience level number if the level is greater than zero.
        if ((player.experienceLevel > 0) || isEditor()) {

            String levelText = String.valueOf(player.experienceLevel);
            if (isEditor()) levelText = "42";

            // Center the level text horizontally within the element.
            int textWidth = this.getFont().width(levelText);
            int textX = x + (width - textWidth) / 2;

            // Vanilla draws the text at (screenHeight - 31 - 4) when the bar is at (screenHeight - 32 + 3).
            // That places the text 6 pixels above the bar. Here we mimic that by drawing at y - 6.
            int textY = y - 6;

            // Draw a shadow around the text for better readability.
            graphics.drawString(this.getFont(), levelText, textX + 1, textY, 0, false);
            graphics.drawString(this.getFont(), levelText, textX - 1, textY, 0, false);
            graphics.drawString(this.getFont(), levelText, textX, textY + 1, 0, false);
            graphics.drawString(this.getFont(), levelText, textX, textY - 1, 0, false);
            // Draw the main level number in yellow (color code 8453920).
            graphics.drawString(this.getFont(), levelText, textX, textY, 8453920, false);

        }

        graphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);

    }

    /**
     * Retrieves the current Minecraft font for rendering text.
     *
     * @return The font used by Minecraft.
     */
    private Font getFont() {
        return Minecraft.getInstance().font;
    }

    @Override
    public int getAbsoluteWidth() {
        return BAR_WIDTH;
    }

    @Override
    public int getAbsoluteHeight() {
        return BAR_HEIGHT;
    }

}
