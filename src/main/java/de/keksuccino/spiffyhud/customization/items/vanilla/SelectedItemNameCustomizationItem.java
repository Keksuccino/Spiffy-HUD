package de.keksuccino.spiffyhud.customization.items.vanilla;

import com.mojang.blaze3d.vertex.PoseStack;

import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.SelectedItemNameHudElement;
import de.keksuccino.konkrete.properties.PropertiesSection;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;

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
	public void render(PoseStack matrix) {
		
		SelectedItemNameHudElement e = (SelectedItemNameHudElement) this.element;
		
		if (!this.colorHex.equals("") || !this.isSecondItemOfThisType) {
			e.colorHex = this.colorHex;
		}
		
		//Updating width + height before rendering element
		if (!this.element.getHandler().isEditor()) {
			Component text = null;
			ItemStack is = this.element.getHandler().getHighlightingItemStack();
			if (is != null) {
				MutableComponent textComp = (new TextComponent("")).append(is.getHoverName()).withStyle(is.getRarity().color);
				if (is.hasCustomHoverName()) {
					textComp.withStyle(ChatFormatting.ITALIC);
				}
				text = is.getHighlightTip(textComp);
			}

			this.element.width = 10;
			if (text != null) {
				this.element.width = Minecraft.getInstance().font.width(text);
			}
			this.element.height = Minecraft.getInstance().font.lineHeight;
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
