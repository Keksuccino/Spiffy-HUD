package de.keksuccino.spiffyhud.mixin.client;

import de.keksuccino.spiffyhud.SpiffyHud;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Scoreboard.class)
public class MixinScoreboard {

    //Force-display sidebar in layout editor, even if no sidebar exists
    @Inject(at = @At("RETURN"), method = "getDisplayObjective", cancellable = true)
    private void onGetDisplayObjective(int i, CallbackInfoReturnable<Objective> info) {

        if (i == 1) {
            Objective o = info.getReturnValue();
            if (o == null) {
                if (SpiffyHud.getIngameHud().getGui().isEditor()) {
                    Scoreboard s = (Scoreboard) ((Object)this);
                    info.setReturnValue(new Objective(s, "dummysidebar", ObjectiveCriteria.DUMMY, new TextComponent("dummytext"), ObjectiveCriteria.RenderType.INTEGER));
                }
            }
        }

    }

}
