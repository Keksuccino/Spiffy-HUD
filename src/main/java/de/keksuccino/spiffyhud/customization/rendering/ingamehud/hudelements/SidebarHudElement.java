package de.keksuccino.spiffyhud.customization.rendering.ingamehud.hudelements;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;

import de.keksuccino.spiffyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import de.keksuccino.konkrete.rendering.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

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
	public void render(MatrixStack matrix, int scaledWidth, int scaledHeight, float partialTicks) {

		if (this.visible) {
			this.renderScoreboard(matrix);
		}

	}

	protected void renderScoreboard(MatrixStack matrix) {
		
		Color customHeadlineBackColor = null;
		if (this.customHeadlineBackgroundColorHex != null) {
			customHeadlineBackColor = RenderUtils.getColorFromHexString(this.customHeadlineBackgroundColorHex);
		}
		Color customBodyBackColor = null;
		if (this.customBodyBackgroundColorHex != null) {
			customBodyBackColor = RenderUtils.getColorFromHexString(this.customBodyBackgroundColorHex);
		}

		Scoreboard scoreboard0 = this.mc.world.getScoreboard();
		ScoreboardObjective objective0 = null;
		Team team = scoreboard0.getPlayerTeam(this.mc.player.getEntityName());
		if (team != null) {
			int j2 = team.getColor().getColorIndex();
			if (j2 >= 0) {
				objective0 = scoreboard0.getObjectiveForSlot(3 + j2);
			}
		}
		ScoreboardObjective objective = objective0 != null ? objective0 : scoreboard0.getObjectiveForSlot(1);

		if (objective != null) {

			TextRenderer font = MinecraftClient.getInstance().textRenderer;

			Scoreboard scoreboard = objective.getScoreboard();
			Collection<ScoreboardPlayerScore> collection = scoreboard.getAllPlayerScores(objective);
			List<ScoreboardPlayerScore> list = collection.stream().filter((score) -> {
				return score.getPlayerName() != null && !score.getPlayerName().startsWith("#");
			}).collect(Collectors.toList());
			if (list.size() > 15) {
				collection = Lists.newArrayList(Iterables.skip(list, collection.size() - 15));
			} else {
				collection = list;
			}

			List<Pair<ScoreboardPlayerScore, Text>> rowList = Lists.newArrayListWithCapacity(collection.size());
			Text scoreboardHeadlineText = objective.getDisplayName();
			if (this.handler.isEditor()) {
				scoreboardHeadlineText = new LiteralText("Headline");
			}
			int scoreboardHeadlineWidth = font.getWidth(scoreboardHeadlineText);
			int scoreboardWidth = scoreboardHeadlineWidth;
			int k = font.getWidth(": ");

			LiteralText dummyName = new LiteralText("Player Name");
			
			if (!this.handler.isEditor()) {
				for(ScoreboardPlayerScore score : collection) {
					Team scoreplayerteam2 = scoreboard.getPlayerTeam(score.getPlayerName());
					Text itextcomponent1 = Team.decorateName(scoreplayerteam2, new LiteralText(score.getPlayerName()));
					rowList.add(Pair.of(score, itextcomponent1));
					scoreboardWidth = Math.max(scoreboardWidth, font.getWidth(itextcomponent1) + k + font.getWidth(Integer.toString(score.getScore())));
				}
			} else {
				ScoreboardPlayerScore sc = new ScoreboardPlayerScore(scoreboard, objective, dummyName.getString()) {
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
				scoreboardWidth = (font.getWidth(dummyName) + k + font.getWidth(Integer.toString(0))) + 4;
			}
			
			int scoreboardHeight = (collection.size() * 9) + 9 + 1;
			if (this.handler.isEditor()) {
				scoreboardHeight = (rowList.size() * 9) + 9 + 1;
			}
			
			this.width = scoreboardWidth;
			this.height = scoreboardHeight;

			int posY = this.y + scoreboardHeight;
			int posX = this.x;
			
			int headlineBackColor = this.mc.options.getTextBackgroundColor(0.4F);
			if (customHeadlineBackColor != null) {
				headlineBackColor = customHeadlineBackColor.getRGB();
			}
			int bodyBackColor = this.mc.options.getTextBackgroundColor(0.3F);
			if (customBodyBackColor != null) {
				bodyBackColor = customBodyBackColor.getRGB();
			}

			//Loop over all sidebar rows
			int row = 0;
			for(Pair<ScoreboardPlayerScore, Text> pair : rowList) {
				
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
				
				ScoreboardPlayerScore score1 = pair.getFirst();
				Text rowText = pair.getSecond();
				String rowScore =  "" + score1.getScore();
				if (scoreTextColor == -1) {
					rowScore = Formatting.RED + rowScore;
				}
				
				int rowPosY = posY - row * 9;
				int stringPosXLeft = posX + 2;
				int stringPosXRight = posX + scoreboardWidth - 1;

				//Draw line background
				fill(matrix, posX, rowPosY, posX + scoreboardWidth, rowPosY + 9, bodyBackColor);

				//Draw line text
				font.draw(matrix, rowText, (float)stringPosXLeft, (float)rowPosY, nameTextColor);
				//Draw line score
				font.draw(matrix, rowScore, (float)(stringPosXRight - font.getWidth(rowScore)), (float)rowPosY, scoreTextColor);
				
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
