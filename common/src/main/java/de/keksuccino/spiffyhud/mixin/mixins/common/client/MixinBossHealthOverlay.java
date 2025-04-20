package de.keksuccino.spiffyhud.mixin.mixins.common.client;

import de.keksuccino.spiffyhud.customization.VanillaHudElements;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossHealthOverlay.class)
public class MixinBossHealthOverlay {

    /**
     * @reason Hides the boss bars when hidden via Spiffy HUD.
     */
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void before_render_Spiffy(GuiGraphics $$0, CallbackInfo info) {
        if (VanillaHudElements.isHidden(VanillaHudElements.BOSS_BARS_IDENTIFIER)) info.cancel();
    }

}
