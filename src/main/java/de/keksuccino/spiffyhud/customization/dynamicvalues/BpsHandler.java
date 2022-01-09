package de.keksuccino.spiffyhud.customization.dynamicvalues;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.world.BlockEvent.EntityPlaceEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BpsHandler {
	
	private static int lastBps = 0;
	private static int currentBps = 0;
	private static long lastReset = 0L;
	
	public static void init() {
		MinecraftForge.EVENT_BUS.register(new BpsHandler());
	}
	
	public static int getBps() {
		if (lastBps == 0) {
			return currentBps;
		}
		return lastBps;
	}
	
	@SubscribeEvent
	public void onTick(ClientTickEvent e) {
		long ms = System.currentTimeMillis();
		if ((lastReset + 1000) < ms) {
			lastBps = currentBps;
			currentBps = 0;
			lastReset = ms;
		}
	}
	
	@SubscribeEvent
	public void onBlockPlaced(EntityPlaceEvent e) {
		if (Minecraft.getInstance().player != null) {
			if (e.getEntity().getUniqueID().toString().equals(Minecraft.getInstance().player.getUniqueID().toString())) {
				currentBps++;
			}
		}
	}

}
