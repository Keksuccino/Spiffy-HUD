package de.keksuccino.spiffyhud.customization.helper.editor.elements.custombars;

import de.keksuccino.spiffyhud.customization.helper.editor.LayoutEditorScreen;
import de.keksuccino.spiffyhud.customization.items.custombars.CustomExpBarCustomizationItem;
import de.keksuccino.konkrete.properties.PropertiesSection;

public class LayoutCustomExpBar extends LayoutCustomBarBase {

	public LayoutCustomExpBar(CustomExpBarCustomizationItem object, LayoutEditorScreen handler) {
		super(object, handler);
	}
	
	@Override
	protected PropertiesSection getPropertiesRaw() {
		PropertiesSection s = super.getPropertiesRaw();
		s.addEntry("action", "addcustomexpbar");
		return s;
	}

}
