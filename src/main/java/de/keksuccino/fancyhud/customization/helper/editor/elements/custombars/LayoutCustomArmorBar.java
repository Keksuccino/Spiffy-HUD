package de.keksuccino.fancyhud.customization.helper.editor.elements.custombars;

import de.keksuccino.fancyhud.customization.helper.editor.LayoutEditorScreen;
import de.keksuccino.fancyhud.customization.items.custombars.CustomArmorBarCustomizationItem;
import de.keksuccino.konkrete.gui.content.AdvancedButton;
import de.keksuccino.konkrete.input.StringUtils;
import de.keksuccino.konkrete.localization.Locals;
import de.keksuccino.konkrete.properties.PropertiesSection;

public class LayoutCustomArmorBar extends LayoutCustomBarBase {

	public LayoutCustomArmorBar(CustomArmorBarCustomizationItem object, LayoutEditorScreen handler) {
		super(object, handler);
	}
	
	@Override
	public void init() {
		
		super.init();
		
		this.rightclickMenu.addSeparator();
		
		CustomArmorBarCustomizationItem i = (CustomArmorBarCustomizationItem) this.getObject();
		
		/** HIDE WHEN EMPTY **/
		String hideWhenEmptyLabel = Locals.localize("fancyhud.helper.editor.elements.vanilla.bars.hidewhenempty.on");
		if (!i.hideWhenEmpty) {
			hideWhenEmptyLabel = Locals.localize("fancyhud.helper.editor.elements.vanilla.bars.hidewhenempty.off");
		}
		AdvancedButton hideWhenEmptyButton = new AdvancedButton(0, 0, 0, 16, hideWhenEmptyLabel, true, (press) -> {
			if (i.hideWhenEmpty) {
				((AdvancedButton)press).setMessage(Locals.localize("fancyhud.helper.editor.elements.vanilla.bars.hidewhenempty.off"));
				i.hideWhenEmpty = false;
			} else {
				((AdvancedButton)press).setMessage(Locals.localize("fancyhud.helper.editor.elements.vanilla.bars.hidewhenempty.on"));
				i.hideWhenEmpty = true;
			}
		});
		hideWhenEmptyButton.setDescription(StringUtils.splitLines(Locals.localize("fancyhud.helper.editor.elements.vanilla.bars.hidewhenempty.btn.desc"), "%n%"));
		this.rightclickMenu.addContent(hideWhenEmptyButton);
		
	}
	
	@Override
	protected PropertiesSection getPropertiesRaw() {
		PropertiesSection s = super.getPropertiesRaw();
		s.addEntry("action", "addcustomarmorbar");
		s.addEntry("hidewhenempty", "" + ((CustomArmorBarCustomizationItem)this.getObject()).hideWhenEmpty);
		return s;
	}

}
