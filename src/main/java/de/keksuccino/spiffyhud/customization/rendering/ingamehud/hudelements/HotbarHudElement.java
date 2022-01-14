package de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements;

import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.konkrete.rendering.RenderUtils;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import net.minecraft.client.gui.hud.SpectatorHud;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameMode;

public class HotbarHudElement extends IngameHudElement {
	
	protected static final Identifier WIDGETS_TEX_PATH = new Identifier("textures/gui/widgets.png");
	
	protected SpectatorHud spectatorGui = new SpectatorHud(mc);

	public HotbarHudElement(CustomizableIngameGui handler) {
		super(handler);
		
		this.width = 182; //offhand nicht mit dazu gerechnet (+29)
		this.height = 22;
	}

	@Override
	public void render(MatrixStack matrix, int scaledWidth, int scaledHeight, float partialTicks) {

		if (this.visible) {

			if (mc.interactionManager.getCurrentGameMode() == GameMode.SPECTATOR) {
				this.spectatorGui.renderSpectatorMenu(matrix);
			} else {
				this.renderHotbarRaw(matrix, partialTicks);
			}

		}

	}

	protected void renderHotbarRaw(MatrixStack matrix, float partialTicks) {

		PlayerEntity playerentity = this.getRenderViewPlayer();
		if (playerentity != null) {
			
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderUtils.bindTexture(WIDGETS_TEX_PATH);
			
			ItemStack itemstack = playerentity.getOffHandStack();
			Arm handside = playerentity.getMainArm().getOpposite();
			
			this.drawTexture(matrix, x, y, 0, 0, 182, 22);
			int currentItem = 4;
			if (!this.handler.isEditor()) {
				currentItem = playerentity.getInventory().selectedSlot;
			}
			this.drawTexture(matrix, x - 1 + currentItem * 20, y - 1, 0, 22, 24, 22);
			
			if (!itemstack.isEmpty() || this.handler.isEditor()) {
				if (handside == Arm.LEFT) {
					this.drawTexture(matrix, x - 29, y - 1, 24, 22, 29, 24);
				} else {
					this.drawTexture(matrix, x + 182, y - 1, 53, 22, 29, 24);
				}
			}

//			RenderSystem.enableRescaleNormal();
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();

			if (!this.handler.isEditor()) {

				int i1 = 1;

				for(int i2 = 0; i2 < 9; ++i2) {
					int j1 = x + 1 + i2 * 20 + 2;
					int k1 = y + 3;
					this.renderSlot(j1, k1, partialTicks, playerentity, playerentity.getInventory().main.get(i2), i1++);
				}
				
				if (!itemstack.isEmpty()) {
					int i2 = y + 3;
					if (handside == Arm.LEFT) {
						this.renderSlot(x - 26, i2, partialTicks, playerentity, itemstack, i1++);
					} else {
						this.renderSlot(x + 182 + 10, i2, partialTicks, playerentity, itemstack, i1++);
					}
				}
				
			}

			if (this.mc.options.attackIndicator == AttackIndicator.HOTBAR) {
				float f = this.mc.player.getAttackCooldownProgress(0.0F);
				if (f < 1.0F) {
					int j2 = y + 2;
					int k2 = x + 182 + 6;
					if (handside == Arm.RIGHT) {
						k2 = x - 22;
					}

					RenderUtils.bindTexture(GUI_ICONS_TEXTURE);
					int l1 = (int)(f * 19.0F);
					RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
					this.drawTexture(matrix, k2, j2, 0, 94, 18, 18);
					this.drawTexture(matrix, k2, j2 + 18 - l1, 18, 112 - l1, 18, l1);
				}
			}

//			RenderSystem.disableRescaleNormal();
			RenderSystem.disableBlend();
		}

	}

	private void renderSlot(int x, int y, float partial, PlayerEntity player, ItemStack stack, int slot) {
		if (!stack.isEmpty()) {
			MatrixStack posestack = RenderSystem.getModelViewStack();
			float f = (float)stack.getBobbingAnimationTime() - partial;
			if (f > 0.0F) {
				float f1 = 1.0F + f / 5.0F;
				posestack.push();
				posestack.translate((x + 8), (y + 12), 0.0D);
				posestack.scale(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
				posestack.translate((-(x + 8)), (-(y + 12)), 0.0D);
				RenderSystem.applyModelViewMatrix();
			}

			this.mc.getItemRenderer().renderInGuiWithOverrides(player, stack, x, y, slot);
			RenderSystem.setShader(GameRenderer::getPositionColorShader);
			if (f > 0.0F) {
				posestack.pop();
				RenderSystem.applyModelViewMatrix();
			}

			this.mc.getItemRenderer().renderGuiItemOverlay(this.mc.textRenderer, stack, x, y);
		}
	}
	
	protected PlayerEntity getRenderViewPlayer() {
		return !(this.mc.getCameraEntity() instanceof PlayerEntity) ? null : (PlayerEntity)this.mc.getCameraEntity();
	}

}
