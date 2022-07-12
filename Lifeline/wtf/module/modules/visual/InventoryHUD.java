package Lifeline.wtf.module.modules.visual;

import Lifeline.wtf.events.EventTarget;
import Lifeline.wtf.events.rendering.EventRender2D;
import Lifeline.wtf.gui.font.FontLoaders;
import Lifeline.wtf.module.Category;
import Lifeline.wtf.module.Module;
import Lifeline.wtf.module.value.Numbers;
import Lifeline.wtf.utils.render.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class InventoryHUD extends Module {
    public final Numbers<Double> posX = new Numbers<Double>("PosX", "PosX", 0.0, 0.0, 1000.0, 1.0);
    public final Numbers<Double> posY = new Numbers<Double>("PosY", "PosY", 10.0, 0.0, 1000.0, 1.0);

    public InventoryHUD() {
        super("InventoryHUD", new String[]{"InventoryHUD", "InventoryHUD"}, Category.Visual);
        this.addValues(posX,posY);
        removed = true;
    }
    private int x2;
    int y2;
    @EventTarget
    public void renderinventory(EventRender2D e) {
        float boxWidth = 165;
        RenderUtil.drawRect((posX.getValue() - 3) + 0.5D, (posY.getValue() - 3) -10D, (posX.getValue() + boxWidth) - 0.6D, (posY.getValue() + 59) - 0.6D,new Color(0,0,0,120).getRGB());

        FontLoaders.kiona22.drawStringWithShadow("Inventory",this.posX.getValue(),this.posY.getValue()-9.7D,new Color(255,255,255).getRGB());

        GlStateManager.pushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        ItemStack[] items = mc.thePlayer.inventory.mainInventory;
        for (int size = items.length, item = 9; item < size; ++item) {
            final int slotX = (int) (posX.getValue() + (item) % 9 * 18);
            final int slotY = (int) (posY.getValue() + 2 + (item / 9 - 1) * 18);
            mc.getRenderItem().renderItemAndEffectIntoGUI(items[item], slotX + 1, slotY);
            mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, items[item], slotX, slotY);
        }
        RenderHelper.disableStandardItemLighting();
        mc.getRenderItem().zLevel = 0.0F;
        GlStateManager.popMatrix();
    }
}
