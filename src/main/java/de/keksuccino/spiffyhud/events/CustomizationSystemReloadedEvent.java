package de.keksuccino.spiffyhud.events;

import net.minecraftforge.eventbus.api.Event;

public class CustomizationSystemReloadedEvent extends Event {
	
	@Override
	public boolean isCancelable() {
		return false;
	}

}
