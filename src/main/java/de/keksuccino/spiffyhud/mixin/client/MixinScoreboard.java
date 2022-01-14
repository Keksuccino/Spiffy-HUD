package de.keksuccino.spiffyhud.mixin.client;

import de.keksuccino.spiffyhud.SpiffyHud;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.LiteralText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Scoreboard.class)
public class MixinScoreboard {

    //Force-display sidebar in layout editor, even if no sidebar exists
    @Inject(at = @At("RETURN"), method = "getObjectiveForSlot", cancellable = true)
    private void onGetDisplayObjective(int i, CallbackInfoReturnable<ScoreboardObjective> info) {

        if (i == 1) {
            ScoreboardObjective o = info.getReturnValue();
            if (o == null) {
                if (SpiffyHud.getIngameHud().getGui().isEditor()) {
                    Scoreboard s = (Scoreboard) ((Object)this);
                    info.setReturnValue(new ScoreboardObjective(s, "dummysidebar", ScoreboardCriterion.DUMMY, new LiteralText("dummytext"), ScoreboardCriterion.RenderType.INTEGER));
                }
            }
        }

    }

}
