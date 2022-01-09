package de.keksuccino.spiffyhud.customization.rendering.ingamehud;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.konkrete.rendering.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundBossEventPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.BossEvent;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class CustomizableBossHealthOverlay extends BossHealthOverlay {

	protected static final ResourceLocation GUI_BARS_TEXTURES = new ResourceLocation("textures/gui/bars.png");
	
	protected CustomizableIngameGui handler;
	
	public CustomizableBossHealthOverlay(Minecraft clientIn, CustomizableIngameGui handler) {
		super(clientIn);
		this.handler = handler;
		
		if (this.handler.isEditor()) {
			this.setupEditorMode();
		}
	}
	
	//render
	@Override
	public void render(PoseStack matrix) {

		Minecraft mc = Minecraft.getInstance();
		Map<UUID, LerpingBossEvent> events = this.getEventsMap();

		if (!events.isEmpty()) {
			int posX = this.handler.bossBarElement.x;
			int posY = this.handler.bossBarElement.y;

			for(LerpingBossEvent be : events.values()) {
				RenderGameOverlayEvent.BossInfo e = ForgeHooksClient.renderBossEventPre(matrix, mc.getWindow(), be, posX, posY, 10 + mc.font.lineHeight);
				if (!e.isCanceled()) {
					RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
					RenderUtils.bindTexture(GUI_BARS_TEXTURES);
					this.drawBar(matrix, posX, posY, be);
					Component bossName = be.getName();
					int bossNameWidth = mc.font.width(bossName);
					int bossNameX = (posX + 91) - (bossNameWidth / 2);
					int bossNameY = posY - 9;
					mc.font.drawShadow(matrix, bossName, (float)bossNameX, (float)bossNameY, 16777215);
				}
				posY += e.getIncrement();
				ForgeHooksClient.renderBossEventPost(matrix, mc.getWindow());
				if (posY >= mc.getWindow().getGuiScaledHeight() / 3) {
					break;
				}
			}

		}

	}

	protected void drawBar(PoseStack matrix, int posX, int posY, BossEvent event) {
		this.blit(matrix, posX, posY, 0, event.getColor().ordinal() * 5 * 2, 182, 5);
		if (event.getOverlay() != BossEvent.BossBarOverlay.PROGRESS) {
			this.blit(matrix, posX, posY, 0, 80 + (event.getOverlay().ordinal() - 1) * 5 * 2, 182, 5);
		}

		int i = (int)(event.getProgress() * 183.0F);
		if (i > 0) {
			this.blit(matrix, posX, posY, 0, event.getColor().ordinal() * 5 * 2 + 5, i, 5);
			if (event.getOverlay() != BossEvent.BossBarOverlay.PROGRESS) {
				this.blit(matrix, posX, posY, 0, 80 + (event.getOverlay().ordinal() - 1) * 5 * 2 + 5, i, 5);
			}
		}
	}
	
	public Map<UUID, LerpingBossEvent> getEventsMap() {
		try {
			Field f = ObfuscationReflectionHelper.findField(BossHealthOverlay.class, "f_93699_");
			return (Map<UUID, LerpingBossEvent>) f.get(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected void setupEditorMode() {
		UUID uuid = UUID.randomUUID();
		LerpingBossEvent event = new LerpingBossEvent(uuid, new TextComponent("Boss Bar"), 50.0F, BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS, true, false, true);
		this.update(ClientboundBossEventPacket.createAddPacket(event));
	}

}
