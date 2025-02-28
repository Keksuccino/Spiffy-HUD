package de.keksuccino.spiffyhud.mixin.mixins.common.client;

import net.minecraft.client.gui.Gui;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Gui.class)
public interface IMixinGui {

    @Accessor("tickCount") int getTickCount_Spiffy();

    @Accessor("overlayMessageString") Component get_overlayMessageString_Spiffy();

    @Accessor("overlayMessageTime") int get_overlayMessageTime_Spiffy();

    @Accessor("toolHighlightTimer") int get_toolHighlightTimer_Spiffy();

}
