package de.keksuccino.spiffyhud.customization.elements.vanillalike.jumpmeter;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import net.minecraft.client.Minecraft;
import de.keksuccino.fancymenu.util.rendering.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PlayerRideableJumping;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;

public class VanillaLikeJumpMeterElement extends AbstractElement {

    private static final Logger LOGGER = LogManager.getLogger();

    // Texture containing GUI icons.
    private static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation("textures/gui/icons.png");

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

        // Calculate the jump riding scale and determine the width of the jump meter fill.
        float jumpScale = this.minecraft.player.getJumpRidingScale();
        int fillWidth = (int) (jumpScale * elementWidth);
        if (isEditor()) fillWidth = BAR_WIDTH / 2;

        // Draw the jump meter background (texture region starting at y = 84).
        graphics.blit(GUI_ICONS_LOCATION, elementX, elementY, 0, 84, elementWidth, elementHeight);

        // If there is a jump meter fill value, render the filled portion (texture region at y = 89).
        if (fillWidth > 0) {
            graphics.blit(GUI_ICONS_LOCATION, elementX, elementY, 0, 89, fillWidth, elementHeight);
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
