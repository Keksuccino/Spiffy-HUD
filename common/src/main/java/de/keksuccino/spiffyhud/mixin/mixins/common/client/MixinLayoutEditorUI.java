package de.keksuccino.spiffyhud.mixin.mixins.common.client;

import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorUI;
import de.keksuccino.fancymenu.util.rendering.ui.contextmenu.v2.ContextMenu;
import de.keksuccino.spiffyhud.customization.SpiffyOverlayScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LayoutEditorUI.class)
public class MixinLayoutEditorUI {

    //Disable scaling options for SpiffyOverlayScreen, so it's not possible to apply custom scaling to the HUD (to not break stuff, just in case)
    @Inject(method = "buildRightClickContextMenu", at = @At("RETURN"), remap = false)
    private static void onReturnBuildRightClickContextMenu_Spiffy(LayoutEditorScreen editor, CallbackInfoReturnable<ContextMenu> info) {

        ContextMenu menu = info.getReturnValue();
        if (menu == null) return;

        ContextMenu.ContextMenuEntry<?> forcedGuiScaleEntry = menu.getEntry("forced_gui_scale");
        if (forcedGuiScaleEntry != null) {
            forcedGuiScaleEntry.addIsActiveSupplier((menu1, entry) -> !(editor.layoutTargetScreen instanceof SpiffyOverlayScreen));
        }

    }

}
