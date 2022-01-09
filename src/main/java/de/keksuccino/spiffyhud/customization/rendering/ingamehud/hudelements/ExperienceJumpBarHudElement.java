package de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.spiffyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import de.keksuccino.konkrete.rendering.RenderUtils;
import net.minecraft.client.gui.AbstractGui;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

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

		if (CustomizableIngameGui.renderHealth && !CustomizableIngameGui.renderJumpBar) {
			
			if (this.visible) {
				this.handler.bind(GUI_ICONS_LOCATION);
			}
			
			if (this.fireEvents) {
				if (this.handler.pre(ElementType.EXPERIENCE, matrix)) return;
			}
			
			if (this.visible) {
				
				RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				RenderSystem.disableBlend();

				if (mc.playerController.gameIsSurvivalOrAdventure() || this.handler.isEditor()) {
					this.renderExperienceRaw(matrix);
				}
				
				RenderSystem.enableBlend();
				RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				
			}
			
			if (this.fireEvents) {
				this.handler.post(ElementType.EXPERIENCE, matrix);
			}
			
		} else if (CustomizableIngameGui.renderJumpBar) {
			
			if (this.visible) {
				this.handler.bind(GUI_ICONS_LOCATION);
			}
			
			if (this.fireEvents) {
				if (this.handler.pre(ElementType.JUMPBAR, matrix)) return;
			}
			
			if (this.visible) {
				
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				RenderSystem.disableBlend();

				this.renderHorseJumpBarRaw(matrix);

				RenderSystem.enableBlend();
				mc.getProfiler().endSection();
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				
			}
			
			if (this.fireEvents) {
				this.handler.post(ElementType.JUMPBAR, matrix);
			}
			
		}

	}
	
	protected void renderExperienceRaw(MatrixStack matrix) {
		
		this.mc.getProfiler().startSection("expBar");
		this.mc.getTextureManager().bindTexture(AbstractGui.GUI_ICONS_LOCATION);
		int i = 124 * 9;
		if (!this.handler.isEditor()) {
			i = this.mc.player.xpBarCap();
		}
		if (i > 0) {
			float exp = 0.6F;
			if (!this.handler.isEditor()) {
				exp = this.mc.player.experience;
			}
			int k = (int)(exp * 183.0F);
			this.blit(matrix, this.x, this.y, 0, 64, 182, 5);
			if (k > 0) {
				this.blit(matrix, this.x, this.y, 0, 69, k, 5);
			}
		}
		this.mc.getProfiler().endSection();
		
		if (this.showLvl) {
			if ((this.mc.player.experienceLevel > 0) || this.handler.isEditor()) {
				this.mc.getProfiler().startSection("expLevel");
				
				String s = "42";
				if (!this.handler.isEditor()) {
					s = "" + this.mc.player.experienceLevel;
				}
				int sPosX = (this.x + 91) - (mc.fontRenderer.getStringWidth(s) / 2);
				int sPosY = this.y - 6;
				
				//Draw exp string shadow
				mc.fontRenderer.drawString(matrix, s, (float)(sPosX + 1), (float)sPosY, 0);
				mc.fontRenderer.drawString(matrix, s, (float)(sPosX - 1), (float)sPosY, 0);
				mc.fontRenderer.drawString(matrix, s, (float)sPosX, (float)(sPosY + 1), 0);
				mc.fontRenderer.drawString(matrix, s, (float)sPosX, (float)(sPosY - 1), 0);
				
				//Draw exp string
				int c = 8453920;
				if (this.lvlColor != null) {
					c = this.lvlColor.getRGB();
				}
				mc.fontRenderer.drawString(matrix, s, (float)sPosX, (float)sPosY, c);
				
				this.mc.getProfiler().endSection();
			}
		}

	}
	
	protected void renderHorseJumpBarRaw(MatrixStack matrix) {
		
		this.mc.getProfiler().startSection("jumpBar");
		this.mc.getTextureManager().bindTexture(AbstractGui.GUI_ICONS_LOCATION);
		float f = this.mc.player.getHorseJumpPower();
		int j = (int)(f * 183.0F);
		this.blit(matrix, this.x, this.y, 0, 84, 182, 5);
		if (j > 0) {
			this.blit(matrix, this.x, this.y, 0, 89, j, 5);
		}

		this.mc.getProfiler().endSection();
		
	}

}
