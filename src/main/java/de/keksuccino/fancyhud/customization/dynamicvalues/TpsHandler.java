package de.keksuccino.fancyhud.customization.dynamicvalues;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TpsHandler {
	
	private static int lastTps = 0;
	private static int currentTps = 0;
	private static long lastReset = 0L;
	
	public static void init() {
		MinecraftForge.EVENT_BUS.register(new TpsHandler());
	}
	
	public static int getTps() {
		if (lastTps == 0) {
			return currentTps;
		}
		return lastTps;
	}
	
	@SubscribeEvent
	public void onTick(ClientTickEvent e) {
		long ms = System.currentTimeMillis();
		if ((lastReset + 1000) < ms) {
			lastTps = currentTps;
			currentTps = 0;
			lastReset = ms;
		}
		currentTps++;
	}

}
