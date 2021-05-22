package de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements;

import java.awt.Color;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.fancyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import de.keksuccino.konkrete.rendering.RenderUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.ITextComponent;

public class OverlayMessageHudElement extends IngameHudElement {

	private static final int WHITE = 0xFFFFFF;
	
	public String colorHex = "";
	protected String lastColorHex = "";
	protected Color color;
	
	public OverlayMessageHudElement(CustomizableIngameGui handler) {
		super(handler);
	}

	@Override
	public void render(MatrixStack matrix, int scaledWidth, int scaledHeight, float partialTicks) {

		if (!this.lastColorHex.equals(this.colorHex) && (this.colorHex != null)) {
			this.color = RenderUtils.getColorFromHexString(this.colorHex);
		}
		this.lastColorHex = this.colorHex;
		
		this.renderOverlayMessage(matrix, scaledWidth, scaledHeight, partialTicks);
		
	}
	
	protected void renderOverlayMessage( MatrixStack matrix, int scaledWidth, int scaledHeight, float partialTicks) {
		
		this.width = 10;
		this.height = mc.fontRenderer.FONT_HEIGHT;
		
		if (!this.visible) {
			return;
		}
		
		ITextComponent overlayMessage = new StringTextComponent("Overlay Status Message");
		if (!this.handler.isEditor()) {
			overlayMessage = this.handler.getOverlayMessage();
		}
		
		if (overlayMessage != null) {

			if ((this.handler.getOverlayMessageTime() > 0) || this.handler.isEditor()) {
				mc.getProfiler().startSection("overlayMessage");
				float hue = (float)this.handler.getOverlayMessageTime() - partialTicks;
				int opacity = 255;
				if (!this.handler.isEditor()) {
					opacity = (int)(hue * 255.0F / 20.0F);
				}
				if (opacity > 255) {
					opacity = 255;
				}
				int posX = this.x;
				int posY = this.y; // -72
				int stringWidth = mc.fontRenderer.getStringPropertyWidth(overlayMessage);
				
				this.width = stringWidth;
				this.height = mc.fontRenderer.FONT_HEIGHT;
				
				int baseColor = WHITE;
				if (this.color != null) {
					baseColor = this.color.getRGB();
				}
				if (opacity > 8) {
					RenderSystem.enableBlend();
//					RenderSystem.defaultBlendFunc();
					int c = (this.handler.getAnimateOverlayMessageColor() ? MathHelper.hsvToRGB(hue / 50.0F, 0.7F, 0.6F) & baseColor : baseColor);
//					this.handler.func_238448_a_(matrix, this.mc.fontRenderer, -4, this.mc.fontRenderer.getStringPropertyWidth(overlayMessage), 16777215 | (opacity << 24));
					this.mc.fontRenderer.func_238422_b_(matrix, overlayMessage.func_241878_f(), posX, posY, c | (MathHelper.ceil(opacity) << 24));
					RenderSystem.disableBlend();
				}

				mc.getProfiler().endSection();
			}

		}
		
	}

}
