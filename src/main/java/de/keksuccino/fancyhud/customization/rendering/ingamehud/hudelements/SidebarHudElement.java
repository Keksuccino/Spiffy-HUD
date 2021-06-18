package de.keksuccino.fancyhud.customization.rendering.ingamehud.hudelements;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.datafixers.util.Pair;

import de.keksuccino.fancyhud.customization.rendering.ingamehud.CustomizableIngameGui;
import de.keksuccino.konkrete.rendering.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

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
		ScoreObjective objective0 = null;
		ScorePlayerTeam scoreplayerteam = scoreboard0.getPlayersTeam(this.mc.player.getScoreboardName());
		if (scoreplayerteam != null) {
			int j2 = scoreplayerteam.getColor().getColorIndex();
			if (j2 >= 0) {
				objective0 = scoreboard0.getObjectiveInDisplaySlot(3 + j2);
			}
		}
		ScoreObjective objective = objective0 != null ? objective0 : scoreboard0.getObjectiveInDisplaySlot(1);

		if (objective != null) {

			FontRenderer font = Minecraft.getInstance().fontRenderer;
			Scoreboard scoreboard = objective.getScoreboard();
			Collection<Score> collection = scoreboard.getSortedScores(objective);
			List<Score> list = collection.stream().filter((score) -> {
				return score.getPlayerName() != null && !score.getPlayerName().startsWith("#");
			}).collect(Collectors.toList());
			if (list.size() > 15) {
				collection = Lists.newArrayList(Iterables.skip(list, collection.size() - 15));
			} else {
				collection = list;
			}

			List<Pair<Score, ITextComponent>> rowList = Lists.newArrayListWithCapacity(collection.size());
			ITextComponent scoreboardHeadlineText = objective.getDisplayName();
			if (this.handler.isEditor()) {
				scoreboardHeadlineText = new StringTextComponent("Headline");
			}
			int scoreboardHeadlineWidth = font.getStringPropertyWidth(scoreboardHeadlineText);
			int scoreboardWidth = scoreboardHeadlineWidth;
			int k = font.getStringWidth(": ");

			StringTextComponent dummyName = new StringTextComponent("Player Name");
			
			if (!this.handler.isEditor()) {
				for(Score score : collection) {
					ScorePlayerTeam scoreplayerteam2 = scoreboard.getPlayersTeam(score.getPlayerName());
					ITextComponent itextcomponent1 = ScorePlayerTeam.func_237500_a_(scoreplayerteam2, new StringTextComponent(score.getPlayerName()));
					rowList.add(Pair.of(score, itextcomponent1));
					scoreboardWidth = Math.max(scoreboardWidth, font.getStringPropertyWidth(itextcomponent1) + k + font.getStringWidth(Integer.toString(score.getScorePoints())));
				}
			} else {
				Score sc = new Score(scoreboard, objective, dummyName.getString()) {
					@Override
					public int getScorePoints() {
						return 0;
					}
				};
				rowList = new ArrayList<Pair<Score,ITextComponent>>();
				rowList.add(Pair.of(sc, dummyName));
				rowList.add(Pair.of(sc, dummyName));
				rowList.add(Pair.of(sc, dummyName));
				rowList.add(Pair.of(sc, dummyName));
			}
			
			scoreboardWidth += 4;
			
			if (this.handler.isEditor()) {
				scoreboardWidth = (font.getStringPropertyWidth(dummyName) + k + font.getStringWidth(Integer.toString(0))) + 4;
			}
			
			int scoreboardHeight = (collection.size() * 9) + 9 + 1;
			if (this.handler.isEditor()) {
				scoreboardHeight = (rowList.size() * 9) + 9 + 1;
			}
			
			this.width = scoreboardWidth;
			this.height = scoreboardHeight;
			
//			int posY = this.scaledHeight / 2 + scoreboardHeight / 3;
			int posY = this.y + scoreboardHeight;
//			int posX = this.scaledWidth - scoreboardWidth - 3;
			int posX = this.x;
			
			int headlineBackColor = this.mc.gameSettings.getTextBackgroundColor(0.4F);
			if (customHeadlineBackColor != null) {
				headlineBackColor = customHeadlineBackColor.getRGB();
			}
			int bodyBackColor = this.mc.gameSettings.getTextBackgroundColor(0.3F);
			if (customBodyBackColor != null) {
				bodyBackColor = customBodyBackColor.getRGB();
			}

			int row = 0;
			for(Pair<Score, ITextComponent> pair : rowList) {
				
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
				ITextComponent rowText = pair.getSecond();
				String rowScore =  "" + score1.getScorePoints();
				if (scoreTextColor == -1) {
					rowScore = TextFormatting.RED + rowScore;
				}
				
				int rowPosY = posY - row * 9;
				int stringPosXLeft = posX + 2;
				int stringPosXRight = posX + scoreboardWidth - 1;
				
				fill(matrix, posX, rowPosY, posX + scoreboardWidth, rowPosY + 9, bodyBackColor);
				font.drawText(matrix, rowText, (float)stringPosXLeft, (float)rowPosY, nameTextColor);
				font.drawString(matrix, rowScore, (float)(stringPosXRight - font.getStringWidth(rowScore)), (float)rowPosY, scoreTextColor);
				
				boolean b = (row == collection.size());
				if (this.handler.isEditor()) {
					b = (row == rowList.size());
				}
				if (b) {
					fill(matrix, posX, rowPosY - 9 - 1, posX + scoreboardWidth, rowPosY - 1, headlineBackColor);
					fill(matrix, posX, rowPosY - 1, posX + scoreboardWidth, rowPosY, bodyBackColor);
					font.drawText(matrix, scoreboardHeadlineText, (float)(posX + scoreboardWidth / 2 - scoreboardHeadlineWidth / 2), (float)(rowPosY - 9), headlineTextColor);
				}
				
			}

		}

	}

}
