package de.keksuccino.fancyhud;

import java.awt.Color;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.fancyhud.api.IngameHud;
import de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements.IngameHudElement;

public class ExampleCustomVanillaElement extends IngameHudElement {

	//A custom property variable that can be customized
	public boolean isRedColor = false;
	
	public ExampleCustomVanillaElement() {
		super(IngameHud.getInstance().getGui());
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
