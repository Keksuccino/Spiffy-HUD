package de.keksuccino.spiffyhud.events.world;

import de.keksuccino.konkrete.events.EventBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

public class PlayerItemPlacedEvent extends EventBase {

    public ItemStack itemStack;
    public ItemUsageContext context;
    public BlockPos pos;
    public PlayerEntity player;
    public ActionResult result;

    public PlayerItemPlacedEvent(ItemStack stack, ItemUsageContext context, BlockPos pos, PlayerEntity player, ActionResult result) {
        this.itemStack = stack;
        this.context = context;
        this.pos = pos;
        this.player = player;
        this.result = result;
    }

    @Override
    public boolean isCancelable() {
        return false;
    }

}
