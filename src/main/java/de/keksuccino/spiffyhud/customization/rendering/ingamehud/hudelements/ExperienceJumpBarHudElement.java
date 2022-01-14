package de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements;

import java.awt.Color;
import net.minecraft.client.util.math.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.spiffyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import de.keksuccino.konkrete.rendering.RenderUtils;

public class ExperienceJumpBarHudElement extends IngameHudElement {
	
	public boolean showLvl = true;
	public String lvlColorHex = "";
	
	protected String lastLvlColorHex = "";
	protected Color lvlColor;

	public ExperienceJumpBarHudElement(CustomizableIngameGui handler) {
		super(handler);
		
		this.width = 182;
		this.height = 5;
	}

	@Override
	public void render(MatrixStack matrix, int scaledWidth, int scaledHeight, float partialTicks) {
		
		if (!this.lastLvlColorHex.equals(this.lvlColorHex)) {
			this.lvlColor = RenderUtils.getColorFromHexString(this.lvlColorHex);
		}
		this.lastLvlColorHex = this.lvlColorHex;

		if (!this.mc.player.hasJumpingMount() && !this.mc.options.hudHidden) {
			
			if (this.visible) {
				this.handler.bind(GUI_ICONS_TEXTURE);
			}
			
			if (this.visible) {
				
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
				RenderSystem.disableBlend();

				if (mc.interactionManager.hasExperienceBar() || this.handler.isEditor()) {
					this.renderExperienceRaw(matrix);
				}
				
				RenderSystem.enableBlend();
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
				
			}
			
		}

		if (this.mc.player.hasJumpingMount() && !this.mc.options.hudHidden) {
			
			if (this.visible) {
				this.handler.bind(GUI_ICONS_TEXTURE);
			}
			
			if (this.visible) {
				
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
				RenderSystem.disableBlend();

				this.renderHorseJumpBarRaw(matrix);

				RenderSystem.enableBlend();
				mc.getProfiler().pop();
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
				
			}
			
		}

	}
	
	protected void renderExperienceRaw(MatrixStack matrix) {
		
		this.mc.getProfiler().push("expBar");
		RenderUtils.bindTexture(GUI_ICONS_TEXTURE);
		int i = 124 * 9;
		if (!this.handler.isEditor()) {
			i = this.mc.player.getNextLevelExperience();
		}
		if (i > 0) {
			float exp = 0.6F;
			if (!this.handler.isEditor()) {
				exp = this.mc.player.experienceProgress;
			}
			int k = (int)(exp * 183.0F);
			this.drawTexture(matrix, this.x, this.y, 0, 64, 182, 5);
			if (k > 0) {
				this.drawTexture(matrix, this.x, this.y, 0, 69, k, 5);
			}
		}
		this.mc.getProfiler().pop();
		
		if (this.showLvl) {
			if ((this.mc.player.experienceLevel > 0) || this.handler.isEditor()) {
				this.mc.getProfiler().push("expLevel");
				
				String s = "42";
				if (!this.handler.isEditor()) {
					s = "" + this.mc.player.experienceLevel;
				}
				int sPosX = (this.x + 91) - (mc.textRenderer.getWidth(s) / 2);
				int sPosY = this.y - 6;
				
				//Draw exp string shadow
				mc.textRenderer.draw(matrix, s, (float)(sPosX + 1), (float)sPosY, 0);
				mc.textRenderer.draw(matrix, s, (float)(sPosX - 1), (float)sPosY, 0);
				mc.textRenderer.draw(matrix, s, (float)sPosX, (float)(sPosY + 1), 0);
				mc.textRenderer.draw(matrix, s, (float)sPosX, (float)(sPosY - 1), 0);
				
				//Draw exp string
				int c = 8453920;
				if (this.lvlColor != null) {
					c = this.lvlColor.getRGB();
				}
				mc.textRenderer.draw(matrix, s, (float)sPosX, (float)sPosY, c);
				
				this.mc.getProfiler().pop();
			}
		}

	}
	
	protected void renderHorseJumpBarRaw(MatrixStack matrix) {
		
		this.mc.getProfiler().push("jumpBar");
		RenderUtils.bindTexture(GUI_ICONS_TEXTURE);
		float f = this.mc.player.getMountJumpStrength();
		int j = (int)(f * 183.0F);
		this.drawTexture(matrix, this.x, this.y, 0, 84, 182, 5);
		if (j > 0) {
			this.drawTexture(matrix, this.x, this.y, 0, 89, j, 5);
		}

		this.mc.getProfiler().pop();
		
	}

}
