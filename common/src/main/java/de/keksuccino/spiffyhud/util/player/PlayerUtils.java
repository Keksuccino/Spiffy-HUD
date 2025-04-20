package de.keksuccino.spiffyhud.util.player;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;

public class PlayerUtils {

    public static float getItemUseProgress(Player player) {

        // If there's no player or the player isn’t using an item, return 0 progress.
        if (player == null || !player.isUsingItem()) {
            return 0.0F;
        }

        // Get the item stack that is currently being used.
        ItemStack stack = player.getUseItem();
        if (stack.isEmpty()) {
            return 0.0F;
        }

        // Get the total duration of the use action (e.g., eating, drawing a bow).
        int totalDuration = stack.getUseDuration(player);
        // Get how many ticks remain until the use is complete.
        int remainingTicks = player.getUseItemRemainingTicks();

        // Avoid division by zero (shouldn't happen if use duration is set properly).
        if (totalDuration <= 0) {
            return 0.0F;
        }

        // Compute linear progress: 0.0 when just started, 1.0 when finished.
        float progress = (totalDuration - remainingTicks) / (float) totalDuration;

        // For items like bows, vanilla applies a nonlinear scaling so that the pull feels right.
        if (stack.getItem() instanceof BowItem) {
            progress = (progress * progress + progress * 2.0F) / 3.0F;
            if (progress > 1.0F) {
                progress = 1.0F;
            }
        }

        return progress;

    }

}
