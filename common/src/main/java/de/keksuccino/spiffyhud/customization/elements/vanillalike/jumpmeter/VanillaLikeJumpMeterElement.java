package de.keksuccino.spiffyhud.customization.elements.vanillalike.jumpmeter;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.PlayerRideableJumping;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;

public class VanillaLikeJumpMeterElement extends AbstractElement {

    private static final Logger LOGGER = LogManager.getLogger();

    // Sprite resources for jump meter in 1.21.1
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

        // Enable blending and reset shader colors.
        RenderSystem.enableBlend();
        RenderingUtils.resetShaderColor(graphics);

        // Render the jump meter inside the element's bounds.
        this.renderJumpMeter(graphics, elementX, elementY, elementWidth, elementHeight);

        RenderingUtils.resetShaderColor(graphics);

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

        // Enable blending and set the shader color with the desired opacity.
        RenderSystem.enableBlend();
        graphics.setColor(1.0f, 1.0f, 1.0f, this.opacity);

        // Get the player's rideable jump vehicle, if any.
        PlayerRideableJumping rideable = Objects.requireNonNull(this.minecraft.player).jumpableVehicle();

        // Calculate the jump riding scale and determine the width of the jump meter fill.
        float jumpScale = this.minecraft.player.getJumpRidingScale();
        int fillWidth = (int) (jumpScale * elementWidth);
        if (isEditor()) fillWidth = BAR_WIDTH / 2;

        // Draw the jump meter background
        graphics.blitSprite(JUMP_BAR_BACKGROUND_SPRITE, elementX, elementY, elementWidth, elementHeight);

        // If the player is riding an entity with jump cooldown, render the cooldown overlay
        if ((rideable != null && rideable.getJumpCooldown() > 0) && !isEditor()) {
            graphics.blitSprite(JUMP_BAR_COOLDOWN_SPRITE, elementX, elementY, elementWidth, elementHeight);
        }
        // Otherwise, if there is a jump meter fill value, render the filled portion
        else if (fillWidth > 0) {
            graphics.blitSprite(JUMP_BAR_PROGRESS_SPRITE, elementWidth, elementHeight, 0, 0, elementX, elementY, fillWidth, elementHeight);
        }

        graphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);

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
