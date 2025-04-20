package de.keksuccino.spiffyhud.mixin.mixins.common.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import de.keksuccino.spiffyhud.customization.elements.chatcustomizer.ChatCustomizerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.client.gui.screens.ChatScreen;

@Mixin(ChatScreen.class)
public class MixinChatScreen {

    /**
     * Apply custom input field background color
     */
    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/ChatScreen;fill(Lcom/mojang/blaze3d/vertex/PoseStack;IIIII)V", ordinal = 0))
    private void customizeInputBackgroundFill_Spiffy(PoseStack pose, int minX, int minY, int maxX, int maxY, int color, Operation<Void> original) {
        if (ChatCustomizerHandler.inputBackgroundColor != null) {
            // Use the custom color with its own alpha
            int customColor = ChatCustomizerHandler.inputBackgroundColor.getColorInt();
            original.call(pose, minX, minY, maxX, maxY, customColor);
        } else {
            original.call(pose, minX, minY, maxX, maxY, color);
        }
    }

}