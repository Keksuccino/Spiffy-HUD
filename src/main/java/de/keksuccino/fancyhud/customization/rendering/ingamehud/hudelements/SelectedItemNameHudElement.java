package de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements;

import java.awt.Color;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.fancyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import de.keksuccino.konkrete.rendering.RenderUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class SelectedItemNameHudElement extends IngameHudElement {

	//Not really working, because every item has it's own formatting, so the base color is never visible
	public String colorHex = "";
	protected String lastColorHex = "";
	protected Color color;
	
	public SelectedItemNameHudElement(CustomizableIngameGui handler) {
		super(handler);
		
		//this.width = 10;
		//this.height = 10;
	}

	@Override
	public void render(MatrixStack matrix, int scaledWidth, int scaledHeight, float partialTicks) {
		
		if (!this.lastColorHex.equals(this.colorHex) && (this.colorHex != null)) {
			this.color = RenderUtils.getColorFromHexString(this.colorHex);
		}
		this.lastColorHex = this.colorHex;
		
		this.renderSelectedItemName(matrix, scaledWidth, scaledHeight);
		
	}
	
	protected void renderSelectedItemName(MatrixStack matrix, int scaledWidth, int scaledHeight) {
		
		this.height = this.mc.fontRenderer.FONT_HEIGHT;
		this.width = 20;
		
		if (!this.visible) {
			return;
		}
		
		this.mc.getProfiler().startSection("selectedItemName");
		if ((this.handler.getRemainingHighlightingTicks() > 0 && !this.handler.getHighlightingItemStack().isEmpty()) || this.handler.isEditor()) {
			
			ITextComponent highlightTip = new StringTextComponent("Selected Item Name");
			
			if (!this.handler.isEditor()) {
				IFormattableTextComponent iformattabletextcomponent = (new StringTextComponent("")).append(this.handler.getHighlightingItemStack().getDisplayName()).mergeStyle(this.handler.getHighlightingItemStack().getRarity().color);
				if (this.handler.getHighlightingItemStack().hasDisplayName()) {
					iformattabletextcomponent.mergeStyle(TextFormatting.ITALIC);
				}
				
				highlightTip = this.handler.getHighlightingItemStack().getHighlightTip(iformattabletextcomponent);
			}
			
			int i = this.mc.fontRenderer.getStringPropertyWidth(highlightTip);
			int posX = this.x; //(scaledWidth - i) / 2;
			int posY = this.y; //scaledHeight - 59;
			if (!this.mc.playerController.shouldDrawHUD()) {
				posY += 14;
			}
			
			this.width = i;

			int l = 255;
			if (!this.handler.isEditor()) {
				l = (int)((float)this.handler.getRemainingHighlightingTicks() * 256.0F / 10.0F);
			}
			if (l > 255) {
				l = 255;
			}

			if (l > 0) {
				RenderSystem.pushMatrix();
				RenderSystem.enableBlend();
				RenderSystem.defaultBlendFunc();
				fill(matrix, posX - 2, posY - 2, posX + i + 2, posY + 9 + 2, this.mc.gameSettings.getChatBackgroundColor(0));
				FontRenderer font = null;
				if (!this.handler.isEditor()) {
					font = this.handler.getHighlightingItemStack().getItem().getFontRenderer(this.handler.getHighlightingItemStack());
				}
				int c = 16777215;
				if (this.color != null) {
					c = this.color.getRGB();
				}
				if (font == null) {
					this.mc.fontRenderer.func_243246_a(matrix, highlightTip, (float)posX, (float)posY, c + (l << 24));
				} else {
					int i2 = font.getStringPropertyWidth(highlightTip);
					posX = (scaledWidth - i2) / 2;
					font.func_243246_a(matrix, highlightTip, (float)posX, (float)posY, c + (l << 24));
					this.width = i2;
					this.height = font.FONT_HEIGHT;
				}
				RenderSystem.disableBlend();
				RenderSystem.popMatrix();
			}
		}

		this.mc.getProfiler().endSection();
		
	}

}
