package de.keksuccino.spiffyhud.customization.helper.editor;

import com.mojang.blaze3d.matrix.MatrixStack;

import de.keksuccino.spiffyhud.customization.CustomizationHandler;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.gui.ForgeIngameGui;

public class EditorIngameHud extends CustomizableIngameGui {
	
	private static ForgeIngameGui lightModeHud;

	public EditorIngameHud() {
		super(Minecraft.getInstance(), true);
		if (lightModeHud == null) {
			lightModeHud = new ForgeIngameGui(Minecraft.getInstance());
		}
	}
	
	@Override
	public void renderIngameGui(MatrixStack matrix, float partialTicks) {
		
		if (CustomizationHandler.isLightModeEnabled()) {
			lightModeHud.renderIngameGui(matrix, partialTicks);
		} else {
			super.renderIngameGui(matrix, partialTicks);
		}
		
	}
	
	@Override
	protected void renderTitle(int width, int height, float partialTicks, MatrixStack matrix) {
		
		this.titlesTimer = Integer.MAX_VALUE;
		this.displayedTitle = new StringTextComponent("Title");
		this.displayedSubTitle = new StringTextComponent("Subtitle");
		
		super.renderTitle(width, height, partialTicks, matrix);
		
	}
	
}
