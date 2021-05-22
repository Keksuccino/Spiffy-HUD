package de.keksuccino.fancyhud.customization.helper;

import net.minecraftforge.common.MinecraftForge;

public class CustomizationHelper {

	public static void init() {
		
		MinecraftForge.EVENT_BUS.register(new CustomizationHelperEvents());
		
	}
	
}
