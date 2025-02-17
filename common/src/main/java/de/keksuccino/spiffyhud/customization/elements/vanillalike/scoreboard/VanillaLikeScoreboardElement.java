package de.keksuccino.spiffyhud.customization.elements.vanillalike.scoreboard;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.util.rendering.DrawableColor;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.spiffyhud.util.SpiffyAlignment;
import de.keksuccino.spiffyhud.util.rendering.ElementMobilizer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.scores.*;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class VanillaLikeScoreboardElement extends AbstractElement {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String SPACER = ": ";

    private final Minecraft minecraft = Minecraft.getInstance();
    private int sidebarWidth = 100;
    private int sidebarHeight = 100;
    private int sidebarOriginalX = 0;
    private int sidebarOriginalY = 0;
    private boolean renderSidebar = false;

    @NotNull
    public SpiffyAlignment spiffyAlignment = SpiffyAlignment.TOP_LEFT;
    @Nullable
    public DrawableColor customTitleBackgroundColor = null;
    @Nullable
    public DrawableColor customLineBackgroundColor = null;

    public VanillaLikeScoreboardElement(@NotNull ElementBuilder<?, ?> builder) {
        super(builder);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {

        if (this.minecraft.player == null) return;
        if (this.minecraft.level == null) return;

        //Update size and originalPos of sidebar before render
        this.renderSidebar = false;
        this.renderScoreboard(graphics);
        this.renderSidebar = true;

        int x = this.getAbsoluteX();
        int y = this.getAbsoluteY();
        Integer[] alignedBody = SpiffyAlignment.calculateElementBodyPosition(this.spiffyAlignment, x, y, this.getAbsoluteWidth(), this.getAbsoluteHeight(), this.sidebarWidth, this.sidebarHeight);
        x = alignedBody[0];
        y = alignedBody[1];

        ElementMobilizer.mobilize(graphics, -this.sidebarOriginalX, -this.sidebarOriginalY, x, y, () -> {

            RenderSystem.enableBlend();
            RenderingUtils.resetShaderColor(graphics);

            //-------------------------------

            this.renderScoreboard(graphics);

            //-------------------------------

            RenderingUtils.resetShaderColor(graphics);

        });

    }

    private void renderScoreboard(GuiGraphics graphics) {

        int m;
        Objective objective2;
        Scoreboard scoreboard = this.minecraft.level.getScoreboard();
        Objective objective = null;
        PlayerTeam playerTeam = scoreboard.getPlayersTeam(this.minecraft.player.getScoreboardName());
        if (playerTeam != null && (m = playerTeam.getColor().getId()) >= 0) {
            objective = scoreboard.getDisplayObjective(3 + m);
        }
        objective2 = objective != null ? objective : scoreboard.getDisplayObjective(1);
        //Tweak to Vanilla logic
        if (isEditor()) objective2 = new Objective(scoreboard, "", ObjectiveCriteria.DUMMY, Component.empty(), ObjectiveCriteria.RenderType.INTEGER);
        //---------------------------
        if (objective2 != null) {
            this.displayScoreboardSidebar(graphics, objective2);
        }

    }

    private void displayScoreboardSidebar(GuiGraphics guiGraphics, Objective objective) {
        int i;
        Scoreboard scoreboard = objective.getScoreboard();
        Collection<Score> scores = scoreboard.getPlayerScores(objective);
        List<Score> tempScores = scores.stream().filter(score -> (score.getOwner() != null) && !score.getOwner().startsWith("#")).collect(Collectors.toList());
        scores = tempScores.size() > 15 ? Lists.newArrayList(Iterables.skip(tempScores, scores.size() - 15)) : tempScores;
        ArrayList<Pair<Score, MutableComponent>> scoreComponents = Lists.newArrayListWithCapacity(scores.size());
        Component title = objective.getDisplayName();
        //Tweak to Vanilla logic
        if (isEditor()) {
            title = Component.translatable("spiffyhud.elements.dummy.scoreboard_sidebar.title").withStyle(ChatFormatting.BOLD);
            List<Score> entryList = new ArrayList<>();
            String name = I18n.get("spiffyhud.elements.dummy.scoreboard_sidebar.line");
            entryList.add(new DummyScore(objective, "", 0));
            entryList.add(new DummyScore(objective, name, 0));
            entryList.add(new DummyScore(objective, name, 0));
            entryList.add(new DummyScore(objective, name, 0));
            entryList.add(new DummyScore(objective, name, 0));
            entryList.add(new DummyScore(objective, name, 0));
            entryList.add(new DummyScore(objective, name, 0));
            scores = entryList;
        }
        //--------------------------
        int j = i = this.getFont().width(title);
        int k = this.getFont().width(SPACER);
        for (Score score2 : scores) {
            PlayerTeam playerTeam = scoreboard.getPlayersTeam(score2.getOwner());
            MutableComponent component2 = PlayerTeam.formatNameForTeam(playerTeam, Component.literal(score2.getOwner()));
            scoreComponents.add(Pair.of(score2, component2));
            j = Math.max(j, this.getFont().width(component2) + k + this.getFont().width(Integer.toString(score2.getScore())));
        }
        int lineCount = scores.size();
        Objects.requireNonNull(this.getFont());
        int totalLineHeight = lineCount * this.getFont().lineHeight;
        int m = getScreenHeight() / 2 + totalLineHeight / 3;
        int o = getScreenWidth() - j - 3;
        int p = 0;
        int linesBackgroundColor = this.minecraft.options.getBackgroundColor(0.3f);
        //Tweak to Vanilla logic
        if (this.customLineBackgroundColor != null) linesBackgroundColor = this.customLineBackgroundColor.getColorInt();
        int titleBackgroundColor = this.minecraft.options.getBackgroundColor(0.4f);
        //Tweak to Vanilla logic
        if (this.customTitleBackgroundColor != null) titleBackgroundColor = this.customTitleBackgroundColor.getColorInt();
        //Tweak to Vanilla logic
        int tweakValue = m - lineCount * this.getFont().lineHeight;
        this.sidebarWidth = Math.max(1, p - (o - 2));
        this.sidebarHeight = Math.max(1, m - (tweakValue - this.getFont().lineHeight - 1));
        this.sidebarOriginalX = o - 2;
        this.sidebarOriginalY = tweakValue - this.getFont().lineHeight - 1;
        //-------------------------
        //Tweak to Vanilla logic (if wrap)
        if (this.renderSidebar) {
            for (Pair pair : scoreComponents) {
                ++p;
                Score score2 = (Score) pair.getFirst();
                Component component3 = (Component) pair.getSecond();
                String string = "" + ChatFormatting.RED + score2.getScore();
                int s = o;
                Objects.requireNonNull(this.getFont());
                int t = m - p * 9;
                int u = getScreenWidth() - 3 + 2;
                Objects.requireNonNull(this.getFont());
                guiGraphics.fill(s - 2, t, u, t + 9, linesBackgroundColor);
                guiGraphics.drawString(this.getFont(), component3, s, t, -1, false);
                guiGraphics.drawString(this.getFont(), string, u - this.getFont().width(string), t, -1, false);
                if (p != scores.size()) continue;
                Objects.requireNonNull(this.getFont());
                guiGraphics.fill(s - 2, t - 9 - 1, u, t - 1, titleBackgroundColor);
                guiGraphics.fill(s - 2, t - 1, u, t, linesBackgroundColor);
                Font font = this.getFont();
                int n3 = s + j / 2 - i / 2;
                Objects.requireNonNull(this.getFont());
                guiGraphics.drawString(font, title, n3, t - 9, -1, false);
            }
        }
    }

    private Font getFont() {
        return Minecraft.getInstance().font;
    }

    @Override
    public int getAbsoluteWidth() {
        return 100;
    }

    @Override
    public int getAbsoluteHeight() {
        return 100;
    }

    private static class DummyScore extends Score {

        int score;

        public DummyScore(Objective objective, String display, int score) {
            super(objective.getScoreboard(), objective, "");
            this.score = score;
        }

        @Override
        public int getScore() {
            return this.score;
        }

    }

}
