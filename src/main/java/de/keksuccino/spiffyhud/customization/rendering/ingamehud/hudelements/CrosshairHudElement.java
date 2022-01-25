package de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.spiffyhud.api.InGameHudOverlay;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import de.keksuccino.spiffyhud.logger.Logging;
import de.keksuccino.konkrete.resources.ExternalTextureResourceLocation;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.settings.AttackIndicatorStatus;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

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

		if (this.fireEvents) {
			if (this.handler.pre(ElementType.CROSSHAIRS, matrix)) return;
		}

		if (this.renderElement) {
			if (this.visible) {

				this.renderCrosshairRaw(matrix);

			}
		}
        
        if (this.fireEvents) {
        	this.handler.post(ElementType.CROSSHAIRS, matrix);
        }

	}
	
	protected void renderCrosshairRaw(MatrixStack matrix) {
		
		this.width = 15;
		this.height = 15;
		
		if (this.crosshairTexture != null) {
			this.width = this.crosshairTexture.getWidth();
			this.height = this.crosshairTexture.getHeight();
		}
		
		this.handler.bind(AbstractGui.GUI_ICONS_LOCATION);
		
		RenderSystem.enableBlend();

		GameSettings gamesettings = this.mc.gameSettings;
		if (gamesettings.getPointOfView().func_243192_a()) {
			if (this.mc.playerController.getCurrentGameType() != GameType.SPECTATOR || this.isTargetNamedMenuProvider(this.mc.objectMouseOver)) {
				if (gamesettings.showDebugInfo && !gamesettings.hideGUI && !this.mc.player.hasReducedDebug() && !gamesettings.reducedDebugInfo) {
					if (this.crosshairTexture == null) {
						
			            RenderSystem.enableAlphaTest();
						RenderSystem.pushMatrix();
						RenderSystem.translatef((float)this.x, (float)this.y, (float)this.getBlitOffset());
						ActiveRenderInfo activerenderinfo = this.mc.gameRenderer.getActiveRenderInfo();
						RenderSystem.rotatef(activerenderinfo.getPitch(), -1.0F, 0.0F, 0.0F);
						RenderSystem.rotatef(activerenderinfo.getYaw(), 0.0F, 1.0F, 0.0F);
						RenderSystem.scalef(-1.0F, -1.0F, -1.0F);
						RenderSystem.renderCrosshair(10);
						RenderSystem.popMatrix();
						RenderSystem.disableAlphaTest();
						
					} else {
						
						this.renderCustomCrosshair(matrix);
						
					}
				} else {
		            if (this.crosshairTexture == null) {
		            	
		            	RenderSystem.enableAlphaTest();
						if (this.invertCrosshairColors) {
							RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
						} else {
							RenderSystem.defaultBlendFunc();
						}
						this.blit(matrix, this.x, this.y, 0, 0, 15, 15);
						this.renderAttackIndicator(matrix);

					} else {
		            	
		            	if (this.invertCrosshairColors) {
							RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
						} else {
							RenderSystem.defaultBlendFunc();
						}
		            	
		            	RenderSystem.enableAlphaTest();
		            	this.renderAttackIndicator(matrix);
		            	this.renderCustomCrosshair(matrix);

					}
					RenderSystem.disableAlphaTest();
				}
			}
		}
		
		RenderSystem.disableBlend();

	}
	
	protected void renderAttackIndicator(MatrixStack matrix) {
		
		if (!this.renderAttackIndicator) {
			return;
		}
		
		this.handler.bind(AbstractGui.GUI_ICONS_LOCATION);
		
		if (this.mc.gameSettings.attackIndicator == AttackIndicatorStatus.CROSSHAIR) {
			float f = this.mc.player.getCooledAttackStrength(0.0F);
			boolean flag = false;
			if (this.mc.pointedEntity != null && this.mc.pointedEntity instanceof LivingEntity && f >= 1.0F) {
				flag = this.mc.player.getCooldownPeriod() > 5.0F;
				flag = flag & this.mc.pointedEntity.isAlive();
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
	
	protected void renderCustomCrosshair(MatrixStack matrix) {
		if (this.crosshairTexture != null) {
			if (!this.crosshairTexture.isReady()) {
				this.crosshairTexture.loadTexture();
			}
			ResourceLocation r = this.crosshairTexture.getResourceLocation();
			if (r != null) {
				Minecraft.getInstance().getTextureManager().bindTexture(r);
				RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				blit(matrix, this.x, this.y, 0.0F, 0.0F, this.width, this.height, this.width, this.height);
			} else {
				Logging.error("CrosshairHudElement: Custom Texture is null");
			}
		}
	}
	
	protected boolean isTargetNamedMenuProvider(RayTraceResult rayTraceIn) {
		if (rayTraceIn == null) {
			return false;
		} else if (rayTraceIn.getType() == RayTraceResult.Type.ENTITY) {
			return ((EntityRayTraceResult)rayTraceIn).getEntity() instanceof INamedContainerProvider;
		} else if (rayTraceIn.getType() == RayTraceResult.Type.BLOCK) {
			BlockPos blockpos = ((BlockRayTraceResult)rayTraceIn).getPos();
			World world = this.mc.world;
			return world.getBlockState(blockpos).getContainer(world, blockpos) != null;
		} else {
			return false;
		}
	}

}
