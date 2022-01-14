package de.keksuccino.spiffyhud.customization.items.visibilityrequirements;

import de.keksuccino.konkrete.Konkrete;
import de.keksuccino.konkrete.events.SubscribeEvent;
import de.keksuccino.spiffyhud.events.hud.RenderHudEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.AirBlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameMode;

import java.util.Calendar;
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
    public static boolean isRaining = false;
    public static boolean isThundering = false;
    public static float playerHealth = 100;
    public static float playerHealthPercent = 100;
    public static int playerFood = 100;
    public static float playerFoodPercent = 100;
    public static boolean isPlayerWithered = false;
    public static boolean isCreative = false;
    public static boolean isSurvival = false;
    public static boolean isAdventure = false;
    public static boolean isSpectator = false;
    public static boolean isPlayerPoisoned = false;
    public static boolean hasPlayerBadStomach = false;
    public static int worldTimeHour = 12;
    public static int worldTimeMinute = 0;
    public static int realTimeHour = 12;
    public static int realTimeMinute = 0;
    public static int realTimeSecond = 0;

    public static void init() {
        Konkrete.getEventHandler().registerEventsFrom(new VisibilityRequirementHandler());
    }

    public static void tick() {
        ClientPlayerEntity p = MinecraftClient.getInstance().player;

        //Caching item names
        inventoryItemNames.clear();
        if (p != null) {
            PlayerInventory inv = p.getInventory();
            if (inv != null) {
                int slot = 0;
                for (ItemStack i : inv.main) {
                    if ((i != ItemStack.EMPTY) && !(i.getItem() instanceof AirBlockItem)) {
                        inventoryItemNames.put(slot, getItemName(i));
                    } else {
                        inventoryItemNames.put(slot, "minecraft:air");
                    }
                    slot++;
                }
                for (ItemStack i : inv.armor) {
                    if ((i != ItemStack.EMPTY) && !(i.getItem() instanceof AirBlockItem)) {
                        inventoryItemNames.put(slot, getItemName(i));
                    } else {
                        inventoryItemNames.put(slot, "minecraft:air");
                    }
                    slot++;
                }
                for (ItemStack i : inv.offHand) {
                    if ((i != ItemStack.EMPTY) && !(i.getItem() instanceof AirBlockItem)) {
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
            if (p.getInventory() != null) {
                activeSlot = p.getInventory().selectedSlot;
            }
        }

        //VR: Is Item In Main Hand
        if (p != null) {
            if (p.getInventory() != null) {
                ItemStack i = p.getMainHandStack();
                isItemInMainHand = (i != ItemStack.EMPTY) && !(i.getItem() instanceof AirBlockItem);
            }
        }

        //VR: Is Item In Off Hand
        if (p != null) {
            if (p.getInventory() != null) {
                ItemStack i = p.getOffHandStack();
                isItemInOffHand = (i != ItemStack.EMPTY) && !(i.getItem() instanceof AirBlockItem);
            }
        }

        //VR: Active Item Name
        if (p != null) {
            activeItemName = inventoryItemNames.get(p.getInventory().selectedSlot);
        }

        //VR: Is Singleplayer & Is Multiplayer
        isSingleplayer = MinecraftClient.getInstance().isIntegratedServerRunning();

        //VR: Player On Ground
        if (p != null) {
            isPlayerOnGround = p.isOnGround();
        }

        //VR: Player Underwater
        if (p != null) {
            isPlayerUnderwater = p.isSubmergedIn(FluidTags.WATER);
        }

        //VR: Player Is Riding Horse
        if (p != null) {
            isPlayerRidingHorse = p.hasJumpingMount();
        }

        //VR: Player Is Riding Entity
        if (p != null) {
            isPlayerRidingEntity = ((p.getVehicle() != null) && (p.getVehicle() instanceof LivingEntity));
        }

        //VR: Player Is In Water
        if (p != null) {
            isPlayerInWater = p.isTouchingWater();
        }

        //VR: Player Is Running
        if (p != null) {
            isPlayerRunning = p.isSprinting();
        }

        //VR: Is Debug Open
        isDebugOpen = MinecraftClient.getInstance().options.debugEnabled;

        //VR: Is Game Paused
        isGamePaused = MinecraftClient.getInstance().isPaused();

        if (MinecraftClient.getInstance().world != null) {

            isRaining = MinecraftClient.getInstance().world.isRaining();

            isThundering = MinecraftClient.getInstance().world.isThundering();

        }

        if (p != null) {

            playerHealth = p.getHealth();

            playerHealthPercent = (p.getHealth() / p.getMaxHealth()) * 100.0F;

            playerFood = p.getHungerManager().getFoodLevel();

            playerFoodPercent = ((float)p.getHungerManager().getFoodLevel() / 20.0F) * 100.0F;

            isPlayerWithered = p.hasStatusEffect(StatusEffects.WITHER);

            isSurvival = (MinecraftClient.getInstance().interactionManager.getCurrentGameMode() == GameMode.SURVIVAL);

            isCreative = (MinecraftClient.getInstance().interactionManager.getCurrentGameMode() == GameMode.CREATIVE);

            isAdventure = (MinecraftClient.getInstance().interactionManager.getCurrentGameMode() == GameMode.ADVENTURE);

            isSpectator = (MinecraftClient.getInstance().interactionManager.getCurrentGameMode() == GameMode.SPECTATOR);

            isPlayerPoisoned = p.hasStatusEffect(StatusEffects.POISON);

            hasPlayerBadStomach = p.hasStatusEffect(StatusEffects.HUNGER);

        }

        worldTimeHour = getWorldTimeHour();
        worldTimeMinute = getWorldTimeMinute();

        Calendar c = Calendar.getInstance();
        if (c != null) {
            realTimeHour = c.get(Calendar.HOUR_OF_DAY);
            realTimeMinute = c.get(Calendar.MINUTE);
            realTimeSecond = c.get(Calendar.SECOND);
        }

    }

    @SubscribeEvent
    public void onRenderGameOverlayPre(RenderHudEvent.Pre e) {
        tick();
    }

    //TODO experimental
    protected static String getItemName(ItemStack i) {
        Identifier key = Registry.ITEM.getId(i.getItem());
        if (key != null) {
            return key.getNamespace() + ":" + key.getPath();
        }
        return "minecraft:air";
    }

    private static long getDayTime() {
        ClientWorld w = MinecraftClient.getInstance().world;
        if (w != null) {
            return w.getTimeOfDay();
        }
        return 1L;
    }

    private static int getWorldTimeHour() {
        long h = 0;
        long dt = getDayTime();
        while (dt >= 24000) {
            dt -= 24000;
        }
        if (dt < 18000) {
            h = (dt / 1000) + 6;
        } else {
            h = (dt / 1000) - 18;
        }
        return (int)h;
    }

    private static int getWorldTimeMinute() {
        long min = 0;
        long i = getDayTime() / 1000;
        long i2 = getDayTime() - (i * 1000);
        if (i2 <= 0) {
            return (int)min;
        }
        min = (long)((float)i2 / 16.6F);
        if (min > 59) {
            min = 0;
        }
        return (int)min;
    }

}
