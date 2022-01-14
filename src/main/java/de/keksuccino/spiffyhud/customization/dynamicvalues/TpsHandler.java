package de.keksuccino.spiffyhud.customization.dynamicvalues;

import de.keksuccino.konkrete.Konkrete;
import de.keksuccino.konkrete.events.SubscribeEvent;
import de.keksuccino.konkrete.events.client.ClientTickEvent;

public class TpsHandler {
	
	private static int lastTps = 0;
	private static int currentTps = 0;
	private static long lastReset = 0L;
	
	public static void init() {
		Konkrete.getEventHandler().registerEventsFrom(new TpsHandler());
	}
	
	public static int getTps() {
		if (lastTps == 0) {
			return currentTps;
		}
		return lastTps;
	}
	
	@SubscribeEvent
	public void onTick(ClientTickEvent.Pre e) {
		long ms = System.currentTimeMillis();
		if ((lastReset + 1000) < ms) {
			lastTps = currentTps;
			currentTps = 0;
			lastReset = ms;
		}
		currentTps++;
	}

}
