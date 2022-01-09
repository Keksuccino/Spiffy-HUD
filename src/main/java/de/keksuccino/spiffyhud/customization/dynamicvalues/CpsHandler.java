package de.keksuccino.spiffyhud.customization.dynamicvalues;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent.MouseInputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CpsHandler {
	
	private static int lastCps = 0;
	private static int currentCps = 0;
	private static long lastReset = 0L;
	
	public static void init() {
		MinecraftForge.EVENT_BUS.register(new CpsHandler());
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
		if (Minecraft.getInstance().gameSettings.keyBindAttack.isKeyDown() && (e.getAction() == GLFW.GLFW_PRESS)) {
			currentCps++;
		}
	}

}
