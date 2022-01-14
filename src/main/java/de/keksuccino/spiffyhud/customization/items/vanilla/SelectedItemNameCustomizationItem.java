package de.keksuccino.spiffyhud.customization.items.vanilla;

import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.SelectedItemNameHudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import de.keksuccino.konkrete.properties.PropertiesSection;

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
			ItemStack is = this.element.getHandler().getHighlightingItemStack();
			MutableText textComp = null;
			if (is != null) {
				textComp = (new LiteralText("")).append(is.getName()).formatted(is.getRarity().formatting);
				if (is.hasCustomName()) {
					textComp.formatted(Formatting.ITALIC);
				}
			}

			this.element.width = 10;
			if (textComp != null) {
				this.element.width = MinecraftClient.getInstance().textRenderer.getWidth(textComp);
			}
			this.element.height = MinecraftClient.getInstance().textRenderer.fontHeight;
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
