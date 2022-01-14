package de.keksuccino.spiffyhud.mixin.client;

import de.keksuccino.spiffyhud.customization.helper.editor.LayoutEditorScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class MixinClientPlayerInteractionManager {

    //Force-render survival HUD elements in editor
    @Inject(at = @At("RETURN"), method = "hasStatusBars", cancellable = true)
    private void onHasStatusBars(CallbackInfoReturnable<Boolean> info) {
        if ((MinecraftClient.getInstance() != null) && (MinecraftClient.getInstance().currentScreen instanceof LayoutEditorScreen)) {
            info.setReturnValue(true);
        }
    }

    //Force-render survival HUD elements in editor
    @Inject(at = @At("RETURN"), method = "hasExperienceBar", cancellable = true)
    private void onHasExperienceBar(CallbackInfoReturnable<Boolean> info) {
        if ((MinecraftClient.getInstance() != null) && (MinecraftClient.getInstance().currentScreen instanceof LayoutEditorScreen)) {
            info.setReturnValue(true);
        }
    }

    //Force-render survival HUD elements in editor
    @Inject(at = @At("RETURN"), method = "getCurrentGameMode", cancellable = true)
    private void onGetCurrentGameMode(CallbackInfoReturnable<GameMode> info) {
        if ((MinecraftClient.getInstance() != null) && (MinecraftClient.getInstance().currentScreen instanceof LayoutEditorScreen)) {
            info.setReturnValue(GameMode.SURVIVAL);
        }
    }

}
