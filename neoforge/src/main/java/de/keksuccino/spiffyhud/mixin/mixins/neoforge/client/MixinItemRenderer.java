package de.keksuccino.spiffyhud.mixin.mixins.neoforge.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.keksuccino.spiffyhud.util.rendering.ItemRenderingUtils;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {

    @WrapOperation(method = "renderQuadList", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;putBulkData(Lcom/mojang/blaze3d/vertex/PoseStack$Pose;Lnet/minecraft/client/renderer/block/model/BakedQuad;FFFFIIZ)V"))
    private static void wrap_putBulkData_in_renderQuadList_Spiffy(VertexConsumer instance, PoseStack.Pose pose, BakedQuad bakedQuad, float v1, float v2, float v3, float alpha, int i1, int i2, boolean b, Operation<Void> original) {
        if (ItemRenderingUtils.getItemOpacity() < 1.0f) {
            original.call(instance, pose, bakedQuad, v1, v2, v3, ItemRenderingUtils.getItemOpacity(), i1, i2, b);
        } else {
            original.call(instance, pose, bakedQuad, v1, v2, v3, alpha, i1, i2, b);
        }
    }

}
