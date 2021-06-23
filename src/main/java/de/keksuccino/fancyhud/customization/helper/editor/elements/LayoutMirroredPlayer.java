package de.keksuccino.fancyhud.customization.helper.editor.elements;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import de.keksuccino.fancyhud.customization.helper.editor.LayoutEditorScreen;
import de.keksuccino.fancyhud.customization.helper.ui.popup.FHTextInputPopup;
import de.keksuccino.fancyhud.customization.items.MirroredPlayerCustomizationItem;
import de.keksuccino.konkrete.gui.content.AdvancedButton;
import de.keksuccino.konkrete.gui.screens.popup.PopupHandler;
import de.keksuccino.konkrete.input.CharacterFilter;
import de.keksuccino.konkrete.input.StringUtils;
import de.keksuccino.konkrete.localization.Locals;
import de.keksuccino.konkrete.math.MathUtils;
import de.keksuccino.konkrete.properties.PropertiesSection;

public class LayoutMirroredPlayer extends LayoutElement {

    public LayoutMirroredPlayer(MirroredPlayerCustomizationItem object, LayoutEditorScreen handler) {
        super(object, true, handler);
    }

    @Override
    public void init() {
        this.stretchable = false;
        this.resizable = false;
        super.init();

        this.rightclickMenu.setAutoclose(true);

        AdvancedButton scaleButton = new AdvancedButton(0, 0, 0, 16, Locals.localize("fancyhud.helper.editor.elements.scale"), true, (press) -> {
            FHTextInputPopup pop = new FHTextInputPopup(new Color(0, 0, 0, 0), Locals.localize("fancyhud.helper.editor.elements.scale"), CharacterFilter.getDoubleCharacterFiler(), 240, (call) -> {
                if (call != null) {
                    if (call.replace(" ", "").equals("")) {
                        if (this.getObject().scale != 1.0F) {
                            this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
                        }
                        this.getObject().scale = 1.0F;
                    } else {
                        if (MathUtils.isFloat(call)) {
                            float f = Float.parseFloat(call);
                            if (this.getObject().scale != f) {
                                this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
                            }
                            this.getObject().scale = f;
                        }
                    }
                }
            });
            pop.setText("" + this.getObject().scale);
            PopupHandler.displayPopup(pop);
        });
        scaleButton.setDescription(StringUtils.splitLines(Locals.localize("fancyhud.helper.editor.elements.scale.btn.desc"), "%n%"));
        this.rightclickMenu.addContent(scaleButton);

        String rotatePlayerLabel = Locals.localize("fancyhud.helper.creator.items.mirroredplayer.rotateplayer.on");
        if (!this.getObject().rotatePlayer) {
            rotatePlayerLabel = Locals.localize("fancyhud.helper.creator.items.mirroredplayer.rotateplayer.off");
        }
        AdvancedButton rotatePlayerButton = new AdvancedButton(0, 0, 0, 16, rotatePlayerLabel, true, (press) -> {
            if (this.getObject().rotatePlayer) {
                ((AdvancedButton)press).setMessage(Locals.localize("fancyhud.helper.creator.items.mirroredplayer.rotateplayer.off"));
                this.getObject().rotatePlayer = false;
            } else {
                ((AdvancedButton)press).setMessage(Locals.localize("fancyhud.helper.creator.items.mirroredplayer.rotateplayer.on"));
                this.getObject().rotatePlayer = true;
            }
        });
        this.rightclickMenu.addContent(rotatePlayerButton);

    }

    @Override
    public List<PropertiesSection> getProperties() {
        List<PropertiesSection> l = new ArrayList<PropertiesSection>();
        PropertiesSection s = new PropertiesSection("customization");

        s.addEntry("action", "addmirroredplayer");
        s.addEntry("actionid", this.object.getActionId());

        s.addEntry("orientation", this.object.orientation);
        s.addEntry("x", "" + this.object.posX);
        s.addEntry("y", "" + this.object.posY);

        s.addEntry("scale", "" + this.getObject().scale);
        s.addEntry("rotateplayer", "" + this.getObject().rotatePlayer);

        this.addVisibilityPropertiesTo(s);

        l.add(s);

        return l;
    }

    protected MirroredPlayerCustomizationItem getObject() {
        return (MirroredPlayerCustomizationItem) this.object;
    }

}
