package de.keksuccino.spiffyhud.mixin.mixins.common.client;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.client.gui.components.spectator.SpectatorGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SpectatorGui.class)
public class MixinSpectatorGui {

    /**
     * @reason Patch out the Z -90 translate() call that seems pretty useless (could be wrong tho) and breaks Spiffy.
     */
    @WrapWithCondition(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/spectator/SpectatorGui;setBlitOffset(I)V"))
    private boolean wrap_translate_in_renderHotbar_Spiffy(SpectatorGui instance, int i) {
        return false;
    }

}
