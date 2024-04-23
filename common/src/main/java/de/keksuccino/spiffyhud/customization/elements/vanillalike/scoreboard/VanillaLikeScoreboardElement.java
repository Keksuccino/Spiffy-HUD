package de.keksuccino.spiffyhud.customization.elements.vanillalike.scoreboard;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.util.rendering.DrawableColor;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.spiffyhud.util.rendering.ElementMobilizer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.numbers.NumberFormat;
import net.minecraft.network.chat.numbers.StyledFormat;
import net.minecraft.world.scores.*;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class VanillaLikeScoreboardElement extends AbstractElement {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Comparator<PlayerScoreEntry> SCORE_DISPLAY_ORDER = Comparator.comparing(PlayerScoreEntry::value).reversed().thenComparing(PlayerScoreEntry::owner, String.CASE_INSENSITIVE_ORDER);
    private static final String SPACER = ": ";

    private final Minecraft minecraft = Minecraft.getInstance();
    private int sidebarWidth = 100;
    private int sidebarHeight = 100;
    private int sidebarOriginalX = 0;
    private int sidebarOriginalY = 0;
    private boolean renderSidebar = false;

    @NotNull
    public de.keksuccino.spiffyhud.util.Alignment alignment = de.keksuccino.spiffyhud.util.Alignment.TOP_LEFT;
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
        Integer[] alignedBody = de.keksuccino.spiffyhud.util.Alignment.calculateElementBodyPosition(this.alignment, x, y, this.getAbsoluteWidth(), this.getAbsoluteHeight(), this.sidebarWidth, this.sidebarHeight);
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

        Objective objective2;
        DisplaySlot displaySlot;
        Scoreboard scoreboard = this.minecraft.level.getScoreboard();
        Objective objective = null;
        PlayerTeam playerTeam = scoreboard.getPlayersTeam(this.minecraft.player.getScoreboardName());
        if (playerTeam != null && (displaySlot = DisplaySlot.teamColorToSlot(playerTeam.getColor())) != null) {
            objective = scoreboard.getDisplayObjective(displaySlot);
        }
        objective2 = objective != null ? objective : scoreboard.getDisplayObjective(DisplaySlot.SIDEBAR);
        //Tweak to Vanilla logic
        if (isEditor()) objective2 = new Objective(scoreboard, "", ObjectiveCriteria.DUMMY, Component.empty(), ObjectiveCriteria.RenderType.INTEGER, false, null);
        //---------------------------
        if (objective2 != null) {
            this.displayScoreboardSidebar(graphics, objective2);
        }

    }

    private void displayScoreboardSidebar(GuiGraphics graphics, Objective objective) {
        int titleWidth;
        Scoreboard scoreboard = objective.getScoreboard();
        NumberFormat numberFormat = objective.numberFormatOrDefault(StyledFormat.SIDEBAR_DEFAULT);
        record DisplayEntry(Component name, Component score, int scoreWidth) {}
        DisplayEntry[] lvs = (DisplayEntry[])scoreboard.listPlayerScores(objective).stream().filter(playerScoreEntry -> !playerScoreEntry.isHidden()).sorted(SCORE_DISPLAY_ORDER).limit(15L).map(playerScoreEntry -> {
            PlayerTeam playerTeam = scoreboard.getPlayersTeam(playerScoreEntry.owner());
            Component component = playerScoreEntry.ownerName();
            MutableComponent component2 = PlayerTeam.formatNameForTeam(playerTeam, component);
            MutableComponent component3 = playerScoreEntry.formatValue(numberFormat);
            int i = this.getFont().width(component3);
            return new DisplayEntry(component2, component3, i);
        }).toArray(i -> new DisplayEntry[i]);
        Component component = objective.getDisplayName();
        //Tweak to Vanilla logic
        if (isEditor()) {
            component = Component.translatable("spiffyhud.elements.dummy.scoreboard_sidebar.title").withStyle(ChatFormatting.BOLD);
            List<DisplayEntry> entryList = new ArrayList<>();
            Component name = Component.translatable("spiffyhud.elements.dummy.scoreboard_sidebar.line");
            Component score = Component.literal("0").withStyle(ChatFormatting.RED);
            int scoreWidth = this.minecraft.font.width(score);
            entryList.add(new DisplayEntry(Component.empty(), Component.empty(), scoreWidth));
            entryList.add(new DisplayEntry(name, score, scoreWidth));
            entryList.add(new DisplayEntry(name, score, scoreWidth));
            entryList.add(new DisplayEntry(name, score, scoreWidth));
            entryList.add(new DisplayEntry(name, score, scoreWidth));
            entryList.add(new DisplayEntry(name, score, scoreWidth));
            entryList.add(new DisplayEntry(name, score, scoreWidth));
            lvs = entryList.toArray(new DisplayEntry[0]);
        }
        //--------------------------
        int totalSidebarWidth = titleWidth = this.getFont().width(component);
        int k = this.getFont().width(SPACER);
        for (DisplayEntry lv : lvs) {
            totalSidebarWidth = Math.max(totalSidebarWidth, this.getFont().width(lv.name) + (lv.scoreWidth > 0 ? k + lv.scoreWidth : 0));
        }
        int l = totalSidebarWidth;
        DisplayEntry[] finalLvs = lvs;
        Component finalComponent = component;
        graphics.drawManaged(() -> {
            int lineCount = finalLvs.length;
            int totalLineHeight = lineCount * this.getFont().lineHeight;
            int m = getScreenHeight() / 2 + totalLineHeight / 3;
            int n = 3;
            int o = getScreenWidth() - l - 3;
            int p = getScreenWidth() - 3 + 2;
            int linesBackgroundColor = this.minecraft.options.getBackgroundColor(0.3f);
            //Tweak to Vanilla logic
            if (this.customLineBackgroundColor != null) linesBackgroundColor = this.customLineBackgroundColor.getColorInt();
            int titleBackgroundColor = this.minecraft.options.getBackgroundColor(0.4f);
            //Tweak to Vanilla logic
            if (this.customTitleBackgroundColor != null) titleBackgroundColor = this.customTitleBackgroundColor.getColorInt();
            int s = m - lineCount * this.getFont().lineHeight;
            //Tweak to Vanilla logic
            this.sidebarWidth = Math.max(1, p - (o - 2));
            this.sidebarHeight = Math.max(1, m - (s - this.getFont().lineHeight - 1));
            this.sidebarOriginalX = o - 2;
            this.sidebarOriginalY = s - this.getFont().lineHeight - 1;
            //-------------------------
            //Tweak to Vanilla logic (if wrap)
            if (this.renderSidebar) {
                graphics.fill(o - 2, s - this.getFont().lineHeight - 1, p, s - 1, titleBackgroundColor);
                graphics.fill(o - 2, s - 1, p, m, linesBackgroundColor);
                graphics.drawString(this.getFont(), finalComponent, o + l / 2 - titleWidth / 2, s - this.getFont().lineHeight, -1, false);
                for (int t = 0; t < lineCount; ++t) {
                    DisplayEntry lv = finalLvs[t];
                    int u = m - (lineCount - t) * this.getFont().lineHeight;
                    graphics.drawString(this.getFont(), lv.name, o, u, -1, false);
                    graphics.drawString(this.getFont(), lv.score, p - lv.scoreWidth, u, -1, false);
                }
            }
        });
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

}
