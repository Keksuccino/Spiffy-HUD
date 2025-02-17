package de.keksuccino.spiffyhud.customization.elements.vanillalike.food;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.spiffyhud.SpiffyUtils;
import de.keksuccino.spiffyhud.util.SpiffyAlignment;
import de.keksuccino.spiffyhud.util.rendering.SpiffyRenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VanillaLikePlayerFoodElement extends AbstractElement {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation("textures/gui/icons.png");

    private final Minecraft minecraft = Minecraft.getInstance();
    protected final RandomSource random = RandomSource.create();
    protected int tickCount;

    // Metrics for the food bar's bounding box within the element
    private int barWidth = 100;
    private int barHeight = 100;

    @NotNull
    public SpiffyAlignment spiffyAlignment = SpiffyAlignment.TOP_LEFT;

    public VanillaLikePlayerFoodElement(@NotNull ElementBuilder<?, ?> builder) {
        super(builder);
    }

    /**
     * Renders the food bar entirely within the element's bounds.
     * First, it updates the food bar's metrics based on a provisional position,
     * then calculates the aligned position within the element, and finally renders the bar.
     */
    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {

        this.tickCount = SpiffyUtils.getGuiAccessor().getTickCount_Spiffy();

        if (this.minecraft.player == null || this.minecraft.level == null) {
            return;
        }

        // Use the element's top-left as a provisional base position to update bar metrics.
        int provisionalBaseX = this.getAbsoluteX();
        int provisionalBaseY = this.getAbsoluteY();
        this.updateBarMetrics(provisionalBaseX, provisionalBaseY);

        // Calculate the aligned position of the food bar within the element.
        int elementX = this.getAbsoluteX();
        int elementY = this.getAbsoluteY();
        Integer[] alignedPosition = SpiffyAlignment.calculateElementBodyPosition(
                this.spiffyAlignment,
                elementX,
                elementY,
                this.getAbsoluteWidth(),
                this.getAbsoluteHeight(),
                this.barWidth,
                this.barHeight
        );
        int barPosX = alignedPosition[0];
        int barPosY = alignedPosition[1];

        // Render the food bar at the calculated aligned position.
        RenderSystem.enableBlend();
        RenderingUtils.resetShaderColor(graphics);

        this.renderFoodBar(graphics, barPosX, barPosY);

        RenderingUtils.resetShaderColor(graphics);

    }

    /**
     * Updates the metrics (original position, width, height) of the food bar based on the layout of food icons.
     *
     * @param baseX The provisional base X coordinate (typically the element's top-left X).
     * @param baseY The provisional base Y coordinate (typically the element's top-left Y).
     */
    private void updateBarMetrics(int baseX, int baseY) {

        Player player = getCameraPlayer();
        if (player == null) return;

        final int numIcons = 10;
        final int ICON_WIDTH = 9;
        final int ICON_SPACING = 8;
        boolean renderIconsLeftToRight = shouldRenderIconsLeftToRight();

        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        // Loop through each icon slot to determine the bar's bounding box.
        for (int i = 0; i < numIcons; i++) {
            // For left-based and center-based alignments, draw left-to-right.
            int iconX = renderIconsLeftToRight ? baseX + i * ICON_SPACING : baseX + ((numIcons - 1 - i) * ICON_SPACING);
            int iconY = baseY;
            minX = Math.min(minX, iconX);
            minY = Math.min(minY, iconY);
            maxX = Math.max(maxX, iconX + ICON_WIDTH);
            maxY = Math.max(maxY, iconY + ICON_WIDTH); // ICON_HEIGHT is the same as ICON_WIDTH (9)
        }
        this.barWidth = maxX - minX;
        this.barHeight = maxY - minY;

    }

    /**
     * Renders the food bar icons (background, full, or half food) at the specified base position.
     * When the icons are to be rendered from the left side (i.e. left- or center–based alignment),
     * we mirror the texture by swapping the U texture coordinates.
     *
     * @param graphics The graphics context used for rendering.
     * @param baseX    The X coordinate where the food bar starts.
     * @param baseY    The Y coordinate where the food bar starts.
     */
    private void renderFoodBar(GuiGraphics graphics, int baseX, int baseY) {

        Player player = getCameraPlayer();
        if (player == null) return;

        FoodData foodData = player.getFoodData();
        int foodLevel = foodData.getFoodLevel();

        if (isEditor()) {
            foodLevel = 9;
        }

        final int numIcons = 10;
        final int ICON_WIDTH = 9;
        final int ICON_HEIGHT = 9;
        final int ICON_SPACING = 8;
        // When rendering left-to-right (for left- and center–based alignments),
        // we want to mirror the icon textures.
        boolean mirrorIcons = shouldRenderIconsLeftToRight();

        // Define texture source parameters.
        int hungerTextureOffset = 0;
        int textureBaseX = 16;
        if (player.hasEffect(MobEffects.HUNGER)) {
            textureBaseX += 36;
            hungerTextureOffset = 13;
        }
        int emptyIconTexX = 16 + hungerTextureOffset * 9;
        int fullIconTexX = textureBaseX + 36;
        int halfIconTexX = textureBaseX + 45;
        int texV = 27;

        // Assume texture size for icons.png is 256x256.
        final int TEXTURE_WIDTH = 256;
        final int TEXTURE_HEIGHT = 256;

        // Loop through each food icon slot.
        for (int i = 0; i < numIcons; i++) {

            // Determine horizontal position based on drawing order.
            int iconX = mirrorIcons ? baseX + i * ICON_SPACING : baseX + ((numIcons - 1 - i) * ICON_SPACING);
            int iconY = baseY;

            // Apply a slight vertical wiggle if saturation is depleted.
            if (foodData.getSaturationLevel() <= 0.0f && tickCount % (foodLevel * 3 + 1) == 0) {
                iconY += this.random.nextInt(3) - 1; // Random offset: -1, 0, or 1
            }

            if (mirrorIcons) {
                // Draw mirrored icons by swapping U texture coordinates.
                SpiffyRenderUtils.blitMirrored(graphics, GUI_ICONS_LOCATION, iconX, iconY, 0,
                        emptyIconTexX, texV, ICON_WIDTH, ICON_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
                if (i * 2 + 1 < foodLevel) {
                    SpiffyRenderUtils.blitMirrored(graphics, GUI_ICONS_LOCATION, iconX, iconY, 0,
                            fullIconTexX, texV, ICON_WIDTH, ICON_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
                } else if (i * 2 + 1 == foodLevel) {
                    SpiffyRenderUtils.blitMirrored(graphics, GUI_ICONS_LOCATION, iconX, iconY, 0,
                            halfIconTexX, texV, ICON_WIDTH, ICON_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
                }
            } else {
                // Normal (non-mirrored) drawing.
                graphics.blit(GUI_ICONS_LOCATION, iconX, iconY, emptyIconTexX, texV, ICON_WIDTH, ICON_HEIGHT);
                if (i * 2 + 1 < foodLevel) {
                    graphics.blit(GUI_ICONS_LOCATION, iconX, iconY, fullIconTexX, texV, ICON_WIDTH, ICON_HEIGHT);
                } else if (i * 2 + 1 == foodLevel) {
                    graphics.blit(GUI_ICONS_LOCATION, iconX, iconY, halfIconTexX, texV, ICON_WIDTH, ICON_HEIGHT);
                }
            }

        }

    }

    /**
     * Helper method to determine whether the food icons should be rendered left-to-right.
     * Returns true for left-based and center-based alignments.
     *
     * @return true if alignment is TOP_LEFT, MID_LEFT, BOTTOM_LEFT, TOP_CENTERED, MID_CENTERED, or BOTTOM_CENTERED.
     */
    private boolean shouldRenderIconsLeftToRight() {
        return spiffyAlignment == SpiffyAlignment.TOP_LEFT ||
                spiffyAlignment == SpiffyAlignment.MID_LEFT ||
                spiffyAlignment == SpiffyAlignment.BOTTOM_LEFT ||
                spiffyAlignment == SpiffyAlignment.TOP_CENTERED ||
                spiffyAlignment == SpiffyAlignment.MID_CENTERED ||
                spiffyAlignment == SpiffyAlignment.BOTTOM_CENTERED;
    }

    @Nullable
    private Player getCameraPlayer() {
        return (Minecraft.getInstance().getCameraEntity() instanceof Player p) ? p : null;
    }

    @Override
    public int getAbsoluteWidth() {
        return 100;
    }

    @Override
    public int getAbsoluteHeight() {
        return 20;
    }

}
