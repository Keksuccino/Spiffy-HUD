package de.keksuccino.spiffyhud.customization;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.ScreenCustomization;
import de.keksuccino.fancymenu.customization.layer.ScreenCustomizationLayer;
import de.keksuccino.fancymenu.customization.layer.ScreenCustomizationLayerHandler;
import de.keksuccino.fancymenu.events.screen.*;
import de.keksuccino.fancymenu.util.event.acara.EventHandler;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpiffyGui implements Renderable {

    private static final Logger LOGGER = LogManager.getLogger();

    private static boolean initialized = false;
    private static SpiffyOverlayScreen spiffyOverlayScreen = new SpiffyOverlayScreen();

    private int lastScreenWidth = 0;
    private int lastScreenHeight = 0;

    public SpiffyGui() {

        if (!initialized) {
            //TODO init stuff here if needed
            initialized = true;
        }

        this.setNewOverlayScreen();
        this.lastScreenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        this.lastScreenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        this.initOverlayScreen(false);
        this.tickOverlayUpdate();

    }


    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {

        if (!this.shouldRenderCustomizations()) return;

        this.tickOverlayUpdate();

        this.runLayerTask(() -> {

            EventHandler.INSTANCE.postEvent(new ScreenTickEvent.Pre(spiffyOverlayScreen));
            spiffyOverlayScreen.tick();
            EventHandler.INSTANCE.postEvent(new ScreenTickEvent.Post(spiffyOverlayScreen));

            this.restoreRenderDefaults(graphics);

            EventHandler.INSTANCE.postEvent(new RenderScreenEvent.Pre(spiffyOverlayScreen, graphics, mouseX, mouseY, partial));
            spiffyOverlayScreen.render(graphics, mouseX, mouseY, partial);
            this.restoreRenderDefaults(graphics);
            EventHandler.INSTANCE.postEvent(new RenderScreenEvent.Post(spiffyOverlayScreen, graphics, mouseX, mouseY, partial));

            this.restoreRenderDefaults(graphics);

        });

    }

    //TODO update to fit the HUD render defaults
    private void restoreRenderDefaults(@NotNull GuiGraphics graphics) {
        RenderingUtils.resetShaderColor(graphics);
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
    }

    private boolean shouldRenderCustomizations() {
        return (spiffyOverlayScreen != null) && (this.getLayer() != null);
    }

    @Nullable
    private ScreenCustomizationLayer getLayer() {
        if (spiffyOverlayScreen == null) return null;
        ScreenCustomizationLayer l = ScreenCustomizationLayerHandler.getLayerOfScreen(spiffyOverlayScreen);
        if (l != null) l.loadEarly = true;
        return l;
    }

    private void tickOverlayUpdate() {
        try {
            int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
            //Re-init overlay on window size change
            if ((screenWidth != this.lastScreenWidth) || (screenHeight != this.lastScreenHeight)) {
                this.initOverlayScreen(true);
            }
            this.lastScreenWidth = screenWidth;
            this.lastScreenHeight = screenHeight;
        } catch (Exception ex) {
            LOGGER.error("[SPIFFY HUD] Failed to tick SpiffyGui!", ex);
        }
    }

    private void setNewOverlayScreen() {
        spiffyOverlayScreen = new SpiffyOverlayScreen();
        ScreenCustomizationLayerHandler.registerScreen(spiffyOverlayScreen);
        this.getLayer(); //dummy call to let the method set loadEarly to true
    }

    private void initOverlayScreen(boolean resize) {
        this.runLayerTask(() -> {
            try {
                RenderingUtils.resetGuiScale();
                if (!resize) {
                    EventHandler.INSTANCE.postEvent(new OpenScreenEvent(spiffyOverlayScreen));
                }
                spiffyOverlayScreen.width = Minecraft.getInstance().getWindow().getGuiScaledWidth();
                spiffyOverlayScreen.height = Minecraft.getInstance().getWindow().getGuiScaledHeight();
                EventHandler.INSTANCE.postEvent(new InitOrResizeScreenStartingEvent(spiffyOverlayScreen, resize ? InitOrResizeScreenEvent.InitializationPhase.RESIZE : InitOrResizeScreenEvent.InitializationPhase.INIT));
                EventHandler.INSTANCE.postEvent(new InitOrResizeScreenEvent.Pre(spiffyOverlayScreen, resize ? InitOrResizeScreenEvent.InitializationPhase.RESIZE : InitOrResizeScreenEvent.InitializationPhase.INIT));
                spiffyOverlayScreen.init(Minecraft.getInstance(), spiffyOverlayScreen.width, spiffyOverlayScreen.height);
                EventHandler.INSTANCE.postEvent(new InitOrResizeScreenEvent.Post(spiffyOverlayScreen, resize ? InitOrResizeScreenEvent.InitializationPhase.RESIZE : InitOrResizeScreenEvent.InitializationPhase.INIT));
                EventHandler.INSTANCE.postEvent(new InitOrResizeScreenCompletedEvent(spiffyOverlayScreen, resize ? InitOrResizeScreenEvent.InitializationPhase.RESIZE : InitOrResizeScreenEvent.InitializationPhase.INIT));
                if (!resize) {
                    EventHandler.INSTANCE.postEvent(new OpenScreenPostInitEvent(spiffyOverlayScreen));
                }
            } catch (Exception ex) {
                LOGGER.error("[SPIFFY HUD] Failed to initialize SpiffyOverlayScreen!", ex);
            }
        });
    }

    private void runLayerTask(@NotNull Runnable run) {
        try {
            boolean customizationEnabled = ScreenCustomization.isScreenCustomizationEnabled();
            ScreenCustomization.setScreenCustomizationEnabled(true);
            Screen current = Minecraft.getInstance().screen;
            if (!(current instanceof SpiffyOverlayScreen)) {
                Minecraft.getInstance().screen = spiffyOverlayScreen;
                run.run();
                Minecraft.getInstance().screen = current;
            }
            ScreenCustomization.setScreenCustomizationEnabled(customizationEnabled);
        } catch (Exception ex) {
            LOGGER.error("[SPIFFY HUD] Failed to run layer task!", ex);
        }
    }

}
