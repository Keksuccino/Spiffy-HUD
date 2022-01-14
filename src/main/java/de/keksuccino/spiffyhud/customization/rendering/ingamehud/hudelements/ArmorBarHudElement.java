package de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements;

import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.spiffyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import net.minecraft.client.util.math.MatrixStack;

public class ArmorBarHudElement extends IngameHudElement {
	
	public BarAlignment alignment = BarAlignment.LEFT;
	public boolean hideWhenEmpty = false;
	
	public ArmorBarHudElement(CustomizableIngameGui handler) {
		super(handler);
		
		this.width = (int) (80 * this.scale);
		this.height = (int) (9 * this.scale);
	}

	@Override
	public void render(MatrixStack matrix, int scaledWidth, int scaledHeight, float partialTicks) {
		
		this.width = (int) (80 * this.scale);
		this.height = (int) (9 * this.scale);
		
		if (this.visible) {
			this.renderArmorRaw(matrix);
		}
		
	}

	
	protected void renderArmorRaw(MatrixStack matrix) {

		mc.getProfiler().push("armor");

		RenderSystem.enableBlend();
		int left = (int) (this.x / this.scale);
		if (this.alignment == BarAlignment.RIGHT) {
			left = left + 90 - 18;
		}
		int top = (int) (this.y / this.scale);

		int level = 10;
		if (!this.handler.isEditor()) {
			level = mc.player.getArmor();
			
			if ((level <= 0) && this.hideWhenEmpty) {
				return;
			}
		}

        matrix.push();

		matrix.scale(this.scale, this.scale, this.scale);
		
		for (int i = 1; level > 0 && i < 20; i += 2) {

			if (i < level) {
				drawTexture(matrix, left, top, 34, 9, 9, 9);
			}
			else if (i == level) {
				drawTexture(matrix, left, top, 25, 9, 9, 9);
			}
			else if (i > level) {
				drawTexture(matrix, left, top, 16, 9, 9, 9);
			}

			if (this.alignment == BarAlignment.RIGHT) {
				left -= 8;
			} else {
				left += 8;
			}
		}
		
		matrix.pop();

		RenderSystem.disableBlend();
		mc.getProfiler().pop();

	}
	
	public int getCurrentHealthBarHeight() {
		return this.handler.healthElement.currentHealthHeight;
	}
	
	public boolean isHealthBarAtDefaultPos() {
		return this.handler.healthElement.isDefaultPos;
	}

}
