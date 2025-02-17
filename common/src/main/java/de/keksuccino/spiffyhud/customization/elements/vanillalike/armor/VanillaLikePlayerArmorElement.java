package de.keksuccino.spiffyhud.customization.elements.vanillalike.armor;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.spiffyhud.SpiffyUtils;
import de.keksuccino.spiffyhud.util.SizeAndPositionRecorder;
import de.keksuccino.spiffyhud.util.SpiffyAlignment;
import de.keksuccino.spiffyhud.util.rendering.ElementMobilizer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VanillaLikePlayerArmorElement extends AbstractElement {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation("textures/gui/icons.png");

    private final Minecraft minecraft = Minecraft.getInstance();
    protected final RandomSource random = RandomSource.create();
    protected int tickCount;

    private int barWidth = 100;
    private int barHeight = 100;
    private int barOriginalX = 0;
    private int barOriginalY = 0;
    private boolean shouldRenderBar = false;

    @NotNull
    public SpiffyAlignment spiffyAlignment = SpiffyAlignment.TOP_LEFT;

    public VanillaLikePlayerArmorElement(@NotNull ElementBuilder<?, ?> builder) {
        super(builder);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {

        this.tickCount = SpiffyUtils.getGuiAccessor().getTickCount_Spiffy();

        if (this.minecraft.player == null) return;
        if (this.minecraft.level == null) return;

        //Update size and originalPos of bar before render
        this.shouldRenderBar = false;
        this.renderPlayerHealth(graphics);
        this.shouldRenderBar = true;

        int x = this.getAbsoluteX();
        int y = this.getAbsoluteY();
        Integer[] alignedBody = SpiffyAlignment.calculateElementBodyPosition(this.spiffyAlignment, x, y, this.getAbsoluteWidth(), this.getAbsoluteHeight(), this.barWidth, this.barHeight);
        x = alignedBody[0];
        y = alignedBody[1];

        ElementMobilizer.mobilize(graphics, -this.barOriginalX, -this.barOriginalY, x, y, () -> {

            RenderSystem.enableBlend();
            RenderingUtils.resetShaderColor(graphics);

            //-------------------------------

            this.renderPlayerHealth(graphics);

            //-------------------------------

            RenderingUtils.resetShaderColor(graphics);

        });

    }

    private void renderPlayerHealth(GuiGraphics graphics) {
        int barX;
        int barY = 0; // y is always 0, because parent render method translates it to correct position
        Player player = this.getCameraPlayer();
        if (player == null) {
            return;
        }
        this.random.setSeed(this.tickCount * 312871L);
        int m = getScreenWidth() / 2 - 91;
        int u = player.getArmorValue();
        //Tweak to Vanilla logic
        if (isEditor()) u = 10;
        //Tweak to Vanilla logic
        SizeAndPositionRecorder recorder = new SizeAndPositionRecorder();
        recorder.setWidthOffset(9);
        recorder.setHeightOffset(9);
        //------------------
        for (int w = 0; w < 10; ++w) {
            if (u <= 0) continue;
            barX = m + w * 8;
            //Tweak to Vanilla logic
            recorder.updateX(barX);
            recorder.updateY(barY);
            //----------------------
            //Tweak to Vanilla logic (if wrap)
            if (this.shouldRenderBar) {
                if (w * 2 + 1 < u) {
                    graphics.blit(GUI_ICONS_LOCATION, barX, barY, 34, 9, 9, 9);
                }
                if (w * 2 + 1 == u) {
                    graphics.blit(GUI_ICONS_LOCATION, barX, barY, 25, 9, 9, 9);
                }
                if (w * 2 + 1 <= u) continue;
                graphics.blit(GUI_ICONS_LOCATION, barX, barY, 16, 9, 9, 9);
            }
        }
        //Tweak to Vanilla logic
        this.barOriginalX = recorder.getX();
        this.barOriginalY = recorder.getY();
        this.barWidth = recorder.getWidth();
        this.barHeight = recorder.getHeight();
        //-----------------------
    }

    private Font getFont() {
        return Minecraft.getInstance().font;
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
