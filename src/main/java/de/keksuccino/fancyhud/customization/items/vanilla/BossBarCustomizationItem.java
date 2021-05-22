package de.keksuccino.fancyhud.customization.items.vanilla;

import com.mojang.blaze3d.matrix.MatrixStack;

import de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements.IngameHudElement;
import de.keksuccino.konkrete.properties.PropertiesSection;

public class BossBarCustomizationItem extends VanillaCustomizationItem {

	public BossBarCustomizationItem(IngameHudElement element, PropertiesSection props, boolean isSecondItemOfThisType) {
		super(element, "Boss Bar", props, isSecondItemOfThisType);
		
		if (this.isOriginalOrientation) {
			this.orientation = "top-centered";
		}
		
	}
	
	@Override
	public void render(MatrixStack matrix) {
		
		if (!this.orientation.equals("top-centered")) {
			this.isOriginalOrientation = false;
		}

		if (this.posX == Integer.MAX_VALUE) {
			this.posX = 0; //-91
			this.isOriginalPosX = true;
		}
		if ((this.posX != 0) || !this.isOriginalOrientation) {
			this.isOriginalPosX = false;
		}

		if (this.posY == Integer.MAX_VALUE) {
			this.posY = 12;
			this.isOriginalPosY = true;
		}
		if ((this.posY != 12) || !this.isOriginalOrientation) {
			this.isOriginalPosY = false;
		}

		super.render(matrix);
	}

}
