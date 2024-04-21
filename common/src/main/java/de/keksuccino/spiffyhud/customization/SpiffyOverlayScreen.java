package de.keksuccino.spiffyhud.customization;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.ScreenCustomization;
import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.fancymenu.util.rendering.ui.widget.RendererWidget;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class SpiffyOverlayScreen extends Screen {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final ResourceLocation CROSSHAIR_SPRITE = new ResourceLocation("hud/crosshair");
    private static final ResourceLocation CROSSHAIR_ATTACK_INDICATOR_BACKGROUND_SPRITE = new ResourceLocation("hud/crosshair_attack_indicator_background");
    private static final ResourceLocation CROSSHAIR_ATTACK_INDICATOR_PROGRESS_SPRITE = new ResourceLocation("hud/crosshair_attack_indicator_progress");
    private static final ResourceLocation HOTBAR_SPRITE = new ResourceLocation("hud/hotbar");
    private static final ResourceLocation HOTBAR_SELECTION_SPRITE = new ResourceLocation("hud/hotbar_selection");
    private static final ResourceLocation HOTBAR_OFFHAND_LEFT_SPRITE = new ResourceLocation("hud/hotbar_offhand_left");
    private static final ResourceLocation HOTBAR_ATTACK_INDICATOR_BACKGROUND_SPRITE = new ResourceLocation("hud/hotbar_attack_indicator_background");
    private static final ResourceLocation HOTBAR_ATTACK_INDICATOR_PROGRESS_SPRITE = new ResourceLocation("hud/hotbar_attack_indicator_progress");
    private static final ResourceLocation JUMP_BAR_BACKGROUND_SPRITE = new ResourceLocation("hud/jump_bar_background");
    private static final ResourceLocation JUMP_BAR_PROGRESS_SPRITE = new ResourceLocation("hud/jump_bar_progress");
    private static final ResourceLocation EXPERIENCE_BAR_BACKGROUND_SPRITE = new ResourceLocation("hud/experience_bar_background");
    private static final ResourceLocation EXPERIENCE_BAR_PROGRESS_SPRITE = new ResourceLocation("hud/experience_bar_progress");
    private static final ResourceLocation ARMOR_FULL_SPRITE = new ResourceLocation("hud/armor_full");
    private static final ResourceLocation FOOD_EMPTY_SPRITE = new ResourceLocation("hud/food_empty");
    private static final ResourceLocation FOOD_FULL_SPRITE = new ResourceLocation("hud/food_full");
    private static final ResourceLocation AIR_SPRITE = new ResourceLocation("hud/air");
    private static final ResourceLocation HEART_VEHICLE_CONTAINER_SPRITE = new ResourceLocation("hud/heart/vehicle_container");
    private static final ResourceLocation HEART_VEHICLE_FULL_SPRITE = new ResourceLocation("hud/heart/vehicle_full");
    private static final ResourceLocation HEART_PLAYER_CONTAINER_SPRITE = new ResourceLocation("hud/heart/container");
    private static final ResourceLocation HEART_PLAYER_FULL_NORMAL_SPRITE = new ResourceLocation("hud/heart/full");
    private static final ResourceLocation BOSS_BAR_BACKGROUND_SPRITE = new ResourceLocation("boss_bar/pink_background");
    private static final ResourceLocation BOSS_BAR_PROGRESS_SPRITE = new ResourceLocation("boss_bar/pink_progress");
    private static final ResourceLocation EFFECT_BACKGROUND_SPRITE = new ResourceLocation("hud/effect_background");

    public final boolean showFancyMenuOverlay;
    private float overlayMessageTime = 60;

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

        this.addRenderableWidget(this.buildSelectedItemNameWidget()).setMessage(Component.translatable("spiffyhud.elements.dummy.selected_item_name"));

        this.addRenderableWidget(this.buildScoreboardSidebarWidget()).setMessage(Component.translatable("spiffyhud.elements.dummy.scoreboard_sidebar"));

        this.addRenderableWidget(this.buildFoodBarWidget()).setMessage(Component.translatable("spiffyhud.elements.dummy.food_bar"));

        this.addRenderableWidget(this.buildArmorBarWidget()).setMessage(Component.translatable("spiffyhud.elements.dummy.armor_bar"));

        this.addRenderableWidget(this.buildAirBarWidget()).setMessage(Component.translatable("spiffyhud.elements.dummy.air_bar"));

        this.addRenderableWidget(this.buildHealthBarWidget()).setMessage(Component.translatable("spiffyhud.elements.dummy.health_bar"));

        this.addRenderableWidget(this.buildMountHealthBarWidget()).setMessage(Component.translatable("spiffyhud.elements.dummy.mount_health_bar"));

        this.addRenderableWidget(this.buildOverlayMessageWidget()).setMessage(Component.translatable("spiffyhud.elements.dummy.overlay_message"));

        this.addRenderableWidget(this.buildCrosshairWidget()).setMessage(Component.translatable("spiffyhud.elements.dummy.crosshair"));

        this.addRenderableWidget(this.buildAttackIndicatorWidget()).setMessage(Component.translatable("spiffyhud.elements.dummy.attack_indicator"));

        this.addRenderableWidget(this.buildTitleWidget()).setMessage(Component.translatable("spiffyhud.elements.dummy.title"));

        this.addRenderableWidget(this.buildSubtitleWidget()).setMessage(Component.translatable("spiffyhud.elements.dummy.subtitle"));

        this.addRenderableWidget(this.buildBossBarWidget()).setMessage(Component.translatable("spiffyhud.elements.dummy.boss_bars"));

        this.addRenderableWidget(this.buildEffectsWidget()).setMessage(Component.translatable("spiffyhud.elements.dummy.effects"));

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

                    graphics.blitSprite(HOTBAR_SPRITE, screenCenter - 91, this.height - 22, 182, 22);
                    graphics.blitSprite(HOTBAR_SELECTION_SPRITE, screenCenter - 91 - 1 + 20, this.height - 22 - 1, 24, 23);
                    graphics.blitSprite(HOTBAR_OFFHAND_LEFT_SPRITE, screenCenter - 91 - 29, this.height - 23, 29, 24);

                    RenderingUtils.resetShaderColor(graphics);

                }
        ).setWidgetIdentifierFancyMenu(VanillaHudElements.HOTBAR_IDENTIFIER);

    }

    protected RendererWidget buildJumpMeterWidget() {

        int screenCenter = this.width / 2;
        int widgetX = screenCenter - 91;
        int widgetY = (this.height - 32 + 3) - 5; // -5 is a custom offset to move the jump meter above the exp bar, so they don't overlap
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
        ).setWidgetIdentifierFancyMenu(VanillaHudElements.JUMP_METER_IDENTIFIER);

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

                    //Render EXP bar
                    int progressWidth = (int)(0.5F * 183.0f);
                    graphics.blitSprite(EXPERIENCE_BAR_BACKGROUND_SPRITE, x, y, width, height);
                    graphics.blitSprite(EXPERIENCE_BAR_PROGRESS_SPRITE, width, height, 0, 0, x, y, progressWidth, 5);

                    //Render level text
                    RenderSystem.enableBlend();
                    String levelString = "42";
                    int textX = x + (width / 2) - (font.width(levelString) / 2);
                    int textY = y - 6;
                    Font font = Minecraft.getInstance().font;
                    graphics.drawString(font, levelString, textX + 1, textY, 0, false);
                    graphics.drawString(font, levelString, textX - 1, textY, 0, false);
                    graphics.drawString(font, levelString, textX, textY + 1, 0, false);
                    graphics.drawString(font, levelString, textX, textY - 1, 0, false);
                    graphics.drawString(font, levelString, textX, textY, 8453920, false);

                    RenderingUtils.resetShaderColor(graphics);

                }
        ).setWidgetIdentifierFancyMenu(VanillaHudElements.EXPERIENCE_BAR_IDENTIFIER);

    }

    protected RendererWidget buildSelectedItemNameWidget() {

        Font font = Minecraft.getInstance().font;
        MutableComponent mutableComponent = Component.empty().append(Component.translatable("spiffyhud.elements.dummy.selected_item_name")).withStyle(ChatFormatting.LIGHT_PURPLE);
        int textWidth = font.width(mutableComponent);
        int textX = (this.width - textWidth) / 2;
        int textY = (this.height - 59) - 18; // -18 is a custom offset to move the text above the bars that got moved up because of the Jump Meter

        return new RendererWidget(textX, textY, textWidth, font.lineHeight,
                (graphics, mouseX, mouseY, partial, x, y, width, height, widget) -> {

                    if (!(Minecraft.getInstance().screen instanceof LayoutEditorScreen)) return;
                    RenderSystem.enableBlend();

                    graphics.fill(textX - 2, textY - 2, textX + textWidth + 2, textY + font.lineHeight + 2, Minecraft.getInstance().options.getBackgroundColor(0));
                    graphics.drawString(font, mutableComponent, textX, textY, -1);

                    RenderingUtils.resetShaderColor(graphics);

                }
        ).setWidgetIdentifierFancyMenu(VanillaHudElements.SELECTED_ITEM_NAME_IDENTIFIER);

    }

    protected RendererWidget buildScoreboardSidebarWidget() {

        String spacer = ": ";
        Minecraft minecraft = Minecraft.getInstance();
        Font font = Minecraft.getInstance().font;

        //Build line entries
        record DisplayEntry(Component name, Component score, int scoreWidth) {}
        List<DisplayEntry> entryList = new ArrayList<>();
        Component name = Component.translatable("spiffyhud.elements.dummy.scoreboard_sidebar.line");
        Component score = Component.literal("0").withStyle(ChatFormatting.RED);
        int scoreWidth = font.width(score);
        entryList.add(new DisplayEntry(Component.empty(), Component.empty(), scoreWidth));
        entryList.add(new DisplayEntry(name, score, scoreWidth));
        entryList.add(new DisplayEntry(name, score, scoreWidth));
        entryList.add(new DisplayEntry(name, score, scoreWidth));
        entryList.add(new DisplayEntry(name, score, scoreWidth));
        entryList.add(new DisplayEntry(name, score, scoreWidth));
        entryList.add(new DisplayEntry(name, score, scoreWidth));
        DisplayEntry[] entries = entryList.toArray(new DisplayEntry[0]);

        Component title = Component.translatable("spiffyhud.elements.dummy.scoreboard_sidebar.title").withStyle(ChatFormatting.BOLD);
        int titleWidth = font.width(title);
        int totalSidebarWidth = titleWidth;
        int spacerWidth = font.width(spacer);
        for (DisplayEntry entry : entries) {
            totalSidebarWidth = Math.max(totalSidebarWidth, font.width(entry.name) + (entry.scoreWidth > 0 ? spacerWidth + entry.scoreWidth : 0));
        }
        int linesCount = entries.length;
        int linesHeight = linesCount * font.lineHeight;
        int sidebarYEnd = this.height / 2 + linesHeight / 3;
        int sidebarXStart = this.width - totalSidebarWidth - 3;
        int sidebarXEnd = this.width - 3 + 2;
        int backgroundColorNormal = minecraft.options.getBackgroundColor(0.3f);
        int backgroundColorTitle = minecraft.options.getBackgroundColor(0.4f);
        int sidebarYBase = sidebarYEnd - linesCount * font.lineHeight;
        int finalTotalSidebarWidth = totalSidebarWidth;
        int sidebarY = sidebarYBase - font.lineHeight - 1;
        int sidebarHeight = Math.max(1, sidebarYEnd - sidebarY);

        return new RendererWidget(sidebarXStart, sidebarY, totalSidebarWidth, sidebarHeight,
                (graphics, mouseX, mouseY, partial, x, y, width, height, widget) -> {

                    if (!(Minecraft.getInstance().screen instanceof LayoutEditorScreen)) return;
                    RenderSystem.enableBlend();

                    //Render title background
                    graphics.fill(sidebarXStart - 2, sidebarYBase - font.lineHeight - 1, sidebarXEnd, sidebarYBase - 1, backgroundColorTitle);
                    //Render lines background
                    graphics.fill(sidebarXStart - 2, sidebarYBase - 1, sidebarXEnd, sidebarYEnd, backgroundColorNormal);
                    //Render title
                    graphics.drawString(font, title, sidebarXStart + finalTotalSidebarWidth / 2 - titleWidth / 2, sidebarYBase - font.lineHeight, -1, false);
                    //Render lines
                    for (int t = 0; t < linesCount; ++t) {
                        DisplayEntry entry = entries[t];
                        int u = sidebarYEnd - (linesCount - t) * font.lineHeight;
                        graphics.drawString(font, entry.name, sidebarXStart, u, -1, false);
                        graphics.drawString(font, entry.score, sidebarXEnd - entry.scoreWidth, u, -1, false);
                    }

                    RenderingUtils.resetShaderColor(graphics);

                }
        ).setWidgetIdentifierFancyMenu(VanillaHudElements.SCOREBOARD_SIDEBAR_IDENTIFIER);

    }

    protected RendererWidget buildFoodBarWidget() {

        int barX = this.width / 2 + 91;
        int barY = (this.height - 39) - 5; // -5 is a custom offset to move it above the Jump Meter

        return new RendererWidget(barX - 90, barY, 90, 9,
                (graphics, mouseX, mouseY, partial, x, y, width, height, widget) -> {

                    if (!(Minecraft.getInstance().screen instanceof LayoutEditorScreen)) return;
                    RenderSystem.enableBlend();

                    for (int i = 0; i < 10; ++i) {
                        int offsetX = barX - i * 8 - 9;
                        graphics.blitSprite(FOOD_EMPTY_SPRITE, offsetX, barY, 9, 9);
                        graphics.blitSprite(FOOD_FULL_SPRITE, offsetX, barY, 9, 9);
                    }

                    RenderingUtils.resetShaderColor(graphics);

                }
        ).setWidgetIdentifierFancyMenu(VanillaHudElements.FOOD_BAR_IDENTIFIER);

    }

    protected RendererWidget buildArmorBarWidget() {

        int barX = this.width / 2 - 91;
        int barY = (this.height - 39 - 10) - 5; // -5 is a custom offset to move it above the Jump Meter

        return new RendererWidget(barX, barY, 90, 9,
                (graphics, mouseX, mouseY, partial, x, y, width, height, widget) -> {

                    if (!(Minecraft.getInstance().screen instanceof LayoutEditorScreen)) return;
                    RenderSystem.enableBlend();

                    for (int i = 0; i < 10; ++i) {
                        int offsetX = barX + i * 8;
                        graphics.blitSprite(ARMOR_FULL_SPRITE, offsetX, barY, 9, 9);
                    }

                    RenderingUtils.resetShaderColor(graphics);

                }
        ).setWidgetIdentifierFancyMenu(VanillaHudElements.ARMOR_BAR_IDENTIFIER);

    }

    protected RendererWidget buildAirBarWidget() {

        int barX = this.width / 2 + 91;
        int barY = (this.height - 39 - 10) - 5; // -5 is a custom offset to move it above the Jump Meter

        return new RendererWidget(barX - 90, barY, 90, 9,
                (graphics, mouseX, mouseY, partial, x, y, width, height, widget) -> {

                    if (!(Minecraft.getInstance().screen instanceof LayoutEditorScreen)) return;
                    RenderSystem.enableBlend();

                    for (int i = 0; i < 10; ++i) {
                        int offsetX = barX - i * 8 - 9;
                        graphics.blitSprite(AIR_SPRITE, offsetX, barY, 9, 9);
                    }

                    RenderingUtils.resetShaderColor(graphics);

                }
        ).setWidgetIdentifierFancyMenu(VanillaHudElements.AIR_BAR_IDENTIFIER);

    }

    protected RendererWidget buildMountHealthBarWidget() {

        int barX = this.width / 2 + 91;
        int barY = (this.height - 39 - 10) - 15; // -15 is a custom offset to move it above the Jump Meter and the other bars that got moved up because of it

        return new RendererWidget(barX - 90, barY, 90, 9,
                (graphics, mouseX, mouseY, partial, x, y, width, height, widget) -> {

                    if (!(Minecraft.getInstance().screen instanceof LayoutEditorScreen)) return;
                    RenderSystem.enableBlend();

                    for (int i = 0; i < 10; ++i) {
                        int offsetX = barX - i * 8 - 9;
                        graphics.blitSprite(HEART_VEHICLE_CONTAINER_SPRITE, offsetX, barY, 9, 9);
                        graphics.blitSprite(HEART_VEHICLE_FULL_SPRITE, offsetX, barY, 9, 9);
                    }

                    RenderingUtils.resetShaderColor(graphics);

                }
        ).setWidgetIdentifierFancyMenu(VanillaHudElements.MOUNT_HEALTH_BAR_IDENTIFIER);

    }

    protected RendererWidget buildHealthBarWidget() {

        int barX = this.width / 2 - 91;
        int barY = (this.height - 39) - 5; // -5 is a custom offset to move it above the Jump Meter

        return new RendererWidget(barX, barY, 90, 9,
                (graphics, mouseX, mouseY, partial, x, y, width, height, widget) -> {

                    if (!(Minecraft.getInstance().screen instanceof LayoutEditorScreen)) return;
                    RenderSystem.enableBlend();

                    for (int w = 0; w < 10; ++w) {
                        int i = barX + w * 8;
                        graphics.blitSprite(HEART_PLAYER_CONTAINER_SPRITE, i, barY, 9, 9);
                        graphics.blitSprite(HEART_PLAYER_FULL_NORMAL_SPRITE, i, barY, 9, 9);
                    }

                    RenderingUtils.resetShaderColor(graphics);

                }
        ).setWidgetIdentifierFancyMenu(VanillaHudElements.PLAYER_HEALTH_BAR_IDENTIFIER);

    }

    protected RendererWidget buildOverlayMessageWidget() {

        Font font = Minecraft.getInstance().font;
        MutableComponent message = Component.translatable("spiffyhud.elements.dummy.overlay_message");
        int messageWidth = font.width(message);
        int textX = (this.width / 2) - (messageWidth / 2);
        int textY = ((this.height - 68) - 4) - 18; // -18 is a custom offset to move the text above the other elements that got moved up because of showing more elements at once than normal

        return new RendererWidget(textX - 2, textY - 2, messageWidth + 4, font.lineHeight + 4,
                (graphics, mouseX, mouseY, partial, x, y, width, height, widget) -> {

                    if (!(Minecraft.getInstance().screen instanceof LayoutEditorScreen)) return;
                    RenderSystem.enableBlend();

                    float h = this.overlayMessageTime - partial;
                    int animatedTextColor = -1;
                    try {
                       animatedTextColor = Mth.hsvToRgb(Math.max(0.0f, h / 50.0f), 0.7f, 0.6f) & 0xFFFFFF;
                    } catch (Exception ex) {
                        LOGGER.error("[SPIFFY HUD] Failed to calculate animated overlay message color!", ex);
                    }

                    //Render text
                    graphics.drawString(font, message, textX, textY, animatedTextColor);

                    //Tick overlay message time here because Screen#tick() doesn't work in the editor
                    if (this.overlayMessageTime > 1) {
                        this.overlayMessageTime = this.overlayMessageTime - 0.25F; //render ticks faster than Screen#tick(), so subtract 0.25F instead of 1.0F
                    } else {
                        this.overlayMessageTime = 60;
                    }

                    RenderingUtils.resetShaderColor(graphics);

                }
        ).setWidgetIdentifierFancyMenu(VanillaHudElements.OVERLAY_MESSAGE_IDENTIFIER);

    }

    protected RendererWidget buildCrosshairWidget() {

        int crosshairX = (this.width - 15) / 2;
        int crosshairY = (this.height - 15) / 2;

        return new RendererWidget(crosshairX, crosshairY, 15, 15,
                (graphics, mouseX, mouseY, partial, x, y, width, height, widget) -> {

                    if (!(Minecraft.getInstance().screen instanceof LayoutEditorScreen)) return;
                    RenderSystem.enableBlend();

                    RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                    graphics.blitSprite(CROSSHAIR_SPRITE, crosshairX, crosshairY, 15, 15);
                    RenderSystem.defaultBlendFunc();

                    RenderingUtils.resetShaderColor(graphics);

                }
        ).setWidgetIdentifierFancyMenu(VanillaHudElements.CROSSHAIR_IDENTIFIER);

    }

    protected RendererWidget buildAttackIndicatorWidget() {

        float attackStrength = 0.5F;
        int indicatorX = (this.width / 2) + 1;
        int indicatorY = (this.height / 2 - 7 + 16) + 40;
        int hotIndicatorX = indicatorX - 2 - 18;
        int hotIndicatorY = indicatorY - 6;
        int progressSpriteWidth = (int)(attackStrength * 17.0f);

        return new RendererWidget(hotIndicatorX, hotIndicatorY, 18 + 2 + 16, 18,
                (graphics, mouseX, mouseY, partial, x, y, width, height, widget) -> {

                    if (!(Minecraft.getInstance().screen instanceof LayoutEditorScreen)) return;
                    RenderSystem.enableBlend();

                    RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                    graphics.blitSprite(CROSSHAIR_ATTACK_INDICATOR_BACKGROUND_SPRITE, indicatorX, indicatorY, 16, 4);
                    graphics.blitSprite(CROSSHAIR_ATTACK_INDICATOR_PROGRESS_SPRITE, 16, 4, 0, 0, indicatorX, indicatorY, progressSpriteWidth, 4);
                    RenderSystem.defaultBlendFunc();

                    int hotProgress = 10;
                    graphics.blitSprite(HOTBAR_ATTACK_INDICATOR_BACKGROUND_SPRITE, hotIndicatorX, hotIndicatorY, 18, 18);
                    graphics.blitSprite(HOTBAR_ATTACK_INDICATOR_PROGRESS_SPRITE, 18, 18, 0, 18 - hotProgress, hotIndicatorX, hotIndicatorY + 18 - hotProgress, 18, hotProgress);

                    RenderingUtils.resetShaderColor(graphics);

                }
        ).setWidgetIdentifierFancyMenu(VanillaHudElements.ATTACK_INDICATOR_IDENTIFIER);

    }

    protected RendererWidget buildTitleWidget() {

        Font font = Minecraft.getInstance().font;
        Component title = Component.translatable("spiffyhud.elements.dummy.title");
        int titleWidth = font.width(title);
        int totalWidth = titleWidth * 4;
        int totalHeight = font.lineHeight * 4;
        int textX = (this.width / 2) - (totalWidth / 2);
        int textY = (this.height / 2) - (12 * 4);

        return new RendererWidget(textX, textY, totalWidth, totalHeight,
                (graphics, mouseX, mouseY, partial, x, y, width, height, widget) -> {

                    if (!(Minecraft.getInstance().screen instanceof LayoutEditorScreen)) return;
                    RenderSystem.enableBlend();

                    graphics.pose().pushPose();
                    graphics.pose().translate((float)this.width / 2, (float)this.height / 2, 0.0f);

                    RenderSystem.enableBlend();

                    //Render title
                    graphics.pose().pushPose();
                    graphics.pose().scale(4.0f, 4.0f, 4.0f);
                    graphics.drawString(font, title, -titleWidth / 2, -12, -1);
                    graphics.pose().popPose();

                    graphics.pose().popPose();

                    RenderingUtils.resetShaderColor(graphics);

                }
        ).setWidgetIdentifierFancyMenu(VanillaHudElements.TITLE_IDENTIFIER);

    }

    protected RendererWidget buildSubtitleWidget() {

        Font font = Minecraft.getInstance().font;
        Component subtitle = Component.translatable("spiffyhud.elements.dummy.subtitle");
        int subtitleWidth = font.width(subtitle);
        int totalWidth = subtitleWidth * 2;
        int totalHeight = font.lineHeight * 2;
        int textX = (this.width / 2) - (totalWidth / 2);
        int textY = (this.height / 2) + (6 * 2);

        return new RendererWidget(textX, textY, totalWidth, totalHeight,
                (graphics, mouseX, mouseY, partial, x, y, width, height, widget) -> {

                    if (!(Minecraft.getInstance().screen instanceof LayoutEditorScreen)) return;
                    RenderSystem.enableBlend();

                    graphics.pose().pushPose();
                    graphics.pose().translate((float)this.width / 2, (float)this.height / 2, 0.0f);

                    RenderSystem.enableBlend();

                    //Render subtitle
                    graphics.pose().pushPose();
                    graphics.pose().scale(2.0f, 2.0f, 2.0f);
                    graphics.drawString(font, subtitle, -subtitleWidth / 2, 6, -1);
                    graphics.pose().popPose();

                    graphics.pose().popPose();

                    RenderingUtils.resetShaderColor(graphics);

                }
        ).setWidgetIdentifierFancyMenu(VanillaHudElements.SUBTITLE_IDENTIFIER);

    }

    protected RendererWidget buildBossBarWidget() {

        Font font = Minecraft.getInstance().font;
        Component bossName = Component.translatable("spiffyhud.elements.dummy.boss_bars.bar");
        int bossNameWidth = font.width(bossName);
        int barX = this.width / 2 - 91;
        int barY = 12;
        int totalY = barY - 9;
        int totalHeight = 53;

        return new RendererWidget(barX, totalY, 182, totalHeight,
                (graphics, mouseX, mouseY, partial, x, y, width, height, widget) -> {

                    if (!(Minecraft.getInstance().screen instanceof LayoutEditorScreen)) return;
                    RenderSystem.enableBlend();

                    int y2 = barY;
                    for (int i = 0; i < 3; i++) {

                        //Draw bar
                        graphics.blitSprite(BOSS_BAR_BACKGROUND_SPRITE, 182, 5, 0, 0, barX, y2, 182, 5);
                        //Draw progress
                        graphics.blitSprite(BOSS_BAR_PROGRESS_SPRITE, 182, 5, 0, 0, barX, y2, 182 / 2, 5);

                        int n = this.width / 2 - bossNameWidth / 2;
                        int o = y2 - 9;
                        graphics.drawString(font, bossName, n, o, -1);

                        y2 += 10 + font.lineHeight;

                    }

                    RenderingUtils.resetShaderColor(graphics);

                }
        ).setWidgetIdentifierFancyMenu(VanillaHudElements.BOSS_BARS_IDENTIFIER);

    }

    protected RendererWidget buildEffectsWidget() {

        List<MobEffectInstance> effects = List.of(
                new MobEffectInstance(MobEffects.HEAL),
                new MobEffectInstance(MobEffects.FIRE_RESISTANCE),
                new MobEffectInstance(MobEffects.LUCK),
                new MobEffectInstance(MobEffects.BAD_OMEN),
                new MobEffectInstance(MobEffects.CONFUSION));
        int effectsWidth = 25 * 3;
        int effectsHeight = 24 + 2 + 24; //two rows
        int effectsX = this.width - effectsWidth;
        int effectsY = 1;

        return new RendererWidget(effectsX, effectsY, effectsWidth, effectsHeight,
                (graphics, mouseX, mouseY, partial, x, y, width, height, widget) -> {

                    if (!(Minecraft.getInstance().screen instanceof LayoutEditorScreen)) return;
                    RenderSystem.enableBlend();

                    int i = 0;
                    int j = 0;
                    MobEffectTextureManager mobEffectTextureManager = Minecraft.getInstance().getMobEffectTextures();
                    ArrayList<Runnable> list = Lists.newArrayListWithExpectedSize(effects.size());
                    for (MobEffectInstance mobEffectInstance : Ordering.natural().reverse().sortedCopy(effects)) {
                        MobEffect mobEffect = mobEffectInstance.getEffect();
                        if (!mobEffectInstance.showIcon()) continue;
                        int k = this.width;
                        int l = 1;
                        if (mobEffect.isBeneficial()) {
                            k -= 25 * ++i;
                        } else {
                            k -= 25 * ++j;
                            l += 26;
                        }
                        float f = 1.0f;
                        int n;
                        graphics.blitSprite(EFFECT_BACKGROUND_SPRITE, k, l, 24, 24);
                        TextureAtlasSprite textureAtlasSprite = mobEffectTextureManager.get(mobEffect);
                        n = k;
                        int o = l;
                        int finalN = n;
                        list.add(() -> {
                            graphics.setColor(1.0f, 1.0f, 1.0f, f);
                            graphics.blit(finalN + 3, o + 3, 0, 18, 18, textureAtlasSprite);
                            graphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
                        });
                    }
                    list.forEach(Runnable::run);

                    RenderingUtils.resetShaderColor(graphics);

                }
        ).setWidgetIdentifierFancyMenu(VanillaHudElements.EFFECTS_IDENTIFIER);

    }

}
