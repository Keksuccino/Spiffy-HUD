package de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;

import de.keksuccino.spiffyhud.SpiffyHud;
import de.keksuccino.spiffyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import de.keksuccino.konkrete.rendering.RenderUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.Scoreboard;
import net.minecraftforge.client.gui.ForgeIngameGui;

public class SidebarHudElement extends IngameHudElement {
	
	public String customHeadlineBackgroundColorHex = null;
	public String customBodyBackgroundColorHex = null;
	
	public String customHeadlineTextBaseColorHex = null;
	public String customNameTextBaseColorHex = null;
	public String customScoreTextBaseColorHex = null;
	
	public String custom = null;

	public SidebarHudElement(CustomizableIngameGui handler) {
		super(handler);
	}

	@Override
	public void render(PoseStack matrix, int scaledWidth, int scaledHeight, float partialTicks) {

		if (this.fireEvents) {
			if (handler.pre(ForgeIngameGui.SCOREBOARD_ELEMENT, matrix)) return;
		}

		if (this.visible) {
			this.renderScoreboard(matrix);
		}

		if (this.fireEvents) {
			handler.post(ForgeIngameGui.SCOREBOARD_ELEMENT, matrix);
		}

	}

	protected void renderScoreboard(PoseStack matrix) {
		
		Color customHeadlineBackColor = null;
		if (this.customHeadlineBackgroundColorHex != null) {
			customHeadlineBackColor = RenderUtils.getColorFromHexString(this.customHeadlineBackgroundColorHex);
		}
		Color customBodyBackColor = null;
		if (this.customBodyBackgroundColorHex != null) {
			customBodyBackColor = RenderUtils.getColorFromHexString(this.customBodyBackgroundColorHex);
		}

		Scoreboard scoreboard0 = this.mc.level.getScoreboard();
		Objective objective0 = null;
		PlayerTeam team = scoreboard0.getPlayersTeam(this.mc.player.getScoreboardName());
		if (team != null) {
			int j2 = team.getColor().getId();
			if (j2 >= 0) {
				objective0 = scoreboard0.getDisplayObjective(3 + j2);
			}
		}
		Objective objective = objective0 != null ? objective0 : scoreboard0.getDisplayObjective(1);

		if (objective != null) {

			Font font = Minecraft.getInstance().font;

			Scoreboard scoreboard = objective.getScoreboard();
			Collection<Score> collection = scoreboard.getPlayerScores(objective);
			List<Score> list = collection.stream().filter((score) -> {
				return score.getOwner() != null && !score.getOwner().startsWith("#");
			}).collect(Collectors.toList());
			if (list.size() > 15) {
				collection = Lists.newArrayList(Iterables.skip(list, collection.size() - 15));
			} else {
				collection = list;
			}

			List<Pair<Score, Component>> rowList = Lists.newArrayListWithCapacity(collection.size());
			Component scoreboardHeadlineText = objective.getDisplayName();
			if (this.handler.isEditor()) {
				scoreboardHeadlineText = new TextComponent("Headline");
			}
			int scoreboardHeadlineWidth = font.width(scoreboardHeadlineText);
			int scoreboardWidth = scoreboardHeadlineWidth;
			int k = font.width(": ");

			TextComponent dummyName = new TextComponent("Player Name");
			
			if (!this.handler.isEditor()) {
				for(Score score : collection) {
					PlayerTeam scoreplayerteam2 = scoreboard.getPlayersTeam(score.getOwner());
					Component itextcomponent1 = PlayerTeam.formatNameForTeam(scoreplayerteam2, new TextComponent(score.getOwner()));
					rowList.add(Pair.of(score, itextcomponent1));
					scoreboardWidth = Math.max(scoreboardWidth, font.width(itextcomponent1) + k + font.width(Integer.toString(score.getScore())));
				}
			} else {
				Score sc = new Score(scoreboard, objective, dummyName.getString()) {
					@Override
					public int getScore() {
						return 0;
					}
				};
				rowList = new ArrayList<>();
				rowList.add(Pair.of(sc, dummyName));
				rowList.add(Pair.of(sc, dummyName));
				rowList.add(Pair.of(sc, dummyName));
				rowList.add(Pair.of(sc, dummyName));
			}
			
			scoreboardWidth += 4;
			
			if (this.handler.isEditor()) {
				scoreboardWidth = (font.width(dummyName) + k + font.width(Integer.toString(0))) + 4;
			}
			
			int scoreboardHeight = (collection.size() * 9) + 9 + 1;
			if (this.handler.isEditor()) {
				scoreboardHeight = (rowList.size() * 9) + 9 + 1;
			}
			
			this.width = scoreboardWidth;
			this.height = scoreboardHeight;

			int posY = this.y + scoreboardHeight;
			int posX = this.x;
			
			int headlineBackColor = this.mc.options.getBackgroundColor(0.4F);
			if (customHeadlineBackColor != null) {
				headlineBackColor = customHeadlineBackColor.getRGB();
			}
			int bodyBackColor = this.mc.options.getBackgroundColor(0.3F);
			if (customBodyBackColor != null) {
				bodyBackColor = customBodyBackColor.getRGB();
			}

			//Loop over all sidebar rows
			int row = 0;
			for(Pair<Score, Component> pair : rowList) {
				
				++row;

				int headlineTextColor = -1;
				if (this.customHeadlineTextBaseColorHex != null) {
					Color c = RenderUtils.getColorFromHexString(this.customHeadlineTextBaseColorHex);
					if (c != null) {
						headlineTextColor = c.getRGB();
					}
				}
				int nameTextColor = -1;
				if (this.customNameTextBaseColorHex != null) {
					Color c = RenderUtils.getColorFromHexString(this.customNameTextBaseColorHex);
					if (c != null) {
						nameTextColor = c.getRGB();
					}
				}
				int scoreTextColor = -1;
				if (this.customScoreTextBaseColorHex != null) {
					Color c = RenderUtils.getColorFromHexString(this.customScoreTextBaseColorHex);
					if (c != null) {
						scoreTextColor = c.getRGB();
					}
				}
				
				Score score1 = pair.getFirst();
				Component rowText = pair.getSecond();
				String rowScore =  "" + score1.getScore();
				if (scoreTextColor == -1) {
					rowScore = ChatFormatting.RED + rowScore;
				}
				
				int rowPosY = posY - row * 9;
				int stringPosXLeft = posX + 2;
				int stringPosXRight = posX + scoreboardWidth - 1;

				//Draw line background
				fill(matrix, posX, rowPosY, posX + scoreboardWidth, rowPosY + 9, bodyBackColor);

				//Draw line text
				font.draw(matrix, rowText, (float)stringPosXLeft, (float)rowPosY, nameTextColor);
				//Draw line score
				font.draw(matrix, rowScore, (float)(stringPosXRight - font.width(rowScore)), (float)rowPosY, scoreTextColor);
				
				boolean b = (row == collection.size());
				if (this.handler.isEditor()) {
					b = (row == rowList.size());
				}
				if (b) {
					fill(matrix, posX, rowPosY - 9 - 1, posX + scoreboardWidth, rowPosY - 1, headlineBackColor);
					fill(matrix, posX, rowPosY - 1, posX + scoreboardWidth, rowPosY, bodyBackColor);
					font.draw(matrix, scoreboardHeadlineText, (float)(posX + scoreboardWidth / 2 - scoreboardHeadlineWidth / 2), (float)(rowPosY - 9), headlineTextColor);
				}
				
			}

		}

	}

}
