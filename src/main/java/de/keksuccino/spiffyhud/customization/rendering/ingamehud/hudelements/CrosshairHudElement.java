package de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.konkrete.rendering.RenderUtils;
import de.keksuccino.spiffyhud.SpiffyHud;
import de.keksuccino.spiffyhud.api.InGameHudOverlay;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import de.keksuccino.konkrete.resources.ExternalTextureResourceLocation;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

public class CrosshairHudElement extends IngameHudElement {

	public ExternalTextureResourceLocation crosshairTexture = null;
	public boolean invertCrosshairColors = true;
	public boolean renderAttackIndicator = true;
	
	public CrosshairHudElement(CustomizableIngameGui handler) {
		super(handler);
		
		this.width = 15;
		this.height = 15;
		
	}

	@Override
	public void render(MatrixStack matrix, int scaledWidth, int scaledHeight, float partialTicks) {

		this.renderElement = InGameHudOverlay.isRenderingEnabledForElement("crosshair");
		this.elementActive = InGameHudOverlay.isElementActive("crosshair");

		if (this.renderElement) {
			if (this.visible) {

				this.renderCrosshairRaw(matrix);

			}
		}

	}

	protected void renderCrosshairRaw(MatrixStack matrix) {

		this.width = 15;
		this.height = 15;

		if (this.crosshairTexture != null) {
			this.width = this.crosshairTexture.getWidth();
			this.height = this.crosshairTexture.getHeight();
		}

		GameOptions options = this.mc.options;

		if (options.getPerspective().isFirstPerson()) {

			if (this.mc.interactionManager.getCurrentGameMode() != GameMode.SPECTATOR || this.canRenderCrosshairForSpectator(this.mc.crosshairTarget)) {

				if (options.debugEnabled && !options.hudHidden && !this.mc.player.hasReducedDebugInfo() && !options.reducedDebugInfo) {

					if (this.crosshairTexture == null) {
						Camera camera = this.mc.gameRenderer.getCamera();
						MatrixStack MatrixStack = RenderSystem.getModelViewStack();
						MatrixStack.push();
						MatrixStack.translate((float)this.x, (float)this.y, (float)this.getZOffset());
						MatrixStack.multiply(Vec3f.NEGATIVE_X.getDegreesQuaternion(camera.getPitch()));
						MatrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(camera.getYaw()));
						MatrixStack.scale(-1.0F, -1.0F, -1.0F);
						RenderSystem.applyModelViewMatrix();
						RenderSystem.renderCrosshair(10);
						MatrixStack.pop();
						RenderSystem.applyModelViewMatrix();
					} else {
						this.renderCustomCrosshair(matrix);
					}

				} else {

					if (this.invertCrosshairColors) {
						RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ONE_MINUS_DST_COLOR, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
					} else {
						RenderSystem.defaultBlendFunc();
					}

					if (this.crosshairTexture == null) {
						this.drawTexture(matrix, this.x, this.y, 0, 0, 15, 15);
						this.renderAttackIndicator(matrix);
					} else {
						this.renderAttackIndicator(matrix);
						this.renderCustomCrosshair(matrix);
					}

				}

			}
		}

		RenderSystem.disableBlend();

	}
	
	protected void renderAttackIndicator(MatrixStack matrix) {
		
		if (!this.renderAttackIndicator) {
			return;
		}
		
		RenderUtils.bindTexture(GUI_ICONS_TEXTURE);
		
		if (this.mc.options.attackIndicator == AttackIndicator.CROSSHAIR) {
			float f = this.mc.player.getAttackCooldownProgress(0.0F);
			boolean flag = false;
			if (this.mc.targetedEntity != null && this.mc.targetedEntity instanceof LivingEntity && f >= 1.0F) {
				flag = this.mc.player.getAttackCooldownProgressPerTick() > 5.0F;
				flag = flag & this.mc.targetedEntity.isAlive();
			}
			
			int crossPosY = this.y + (15 /2) - 7 + 16;
			int crossPosX = this.x + (15 /2) - 8;
			if (flag) {
				this.drawTexture(matrix, crossPosX, crossPosY, 68, 94, 16, 16);
			} else if (f < 1.0F) {
				int l = (int)(f * 17.0F);
				this.drawTexture(matrix, crossPosX, crossPosY, 36, 94, 16, 4);
				this.drawTexture(matrix, crossPosX, crossPosY, 52, 94, l, 4);
			}
		}
	}
	
	protected void renderCustomCrosshair(MatrixStack matrix) {
		if (this.crosshairTexture != null) {
			if (!this.crosshairTexture.isReady()) {
				this.crosshairTexture.loadTexture();
			}
			Identifier r = this.crosshairTexture.getResourceLocation();
			if (r != null) {
				RenderUtils.bindTexture(r);
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
				drawTexture(matrix, this.x, this.y, 0.0F, 0.0F, this.width, this.height, this.width, this.height);
			} else {
				SpiffyHud.LOGGER.error("[SPIFFY HUD] ERROR: CrosshairHudElement: Custom texture is null");
			}
		}
	}

	protected boolean canRenderCrosshairForSpectator(HitResult result) {
		if (result == null) {
			return false;
		} else if (result.getType() == HitResult.Type.ENTITY) {
			return ((EntityHitResult)result).getEntity() instanceof NamedScreenHandlerFactory;
		} else if (result.getType() == HitResult.Type.BLOCK) {
			BlockPos blockpos = ((BlockHitResult)result).getBlockPos();
			World level = this.mc.world;
			return level.getBlockState(blockpos).createScreenHandlerFactory(level, blockpos) != null;
		} else {
			return false;
		}
	}

}
