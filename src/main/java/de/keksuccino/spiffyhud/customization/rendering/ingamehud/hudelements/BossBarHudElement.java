package de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.spiffyhud.api.InGameHudOverlay;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import net.minecraft.client.gui.AbstractGui;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

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

		if (this.fireEvents) {
        	if (this.handler.pre(ElementType.BOSSHEALTH, matrix)) return;
        }

		if (this.renderElement) {
			if (this.visible) {

				this.handler.bind(AbstractGui.GUI_ICONS_LOCATION);
				RenderSystem.defaultBlendFunc();
				mc.getProfiler().startSection("bossHealth");
				RenderSystem.enableBlend();

				this.handler.getBossOverlay().func_238484_a_(matrix);

				RenderSystem.disableBlend();
				mc.getProfiler().endSection();

			}
		}
        
        if (this.fireEvents) {
        	this.handler.post(ElementType.BOSSHEALTH, matrix);
        }

	}

}
