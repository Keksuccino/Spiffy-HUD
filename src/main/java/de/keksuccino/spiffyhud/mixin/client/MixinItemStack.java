package de.keksuccino.spiffyhud.mixin.client;

import de.keksuccino.konkrete.Konkrete;
import de.keksuccino.spiffyhud.events.world.PlayerItemPlacedEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class MixinItemStack {

    ItemStack stack = (ItemStack)((Object)this);

    @Inject(at = @At("RETURN"), method = "useOnBlock", cancellable = true)
    private void onUseOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> info) {

        if (context.getWorld().isClient()) {
            PlayerItemPlacedEvent e = new PlayerItemPlacedEvent(stack, context, context.getBlockPos(), context.getPlayer(), info.getReturnValue());
            Konkrete.getEventHandler().callEventsFor(e);
        }

    }

}
