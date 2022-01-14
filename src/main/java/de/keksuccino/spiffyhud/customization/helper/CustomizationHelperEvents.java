package de.keksuccino.spiffyhud.customization.helper;

import de.keksuccino.konkrete.events.SubscribeEvent;
import de.keksuccino.konkrete.events.client.GuiScreenEvent;
import de.keksuccino.spiffyhud.SpiffyHud;
import de.keksuccino.spiffyhud.customization.helper.ui.UIBase;
import de.keksuccino.konkrete.gui.content.AdvancedButton;
import de.keksuccino.konkrete.input.StringUtils;
import de.keksuccino.konkrete.localization.Locals;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.util.Identifier;

public class CustomizationHelperEvents {
	
	private static final Identifier OPEN_HELPER_BUTTON_TEXTURE_IDLE = new Identifier("spiffyhud", "/helper/openhelper_btn_idle.png");
	private static final Identifier OPEN_HELPER_BUTTON_TEXTURE_HOVER = new Identifier("spiffyhud", "/helper/openhelper_btn_hover.png");
	
	protected AdvancedButton openHelperButton;
	
	@SubscribeEvent
	public void onInitScreenPost(GuiScreenEvent.InitGuiEvent.Post e) {
		
		if (e.getGui() instanceof GameMenuScreen) {

			if (SpiffyHud.config.getOrDefault("showcustomizationcontrols", true)) {

				int btnwidth = (int) (107 * UIBase.getUIScale());
				int btnheight = (int) (107 * UIBase.getUIScale());

				this.openHelperButton = new AdvancedButton(0, e.getGui().height - btnheight , btnwidth, btnheight, "", false, (press) -> {
					MinecraftClient.getInstance().setScreen(new CustomizationHelperScreen());
				});
				this.openHelperButton.setBackgroundTexture(OPEN_HELPER_BUTTON_TEXTURE_IDLE, OPEN_HELPER_BUTTON_TEXTURE_HOVER);
				this.openHelperButton.setDescription(StringUtils.splitLines(Locals.localize("spiffyhud.helper.openhelper"), "%n%"));

				e.addWidget(openHelperButton);

			}

		}
		
	}

}
