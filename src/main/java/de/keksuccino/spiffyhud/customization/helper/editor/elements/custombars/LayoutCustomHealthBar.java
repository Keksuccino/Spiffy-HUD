package de.keksuccino.spiffyhud.customization.helper.editor.elements.custombars;

import de.keksuccino.spiffyhud.customization.helper.editor.LayoutEditorScreen;
import de.keksuccino.spiffyhud.customization.items.custombars.CustomHealthBarCustomizationItem;
import de.keksuccino.konkrete.gui.content.AdvancedButton;
import de.keksuccino.konkrete.input.StringUtils;
import de.keksuccino.konkrete.localization.Locals;
import de.keksuccino.konkrete.properties.PropertiesSection;

public class LayoutCustomHealthBar extends LayoutCustomBarBase {

	public LayoutCustomHealthBar(CustomHealthBarCustomizationItem object, LayoutEditorScreen handler) {
		super(object, handler);
	}
	
	@Override
	public void init() {
		
		super.init();
		
		this.rightclickMenu.addSeparator();
		
		CustomHealthBarCustomizationItem i = (CustomHealthBarCustomizationItem) this.getObject();
		
		/** HIDE WHEN FULL **/
		String hideWhenFullLabel = Locals.localize("spiffyhud.helper.editor.elements.vanilla.bars.hidewhenfull.on");
		if (!i.hideWhenFull) {
			hideWhenFullLabel = Locals.localize("spiffyhud.helper.editor.elements.vanilla.bars.hidewhenfull.off");
		}
		AdvancedButton hideWhenFullButton = new AdvancedButton(0, 0, 0, 16, hideWhenFullLabel, true, (press) -> {
			if (i.hideWhenFull) {
				((AdvancedButton)press).setMessage(Locals.localize("spiffyhud.helper.editor.elements.vanilla.bars.hidewhenfull.off"));
				i.hideWhenFull = false;
			} else {
				((AdvancedButton)press).setMessage(Locals.localize("spiffyhud.helper.editor.elements.vanilla.bars.hidewhenfull.on"));
				i.hideWhenFull = true;
			}
		});
		hideWhenFullButton.setDescription(StringUtils.splitLines(Locals.localize("spiffyhud.helper.editor.elements.vanilla.bars.hidewhenfull.btn.desc"), "%n%"));
		this.rightclickMenu.addContent(hideWhenFullButton);
		
	}
	
	@Override
	protected PropertiesSection getPropertiesRaw() {
		PropertiesSection s = super.getPropertiesRaw();
		s.addEntry("action", "addcustomhealthbar");
		s.addEntry("hidewhenfull", "" + ((CustomHealthBarCustomizationItem)this.getObject()).hideWhenFull);
		return s;
	}

}
