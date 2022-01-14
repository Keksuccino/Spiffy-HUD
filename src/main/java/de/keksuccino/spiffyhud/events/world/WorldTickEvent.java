package de.keksuccino.spiffyhud.events.world;

import de.keksuccino.konkrete.events.EventBase;
import net.minecraft.server.world.ServerWorld;

public class WorldTickEvent extends EventBase {

    public ServerWorld world;

    public WorldTickEvent(ServerWorld world) {
        this.world = world;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }

}
