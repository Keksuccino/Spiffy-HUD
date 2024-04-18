package de.keksuccino.spiffyhud.customization;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.ScreenCustomization;
import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.fancymenu.util.rendering.ui.widget.RendererWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class SpiffyOverlayScreen extends Screen {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final int CUSTOM_BARS_OFFSET = -10;

    private static final ResourceLocation HOTBAR_SPRITE = new ResourceLocation("hud/hotbar");
    private static final ResourceLocation HOTBAR_SELECTION_SPRITE = new ResourceLocation("hud/hotbar_selection");
    private static final ResourceLocation HOTBAR_OFFHAND_LEFT_SPRITE = new ResourceLocation("hud/hotbar_offhand_left");
    private static final ResourceLocation HOTBAR_ATTACK_INDICATOR_BACKGROUND_SPRITE = new ResourceLocation("hud/hotbar_attack_indicator_background");
    private static final ResourceLocation HOTBAR_ATTACK_INDICATOR_PROGRESS_SPRITE = new ResourceLocation("hud/hotbar_attack_indicator_progress");
    private static final ResourceLocation JUMP_BAR_BACKGROUND_SPRITE = new ResourceLocation("hud/jump_bar_background");
    private static final ResourceLocation JUMP_BAR_PROGRESS_SPRITE = new ResourceLocation("hud/jump_bar_progress");
    private static final ResourceLocation EXPERIENCE_BAR_BACKGROUND_SPRITE = new ResourceLocation("hud/experience_bar_background");
    private static final ResourceLocation EXPERIENCE_BAR_PROGRESS_SPRITE = new ResourceLocation("hud/experience_bar_progress");

    public final boolean showFancyMenuOverlay;

    public SpiffyOverlayScreen(boolean showFancyMenuOverlay) {
        super(Component.empty());
        this.forceEnableCustomizations();
        this.showFancyMenuOverlay = showFancyMenuOverlay;
    }

    protected void forceEnableCustomizations() {
        if (!ScreenCustomization.isCustomizationEnabledForScreen(this)) {
            LOGGER.info("[SPIFFY HUD] Force-enabling customizations for SpiffyOverlayScreen..");
            ScreenCustomization.setCustomizationForScreenEnabled(this, true);
        }
    }

    @Override
    protected void init() {

        this.addRenderableWidget(this.buildHotbarWidget()).setMessage(Component.translatable("spiffyhud.elements.dummy.hotbar"));

        this.addRenderableWidget(this.buildJumpMeterWidget()).setMessage(Component.translatable("spiffyhud.elements.dummy.jump_meter"));

        this.addRenderableWidget(this.buildExperienceBarWidget()).setMessage(Component.translatable("spiffyhud.elements.dummy.experience_bar"));

    }

    @Override
    public void renderBackground(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        //empty
    }

    protected RendererWidget buildHotbarWidget() {

        int screenCenter = this.width / 2;
        int hotbarWidgetX = screenCenter - 91 - 29;
        int hotbarWidgetY = this.height - 22;
        int hotbarWidgetWidth = 182 + 29;
        int hotbarWidgetHeight = 22;

        return new RendererWidget(hotbarWidgetX, hotbarWidgetY, hotbarWidgetWidth, hotbarWidgetHeight,
                (graphics, mouseX, mouseY, partial, x, y, width, height, widget) -> {

                    if (!(Minecraft.getInstance().screen instanceof LayoutEditorScreen)) return;

                    RenderSystem.enableBlend();

                    graphics.pose().pushPose();
                    graphics.pose().translate(0.0f, 0.0f, -90.0f);
                    graphics.blitSprite(HOTBAR_SPRITE, screenCenter - 91, this.height - 22, 182, 22);
                    graphics.blitSprite(HOTBAR_SELECTION_SPRITE, screenCenter - 91 - 1 + 20, this.height - 22 - 1, 24, 23);
                    graphics.blitSprite(HOTBAR_OFFHAND_LEFT_SPRITE, screenCenter - 91 - 29, this.height - 23, 29, 24);
                    graphics.pose().popPose();

                    //Force-render attack indicator in hotbar
                    float f = 0.5F;
                    int n = this.height - 20;
                    int o = screenCenter + 91 + 6;
                    int p = (int)(f * 19.0f);
                    graphics.blitSprite(HOTBAR_ATTACK_INDICATOR_BACKGROUND_SPRITE, o, n, 18, 18);
                    graphics.blitSprite(HOTBAR_ATTACK_INDICATOR_PROGRESS_SPRITE, 18, 18, 0, 18 - p, o, n + 18 - p, 18, p);

                    RenderingUtils.resetShaderColor(graphics);

                }
        ).setWidgetIdentifierFancyMenu("spiffy_hotbar_dummy");

    }

    protected RendererWidget buildJumpMeterWidget() {

        int screenCenter = this.width / 2;
        int widgetX = screenCenter - 91;
        int widgetY = this.height - 32 + 3 + CUSTOM_BARS_OFFSET;
        int widgetWidth = 182;
        int widgetHeight = 5;

        return new RendererWidget(widgetX, widgetY, widgetWidth, widgetHeight,
                (graphics, mouseX, mouseY, partial, x, y, width, height, widget) -> {

                    if (!(Minecraft.getInstance().screen instanceof LayoutEditorScreen)) return;
                    RenderSystem.enableBlend();
                    float jumpProgress = 0.5F;
                    int progressWidth = (int)(jumpProgress * 183.0f);
                    RenderSystem.enableBlend();
                    graphics.blitSprite(JUMP_BAR_BACKGROUND_SPRITE, x, y, width, height);
                    graphics.blitSprite(JUMP_BAR_PROGRESS_SPRITE, width, height, 0, 0, x, y, progressWidth, 5);
                    RenderingUtils.resetShaderColor(graphics);

                }
        ).setWidgetIdentifierFancyMenu("spiffy_jump_meter_dummy");

    }

    protected RendererWidget buildExperienceBarWidget() {

        int screenCenter = this.width / 2;
        int widgetX = screenCenter - 91;
        int widgetY = this.height - 32 + 3;
        int widgetWidth = 182;
        int widgetHeight = 5;

        return new RendererWidget(widgetX, widgetY, widgetWidth, widgetHeight,
                (graphics, mouseX, mouseY, partial, x, y, width, height, widget) -> {

                    if (!(Minecraft.getInstance().screen instanceof LayoutEditorScreen)) return;
                    RenderSystem.enableBlend();
                    Font font = Minecraft.getInstance().font;
                    int progressWidth = (int)(0.5F * 183.0f);
                    graphics.blitSprite(EXPERIENCE_BAR_BACKGROUND_SPRITE, x, y, width, height);
                    graphics.blitSprite(EXPERIENCE_BAR_PROGRESS_SPRITE, width, height, 0, 0, x, y, progressWidth, 5);
                    String levelString = "42";
                    progressWidth = (this.width - font.width(levelString)) / 2;
                    height = height - 8;
                    graphics.drawString(font, levelString, progressWidth + 1, height, 0, false);
                    graphics.drawString(font, levelString, progressWidth - 1, height, 0, false);
                    graphics.drawString(font, levelString, progressWidth, height + 1, 0, false);
                    graphics.drawString(font, levelString, progressWidth, height - 1, 0, false);
                    graphics.drawString(font, levelString, progressWidth, height, 8453920, false);
                    RenderingUtils.resetShaderColor(graphics);

                }
        ).setWidgetIdentifierFancyMenu("spiffy_experience_bar_dummy");

    }

}
