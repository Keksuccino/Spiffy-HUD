package de.keksuccino.fancyhud.customization.helper.editor.elements.custombars;

import java.awt.Color;

import de.keksuccino.fancyhud.customization.helper.editor.LayoutEditorScreen;
import de.keksuccino.fancyhud.customization.helper.ui.popup.FHTextInputPopup;
import de.keksuccino.fancyhud.customization.items.custombars.CustomFoodBarCustomizationItem;
import de.keksuccino.konkrete.gui.content.AdvancedButton;
import de.keksuccino.konkrete.gui.screens.popup.PopupHandler;
import de.keksuccino.konkrete.input.CharacterFilter;
import de.keksuccino.konkrete.input.StringUtils;
import de.keksuccino.konkrete.localization.Locals;
import de.keksuccino.konkrete.math.MathUtils;
import de.keksuccino.konkrete.properties.PropertiesSection;

public class LayoutCustomFoodBar extends LayoutCustomBarBase {

	public LayoutCustomFoodBar(CustomFoodBarCustomizationItem object, LayoutEditorScreen handler) {
		super(object, handler);
	}
	
	@Override
	public void init() {
		
		super.init();
		
		this.rightclickMenu.addSeparator();
		
		CustomFoodBarCustomizationItem i = (CustomFoodBarCustomizationItem) this.getObject();
		
		/** HIDE WHEN FULL **/
		String hideWhenFullLabel = Locals.localize("fancyhud.helper.editor.elements.vanilla.bars.hidewhenfull.on");
		if (!i.hideWhenFull) {
			hideWhenFullLabel = Locals.localize("fancyhud.helper.editor.elements.vanilla.bars.hidewhenfull.off");
		}
		AdvancedButton hideWhenFullButton = new AdvancedButton(0, 0, 0, 16, hideWhenFullLabel, true, (press) -> {
			if (i.hideWhenFull) {
				((AdvancedButton)press).setMessage(Locals.localize("fancyhud.helper.editor.elements.vanilla.bars.hidewhenfull.off"));
				i.hideWhenFull = false;
			} else {
				((AdvancedButton)press).setMessage(Locals.localize("fancyhud.helper.editor.elements.vanilla.bars.hidewhenfull.on"));
				i.hideWhenFull = true;
			}
		});
		hideWhenFullButton.setDescription(StringUtils.splitLines(Locals.localize("fancyhud.helper.editor.elements.vanilla.bars.hidewhenfull.btn.desc"), "%n%"));
		this.rightclickMenu.addContent(hideWhenFullButton);
		
		/** MAX FOOD **/
		AdvancedButton maxFoodButton = new AdvancedButton(0, 0, 0, 16, Locals.localize("fancyhud.helper.creator.add.customfoodbar.maxfood"), true, (press) -> {
			FHTextInputPopup pop = new FHTextInputPopup(new Color(0, 0, 0, 0), Locals.localize("fancyhud.helper.creator.add.customfoodbar.maxfood"), CharacterFilter.getIntegerCharacterFiler(), 240, (call) -> {
				if (call != null) {
					if (MathUtils.isInteger(call)) {
						int mf = Integer.parseInt(call);
						if (mf != i.maxFood) {
							this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
						}
						i.maxFood = mf;
					}
				}
			});
			pop.setText("" + i.maxFood);
			PopupHandler.displayPopup(pop);
		});
		this.rightclickMenu.addContent(maxFoodButton);
		
	}
	
	@Override
	protected PropertiesSection getPropertiesRaw() {
		PropertiesSection s = super.getPropertiesRaw();
		s.addEntry("action", "addcustomfoodbar");
		s.addEntry("maxfood", "" + ((CustomFoodBarCustomizationItem)this.getObject()).maxFood);
		s.addEntry("hidewhenfull", "" + ((CustomFoodBarCustomizationItem)this.getObject()).hideWhenFull);
		return s;
	}

}
