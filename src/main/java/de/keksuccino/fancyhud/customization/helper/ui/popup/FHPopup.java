package de.keksuccino.fancyhud.customization.helper.ui.popup;

import de.keksuccino.fancyhud.customization.helper.ui.UIBase;
import de.keksuccino.konkrete.gui.content.AdvancedButton;
import de.keksuccino.konkrete.gui.screens.popup.Popup;

public class FHPopup extends Popup {

	public FHPopup(int backgroundAlpha) {
		super(backgroundAlpha);
	}
	
	@Override
	protected void colorizePopupButton(AdvancedButton b) {
		UIBase.colorizeButton(b);
	}

}
