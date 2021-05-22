package de.keksuccino.fancyhud.customization.helper.editor;

import com.mojang.blaze3d.matrix.MatrixStack;

import de.keksuccino.fancyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;

public class EditorIngameHud extends CustomizableIngameGui {

	public EditorIngameHud() {
		super(Minecraft.getInstance(), true);
	}
	
	@Override
	public void renderIngameGui(MatrixStack matrix, float partialTicks) {
		
		super.renderIngameGui(matrix, partialTicks);
		
	}
	
	@Override
	protected void renderTitle(int width, int height, float partialTicks, MatrixStack matrix) {
		
		this.titlesTimer = Integer.MAX_VALUE;
		this.displayedTitle = new StringTextComponent("Title");
		this.displayedSubTitle = new StringTextComponent("Subtitle");
		
		super.renderTitle(width, height, partialTicks, matrix);
	}
	
}
