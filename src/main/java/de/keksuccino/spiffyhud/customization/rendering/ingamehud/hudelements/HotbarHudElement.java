package de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.konkrete.rendering.RenderUtils;
import de.keksuccino.spiffyhud.api.InGameHudOverlay;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.client.gui.components.spectator.SpectatorGui;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraftforge.client.gui.ForgeIngameGui;

public class HotbarHudElement extends IngameHudElement {
	
	protected static final ResourceLocation WIDGETS_TEX_PATH = new ResourceLocation("textures/gui/widgets.png");
	
	protected SpectatorGui spectatorGui = new SpectatorGui(mc);

	public HotbarHudElement(CustomizableIngameGui handler) {
		super(handler);
		
		this.width = 182; //offhand nicht mit dazu gerechnet (+29)
		this.height = 22;
	}

	@Override
	public void render(PoseStack matrix, int scaledWidth, int scaledHeight, float partialTicks) {

		//TODO übernehmen
		this.renderElement = InGameHudOverlay.isRenderingEnabledForElement("hotbar");
		//TODO übernehmen
		this.elementActive = InGameHudOverlay.isElementActive("hotbar");

		if (this.fireEvents) {
			if (this.handler.pre(ForgeIngameGui.HOTBAR_ELEMENT, matrix)) return;
		}

		//TODO übernehmen
		if (this.renderElement) {
			if (this.visible) {

				if (mc.gameMode.getPlayerMode() == GameType.SPECTATOR) {
					this.spectatorGui.renderHotbar(matrix);
				} else {
					this.renderHotbarRaw(matrix, partialTicks);
				}

			}
		}

		if (this.fireEvents) {
			this.handler.post(ForgeIngameGui.HOTBAR_ELEMENT, matrix);
		}

	}

	protected void renderHotbarRaw(PoseStack matrix, float partialTicks) {

		Player playerentity = this.getRenderViewPlayer();
		if (playerentity != null) {
			
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderUtils.bindTexture(WIDGETS_TEX_PATH);
			
			ItemStack itemstack = playerentity.getOffhandItem();
			HumanoidArm handside = playerentity.getMainArm().getOpposite();
			
			this.blit(matrix, x, y, 0, 0, 182, 22);
			int currentItem = 4;
			if (!this.handler.isEditor()) {
				currentItem = playerentity.getInventory().selected;
			}
			this.blit(matrix, x - 1 + currentItem * 20, y - 1, 0, 22, 24, 22);
			
			if (!itemstack.isEmpty() || this.handler.isEditor()) {
				if (handside == HumanoidArm.LEFT) {
					this.blit(matrix, x - 29, y - 1, 24, 22, 29, 24);
				} else {
					this.blit(matrix, x + 182, y - 1, 53, 22, 29, 24);
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
					this.renderSlot(j1, k1, partialTicks, playerentity, playerentity.getInventory().items.get(i2), i1++);
				}
				
				if (!itemstack.isEmpty()) {
					int i2 = y + 3;
					if (handside == HumanoidArm.LEFT) {
						this.renderSlot(x - 26, i2, partialTicks, playerentity, itemstack, i1++);
					} else {
						this.renderSlot(x + 182 + 10, i2, partialTicks, playerentity, itemstack, i1++);
					}
				}
				
			}

			if (this.mc.options.attackIndicator == AttackIndicatorStatus.HOTBAR) {
				float f = this.mc.player.getAttackStrengthScale(0.0F);
				if (f < 1.0F) {
					int j2 = y + 2;
					int k2 = x + 182 + 6;
					if (handside == HumanoidArm.RIGHT) {
						k2 = x - 22;
					}

					RenderUtils.bindTexture(GUI_ICONS_LOCATION);
					int l1 = (int)(f * 19.0F);
					RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
					this.blit(matrix, k2, j2, 0, 94, 18, 18);
					this.blit(matrix, k2, j2 + 18 - l1, 18, 112 - l1, 18, l1);
				}
			}

//			RenderSystem.disableRescaleNormal();
			RenderSystem.disableBlend();
		}

	}

	private void renderSlot(int x, int y, float partial, Player player, ItemStack stack, int slot) {
		if (!stack.isEmpty()) {
			PoseStack posestack = RenderSystem.getModelViewStack();
			float f = (float)stack.getPopTime() - partial;
			if (f > 0.0F) {
				float f1 = 1.0F + f / 5.0F;
				posestack.pushPose();
				posestack.translate((x + 8), (y + 12), 0.0D);
				posestack.scale(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
				posestack.translate((-(x + 8)), (-(y + 12)), 0.0D);
				RenderSystem.applyModelViewMatrix();
			}

			this.mc.getItemRenderer().renderAndDecorateItem(player, stack, x, y, slot);
			RenderSystem.setShader(GameRenderer::getPositionColorShader);
			if (f > 0.0F) {
				posestack.popPose();
				RenderSystem.applyModelViewMatrix();
			}

			this.mc.getItemRenderer().renderGuiItemDecorations(this.mc.font, stack, x, y);
		}
	}
	
	protected Player getRenderViewPlayer() {
		return !(this.mc.getCameraEntity() instanceof Player) ? null : (Player)this.mc.getCameraEntity();
	}

}
