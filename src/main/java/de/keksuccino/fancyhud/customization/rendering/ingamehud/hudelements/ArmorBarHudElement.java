package de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.fancyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

public class ArmorBarHudElement extends IngameHudElement {
	
	public BarAlignment alignment = BarAlignment.LEFT;
	
	public ArmorBarHudElement(CustomizableIngameGui handler) {
		super(handler);
		
		this.width = 80;
		this.height = 9;
	}

	@Override
	public void render(MatrixStack matrix, int scaledWidth, int scaledHeight, float partialTicks) {

		if (this.fireEvents) {
			if (this.handler.pre(ElementType.ARMOR, matrix, false)) return;
		}
		
		if (this.visible) {
			this.renderArmorRaw(matrix);
		}
		
		if (this.fireEvents) {
			this.handler.post(ElementType.ARMOR, matrix, false);
		}
		
	}

	
	protected void renderArmorRaw(MatrixStack mStack) {

		mc.getProfiler().startSection("armor");

		RenderSystem.enableBlend();
		int left = this.x;
		if (this.alignment == BarAlignment.RIGHT) {
			left = this.x + 90 - 18;
		}
		int top = this.y;

		int level = 10;
		if (!this.handler.isEditor()) {
			level = mc.player.getTotalArmorValue();
		}
		for (int i = 1; level > 0 && i < 20; i += 2) {

			if (i < level) {
				blit(mStack, left, top, 34, 9, 9, 9);
			}
			else if (i == level) {
				blit(mStack, left, top, 25, 9, 9, 9);
			}
			else if (i > level) {
				blit(mStack, left, top, 16, 9, 9, 9);
			}

			if (this.alignment == BarAlignment.RIGHT) {
				left -= 8;
			} else {
				left += 8;
			}
		}

		RenderSystem.disableBlend();
		mc.getProfiler().endSection();

	}
	
	public int getCurrentHealthBarHeight() {
		return this.handler.healthElement.currentHealthHeight;
	}
	
	public boolean isHealthBarAtDefaultPos() {
		return this.handler.healthElement.isDefaultPos;
	}

}
