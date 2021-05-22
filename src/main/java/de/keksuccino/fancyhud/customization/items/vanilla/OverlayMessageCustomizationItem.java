package de.keksuccino.fancyhud.customization.items.vanilla;

import com.mojang.blaze3d.matrix.MatrixStack;

import de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements.OverlayMessageHudElement;
import de.keksuccino.konkrete.properties.PropertiesSection;
import net.minecraft.client.Minecraft;

public class OverlayMessageCustomizationItem extends VanillaCustomizationItem {

	public String colorHex = "";
	
	public OverlayMessageCustomizationItem(OverlayMessageHudElement element, PropertiesSection props, boolean isSecondItemOfThisType) {
		super(element, "Overlay Status Message", props, isSecondItemOfThisType);
		
		if (this.isOriginalOrientation) {
			this.orientation = "bottom-centered";
		}
		
		String color = props.getEntryValue("basecolor");
		if (color != null) {
			this.colorHex = color;
		}
		
	}
	
	@Override
	public void render(MatrixStack matrix) {

		OverlayMessageHudElement e = (OverlayMessageHudElement) this.element;
		
		if (!this.colorHex.equals("") || !this.isSecondItemOfThisType) {
			e.colorHex = this.colorHex;
		}
		
		//Updating width + height before rendering element
		if (!this.element.getHandler().isEditor()) {
			this.element.width = 10;
			if (this.element.getHandler().getOverlayMessage() != null) {
				this.element.width = Minecraft.getInstance().fontRenderer.getStringPropertyWidth(this.element.getHandler().getOverlayMessage());
			}
			this.element.height = Minecraft.getInstance().fontRenderer.FONT_HEIGHT;
		}
		//--------------------------
		
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
			this.posY = -62;
			this.isOriginalPosY = true;
		}
		if ((this.posY != -62) || !this.isOriginalOrientation) {
			this.isOriginalPosY = false;
		}
		
		super.render(matrix);
	}

}
