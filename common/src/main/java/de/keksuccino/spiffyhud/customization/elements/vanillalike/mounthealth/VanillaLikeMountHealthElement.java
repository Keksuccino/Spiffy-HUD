package de.keksuccino.spiffyhud.customization.elements.vanillalike.mounthealth;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.spiffyhud.SpiffyUtils;
import de.keksuccino.spiffyhud.util.SpiffyAlignment;
import de.keksuccino.spiffyhud.util.rendering.SpiffyRenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VanillaLikeMountHealthElement extends AbstractElement {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation("textures/gui/icons.png");

    private final Minecraft minecraft = Minecraft.getInstance();
    protected int tickCount;

    // These values will be computed based on the drawn hearts layout.
    private int barWidth = 100;
    private int barHeight = 100;
    // Since the new drawing method draws directly using absolute positions, these remain 0.
    private int barOriginalX = 0;
    private int barOriginalY = 0;

    // When false, the drawing method only computes layout dimensions.
    private boolean shouldRenderBar = false;

    @NotNull
    public SpiffyAlignment spiffyAlignment = SpiffyAlignment.TOP_LEFT;

    public VanillaLikeMountHealthElement(@NotNull ElementBuilder<?, ?> builder) {
        super(builder);
    }

    /**
     * Main render method.
     *
     * First, we compute the layout of the health bar (without drawing) so that the correct
     * bar width/height can be determined. Then, using the element's absolute position and the chosen
     * alignment, we render the hearts at the proper offset.
     */
    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        this.tickCount = SpiffyUtils.getGuiAccessor().getTickCount_Spiffy();

        if (this.minecraft.player == null || this.minecraft.level == null) {
            return;
        }

        //––– First, compute the layout dimensions without actually drawing the hearts.
        this.shouldRenderBar = false;
        // Draw at (0,0) only to calculate the layout (barWidth and barHeight will be updated)
        renderVehicleHealth(graphics, 0, 0);

        //––– Compute the position of the bar within the element based on alignment.
        int elementX = this.getAbsoluteX();
        int elementY = this.getAbsoluteY();
        Integer[] alignedBody = SpiffyAlignment.calculateElementBodyPosition(
                this.spiffyAlignment,
                elementX,
                elementY,
                this.getAbsoluteWidth(),
                this.getAbsoluteHeight(),
                this.barWidth,
                this.barHeight
        );
        int barRenderX = alignedBody[0];
        int barRenderY = alignedBody[1];

        //––– Now, actually render the hearts using the computed offset.
        this.shouldRenderBar = true;
        renderVehicleHealth(graphics, barRenderX, barRenderY);
    }

    /**
     * Renders (or computes) the mount health bar.
     *
     * The bar is drawn as a series of heart icons arranged in rows.
     * <p>
     * For each row:
     * - The drawing area is considered to have a fixed width of 10 hearts (10×8 pixels).
     * - For left–, center– or right–based alignments, hearts are drawn in natural (left-to-right) order.
     * - For right–based alignments, hearts are drawn in reverse order so that the full hearts start on the right.
     * <p>
     * Additionally, if the vertical alignment is top–based (TOP_LEFT, TOP_CENTERED, TOP_RIGHT),
     * then the order of rows is inverted so that rows with full hearts appear at the top.
     *
     * @param graphics the GUI graphics context used for rendering.
     * @param baseX    the x–offset at which to start drawing the bar (already computed from element alignment).
     * @param baseY    the y–offset at which to start drawing the bar.
     */
    private void renderVehicleHealth(GuiGraphics graphics, int baseX, int baseY) {
        LivingEntity mount = this.getPlayerVehicleWithHealth();
        int totalHearts = (mount != null) ? this.getVehicleMaxHearts(mount) : 10;
        int currentHealth = (int) Math.ceil((mount != null) ? mount.getHealth() : 0);

        // For editor mode, use fixed values.
        if (isEditor()) {
            totalHearts = 20;
            currentHealth = 9;
        }
        if (totalHearts == 0) {
            return;
        }

        final int heartsPerRow = 10;
        // For drawing alignment purposes, we always use a fixed row width.
        final int fixedRowWidth = heartsPerRow * 8;
        // Determine the number of rows needed.
        int totalRows = (int) Math.ceil(totalHearts / (double) heartsPerRow);

        // For full rows (or if total hearts is fewer than heartsPerRow), we use fixedRowWidth.
        int computedBarWidth = (totalHearts >= heartsPerRow) ? fixedRowWidth : fixedRowWidth;
        int computedBarHeight = totalRows * 10;

        // Save computed bar dimensions so that alignment calculations work correctly.
        this.barOriginalX = 0;
        this.barOriginalY = 0;
        this.barWidth = computedBarWidth;
        this.barHeight = computedBarHeight;

        // Determine if the vertical alignment is top-based.
        boolean isTopAlignment = (this.spiffyAlignment == SpiffyAlignment.TOP_LEFT ||
                this.spiffyAlignment == SpiffyAlignment.TOP_CENTERED ||
                this.spiffyAlignment == SpiffyAlignment.TOP_RIGHT);
        // Determine if the horizontal alignment is right-based.
        boolean isRightAlignment = (this.spiffyAlignment == SpiffyAlignment.TOP_RIGHT ||
                this.spiffyAlignment == SpiffyAlignment.BOTTOM_RIGHT ||
                this.spiffyAlignment == SpiffyAlignment.MID_RIGHT);
        // Determine if the hearts should be mirrored horizontally.
        // Now mirror for left-based and center-based alignments.
        boolean mirrorHearts = (this.spiffyAlignment == SpiffyAlignment.TOP_LEFT ||
                this.spiffyAlignment == SpiffyAlignment.BOTTOM_LEFT ||
                this.spiffyAlignment == SpiffyAlignment.MID_LEFT ||
                this.spiffyAlignment == SpiffyAlignment.TOP_CENTERED ||
                this.spiffyAlignment == SpiffyAlignment.BOTTOM_CENTERED ||
                this.spiffyAlignment == SpiffyAlignment.MID_CENTERED);

        // Iterate over each row.
        // For vertical order: if top–based, invert the row order so that the top row is drawn first.
        for (int row = 0; row < totalRows; row++) {
            int rowBottomUp = isTopAlignment ? (totalRows - 1 - row) : row;

            // Calculate the number of hearts in this row.
            int heartsThisRow = (rowBottomUp == totalRows - 1) ? (totalHearts - (totalRows - 1) * heartsPerRow) : heartsPerRow;

            // For non–right alignments, determine the horizontal offset.
            int rowStartX = 0;
            if (!isRightAlignment) {
                if (this.spiffyAlignment == SpiffyAlignment.TOP_CENTERED ||
                        this.spiffyAlignment == SpiffyAlignment.BOTTOM_CENTERED ||
                        this.spiffyAlignment == SpiffyAlignment.MID_CENTERED) {
                    rowStartX = (fixedRowWidth - heartsThisRow * 8) / 2;
                } else { // Left–based: start at 0.
                    rowStartX = 0;
                }
            }
            // Determine vertical position for this row.
            int rowY = isTopAlignment ? row * 10 : rowBottomUp * 10;

            // Draw each heart in the row.
            for (int col = 0; col < heartsThisRow; col++) {
                int effectiveCol; // Represents the natural order index within the row.
                int heartX;
                if (isRightAlignment) {
                    // For right alignment, reverse the order:
                    // The rightmost heart corresponds to effectiveCol 0.
                    effectiveCol = heartsThisRow - 1 - col;
                    // Position so that the rightmost heart is at the right edge of the fixed row.
                    heartX = baseX + fixedRowWidth - (effectiveCol + 1) * 8;
                } else {
                    // For left or centered, use natural order.
                    effectiveCol = col;
                    heartX = baseX + rowStartX + effectiveCol * 8;
                }
                // Compute overall heart index based on natural order (regardless of drawing order).
                int overallHeartIndex = rowBottomUp * heartsPerRow + effectiveCol;
                int heartY = baseY + rowY;

                if (this.shouldRenderBar) {
                    // Draw empty heart background.
                    if (mirrorHearts) {
                        SpiffyRenderUtils.blitMirrored(graphics, GUI_ICONS_LOCATION, heartX, heartY, 0, 52, 9, 9, 9, 256, 256);
                    } else {
                        graphics.blit(GUI_ICONS_LOCATION, heartX, heartY, 52, 9, 9, 9);
                    }
                    // Each heart represents 2 health points.
                    int heartThreshold = overallHeartIndex * 2 + 1;
                    if (heartThreshold < currentHealth) {
                        // Render full heart.
                        if (mirrorHearts) {
                            SpiffyRenderUtils.blitMirrored(graphics, GUI_ICONS_LOCATION, heartX, heartY, 0, 88, 9, 9, 9, 256, 256);
                        } else {
                            graphics.blit(GUI_ICONS_LOCATION, heartX, heartY, 88, 9, 9, 9);
                        }
                    } else if (heartThreshold == currentHealth) {
                        // Render half heart.
                        if (mirrorHearts) {
                            SpiffyRenderUtils.blitMirrored(graphics, GUI_ICONS_LOCATION, heartX, heartY, 0, 97, 9, 9, 9, 256, 256);
                        } else {
                            graphics.blit(GUI_ICONS_LOCATION, heartX, heartY, 97, 9, 9, 9);
                        }
                    }
                    // Otherwise, leave the empty heart.
                }
            }
        }
    }

    /**
     * Returns the maximum hearts for the given vehicle.
     *
     * @param vehicle the vehicle entity.
     * @return the number of hearts to display, capped at 30.
     */
    private int getVehicleMaxHearts(@Nullable LivingEntity vehicle) {
        if (vehicle == null || !vehicle.showVehicleHealth()) {
            return 0;
        }
        float maxHealth = vehicle.getMaxHealth();
        int hearts = (int) (maxHealth + 0.5f) / 2;
        if (hearts > 30) {
            hearts = 30;
        }
        return hearts;
    }

    /**
     * Returns the vehicle (if any) that the player is riding and which has health.
     *
     * @return the living vehicle entity or null.
     */
    @Nullable
    private LivingEntity getPlayerVehicleWithHealth() {
        Player player = this.getCameraPlayer();
        if (player != null) {
            Entity vehicle = player.getVehicle();
            if (vehicle instanceof LivingEntity) {
                return (LivingEntity) vehicle;
            }
        }
        return null;
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
        return 30;
    }
}
