package de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements;

import java.awt.Color;

import de.keksuccino.spiffyhud.api.InGameHudOverlay;
import net.minecraftforge.client.gui.ForgeIngameGui;
import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.vertex.PoseStack;
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
	public void render(PoseStack matrix, int scaledWidth, int scaledHeight, float partialTicks) {

		//TODO übernehmen
		this.renderElement = InGameHudOverlay.isRenderingEnabledForElement("experience");
		//TODO übernehmen
		this.elementActive = InGameHudOverlay.isElementActive("experience");
		
		if (!this.lastLvlColorHex.equals(this.lvlColorHex)) {
			this.lvlColor = RenderUtils.getColorFromHexString(this.lvlColorHex);
		}
		this.lastLvlColorHex = this.lvlColorHex;

		if (!this.mc.player.isRidingJumpable() && !this.mc.options.hideGui) {

			//TODO übernehmen
			if (this.renderElement) {
				if (this.visible) {
					this.handler.bind(GUI_ICONS_LOCATION);
				}
			}

			if (this.fireEvents) {
				if (this.handler.pre(ForgeIngameGui.EXPERIENCE_BAR_ELEMENT, matrix)) return;
			}

			//TODO übernehmen
			if (this.renderElement) {
				if (this.visible) {

					RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
					RenderSystem.disableBlend();

					if (mc.gameMode.hasExperience() || this.handler.isEditor()) {
						this.renderExperienceRaw(matrix);
					}

					RenderSystem.enableBlend();
					RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

				}
			}

			if (this.fireEvents) {
				this.handler.post(ForgeIngameGui.EXPERIENCE_BAR_ELEMENT, matrix);
			}
			
		}

		if (this.mc.player.isRidingJumpable() && !this.mc.options.hideGui) {

			//TODO übernehmen
			if (this.renderElement) {
				if (this.visible) {
					this.handler.bind(GUI_ICONS_LOCATION);
				}
			}

			if (this.fireEvents) {
				if (this.handler.pre(ForgeIngameGui.JUMP_BAR_ELEMENT, matrix)) return;
			}

			//TODO übernehmen
			if (this.renderElement) {
				if (this.visible) {

					RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
					RenderSystem.disableBlend();

					this.renderHorseJumpBarRaw(matrix);

					RenderSystem.enableBlend();
					mc.getProfiler().pop();
					RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

				}
			}

			if (this.fireEvents) {
				this.handler.post(ForgeIngameGui.JUMP_BAR_ELEMENT, matrix);
			}
			
		}

	}
	
	protected void renderExperienceRaw(PoseStack matrix) {
		
		this.mc.getProfiler().push("expBar");
		RenderUtils.bindTexture(GUI_ICONS_LOCATION);
		int i = 124 * 9;
		if (!this.handler.isEditor()) {
			i = this.mc.player.getXpNeededForNextLevel();
		}
		if (i > 0) {
			float exp = 0.6F;
			if (!this.handler.isEditor()) {
				exp = this.mc.player.experienceProgress;
			}
			int k = (int)(exp * 183.0F);
			this.blit(matrix, this.x, this.y, 0, 64, 182, 5);
			if (k > 0) {
				this.blit(matrix, this.x, this.y, 0, 69, k, 5);
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
				int sPosX = (this.x + 91) - (mc.font.width(s) / 2);
				int sPosY = this.y - 6;
				
				//Draw exp string shadow
				mc.font.draw(matrix, s, (float)(sPosX + 1), (float)sPosY, 0);
				mc.font.draw(matrix, s, (float)(sPosX - 1), (float)sPosY, 0);
				mc.font.draw(matrix, s, (float)sPosX, (float)(sPosY + 1), 0);
				mc.font.draw(matrix, s, (float)sPosX, (float)(sPosY - 1), 0);
				
				//Draw exp string
				int c = 8453920;
				if (this.lvlColor != null) {
					c = this.lvlColor.getRGB();
				}
				mc.font.draw(matrix, s, (float)sPosX, (float)sPosY, c);
				
				this.mc.getProfiler().pop();
			}
		}

	}
	
	protected void renderHorseJumpBarRaw(PoseStack matrix) {
		
		this.mc.getProfiler().push("jumpBar");
		RenderUtils.bindTexture(GUI_ICONS_LOCATION);
		float f = this.mc.player.getJumpRidingScale();
		int j = (int)(f * 183.0F);
		this.blit(matrix, this.x, this.y, 0, 84, 182, 5);
		if (j > 0) {
			this.blit(matrix, this.x, this.y, 0, 89, j, 5);
		}

		this.mc.getProfiler().pop();
		
	}

}
