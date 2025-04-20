package de.keksuccino.spiffyhud.customization.elements.vanillalike.bossbars;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.spiffyhud.util.SizeAndPositionRecorder;
import de.keksuccino.spiffyhud.util.SpiffyAlignment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.BossEvent.BossBarOverlay;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.UUID;

public class VanillaLikeBossOverlayElement extends AbstractElement {

    private static final Logger LOGGER = LogManager.getLogger();

    // Sprite resources for boss bars in 1.21.1
    private static final ResourceLocation[] BAR_BACKGROUND_SPRITES = new ResourceLocation[]{
        ResourceLocation.withDefaultNamespace("boss_bar/pink_background"),
        ResourceLocation.withDefaultNamespace("boss_bar/blue_background"),
        ResourceLocation.withDefaultNamespace("boss_bar/red_background"),
        ResourceLocation.withDefaultNamespace("boss_bar/green_background"),
        ResourceLocation.withDefaultNamespace("boss_bar/yellow_background"),
        ResourceLocation.withDefaultNamespace("boss_bar/purple_background"),
        ResourceLocation.withDefaultNamespace("boss_bar/white_background")
    };
    
    private static final ResourceLocation[] BAR_PROGRESS_SPRITES = new ResourceLocation[]{
        ResourceLocation.withDefaultNamespace("boss_bar/pink_progress"),
        ResourceLocation.withDefaultNamespace("boss_bar/blue_progress"),
        ResourceLocation.withDefaultNamespace("boss_bar/red_progress"),
        ResourceLocation.withDefaultNamespace("boss_bar/green_progress"),
        ResourceLocation.withDefaultNamespace("boss_bar/yellow_progress"),
        ResourceLocation.withDefaultNamespace("boss_bar/purple_progress"),
        ResourceLocation.withDefaultNamespace("boss_bar/white_progress")
    };
    
    private static final ResourceLocation[] OVERLAY_BACKGROUND_SPRITES = new ResourceLocation[]{
        ResourceLocation.withDefaultNamespace("boss_bar/notched_6_background"),
        ResourceLocation.withDefaultNamespace("boss_bar/notched_10_background"),
        ResourceLocation.withDefaultNamespace("boss_bar/notched_12_background"),
        ResourceLocation.withDefaultNamespace("boss_bar/notched_20_background")
    };
    
    private static final ResourceLocation[] OVERLAY_PROGRESS_SPRITES = new ResourceLocation[]{
        ResourceLocation.withDefaultNamespace("boss_bar/notched_6_progress"),
        ResourceLocation.withDefaultNamespace("boss_bar/notched_10_progress"),
        ResourceLocation.withDefaultNamespace("boss_bar/notched_12_progress"),
        ResourceLocation.withDefaultNamespace("boss_bar/notched_20_progress")
    };

    // Dummy events for editor mode.
    private static final List<LerpingBossEvent> DUMMY_EVENTS = List.of(
            new LerpingBossEvent(UUID.randomUUID(), Component.translatable("spiffyhud.elements.dummy.boss_bars.bar"), 0.5F, BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS, false, false, false),
            new LerpingBossEvent(UUID.randomUUID(), Component.translatable("spiffyhud.elements.dummy.boss_bars.bar"), 0.5F, BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS, false, false, false),
            new LerpingBossEvent(UUID.randomUUID(), Component.translatable("spiffyhud.elements.dummy.boss_bars.bar"), 0.5F, BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS, false, false, false)
    );

    private final Minecraft minecraft = Minecraft.getInstance();

    // Cached bounds for the boss bar drawing area (in a local coordinate system).
    // barWidth is the larger of the default bar width (182) or the computed width (if text overflows);
    // barHeight is computed from the total height of all bars.
    // barOriginalX and barOriginalY represent the recorded minimal X/Y of the boss bar area.
    private int barWidth = 182;
    private int barHeight = 0;
    private int barOriginalX = 0;
    private int barOriginalY = 0;

    @NotNull
    public SpiffyAlignment spiffyAlignment = SpiffyAlignment.TOP_LEFT;

    public VanillaLikeBossOverlayElement(@NotNull ElementBuilder<?, ?> builder) {
        super(builder);
    }

    /**
     * Renders the boss overlay element within its bounds.
     * First the local boss bar area is computed and then aligned inside the element.
     * We then render all boss events (dummy events in editor mode or real ones otherwise)
     * using our own drawing code.
     */
    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {

        if (this.minecraft.player == null || this.minecraft.level == null) return;

        // Compute the local (relative) boss bar area.
        this.updateBodySizeAndPosCache();

        // Get the element's absolute bounds.
        int elementX = this.getAbsoluteX();
        int elementY = this.getAbsoluteY();
        int elementWidth = this.getAbsoluteWidth();
        int elementHeight = this.getAbsoluteHeight();

        // Compute the aligned position of the boss bar area within the element.
        // (SpiffyAlignment.calculateElementBodyPosition expects absolute values, so we pass the element bounds
        // and our computed boss bar area dimensions. The returned alignedPosition is absolute.)
        Integer[] alignedPosition = SpiffyAlignment.calculateElementBodyPosition(
                this.spiffyAlignment, elementX, elementY, elementWidth, elementHeight, this.barWidth, this.barHeight);
        // Compute offsets relative to our recorded local boss bar area.
        int offsetX = alignedPosition[0] - this.barOriginalX;
        int offsetY = alignedPosition[1] - this.barOriginalY;

        RenderSystem.enableBlend();
        RenderingUtils.resetShaderColor(graphics);

        // Choose events to render.
        Iterable<LerpingBossEvent> eventsToRender = isEditor() ? DUMMY_EVENTS : Minecraft.getInstance().gui.getBossOverlay().events.values();

        this.renderBossBars(graphics, offsetX, offsetY, eventsToRender);

        RenderingUtils.resetShaderColor(graphics);

    }

    /**
     * Updates the cached local drawing bounds of the boss bar area.
     * Here we use relative coordinates so that the boss bars are laid out starting at X = 0.
     * The default boss bar width is 182.
     */
    private void updateBodySizeAndPosCache() {
        SizeAndPositionRecorder recorder = new SizeAndPositionRecorder();
        recorder.setHeightOffset(5);
        int defaultBarWidth = 182;

        // Use dummy events in editor mode, or real events otherwise.
        Iterable<LerpingBossEvent> eventsToRecord = isEditor() ? DUMMY_EVENTS
                : Minecraft.getInstance().gui.getBossOverlay().events.values();
        if (!eventsToRecord.iterator().hasNext() && !isEditor()) {
            return;
        }
        int currentY = 12; // Starting Y coordinate (local)
        for (LerpingBossEvent bossEvent : eventsToRecord) {
            // For the boss bar background, use X = 0.
            int barPosX = 0;
            int barPosY = currentY;
            recorder.updateX(barPosX);
            recorder.updateY(barPosY);

            // For the text, center it in the default width.
            Component eventName = bossEvent.getName();
            int textWidth = this.minecraft.font.width(eventName);
            int textPosX = (defaultBarWidth - textWidth) / 2;
            int textPosY = barPosY - 9;
            recorder.updateX(textPosX);
            recorder.updateY(textPosY);

            currentY += 10 + this.minecraft.font.lineHeight;
        }
        // Record the minimal X/Y and the total width/height.
        this.barOriginalX = recorder.getX(); // Typically 0 (or negative if text overflows)
        this.barOriginalY = recorder.getY();
        this.barWidth = Math.max(defaultBarWidth, recorder.getWidth());
        this.barHeight = recorder.getHeight();
    }

    /**
     * Renders all boss bars (and their text) from the provided events.
     * The positions are adjusted by the given offsets so that the boss bars render inside the element.
     *
     * @param graphics   The graphics context.
     * @param offsetX    Horizontal offset (from alignment).
     * @param offsetY    Vertical offset (from alignment).
     * @param bossEvents The boss events to render.
     */
    private void renderBossBars(GuiGraphics graphics, int offsetX, int offsetY, Iterable<LerpingBossEvent> bossEvents) {

        // Enable blending and set the shader color with the desired opacity.
        RenderSystem.enableBlend();
        graphics.setColor(1.0f, 1.0f, 1.0f, this.opacity);

        int currentY = offsetY + 12; // Start at the same relative Y as used in updateBodySizeAndPosCache.
        for (LerpingBossEvent bossEvent : bossEvents) {
            // Draw the boss bar background at local X = 0 (plus offset).
            int barPosX = offsetX;
            int barPosY = currentY;
            RenderSystem.enableBlend();
            graphics.setColor(1.0f, 1.0f, 1.0f, this.opacity);
            drawBar(graphics, barPosX, barPosY, bossEvent);
            // Center the boss bar text within the computed element width.
            Component eventName = bossEvent.getName();
            int textWidth = this.minecraft.font.width(eventName);
            int textPosX = offsetX + (getAbsoluteWidth() - textWidth) / 2;
            int textPosY = barPosY - 9;
            RenderSystem.enableBlend();
            graphics.setColor(1.0f, 1.0f, 1.0f, this.opacity);
            graphics.drawString(this.minecraft.font, eventName, textPosX, textPosY, 0xFFFFFF);
            currentY += 10 + this.minecraft.font.lineHeight;
        }

        graphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);

    }

    /**
     * Draws a single boss bar by first drawing its background and then the filled progress.
     *
     * @param graphics  The graphics context.
     * @param barX      The X coordinate (local, plus offset) where the bar is drawn.
     * @param barY      The Y coordinate (local, plus offset) where the bar is drawn.
     * @param bossEvent The boss event providing progress and style.
     */
    private void drawBar(GuiGraphics graphics, int barX, int barY, BossEvent bossEvent) {
        // Draw the background (default width = 182).
        drawBar(graphics, barX, barY, bossEvent, 182, BAR_BACKGROUND_SPRITES, OVERLAY_BACKGROUND_SPRITES);
        // Calculate and draw the filled portion based on progress.
        int filledWidth = Mth.lerpDiscrete(bossEvent.getProgress(), 0, 182);
        if (filledWidth > 0) {
            drawBar(graphics, barX, barY, bossEvent, filledWidth, BAR_PROGRESS_SPRITES, OVERLAY_PROGRESS_SPRITES);
        }
    }

    /**
     * Draws a segment of a boss bar using sprite resources.
     *
     * @param graphics      The graphics context.
     * @param barX          The X coordinate for drawing.
     * @param barY          The Y coordinate for drawing.
     * @param bossEvent     The boss event providing styling.
     * @param barSprites    Array of bar sprites for different colors.
     * @param overlaySprites Array of overlay sprites for different notch patterns.
     */
    private void drawBar(GuiGraphics graphics, int barX, int barY, BossEvent bossEvent, int progress, 
                         ResourceLocation[] barSprites, ResourceLocation[] overlaySprites) {
        RenderSystem.enableBlend();
        graphics.blitSprite(barSprites[bossEvent.getColor().ordinal()], 182, 5, 0, 0, barX, barY, progress, 5);
        if (bossEvent.getOverlay() != BossBarOverlay.PROGRESS) {
            graphics.blitSprite(overlaySprites[bossEvent.getOverlay().ordinal() - 1], 182, 5, 0, 0, barX, barY, progress, 5);
        }
        RenderSystem.disableBlend();
    }

    /**
     * Returns the computed absolute width of the element (the boss bar area).
     */
    @Override
    public int getAbsoluteWidth() {
        return this.barWidth;
    }

    /**
     * Returns the computed absolute height of the element (the boss bar area).
     */
    @Override
    public int getAbsoluteHeight() {
        return this.barHeight;
    }

}
