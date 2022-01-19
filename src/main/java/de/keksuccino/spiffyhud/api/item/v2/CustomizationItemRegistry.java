//TODO übernehmen
package de.keksuccino.spiffyhud.api.item.v2;

import de.keksuccino.spiffyhud.SpiffyHud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomizationItemRegistry {

    protected static Map<String, CustomizationItemContainer> customizationItems = new HashMap<String, CustomizationItemContainer>();

    /**
     * Register your own customization items here.
     */
    public static void registerItem(CustomizationItemContainer item) {
        if (item != null) {
            if (item.getIdentifier() != null) {
                if (customizationItems.containsKey(item.getIdentifier())) {
                    SpiffyHud.LOGGER.warn("[SPIFFY HUD] WARNING! A customization item with the identifier '" + item.getIdentifier() + "' is already registered! Overriding item!");
                }
                customizationItems.put(item.getIdentifier(), item);
            } else {
                SpiffyHud.LOGGER.error("[SPIFFY HUD] ERROR! Item identifier cannot be null for CustomizationItemContainers!");
            }
        }
    }

    /**
     * Unregister a previously added item.
     */
    public static void unregisterItem(String itemIdentifier) {
        customizationItems.remove(itemIdentifier);
    }

    /**
     * Get all registered items as list.
     */
    public static List<CustomizationItemContainer> getItems() {
        List<CustomizationItemContainer> l = new ArrayList<>();
        l.addAll(customizationItems.values());
        return l;
    }

    /**
     * Get a registered item by its identifier.
     */
    public static CustomizationItemContainer getItem(String itemIdentifier) {
        return customizationItems.get(itemIdentifier);
    }

}
