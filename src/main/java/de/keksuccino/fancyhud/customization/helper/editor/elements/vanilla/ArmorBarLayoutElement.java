package de.keksuccino.fancyhud.customization.helper.editor.elements.vanilla;

import java.util.ArrayList;
import java.util.List;

import de.keksuccino.fancyhud.customization.helper.editor.LayoutEditorScreen;
import de.keksuccino.fancyhud.customization.items.vanilla.ArmorBarCustomizationItem;
import de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements.IngameHudElement.BarAlignment;
import de.keksuccino.konkrete.gui.content.AdvancedButton;
import de.keksuccino.konkrete.input.StringUtils;
import de.keksuccino.konkrete.localization.Locals;
import de.keksuccino.konkrete.properties.PropertiesSection;

public class ArmorBarLayoutElement extends VanillaLayoutElement {

	public ArmorBarLayoutElement(ArmorBarCustomizationItem object, LayoutEditorScreen handler) {
		super(object, handler);
	}
	
	@Override
	public void init() {
		
		this.scaleable = true;
		
		super.init();

		this.rightclickMenu.addSeparator();
		
		/** BAR ALIGNMENT **/
		String alignString = Locals.localize("fancyhud.helper.editor.elements.vanilla.bars.alignment.left");
		if (((ArmorBarCustomizationItem)this.getVanillaObject()).barAlignment == BarAlignment.RIGHT) {
			alignString = Locals.localize("fancyhud.helper.editor.elements.vanilla.bars.alignment.right");
		}
		AdvancedButton barAlignmentButton = new AdvancedButton(0, 0, 0, 0, alignString, (press) -> {
			this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
			if (((ArmorBarCustomizationItem)this.getVanillaObject()).barAlignment == BarAlignment.RIGHT) {
				((AdvancedButton)press).setMessage(Locals.localize("fancyhud.helper.editor.elements.vanilla.bars.alignment.left"));
				((ArmorBarCustomizationItem)this.getVanillaObject()).barAlignment = BarAlignment.LEFT;
			} else {
				((AdvancedButton)press).setMessage(Locals.localize("fancyhud.helper.editor.elements.vanilla.bars.alignment.right"));
				((ArmorBarCustomizationItem)this.getVanillaObject()).barAlignment = BarAlignment.RIGHT;
			}
		});
		barAlignmentButton.setDescription(StringUtils.splitLines(Locals.localize("fancyhud.helper.editor.elements.vanilla.bars.alignment.btn.desc", ""), "%n%"));
		this.rightclickMenu.addContent(barAlignmentButton);
		
		/** HIDE WHEN EMPTY **/
		String hideFullString = Locals.localize("fancyhud.helper.editor.elements.vanilla.bars.hidewhenempty.on");
		if (!((ArmorBarCustomizationItem)this.getVanillaObject()).hideWhenEmpty) {
			hideFullString = Locals.localize("fancyhud.helper.editor.elements.vanilla.bars.hidewhenempty.off");
		}
		AdvancedButton hideWhenFullButton = new AdvancedButton(0, 0, 0, 0, hideFullString, (press) -> {
			this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
			if (((ArmorBarCustomizationItem)this.getVanillaObject()).hideWhenEmpty) {
				((AdvancedButton)press).setMessage(Locals.localize("fancyhud.helper.editor.elements.vanilla.bars.hidewhenempty.off"));
				((ArmorBarCustomizationItem)this.getVanillaObject()).hideWhenEmpty = false;
			} else {
				((AdvancedButton)press).setMessage(Locals.localize("fancyhud.helper.editor.elements.vanilla.bars.hidewhenempty.on"));
				((ArmorBarCustomizationItem)this.getVanillaObject()).hideWhenEmpty = true;
			}
		});
		hideWhenFullButton.setDescription(StringUtils.splitLines(Locals.localize("fancyhud.helper.editor.elements.vanilla.bars.hidewhenempty.btn.desc"), "%n%"));
		this.rightclickMenu.addContent(hideWhenFullButton);
		
	}

	@Override
	public List<PropertiesSection> getProperties() {
		List<PropertiesSection> l = new ArrayList<PropertiesSection>();
		PropertiesSection p = new PropertiesSection("customization");
		
		p.addEntry("action", "editarmorbar");
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
			p.addEntry("visible", "false");
		}
		if (!this.getVanillaObject().fireEvents) {
			p.addEntry("fireevents", "" + this.getVanillaObject().fireEvents);
		}
		if (this.getVanillaObject().scale != 1.0F) {
			p.addEntry("scale", "" + this.getVanillaObject().scale);
		}
		
		BarAlignment a = ((ArmorBarCustomizationItem)this.getVanillaObject()).barAlignment;
		if ((a != null) && (a != BarAlignment.LEFT)) {
			p.addEntry("alignment", a.getName());
		}
		
		if (((ArmorBarCustomizationItem)this.getVanillaObject()).hideWhenEmpty) {
			p.addEntry("hidewhenempty", "true");
		}
		
		if (p.getEntries().size() > 1) {
			l.add(p);
		}
		
		return l;
	}
	
	@Override
	public void resetElement() {
		PropertiesSection props = new PropertiesSection("customization");
		this.object = new ArmorBarCustomizationItem(this.handler.ingameHud.armorBarElement, props, false);
	}

}
