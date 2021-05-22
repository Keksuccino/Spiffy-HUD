package de.keksuccino.fancyhud.customization.helper.ui.screens;

import de.keksuccino.fancyhud.FancyHud;
import de.keksuccino.konkrete.config.ConfigEntry;
import de.keksuccino.konkrete.gui.screens.ConfigScreen;
import de.keksuccino.konkrete.localization.Locals;
import net.minecraft.client.gui.screen.Screen;

public class FHConfigScreen extends ConfigScreen {

	public FHConfigScreen(Screen parent) {
		super(FancyHud.config, Locals.localize("fancyhud.config"), parent);
	}
	
	@Override
	protected void init() {
		
		super.init();
		
		for (String s : this.config.getCategorys()) {
			this.setCategoryDisplayName(s, Locals.localize("fancyhud.config.categories." + s));
		}
		
		for (ConfigEntry e : this.config.getAllAsEntry()) {
			this.setValueDisplayName(e.getName(), Locals.localize("fancyhud.config." + e.getName()));
			this.setValueDescription(e.getName(), Locals.localize("fancyhud.config." + e.getName() + ".desc"));
		}
		
	}
	
}
