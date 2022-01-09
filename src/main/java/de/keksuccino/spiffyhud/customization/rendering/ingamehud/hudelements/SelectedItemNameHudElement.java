package de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements;

import java.awt.Color;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.spiffyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import de.keksuccino.konkrete.rendering.RenderUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.GameType;
import net.minecraftforge.client.gui.ForgeIngameGui;

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
	public void render(PoseStack matrix, int scaledWidth, int scaledHeight, float partialTicks) {

		if (this.fireEvents) {
			if (handler.pre(ForgeIngameGui.ITEM_NAME_ELEMENT, matrix)) return;
		}

		if (!this.lastColorHex.equals(this.colorHex) && (this.colorHex != null)) {
			this.color = RenderUtils.getColorFromHexString(this.colorHex);
		}
		this.lastColorHex = this.colorHex;

		if (mc.options.heldItemTooltips && mc.gameMode.getPlayerMode() != GameType.SPECTATOR) {
			this.renderSelectedItemName(matrix, scaledWidth, scaledHeight);
		} else if (mc.player.isSpectator()) {
			handler.getSpectatorGui().renderTooltip(matrix);
		}

		if (this.fireEvents) {
			handler.post(ForgeIngameGui.ITEM_NAME_ELEMENT, matrix);
		}
		
	}
	
	protected void renderSelectedItemName(PoseStack matrix, int scaledWidth, int scaledHeight) {
		
		this.height = this.mc.font.lineHeight;
		this.width = 20;
		
		if (!this.visible) {
			return;
		}
		
		this.mc.getProfiler().push("selectedItemName");
		if ((this.handler.getRemainingHighlightingTicks() > 0 && !this.handler.getHighlightingItemStack().isEmpty()) || this.handler.isEditor()) {
			
			Component highlightTip = new TextComponent("Selected Item Name");
			
			if (!this.handler.isEditor()) {
				MutableComponent iformattabletextcomponent = (new TextComponent("")).append(this.handler.getHighlightingItemStack().getHoverName()).withStyle(this.handler.getHighlightingItemStack().getRarity().color);
				if (this.handler.getHighlightingItemStack().hasCustomHoverName()) {
					iformattabletextcomponent.withStyle(ChatFormatting.ITALIC);
				}
				
				highlightTip = this.handler.getHighlightingItemStack().getHighlightTip(iformattabletextcomponent);
			}
			
			int i = this.mc.font.width(highlightTip);
			int posX = this.x; //(scaledWidth - i) / 2;
			int posY = this.y; //scaledHeight - 59;
			if (!this.mc.gameMode.canHurtPlayer()) {
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
				fill(matrix, posX - 2, posY - 2, posX + i + 2, posY + 9 + 2, this.mc.options.getBackgroundColor(0));
				Font font = null;
				if (!this.handler.isEditor()) {
					font = net.minecraftforge.client.RenderProperties.get(handler.getHighlightingItemStack()).getFont(handler.getHighlightingItemStack());
				}
				int c = 16777215;
				if (this.color != null) {
					c = this.color.getRGB();
				}
				if (font == null) {
					this.mc.font.drawShadow(matrix, highlightTip, (float)posX, (float)posY, c + (l << 24));
				} else {
					int i2 = font.width(highlightTip);
					posX = (scaledWidth - i2) / 2;
					font.drawShadow(matrix, highlightTip, (float)posX, (float)posY, c + (l << 24));
					this.width = i2;
					this.height = font.lineHeight;
				}
				RenderSystem.disableBlend();
			}

		}

		this.mc.getProfiler().pop();
		
	}

}
