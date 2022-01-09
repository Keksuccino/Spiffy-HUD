package de.keksuccino.spiffyhud.customization.helper.editor.elements.vanilla;

import java.util.ArrayList;
import java.util.List;

import de.keksuccino.spiffyhud.customization.helper.editor.LayoutEditorScreen;
import de.keksuccino.spiffyhud.customization.items.vanilla.FoodMountHealthCustomizationItem;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.IngameHudElement.BarAlignment;
import de.keksuccino.konkrete.gui.content.AdvancedButton;
import de.keksuccino.konkrete.input.StringUtils;
import de.keksuccino.konkrete.localization.Locals;
import de.keksuccino.konkrete.properties.PropertiesSection;

public class FoodMountHealthLayoutElement extends VanillaLayoutElement {

	public FoodMountHealthLayoutElement(FoodMountHealthCustomizationItem object, LayoutEditorScreen handler) {
		super(object, handler);
	}
	
	@Override
	public void init() {
		
		this.scaleable = true;
		
		super.init();

		this.rightclickMenu.addSeparator();
		
		/** BAR ALIGNMENT **/
		String alignString = Locals.localize("spiffyhud.helper.editor.elements.vanilla.bars.alignment.left");
		if (((FoodMountHealthCustomizationItem)this.getVanillaObject()).barAlignment == BarAlignment.RIGHT) {
			alignString = Locals.localize("spiffyhud.helper.editor.elements.vanilla.bars.alignment.right");
		}
		AdvancedButton barAlignmentButton = new AdvancedButton(0, 0, 0, 0, alignString, (press) -> {
			this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
			if (((FoodMountHealthCustomizationItem)this.getVanillaObject()).barAlignment == BarAlignment.RIGHT) {
				((AdvancedButton)press).setMessage(Locals.localize("spiffyhud.helper.editor.elements.vanilla.bars.alignment.left"));
				((FoodMountHealthCustomizationItem)this.getVanillaObject()).barAlignment = BarAlignment.LEFT;
			} else {
				((AdvancedButton)press).setMessage(Locals.localize("spiffyhud.helper.editor.elements.vanilla.bars.alignment.right"));
				((FoodMountHealthCustomizationItem)this.getVanillaObject()).barAlignment = BarAlignment.RIGHT;
			}
		});
		barAlignmentButton.setDescription(StringUtils.splitLines(Locals.localize("spiffyhud.helper.editor.elements.vanilla.bars.alignment.btn.desc", ""), "%n%"));
		this.rightclickMenu.addContent(barAlignmentButton);
		
		/** HIDE WHEN FULL **/
		String hideFullString = Locals.localize("spiffyhud.helper.editor.elements.vanilla.bars.hidewhenfull.on");
		if (!((FoodMountHealthCustomizationItem)this.getVanillaObject()).hideWhenFull) {
			hideFullString = Locals.localize("spiffyhud.helper.editor.elements.vanilla.bars.hidewhenfull.off");
		}
		AdvancedButton hideWhenFullButton = new AdvancedButton(0, 0, 0, 0, hideFullString, (press) -> {
			this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
			if (((FoodMountHealthCustomizationItem)this.getVanillaObject()).hideWhenFull) {
				((AdvancedButton)press).setMessage(Locals.localize("spiffyhud.helper.editor.elements.vanilla.bars.hidewhenfull.off"));
				((FoodMountHealthCustomizationItem)this.getVanillaObject()).hideWhenFull = false;
			} else {
				((AdvancedButton)press).setMessage(Locals.localize("spiffyhud.helper.editor.elements.vanilla.bars.hidewhenfull.on"));
				((FoodMountHealthCustomizationItem)this.getVanillaObject()).hideWhenFull = true;
			}
		});
		hideWhenFullButton.setDescription(StringUtils.splitLines(Locals.localize("spiffyhud.helper.editor.elements.vanilla.bars.hidewhenfull.btn.desc"), "%n%"));
		this.rightclickMenu.addContent(hideWhenFullButton);
		
	}

	@Override
	public List<PropertiesSection> getProperties() {
		List<PropertiesSection> l = new ArrayList<PropertiesSection>();
		PropertiesSection p = new PropertiesSection("customization");
		
		p.addEntry("action", "editplayerfoodbar");
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
		if (this.getVanillaObject().scale != 1.0F) {
			p.addEntry("scale", "" + this.getVanillaObject().scale);
		}
		
		BarAlignment a = ((FoodMountHealthCustomizationItem)this.getVanillaObject()).barAlignment;
		if ((a != null) && (a != BarAlignment.RIGHT)) {
			p.addEntry("alignment", a.getName());
		}
		
		if (((FoodMountHealthCustomizationItem)this.getVanillaObject()).hideWhenFull) {
			p.addEntry("hidewhenfull", "true");
		}
		
		if (p.getEntries().size() > 1) {
			l.add(p);
		}
		
		return l;
	}
	
	@Override
	public void resetElement() {
		PropertiesSection props = new PropertiesSection("customization");
		this.object = new FoodMountHealthCustomizationItem(this.handler.ingameHud.foodMountHealthElement, props, false);
	}
	
}
