
package Lifeline.wtf.module.modules.player;

import Lifeline.wtf.events.EventTarget;
import Lifeline.wtf.events.world.EventUpdate;
import Lifeline.wtf.module.Category;
import Lifeline.wtf.module.Module;
import Lifeline.wtf.module.value.Mode;
import Lifeline.wtf.module.value.Numbers;
import Lifeline.wtf.utils.TimerUtil;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C16PacketClientStatus;

public class AutoArmor extends Module {

	private final TimerUtil timer = new TimerUtil();

	//ģʽ
	private final Mode<Enum> modeSetting = new Mode<Enum>("Mode", "Mode", Armormode.values(), Armormode.OpenInv);

	private enum Armormode {
		Vanilla, OpenInv
	}

	//�ӳ�
	private final Numbers<Double> delayValue = new Numbers<Double>("Delay", "Delay", 1.0, 0.0, 5.0, 0.1);

	public AutoArmor() {
		super("AutoArmor", new String[]{}, Category.Player);
		addValues(modeSetting, delayValue);
	}

	@EventTarget
	void onUpdate(EventUpdate event) {
		if (event.isPre()) {
			long delay = delayValue.getValue().intValue() * 100L;
			if (modeSetting.getValue() == Armormode.OpenInv && !(mc.currentScreen instanceof GuiInventory)) return;
			if ((mc.currentScreen == null || mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiChat) && timer.hasPassed(delay)) {
				for (int type = 1; type < 5; ++type) {
					if (mc.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()) {
						ItemStack is = mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack();
						if (isBestArmor(is, type)) continue;
						if (modeSetting.getValue() == Armormode.Vanilla) {
							mc.thePlayer.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
						}
						drop(4 + type);
					}
					for (int i = 9; i < 45; ++i) {
						if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) continue;
						ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
						if (!isBestArmor(is, type) || !(getProtection(is) > 0.0f)) continue;
						this.shiftClick(i);
						this.timer.reset();
						if (delayValue.getValue().longValue() <= 0L) continue;
						return;
					}
				}
			}
		}
	}

	private boolean isBestArmor(ItemStack stack, int type) {
		float prot = getProtection(stack);
		String strType = "";
		if (type == 1) {
			strType = "helmet";
		} else if (type == 2) {
			strType = "chestplate";
		} else if (type == 3) {
			strType = "leggings";
		} else if (type == 4) {
			strType = "boots";
		}
		if (!stack.getUnlocalizedName().contains(strType)) {
			return false;
		}
		for (int i = 5; i < 45; ++i) {
			if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) continue;
			ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
			if (!(getProtection(is) > prot) || !is.getUnlocalizedName().contains(strType)) continue;
			return false;
		}
		return true;
	}

	private void shiftClick(int slot) {
		mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 0, 1, mc.thePlayer);
	}

	private void drop(int slot) {
		mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 1, 4, mc.thePlayer);
	}

	private float getProtection(ItemStack stack) {
		float prot = 0.0f;
		if (stack.getItem() instanceof ItemArmor) {
			ItemArmor armor = (ItemArmor) stack.getItem();
			prot = (float) ((double) prot
					+ ((double) armor.damageReduceAmount + (double) ((100 - armor.damageReduceAmount)
					* EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack)) * 0.0075));
			prot = (float) ((double) prot
					+ (double) EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack)
					/ 100.0);
			prot = (float) ((double) prot
					+ (double) EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack)
					/ 100.0);
			prot = (float) ((double) prot
					+ (double) EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack) / 100.0);
			prot = (float) ((double) prot
					+ (double) EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 50.0);
			prot = (float) ((double) prot
					+ (double) EnchantmentHelper.getEnchantmentLevel(Enchantment.featherFalling.effectId, stack)
					/ 100.0);
		}
		return prot;
	}
}
