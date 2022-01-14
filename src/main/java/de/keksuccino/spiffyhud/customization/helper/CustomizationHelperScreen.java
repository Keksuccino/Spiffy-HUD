package de.keksuccino.spiffyhud.customization.helper;

import de.keksuccino.konkrete.Konkrete;
import de.keksuccino.konkrete.events.client.GuiScreenEvent;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class CustomizationHelperScreen extends Screen {
	
	public static boolean renderBackgroundOverlay = false;
	
	public CustomizationHelperScreen() {
		super(new LiteralText(""));
	}
	
	@Override
	protected void init() {
		
		CustomizationHelperUI.currentHelperScreen = this;
		CustomizationHelperUI.updateUI();
		
	}
	
	@Override
	public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
		
		this.renderBackground(matrix);
		
		CustomizationHelperUI.render(matrix, this);
		
		super.render(matrix, mouseX, mouseY, partialTicks);
		
	}
	
	@Override
	public void renderBackground(MatrixStack matrixStack, int vOffset) {
		
		if (this.client.world != null) {
			if (renderBackgroundOverlay) {
				this.fillGradient(matrixStack, 0, 0, this.width, this.height, -1072689136, -804253680);
			}
			GuiScreenEvent.BackgroundDrawnEvent e = new GuiScreenEvent.BackgroundDrawnEvent(this, matrixStack);
			Konkrete.getEventHandler().callEventsFor(e);
		} else {
			this.renderBackgroundTexture(vOffset);
		}
		
	}

}
