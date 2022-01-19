package de.keksuccino.spiffyhud.api.hud.v2.example;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.spiffyhud.api.InGameHudOverlay;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements.IngameHudElement;

import java.awt.*;

public class ExampleCustomVanillaHudElement extends IngameHudElement {

	//A custom property variable that can be customized
	public boolean isRedColor = false;
	
	public ExampleCustomVanillaHudElement() {
		super(InGameHudOverlay.getGui());
	}

	@Override
	public void render(MatrixStack matrix, int scaledWidth, int scaledHeight, float partialTicks) {
		
		//Add this. Important for being able to hide the element via customizations.
		if (this.visible) {
			
			RenderSystem.enableBlend();
			
			//Setting the correct render color depending on the custom property variable
			Color c = new Color(61, 235, 52, 100);
			if (this.isRedColor) {
				c = new Color(255, 0, 34, 100);
			}
			
			//Rendering the element (simple colored rectangle)
			fill(matrix, this.x, this.y, this.x + 50, this.y + 50, c.getRGB());
			
			RenderSystem.disableBlend();
			
		}
		
	}

}
