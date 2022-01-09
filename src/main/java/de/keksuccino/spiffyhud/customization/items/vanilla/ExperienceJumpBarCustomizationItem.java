package de.keksuccino.spiffyhud.customization.items.vanilla;

import com.mojang.blaze3d.matrix.MatrixStack;

import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.ExperienceJumpBarHudElement;
import de.keksuccino.konkrete.properties.PropertiesSection;

public class ExperienceJumpBarCustomizationItem extends VanillaCustomizationItem {

	public boolean showLvl = true;
	public String lvlColorHex = "";
	
	public ExperienceJumpBarCustomizationItem(ExperienceJumpBarHudElement element, PropertiesSection props, boolean isSecondItemOfThisType) {
		super(element, "Experience Bar", props, isSecondItemOfThisType);
		
		if (this.isOriginalOrientation) {
			this.orientation = "bottom-centered";
		}
		
		String lvlHex = props.getEntryValue("levelcolor");
		if (lvlHex != null) {
			this.lvlColorHex = lvlHex;
		}
		
		String visLvl = props.getEntryValue("showlevel");
		if ((visLvl != null) && visLvl.equalsIgnoreCase("false")) {
			this.showLvl = false;
		}
		
	}
	
	@Override
	public void render(MatrixStack matrix) {
		
		if (!this.lvlColorHex.equals("") || !this.isSecondItemOfThisType) {
			((ExperienceJumpBarHudElement)this.element).lvlColorHex = this.lvlColorHex;
		}
		
		if (!this.showLvl || !this.isSecondItemOfThisType) {
			((ExperienceJumpBarHudElement)this.element).showLvl = this.showLvl;
		}
		
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
			this.posY = -24; //-29
			this.isOriginalPosY = true;
		}
		if ((this.posY != -24) || !this.isOriginalOrientation) {
			this.isOriginalPosY = false;
		}
		
		super.render(matrix);
	}

}
