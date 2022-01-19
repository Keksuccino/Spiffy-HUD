package de.keksuccino.spiffyhud.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DebugHud.class)
public class MixinDebugHud {

    @Shadow @Final @Mutable private TextRenderer textRenderer;

    @Inject(at = @At("HEAD"), method = "render")
    private void onRender(MatrixStack matrices, CallbackInfo ci) {
        if(this.textRenderer == null) {
            this.textRenderer = MinecraftClient.getInstance().textRenderer;
        }
    }

}
