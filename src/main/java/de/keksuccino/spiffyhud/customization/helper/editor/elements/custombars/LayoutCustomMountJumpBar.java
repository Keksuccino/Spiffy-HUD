package de.keksuccino.spiffyhud.customization.helper.editor.elements.custombars;

import de.keksuccino.spiffyhud.customization.helper.editor.LayoutEditorScreen;
import de.keksuccino.spiffyhud.customization.items.custombars.CustomMountJumpBarCustomizationItem;
import de.keksuccino.konkrete.properties.PropertiesSection;

public class LayoutCustomMountJumpBar extends LayoutCustomBarBase {

    public LayoutCustomMountJumpBar(CustomMountJumpBarCustomizationItem object, LayoutEditorScreen handler) {
        super(object, handler);
    }

    @Override
    protected PropertiesSection getPropertiesRaw() {
        PropertiesSection s = super.getPropertiesRaw();
        s.addEntry("action", "addcustommountjumpbar");
        return s;
    }

}
