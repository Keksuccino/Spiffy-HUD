package de.keksuccino.spiffyhud;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.overlay.CustomizationOverlay;
import de.keksuccino.fancymenu.events.screen.InitOrResizeScreenCompletedEvent;
import de.keksuccino.fancymenu.events.screen.RenderScreenEvent;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import de.keksuccino.fancymenu.util.event.acara.EventListener;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.fancymenu.util.rendering.ui.UIBase;
import de.keksuccino.fancymenu.util.rendering.ui.contextmenu.v2.ContextMenu;
import de.keksuccino.fancymenu.util.rendering.ui.tooltip.Tooltip;
import de.keksuccino.fancymenu.util.rendering.ui.tooltip.TooltipHandler;
import de.keksuccino.fancymenu.util.rendering.ui.widget.button.ExtendedButton;
import de.keksuccino.spiffyhud.customization.SpiffyOverlayScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class SpiffyEvents {

    private static final ResourceLocation EDIT_BUTTON_TEXTURE = new ResourceLocation("spiffyhud", "textures/edit_button.png");

    private ContextMenu spiffyMenu;
    private ExtendedButton spiffyButton;

    @EventListener
    public void onInitOrResizeScreenCompleted(InitOrResizeScreenCompletedEvent e) {

        if ((e.getScreen() instanceof PauseScreen p) && p.isPauseScreen() && CustomizationOverlay.isOverlayVisible(e.getScreen())) {

            this.spiffyMenu = new ContextMenu()
                    .setForceDefaultTooltipStyle(true)
                    .setForceUIScale(true);

            this.spiffyMenu.addClickableEntry("customize_hud", Component.translatable("spiffyhud.edit_hud"), (menu, entry) -> {
                Minecraft.getInstance().setScreen(new SpiffyOverlayScreen(true));
            }).setIcon(ContextMenu.IconFactory.getIcon("edit"));

            this.spiffyMenu.addSeparatorEntry("separator_after_customize");

//            this.drippyMenu.addValueCycleEntry("allow_universal_layouts",
//                            CommonCycles.cycleEnabledDisabled("drippyloadingscreen.settings.allow_universal", SpiffyHud.getOptions().allowUniversalLayouts.getValue())
//                                    .addCycleListener(value -> SpiffyHud.getOptions().allowUniversalLayouts.setValue(value.getAsBoolean())))
//                    .setTooltipSupplier((menu, entry) -> Tooltip.of(LocalizationUtils.splitLocalizedLines("drippyloadingscreen.settings.allow_universal.desc")))
//                    .setIcon(ContextMenu.IconFactory.getIcon("layout"));
//
//            this.drippyMenu.addValueCycleEntry("wait_for_textures_in_loading",
//                            CommonCycles.cycleEnabledDisabled("drippyloadingscreen.settings.wait_for_textures_in_loading", SpiffyHud.getOptions().waitForTexturesInLoading.getValue())
//                                    .addCycleListener(value -> SpiffyHud.getOptions().waitForTexturesInLoading.setValue(value.getAsBoolean())))
//                    .setTooltipSupplier((menu, entry) -> Tooltip.of(LocalizationUtils.splitLocalizedLines("drippyloadingscreen.settings.wait_for_textures_in_loading.desc")))
//                    .setIcon(ContextMenu.IconFactory.getIcon("timer"));
//
//            this.drippyMenu.addValueCycleEntry("early_fade_out_elements",
//                            CommonCycles.cycleEnabledDisabled("drippyloadingscreen.settings.early_fade_out_elements", SpiffyHud.getOptions().earlyFadeOutElements.getValue())
//                                    .addCycleListener(value -> SpiffyHud.getOptions().earlyFadeOutElements.setValue(value.getAsBoolean())))
//                    .setTooltipSupplier((menu, entry) -> Tooltip.of(LocalizationUtils.splitLocalizedLines("drippyloadingscreen.settings.early_fade_out_elements.desc")));
//
//            this.drippyMenu.addValueCycleEntry("fade_out_loading_screen",
//                            CommonCycles.cycleEnabledDisabled("drippyloadingscreen.settings.fade_out_loading_screen", SpiffyHud.getOptions().fadeOutLoadingScreen.getValue())
//                                    .addCycleListener(value -> SpiffyHud.getOptions().fadeOutLoadingScreen.setValue(value.getAsBoolean())))
//                    .setTooltipSupplier((menu, entry) -> Tooltip.of(LocalizationUtils.splitLocalizedLines("drippyloadingscreen.settings.fade_out_loading_screen.desc")));

            //------------------------------

            ExtendedButton editButton = new ExtendedButton(-30, 40, 80, 40, Component.empty(), (button) -> {
                spiffyMenu.openMenuAt(10, 80 - 10);
            }) {

                @Override
                public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {

                    var m = CustomizationOverlay.getCurrentMenuBarInstance();
                    if ((m == null) || (!m.isUserNavigatingInMenuBar() && !spiffyMenu.isUserNavigatingInMenu())) {
                        TooltipHandler.INSTANCE.addTooltip(Tooltip.of(LocalizationUtils.splitLocalizedLines("spiffyhud.edit_hud.desc")).setDefaultStyle().setScale(UIBase.getUIScale()), () -> this.isHovered, false, true);
                    }
                    if (this.isHoveredOrFocused() || spiffyMenu.isOpen()) {
                        this.setX(-20);
                    } else {
                        this.setX(-30);
                    }
                    super.render(graphics, mouseX, mouseY, partial);
                    RenderSystem.enableBlend();
                    RenderingUtils.resetShaderColor(graphics);
                    graphics.blit(EDIT_BUTTON_TEXTURE, this.getX(), this.getY(), 0.0f, 0.0f, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());
                    RenderingUtils.resetShaderColor(graphics);

                    spiffyMenu.render(graphics, mouseX, mouseY, partial);

                }

                @Override
                protected void renderBackground(@NotNull GuiGraphics graphics) {
                    boolean b = this.isHovered;
                    if (spiffyMenu.isOpen()) this.isHovered = true;
                    super.renderBackground(graphics);
                    this.isHovered = b;
                }

                @Override
                public boolean mouseClicked(double $$0, double $$1, int $$2) {
                    if (spiffyMenu.isOpen() && !spiffyMenu.isUserNavigatingInMenu() && !this.isHovered()) {
                        spiffyMenu.closeMenu();
                        this.setFocused(false);
                    }
                    return super.mouseClicked($$0, $$1, $$2);
                }

            };
            UIBase.applyDefaultWidgetSkinTo(editButton);

            //2 because MenuBar and DebugOverlay need to be at pos 0 and 1
            if (e.getWidgets().size() >= 2) {
                e.getWidgets().add(2, editButton);
            } else {
                e.getWidgets().add(0, editButton);
            }
            this.spiffyButton = editButton;
            if (e.getWidgets().size() >= 2) {
                e.getWidgets().add(2, this.spiffyMenu);
            } else {
                e.getWidgets().add(0, this.spiffyMenu);
            }

        }

    }

    @EventListener(priority = 0) //FM is -1
    public void onScreenRenderPost(RenderScreenEvent.Post e) {

        if ((e.getScreen() instanceof PauseScreen p) && p.isPauseScreen() && CustomizationOverlay.isOverlayVisible(e.getScreen())) {
            if (this.spiffyButton != null) {
                this.spiffyButton.render(e.getGraphics(), e.getMouseX(), e.getMouseY(), e.getPartial());
            }
        }

    }

}
