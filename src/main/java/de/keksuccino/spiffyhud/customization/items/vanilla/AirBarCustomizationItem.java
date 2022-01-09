package de.keksuccino.spiffyhud.customization.items.vanilla;

import com.mojang.blaze3d.matrix.MatrixStack;

import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.AirBarHudElement;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.IngameHudElement;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.IngameHudElement.BarAlignment;
import de.keksuccino.konkrete.properties.PropertiesSection;

public class AirBarCustomizationItem extends VanillaCustomizationItem {

	protected boolean isDefaultPos = false;
	
	public BarAlignment barAlignment = BarAlignment.RIGHT;
	
	public AirBarCustomizationItem(IngameHudElement element, PropertiesSection props, boolean isSecondItemOfThisType) {
		super(element, "Air Bar", props, isSecondItemOfThisType);
		
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
		
	}
	
	@Override
	public void render(MatrixStack matrix) {
		
		AirBarHudElement he = (AirBarHudElement) this.element;

		if (this.isOriginalOrientation) {
			this.isDefaultPos = true;
		}
		
		if (!this.orientation.equals("bottom-centered")) {
			this.isOriginalOrientation = false;
		}

		if (this.posX == Integer.MAX_VALUE) {
			this.posX = 51;
			this.isOriginalPosX = true;
		}
		if ((this.posX != 51) || !this.isOriginalOrientation) {
			this.isOriginalPosX = false;
		}

		if (this.posY == Integer.MAX_VALUE) {
			this.posY = -(30 + 10);
			this.isOriginalPosY = true;
		}
		if ((this.posY != -(30 + 10)) || !this.isOriginalOrientation) {
			this.isOriginalPosY = false;
		}

		if ((this.posX != 51) || !he.isFoodBarAtDefaultPos() || this.element.getHandler().isEditor()) {
			this.isDefaultPos = false;
		}

		if (isDefaultPos) {
			if (he.isFoodBarAtDefaultPos()) {
				this.posY = -(30 + he.getCurrentFoodBarHeight());
			}
		}
		
		if ((this.barAlignment != BarAlignment.RIGHT) || !this.isSecondItemOfThisType) {
			((AirBarHudElement)this.element).alignment = this.barAlignment;
		}
		
		super.render(matrix);
	}

}
