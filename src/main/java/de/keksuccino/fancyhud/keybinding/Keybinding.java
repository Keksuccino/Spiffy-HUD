package de.keksuccino.fancyhud.keybinding;

import de.keksuccino.fancyhud.FancyHud;
import de.keksuccino.konkrete.config.exceptions.InvalidValueException;
import de.keksuccino.konkrete.input.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class Keybinding {

	public static KeyBinding KeyToggleHelper;
	
	public static void init() {

		KeyToggleHelper = new KeyBinding("Toggle Customization Controls | CTRL + ALT + ", 72, "FancyHud");
		ClientRegistry.registerKeyBinding(KeyToggleHelper);
		
		initGuiClickActions();
		
	}
	
	private static void initGuiClickActions() {

		KeyboardHandler.addKeyPressedListener((c) -> {

			if ((KeyToggleHelper.getKey().getKeyCode() == c.keycode) && KeyboardHandler.isCtrlPressed() && KeyboardHandler.isAltPressed()) {
				try {
					if (FancyHud.config.getOrDefault("showcustomizationcontrols", true)) {
						FancyHud.config.setValue("showcustomizationcontrols", false);
					} else {
						FancyHud.config.setValue("showcustomizationcontrols", true);
					}
					FancyHud.config.syncConfig();
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
