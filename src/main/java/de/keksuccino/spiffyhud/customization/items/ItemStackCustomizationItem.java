package de.keksuccino.spiffyhud.customization.items;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.konkrete.rendering.RenderUtils;
import de.keksuccino.spiffyhud.customization.rendering.ItemRenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.AirBlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import de.keksuccino.konkrete.math.MathUtils;
import de.keksuccino.konkrete.properties.PropertiesSection;

public class ItemStackCustomizationItem extends CustomizationItemBase {

    protected static final Identifier SLOT_PLACEHOLDER = new Identifier("spiffyhud", "helper/slot_placeholder.png");
    protected static final Identifier MISSING_ITEM = new Identifier("spiffyhud", "helper/missing_element.png");

    public int slot = -1000;
    public String itemId = "minecraft:stick";
    public float scale = 1.0F;
    public boolean renderStackSize = true;
    public boolean renderDurability = true;

    public ItemStack item = null;

    //additemstack
    public ItemStackCustomizationItem(PropertiesSection item) {

        super(item);

        this.value = "Item Stack";

        String s = item.getEntryValue("slot");
        if ((s != null) && MathUtils.isInteger(s)) {
            this.slot = Integer.parseInt(s);
        }

        if (this.slot == -1000) {
            String s2 = item.getEntryValue("itemid");
            if (s2 != null) {
                this.itemId = s2;
            }
        } else {
            this.itemId = null;
        }

        String s3 = item.getEntryValue("scale");
        if ((s3 != null) && MathUtils.isFloat(s3)) {
            this.scale = Float.parseFloat(s3);
        }

        String s4 = item.getEntryValue("renderstacksize");
        if ((s4 != null) && s4.equalsIgnoreCase("false")) {
            this.renderStackSize = false;
        }

        String s5 = item.getEntryValue("renderdurability");
        if ((s5 != null) && s5.equalsIgnoreCase("false")) {
            this.renderDurability = false;
        }

    }

    @Override
    public void render(MatrixStack matrix) {

        if (!this.shouldRender()) {
            return;
        }

        this.width = (int) (20 * this.scale);
        this.height = (int) (20 * this.scale);

        if (this.slot != -1000) {
            this.value = "Item Stack: Slot " + this.slot;
        } else if (this.item != null) {
            this.value = "Item Stack: " + this.item.getName().getString();
        }

        if (this.shouldRender()) {
            this.updateItem();
            if (!isEditorActive()) {
                if (this.item != null) {
                    ItemRenderUtils.renderGuiItem(this.getPosX() + 2, this.getPosY() + 2, MinecraftClient.getInstance().getTickDelta(), this.item, this.scale, this.renderStackSize, this.renderDurability);
                }
            } else {
                if ((this.itemId != null) && ((this.item == null) || (this.item.getItem() instanceof AirBlockItem))) {
                    RenderSystem.enableBlend();
                    RenderUtils.bindTexture(MISSING_ITEM);
                    drawTexture(matrix, this.getPosX(), this.getPosY(), 0.0F, 0.0F, this.width, this.height, this.width, this.height);
                } else if (this.slot != -1000) {
                    RenderSystem.enableBlend();
                    RenderUtils.bindTexture(SLOT_PLACEHOLDER);
                    drawTexture(matrix, this.getPosX(), this.getPosY(), 0.0F, 0.0F, this.width, this.height, this.width, this.height);
                    matrix.push();
                    matrix.scale(this.scale, this.scale, this.scale);
                    int scaledX = (int) ((this.getPosX() + (this.width / 2)) / this.scale);
                    int scaledY = (int) (this.getPosY() / this.scale);
                    int scaledY2 = (int) ((this.getPosY() + this.height - (10 * this.scale)) / this.scale);
                    drawCenteredText(matrix, MinecraftClient.getInstance().textRenderer, "Slot", scaledX, scaledY, -1);
                    drawCenteredText(matrix, MinecraftClient.getInstance().textRenderer, "" + this.slot, scaledX, scaledY2, -1);
                    matrix.pop();
                } else if (this.item != null) {
                    ItemRenderUtils.renderGuiItem(this.getPosX() + 2, this.getPosY() + 2, MinecraftClient.getInstance().getTickDelta(), this.item, this.scale, this.renderStackSize, this.renderDurability);
                }
            }
        }

    }

    public void updateItem() {

        if (this.itemId != null) {
            if (this.item != null) {
                return;
            }
            Item i = ItemRenderUtils.getItemByName(this.itemId);
            if (i != null) {
                this.item = new ItemStack(i);
            }
            return;
        }

        ClientPlayerEntity p = MinecraftClient.getInstance().player;
        if (p != null) {
            if (p.getInventory() != null) {
                ItemStack stack;
                if (this.slot == -1) {
                    stack = p.getMainHandStack();
                } else {
                    stack = p.getInventory().getStack(this.slot);
                }
                if (stack != ItemStack.EMPTY) {
                    this.item = stack;
                } else {
                    this.item = null;
                }
            }
        }

    }

    /**
     * Used to force-update the item when it's manually set by an item ID. Slot items will update every tick.
     */
    public void forceItemUpdate() {
        this.item = null;
    }

}
