package de.keksuccino.spiffyhud.mixin.mixins.fabric.client;

import de.keksuccino.spiffyhud.customization.SpiffyGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class MixinGui {

    @Unique private SpiffyGui spiffyGui = null;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/spectator/SpectatorGui;renderHotbar(Lnet/minecraft/client/gui/GuiGraphics;)V"))
    private void beforeRenderSpectatorHotbar_Spiffy(GuiGraphics graphics, float partial, CallbackInfo info) {

        if (this.spiffyGui == null) this.spiffyGui = new SpiffyGui();

        if (!Minecraft.getInstance().options.hideGui) {
            spiffyGui.render(graphics, -10000000, -10000000, partial);
        }

    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderHotbar(FLnet/minecraft/client/gui/GuiGraphics;)V"))
    private void beforeRenderNormalHotbar_Spiffy(GuiGraphics graphics, float partial, CallbackInfo info) {

        if (this.spiffyGui == null) this.spiffyGui = new SpiffyGui();

        if (!Minecraft.getInstance().options.hideGui) {
            spiffyGui.render(graphics, -10000000, -10000000, partial);
        }

    }

}
