package de.keksuccino.spiffyhud.customization.helper.editor.elements.vanilla;

import java.util.ArrayList;
import java.util.List;

import de.keksuccino.spiffyhud.customization.helper.editor.LayoutEditorScreen;
import de.keksuccino.spiffyhud.customization.items.vanilla.TitleCustomizationItem;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.TitleHudElement;
import de.keksuccino.konkrete.properties.PropertiesSection;

public class TitleLayoutElement extends VanillaLayoutElement {

	public TitleLayoutElement(TitleCustomizationItem object, LayoutEditorScreen handler) {
		super(object, handler);
	}

	@Override
	public List<PropertiesSection> getProperties() {
		List<PropertiesSection> l = new ArrayList<>();
		PropertiesSection p = new PropertiesSection("customization");
		
		if (((TitleHudElement)this.getVanillaObject().element).isSubtitle) {
			p.addEntry("action", "editsubtitle");
		} else {
			p.addEntry("action", "edittitle");
		}
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
		
		if (p.getEntries().size() > 1) {
			l.add(p);
		}
		
		return l;
	}

	@Override
	public void resetElement() {
		boolean isSubtitle = ((TitleHudElement)this.getVanillaObject().element).isSubtitle;
		PropertiesSection props = new PropertiesSection("customization");
		if (isSubtitle) {
			this.object = new TitleCustomizationItem(this.handler.ingameHud.subtitleElement, props, false);
		} else {
			this.object = new TitleCustomizationItem(this.handler.ingameHud.titleElement, props, false);
		}
	}

}
