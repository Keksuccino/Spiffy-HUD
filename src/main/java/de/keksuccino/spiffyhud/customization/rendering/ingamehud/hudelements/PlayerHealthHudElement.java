package de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements;

import java.util.Random;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.spiffyhud.api.InGameHudOverlay;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import net.minecraft.Util;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.ForgeIngameGui;

public class PlayerHealthHudElement extends IngameHudElement {

	protected Random rand = new Random();
	protected long healthUpdateCounter;
	protected int playerHealth;
	protected long lastSystemTime;
	protected int lastPlayerHealth;
	public int currentHealthHeight = 10;
	public boolean isDefaultPos = false;
	public boolean hideWhenFull = false;
	
	public BarAlignment alignment = BarAlignment.LEFT;
	
	public PlayerHealthHudElement(CustomizableIngameGui handler) {
		super(handler);

		this.width = (int) (80 * this.scale);
		this.height = (int) (9 * this.scale);
	}

	@Override
	public void render(PoseStack matrix, int scaledWidth, int scaledHeight, float partialTicks) {

		//TODO übernehmen
		this.renderElement = InGameHudOverlay.isRenderingEnabledForElement("playerhealth");
		//TODO übernehmen
		this.elementActive = InGameHudOverlay.isElementActive("playerhealth");

		this.width = (int) (80 * this.scale);
		this.height = (int) (9 * this.scale);
		
		this.currentHealthHeight = (int) (10 * this.scale);

		this.renderHealth(matrix);

	}
	
	protected void renderHealth(PoseStack matrix) {

		//TODO übernehmen
		if (this.renderElement) {
			if (this.visible) {
				this.handler.bind(GUI_ICONS_LOCATION);
			}
		}
		
		if (this.fireEvents) {
			if (this.handler.pre(ForgeIngameGui.PLAYER_HEALTH_ELEMENT, matrix)) return;
		}

		//TODO übernehmen
		if (this.renderElement) {
			if (this.visible) {

				mc.getProfiler().push("health");
				RenderSystem.enableBlend();

				Player player = (Player) this.mc.getCameraEntity();
				int health = 10;
				if (!this.handler.isEditor()) {
					health = Mth.ceil(player.getHealth());

					if ((player.getHealth() >= player.getMaxHealth()) && this.hideWhenFull) {
						return;
					}
				}

				//If white border should be rendered around hearts
				boolean highlight = healthUpdateCounter > (long) this.handler.getGuiTicks() && (healthUpdateCounter - (long) this.handler.getGuiTicks()) / 3L % 2L == 1L;

				if (health < this.playerHealth && player.invulnerableTime > 0) {
					this.lastSystemTime = Util.getMillis();
					this.healthUpdateCounter = (this.handler.getGuiTicks() + 20);
				} else if (health > this.playerHealth && player.invulnerableTime > 0) {
					this.lastSystemTime = Util.getMillis();
					this.healthUpdateCounter = (this.handler.getGuiTicks() + 10);
				}

				if (Util.getMillis() - this.lastSystemTime > 1000L) {
					this.playerHealth = health;
					this.lastPlayerHealth = health;
					this.lastSystemTime = Util.getMillis();
				}

				this.playerHealth = health;
				int healthLast = this.lastPlayerHealth;

				AttributeInstance attrMaxHealth = player.getAttribute(Attributes.MAX_HEALTH);
				float healthMax = (float) attrMaxHealth.getValue();
				float absorb = Mth.ceil(player.getAbsorptionAmount());

				int healthRows = Mth.ceil((healthMax + absorb) / 2.0F / 10.0F);
				int rowHeight = Math.max(10 - (healthRows - 2), 3);

				this.currentHealthHeight = (healthRows * rowHeight);
				if (rowHeight != 10) this.currentHealthHeight += 10 - rowHeight;

				this.currentHealthHeight = (int) (this.currentHealthHeight * this.scale);

				this.rand.setSeed((this.handler.getGuiTicks() * 312871));

				int left = (int) (this.x / this.scale);
				if (this.alignment == BarAlignment.RIGHT) {
					left = left + 90 - 18;
				}
				int top = (int) (this.y / this.scale);

				int regen = -1;
				if (player.hasEffect(MobEffects.REGENERATION)) {
					regen = this.handler.getGuiTicks() % 25;
				}

				final int marginTop = 9 * (mc.level.getLevelData().isHardcore() ? 5 : 0);
				final int background = (highlight ? 25 : 16);
				int marginLeft = 16;
				int marginLeftAbsorption = 160;
				if (player.hasEffect(MobEffects.POISON) && !this.handler.isEditor()) {
					marginLeft += 36;
				} else if (player.hasEffect(MobEffects.WITHER) && !this.handler.isEditor()) {
					marginLeft += 72;
					//absorption hearts are rendered as wither hearts if effect is active
					marginLeftAbsorption -= 36;
				} else if (player.isFullyFrozen() && !this.handler.isEditor()) {
					marginLeft += 126;
				}
				float absorbRemaining = absorb;

				matrix.pushPose();

				matrix.scale(this.scale, this.scale, this.scale);

				for (int i = Mth.ceil((healthMax + absorb) / 2.0F) - 1; i >= 0; --i) {
					int row = Mth.ceil((float) (i + 1) / 10.0F) - 1;
					int x = left + i % 10 * 8;
					if (this.alignment == BarAlignment.RIGHT) {
						x = left - i % 10 * 8;
					}
					int y = top - row * rowHeight;

					if (health <= 4) y += rand.nextInt(2);
					if (i == regen) y -= 2;

					//Render heart background
					blit(matrix, x, y, background, marginTop, 9, 9);

					//Render white border around heart
					if (highlight) {
						//Render half or full heart (????)
						if (i * 2 + 1 < healthLast) {
							blit(matrix, x, y, marginLeft + 54, marginTop, 9, 9);
						} else if (i * 2 + 1 == healthLast) {
							blit(matrix, x, y, marginLeft + 63, marginTop, 9, 9);
						}
					}

					//Render normal or absorption heart
					if (absorbRemaining > 0.0F) {
						if ((absorbRemaining == absorb) && (absorb % 2.0F == 1.0F)) {
							blit(matrix, x, y, marginLeftAbsorption + 9, marginTop, 9, 9);
							absorbRemaining -= 1.0F;
						} else {
							blit(matrix, x, y, marginLeftAbsorption, marginTop, 9, 9);
							absorbRemaining -= 2.0F;
						}
					} else {
						//Render half or full heart
						if (i * 2 + 1 < health) {
							blit(matrix, x, y, marginLeft + 36, marginTop, 9, 9);
						} else if (i * 2 + 1 == health) {
							blit(matrix, x, y, marginLeft + 45, marginTop, 9, 9);
						}
					}
				}

				matrix.popPose();

				RenderSystem.disableBlend();
				mc.getProfiler().pop();

			}
		}
		
		if (this.fireEvents) {
			this.handler.post(ForgeIngameGui.PLAYER_HEALTH_ELEMENT, matrix);
		}

	}

}
