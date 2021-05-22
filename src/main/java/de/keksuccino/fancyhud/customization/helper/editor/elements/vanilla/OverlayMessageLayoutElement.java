package de.keksuccino.fancyhud.customization.helper.editor.elements.vanilla;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import de.keksuccino.fancyhud.customization.helper.editor.LayoutEditorScreen;
import de.keksuccino.fancyhud.customization.helper.ui.popup.FHTextInputPopup;
import de.keksuccino.fancyhud.customization.items.vanilla.OverlayMessageCustomizationItem;
import de.keksuccino.konkrete.gui.content.AdvancedButton;
import de.keksuccino.konkrete.gui.screens.popup.PopupHandler;
import de.keksuccino.konkrete.input.StringUtils;
import de.keksuccino.konkrete.localization.Locals;
import de.keksuccino.konkrete.properties.PropertiesSection;

public class OverlayMessageLayoutElement extends VanillaLayoutElement {

	public OverlayMessageLayoutElement(OverlayMessageCustomizationItem object, LayoutEditorScreen handler) {
		super(object, handler);
	}
	
	@Override
	public void init() {
		super.init();

		this.rightclickMenu.addSeparator();
		
		OverlayMessageCustomizationItem ob = (OverlayMessageCustomizationItem) this.getVanillaObject();
		
		/** BASE COLOR **/
		AdvancedButton lvlColorButton = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancyhud.helper.editor.elements.vanilla.overlaymessage.color"), (press) -> {
			FHTextInputPopup pop = new FHTextInputPopup(new Color(0, 0, 0, 0), Locals.localize("fancyhud.helper.editor.elements.vanilla.overlaymessage.color"), null, 240, (call) -> {
				if (call != ob.colorHex) {
					this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
				}
				if (call != null) {
					ob.colorHex = call;
				} else {
					ob.colorHex = "";
				}
			});
			pop.setText(ob.colorHex);
			PopupHandler.displayPopup(pop);
		});
		lvlColorButton.setDescription(StringUtils.splitLines(Locals.localize("fancyhud.helper.editor.elements.vanilla.overlaymessage.color.desc", ""), "%n%"));
		this.rightclickMenu.addContent(lvlColorButton);
		
	}

	@Override
	public List<PropertiesSection> getProperties() {
		List<PropertiesSection> l = new ArrayList<PropertiesSection>();
		PropertiesSection p = new PropertiesSection("customization");
		
		p.addEntry("action", "editoverlaymessage");
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
		
		OverlayMessageCustomizationItem ob = (OverlayMessageCustomizationItem) this.getVanillaObject();
		
		if ((ob.colorHex != null) && !ob.colorHex.equals("")) {
			p.addEntry("basecolor", ob.colorHex);
		}
		
		if (p.getEntries().size() > 1) {
			l.add(p);
		}
		
		return l;
	}
	
	@Override
	public void resetElement() {
		PropertiesSection props = new PropertiesSection("customization");
		this.object = new OverlayMessageCustomizationItem(this.handler.ingameHud.overlayMessageElement, props, false);
	}

}
