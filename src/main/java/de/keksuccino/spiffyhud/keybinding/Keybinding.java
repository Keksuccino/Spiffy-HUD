package de.keksuccino.spiffyhud.keybinding;

import de.keksuccino.spiffyhud.SpiffyHud;
import de.keksuccino.konkrete.config.exceptions.InvalidValueException;
import de.keksuccino.konkrete.input.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class Keybinding {

	public static KeyBinding KeyToggleHelper;
	
	public static void init() {

		KeyToggleHelper = new KeyBinding("Toggle Customization Controls | CTRL + ALT + ", 72, "Spiffy HUD");
		ClientRegistry.registerKeyBinding(KeyToggleHelper);
		
		initGuiClickActions();
		
	}
	
	private static void initGuiClickActions() {

		KeyboardHandler.addKeyPressedListener((c) -> {

			if ((KeyToggleHelper.getKey().getKeyCode() == c.keycode) && KeyboardHandler.isCtrlPressed() && KeyboardHandler.isAltPressed()) {
				try {
					if (SpiffyHud.config.getOrDefault("showcustomizationcontrols", true)) {
						SpiffyHud.config.setValue("showcustomizationcontrols", false);
					} else {
						SpiffyHud.config.setValue("showcustomizationcontrols", true);
					}
					SpiffyHud.config.syncConfig();
					if (Minecraft.getInstance().currentScreen != null) {
						Minecraft.getInstance().displayGuiScreen(Minecraft.getInstance().currentScreen);
					}
				} catch (InvalidValueException e) {
					e.printStackTrace();
				}
			}
			
		});
		
	}
	
}
