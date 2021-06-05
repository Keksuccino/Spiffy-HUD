package de.keksuccino.fancyhud.customization.items.vanilla;

import com.mojang.blaze3d.matrix.MatrixStack;

import de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements.ArmorBarHudElement;
import de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements.IngameHudElement;
import de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements.IngameHudElement.BarAlignment;
import de.keksuccino.konkrete.properties.PropertiesSection;

public class ArmorBarCustomizationItem extends VanillaCustomizationItem {

	public boolean hideWhenEmpty = false;
	
	protected boolean isDefaultPos = false;
	
	public BarAlignment barAlignment = BarAlignment.LEFT;
	
	public ArmorBarCustomizationItem(IngameHudElement element, PropertiesSection props, boolean isSecondItemOfThisType) {
		super(element, "Armor Bar", props, isSecondItemOfThisType);
		
		if (this.isOriginalOrientation) {
			this.orientation = "bottom-centered";
		}
		
		String align = props.getEntryValue("alignment");
		if ((align != null)) {
			BarAlignment a = BarAlignment.byName(align);
			if (a != null) {
				this.barAlignment = a;
			}
		}
		
		String hideEmpty = props.getEntryValue("hidewhenempty");
		if ((hideEmpty != null) && hideEmpty.equalsIgnoreCase("true")) {
			this.hideWhenEmpty = true;
		}
		
	}
	
	@Override
	public void render(MatrixStack matrix) {
		
		ArmorBarHudElement he = (ArmorBarHudElement) this.element;
		
		if (this.hideWhenEmpty || !this.isSecondItemOfThisType) {
			he.hideWhenEmpty = this.hideWhenEmpty;
		}

		if (this.isOriginalOrientation) {
			this.isDefaultPos = true;
		}
		
		if (!this.orientation.equals("bottom-centered")) {
			this.isOriginalOrientation = false;
		}

		if (this.posX == Integer.MAX_VALUE) {
			this.posX = -51; //-91
			this.isOriginalPosX = true;
		}
		if ((this.posX != -51) || !this.isOriginalOrientation) {
			this.isOriginalPosX = false;
		}

		if (this.posY == Integer.MAX_VALUE) {
			this.posY = -(30 + 10); //(39 + 10)
			this.isOriginalPosY = true;
		}
		if ((this.posY != -(30 + 10)) || !this.isOriginalOrientation) {
			this.isOriginalPosY = false;
		}

		if ((this.posX != -51) || !he.isHealthBarAtDefaultPos() || this.element.getHandler().isEditor()) {
			this.isDefaultPos = false;
		}

		if (isDefaultPos) {
			if (he.isHealthBarAtDefaultPos()) {
				this.posY = -(30 + he.getCurrentHealthBarHeight());
			}
		}
		
		if ((this.barAlignment != BarAlignment.LEFT) || !this.isSecondItemOfThisType) {
			((ArmorBarHudElement)this.element).alignment = this.barAlignment;
		}
		
		super.render(matrix);
	}

}
