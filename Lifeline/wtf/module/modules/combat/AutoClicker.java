package Lifeline.wtf.module.modules.combat;

import Lifeline.wtf.events.EventTarget;
import Lifeline.wtf.events.world.EventUpdate;
import Lifeline.wtf.module.Category;
import Lifeline.wtf.module.Module;
import Lifeline.wtf.module.value.Numbers;
import Lifeline.wtf.module.value.Option;
import Lifeline.wtf.utils.TimerUtil;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;

public class AutoClicker extends Module {

    private final TimerUtil timer;
    private Numbers<Double> cps;
    private Numbers<Double> cpsMin;
    private Option<Boolean> autoblock;
    public boolean doBlock;

    public AutoClicker() {
        super("AutoClicker", new String[]{}, Category.Combat);
        this.timer = new TimerUtil();
        this.cps = new Numbers<Double>("CPSMax", "CpsMax", 5.0, 1.0, 20.0, 1.0);
        this.cpsMin = new Numbers<Double>("CPSMin", "CpsMin", 5.0, 1.0, 20.0, 1.0);
        this.autoblock = new Option<Boolean>("AutoBlock", "AutoBlock", false);
        this.doBlock = true;
        this.addValues(this.cps, this.cpsMin, this.autoblock);
    }

    @EventTarget
    public void onTick(final EventUpdate event) {
        try {
            //this.status = this.cps.getValue().toString();
            final int key = AutoClicker.mc.gameSettings.keyBindAttack.getKeyCode();
            if (AutoClicker.mc.gameSettings.keyBindAttack.isKeyDown()) {
                final float delays = (float) (this.cpsMin.getValue() + 2.0);
                if (this.timer.delay(delays * 10.0f)) {
                    AutoClicker.mc.thePlayer.swingItem();
                    KeyBinding.onTick(key);
                    try {
                        if (this.autoblock.getValue()) {
                            if (AutoClicker.mc.thePlayer.getCurrentEquippedItem() == null) {
                                return;
                            }
                            if (!(AutoClicker.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword)) {
                                return;
                            }
                            if (this.autoblock.getValue() && AutoClicker.mc.objectMouseOver.entityHit != null && AutoClicker.mc.objectMouseOver.entityHit.isEntityAlive() && AutoClicker.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword && this.timer.delay(100.0f)) {
                                AutoClicker.mc.thePlayer.getCurrentEquippedItem().useItemRightClick((World)AutoClicker.mc.theWorld, (EntityPlayer)AutoClicker.mc.thePlayer);
                                this.timer.reset();
                            }
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    this.timer.reset();
                }
            }
        }
        catch (NullPointerException ex) {}
    }
}