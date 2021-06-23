package de.keksuccino.fancyhud.customization.items.visibilityrequirements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.AirItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class VisibilityRequirementHandler {

    public static int activeSlot = -1000;
    public static boolean isItemInMainHand = false;
    public static boolean isItemInOffHand = false;
    public static String activeItemType = "weapon"; //weapon, tool, food, block, potion, misc
    public static String activeItemName = null;
    public static boolean isSingleplayer = false;
    public static boolean isPlayerOnGround = false;
    public static boolean isPlayerUnderwater = false;
    public static boolean isPlayerRidingHorse = false;
    public static boolean isPlayerRidingEntity = false;
    public static boolean isPlayerInWater = false;
    public static boolean isPlayerRunning = false;
    public static boolean isDebugOpen = false;
    public static boolean isGamePaused = false;
    public static Map<Integer, String> inventoryItemNames = new HashMap<Integer, String>();

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new VisibilityRequirementHandler());
    }

    public static void tick() {
        ClientPlayerEntity p = Minecraft.getInstance().player;

        //Caching item names
        inventoryItemNames.clear();
        if (p != null) {
            PlayerInventory inv = p.inventory;
            if (inv != null) {
                int slot = 0;
                for (ItemStack i : inv.mainInventory) {
                    if ((i != ItemStack.EMPTY) && !(i.getItem() instanceof AirItem)) {
                        inventoryItemNames.put(slot, getItemName(i));
                    } else {
                        inventoryItemNames.put(slot, "minecraft:air");
                    }
                    slot++;
                }
                for (ItemStack i : inv.armorInventory) {
                    if ((i != ItemStack.EMPTY) && !(i.getItem() instanceof AirItem)) {
                        inventoryItemNames.put(slot, getItemName(i));
                    } else {
                        inventoryItemNames.put(slot, "minecraft:air");
                    }
                    slot++;
                }
                for (ItemStack i : inv.offHandInventory) {
                    if ((i != ItemStack.EMPTY) && !(i.getItem() instanceof AirItem)) {
                        inventoryItemNames.put(slot, getItemName(i));
                    } else {
                        inventoryItemNames.put(slot, "minecraft:air");
                    }
                    slot++;
                }
            }
        }

        //VR: Active Slot
        if (p != null) {
            if (p.inventory != null) {
                activeSlot = p.inventory.currentItem;
            }
        }

        //VR: Is Item In Main Hand
        if (p != null) {
            if (p.inventory != null) {
                ItemStack i = p.getHeldItemMainhand();
                isItemInMainHand = (i != ItemStack.EMPTY) && !(i.getItem() instanceof AirItem);
            }
        }

        //VR: Is Item In Off Hand
        if (p != null) {
            if (p.inventory != null) {
                ItemStack i = p.getHeldItemOffhand();
                isItemInOffHand = (i != ItemStack.EMPTY) && !(i.getItem() instanceof AirItem);
            }
        }

        //VR: Active Item Type
        //TODO implementieren

        //VR: Active Item Name
        if (p != null) {
//            if (p.inventory != null) {
//                ItemStack i = p.getHeldItemMainhand();
//                if (i != ItemStack.EMPTY) {
//                    ResourceLocation key = ForgeRegistries.ITEMS.getKey(i.getItem());
//                    if (key != null) {
//                        activeItemName = key.getNamespace() + ":" + key.getPath();
//                    } else {
//                        activeItemName = null;
//                    }
//                } else {
//                    activeItemName = null;
//                }
//            }
            activeItemName = inventoryItemNames.get(p.inventory.currentItem);
        }

        //VR: Is Singleplayer & Is Multiplayer
        isSingleplayer = Minecraft.getInstance().isSingleplayer();

        //VR: Player On Ground
        if (p != null) {
            isPlayerOnGround = p.isOnGround();
        }

        //VR: Player Underwater
        if (p != null) {
            isPlayerUnderwater = p.areEyesInFluid(FluidTags.WATER);
        }

        //VR: Player Is Riding Horse
        if (p != null) {
            isPlayerRidingHorse = p.isRidingHorse();
        }

        //VR: Player Is Riding Entity
        if (p != null) {
            isPlayerRidingEntity = ((p.getRidingEntity() != null) && (p.getRidingEntity() instanceof LivingEntity));
        }

        //VR: Player Is In Water
        if (p != null) {
            isPlayerInWater = p.isInWater();
        }

        //VR: Player Is Running
        if (p != null) {
            isPlayerRunning = p.isSprinting();
        }

        //VR: Is Debug Open
        isDebugOpen = Minecraft.getInstance().gameSettings.showDebugInfo;

        //VR: Is Game Paused
        isGamePaused = Minecraft.getInstance().isGamePaused();

    }

    @SubscribeEvent
    public void onRenderGameOverlayPre(RenderGameOverlayEvent.Pre e) {
        if (e.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            tick();
        }
    }

    protected static String getItemName(ItemStack i) {
        ResourceLocation key = ForgeRegistries.ITEMS.getKey(i.getItem());
        if (key != null) {
            return key.getNamespace() + ":" + key.getPath();
        }
        return "minecraft:air";
    }

}
