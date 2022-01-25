package de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements;

import java.awt.Color;

import de.keksuccino.spiffyhud.api.InGameHudOverlay;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.spiffyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import de.keksuccino.konkrete.rendering.RenderUtils;

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

		this.renderElement = InGameHudOverlay.isRenderingEnabledForElement("overlaymessage");
		this.elementActive = InGameHudOverlay.isElementActive("overlaymessage");

		if (!this.lastColorHex.equals(this.colorHex) && (this.colorHex != null)) {
			this.color = RenderUtils.getColorFromHexString(this.colorHex);
		}
		this.lastColorHex = this.colorHex;

		this.renderOverlayMessage(matrix, partialTicks);
		
	}
	
	protected void renderOverlayMessage( MatrixStack matrix, float partialTicks) {
		
		this.width = 10;
		this.height = mc.textRenderer.fontHeight;

		if (!this.renderElement) {
			return;
		}
		if (!this.visible) {
			return;
		}
		
		Text overlayMessage = new LiteralText("Overlay Status Message");
		if (!this.handler.isEditor()) {
			overlayMessage = this.handler.getOverlayMessageText();
		}
		
		if (overlayMessage != null) {

			if ((this.handler.getOverlayMessageTime() > 0) || this.handler.isEditor()) {
				mc.getProfiler().push("overlayMessage");
				float hue = (float)this.handler.getOverlayMessageTime() - partialTicks;
				int opacity = 255;
				if (!this.handler.isEditor()) {
					opacity = (int)(hue * 255.0F / 20.0F);
				}
				if (opacity > 255) {
					opacity = 255;
				}
				int posX = this.x;
				int posY = this.y;
				int stringWidth = mc.textRenderer.getWidth(overlayMessage);
				
				this.width = stringWidth;
				this.height = mc.textRenderer.fontHeight;
				
				int baseColor = WHITE;
				if (this.color != null) {
					baseColor = this.color.getRGB();
				}
				if (opacity > 8) {
					RenderSystem.enableBlend();
					int c = (this.handler.getAnimateOverlayMessageColor() ? MathHelper.hsvToRgb(hue / 50.0F, 0.7F, 0.6F) & baseColor : baseColor);
					this.mc.textRenderer.draw(matrix, overlayMessage.asOrderedText(), posX, posY, c | (MathHelper.ceil(opacity) << 24));
					RenderSystem.disableBlend();
				}

				mc.getProfiler().pop();
			}

		}
		
	}

}
