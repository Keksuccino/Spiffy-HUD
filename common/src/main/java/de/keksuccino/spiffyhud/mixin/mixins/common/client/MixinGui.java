package de.keksuccino.spiffyhud.mixin.mixins.common.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Gui.class)
public class MixinGui {

    /**
     * @reason Patch out the Z -90 translate() call that seems pretty useless (could be wrong tho) and breaks Spiffy.
     */
    @WrapOperation(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V"))
    private void wrap_translate_in_renderHotbar_Spiffy(PoseStack instance, float $$0, float $$1, float $$2, Operation<Void> original) {
        original.call(instance, 0.0F, 0.0F, 0.0F);
    }

}
