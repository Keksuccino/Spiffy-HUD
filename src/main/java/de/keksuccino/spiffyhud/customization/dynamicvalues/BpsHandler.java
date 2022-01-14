package de.keksuccino.spiffyhud.customization.dynamicvalues;

import de.keksuccino.konkrete.Konkrete;
import de.keksuccino.konkrete.events.SubscribeEvent;
import de.keksuccino.konkrete.events.client.ClientTickEvent;
import de.keksuccino.spiffyhud.events.world.PlayerItemPlacedEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.ActionResult;

public class BpsHandler {
	
	private static int lastBps = 0;
	private static int currentBps = 0;
	private static long lastReset = 0L;
	
	public static void init() {
		Konkrete.getEventHandler().registerEventsFrom(new BpsHandler());
	}
	
	public static int getBps() {
		if (lastBps == 0) {
			return currentBps;
		}
		return lastBps;
	}
	
	@SubscribeEvent
	public void onTick(ClientTickEvent.Pre e) {
		long ms = System.currentTimeMillis();
		if ((lastReset + 1000) < ms) {
			lastBps = currentBps;
			currentBps = 0;
			lastReset = ms;
		}
	}

	@SubscribeEvent
	public void onBlockPlaced(PlayerItemPlacedEvent e) {
		if (MinecraftClient.getInstance().player != null) {
			if (e.player.getUuid().toString().equals(MinecraftClient.getInstance().player.getUuid().toString())) {
				if (e.result == ActionResult.SUCCESS) {
					currentBps++;
				}
			}
		}
	}

}
