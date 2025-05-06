package de.keksuccino.spiffyhud.customization.elements.vanillalike.jumpmeter;

import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.PlayerRideableJumping;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;

public class VanillaLikeJumpMeterElement extends AbstractElement {

    private static final Logger LOGGER = LogManager.getLogger();

    // Sprite resources for jump meter in 1.21.5
    private static final ResourceLocation JUMP_BAR_BACKGROUND_SPRITE = ResourceLocation.withDefaultNamespace("hud/jump_bar_background");
    private static final ResourceLocation JUMP_BAR_COOLDOWN_SPRITE = ResourceLocation.withDefaultNamespace("hud/jump_bar_cooldown");
    private static final ResourceLocation JUMP_BAR_PROGRESS_SPRITE = ResourceLocation.withDefaultNamespace("hud/jump_bar_progress");

    private static final int BAR_WIDTH = 182;
    private static final int BAR_HEIGHT = 5;

    private final Minecraft minecraft = Minecraft.getInstance();

    public VanillaLikeJumpMeterElement(@NotNull ElementBuilder<?, ?> builder) {
        super(builder);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        // Ensure that both the player and the level exist before rendering.
        if (this.minecraft.player == null || this.minecraft.level == null) return;

        // Retrieve the element's absolute position and size.
        int elementX = this.getAbsoluteX();
        int elementY = this.getAbsoluteY();
        int elementWidth = this.getAbsoluteWidth();
        int elementHeight = this.getAbsoluteHeight();

        // Render the jump meter inside the element's bounds.
        this.renderJumpMeter(graphics, elementX, elementY, elementWidth, elementHeight);
    }

    /**
     * Renders the jump meter bar within the specified element bounds.
     *
     * @param graphics      The graphics context used for rendering.
     * @param elementX      The absolute x-coordinate of the element.
     * @param elementY      The absolute y-coordinate of the element.
     * @param elementWidth  The width of the element.
     * @param elementHeight The height of the element.
     */
    private void renderJumpMeter(GuiGraphics graphics, int elementX, int elementY, int elementWidth, int elementHeight) {
        // Calculate the color with opacity
        int color = ARGB.color(Math.round(this.opacity * 255f), 255, 255, 255);

        // Get the player's rideable jump vehicle, if any.
        PlayerRideableJumping rideable = Objects.requireNonNull(this.minecraft.player).jumpableVehicle();

        // Calculate the jump riding scale and determine the width of the jump meter fill.
        float jumpScale = this.minecraft.player.getJumpRidingScale();
        int fillWidth = (int) (jumpScale * elementWidth);
        if (isEditor()) fillWidth = BAR_WIDTH / 2;

        // Draw the jump meter background with proper color
        graphics.blitSprite(
            RenderType::guiTextured,
            JUMP_BAR_BACKGROUND_SPRITE,
            elementX,
            elementY,
            elementWidth,
            elementHeight,
            color
        );

        // If the player is riding an entity with jump cooldown, render the cooldown overlay
        if ((rideable != null && rideable.getJumpCooldown() > 0) && !isEditor()) {
            graphics.blitSprite(
                RenderType::guiTextured,
                JUMP_BAR_COOLDOWN_SPRITE,
                elementX,
                elementY,
                elementWidth,
                elementHeight,
                color
            );
        }
        // Otherwise, if there is a jump meter fill value, render the filled portion
        else if (fillWidth > 0) {
            // Use the correct method signature for the progress bar
            // In 1.21.5, we need to use a different approach for partial sprite rendering
            
            // First enable scissor test to clip the sprite to the desired width
            graphics.enableScissor(elementX, elementY, elementX + fillWidth, elementY + elementHeight);
            
            // Then render the full sprite (it will be clipped by the scissor test)
            graphics.blitSprite(
                RenderType::guiTextured,
                JUMP_BAR_PROGRESS_SPRITE,
                elementX,
                elementY,
                elementWidth,
                elementHeight,
                color
            );
            
            // Finally, disable the scissor test
            graphics.disableScissor();
        }
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
