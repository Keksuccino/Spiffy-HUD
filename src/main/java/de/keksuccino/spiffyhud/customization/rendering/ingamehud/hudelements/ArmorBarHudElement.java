package de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.spiffyhud.api.InGameHudOverlay;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import net.minecraftforge.client.gui.ForgeIngameGui;

public class ArmorBarHudElement extends IngameHudElement {
	
	public BarAlignment alignment = BarAlignment.LEFT;
	public boolean hideWhenEmpty = false;
	
	public ArmorBarHudElement(CustomizableIngameGui handler) {
		super(handler);
		
		this.width = (int) (80 * this.scale);
		this.height = (int) (9 * this.scale);
	}

	@Override
	public void render(PoseStack matrix, int scaledWidth, int scaledHeight, float partialTicks) {

		//TODO übernehmen
		this.renderElement = InGameHudOverlay.isRenderingEnabledForElement("armor");
		//TODO übernehmen
		this.elementActive = InGameHudOverlay.isElementActive("armor");
		
		this.width = (int) (80 * this.scale);
		this.height = (int) (9 * this.scale);

		if (this.fireEvents) {
			if (this.handler.pre(ForgeIngameGui.ARMOR_LEVEL_ELEMENT, matrix)) return;
		}

		//TODO übernehmen
		if (this.renderElement) {
			if (this.visible) {
				this.renderArmorRaw(matrix);
			}
		}

		if (this.fireEvents) {
			this.handler.post(ForgeIngameGui.ARMOR_LEVEL_ELEMENT, matrix);
		}
		
	}

	
	protected void renderArmorRaw(PoseStack matrix) {

		mc.getProfiler().push("armor");

		RenderSystem.enableBlend();
		int left = (int) (this.x / this.scale);
		if (this.alignment == BarAlignment.RIGHT) {
			left = left + 90 - 18;
		}
		int top = (int) (this.y / this.scale);

		int level = 10;
		if (!this.handler.isEditor()) {
			level = mc.player.getArmorValue();
			
			if ((level <= 0) && this.hideWhenEmpty) {
				return;
			}
		}

        matrix.pushPose();

		matrix.scale(this.scale, this.scale, this.scale);
		
		for (int i = 1; level > 0 && i < 20; i += 2) {

			if (i < level) {
				blit(matrix, left, top, 34, 9, 9, 9);
			}
			else if (i == level) {
				blit(matrix, left, top, 25, 9, 9, 9);
			}
			else if (i > level) {
				blit(matrix, left, top, 16, 9, 9, 9);
			}

			if (this.alignment == BarAlignment.RIGHT) {
				left -= 8;
			} else {
				left += 8;
			}
		}
		
		matrix.popPose();

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
