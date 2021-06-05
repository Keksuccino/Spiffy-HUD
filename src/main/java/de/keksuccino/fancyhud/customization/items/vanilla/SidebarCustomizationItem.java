package de.keksuccino.fancyhud.customization.items.vanilla;

import com.mojang.blaze3d.matrix.MatrixStack;

import de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements.SidebarHudElement;
import de.keksuccino.konkrete.properties.PropertiesSection;

public class SidebarCustomizationItem extends VanillaCustomizationItem {

	protected boolean isDefaultPos = false;
	
	public String customHeadlineBackgroundColorHex = null;
	public String customBodyBackgroundColorHex = null;
	
	public String customHeadlineTextBaseColorHex = null;
	public String customNameTextBaseColorHex = null;
	public String customScoreTextBaseColorHex = null;
	
	public SidebarCustomizationItem(SidebarHudElement element, PropertiesSection props, boolean isSecondItemOfThisType) {
		super(element, "Sidebar", props, isSecondItemOfThisType);
		
		if (this.isOriginalOrientation) {
			this.orientation = "mid-right";
		}
		
		this.customHeadlineBackgroundColorHex = props.getEntryValue("headlinebackhex");
		this.customBodyBackgroundColorHex = props.getEntryValue("bodybackhex");
		
		this.customHeadlineTextBaseColorHex = props.getEntryValue("headlinetexthex");
		this.customNameTextBaseColorHex = props.getEntryValue("nametexthex");
		this.customScoreTextBaseColorHex = props.getEntryValue("scoretexthex");
		
	}
	
	@Override
	public void render(MatrixStack matrix) {
		
		SidebarHudElement he = (SidebarHudElement) this.element;

		if (this.isOriginalOrientation) {
			this.isDefaultPos = true;
		}
		
		if (!this.orientation.equals("mid-right")) {
			this.isOriginalOrientation = false;
		}

		if (this.posX == Integer.MAX_VALUE) {
			this.posX = -1;
			this.isOriginalPosX = true;
		}
		if ((this.posX != -1) || !this.isOriginalOrientation) {
			this.isOriginalPosX = false;
		}

		if (this.posY == Integer.MAX_VALUE) {
			this.posY = 0;
			this.isOriginalPosY = true;
		}
		if ((this.posY != 0) || !this.isOriginalOrientation) {
			this.isOriginalPosY = false;
		}
		
		if ((this.customHeadlineBackgroundColorHex != null) || !this.isSecondItemOfThisType) {
			he.customHeadlineBackgroundColorHex = this.customHeadlineBackgroundColorHex;
		}
		if ((this.customBodyBackgroundColorHex != null) || !this.isSecondItemOfThisType) {
			he.customBodyBackgroundColorHex = this.customBodyBackgroundColorHex;
		}
		
		if ((this.customHeadlineTextBaseColorHex != null) || !this.isSecondItemOfThisType) {
			he.customHeadlineTextBaseColorHex = this.customHeadlineTextBaseColorHex;
		}
		if ((this.customNameTextBaseColorHex != null) || !this.isSecondItemOfThisType) {
			he.customNameTextBaseColorHex = this.customNameTextBaseColorHex;
		}
		if ((this.customScoreTextBaseColorHex != null) || !this.isSecondItemOfThisType) {
			he.customScoreTextBaseColorHex = this.customScoreTextBaseColorHex;
		}
		
		super.render(matrix);
	}

}
