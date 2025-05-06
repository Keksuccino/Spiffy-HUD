package de.keksuccino.spiffyhud.mixin.mixins.common.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.keksuccino.spiffyhud.util.rendering.BlockRenderingUtils;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ModelBlockRenderer.class)
public class MixinModelBlockRenderer {

    @WrapOperation(method = "putQuadData", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;putBulkData(Lcom/mojang/blaze3d/vertex/PoseStack$Pose;Lnet/minecraft/client/renderer/block/model/BakedQuad;[FFFFF[IIZ)V"))
    private void wrap_putBulkData_in_putQuadData_Spiffy(VertexConsumer instance, PoseStack.Pose pose, BakedQuad bakedQuad, float[] f1, float f2, float f3, float f4, float alpha, int[] f6, int f7, boolean f8, Operation<Void> original) {
        if (BlockRenderingUtils.getBlockOpacity() < 1.0f) {
            original.call(instance, pose, bakedQuad, f1, f2, f3, f4, BlockRenderingUtils.getBlockOpacity(), f6, f7, f8);
        } else {
            original.call(instance, pose, bakedQuad, f1, f2, f3, f4, alpha, f6, f7, f8);
        }
    }

    @WrapOperation(method = "renderQuadList", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;putBulkData(Lcom/mojang/blaze3d/vertex/PoseStack$Pose;Lnet/minecraft/client/renderer/block/model/BakedQuad;FFFFII)V"))
    private static void wrap_putBulkData_in_renderQuadList_Spiffy(VertexConsumer instance, PoseStack.Pose p_85996_, BakedQuad p_85997_, float p_85999_, float p_86000_, float p_86001_, float alpha, int p_86003_, int p_331548_, Operation<Void> original) {
        if (BlockRenderingUtils.getBlockOpacity() < 1.0f) {
            original.call(instance, p_85996_, p_85997_, p_85999_, p_86000_, p_86001_, BlockRenderingUtils.getBlockOpacity(), p_86003_, p_331548_);
        } else {
            original.call(instance, p_85996_, p_85997_, p_85999_, p_86000_, p_86001_, alpha, p_86003_, p_331548_);
        }
    }

}
