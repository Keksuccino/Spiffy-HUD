package de.keksuccino.spiffyhud.mixin.client;

import de.keksuccino.konkrete.Konkrete;
import de.keksuccino.spiffyhud.events.MouseInputEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MixinMouse {

    @Inject(at = @At("TAIL"), method = "onMouseButton")
    private void onOnMouseButton(long window, int button, int action, int mods, CallbackInfo info) {

        if (window == MinecraftClient.getInstance().getWindow().getHandle()) {
            MouseInputEvent e = new MouseInputEvent(button, action, mods);
            Konkrete.getEventHandler().callEventsFor(e);
        }

    }

}
