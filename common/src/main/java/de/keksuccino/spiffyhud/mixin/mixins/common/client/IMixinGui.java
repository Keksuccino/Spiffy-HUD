package de.keksuccino.spiffyhud.mixin.mixins.common.client;

import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Gui.class)
public interface IMixinGui {

    @Accessor("tickCount") int getTickCount_Spiffy();

}
