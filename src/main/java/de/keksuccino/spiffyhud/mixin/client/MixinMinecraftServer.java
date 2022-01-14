package de.keksuccino.spiffyhud.mixin.client;

import de.keksuccino.konkrete.Konkrete;
import de.keksuccino.spiffyhud.events.world.WorldTickEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer {

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;tick(Ljava/util/function/BooleanSupplier;)V"), method = "tickWorlds")
    private void onWorldTickInTickWorlds(ServerWorld instance, BooleanSupplier shouldKeepTicking) {

        WorldTickEvent e = new WorldTickEvent(instance);
        Konkrete.getEventHandler().callEventsFor(e);
        if (!e.isCanceled()) {
            instance.tick(shouldKeepTicking);
        }

    }

}
