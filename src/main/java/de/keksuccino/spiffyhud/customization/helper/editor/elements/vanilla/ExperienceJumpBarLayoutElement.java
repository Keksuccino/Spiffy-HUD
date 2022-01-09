package de.keksuccino.spiffyhud.customization.helper.editor.elements.vanilla;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import de.keksuccino.spiffyhud.customization.helper.editor.LayoutEditorScreen;
import de.keksuccino.spiffyhud.customization.helper.ui.popup.FHTextInputPopup;
import de.keksuccino.spiffyhud.customization.items.vanilla.ExperienceJumpBarCustomizationItem;
import de.keksuccino.konkrete.gui.content.AdvancedButton;
import de.keksuccino.konkrete.gui.screens.popup.PopupHandler;
import de.keksuccino.konkrete.input.StringUtils;
import de.keksuccino.konkrete.localization.Locals;
import de.keksuccino.konkrete.properties.PropertiesSection;

public class ExperienceJumpBarLayoutElement extends VanillaLayoutElement {

	public ExperienceJumpBarLayoutElement(ExperienceJumpBarCustomizationItem object, LayoutEditorScreen handler) {
		super(object, handler);
	}
	
	@Override
	public void init() {
		super.init();

		this.rightclickMenu.addSeparator();
		
		ExperienceJumpBarCustomizationItem ob = (ExperienceJumpBarCustomizationItem) this.getVanillaObject();
		
		/** LEVEL VISIBILITY **/
		String showLvlString = Locals.localize("spiffyhud.helper.editor.elements.vanilla.exp.level.visibility.on");
		if (!ob.showLvl) {
			showLvlString = Locals.localize("spiffyhud.helper.editor.elements.vanilla.exp.level.visibility.off");
		}
		AdvancedButton showLvlButton = new AdvancedButton(0, 0, 0, 0, showLvlString, (press) -> {
			this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
			if (ob.showLvl) {
				((AdvancedButton)press).setMessage(Locals.localize("spiffyhud.helper.editor.elements.vanilla.exp.level.visibility.off"));
				ob.showLvl = false;
			} else {
				((AdvancedButton)press).setMessage(Locals.localize("spiffyhud.helper.editor.elements.vanilla.exp.level.visibility.on"));
				ob.showLvl = true;
			}
		});
		this.rightclickMenu.addContent(showLvlButton);
		
		/** LEVEL COLOR **/
		AdvancedButton lvlColorButton = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.editor.elements.vanilla.exp.level.color"), (press) -> {
			FHTextInputPopup pop = new FHTextInputPopup(new Color(0, 0, 0, 0), Locals.localize("spiffyhud.helper.editor.elements.vanilla.exp.level.color"), null, 240, (call) -> {
				if (call != ob.lvlColorHex) {
					this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
				}
				if (call != null) {
					ob.lvlColorHex = call;
				} else {
					ob.lvlColorHex = "";
				}
			});
			pop.setText(ob.lvlColorHex);
			PopupHandler.displayPopup(pop);
		});
		lvlColorButton.setDescription(StringUtils.splitLines(Locals.localize("spiffyhud.helper.editor.elements.vanilla.exp.level.color.desc", ""), "%n%"));
		this.rightclickMenu.addContent(lvlColorButton);
		
	}

	@Override
	public List<PropertiesSection> getProperties() {
		List<PropertiesSection> l = new ArrayList<PropertiesSection>();
		PropertiesSection p = new PropertiesSection("customization");
		
		p.addEntry("action", "editexperiencebar");
		if (!this.getVanillaObject().isOriginalOrientation) {
			p.addEntry("orientation", this.object.orientation);
		}
		if (!this.getVanillaObject().isOriginalPosX) {
			p.addEntry("x", "" + this.object.posX);
		}
		if (!this.getVanillaObject().isOriginalPosY) {
			p.addEntry("y", "" + this.object.posY);
		}
		if (!this.getVanillaObject().vanillaVisible) {
			p.addEntry("visible", "" + this.getVanillaObject().vanillaVisible);
		}
		if (!this.getVanillaObject().fireEvents) {
			p.addEntry("fireevents", "" + this.getVanillaObject().fireEvents);
		}
		
		ExperienceJumpBarCustomizationItem ob = (ExperienceJumpBarCustomizationItem) this.getVanillaObject();
		
		if (!ob.showLvl) {
			p.addEntry("showlevel", "false");
		}
		if (!ob.lvlColorHex.equals("")) {
			p.addEntry("levelcolor", ob.lvlColorHex);
		}
		
		if (p.getEntries().size() > 1) {
			l.add(p);
		}
		
		return l;
	}
	
	@Override
	public void resetElement() {
		PropertiesSection props = new PropertiesSection("customization");
		this.object = new ExperienceJumpBarCustomizationItem(this.handler.ingameHud.experienceJumpBarElement, props, false);
	}

}
