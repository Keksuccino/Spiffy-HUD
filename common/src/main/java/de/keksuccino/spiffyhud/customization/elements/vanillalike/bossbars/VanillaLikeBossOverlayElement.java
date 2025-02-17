package de.keksuccino.spiffyhud.customization.elements.vanillalike.bossbars;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.spiffyhud.util.SizeAndPositionRecorder;
import de.keksuccino.spiffyhud.util.SpiffyAlignment;
import de.keksuccino.spiffyhud.util.rendering.ElementMobilizer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.BossEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.UUID;

public class VanillaLikeBossOverlayElement extends AbstractElement {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final ResourceLocation GUI_BARS_LOCATION = new ResourceLocation("textures/gui/bars.png");

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
    public SpiffyAlignment spiffyAlignment = SpiffyAlignment.TOP_LEFT;

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
        Integer[] alignedBody = SpiffyAlignment.calculateElementBodyPosition(this.spiffyAlignment, x, y, this.getAbsoluteWidth(), this.getAbsoluteHeight(), this.barWidth, this.barHeight);
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
        this.drawBar(guiGraphics, x, y, bossEvent, 182, 0);
        int i = (int)(bossEvent.getProgress() * 183.0f);
        if (i > 0) {
            this.drawBar(guiGraphics, x, y, bossEvent, i, 5);
        }
    }

    private void drawBar(GuiGraphics guiGraphics, int x, int y, BossEvent bossEvent, int width, int i) {
        guiGraphics.blit(GUI_BARS_LOCATION, x, y, 0, bossEvent.getColor().ordinal() * 5 * 2 + i, width, 5);
        if (bossEvent.getOverlay() != BossEvent.BossBarOverlay.PROGRESS) {
            RenderSystem.enableBlend();
            guiGraphics.blit(GUI_BARS_LOCATION, x, y, 0, 80 + (bossEvent.getOverlay().ordinal() - 1) * 5 * 2 + i, width, 5);
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
