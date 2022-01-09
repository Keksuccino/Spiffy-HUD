package de.keksuccino.spiffyhud.keybinding;

import de.keksuccino.spiffyhud.SpiffyHud;
import de.keksuccino.konkrete.config.exceptions.InvalidValueException;
import de.keksuccino.konkrete.input.KeyboardHandler;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientRegistry;

public class Keybinding {

	public static KeyMapping KeyToggleHelper;
	
	public static void init() {

		KeyToggleHelper = new KeyMapping("Toggle Customization Controls | CTRL + ALT + ", 72, "Spiffy HUD");
		ClientRegistry.registerKeyBinding(KeyToggleHelper);
		
		initGuiClickActions();
		
	}
	
	private static void initGuiClickActions() {

		KeyboardHandler.addKeyPressedListener((c) -> {

			if ((KeyToggleHelper.getKey().getValue() == c.keycode) && KeyboardHandler.isCtrlPressed() && KeyboardHandler.isAltPressed()) {
				try {
					if (SpiffyHud.config.getOrDefault("showcustomizationcontrols", true)) {
						SpiffyHud.config.setValue("showcustomizationcontrols", false);
					} else {
						SpiffyHud.config.setValue("showcustomizationcontrols", true);
					}
					SpiffyHud.config.syncConfig();
					if (Minecraft.getInstance().screen != null) {
						Minecraft.getInstance().setScreen(Minecraft.getInstance().screen);
					}
				} catch (InvalidValueException e) {
					e.printStackTrace();
				}
			}
			
		});
		
	}
	
}
