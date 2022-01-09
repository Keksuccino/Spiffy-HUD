package de.keksuccino.spiffyhud.customization.helper.editor;

import com.mojang.blaze3d.vertex.PoseStack;

import de.keksuccino.spiffyhud.customization.CustomizationHandler;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
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
	public void render(PoseStack matrix, float partialTicks) {
		
		if (CustomizationHandler.isLightModeEnabled()) {
			lightModeHud.render(matrix, partialTicks);
		} else {
			super.render(matrix, partialTicks);
		}
		
	}
	
	@Override
	protected void renderTitle(int width, int height, float partialTicks, PoseStack matrix) {
		
		this.titleTime = Integer.MAX_VALUE;
		this.title = new TextComponent("Title");
		this.subtitle = new TextComponent("Subtitle");
		
		super.renderTitle(width, height, partialTicks, matrix);
		
	}
	
}
