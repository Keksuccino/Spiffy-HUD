package de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements;

import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.konkrete.rendering.RenderUtils;
import de.keksuccino.spiffyhud.api.InGameHudOverlay;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import net.minecraft.client.util.math.MatrixStack;

public class BossBarHudElement extends IngameHudElement {
	
	public BossBarHudElement(CustomizableIngameGui handler) {
		super(handler);
		
		this.width = 182;
		this.height = 9;
	}

	@Override
	public void render(MatrixStack matrix, int scaledWidth, int scaledHeight, float partialTicks) {

		this.renderElement = InGameHudOverlay.isRenderingEnabledForElement("boss");
		this.elementActive = InGameHudOverlay.isElementActive("boss");

		if (this.renderElement) {
			if (this.visible) {

				RenderUtils.bindTexture(GUI_ICONS_TEXTURE);
				RenderSystem.defaultBlendFunc();
				mc.getProfiler().push("bossHealth");
				RenderSystem.enableBlend();

				this.handler.getBossBarHud().render(matrix);

				RenderSystem.disableBlend();
				mc.getProfiler().pop();

			}
		}

	}

}
