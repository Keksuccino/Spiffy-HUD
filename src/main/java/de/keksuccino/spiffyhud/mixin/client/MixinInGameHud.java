package de.keksuccino.spiffyhud.mixin.client;

import de.keksuccino.konkrete.Konkrete;
import de.keksuccino.spiffyhud.events.hud.*;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.Entity;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import de.keksuccino.spiffyhud.customization.CustomizationHandler;
import de.keksuccino.spiffyhud.customization.helper.editor.LayoutEditorScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(InGameHud.class)
public class MixinInGameHud {

	private InGameHud hud = (InGameHud)((Object)this);
	
	@Inject(at = @At(value = "HEAD"), method = "render", cancellable = true)
	protected void onRenderPre(MatrixStack matrix, float partialTicks, CallbackInfo info) {

		RenderHudEvent.Pre e = new RenderHudEvent.Pre(matrix, hud);
		Konkrete.getEventHandler().callEventsFor(e);
		if (e.isCanceled()) {
			info.cancel();
		}

		if (CustomizationHandler.isLightModeEnabled() && !(MinecraftClient.getInstance().currentScreen instanceof LayoutEditorScreen)) {

			CustomizationHandler.INGAME_GUI.renderBackgroundItems(matrix);
			
		}
		
	}
	
	@Inject(at = @At(value = "TAIL"), method = "render")
	protected void onRenderPost(MatrixStack matrix, float partialTicks, CallbackInfo info) {

		RenderHudEvent.Post e = new RenderHudEvent.Post(matrix, hud);
		Konkrete.getEventHandler().callEventsFor(e);

		if (CustomizationHandler.isLightModeEnabled() && !(MinecraftClient.getInstance().currentScreen instanceof LayoutEditorScreen)) {

			CustomizationHandler.INGAME_GUI.renderCustomVanillaElements(matrix, partialTicks);
			
			CustomizationHandler.INGAME_GUI.renderForegroundItems(matrix);
			
		}
		
	}

	//RenderBossBarsEvent Pre + Post
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/BossBarHud;render(Lnet/minecraft/client/util/math/MatrixStack;)V"), method = "render")
	private void onRenderBossBarsInRender(BossBarHud instance, MatrixStack matrices) {
		if (!CustomizationHandler.isLightModeEnabled()) {

			RenderBossBarsEvent.Pre e = new RenderBossBarsEvent.Pre(matrices, hud);
			Konkrete.getEventHandler().callEventsFor(e);
			if (!e.isCanceled()) {
				instance.render(matrices);
			}
			RenderBossBarsEvent.Post e2 = new RenderBossBarsEvent.Post(matrices, hud);
			Konkrete.getEventHandler().callEventsFor(e2);

		} else {
			instance.render(matrices);
		}
	}

	//RenderCrosshairEvent Pre
	@Inject(at = @At("HEAD"), method = "renderCrosshair", cancellable = true)
	private void onRenderCrosshairPre(MatrixStack matrices, CallbackInfo info) {
		if (!CustomizationHandler.isLightModeEnabled()) {
			RenderCrosshairEvent.Pre e = new RenderCrosshairEvent.Pre(matrices, hud);
			Konkrete.getEventHandler().callEventsFor(e);
			if (e.isCanceled()) {
				info.cancel();
			}
		}
	}

	//RenderCrosshairEvent Post
	@Inject(at = @At("RETURN"), method = "renderCrosshair")
	private void onRenderCrosshairPost(MatrixStack matrices, CallbackInfo info) {
		if (!CustomizationHandler.isLightModeEnabled()) {
			RenderCrosshairEvent.Post e = new RenderCrosshairEvent.Post(matrices, hud);
			Konkrete.getEventHandler().callEventsFor(e);
		}
	}

	//RenderHotbarEvent Pre
	@Inject(at = @At("HEAD"), method = "renderHotbar", cancellable = true)
	private void onRenderHotbarPre(float tickDelta, MatrixStack matrices, CallbackInfo info) {
		if (!CustomizationHandler.isLightModeEnabled()) {
			RenderHotbarEvent.Pre e = new RenderHotbarEvent.Pre(matrices, hud);
			Konkrete.getEventHandler().callEventsFor(e);
			if (e.isCanceled()) {
				info.cancel();
			}
		}
	}

	//RenderHotbarEvent Post
	@Inject(at = @At("RETURN"), method = "renderHotbar")
	private void onRenderHotbarPost(float tickDelta, MatrixStack matrices, CallbackInfo info) {
		if (!CustomizationHandler.isLightModeEnabled()) {
			RenderHotbarEvent.Post e = new RenderHotbarEvent.Post(matrices, hud);
			Konkrete.getEventHandler().callEventsFor(e);
		}
	}

	//RenderMountHealthEvent Pre
	@Inject(at = @At("HEAD"), method = "renderMountHealth", cancellable = true)
	private void onRenderMountHealthPre(MatrixStack matrices, CallbackInfo info) {
		if (!CustomizationHandler.isLightModeEnabled()) {
			RenderMountHealthEvent.Pre e = new RenderMountHealthEvent.Pre(matrices, hud);
			Konkrete.getEventHandler().callEventsFor(e);
			if (e.isCanceled()) {
				info.cancel();
			}
		}
	}

	//RenderMountHealthEvent Post
	@Inject(at = @At("RETURN"), method = "renderMountHealth")
	private void onRenderMountHealthPost(MatrixStack matrices, CallbackInfo info) {
		if (!CustomizationHandler.isLightModeEnabled()) {
			RenderMountHealthEvent.Post e = new RenderMountHealthEvent.Post(matrices, hud);
			Konkrete.getEventHandler().callEventsFor(e);
		}
	}

	//RenderSidebarEvent Pre
	@Inject(at = @At("HEAD"), method = "renderScoreboardSidebar", cancellable = true)
	private void onRenderScoreboardSidebarPre(MatrixStack matrices, ScoreboardObjective objective, CallbackInfo info) {
		if (!CustomizationHandler.isLightModeEnabled()) {
			RenderSidebarEvent.Pre e = new RenderSidebarEvent.Pre(matrices, hud);
			Konkrete.getEventHandler().callEventsFor(e);
			if (e.isCanceled()) {
				info.cancel();
			}
		}
	}

	//RenderSidebarEvent Post
	@Inject(at = @At("RETURN"), method = "renderScoreboardSidebar")
	private void onRenderScoreboardSidebarPost(MatrixStack matrices, ScoreboardObjective objective, CallbackInfo info) {
		if (!CustomizationHandler.isLightModeEnabled()) {
			RenderSidebarEvent.Post e = new RenderSidebarEvent.Post(matrices, hud);
			Konkrete.getEventHandler().callEventsFor(e);
		}
	}

	//RenderStatusBarsEvent Pre
	@Inject(at = @At("HEAD"), method = "renderStatusBars", cancellable = true)
	private void onRenderStatusBarsPre(MatrixStack matrices, CallbackInfo info) {
		if (!CustomizationHandler.isLightModeEnabled()) {
			RenderStatusBarsEvent.Pre e = new RenderStatusBarsEvent.Pre(matrices, hud);
			Konkrete.getEventHandler().callEventsFor(e);
			if (e.isCanceled()) {
				info.cancel();
			}
		}
	}

	//RenderStatusBarsEvent Post
	@Inject(at = @At("RETURN"), method = "renderStatusBars")
	private void onRenderStatusBarsPost(MatrixStack matrices, CallbackInfo info) {
		if (!CustomizationHandler.isLightModeEnabled()) {
			RenderStatusBarsEvent.Post e = new RenderStatusBarsEvent.Post(matrices, hud);
			Konkrete.getEventHandler().callEventsFor(e);
		}
	}

	//RenderVignetteEvent Pre
	@Inject(at = @At("HEAD"), method = "renderVignetteOverlay", cancellable = true)
	private void onRenderVignetteOverlayPre(Entity entity, CallbackInfo info) {
		if (!CustomizationHandler.isLightModeEnabled()) {
			RenderVignetteEvent.Pre e = new RenderVignetteEvent.Pre(new MatrixStack(), hud);
			Konkrete.getEventHandler().callEventsFor(e);
			if (e.isCanceled()) {
				info.cancel();
			}
		}
	}

	//RenderVignetteEvent Post
	@Inject(at = @At("RETURN"), method = "renderVignetteOverlay")
	private void onRenderVignetteOverlayPost(Entity entity, CallbackInfo info) {
		if (!CustomizationHandler.isLightModeEnabled()) {
			RenderVignetteEvent.Post e = new RenderVignetteEvent.Post(new MatrixStack(), hud);
			Konkrete.getEventHandler().callEventsFor(e);
		}
	}
	
}
