package de.keksuccino.spiffyhud.mixin.mixins.common.client;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.network.syncher.SynchedEntityData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SynchedEntityData.class)
public interface IMixinSynchedEntityData {

    @Accessor("itemsById") Int2ObjectMap<SynchedEntityData.DataItem<?>> get_itemsById_Spiffy();

}
