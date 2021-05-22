package de.keksuccino.fancyhud.customization.items.vanilla;

import com.mojang.blaze3d.matrix.MatrixStack;

import de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements.SelectedItemNameHudElement;
import de.keksuccino.konkrete.properties.PropertiesSection;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class SelectedItemNameCustomizationItem extends VanillaCustomizationItem {

	public String colorHex = "";
	
	public SelectedItemNameCustomizationItem(SelectedItemNameHudElement element, PropertiesSection props, boolean isSecondItemOfThisType) {
		super(element, "Selected Item Name", props, isSecondItemOfThisType);
		
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
		
		SelectedItemNameHudElement e = (SelectedItemNameHudElement) this.element;
		
		if (!this.colorHex.equals("") || !this.isSecondItemOfThisType) {
			e.colorHex = this.colorHex;
		}
		
		//Updating width + height before rendering element
		if (!this.element.getHandler().isEditor()) {
			ITextComponent text = null;
			ItemStack is = this.element.getHandler().getHighlightingItemStack();
			if (is != null) {
				IFormattableTextComponent textComp = (new StringTextComponent("")).append(is.getDisplayName()).mergeStyle(is.getRarity().color);
				if (is.hasDisplayName()) {
					textComp.mergeStyle(TextFormatting.ITALIC);
				}
				text = is.getHighlightTip(textComp);
			}

			this.element.width = 10;
			if (text != null) {
				this.element.width = Minecraft.getInstance().fontRenderer.getStringPropertyWidth(text);
			}
			this.element.height = Minecraft.getInstance().fontRenderer.FONT_HEIGHT;
		}
		//-----------------------
		
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
			this.posY = -49;
			this.isOriginalPosY = true;
		}
		if ((this.posY != -49) || !this.isOriginalOrientation) {
			this.isOriginalPosY = false;
		}
		
		super.render(matrix);
	}

}
