package de.keksuccino.spiffyhud.customization.requirements;

import de.keksuccino.fancymenu.customization.loadingrequirement.LoadingRequirementRegistry;

public class Requirements {

    public static final IsPlayerUsingItemRequirement IS_PLAYER_USING_ITEM = new IsPlayerUsingItemRequirement();

    public static void registerAll() {

        LoadingRequirementRegistry.register(IS_PLAYER_USING_ITEM);

    }

}
