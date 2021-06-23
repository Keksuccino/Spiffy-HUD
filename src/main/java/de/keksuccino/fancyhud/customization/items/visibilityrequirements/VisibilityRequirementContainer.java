package de.keksuccino.fancyhud.customization.items.visibilityrequirements;

import de.keksuccino.fancyhud.customization.items.CustomizationItemBase;
import de.keksuccino.fancyhud.customization.rendering.ItemRenderUtils;
import de.keksuccino.konkrete.math.MathUtils;
import de.keksuccino.konkrete.properties.PropertiesSection;
import net.minecraft.item.AirItem;
import net.minecraft.item.Item;

public class VisibilityRequirementContainer {

    //Visibility Requirements
    //VR show-if values are always the requirement that must be met to show the element.
    //So if the system should check for the main hand item and it's show-if value is set to FALSE, the element is visible if NO ITEM IS IN THE MAIN HAND.
    //---------
    public boolean vrCheckForActiveSlot = false;
    public boolean vrShowIfActiveSlot = false;
    public int vrActiveSlot = -1000;
    //---------
    public boolean vrCheckForItemInMainHand = false;
    public boolean vrShowIfItemInMainHand = false;
    //---------
    public boolean vrCheckForItemInOffHand = false;
    public boolean vrShowIfItemInOffHand = false;
    //---------
    public boolean vrCheckForActiveItemType = false;
    public boolean vrShowIfActiveItemType = false;
    public String vrActiveItemType = "weapon"; //weapon, tool, food, block, potion, misc
    //---------
    public boolean vrCheckForActiveItemName = false;
    public boolean vrShowIfActiveItemName = false;
    public String vrActiveItemName = "minecraft:stick";
    //---------
    public boolean vrCheckForSingleplayer = false;
    public boolean vrShowIfSingleplayer = false;
    //---------
    public boolean vrCheckForMultiplayer = false;
    public boolean vrShowIfMultiplayer = false;
    //---------
    public boolean vrCheckForPlayerOnGround = false;
    public boolean vrShowIfPlayerOnGround = false;
    //---------
    public boolean vrCheckForPlayerUnderwater = false;
    public boolean vrShowIfPlayerUnderwater = false;
    //---------
    public boolean vrCheckForPlayerIsRidingHorse = false;
    public boolean vrShowIfPlayerIsRidingHorse = false;
    //---------
    public boolean vrCheckForPlayerIsRidingEntity = false;
    public boolean vrShowIfPlayerIsRidingEntity = false;
    //---------
    public boolean vrCheckForPlayerIsInWater = false;
    public boolean vrShowIfPlayerIsInWater = false;
    //---------
    public boolean vrCheckForPlayerIsRunning = false;
    public boolean vrShowIfPlayerIsRunning = false;
    //---------
    public boolean vrCheckForSlotItemName = false;
    public boolean vrShowIfSlotItemName = false;
    public String vrSlotItemName = "minecraft:stick";
    public int vrSlotItemNameSlot = 0;
    //---------
    public boolean vrCheckForDebugOpen = false;
    public boolean vrShowIfDebugOpen = false;
    //---------
    public boolean vrCheckForGamePaused = false;
    public boolean vrShowIfGamePaused = false;
    //---------

    public CustomizationItemBase item;

    public VisibilityRequirementContainer(PropertiesSection properties, CustomizationItemBase item) {

        this.item = item;

        //VR: Active Slot
        String vrStringShowIfActiveSlot = properties.getEntryValue("vr:showif:activeslot");
        if (vrStringShowIfActiveSlot != null) {
            if (vrStringShowIfActiveSlot.equalsIgnoreCase("true")) {
                this.vrShowIfActiveSlot = true;
            }
            String activeSlot = properties.getEntryValue("vr:value:activeslot");
            if ((activeSlot != null) && MathUtils.isInteger(activeSlot)) {
                this.vrCheckForActiveSlot = true;
                this.vrActiveSlot = Integer.parseInt(activeSlot);
            }
        }

        //VR: Is Item In Main Hand
        String vrStringShowIfItemInMainHand = properties.getEntryValue("vr:showif:iteminmainhand");
        if (vrStringShowIfItemInMainHand != null) {
            this.vrCheckForItemInMainHand = true;
            if (vrStringShowIfItemInMainHand.equalsIgnoreCase("true")) {
                this.vrShowIfItemInMainHand = true;
            }
        }

        //VR: Is Item In Off Hand
        String vrStringShowIfItemInOffHand = properties.getEntryValue("vr:showif:iteminoffhand");
        if (vrStringShowIfItemInOffHand != null) {
            this.vrCheckForItemInOffHand = true;
            if (vrStringShowIfItemInOffHand.equalsIgnoreCase("true")) {
                this.vrShowIfItemInOffHand = true;
            }
        }

        //VR: Active Item Type
        String vrStringShowIfActiveItemType = properties.getEntryValue("vr:showif:activeitemtype");
        if (vrStringShowIfActiveItemType != null) {
            if (vrStringShowIfActiveItemType.equalsIgnoreCase("true")) {
                this.vrShowIfActiveItemType = true;
            }
            String itemType = properties.getEntryValue("vr:value:activeitemtype");
            if (itemType != null) {
                //TODO enum for item types
                if (itemType.equals("weapon") || itemType.equals("tool") || itemType.equals("food") || itemType.equals("block") || itemType.equals("potion")) {
                    this.vrCheckForActiveItemType = true;
                    this.vrActiveItemType = itemType;
                }
            }
        }

        //VR: Active Item Name
        String vrStringShowIfActiveItemName = properties.getEntryValue("vr:showif:activeitemname");
        if (vrStringShowIfActiveItemName != null) {
            if (vrStringShowIfActiveItemName.equalsIgnoreCase("true")) {
                this.vrShowIfActiveItemName = true;
            }
            String itemName = properties.getEntryValue("vr:value:activeitemname");
            if (itemName != null) {
                Item i = ItemRenderUtils.getItemByName(itemName);
                if ((i != null) && (itemName.equalsIgnoreCase("minecraft:air") || !(i instanceof AirItem))) {
                    this.vrCheckForActiveItemName = true;
                    this.vrActiveItemName = itemName;
                }
            }
        }

        //VR: Slot Item Name
        String vrStringShowIfSlotItemName = properties.getEntryValue("vr:showif:slotitemname");
        if (vrStringShowIfSlotItemName != null) {
            if (vrStringShowIfSlotItemName.equalsIgnoreCase("true")) {
                this.vrShowIfSlotItemName = true;
            }
            String slotAndItemName = properties.getEntryValue("vr:value:slotitemname");
            if ((slotAndItemName != null) && slotAndItemName.contains(":")) {
                String slotString = slotAndItemName.split("[:]", 2)[0];
                String itemNameString = slotAndItemName.split("[:]", 2)[1];
                if (MathUtils.isInteger(slotString)) {
                    Item i = ItemRenderUtils.getItemByName(itemNameString);
                    if ((i != null) && (itemNameString.equalsIgnoreCase("minecraft:air") || !(i instanceof AirItem))) {
                        this.vrCheckForSlotItemName = true;
                        this.vrSlotItemName = itemNameString;
                        this.vrSlotItemNameSlot = Integer.parseInt(slotString);
                    }
                }
            }
        }

        //VR: Is Singleplayer
        String vrStringShowIfSingleplayer = properties.getEntryValue("vr:showif:singleplayer");
        if (vrStringShowIfSingleplayer != null) {
            this.vrCheckForSingleplayer = true;
            if (vrStringShowIfSingleplayer.equalsIgnoreCase("true")) {
                this.vrShowIfSingleplayer = true;
            }
        }

        //VR: Is Multiplayer
        String vrStringShowIfMultiplayer = properties.getEntryValue("vr:showif:multiplayer");
        if (vrStringShowIfMultiplayer != null) {
            this.vrCheckForMultiplayer = true;
            if (vrStringShowIfMultiplayer.equalsIgnoreCase("true")) {
                this.vrShowIfMultiplayer = true;
            }
        }

        //VR: Player On Ground
        String vrStringShowIfPlayerOnGround = properties.getEntryValue("vr:showif:playeronground");
        if (vrStringShowIfPlayerOnGround != null) {
            this.vrCheckForPlayerOnGround = true;
            if (vrStringShowIfPlayerOnGround.equalsIgnoreCase("true")) {
                this.vrShowIfPlayerOnGround = true;
            }
        }

        //VR: Player Underwater
        String vrStringShowIfPlayerUnderwater = properties.getEntryValue("vr:showif:playerunderwater");
        if (vrStringShowIfPlayerUnderwater != null) {
            this.vrCheckForPlayerUnderwater = true;
            if (vrStringShowIfPlayerUnderwater.equalsIgnoreCase("true")) {
                this.vrShowIfPlayerUnderwater = true;
            }
        }

        //VR: Player Is Riding Horse
        String vrStringShowIfPlayerIsRidingHorse = properties.getEntryValue("vr:showif:playerisridinghorse");
        if (vrStringShowIfPlayerIsRidingHorse != null) {
            this.vrCheckForPlayerIsRidingHorse = true;
            if (vrStringShowIfPlayerIsRidingHorse.equalsIgnoreCase("true")) {
                this.vrShowIfPlayerIsRidingHorse = true;
            }
        }

        //VR: Player Is Riding Entity
        String vrStringShowIfPlayerIsRidingEntity = properties.getEntryValue("vr:showif:playerisridingentity");
        if (vrStringShowIfPlayerIsRidingEntity != null) {
            this.vrCheckForPlayerIsRidingEntity = true;
            if (vrStringShowIfPlayerIsRidingEntity.equalsIgnoreCase("true")) {
                this.vrShowIfPlayerIsRidingEntity = true;
            }
        }

        //VR: Player Is In Water
        String vrStringShowIfPlayerIsInWater = properties.getEntryValue("vr:showif:playerisinwater");
        if (vrStringShowIfPlayerIsInWater != null) {
            this.vrCheckForPlayerIsInWater = true;
            if (vrStringShowIfPlayerIsInWater.equalsIgnoreCase("true")) {
                this.vrShowIfPlayerIsInWater = true;
            }
        }

        //VR: Player Is Running
        String vrStringShowIfPlayerIsRunning = properties.getEntryValue("vr:showif:playerisrunning");
        if (vrStringShowIfPlayerIsRunning != null) {
            this.vrCheckForPlayerIsRunning = true;
            if (vrStringShowIfPlayerIsRunning.equalsIgnoreCase("true")) {
                this.vrShowIfPlayerIsRunning = true;
            }
        }

        //VR: Is Debug Open
        String vrStringShowIfDebugOpen = properties.getEntryValue("vr:showif:debugopen");
        if (vrStringShowIfDebugOpen != null) {
            this.vrCheckForDebugOpen = true;
            if (vrStringShowIfDebugOpen.equalsIgnoreCase("true")) {
                this.vrShowIfDebugOpen = true;
            }
        }

        //VR: Is Game Paused
        String vrStringShowIfGamePaused = properties.getEntryValue("vr:showif:gamepaused");
        if (vrStringShowIfGamePaused != null) {
            this.vrCheckForGamePaused = true;
            if (vrStringShowIfGamePaused.equalsIgnoreCase("true")) {
                this.vrShowIfGamePaused = true;
            }
        }

    }

    public boolean isVisible() {

        //VR: Active Slot
        if (this.vrCheckForActiveSlot) {
            if (this.vrShowIfActiveSlot) {
                if (this.vrActiveSlot != VisibilityRequirementHandler.activeSlot) {
                    return false;
                }
            } else {
                if (this.vrActiveSlot == VisibilityRequirementHandler.activeSlot) {
                    return false;
                }
            }
        }

        //VR: Is Item In Main Hand
        if (this.vrCheckForItemInMainHand) {
            boolean isItemInHand = VisibilityRequirementHandler.isItemInMainHand;
            if (this.vrShowIfItemInMainHand) {
                if (!isItemInHand) {
                    return false;
                }
            } else {
                if (isItemInHand) {
                    return false;
                }
            }
        }

        //VR: Is Item In Off Hand
        if (this.vrCheckForItemInOffHand) {
            boolean isItemInHand = VisibilityRequirementHandler.isItemInOffHand;
            if (this.vrShowIfItemInOffHand) {
                if (!isItemInHand) {
                    return false;
                }
            } else {
                if (isItemInHand) {
                    return false;
                }
            }
        }

        //VR: Active Item Type
        if (this.vrCheckForActiveItemType) {
            //TODO implementieren
        }

        //VR: Active Item Name
        if (this.vrCheckForActiveItemName) {
            String itemName = VisibilityRequirementHandler.activeItemName;
            if (itemName != null) {
                if (this.vrShowIfActiveItemName) {
                    if (!itemName.equalsIgnoreCase(this.vrActiveItemName)) {
                        return false;
                    }
                } else {
                    if (itemName.equalsIgnoreCase(this.vrActiveItemName)) {
                        return false;
                    }
                }
            }
        }

        //VR: Slot Item Name
        if (this.vrCheckForSlotItemName) {
            String itemName = VisibilityRequirementHandler.inventoryItemNames.get(this.vrSlotItemNameSlot);
            if (itemName != null) {
                if (this.vrShowIfSlotItemName) {
                    if (!itemName.equalsIgnoreCase(this.vrSlotItemName)) {
                        return false;
                    }
                } else {
                    if (itemName.equalsIgnoreCase(this.vrSlotItemName)) {
                        return false;
                    }
                }
            }
        }

        //VR: Is Singleplayer
        if (this.vrCheckForSingleplayer) {
            if (this.vrShowIfSingleplayer) {
                if (!VisibilityRequirementHandler.isSingleplayer) {
                    return false;
                }
            } else {
                if (VisibilityRequirementHandler.isSingleplayer) {
                    return false;
                }
            }
        }

        //VR: Is Multiplayer
        if (this.vrCheckForMultiplayer) {
            if (this.vrShowIfMultiplayer) {
                if (VisibilityRequirementHandler.isSingleplayer) {
                    return false;
                }
            } else {
                if (!VisibilityRequirementHandler.isSingleplayer) {
                    return false;
                }
            }
        }

        //VR: Player On Ground
        if (this.vrCheckForPlayerOnGround) {
            if (this.vrShowIfPlayerOnGround) {
                if (!VisibilityRequirementHandler.isPlayerOnGround) {
                    return false;
                }
            } else {
                if (VisibilityRequirementHandler.isPlayerOnGround) {
                    return false;
                }
            }
        }

        //VR: Player Underwater
        if (this.vrCheckForPlayerUnderwater) {
            if (this.vrShowIfPlayerUnderwater) {
                if (!VisibilityRequirementHandler.isPlayerUnderwater) {
                    return false;
                }
            } else {
                if (VisibilityRequirementHandler.isPlayerUnderwater) {
                    return false;
                }
            }
        }

        //VR: Player Is Riding Horse
        if (this.vrCheckForPlayerIsRidingHorse) {
            if (this.vrShowIfPlayerIsRidingHorse) {
                if (!VisibilityRequirementHandler.isPlayerRidingHorse) {
                    return false;
                }
            } else {
                if (VisibilityRequirementHandler.isPlayerRidingHorse) {
                    return false;
                }
            }
        }

        //VR: Player Is Riding Entity
        if (this.vrCheckForPlayerIsRidingEntity) {
            if (this.vrShowIfPlayerIsRidingEntity) {
                if (!VisibilityRequirementHandler.isPlayerRidingEntity) {
                    return false;
                }
            } else {
                if (VisibilityRequirementHandler.isPlayerRidingEntity) {
                    return false;
                }
            }
        }

        //VR: Player Is In Water
        if (this.vrCheckForPlayerIsInWater) {
            if (this.vrShowIfPlayerIsInWater) {
                if (!VisibilityRequirementHandler.isPlayerInWater) {
                    return false;
                }
            } else {
                if (VisibilityRequirementHandler.isPlayerInWater) {
                    return false;
                }
            }
        }

        //VR: Player Is Running
        if (this.vrCheckForPlayerIsRunning) {
            if (this.vrShowIfPlayerIsRunning) {
                if (!VisibilityRequirementHandler.isPlayerRunning) {
                    return false;
                }
            } else {
                if (VisibilityRequirementHandler.isPlayerRunning) {
                    return false;
                }
            }
        }

        //VR: Is Debug Open
        if (this.vrCheckForDebugOpen) {
            if (this.vrShowIfDebugOpen) {
                if (!VisibilityRequirementHandler.isDebugOpen) {
                    return false;
                }
            } else {
                if (VisibilityRequirementHandler.isDebugOpen) {
                    return false;
                }
            }
        }

        //VR: Is Game Paused
        if (this.vrCheckForGamePaused) {
            if (this.vrShowIfGamePaused) {
                if (!VisibilityRequirementHandler.isGamePaused) {
                    return false;
                }
            } else {
                if (VisibilityRequirementHandler.isGamePaused) {
                    return false;
                }
            }
        }

        return true;

    }

}
