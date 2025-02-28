package de.keksuccino.spiffyhud.customization.elements.vanillalike.air;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.spiffyhud.SpiffyUtils;
import de.keksuccino.spiffyhud.util.SpiffyAlignment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VanillaLikePlayerAirElement extends AbstractElement {

    private static final Logger LOGGER = LogManager.getLogger();

    // The texture containing the air bubble icons.
    private static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation("textures/gui/icons.png");

    // Define constants for bubble dimensions.
    private static final int BUBBLE_SIZE = 9;      // The width (and height) of a bubble in pixels.
    private static final int BUBBLE_SPACING = 8;   // The spacing offset between bubbles.

    private static final int TOTAL_BAR_WIDTH = 81;
    private static final int TOTAL_BAR_HEIGHT = 9;

    private final Minecraft minecraft = Minecraft.getInstance();
    protected int tickCount;

    // Variables to record the calculated air bar size and its drawing origin.
    private int barWidth = 100;
    private int barHeight = 100;

    // Flag to control whether actual drawing should occur.
    private boolean shouldRenderBar = false;

    @NotNull
    public SpiffyAlignment spiffyAlignment = SpiffyAlignment.TOP_LEFT;

    public VanillaLikePlayerAirElement(@NotNull ElementBuilder<?, ?> builder) {
        super(builder);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {

        // Update the tick counter.
        this.tickCount = SpiffyUtils.getGuiAccessor().getTickCount_Spiffy();

        if (this.minecraft.player == null) return;
        if (this.minecraft.level == null) return;

        // First pass: perform a dry-run (without drawing) to update the bar's dimensions.
        this.shouldRenderBar = false;
        // We call renderPlayerAir with an offset of (0,0) because we only need to update barWidth and barHeight.
        this.renderPlayerAir(graphics, 0, 0);

        // Retrieve the element's absolute position and size.
        int elementAbsX = this.getAbsoluteX();
        int elementAbsY = this.getAbsoluteY();
        int elementWidth = this.getAbsoluteWidth();
        int elementHeight = this.getAbsoluteHeight();

        // Calculate the aligned position for the air bar within this element.
        // de.keksuccino.spiffyhud.util.Alignment.calculateElementBodyPosition() returns an array { alignedX, alignedY }.
        Integer[] alignedPosition = SpiffyAlignment.calculateElementBodyPosition(
                this.spiffyAlignment,
                elementAbsX,
                elementAbsY,
                elementWidth,
                elementHeight,
                this.barWidth,
                this.barHeight
        );
        int barAbsX = alignedPosition[0];
        int barAbsY = alignedPosition[1];

        // Directly draw the air bar at the calculated absolute position.
        RenderSystem.enableBlend();
        RenderingUtils.resetShaderColor(graphics);

        this.shouldRenderBar = true;
        this.renderPlayerAir(graphics, barAbsX, barAbsY);

        RenderingUtils.resetShaderColor(graphics);

    }

    /**
     * Renders the player's air bubbles.
     *
     * @param graphics The graphics context.
     * @param offsetX  The absolute X coordinate where the air bar should start.
     * @param offsetY  The absolute Y coordinate where the air bar should start.
     *
     * This method calculates the number of full and empty bubbles based on the player's air supply.
     * It then determines the drawing order (left-to-right for left-based alignment, right-to-left for right-based or centered)
     * and draws each bubble at the appropriate absolute position.
     *
     * When shouldRenderBar is false, the method only updates the bar's dimensions (barWidth and barHeight).
     */
    private void renderPlayerAir(GuiGraphics graphics, int offsetX, int offsetY) {

        Player player = getCameraPlayer();
        if (player == null) {
            return;
        }

        // Enable blending and set the shader color with the desired opacity.
        RenderSystem.enableBlend();
        graphics.setColor(1.0f, 1.0f, 1.0f, this.opacity);

        // Get the maximum and current air supply.
        int maxAir = player.getMaxAirSupply();
        int currentAir = Math.min(player.getAirSupply(), maxAir);

        // Calculate the number of full and empty air bubbles (using vanilla-like logic).
        int fullAirBubbles = Mth.ceil((currentAir - 2) * 10.0 / maxAir);
        int emptyAirBubbles = Mth.ceil(currentAir * 10.0 / maxAir) - fullAirBubbles;
        int totalBubbles = fullAirBubbles + emptyAirBubbles;

        // Total width of the air bar is the width of one bubble plus spacing for remaining bubbles.
        int totalBarWidth = BUBBLE_SIZE + (totalBubbles - 1) * BUBBLE_SPACING;

        // Determine the drawing direction based on the element's alignment.
        // Left-based alignments: TOP_LEFT, MID_LEFT, BOTTOM_LEFT.
        // Right-based alignments: TOP_RIGHT, MID_RIGHT, BOTTOM_RIGHT.
        // Centered alignments (TOP_CENTERED, MID_CENTERED, BOTTOM_CENTERED) are treated like right-based.
        boolean leftAligned = (this.spiffyAlignment == SpiffyAlignment.TOP_LEFT ||
                this.spiffyAlignment == SpiffyAlignment.MID_LEFT ||
                this.spiffyAlignment == SpiffyAlignment.BOTTOM_LEFT);

        // Calculate the starting X coordinate within the air bar.
        // For left-aligned, drawing starts at the left edge (0), for right-based it starts at the right edge.
        int localStartX = leftAligned ? 0 : (totalBarWidth - BUBBLE_SIZE);
        // The base Y coordinate (local) is 0.
        int localStartY = 0;

        // Render each bubble.
        for (int i = 0; i < totalBubbles; i++) {
            int bubbleLocalX;
            if (leftAligned) {
                // In left-to-right mode, each subsequent bubble is shifted to the right.
                bubbleLocalX = localStartX + i * BUBBLE_SPACING;
            } else {
                // In right-to-left mode, each subsequent bubble is shifted to the left.
                bubbleLocalX = localStartX - i * BUBBLE_SPACING;
            }
            int bubbleAbsX = offsetX + bubbleLocalX;
            int bubbleAbsY = offsetY + localStartY;

            // Determine if this bubble should be rendered as full.
            boolean isFullBubble = (i < fullAirBubbles);

            if (this.shouldRenderBar) {
                if (isFullBubble) {
                    // Render a full air bubble.
                    // Texture coordinates: (16,18) with size BUBBLE_SIZE x BUBBLE_SIZE.
                    graphics.blit(GUI_ICONS_LOCATION, bubbleAbsX, bubbleAbsY, 16, 18, BUBBLE_SIZE, BUBBLE_SIZE);
                } else {
                    // Render an empty air bubble.
                    // Texture coordinates: (25,18) with size BUBBLE_SIZE x BUBBLE_SIZE.
                    graphics.blit(GUI_ICONS_LOCATION, bubbleAbsX, bubbleAbsY, 25, 18, BUBBLE_SIZE, BUBBLE_SIZE);
                }
            }
        }

        // Update the bar's recorded drawing origin and size.
        // When called in dry-run mode (offsetX == 0, offsetY == 0) these values help position the bar within the element.
        this.barWidth = totalBarWidth;
        this.barHeight = BUBBLE_SIZE;

        graphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);

    }

    @Nullable
    private Player getCameraPlayer() {
        return (Minecraft.getInstance().getCameraEntity() instanceof Player p) ? p : null;
    }

    @Override
    public int getAbsoluteWidth() {
        return TOTAL_BAR_WIDTH;
    }

    @Override
    public int getAbsoluteHeight() {
        return TOTAL_BAR_HEIGHT;
    }

}
