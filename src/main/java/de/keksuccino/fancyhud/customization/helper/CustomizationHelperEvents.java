package de.keksuccino.fancyhud.customization.helper;

import de.keksuccino.fancyhud.FancyHud;
import de.keksuccino.fancyhud.customization.helper.ui.UIBase;
import de.keksuccino.konkrete.gui.content.AdvancedButton;
import de.keksuccino.konkrete.input.StringUtils;
import de.keksuccino.konkrete.localization.Locals;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CustomizationHelperEvents {
	
	private static final ResourceLocation OPEN_HELPER_BUTTON_TEXTURE_IDLE = new ResourceLocation("fancyhud", "/helper/openhelper_btn_idle.png");
	private static final ResourceLocation OPEN_HELPER_BUTTON_TEXTURE_HOVER = new ResourceLocation("fancyhud", "/helper/openhelper_btn_hover.png");
	
	protected AdvancedButton openHelperButton;
	
	@SubscribeEvent
	public void onInitScreenPost(GuiScreenEvent.InitGuiEvent.Post e) {
		
		if (e.getGui() instanceof IngameMenuScreen) {

			if (FancyHud.config.getOrDefault("showcustomizationcontrols", true)) {

				int btnwidth = (int) (107 * UIBase.getUIScale());
				int btnheight = (int) (107 * UIBase.getUIScale());

				this.openHelperButton = new AdvancedButton(0, e.getGui().height - btnheight , btnwidth, btnheight, "", false, (press) -> {
					Minecraft.getInstance().displayGuiScreen(new CustomizationHelperScreen());
				});
				this.openHelperButton.setBackgroundTexture(OPEN_HELPER_BUTTON_TEXTURE_IDLE, OPEN_HELPER_BUTTON_TEXTURE_HOVER);
				this.openHelperButton.setDescription(StringUtils.splitLines(Locals.localize("fancyhud.helper.openhelper"), "%n%"));

				e.addWidget(openHelperButton);

			}

		}
		
	}

}
