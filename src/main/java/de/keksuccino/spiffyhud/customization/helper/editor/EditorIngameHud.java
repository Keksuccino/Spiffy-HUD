package de.keksuccino.spiffyhud.customization.helper.editor;

import de.keksuccino.konkrete.events.SubscribeEvent;
import de.keksuccino.konkrete.events.client.ClientTickEvent;
import de.keksuccino.spiffyhud.customization.CustomizationHandler;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import de.keksuccino.spiffyhud.events.CustomizationSystemReloadedEvent;
import de.keksuccino.spiffyhud.events.WindowResizedEvent;
import de.keksuccino.spiffyhud.events.hud.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;

public class EditorIngameHud extends CustomizableIngameGui {
	
	private static InGameHud lightModeHud;

	public EditorIngameHud() {
		super(MinecraftClient.getInstance(), true);
		if (lightModeHud == null) {
			lightModeHud = new InGameHud(MinecraftClient.getInstance());
		}
	}
	
	@Override
	public void render(MatrixStack matrix, float partialTicks) {
		
		if (CustomizationHandler.isLightModeEnabled()) {
			lightModeHud.render(matrix, partialTicks);
		} else {
			super.render(matrix, partialTicks);
		}
		
	}

	@SubscribeEvent
	@Override
	public void onWindowResized(WindowResizedEvent e) {
		super.onWindowResized(e);
	}

	@SubscribeEvent
	@Override
	public void onSystemReloaded(CustomizationSystemReloadedEvent e) {
		super.onSystemReloaded(e);
	}

	@SubscribeEvent
	@Override
	public void onClientTick(ClientTickEvent.Pre e) {
		super.onClientTick(e);
	}

	@SubscribeEvent
	@Override
	public void onRenderVignette(RenderVignetteEvent.Pre e) {
		super.onRenderVignette(e);
	}

	@SubscribeEvent
	@Override
	public void onRenderCrosshair(RenderCrosshairEvent.Pre e) {
		super.onRenderCrosshair(e);
	}

	@SubscribeEvent
	@Override
	public void onRenderBossBars(RenderBossBarsEvent.Pre e) {
		super.onRenderBossBars(e);
	}

	@SubscribeEvent
	@Override
	public void onRenderHotbar(RenderHotbarEvent.Pre e) {
		super.onRenderHotbar(e);
	}

	@SubscribeEvent
	@Override
	public void onRenderStatusBars(RenderStatusBarsEvent.Pre e) {
		super.onRenderStatusBars(e);
	}

	@SubscribeEvent
	@Override
	public void onRenderMountHealth(RenderMountHealthEvent.Pre e) {
		super.onRenderMountHealth(e);
	}

	@SubscribeEvent
	@Override
	public void onRenderSidebar(RenderSidebarEvent.Pre e) {
		super.onRenderSidebar(e);
	}
	
}
