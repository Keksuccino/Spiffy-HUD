package de.keksuccino.spiffyhud.customization.dynamicvalues;

import de.keksuccino.konkrete.Konkrete;
import de.keksuccino.konkrete.events.SubscribeEvent;
import de.keksuccino.spiffyhud.events.MouseInputEvent;
import de.keksuccino.spiffyhud.events.world.WorldTickEvent;
import org.lwjgl.glfw.GLFW;
import net.minecraft.client.MinecraftClient;

public class CpsHandler {
	
	private static int lastCps = 0;
	private static int currentCps = 0;
	private static long lastReset = 0L;
	
	public static void init() {
		Konkrete.getEventHandler().registerEventsFrom(new CpsHandler());
	}
	
	public static int getCps() {
		if (lastCps == 0) {
			return currentCps;
		}
		return lastCps;
	}

	@SubscribeEvent
	public void onTick(WorldTickEvent e) {
		long ms = System.currentTimeMillis();
		if ((lastReset + 1000) < ms) {
			lastCps = currentCps;
			currentCps = 0;
			lastReset = ms;
		}
	}

	@SubscribeEvent
	public void onMouseClick(MouseInputEvent e) {
		if (MinecraftClient.getInstance().options.attackKey.isPressed() && (e.action == GLFW.GLFW_PRESS)) {
			currentCps++;
		}
	}

}
