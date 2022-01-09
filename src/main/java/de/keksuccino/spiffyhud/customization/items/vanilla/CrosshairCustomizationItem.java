package de.keksuccino.spiffyhud.customization.items.vanilla;

import java.io.File;

import com.mojang.blaze3d.matrix.MatrixStack;

import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.CrosshairHudElement;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.IngameHudElement;
import de.keksuccino.spiffyhud.logger.Logging;
import de.keksuccino.konkrete.properties.PropertiesSection;
import de.keksuccino.konkrete.resources.TextureHandler;

public class CrosshairCustomizationItem extends VanillaCustomizationItem {

	public boolean invertCrosshairColors = true;
	public boolean renderAttackIndicator = true;
	public String crosshairTexturePath = null;
	protected String lastCrosshairTexturePath = null;
	
	public CrosshairCustomizationItem(IngameHudElement element, PropertiesSection props, boolean isSecondItemOfThisType) {
		super(element, "Crosshair", props, isSecondItemOfThisType);
		
		if (this.isOriginalOrientation) {
			this.orientation = "mid-centered";
		}
		
		String crossTexture = props.getEntryValue("texture");
		if (crossTexture != null) {
			File f = new File(crossTexture);
			if (f.exists() && f.isFile() && (f.getPath().toLowerCase().endsWith(".png") || f.getPath().toLowerCase().endsWith(".jpg") || f.getPath().toLowerCase().endsWith(".jpeg"))) {
				this.crosshairTexturePath = crossTexture;
			} else {
				Logging.error("CrosshairCustomizationItem: Invalid crosshair texture: " + crossTexture);
			}
		}
		
		String invert = props.getEntryValue("invertcolors");
		if ((invert != null) && invert.equalsIgnoreCase("false")) {
			this.invertCrosshairColors = false;
		}
		
		String attackindic = props.getEntryValue("attackindicator");
		if ((attackindic != null) && attackindic.equalsIgnoreCase("false")) {
			this.renderAttackIndicator = false;
		}
		
	}
	
	@Override
	public void render(MatrixStack matrix) {
		
		CrosshairHudElement e = (CrosshairHudElement) this.element;
		
		if ((this.crosshairTexturePath == null) || !this.crosshairTexturePath.equals(this.lastCrosshairTexturePath)) {
			if (this.crosshairTexturePath == null) {
				if (!this.isSecondItemOfThisType) {
					e.crosshairTexture = null;
				}
			} else {
				e.crosshairTexture = TextureHandler.getResource(this.crosshairTexturePath);
			}
		}
		this.lastCrosshairTexturePath = this.crosshairTexturePath;
		
		if (!this.invertCrosshairColors || !this.isSecondItemOfThisType) {
			e.invertCrosshairColors = this.invertCrosshairColors;
		}
		
		if (!this.renderAttackIndicator || !this.isSecondItemOfThisType) {
			e.renderAttackIndicator = this.renderAttackIndicator;
		}

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
			this.posY = 0;
			this.isOriginalPosY = true;
		}
		if ((this.posY != 0) || !this.isOriginalOrientation) {
			this.isOriginalPosY = false;
		}
		
		super.render(matrix);
		
	}

}
