package de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.konkrete.rendering.RenderUtils;
import de.keksuccino.spiffyhud.api.InGameHudOverlay;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import net.minecraftforge.client.gui.ForgeIngameGui;

public class BossBarHudElement extends IngameHudElement {
	
	public BossBarHudElement(CustomizableIngameGui handler) {
		super(handler);
		
		this.width = 182;
		this.height = 9;
	}

	@Override
	public void render(PoseStack matrix, int scaledWidth, int scaledHeight, float partialTicks) {

		//TODO übernehmen
		this.renderElement = InGameHudOverlay.isRenderingEnabledForElement("boss");
		//TODO übernehmen
		this.elementActive = InGameHudOverlay.isElementActive("boss");

		if (this.fireEvents) {
        	if (this.handler.pre(ForgeIngameGui.BOSS_HEALTH_ELEMENT, matrix)) return;
        }

		//TODO übernehmen
		if (this.renderElement) {
			if (this.visible) {

				RenderUtils.bindTexture(GUI_ICONS_LOCATION);
				RenderSystem.defaultBlendFunc();
				mc.getProfiler().push("bossHealth");
				RenderSystem.enableBlend();

				this.handler.getBossOverlay().render(matrix);

				RenderSystem.disableBlend();
				mc.getProfiler().pop();

			}
		}

        if (this.fireEvents) {
        	this.handler.post(ForgeIngameGui.BOSS_HEALTH_ELEMENT, matrix);
        }

	}

}
