package de.keksuccino.spiffyhud.customization.elements.vanillalike.bossbars;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.spiffyhud.util.SizeAndPositionRecorder;
import de.keksuccino.spiffyhud.util.rendering.ElementMobilizer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.UUID;

public class VanillaLikeBossOverlayElement extends AbstractElement {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final ResourceLocation[] BAR_BACKGROUND_SPRITES = new ResourceLocation[]{new ResourceLocation("boss_bar/pink_background"), new ResourceLocation("boss_bar/blue_background"), new ResourceLocation("boss_bar/red_background"), new ResourceLocation("boss_bar/green_background"), new ResourceLocation("boss_bar/yellow_background"), new ResourceLocation("boss_bar/purple_background"), new ResourceLocation("boss_bar/white_background")};
    private static final ResourceLocation[] BAR_PROGRESS_SPRITES = new ResourceLocation[]{new ResourceLocation("boss_bar/pink_progress"), new ResourceLocation("boss_bar/blue_progress"), new ResourceLocation("boss_bar/red_progress"), new ResourceLocation("boss_bar/green_progress"), new ResourceLocation("boss_bar/yellow_progress"), new ResourceLocation("boss_bar/purple_progress"), new ResourceLocation("boss_bar/white_progress")};
    private static final ResourceLocation[] OVERLAY_BACKGROUND_SPRITES = new ResourceLocation[]{new ResourceLocation("boss_bar/notched_6_background"), new ResourceLocation("boss_bar/notched_10_background"), new ResourceLocation("boss_bar/notched_12_background"), new ResourceLocation("boss_bar/notched_20_background")};
    private static final ResourceLocation[] OVERLAY_PROGRESS_SPRITES = new ResourceLocation[]{new ResourceLocation("boss_bar/notched_6_progress"), new ResourceLocation("boss_bar/notched_10_progress"), new ResourceLocation("boss_bar/notched_12_progress"), new ResourceLocation("boss_bar/notched_20_progress")};

    private static final List<LerpingBossEvent> DUMMY_EVENTS = List.of(
            new LerpingBossEvent(UUID.randomUUID(), Component.translatable("spiffyhud.elements.dummy.boss_bars.bar"), 0.5F, BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS, false, false, false),
            new LerpingBossEvent(UUID.randomUUID(), Component.translatable("spiffyhud.elements.dummy.boss_bars.bar"), 0.5F, BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS, false, false, false),
            new LerpingBossEvent(UUID.randomUUID(), Component.translatable("spiffyhud.elements.dummy.boss_bars.bar"), 0.5F, BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS, false, false, false)
    );

    private final Minecraft minecraft = Minecraft.getInstance();

    private int barWidth = 100;
    private int barHeight = 100;
    private int barOriginalX = 0;
    private int barOriginalY = 0;

    @NotNull
    public de.keksuccino.spiffyhud.util.Alignment alignment = de.keksuccino.spiffyhud.util.Alignment.TOP_LEFT;

    public VanillaLikeBossOverlayElement(@NotNull ElementBuilder<?, ?> builder) {
        super(builder);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {

        if (this.minecraft.player == null) return;
        if (this.minecraft.level == null) return;

        //Update size and originalPos of bar before render
        this.updateBodySizeAndPosCache();

        int x = this.getAbsoluteX();
        int y = this.getAbsoluteY();
        Integer[] alignedBody = de.keksuccino.spiffyhud.util.Alignment.calculateElementBodyPosition(this.alignment, x, y, this.getAbsoluteWidth(), this.getAbsoluteHeight(), this.barWidth, this.barHeight);
        x = alignedBody[0];
        y = alignedBody[1];

        ElementMobilizer.mobilize(graphics, -this.barOriginalX, -this.barOriginalY, x, y, () -> {

            RenderSystem.enableBlend();
            RenderingUtils.resetShaderColor(graphics);

//            graphics.pose().scale(-1.0F, 1.0F, 1.0F);
//            graphics.pose().translate(-this.barWidth, 0.0F, 0.0F);

            //-------------------------------

            if (!isEditor()) {
                Minecraft.getInstance().gui.getBossOverlay().render(graphics);
            } else {
                this.renderDummyBars(graphics);
            }

            //-------------------------------

            RenderingUtils.resetShaderColor(graphics);

        });

    }

    //This is the render() method of the BossHealthOverlay, but without the rendering part
    private void updateBodySizeAndPosCache() {
        //Tweak to Vanilla logic
        SizeAndPositionRecorder recorder = new SizeAndPositionRecorder();
        recorder.setHeightOffset(5);
        //--------------------
        if (Minecraft.getInstance().gui.getBossOverlay().events.isEmpty() && !isEditor()) { //added isEditor check
            return;
        }
        int i = getScreenWidth();
        int j = 12;
        for (LerpingBossEvent lerpingBossEvent : isEditor() ? DUMMY_EVENTS : Minecraft.getInstance().gui.getBossOverlay().events.values()) { //added isEditor check
            int barX = i / 2 - 91;
            int barY = j;
            //Tweak to Vanilla logic
            recorder.updateX(barX);
            recorder.updateY(barY);
            //-----------------------
            //this.drawBar(guiGraphics, barX, barY, lerpingBossEvent);
            Component component = lerpingBossEvent.getName();
            int m = this.minecraft.font.width(component);
            int textX = i / 2 - m / 2;
            int textY = barY - 9;
            //Tweak to Vanilla logic
            recorder.updateX(textX);
            recorder.updateY(textY);
            //-----------------------
            //guiGraphics.drawString(this.minecraft.font, component, textX, textY, 0xFFFFFF);
            if ((j += 10 + this.minecraft.font.lineHeight) < getScreenHeight() / 3) continue;
            break;
        }
        //Tweak to Vanilla logic
        this.barOriginalX = recorder.getX();
        this.barOriginalY = recorder.getY();
        this.barWidth = 182;
        this.barHeight = recorder.getHeight();
        //--------------------
    }

    private void renderDummyBars(GuiGraphics graphics) {
        int i = getScreenWidth();
        int j = 12;
        for (LerpingBossEvent lerpingBossEvent : DUMMY_EVENTS) {
            int barX = i / 2 - 91;
            int barY = j;
            this.drawBar(graphics, barX, barY, lerpingBossEvent);
            Component component = lerpingBossEvent.getName();
            int m = this.minecraft.font.width(component);
            int textX = i / 2 - m / 2;
            int textY = barY - 9;
            graphics.drawString(this.minecraft.font, component, textX, textY, 0xFFFFFF);
            if ((j += 10 + this.minecraft.font.lineHeight) < getScreenHeight() / 3) continue;
            break;
        }
    }

    private void drawBar(GuiGraphics guiGraphics, int x, int y, BossEvent bossEvent) {
        this.drawBar(guiGraphics, x, y, bossEvent, 182, BAR_BACKGROUND_SPRITES, OVERLAY_BACKGROUND_SPRITES);
        int i = Mth.lerpDiscrete(bossEvent.getProgress(), 0, 182);
        if (i > 0) {
            this.drawBar(guiGraphics, x, y, bossEvent, i, BAR_PROGRESS_SPRITES, OVERLAY_PROGRESS_SPRITES);
        }
    }

    private void drawBar(GuiGraphics guiGraphics, int x, int y, BossEvent bossEvent, int progress, ResourceLocation[] barProgressSprites, ResourceLocation[] overlayProgressSprites) {
        guiGraphics.blitSprite(barProgressSprites[bossEvent.getColor().ordinal()], 182, 5, 0, 0, x, y, progress, 5);
        if (bossEvent.getOverlay() != BossEvent.BossBarOverlay.PROGRESS) {
            RenderSystem.enableBlend();
            guiGraphics.blitSprite(overlayProgressSprites[bossEvent.getOverlay().ordinal() - 1], 182, 5, 0, 0, x, y, progress, 5);
            RenderSystem.disableBlend();
        }
    }

    @Override
    public int getAbsoluteWidth() {
        return 200;
    }

    @Override
    public int getAbsoluteHeight() {
        return 60;
    }

    protected boolean isInEditor() {
        return isEditor();
    }

}
