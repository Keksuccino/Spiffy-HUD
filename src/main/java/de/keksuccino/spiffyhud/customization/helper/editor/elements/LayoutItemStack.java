package de.keksuccino.spiffyhud.customization.helper.editor.elements;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import de.keksuccino.spiffyhud.customization.helper.editor.LayoutEditorScreen;
import de.keksuccino.spiffyhud.customization.helper.ui.content.FHContextMenu;
import de.keksuccino.spiffyhud.customization.helper.ui.popup.FHTextInputPopup;
import de.keksuccino.spiffyhud.customization.items.ItemStackCustomizationItem;
import de.keksuccino.konkrete.gui.content.AdvancedButton;
import de.keksuccino.konkrete.gui.screens.popup.PopupHandler;
import de.keksuccino.konkrete.input.CharacterFilter;
import de.keksuccino.konkrete.input.StringUtils;
import de.keksuccino.konkrete.localization.Locals;
import de.keksuccino.konkrete.math.MathUtils;
import de.keksuccino.konkrete.properties.PropertiesSection;

public class LayoutItemStack extends LayoutElement {

    public LayoutItemStack(ItemStackCustomizationItem object, LayoutEditorScreen handler) {
        super(object, true, handler);
    }

    @Override
    public void init() {
        this.stretchable = false;
        this.resizable = false;
        super.init();

        this.rightclickMenu.setAutoclose(true);

        AdvancedButton scaleButton = new AdvancedButton(0, 0, 0, 16, Locals.localize("spiffyhud.helper.editor.elements.scale"), true, (press) -> {
            FHTextInputPopup pop = new FHTextInputPopup(new Color(0, 0, 0, 0), Locals.localize("spiffyhud.helper.editor.elements.scale"), CharacterFilter.getDoubleCharacterFiler(), 240, (call) -> {
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
        scaleButton.setDescription(StringUtils.splitLines(Locals.localize("spiffyhud.helper.editor.elements.scale.btn.desc"), "%n%"));
        this.rightclickMenu.addContent(scaleButton);

        FHContextMenu setItemMenu = new FHContextMenu();
        this.rightclickMenu.addChild(setItemMenu);

        AdvancedButton setBySlotButton = new AdvancedButton(0, 0, 0, 16, Locals.localize("spiffyhud.helper.creator.items.itemstack.setitem.byslot"), true, (press) -> {
            FHTextInputPopup pop = new FHTextInputPopup(new Color(0, 0, 0, 0), Locals.localize("spiffyhud.helper.creator.items.itemstack.setitem.byslot"), CharacterFilter.getIntegerCharacterFiler(), 240, (call) -> {
                if (call != null) {
                    if (call.replace(" ", "").equals("")) {
                        if (this.getObject().slot != -1000) {
                            this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
                        }
                        this.getObject().slot = -1000;
                        if (this.getObject().itemId == null) {
                            this.getObject().itemId = "minecraft:stick";
                        }
                    } else {
                        if (MathUtils.isInteger(call)) {
                            int i = Integer.parseInt(call);
                            if (this.getObject().slot != i) {
                                this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
                            }
                            if (i < -1000) {
                                i = -1000;
                                this.getObject().forceItemUpdate();
                                this.getObject().itemId = "minecraft:stick";
                            } else {
                                this.getObject().forceItemUpdate();
                                this.getObject().itemId = null;
                            }
                            this.getObject().slot = i;
                        }
                    }
                }
            });
            pop.setText("" + this.getObject().slot);
            PopupHandler.displayPopup(pop);
        });
        setBySlotButton.setDescription(StringUtils.splitLines(Locals.localize("spiffyhud.helper.creator.items.itemstack.setitem.byslot.btn.desc"), "%n%"));
        setItemMenu.addContent(setBySlotButton);

        AdvancedButton setByIdButton = new AdvancedButton(0, 0, 0, 16, Locals.localize("spiffyhud.helper.creator.items.itemstack.setitem.byid"), true, (press) -> {
            FHTextInputPopup pop = new FHTextInputPopup(new Color(0, 0, 0, 0), Locals.localize("spiffyhud.helper.creator.items.itemstack.setitem.byid"), null, 240, (call) -> {
                if (call != null) {
                    if (call.replace(" ", "").equals("")) {
                        if (this.getObject().itemId != null) {
                            this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
                        }
                        this.getObject().slot = -1000;
                        this.getObject().forceItemUpdate();
                        this.getObject().itemId = "minecraft:stick";
                    } else {
                        if ((this.getObject().itemId == null) || !this.getObject().itemId.equals(call)) {
                            this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
                        }
                        this.getObject().forceItemUpdate();
                        this.getObject().slot = -1000;
                        this.getObject().itemId = call;
                    }
                }
            });
           if (this.getObject().itemId != null) {
               pop.setText(this.getObject().itemId);
           }
            PopupHandler.displayPopup(pop);
        });
        setByIdButton.setDescription(StringUtils.splitLines(Locals.localize("spiffyhud.helper.creator.items.itemstack.setitem.byid.btn.desc"), "%n%"));
        setItemMenu.addContent(setByIdButton);

        AdvancedButton setItemButton = new AdvancedButton(0, 0, 0, 16, Locals.localize("spiffyhud.helper.creator.items.itemstack.setitem"), true, (press) -> {
            if (!setItemMenu.isOpen()) {
                setItemMenu.setParentButton((AdvancedButton) press);
                setItemMenu.openMenuAt(0, press.y);
            } else {
                setItemMenu.closeMenu();
            }
        });
        setItemButton.setDescription(StringUtils.splitLines(Locals.localize("spiffyhud.helper.creator.items.itemstack.setitem.btn.desc"), "%n%"));
        this.rightclickMenu.addContent(setItemButton);

        String stackSizeLabel = Locals.localize("spiffyhud.helper.creator.items.itemstack.showstacksize.on");
        if (!this.getObject().renderStackSize) {
            stackSizeLabel = Locals.localize("spiffyhud.helper.creator.items.itemstack.showstacksize.off");
        }
        AdvancedButton showStackSizeButton = new AdvancedButton(0, 0, 0, 16, stackSizeLabel, true, (press) -> {
            if (this.getObject().renderStackSize) {
                ((AdvancedButton)press).setMessage(Locals.localize("spiffyhud.helper.creator.items.itemstack.showstacksize.off"));
                this.getObject().renderStackSize = false;
            } else {
                ((AdvancedButton)press).setMessage(Locals.localize("spiffyhud.helper.creator.items.itemstack.showstacksize.on"));
                this.getObject().renderStackSize = true;
            }
        });
        this.rightclickMenu.addContent(showStackSizeButton);

        String durabLabel = Locals.localize("spiffyhud.helper.creator.items.itemstack.showdurability.on");
        if (!this.getObject().renderDurability) {
            durabLabel = Locals.localize("spiffyhud.helper.creator.items.itemstack.showdurability.off");
        }
        AdvancedButton showDurabilityButton = new AdvancedButton(0, 0, 0, 16, durabLabel, true, (press) -> {
            if (this.getObject().renderDurability) {
                ((AdvancedButton)press).setMessage(Locals.localize("spiffyhud.helper.creator.items.itemstack.showdurability.off"));
                this.getObject().renderDurability = false;
            } else {
                ((AdvancedButton)press).setMessage(Locals.localize("spiffyhud.helper.creator.items.itemstack.showdurability.on"));
                this.getObject().renderDurability = true;
            }
        });
        this.rightclickMenu.addContent(showDurabilityButton);

    }

    @Override
    public List<PropertiesSection> getProperties() {
        List<PropertiesSection> l = new ArrayList<PropertiesSection>();
        PropertiesSection s = new PropertiesSection("customization");

        s.addEntry("action", "additemstack");
        s.addEntry("actionid", this.object.getActionId());

        s.addEntry("orientation", this.object.orientation);
        s.addEntry("x", "" + this.object.posX);
        s.addEntry("y", "" + this.object.posY);

        if (this.getObject().itemId == null) {
            s.addEntry("slot", "" + this.getObject().slot);
        } else {
            s.addEntry("itemid", this.getObject().itemId);
        }
        s.addEntry("scale", "" + this.getObject().scale);
        s.addEntry("renderstacksize", "" + this.getObject().renderStackSize);
        s.addEntry("renderdurability", "" + this.getObject().renderDurability);

        this.addVisibilityPropertiesTo(s);

        l.add(s);

        return l;
    }

    protected ItemStackCustomizationItem getObject() {
        return (ItemStackCustomizationItem) this.object;
    }

}
