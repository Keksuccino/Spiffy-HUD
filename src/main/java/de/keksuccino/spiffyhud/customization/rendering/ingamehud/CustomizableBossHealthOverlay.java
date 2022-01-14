package de.keksuccino.spiffyhud.customization.rendering.ingamehud;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;
import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.konkrete.reflection.ReflectionHelper;
import de.keksuccino.konkrete.rendering.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.network.packet.s2c.play.BossBarS2CPacket;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CustomizableBossHealthOverlay extends BossBarHud {

	protected static final Identifier GUI_BARS_TEXTURES = new Identifier("textures/gui/bars.png");
	
	protected CustomizableIngameGui handler;
	
	public CustomizableBossHealthOverlay(MinecraftClient clientIn, CustomizableIngameGui handler) {
		super(clientIn);
		this.handler = handler;
		
		if (this.handler.isEditor()) {
			this.setupEditorMode();
		}
	}

	@Override
	public void render(MatrixStack matrix) {

		MinecraftClient mc = MinecraftClient.getInstance();
		Map<UUID, ClientBossBar> events = this.getEventsMap();

		if (!events.isEmpty()) {
			int posX = this.handler.bossBarElement.x;
			int posY = this.handler.bossBarElement.y;

			for(ClientBossBar be : events.values()) {

				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
				RenderUtils.bindTexture(GUI_BARS_TEXTURES);
				this.renderBossBar(matrix, posX, posY, be);
				Text bossName = be.getName();
				int bossNameWidth = mc.textRenderer.getWidth(bossName);
				int bossNameX = (posX + 91) - (bossNameWidth / 2);
				int bossNameY = posY - 9;
				mc.textRenderer.drawWithShadow(matrix, bossName, (float)bossNameX, (float)bossNameY, 16777215);

				posY += (10 + MinecraftClient.getInstance().textRenderer.fontHeight);

				if (posY >= mc.getWindow().getScaledHeight() / 3) {
					break;
				}

			}

		}

	}

	protected void renderBossBar(MatrixStack matrix, int posX, int posY, BossBar event) {
		this.drawTexture(matrix, posX, posY, 0, event.getColor().ordinal() * 5 * 2, 182, 5);
		if (event.getStyle() != BossBar.Style.PROGRESS) {
			this.drawTexture(matrix, posX, posY, 0, 80 + (event.getStyle().ordinal() - 1) * 5 * 2, 182, 5);
		}

		int i = (int)(event.getPercent() * 183.0F);
		if (i > 0) {
			this.drawTexture(matrix, posX, posY, 0, event.getColor().ordinal() * 5 * 2 + 5, i, 5);
			if (event.getStyle() != BossBar.Style.PROGRESS) {
				this.drawTexture(matrix, posX, posY, 0, 80 + (event.getStyle().ordinal() - 1) * 5 * 2 + 5, i, 5);
			}
		}
	}
	
	public Map<UUID, ClientBossBar> getEventsMap() {
		try {
			Field f = ReflectionHelper.findField(BossBarHud.class, "bossBars", "field_2060");
			return (Map<UUID, ClientBossBar>) f.get(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected void setupEditorMode() {
		UUID uuid = UUID.randomUUID();
		ClientBossBar event = new ClientBossBar(uuid, new LiteralText("Boss Bar"), 50.0F, BossBar.Color.PURPLE, BossBar.Style.PROGRESS, true, false, true);
		this.handlePacket(BossBarS2CPacket.add(event));
	}

}
