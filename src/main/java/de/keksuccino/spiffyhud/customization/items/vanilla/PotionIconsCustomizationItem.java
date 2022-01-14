package de.keksuccino.spiffyhud.customization.items.vanilla;

import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.IngameHudElement;
import net.minecraft.client.util.math.MatrixStack;
import de.keksuccino.konkrete.properties.PropertiesSection;

public class PotionIconsCustomizationItem extends VanillaCustomizationItem {

	public PotionIconsCustomizationItem(IngameHudElement element, PropertiesSection props, boolean isSecondItemOfThisType) {
		super(element, "Potion Icons", props, isSecondItemOfThisType);
		
		if (this.isOriginalOrientation) {
			this.orientation = "top-right";
		}
		
	}
	
	@Override
	public void render(MatrixStack matrix) {

		if (!this.orientation.equals("top-right")) {
			this.isOriginalOrientation = false;
		}

		if (this.posX == Integer.MAX_VALUE) {
			this.posX = 1;
			this.isOriginalPosX = true;
		}
		if ((this.posX != 1) || !this.isOriginalOrientation) {
			this.isOriginalPosX = false;
		}

		if (this.posY == Integer.MAX_VALUE) {
			this.posY = 1;
			this.isOriginalPosY = true;
		}
		if ((this.posY != 1) || !this.isOriginalOrientation) {
			this.isOriginalPosY = false;
		}
		
		super.render(matrix);
	}

}
