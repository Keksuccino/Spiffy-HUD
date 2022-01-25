package de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import com.mojang.math.Vector3f;
import de.keksuccino.konkrete.rendering.RenderUtils;
import de.keksuccino.spiffyhud.SpiffyHud;
import de.keksuccino.spiffyhud.api.InGameHudOverlay;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import de.keksuccino.konkrete.resources.ExternalTextureResourceLocation;
import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.client.Camera;
import net.minecraft.client.Options;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.gui.ForgeIngameGui;

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
	public void render(PoseStack matrix, int scaledWidth, int scaledHeight, float partialTicks) {

		//TODO übernehmen
		this.renderElement = InGameHudOverlay.isRenderingEnabledForElement("crosshair");
		//TODO übernehmen
		this.elementActive = InGameHudOverlay.isElementActive("crosshair");

		if (this.fireEvents) {
			if (this.handler.pre(ForgeIngameGui.CROSSHAIR_ELEMENT, matrix)) return;
		}

		//TODO übernehmen
		if (this.renderElement) {
			if (this.visible) {

				this.renderCrosshairRaw(matrix);

			}
		}

        if (this.fireEvents) {
        	this.handler.post(ForgeIngameGui.CROSSHAIR_ELEMENT, matrix);
        }

	}

	protected void renderCrosshairRaw(PoseStack matrix) {

		this.width = 15;
		this.height = 15;

		if (this.crosshairTexture != null) {
			this.width = this.crosshairTexture.getWidth();
			this.height = this.crosshairTexture.getHeight();
		}

		Options options = this.mc.options;

		if (options.getCameraType().isFirstPerson()) {

			if (this.mc.gameMode.getPlayerMode() != GameType.SPECTATOR || this.canRenderCrosshairForSpectator(this.mc.hitResult)) {

				if (options.renderDebug && !options.hideGui && !this.mc.player.isReducedDebugInfo() && !options.reducedDebugInfo) {

					if (this.crosshairTexture == null) {
						Camera camera = this.mc.gameRenderer.getMainCamera();
						PoseStack posestack = RenderSystem.getModelViewStack();
						posestack.pushPose();
						posestack.translate((float)this.x, (float)this.y, (float)this.getBlitOffset());
						posestack.mulPose(Vector3f.XN.rotationDegrees(camera.getXRot()));
						posestack.mulPose(Vector3f.YP.rotationDegrees(camera.getYRot()));
						posestack.scale(-1.0F, -1.0F, -1.0F);
						RenderSystem.applyModelViewMatrix();
						RenderSystem.renderCrosshair(10);
						posestack.popPose();
						RenderSystem.applyModelViewMatrix();
					} else {
						this.renderCustomCrosshair(matrix);
					}

				} else {

					if (this.invertCrosshairColors) {
						RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
					} else {
						RenderSystem.defaultBlendFunc();
					}

					if (this.crosshairTexture == null) {
						this.blit(matrix, this.x, this.y, 0, 0, 15, 15);
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
	
	protected void renderAttackIndicator(PoseStack matrix) {
		
		if (!this.renderAttackIndicator) {
			return;
		}
		
		RenderUtils.bindTexture(GUI_ICONS_LOCATION);
		
		if (this.mc.options.attackIndicator == AttackIndicatorStatus.CROSSHAIR) {
			float f = this.mc.player.getAttackStrengthScale(0.0F);
			boolean flag = false;
			if (this.mc.crosshairPickEntity != null && this.mc.crosshairPickEntity instanceof LivingEntity && f >= 1.0F) {
				flag = this.mc.player.getCurrentItemAttackStrengthDelay() > 5.0F;
				flag = flag & this.mc.crosshairPickEntity.isAlive();
			}
			
			int crossPosY = this.y + (15 /2) - 7 + 16;
			int crossPosX = this.x + (15 /2) - 8;
			if (flag) {
				this.blit(matrix, crossPosX, crossPosY, 68, 94, 16, 16);
			} else if (f < 1.0F) {
				int l = (int)(f * 17.0F);
				this.blit(matrix, crossPosX, crossPosY, 36, 94, 16, 4);
				this.blit(matrix, crossPosX, crossPosY, 52, 94, l, 4);
			}
		}
	}
	
	protected void renderCustomCrosshair(PoseStack matrix) {
		if (this.crosshairTexture != null) {
			if (!this.crosshairTexture.isReady()) {
				this.crosshairTexture.loadTexture();
			}
			ResourceLocation r = this.crosshairTexture.getResourceLocation();
			if (r != null) {
				RenderUtils.bindTexture(r);
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
				blit(matrix, this.x, this.y, 0.0F, 0.0F, this.width, this.height, this.width, this.height);
			} else {
				SpiffyHud.LOGGER.error("[SPIFFY HUD] ERROR: CrosshairHudElement: Custom texture is null");
			}
		}
	}

	protected boolean canRenderCrosshairForSpectator(HitResult result) {
		if (result == null) {
			return false;
		} else if (result.getType() == HitResult.Type.ENTITY) {
			return ((EntityHitResult)result).getEntity() instanceof MenuProvider;
		} else if (result.getType() == HitResult.Type.BLOCK) {
			BlockPos blockpos = ((BlockHitResult)result).getBlockPos();
			Level level = this.mc.level;
			return level.getBlockState(blockpos).getMenuProvider(level, blockpos) != null;
		} else {
			return false;
		}
	}

}
