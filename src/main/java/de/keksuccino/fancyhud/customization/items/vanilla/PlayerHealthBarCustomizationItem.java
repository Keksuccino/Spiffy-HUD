package de.keksuccino.fancyhud.customization.items.vanilla;

import com.mojang.blaze3d.matrix.MatrixStack;

import de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements.IngameHudElement;
import de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements.PlayerHealthHudElement;
import de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements.IngameHudElement.BarAlignment;
import de.keksuccino.konkrete.properties.PropertiesSection;

public class PlayerHealthBarCustomizationItem extends VanillaCustomizationItem {

	public BarAlignment barAlignment = BarAlignment.LEFT;
	
	public PlayerHealthBarCustomizationItem(IngameHudElement element, PropertiesSection props, boolean isSecondItemOfThisType) {
		super(element, "Player Health Bar", props, isSecondItemOfThisType);
		
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

		if (this.isOriginalOrientation) {
			((PlayerHealthHudElement)this.element).isDefaultPos = true;
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
			this.posY = -30; //-39
			this.isOriginalPosY = true;
		}
		if ((this.posY != -30) || !this.isOriginalOrientation) {
			this.isOriginalPosY = false;
		}

		if (this.posX != -51) {
			((PlayerHealthHudElement)this.element).isDefaultPos = false;
		}
		
		if ((this.barAlignment != BarAlignment.LEFT) || !this.isSecondItemOfThisType) {
			((PlayerHealthHudElement)this.element).alignment = this.barAlignment;
		}
		
		super.render(matrix);
	}

}
