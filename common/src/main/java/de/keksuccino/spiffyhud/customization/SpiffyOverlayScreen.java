package de.keksuccino.spiffyhud.customization;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.ScreenCustomization;
import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.fancymenu.util.rendering.ui.widget.RendererWidget;
import de.keksuccino.spiffyhud.util.SizeAndPositionRecorder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SpiffyOverlayScreen extends Screen {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation("textures/gui/widgets.png");
    private static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation("textures/gui/icons.png");
    private static final ResourceLocation BARS_LOCATION = new ResourceLocation("textures/gui/bars.png");

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
    public void renderBackground(@NotNull GuiGraphics $$0) {
    }

    protected RendererWidget buildHotbarWidget() {
        int screenCenter = this.width / 2;
        int x = screenCenter - 91;
        int y = this.height - 22;
        int widgetWidth = 182;
        int widgetHeight = 22;
        return new SpiffyRendererWidget(x, y, widgetWidth, widgetHeight, (graphics, mX, mY, partial, gx, gy, gwidth, gheight, widget) -> {
            RenderSystem.enableBlend();
            // Draw the hotbar background from widgets.png (u=0,v=0,size 182x22)
            graphics.blit(WIDGETS_LOCATION, screenCenter - 91, this.height - 22, 0, 0, 182, 22, 256, 256);
            // Draw selection overlay (from widgets.png, u=0,v=22,size 24x22) for the selected slot
            int selected = Minecraft.getInstance().player.getInventory().selected;
            graphics.blit(WIDGETS_LOCATION, screenCenter - 91 - 1 + selected * 20, this.height - 22 - 1, 0, 22, 24, 22, 256, 256);
            // Draw offhand element if available (using u=24,v=22 or u=53,v=22 depending on main arm)
            if (!Minecraft.getInstance().player.getOffhandItem().isEmpty()) {
                boolean leftHanded = Minecraft.getInstance().player.getMainArm() == HumanoidArm.LEFT;
                if (leftHanded) {
                    graphics.blit(WIDGETS_LOCATION, screenCenter - 91 - 29, this.height - 23, 24, 22, 29, 24, 256, 256);
                } else {
                    graphics.blit(WIDGETS_LOCATION, screenCenter + 91, this.height - 23, 53, 22, 29, 24, 256, 256);
                }
            }
            RenderingUtils.resetShaderColor(graphics);
        }).setWidgetIdentifierFancyMenu(VanillaHudElements.HOTBAR_IDENTIFIER);
    }

    protected RendererWidget buildJumpMeterWidget() {
        int screenCenter = this.width / 2;
        int x = screenCenter - 91;
        int y = (this.height - 32 + 3) - 5; // custom offset
        int width = 182;
        int height = 5;
        return new SpiffyRendererWidget(x, y, width, height, (graphics, mX, mY, partial, gx, gy, gwidth, gheight, widget) -> {
            RenderSystem.enableBlend();
            // Draw jump meter background (using GUI_ICONS_LOCATION as in the older Gui.renderJumpMeter)
            graphics.blit(GUI_ICONS_LOCATION, gx, gy, 0, 84, gwidth, gheight, 256, 256);
            // Draw progress overlay (dummy fixed progress of 50%)
            int progressWidth = (int)(0.5F * 183.0f);
            graphics.blit(GUI_ICONS_LOCATION, gx, gy, 0, 89, progressWidth, gheight, 256, 256);
            RenderingUtils.resetShaderColor(graphics);
        }).setWidgetIdentifierFancyMenu(VanillaHudElements.JUMP_METER_IDENTIFIER);
    }

    protected RendererWidget buildExperienceBarWidget() {
        int screenCenter = this.width / 2;
        int x = screenCenter - 91;
        int y = this.height - 32 + 3;
        int width = 182;
        int height = 5;
        return new SpiffyRendererWidget(x, y, width, height, (graphics, mX, mY, partial, gx, gy, gwidth, gheight, widget) -> {
            RenderSystem.enableBlend();
            // Draw experience bar background from GUI_ICONS_LOCATION (u=0,v=64,size 182x5)
            graphics.blit(GUI_ICONS_LOCATION, gx, gy, 0, 64, gwidth, gheight, 256, 256);
            // Draw progress (dummy progress of 50%)
            int progressWidth = (int)(0.5F * 183.0f);
            graphics.blit(GUI_ICONS_LOCATION, gx, gy, 0, 69, progressWidth, gheight, 256, 256);
            // Draw dummy level text ("42")
            String levelString = "42";
            Font font = Minecraft.getInstance().font;
            int textX = gx + (gwidth / 2) - (font.width(levelString) / 2);
            int textY = gy - 6;
            graphics.drawString(Minecraft.getInstance().font, levelString, textX, textY, 8453920);
            RenderingUtils.resetShaderColor(graphics);
        }).setWidgetIdentifierFancyMenu(VanillaHudElements.EXPERIENCE_BAR_IDENTIFIER);
    }

    protected RendererWidget buildSelectedItemNameWidget() {

        Font font = Minecraft.getInstance().font;
        MutableComponent mutableComponent = Component.empty().append(Component.translatable("spiffyhud.elements.dummy.selected_item_name")).withStyle(ChatFormatting.LIGHT_PURPLE);
        int textWidth = font.width(mutableComponent);
        int textX = (this.width - textWidth) / 2;
        int textY = (this.height - 59) - 18; // -18 is a custom offset to move the text above the bars that got moved up because of the Jump Meter

        return new SpiffyRendererWidget(textX, textY, textWidth, font.lineHeight,
                (graphics, mouseX, mouseY, partial, x, y, width, height, widget) -> {
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

        return new SpiffyRendererWidget(sidebarXStart, sidebarY, totalSidebarWidth, sidebarHeight,
                (graphics, mouseX, mouseY, partial, x, y, width, height, widget) -> {
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
        int barY = (this.height - 39) - 5;
        return new SpiffyRendererWidget(barX - 90, barY, 90, 9, (graphics, mX, mY, partial, gx, gy, gwidth, gheight, widget) -> {
            RenderSystem.enableBlend();
            // Draw 10 food icons using GUI_ICONS_LOCATION:
            for (int i = 0; i < 10; i++) {
                int offsetX = barX - i * 8 - 9;
                // Empty food icon: (u=16,v=27, size 9x9)
                graphics.blit(GUI_ICONS_LOCATION, offsetX, barY, 16, 27, 9, 9, 256, 256);
                // Full overlay: (u=52,v=27)
                graphics.blit(GUI_ICONS_LOCATION, offsetX, barY, 52, 27, 9, 9, 256, 256);
            }
            RenderingUtils.resetShaderColor(graphics);
        }).setWidgetIdentifierFancyMenu(VanillaHudElements.FOOD_BAR_IDENTIFIER);
    }

    protected RendererWidget buildArmorBarWidget() {
        int barX = this.width / 2 - 91;
        int barY = (this.height - 39 - 10) - 5;
        return new SpiffyRendererWidget(barX, barY, 90, 9, (graphics, mX, mY, partial, gx, gy, gwidth, gheight, widget) -> {
            RenderSystem.enableBlend();
            // Draw 10 armor icons (using dummy coordinates, e.g., u=34,v=9 for full armor icon)
            for (int i = 0; i < 10; i++) {
                int offsetX = barX + i * 8;
                graphics.blit(GUI_ICONS_LOCATION, offsetX, barY, 34, 9, 9, 9, 256, 256);
            }
            RenderingUtils.resetShaderColor(graphics);
        }).setWidgetIdentifierFancyMenu(VanillaHudElements.ARMOR_BAR_IDENTIFIER);
    }

    protected RendererWidget buildAirBarWidget() {
        int barX = this.width / 2 + 91;
        int barY = (this.height - 39 - 10) - 5;
        return new SpiffyRendererWidget(barX - 90, barY, 90, 9, (graphics, mX, mY, partial, gx, gy, gwidth, gheight, widget) -> {
            RenderSystem.enableBlend();
            // Draw 10 air bubble icons (using dummy coordinates, e.g., u=16,v=18)
            for (int i = 0; i < 10; i++) {
                int offsetX = barX - i * 8 - 9;
                graphics.blit(GUI_ICONS_LOCATION, offsetX, barY, 16, 18, 9, 9, 256, 256);
            }
            RenderingUtils.resetShaderColor(graphics);
        }).setWidgetIdentifierFancyMenu(VanillaHudElements.AIR_BAR_IDENTIFIER);
    }

    protected RendererWidget buildMountHealthBarWidget() {
        int barX = this.width / 2 + 91;
        int barY = (this.height - 39 - 10) - 15;
        return new SpiffyRendererWidget(barX - 90, barY, 90, 9, (graphics, mX, mY, partial, gx, gy, gwidth, gheight, widget) -> {
            RenderSystem.enableBlend();
            // Draw dummy mount health icons (container from u=52,v=9 and overlay from u=88,v=9)
            for (int i = 0; i < 10; i++) {
                int offsetX = barX - i * 8 - 9;
                graphics.blit(GUI_ICONS_LOCATION, offsetX, barY, 52, 9, 9, 9, 256, 256);
                graphics.blit(GUI_ICONS_LOCATION, offsetX, barY, 88, 9, 9, 9, 256, 256);
            }
            RenderingUtils.resetShaderColor(graphics);
        }).setWidgetIdentifierFancyMenu(VanillaHudElements.MOUNT_HEALTH_BAR_IDENTIFIER);
    }

    protected RendererWidget buildHealthBarWidget() {
        int barX = this.width / 2 - 91;
        int barY = (this.height - 39) - 5;
        return new SpiffyRendererWidget(barX, barY, 90, 9, (graphics, mX, mY, partial, gx, gy, gwidth, gheight, widget) -> {
            Gui.HeartType heartType = Gui.HeartType.forPlayer(Minecraft.getInstance().player);
            RenderSystem.enableBlend();
            // Draw dummy player health bar (container from u=16,v=9 and full overlay from u=88,v=9)
            for (int i = 0; i < 10; i++) {
                int posX = barX + i * 8;
                graphics.blit(GUI_ICONS_LOCATION, posX, barY, Gui.HeartType.CONTAINER.getX(false, false), 0, 9, 9);
                graphics.blit(GUI_ICONS_LOCATION, posX, barY, heartType.getX(false, false), 0, 9, 9);
            }
            RenderingUtils.resetShaderColor(graphics);
        }).setWidgetIdentifierFancyMenu(VanillaHudElements.PLAYER_HEALTH_BAR_IDENTIFIER);
    }

    protected RendererWidget buildOverlayMessageWidget() {
        Font font = Minecraft.getInstance().font;
        Component message = Component.literal("Overlay Message");
        int messageWidth = font.width(message);
        int textX = (this.width / 2) - (messageWidth / 2);
        int textY = ((this.height - 68) - 4) - 18;
        return new SpiffyRendererWidget(textX - 2, textY - 2, messageWidth + 4, font.lineHeight + 4, (graphics, mX, mY, partial, gx, gy, gwidth, gheight, widget) -> {
            RenderSystem.enableBlend();
            // Use a dummy animated color calculation (as in older Gui)
            int animatedTextColor = Mth.hsvToRgb((60 - partial) / 50.0f, 0.7f, 0.6f) & 0xFFFFFF;
            graphics.drawString(Minecraft.getInstance().font, message, textX, textY, animatedTextColor);
            RenderingUtils.resetShaderColor(graphics);
        }).setWidgetIdentifierFancyMenu(VanillaHudElements.OVERLAY_MESSAGE_IDENTIFIER);
    }

    protected RendererWidget buildCrosshairWidget() {
        int crosshairX = (this.width - 15) / 2;
        int crosshairY = (this.height - 15) / 2;
        return new SpiffyRendererWidget(crosshairX, crosshairY, 15, 15, (graphics, mX, mY, partial, gx, gy, gwidth, gheight, widget) -> {
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR,
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO);
            graphics.blit(GUI_ICONS_LOCATION, crosshairX, crosshairY, 0, 0, 15, 15, 256, 256);
            RenderSystem.defaultBlendFunc();
            RenderingUtils.resetShaderColor(graphics);
        }).setWidgetIdentifierFancyMenu(VanillaHudElements.CROSSHAIR_IDENTIFIER);
    }

    protected RendererWidget buildAttackIndicatorWidget() {

        int crossX = (this.width / 2) + 1;
        int crossY = (this.height / 2 - 7 + 16) + 40;
        int hotX = crossX - 2 - 18;
        int hotY = crossY - 6;
        float progress = 0.5F;

        return new SpiffyRendererWidget(hotX, hotY, 18 + 2 + 16, 18,
                (graphics, mouseX, mouseY, partial, x, y, width, height, widget) -> {

                    RenderSystem.enableBlend();

                    // Render crosshair indicator
                    int l = (int)(progress * 17.0f);
                    RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                    graphics.blit(GUI_ICONS_LOCATION, crossX, crossY, 36, 94, 16, 4);
                    graphics.blit(GUI_ICONS_LOCATION, crossX, crossY, 52, 94, l, 4);
                    RenderSystem.defaultBlendFunc();

                    RenderSystem.enableBlend();

                    // Render hotbar indicator
                    int p = (int)(progress * 19.0f);
                    graphics.blit(GUI_ICONS_LOCATION, hotX, hotY, 0, 94, 18, 18);
                    graphics.blit(GUI_ICONS_LOCATION, hotX, hotY + 18 - p, 18, 112 - p, 18, p);

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

        return new SpiffyRendererWidget(textX, textY, totalWidth, totalHeight,
                (graphics, mouseX, mouseY, partial, x, y, width, height, widget) -> {
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
        Component subtitle = Component.literal("Subtitle");
        int subtitleWidth = font.width(subtitle);
        int totalWidth = subtitleWidth * 2;
        int totalHeight = font.lineHeight * 2;
        int textX = (this.width / 2) - (totalWidth / 2);
        int textY = (this.height / 2) + (6 * 2);
        return new SpiffyRendererWidget(textX, textY, totalWidth, totalHeight, (graphics, mX, mY, partial, gx, gy, gwidth, gheight, widget) -> {
            RenderSystem.enableBlend();
            graphics.pose().pushPose();
            graphics.pose().translate(this.width / 2, this.height / 2, 0.0f);
            graphics.pose().pushPose();
            graphics.pose().scale(2.0f, 2.0f, 2.0f);
            graphics.drawString(Minecraft.getInstance().font, subtitle, -subtitleWidth / 2, 6, 0xFFFFFF);
            graphics.pose().popPose();
            graphics.pose().popPose();
            RenderingUtils.resetShaderColor(graphics);
        }).setWidgetIdentifierFancyMenu(VanillaHudElements.SUBTITLE_IDENTIFIER);
    }

    protected RendererWidget buildBossBarWidget() {
        Font font = Minecraft.getInstance().font;
        Component bossName = Component.literal("Boss Bar");
        int bossNameWidth = font.width(bossName);
        int barX = this.width / 2 - 91;
        int barY = 12;
        int totalY = barY - 9;
        int totalHeight = 53;
        return new SpiffyRendererWidget(barX, totalY, 182, totalHeight, (graphics, mX, mY, partial, gx, gy, gwidth, gheight, widget) -> {
            RenderSystem.enableBlend();
            int yPos = barY;
            for (int i = 0; i < 3; i++) {
                // Draw boss bar background and progress using BARS_LOCATION
                graphics.blit(BARS_LOCATION, barX, yPos, 0, 0, 182, 5, 256, 256);
                graphics.blit(BARS_LOCATION, barX, yPos, 0, 5, 182 / 2, 5, 256, 256);
                int n = this.width / 2 - bossNameWidth / 2;
                int o = yPos - 9;
                graphics.drawString(Minecraft.getInstance().font, bossName, n, o, 0xFFFFFF);
                yPos += 10 + font.lineHeight;
            }
            RenderingUtils.resetShaderColor(graphics);
        }).setWidgetIdentifierFancyMenu(VanillaHudElements.BOSS_BARS_IDENTIFIER);
    }

    protected RendererWidget buildEffectsWidget() {
        return new SpiffyRendererWidget(this.width - 75, 1, 75, 50, (graphics, mX, mY, partial, gx, gy, gwidth, gheight, widget) -> {
            Collection<MobEffectInstance> effects = List.of(new MobEffectInstance(MobEffects.LUCK, 300), new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 300), new MobEffectInstance(MobEffects.BAD_OMEN, 300));
            RenderSystem.enableBlend();
            int i = 0;
            int j = 0;
            MobEffectTextureManager mobEffectTextureManager = this.minecraft.getMobEffectTextures();
            ArrayList<Runnable> list = Lists.newArrayListWithExpectedSize(effects.size());
            for (MobEffectInstance effect : Ordering.natural().reverse().sortedCopy(effects)) {
                int n;
                MobEffect mobEffect = effect.getEffect();
                if (!effect.showIcon()) continue;
                int effectX = this.width;
                int effectY = 1;
                if (this.minecraft.isDemo()) {
                    effectY += 15;
                }
                if (mobEffect.isBeneficial()) {
                    effectX -= 25 * ++i;
                } else {
                    effectX -= 25 * ++j;
                    effectY += 26;
                }
                float f = 1.0f;
                if (effect.isAmbient()) {
                    graphics.blit(AbstractContainerScreen.INVENTORY_LOCATION, effectX, effectY, 165, 166, 24, 24);
                } else {
                    graphics.blit(AbstractContainerScreen.INVENTORY_LOCATION, effectX, effectY, 141, 166, 24, 24);
                    if (effect.endsWithin(200)) {
                        int m = effect.getDuration();
                        n = 10 - m / 20;
                        f = Mth.clamp((float)m / 10.0f / 5.0f * 0.5f, 0.0f, 0.5f) + Mth.cos((float)m * (float)Math.PI / 5.0f) * Mth.clamp((float)n / 10.0f * 0.25f, 0.0f, 0.25f);
                    }
                }
                TextureAtlasSprite textureAtlasSprite = mobEffectTextureManager.get(mobEffect);
                n = effectX;
                int o = effectY;
                float g = f;
                int finalN = n;
                list.add(() -> {
                    graphics.setColor(1.0f, 1.0f, 1.0f, g);
                    graphics.blit(finalN + 3, o + 3, 0, 18, 18, textureAtlasSprite);
                    graphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
                });
            }
            list.forEach(Runnable::run);
            RenderingUtils.resetShaderColor(graphics);
        }).setWidgetIdentifierFancyMenu(VanillaHudElements.EFFECTS_IDENTIFIER);
    }

    private static class SpiffyRendererWidget extends RendererWidget {

        public SpiffyRendererWidget(int x, int y, int width, int height, @NotNull RendererWidgetBody body) {
            super(x, y, width, height, body);
        }

        @Override
        public void render(@NotNull GuiGraphics $$0, int $$1, int $$2, float $$3) {
            //Don't render widgets when not in the editor
            if (!(Minecraft.getInstance().screen instanceof LayoutEditorScreen)) return;
            super.render($$0, $$1, $$2, $$3);
        }

    }

}
