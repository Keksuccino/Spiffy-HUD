package de.keksuccino.spiffyhud.customization.elements.vanillalike.attackindicator;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class VanillaLikeAttackIndicatorElement extends AbstractElement {

    private static final Logger LOGGER = LogManager.getLogger();

    // Texture used for drawing the experience bar icons.
    private static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation("textures/gui/icons.png");

    private final Minecraft minecraft = Minecraft.getInstance();

    public boolean isHotbar = false;

    public VanillaLikeAttackIndicatorElement(@NotNull ElementBuilder<?, ?> builder) {
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
        this.renderAttackIndicator(graphics, this.isHotbar, elementX, elementY, elementWidth, elementHeight);

        RenderingUtils.resetShaderColor(graphics);

    }

    /**
     * Renders the attack indicator in either crosshair or hotbar mode, scaled to the specified dimensions.
     *
     * @param graphics the GuiGraphics object used for rendering
     * @param isHotbar whether to render the hotbar version (true) or crosshair version (false)
     * @param x the x position to render at
     * @param y the y position to render at
     * @param width the width to render the indicator at
     * @param height the height to render the indicator at
     */
    public void renderAttackIndicator(GuiGraphics graphics, boolean isHotbar, int x, int y, int width, int height) {

        float attackStrength = this.minecraft.player.getAttackStrengthScale(0.0f);
        if (isEditor()) attackStrength = 0.5f;

        RenderSystem.enableBlend();
        graphics.setColor(1.0F, 1.0F, 1.0F, this.opacity);

        if (isHotbar) {
            // Render hotbar version (only shown when not fully charged)
            if (attackStrength < 1.0f) {
                // First render the background
                graphics.blit(GUI_ICONS_LOCATION, x, y, width, height, 0, 94, 18, 18, 256, 256);

                // Then render the foreground based on attack strength
                int fillHeight = (int)(height * attackStrength);
                if (fillHeight > 0) {
                    // Calculate the texture offset based on the fill height proportion
                    int textureFillHeight = (int)(18 * attackStrength);
                    graphics.blit(GUI_ICONS_LOCATION,
                            x, y + height - fillHeight,
                            width, fillHeight,
                            18, 112 - textureFillHeight,
                            18, textureFillHeight,
                            256, 256);
                }
            }
        } else {
            // Render crosshair version
            boolean readyToAttack = false;
            if (this.minecraft.crosshairPickEntity != null &&
                    this.minecraft.crosshairPickEntity instanceof LivingEntity &&
                    attackStrength >= 1.0f) {
                readyToAttack = this.minecraft.player.getCurrentItemAttackStrengthDelay() > 5.0f;
                readyToAttack &= this.minecraft.crosshairPickEntity.isAlive();
            }

            if (readyToAttack) {
                // Render the "ready to attack" indicator
                graphics.blit(GUI_ICONS_LOCATION, x, y, width, height, 68, 94, 16, 16, 256, 256);
            } else if (attackStrength < 1.0f) {
                // Determine a proper height for the progress bar (maintaining aspect ratio)
                int barHeight = height;
                if (width / height > 4) { // Original aspect ratio is 16:4 or 4:1
                    barHeight = Math.max(1, width / 4);
                }

                // First render the background bar
                graphics.blit(GUI_ICONS_LOCATION, x, y, width, barHeight, 36, 94, 16, 4, 256, 256);

                // Then render the foreground based on attack strength
                int fillWidth = (int)(width * attackStrength);
                if (fillWidth > 0) {
                    // The texture width is 17 for the fill bar
                    int textureWidth = (int)(17 * attackStrength);
                    graphics.blit(GUI_ICONS_LOCATION,
                            x, y,
                            fillWidth, barHeight,
                            52, 94,
                            textureWidth, 4,
                            256, 256);
                }
            }
        }

        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);

    }

}
