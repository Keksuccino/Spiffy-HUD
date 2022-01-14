package de.keksuccino.spiffyhud.customization.helper.ui.screens;

import de.keksuccino.spiffyhud.SpiffyHud;
import net.minecraft.client.gui.screen.Screen;
import de.keksuccino.konkrete.config.ConfigEntry;
import de.keksuccino.konkrete.gui.screens.ConfigScreen;
import de.keksuccino.konkrete.localization.Locals;

public class FHConfigScreen extends ConfigScreen {

	public FHConfigScreen(Screen parent) {
		super(SpiffyHud.config, Locals.localize("spiffyhud.config"), parent);
	}
	
	@Override
	protected void init() {
		
		super.init();
		
		for (String s : this.config.getCategorys()) {
			this.setCategoryDisplayName(s, Locals.localize("spiffyhud.config.categories." + s));
		}
		
		for (ConfigEntry e : this.config.getAllAsEntry()) {
			this.setValueDisplayName(e.getName(), Locals.localize("spiffyhud.config." + e.getName()));
			this.setValueDescription(e.getName(), Locals.localize("spiffyhud.config." + e.getName() + ".desc"));
		}
		
	}
	
}
