package de.keksuccino.spiffyhud.customization.helper;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;

public class CustomizationHelperScreen extends Screen {
	
	public static boolean renderBackgroundOverlay = false;
	
	public CustomizationHelperScreen() {
		super(new TextComponent(""));
	}
	
	@Override
	protected void init() {
		
		CustomizationHelperUI.currentHelperScreen = this;
		CustomizationHelperUI.updateUI();
		
	}
	
	@Override
	public void render(PoseStack matrix, int mouseX, int mouseY, float partialTicks) {
		
		this.renderBackground(matrix);
		
		CustomizationHelperUI.render(matrix, this);
		
		super.render(matrix, mouseX, mouseY, partialTicks);
		
	}
	
	@Override
	public void renderBackground(PoseStack matrixStack, int vOffset) {
		
		if (this.minecraft.level != null) {
			if (renderBackgroundOverlay) {
				this.fillGradient(matrixStack, 0, 0, this.width, this.height, -1072689136, -804253680);
			}
			net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.ScreenEvent.BackgroundDrawnEvent(this, matrixStack));
		} else {
			this.renderDirtBackground(vOffset);
		}
		
	}

}
