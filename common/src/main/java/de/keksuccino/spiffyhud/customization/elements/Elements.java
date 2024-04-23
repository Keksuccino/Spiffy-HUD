package de.keksuccino.spiffyhud.customization.elements;

import de.keksuccino.fancymenu.customization.element.ElementRegistry;
import de.keksuccino.spiffyhud.customization.elements.vanillalike.hotbar.VanillaLikeHotbarElementBuilder;
import de.keksuccino.spiffyhud.customization.elements.vanillalike.playerhealth.VanillaLikePlayerHealthElementBuilder;
import de.keksuccino.spiffyhud.customization.elements.vanillalike.scoreboard.VanillaLikeScoreboardElementBuilder;

public class Elements {

    public static final VanillaLikeHotbarElementBuilder VANILLA_LIKE_HOTBAR = new VanillaLikeHotbarElementBuilder();
    public static final VanillaLikeScoreboardElementBuilder VANILLA_LIKE_SCOREBOARD = new VanillaLikeScoreboardElementBuilder();
    public static final VanillaLikePlayerHealthElementBuilder VANILLA_LIKE_PLAYER_HEALTH = new VanillaLikePlayerHealthElementBuilder();

    public static void registerAll() {

        ElementRegistry.register(VANILLA_LIKE_HOTBAR);
        ElementRegistry.register(VANILLA_LIKE_SCOREBOARD);
        ElementRegistry.register(VANILLA_LIKE_PLAYER_HEALTH);

    }

}
