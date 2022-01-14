package de.keksuccino.spiffyhud.customization.helper.ui.content;

import de.keksuccino.spiffyhud.customization.helper.ui.UIBase;
import net.minecraft.client.gui.widget.ClickableWidget;
import de.keksuccino.konkrete.gui.content.AdvancedButton;

public class CustomizationButton extends AdvancedButton {

	public CustomizationButton(int x, int y, int widthIn, int heightIn, String buttonText, PressAction onPress) {
		super(x, y, widthIn, heightIn, buttonText, onPress);
		this.ignoreBlockedInput = true;
		this.ignoreLeftMouseDownClickBlock = true;
		this.enableRightclick = true;
		this.setLabelShadow(false);
		UIBase.colorizeButton(this);
	}
	
	public CustomizationButton(int x, int y, int widthIn, int heightIn, String buttonText, boolean b, PressAction onPress) {
		super(x, y, widthIn, heightIn, buttonText, b, onPress);
		this.ignoreBlockedInput = true;
		this.ignoreLeftMouseDownClickBlock = true;
		this.enableRightclick = true;
		this.setLabelShadow(false);
		UIBase.colorizeButton(this);
	}

	public static boolean isCustomizationButton(ClickableWidget w) {
		return (w instanceof CustomizationButton);
	}

}
