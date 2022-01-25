package de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements;

import java.awt.Color;
import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.spiffyhud.api.InGameHudOverlay;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import de.keksuccino.konkrete.rendering.RenderUtils;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameMode;

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

		this.renderElement = InGameHudOverlay.isRenderingEnabledForElement("selecteditem");
		this.elementActive = InGameHudOverlay.isElementActive("selecteditem");

		if (!this.lastColorHex.equals(this.colorHex) && (this.colorHex != null)) {
			this.color = RenderUtils.getColorFromHexString(this.colorHex);
		}
		this.lastColorHex = this.colorHex;

		if (mc.options.heldItemTooltips && mc.interactionManager.getCurrentGameMode() != GameMode.SPECTATOR) {
			this.renderSelectedItemName(matrix, scaledWidth, scaledHeight);
		} else if (mc.player.isSpectator()) {
			handler.getSpectatorHud().render(matrix);
		}
		
	}
	
	protected void renderSelectedItemName(MatrixStack matrix, int scaledWidth, int scaledHeight) {
		
		this.height = this.mc.textRenderer.fontHeight;
		this.width = 20;

		if (!this.renderElement) {
			return;
		}
		if (!this.visible) {
			return;
		}
		
		this.mc.getProfiler().push("selectedItemName");
		if ((this.handler.getRemainingHighlightingTicks() > 0 && !this.handler.getHighlightingItemStack().isEmpty()) || this.handler.isEditor()) {
			
			Text highlightTip = new LiteralText("Selected Item Name");
			
			if (!this.handler.isEditor()) {
				MutableText iformattabletextcomponent = (new LiteralText("")).append(this.handler.getHighlightingItemStack().getName()).formatted(this.handler.getHighlightingItemStack().getRarity().formatting);
				if (this.handler.getHighlightingItemStack().hasCustomName()) {
					iformattabletextcomponent.formatted(Formatting.ITALIC);
				}
				
				highlightTip = iformattabletextcomponent;
			}
			
			int i = this.mc.textRenderer.getWidth(highlightTip);
			int posX = this.x; //(scaledWidth - i) / 2;
			int posY = this.y; //scaledHeight - 59;
			if (!this.mc.interactionManager.hasStatusBars()) {
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
				RenderSystem.enableBlend();
				RenderSystem.defaultBlendFunc();
				fill(matrix, posX - 2, posY - 2, posX + i + 2, posY + 9 + 2, this.mc.options.getTextBackgroundColor(0));
				TextRenderer font = null;
				if (!this.handler.isEditor()) {
					font = handler.getTextRenderer();
				}
				int c = 16777215;
				if (this.color != null) {
					c = this.color.getRGB();
				}
				if (font == null) {
					this.mc.textRenderer.drawWithShadow(matrix, highlightTip, (float)posX, (float)posY, c + (l << 24));
				} else {
					int i2 = font.getWidth(highlightTip);
					posX = (scaledWidth - i2) / 2;
					font.drawWithShadow(matrix, highlightTip, (float)posX, (float)posY, c + (l << 24));
					this.width = i2;
					this.height = font.fontHeight;
				}
				RenderSystem.disableBlend();
			}

		}

		this.mc.getProfiler().pop();
		
	}

}
