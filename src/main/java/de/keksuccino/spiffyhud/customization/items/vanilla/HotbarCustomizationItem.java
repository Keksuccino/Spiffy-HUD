package de.keksuccino.spiffyhud.customization.items.vanilla;

import com.mojang.blaze3d.matrix.MatrixStack;

import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.IngameHudElement;
import de.keksuccino.konkrete.properties.PropertiesSection;

public class HotbarCustomizationItem extends VanillaCustomizationItem {

	public HotbarCustomizationItem(IngameHudElement element, PropertiesSection props, boolean isSecondItemOfThisType) {
		super(element, "Hotbar", props, isSecondItemOfThisType);
		
		if (this.isOriginalOrientation) {
			this.orientation = "bottom-centered";
		}
		
	}
	
	@Override
	public void render(MatrixStack matrix) {

		if (!this.orientation.equals("bottom-centered")) {
			this.isOriginalOrientation = false;
		}

		if (this.posX == Integer.MAX_VALUE) {
			this.posX = 0;
			this.isOriginalPosX = true;
		}
		if ((this.posX != 0) || !this.isOriginalOrientation) {
			this.isOriginalPosX = false;
		}

		if (this.posY == Integer.MAX_VALUE) {
			this.posY = 0;
			this.isOriginalPosY = true;
		}
		if ((this.posY != 0) || !this.isOriginalOrientation) {
			this.isOriginalPosY = false;
		}
		
		super.render(matrix);
	}

}
