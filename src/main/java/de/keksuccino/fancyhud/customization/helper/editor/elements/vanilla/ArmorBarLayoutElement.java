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
		
		BarAlignment a = ((ArmorBarCustomizationItem)this.getVanillaObject()).barAlignment;
		if ((a != null) && (a != BarAlignment.LEFT)) {
			p.addEntry("alignment", a.getName());
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
