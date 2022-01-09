package de.keksuccino.spiffyhud.customization.items.vanilla;

import com.mojang.blaze3d.vertex.PoseStack;

import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.TitleHudElement;
import de.keksuccino.konkrete.properties.PropertiesSection;

public class TitleCustomizationItem extends VanillaCustomizationItem {
	
	public TitleCustomizationItem(TitleHudElement element, PropertiesSection props, boolean isSecondItemOfThisType) {
		super(element, "Title", props, isSecondItemOfThisType);
		if (((TitleHudElement)this.element).isSubtitle) {
			this.value = "Subtitle";
		}
		
		if (this.isOriginalOrientation) {
			this.orientation = "mid-centered";
		}
		
	}
	
	@Override
	public void render(PoseStack matrix) {
			
		if (!this.orientation.equals("mid-centered")) {
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
			if (((TitleHudElement)this.element).isSubtitle) {
				this.posY = 20;
			} else {
				this.posY = -20;
			}
			this.isOriginalPosY = true;
		}
		if (((this.posY != 20) && (this.posY != -20)) || !this.isOriginalOrientation) {
			this.isOriginalPosY = false;
		}
		
		super.render(matrix);
	}

}
