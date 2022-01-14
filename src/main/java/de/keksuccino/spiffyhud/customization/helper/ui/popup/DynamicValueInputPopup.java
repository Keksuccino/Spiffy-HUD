package de.keksuccino.spiffyhud.customization.helper.ui.popup;

import java.awt.Color;
import java.util.function.Consumer;
import net.minecraft.client.MinecraftClient;
import de.keksuccino.spiffyhud.customization.helper.ui.content.DynamicValueTextfield;
import de.keksuccino.konkrete.input.CharacterFilter;

public class DynamicValueInputPopup extends FHTextInputPopup {

	public DynamicValueInputPopup(Color color, String title, CharacterFilter filter, int alpha) {
		super(color, title, filter, alpha);
	}
	
	public DynamicValueInputPopup(Color color, String title, CharacterFilter filter, int backgroundAlpha, Consumer<String> callback) {
		super(color, title, filter, backgroundAlpha, callback);
	}

	@Override
	protected void init(Color color, String title, CharacterFilter filter, Consumer<String> callback) {
		
		super.init(color, title, filter, callback);
		
		this.textField = new DynamicValueTextfield(MinecraftClient.getInstance().textRenderer, 0, 0, 200, 20, true, filter);
		this.textField.changeFocus(true);
		this.textField.setFocused(false);
		this.textField.setMaxLength(1000);
		
	}
	

}
