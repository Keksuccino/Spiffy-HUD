package de.keksuccino.spiffyhud.mixin.client;

import de.keksuccino.spiffyhud.SpiffyHud;
import net.minecraft.scoreboard.ScoreCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.text.StringTextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Scoreboard.class)
public class MixinScoreboard {

    //Force-display sidebar in layout editor, even if no sidebar exists
    @Inject(at = @At("RETURN"), method = "getObjectiveInDisplaySlot", cancellable = true)
    private void onGetDisplayObjective(int i, CallbackInfoReturnable<ScoreObjective> info) {

        if (i == 1) {
            ScoreObjective o = info.getReturnValue();
            if (o == null) {
                if (SpiffyHud.getIngameHud().getGui().isEditor()) {
                    Scoreboard s = (Scoreboard) ((Object)this);
                    info.setReturnValue(new ScoreObjective(s, "dummysidebar", ScoreCriteria.DUMMY, new StringTextComponent("dummytext"), ScoreCriteria.RenderType.INTEGER));
                }
            }
        }

    }

}
