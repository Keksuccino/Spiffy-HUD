package de.keksuccino.fancyhud.customization.helper.editor.elements.vanilla;

import java.util.ArrayList;
import java.util.List;

import de.keksuccino.fancyhud.customization.helper.editor.LayoutEditorScreen;
import de.keksuccino.fancyhud.customization.helper.ui.popup.ChooseFilePopup;
import de.keksuccino.fancyhud.customization.items.vanilla.CrosshairCustomizationItem;
import de.keksuccino.konkrete.gui.content.AdvancedButton;
import de.keksuccino.konkrete.gui.screens.popup.PopupHandler;
import de.keksuccino.konkrete.input.StringUtils;
import de.keksuccino.konkrete.localization.Locals;
import de.keksuccino.konkrete.properties.PropertiesSection;

public class CrosshairLayoutElement extends VanillaLayoutElement {

	public CrosshairLayoutElement(CrosshairCustomizationItem object, LayoutEditorScreen handler) {
		super(object, handler);
	}
	
	@Override
	public void init() {
		super.init();

		this.rightclickMenu.addSeparator();
		
		CrosshairCustomizationItem ob = (CrosshairCustomizationItem) this.getVanillaObject();
		
		/** CROSSHAIR TEXTURE **/
		AdvancedButton textureButton = new AdvancedButton(0, 0, 0, 0, Locals.localize("fancyhud.helper.editor.elements.vanilla.crosshair.texture"), (press) -> {
			ChooseFilePopup pop = new ChooseFilePopup((call) -> {
				
				if (call != ob.crosshairTexturePath) {
					this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
				}
				
				if (call.equals("") || (call == null)) {
					ob.crosshairTexturePath = null;
				} else {
					ob.crosshairTexturePath = call;
				}
				
			}, "png", "jpeg", "jpg");
			if (ob.crosshairTexturePath != null) {
				pop.setText(ob.crosshairTexturePath);
			}
			PopupHandler.displayPopup(pop);
		});
		textureButton.setDescription(StringUtils.splitLines(Locals.localize("fancyhud.helper.editor.elements.vanilla.crosshair.texture.desc", ""), "%n%"));
		this.rightclickMenu.addContent(textureButton);
		
		/** INVERT COLORS **/
		String invertString = Locals.localize("fancyhud.helper.editor.elements.vanilla.crosshair.invertcolors.on");
		if (!ob.invertCrosshairColors) {
			invertString = Locals.localize("fancyhud.helper.editor.elements.vanilla.crosshair.invertcolors.off");
		}
		AdvancedButton invertButton = new AdvancedButton(0, 0, 0, 0, invertString, (press) -> {
			if (ob.invertCrosshairColors) {
				ob.invertCrosshairColors = false;
				((AdvancedButton)press).setMessage(Locals.localize("fancyhud.helper.editor.elements.vanilla.crosshair.invertcolors.off"));
			} else {
				ob.invertCrosshairColors = true;
				((AdvancedButton)press).setMessage(Locals.localize("fancyhud.helper.editor.elements.vanilla.crosshair.invertcolors.on"));
			}
		});
		invertButton.setDescription(StringUtils.splitLines(Locals.localize("fancyhud.helper.editor.elements.vanilla.crosshair.invertcolors.desc"), "%n%"));
		this.rightclickMenu.addContent(invertButton);
		
		/** ATTACK INDICATOR **/
		String attackIndicString = Locals.localize("fancyhud.helper.editor.elements.vanilla.crosshair.attackindicator.on");
		if (!ob.renderAttackIndicator) {
			attackIndicString = Locals.localize("fancyhud.helper.editor.elements.vanilla.crosshair.attackindicator.off");
		}
		AdvancedButton attackIndicatorButton = new AdvancedButton(0, 0, 0, 0, attackIndicString, (press) -> {
			if (ob.renderAttackIndicator) {
				ob.renderAttackIndicator = false;
				((AdvancedButton)press).setMessage(Locals.localize("fancyhud.helper.editor.elements.vanilla.crosshair.attackindicator.off"));
			} else {
				ob.renderAttackIndicator = true;
				((AdvancedButton)press).setMessage(Locals.localize("fancyhud.helper.editor.elements.vanilla.crosshair.attackindicator.on"));
			}
		});
		attackIndicatorButton.setDescription(StringUtils.splitLines(Locals.localize("fancyhud.helper.editor.elements.vanilla.crosshair.attackindicator.desc"), "%n%"));
		this.rightclickMenu.addContent(attackIndicatorButton);
		
	}

	@Override
	public List<PropertiesSection> getProperties() {
		List<PropertiesSection> l = new ArrayList<PropertiesSection>();
		PropertiesSection p = new PropertiesSection("customization");
		
		p.addEntry("action", "editcrosshair");
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
		
		CrosshairCustomizationItem ob = (CrosshairCustomizationItem) this.getVanillaObject();
		
		if (ob.crosshairTexturePath != null) {
			p.addEntry("texture", ob.crosshairTexturePath);
		}
		
		if (!ob.renderAttackIndicator) {
			p.addEntry("attackindicator", "false");
		}
		
		if (!ob.invertCrosshairColors) {
			p.addEntry("invertcolors", "false");
		}
		
		if (p.getEntries().size() > 1) {
			l.add(p);
		}
		
		return l;
	}
	
	@Override
	public void resetElement() {
		PropertiesSection props = new PropertiesSection("customization");
		this.object = new CrosshairCustomizationItem(this.handler.ingameHud.crosshairElement, props, false);
	}

}
