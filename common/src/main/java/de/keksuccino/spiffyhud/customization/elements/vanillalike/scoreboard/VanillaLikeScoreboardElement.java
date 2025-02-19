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

    // Minecraft instance for accessing game state
    private final Minecraft minecraft = Minecraft.getInstance();

    // These fields will hold the computed sidebar dimensions and original position
    private int sidebarWidth = 100;
    private int sidebarHeight = 100;
    private int sidebarOriginalX = 0;
    private int sidebarOriginalY = 0;

    // Flag to indicate if the sidebar should actually be drawn
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

    /**
     * Main render method.
     *
     * First, it calls renderScoreboard (with no offset) to update the sidebar’s dimensions and original position.
     * Then, it calculates an aligned position based on the element’s absolute bounds and renders the scoreboard
     * with an offset so that it appears inside the element.
     */
    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {

        if (this.minecraft.player == null || this.minecraft.level == null) {
            return;
        }

        // --- Update sidebar dimensions (without applying any offset) ---
        // This call sets sidebarWidth, sidebarHeight, sidebarOriginalX, and sidebarOriginalY.
        this.renderSidebar = false;
        this.renderScoreboard(graphics, 0, 0, false);

        // Calculate aligned position based on the element's absolute bounds
        int elementX = this.getAbsoluteX();
        int elementY = this.getAbsoluteY();
        Integer[] alignedPosition = SpiffyAlignment.calculateElementBodyPosition(
                this.spiffyAlignment,
                elementX,
                elementY,
                this.getAbsoluteWidth(),
                this.getAbsoluteHeight(),
                this.sidebarWidth,
                this.sidebarHeight
        );

        // Compute the offset required so that the sidebar renders at the correct aligned position.
        int offsetX = alignedPosition[0] - this.sidebarOriginalX;
        int offsetY = alignedPosition[1] - this.sidebarOriginalY;

        RenderSystem.enableBlend();

        // Render the scoreboard applying the offset to all drawing coordinates.
        this.renderSidebar = true;
        this.renderScoreboard(graphics, offsetX, offsetY, true);

        RenderingUtils.resetShaderColor(graphics);

    }

    /**
     * Renders the scoreboard.
     *
     * @param graphics    The graphics context.
     * @param offsetX     Horizontal offset to add to drawing coordinates.
     * @param offsetY     Vertical offset to add to drawing coordinates.
     * @param applyOffset If true, the computed offset is applied while drawing.
     */
    private void renderScoreboard(GuiGraphics graphics, int offsetX, int offsetY, boolean applyOffset) {
        Scoreboard scoreboard = this.minecraft.level.getScoreboard();
        Objective objective = null;
        PlayerTeam playerTeam = scoreboard.getPlayersTeam(this.minecraft.player.getScoreboardName());
        int colorId = -1;
        if (playerTeam != null) {
            colorId = playerTeam.getColor().getId();
        }
        if (playerTeam != null && colorId >= 0) {
            objective = scoreboard.getDisplayObjective(3 + colorId);
        }
        // Fallback to display objective 1 if necessary.
        Objective objectiveToRender = (objective != null) ? objective : scoreboard.getDisplayObjective(1);
        // In editor mode, use a dummy objective.
        if (isEditor()) {
            objectiveToRender = new Objective(scoreboard, "", ObjectiveCriteria.DUMMY, Component.empty(), ObjectiveCriteria.RenderType.INTEGER);
        }
        if (objectiveToRender != null) {
            displayScoreboardSidebar(graphics, objectiveToRender, offsetX, offsetY, applyOffset);
        }
    }

    /**
     * Renders the scoreboard sidebar.
     *
     * All drawing coordinates (for background, score lines, and title) are adjusted by (offsetX, offsetY)
     * if applyOffset is true. This ensures that the sidebar is drawn inside the element’s bounds.
     *
     * @param guiGraphics The graphics context.
     * @param objective   The scoreboard objective to render.
     * @param offsetX     Horizontal offset to apply.
     * @param offsetY     Vertical offset to apply.
     * @param applyOffset If true, the offset is added to all drawing coordinates.
     */
    private void displayScoreboardSidebar(GuiGraphics guiGraphics, Objective objective, int offsetX, int offsetY, boolean applyOffset) {

        Scoreboard scoreboard = objective.getScoreboard();

        // Filter out invalid scores and limit to 15 entries
        Collection<Score> rawScores = scoreboard.getPlayerScores(objective);
        List<Score> validScores = rawScores.stream()
                .filter(score -> (score.getOwner() != null) && !score.getOwner().startsWith("#"))
                .collect(Collectors.toList());
        List<Score> scoresToDisplay = validScores.size() > 15
                ? Lists.newArrayList(Iterables.skip(validScores, validScores.size() - 15))
                : validScores;

        // Build a list pairing each score with its formatted display name
        ArrayList<Pair<Score, MutableComponent>> scoreComponents = Lists.newArrayListWithCapacity(scoresToDisplay.size());
        Component title = objective.getDisplayName();

        // In editor mode, use dummy values
        if (isEditor()) {
            title = Component.translatable("spiffyhud.elements.dummy.scoreboard_sidebar.title").withStyle(ChatFormatting.BOLD);
            List<Score> dummyEntries = new ArrayList<>();
            String dummyLineText = I18n.get("spiffyhud.elements.dummy.scoreboard_sidebar.line");
            for (int i = 0; i < 6; i++) {
                dummyEntries.add(new DummyScore(objective, dummyLineText, 0));
            }
            dummyEntries.add(new DummyScore(objective, "", 0));
            scoresToDisplay = dummyEntries;
        }

        // Determine the maximum width required by the title or any score entry.
        int titleWidth = this.getFont().width(title);
        int spacerWidth = this.getFont().width(SPACER);
        int maxEntryWidth = titleWidth;
        for (Score scoreEntry : scoresToDisplay) {
            PlayerTeam team = scoreboard.getPlayersTeam(scoreEntry.getOwner());
            MutableComponent scoreComponent = PlayerTeam.formatNameForTeam(team, Component.literal(scoreEntry.getOwner()));
            scoreComponents.add(Pair.of(scoreEntry, scoreComponent));
            int entryWidth = this.getFont().width(scoreComponent) + spacerWidth + this.getFont().width(Integer.toString(scoreEntry.getScore()));
            maxEntryWidth = Math.max(maxEntryWidth, entryWidth);
        }

        // Compute base positions using screen dimensions.
        int numberOfLines = scoresToDisplay.size();
        int totalLineHeight = numberOfLines * this.getFont().lineHeight;
        int baseY = getScreenHeight() / 2 + totalLineHeight / 3;
        int baseX = getScreenWidth() - maxEntryWidth - 3;

        // Background colors (custom if set)
        int lineBackgroundColor = this.minecraft.options.getBackgroundColor(0.3f);
        if (this.customLineBackgroundColor != null) {
            lineBackgroundColor = this.customLineBackgroundColor.getColorInt();
        }
        int titleBackgroundColor = this.minecraft.options.getBackgroundColor(0.4f);
        if (this.customTitleBackgroundColor != null) {
            titleBackgroundColor = this.customTitleBackgroundColor.getColorInt();
        }

        // Compute the top Y position of the sidebar
        int topY = baseY - numberOfLines * this.getFont().lineHeight;
        // Record sidebar dimensions and original position (before applying any offset)
        this.sidebarWidth = Math.max(1, maxEntryWidth + 4); // adding padding
        this.sidebarHeight = Math.max(1, baseY - (topY - this.getFont().lineHeight - 1));
        this.sidebarOriginalX = baseX - 2;
        this.sidebarOriginalY = topY - this.getFont().lineHeight - 1;

        // Apply offset if needed
        int effectiveBaseX = baseX + (applyOffset ? offsetX : 0);
        int effectiveBaseY = baseY + (applyOffset ? offsetY : 0);

        // If we are in rendering mode, draw each score line and then the title background.
        if (this.renderSidebar) {
            int lineIndex = 0;
            for (Pair<Score, MutableComponent> scorePair : scoreComponents) {
                lineIndex++;
                Score currentScore = scorePair.getFirst();
                Component entryComponent = scorePair.getSecond();
                String scoreText = "" + ChatFormatting.RED + currentScore.getScore();
                // Compute positions for this line
                int lineX = effectiveBaseX;
                int lineY = effectiveBaseY - lineIndex * 9;
                // rightX is computed from the screen width and offset as well
                int rightX = (getScreenWidth() - 3 + 2) + (applyOffset ? offsetX : 0);
                // Draw background for this score line
                guiGraphics.fill(lineX - 2, lineY, rightX, lineY + 9, lineBackgroundColor);
                // Draw the player's name and score
                guiGraphics.drawString(this.getFont(), entryComponent, lineX, lineY, -1, false);
                guiGraphics.drawString(this.getFont(), scoreText, rightX - this.getFont().width(scoreText), lineY, -1, false);
                // On the last line, also draw the title background and title text
                if (lineIndex == scoresToDisplay.size()) {
                    guiGraphics.fill(lineX - 2, lineY - 9 - 1, rightX, lineY - 1, titleBackgroundColor);
                    guiGraphics.fill(lineX - 2, lineY - 1, rightX, lineY, lineBackgroundColor);
                    Font font = this.getFont();
                    int titleX = lineX + maxEntryWidth / 2 - titleWidth / 2;
                    guiGraphics.drawString(font, title, titleX, lineY - 9, -1, false);
                }
            }
        }

    }

    /**
     * Returns the current Font instance.
     */
    private Font getFont() {
        return Minecraft.getInstance().font;
    }

    /**
     * The absolute width of this element equals the sidebar width.
     */
    @Override
    public int getAbsoluteWidth() {
        return this.sidebarWidth;
    }

    /**
     * The absolute height of this element equals the sidebar height.
     */
    @Override
    public int getAbsoluteHeight() {
        return this.sidebarHeight;
    }

    /**
     * DummyScore is used in editor mode to simulate scoreboard entries.
     */
    private static class DummyScore extends Score {
        int score;

        public DummyScore(Objective objective, String display, int score) {
            super(objective.getScoreboard(), objective, display);
            this.score = score;
        }

        @Override
        public int getScore() {
            return this.score;
        }
    }

}
