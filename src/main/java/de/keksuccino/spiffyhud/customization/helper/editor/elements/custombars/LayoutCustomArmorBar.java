package de.keksuccino.spiffyhud.customization.helper.editor.elements.custombars;

import de.keksuccino.spiffyhud.customization.helper.editor.LayoutEditorScreen;
import de.keksuccino.spiffyhud.customization.helper.ui.popup.FHTextInputPopup;
import de.keksuccino.spiffyhud.customization.items.custombars.CustomArmorBarCustomizationItem;
import de.keksuccino.konkrete.gui.content.AdvancedButton;
import de.keksuccino.konkrete.gui.screens.popup.PopupHandler;
import de.keksuccino.konkrete.input.CharacterFilter;
import de.keksuccino.konkrete.input.StringUtils;
import de.keksuccino.konkrete.localization.Locals;
import de.keksuccino.konkrete.math.MathUtils;
import de.keksuccino.konkrete.properties.PropertiesSection;

import java.awt.*;

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
		String hideWhenEmptyLabel = Locals.localize("spiffyhud.helper.editor.elements.vanilla.bars.hidewhenempty.on");
		if (!i.hideWhenEmpty) {
			hideWhenEmptyLabel = Locals.localize("spiffyhud.helper.editor.elements.vanilla.bars.hidewhenempty.off");
		}
		AdvancedButton hideWhenEmptyButton = new AdvancedButton(0, 0, 0, 16, hideWhenEmptyLabel, true, (press) -> {
			if (i.hideWhenEmpty) {
				((AdvancedButton)press).setMessage(Locals.localize("spiffyhud.helper.editor.elements.vanilla.bars.hidewhenempty.off"));
				i.hideWhenEmpty = false;
			} else {
				((AdvancedButton)press).setMessage(Locals.localize("spiffyhud.helper.editor.elements.vanilla.bars.hidewhenempty.on"));
				i.hideWhenEmpty = true;
			}
		});
		hideWhenEmptyButton.setDescription(StringUtils.splitLines(Locals.localize("spiffyhud.helper.editor.elements.vanilla.bars.hidewhenempty.btn.desc"), "%n%"));
		this.rightclickMenu.addContent(hideWhenEmptyButton);

		/** MAX ARMOR **/
		AdvancedButton maxArmorButton = new AdvancedButton(0, 0, 0, 16, Locals.localize("spiffyhud.helper.creator.add.customarmorbar.maxarmor"), true, (press) -> {
			FHTextInputPopup pop = new FHTextInputPopup(new Color(0, 0, 0, 0), Locals.localize("spiffyhud.helper.creator.add.customarmorbar.maxarmor"), CharacterFilter.getIntegerCharacterFiler(), 240, (call) -> {
				if (call != null) {
					if (MathUtils.isInteger(call)) {
						int mf = Integer.parseInt(call);
						if (mf != i.maxArmor) {
							this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
						}
						i.maxArmor = mf;
					}
				}
			});
			pop.setText("" + i.maxArmor);
			PopupHandler.displayPopup(pop);
		});
		this.rightclickMenu.addContent(maxArmorButton);
		
	}
	
	@Override
	protected PropertiesSection getPropertiesRaw() {
		PropertiesSection s = super.getPropertiesRaw();
		s.addEntry("action", "addcustomarmorbar");
		s.addEntry("maxarmor", "" + ((CustomArmorBarCustomizationItem)this.getObject()).maxArmor);
		s.addEntry("hidewhenempty", "" + ((CustomArmorBarCustomizationItem)this.getObject()).hideWhenEmpty);
		return s;
	}

}
