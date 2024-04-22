package de.keksuccino.spiffyhud.mixin.mixins.common.client;

import de.keksuccino.fancymenu.customization.ScreenCustomization;
import de.keksuccino.spiffyhud.SpiffyHud;
import de.keksuccino.spiffyhud.customization.Shared;
import de.keksuccino.spiffyhud.customization.SpiffyOverlayScreen;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenCustomization.class)
public class MixinScreenCustomization {

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lde/keksuccino/fancymenu/customization/element/elements/Elements;registerAll()V", shift = At.Shift.AFTER), remap = false)
    private static void after_registerElements_in_init_Spiffy(CallbackInfo info) {

        SpiffyHud.earlyInit();

    }

    //If HUD gets re-initialized, don't use normal re-init logic
    @Inject(method = "reInitCurrentScreen", at = @At("HEAD"), remap = false, cancellable = true)
    private static void headReInitCurrentScreen_Spiffy(CallbackInfo info) {
        if ((Minecraft.getInstance().screen instanceof SpiffyOverlayScreen s) && !s.showFancyMenuOverlay) {
            Shared.reInitHudLayouts = true;
            info.cancel();
        }
    }

}
