package de.keksuccino.spiffyhud.keybinding;

import de.keksuccino.spiffyhud.SpiffyHud;
import de.keksuccino.konkrete.config.exceptions.InvalidValueException;
import de.keksuccino.konkrete.input.KeyboardHandler;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;

public class Keybinding {

	public static KeyBinding KeyToggleHelper;
	
	public static void init() {

		KeyToggleHelper = new KeyBinding("Toggle Customization Controls | CTRL + ALT + ", 72, "Spiffy HUD");
		KeyBindingHelper.registerKeyBinding(KeyToggleHelper);
		
		initGuiClickActions();
		
	}
	
	private static void initGuiClickActions() {

		KeyboardHandler.addKeyPressedListener((c) -> {

			if ((KeyBindingHelper.getBoundKeyOf(KeyToggleHelper).getCode() == c.keycode) && KeyboardHandler.isCtrlPressed() && KeyboardHandler.isAltPressed()) {
				try {
					if (SpiffyHud.config.getOrDefault("showcustomizationcontrols", true)) {
						SpiffyHud.config.setValue("showcustomizationcontrols", false);
					} else {
						SpiffyHud.config.setValue("showcustomizationcontrols", true);
					}
					SpiffyHud.config.syncConfig();
					if (MinecraftClient.getInstance().currentScreen != null) {
						MinecraftClient.getInstance().setScreen(MinecraftClient.getInstance().currentScreen);
					}
				} catch (InvalidValueException e) {
					e.printStackTrace();
				}
			}
			
		});
		
	}
	
}
