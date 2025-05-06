package de.keksuccino.spiffyhud.mixin.mixins.common.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.keksuccino.spiffyhud.util.rendering.EntityRenderingUtils;
import de.keksuccino.spiffyhud.util.rendering.SpiffyRenderUtils;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntityRenderer.class)
public class MixinLivingEntityRenderer {

    @WrapOperation(method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ARGB;multiply(II)I"))
    private int wrap_multiply_in_render_Spiffy(int i1, int i2, Operation<Integer> original) {
        if (EntityRenderingUtils.getLivingEntityOpacity() < 1.0f) {
            return SpiffyRenderUtils.colorWithAlpha(original.call(i1, i2), EntityRenderingUtils.getLivingEntityOpacity());
        }
        return original.call(i1, i2);
    }

}
