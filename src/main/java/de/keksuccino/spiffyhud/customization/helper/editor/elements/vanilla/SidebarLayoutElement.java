package de.keksuccino.spiffyhud.customization.helper.editor.elements.vanilla;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import de.keksuccino.spiffyhud.customization.helper.editor.LayoutEditorScreen;
import de.keksuccino.spiffyhud.customization.helper.ui.popup.FHTextInputPopup;
import de.keksuccino.spiffyhud.customization.items.vanilla.SidebarCustomizationItem;
import de.keksuccino.konkrete.gui.content.AdvancedButton;
import de.keksuccino.konkrete.gui.screens.popup.PopupHandler;
import de.keksuccino.konkrete.localization.Locals;
import de.keksuccino.konkrete.properties.PropertiesSection;

public class SidebarLayoutElement extends VanillaLayoutElement {

	public SidebarLayoutElement(SidebarCustomizationItem object, LayoutEditorScreen handler) {
		super(object, handler);
	}
	
	@Override
	public void init() {
		
		SidebarCustomizationItem i = (SidebarCustomizationItem) this.getVanillaObject();
		
		super.init();

		this.rightclickMenu.addSeparator();
		
		/** HEADLINE BACK COLOR **/
		AdvancedButton headlineBackColorButton = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.editor.elements.vanilla.sidebar.headlinebackcolor"), (press) -> {
			FHTextInputPopup pop = new FHTextInputPopup(new Color(0, 0, 0, 0), Locals.localize("spiffyhud.helper.editor.elements.vanilla.sidebar.headlinebackcolor"), null, 240, (call) -> {
				if (call != null) {
					if (!call.equals(i.customHeadlineBackgroundColorHex)) {
						this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
					}
					if (call.replace(" ", "").equals("")) {
						i.customHeadlineBackgroundColorHex = null;
					} else {
						i.customHeadlineBackgroundColorHex = call;
					}
				}
			});
			if (i.customHeadlineBackgroundColorHex != null) {
				pop.setText(i.customHeadlineBackgroundColorHex);
			}
			PopupHandler.displayPopup(pop);
		});
		this.rightclickMenu.addContent(headlineBackColorButton);
		
		/** BODY BACK COLOR **/
		AdvancedButton bodyBackColorButton = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.editor.elements.vanilla.sidebar.bodybackcolor"), (press) -> {
			FHTextInputPopup pop = new FHTextInputPopup(new Color(0, 0, 0, 0), Locals.localize("spiffyhud.helper.editor.elements.vanilla.sidebar.bodybackcolor"), null, 240, (call) -> {
				if (call != null) {
					if (!call.equals(i.customBodyBackgroundColorHex)) {
						this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
					}
					if (call.replace(" ", "").equals("")) {
						i.customBodyBackgroundColorHex = null;
					} else {
						i.customBodyBackgroundColorHex = call;
					}
				}
			});
			if (i.customBodyBackgroundColorHex != null) {
				pop.setText(i.customBodyBackgroundColorHex);
			}
			PopupHandler.displayPopup(pop);
		});
		this.rightclickMenu.addContent(bodyBackColorButton);
		
		this.rightclickMenu.addSeparator();
		
		/** HEADLINE TEXT COLOR **/
		AdvancedButton headlineTextColorButton = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.editor.elements.vanilla.sidebar.headlinetextcolor"), (press) -> {
			FHTextInputPopup pop = new FHTextInputPopup(new Color(0, 0, 0, 0), Locals.localize("spiffyhud.helper.editor.elements.vanilla.sidebar.headlinetextcolor"), null, 240, (call) -> {
				if (call != null) {
					if (!call.equals(i.customHeadlineTextBaseColorHex)) {
						this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
					}
					if (call.replace(" ", "").equals("")) {
						i.customHeadlineTextBaseColorHex = null;
					} else {
						i.customHeadlineTextBaseColorHex = call;
					}
				}
			});
			if (i.customHeadlineTextBaseColorHex != null) {
				pop.setText(i.customHeadlineTextBaseColorHex);
			}
			PopupHandler.displayPopup(pop);
		});
		this.rightclickMenu.addContent(headlineTextColorButton);
		
		/** NAME TEXT COLOR **/
		AdvancedButton nameTextColorButton = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.editor.elements.vanilla.sidebar.nametextcolor"), (press) -> {
			FHTextInputPopup pop = new FHTextInputPopup(new Color(0, 0, 0, 0), Locals.localize("spiffyhud.helper.editor.elements.vanilla.sidebar.nametextcolor"), null, 240, (call) -> {
				if (call != null) {
					if (!call.equals(i.customNameTextBaseColorHex)) {
						this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
					}
					if (call.replace(" ", "").equals("")) {
						i.customNameTextBaseColorHex = null;
					} else {
						i.customNameTextBaseColorHex = call;
					}
				}
			});
			if (i.customNameTextBaseColorHex != null) {
				pop.setText(i.customNameTextBaseColorHex);
			}
			PopupHandler.displayPopup(pop);
		});
		this.rightclickMenu.addContent(nameTextColorButton);
		
		/** SCORE TEXT COLOR **/
		AdvancedButton scoreTextColorButton = new AdvancedButton(0, 0, 0, 0, Locals.localize("spiffyhud.helper.editor.elements.vanilla.sidebar.scoretextcolor"), (press) -> {
			FHTextInputPopup pop = new FHTextInputPopup(new Color(0, 0, 0, 0), Locals.localize("spiffyhud.helper.editor.elements.vanilla.sidebar.scoretextcolor"), null, 240, (call) -> {
				if (call != null) {
					if (!call.equals(i.customScoreTextBaseColorHex)) {
						this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
					}
					if (call.replace(" ", "").equals("")) {
						i.customScoreTextBaseColorHex = null;
					} else {
						i.customScoreTextBaseColorHex = call;
					}
				}
			});
			if (i.customScoreTextBaseColorHex != null) {
				pop.setText(i.customScoreTextBaseColorHex);
			}
			PopupHandler.displayPopup(pop);
		});
		this.rightclickMenu.addContent(scoreTextColorButton);
		
	}

	@Override
	public List<PropertiesSection> getProperties() {
		List<PropertiesSection> l = new ArrayList<PropertiesSection>();
		PropertiesSection p = new PropertiesSection("customization");
		
		p.addEntry("action", "editsidebar");
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
//		if (this.getVanillaObject().scale != 1.0F) {
//			p.addEntry("scale", "" + this.getVanillaObject().scale);
//		}
		
		SidebarCustomizationItem i = (SidebarCustomizationItem) this.getVanillaObject();
		
		if (i.customHeadlineBackgroundColorHex != null) {
			p.addEntry("headlinebackhex", i.customHeadlineBackgroundColorHex);
		}
		if (i.customBodyBackgroundColorHex != null) {
			p.addEntry("bodybackhex", i.customBodyBackgroundColorHex);
		}
		
		if (i.customHeadlineTextBaseColorHex != null) {
			p.addEntry("headlinetexthex", i.customHeadlineTextBaseColorHex);
		}
		if (i.customNameTextBaseColorHex != null) {
			p.addEntry("nametexthex", i.customNameTextBaseColorHex);
		}
		if (i.customScoreTextBaseColorHex != null) {
			p.addEntry("scoretexthex", i.customScoreTextBaseColorHex);
		}
		
		if (p.getEntries().size() > 1) {
			l.add(p);
		}
		
		return l;
	}
	
	@Override
	public void resetElement() {
		PropertiesSection props = new PropertiesSection("customization");
		this.object = new SidebarCustomizationItem(this.handler.ingameHud.sidebarElement, props, false);
	}

}
