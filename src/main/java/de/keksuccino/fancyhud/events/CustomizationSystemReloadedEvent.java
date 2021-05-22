package de.keksuccino.fancyhud.events;

import net.minecraftforge.eventbus.api.Event;

public class CustomizationSystemReloadedEvent extends Event {
	
	@Override
	public boolean isCancelable() {
		return false;
	}

}
