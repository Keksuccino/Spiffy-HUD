package de.keksuccino.spiffyhud.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import de.keksuccino.spiffyhud.customization.helper.editor.LayoutEditorScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerController;

@Mixin(value = PlayerController.class)
public class MixinPlayerController {

	@Inject(at = @At(value = "HEAD"), method = "shouldDrawHUD", cancellable = true)
	protected void onShouldDrawHUD(CallbackInfoReturnable<Boolean> info) {
		if ((Minecraft.getInstance() != null) && (Minecraft.getInstance().currentScreen instanceof LayoutEditorScreen)) {
			info.setReturnValue(true);
		}
	}

	@Inject(at = @At(value = "HEAD"), method = "gameIsSurvivalOrAdventure", cancellable = true)
	protected void onGameIsSurvivalOrAdventure(CallbackInfoReturnable<Boolean> info) {
		if ((Minecraft.getInstance() != null) && (Minecraft.getInstance().currentScreen instanceof LayoutEditorScreen)) {
			info.setReturnValue(true);
		}
	}
	
}
