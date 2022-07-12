/*
 * Decompiled with CFR 0_132.
 */
package Lifeline.wtf.module.modules.move;

import Lifeline.wtf.events.world.EventUpdate;
import Lifeline.wtf.module.Category;
import Lifeline.wtf.module.Module;
import Lifeline.wtf.events.EventTarget;
import Lifeline.wtf.module.ModuleManager;
import Lifeline.wtf.module.modules.world.Scaffold;
import Lifeline.wtf.module.value.Option;

import java.awt.*;

public class Sprint
extends Module {
    private Option<Boolean> omni = new Option<Boolean>("Omni-Directional", "omni", true);

    public Sprint() {
        super("Sprint", new String[]{"run"}, Category.Move);
        this.setColor(new Color(158, 205, 125).getRGB());
        this.addValues(this.omni);
        this.setEnabled(true);
        this.setRemoved(true);
    }

    @EventTarget
    private void onUpdate(EventUpdate event) {
        System.out.println(Scaffold.getSprint());
        if(ModuleManager.getModuleByClass(Scaffold.class).isEnabled()){
            if (this.mc.thePlayer.getFoodStats().getFoodLevel() > 6 && this.omni.getValue() != false ? this.mc.thePlayer.moving() && Scaffold.getSprint() : this.mc.thePlayer.moveForward > 0.0f) {
                this.mc.thePlayer.setSprinting(true);
            }
        }else{
            if (this.mc.thePlayer.getFoodStats().getFoodLevel() > 6 && this.omni.getValue() != false ? this.mc.thePlayer.moving() : this.mc.thePlayer.moveForward > 0.0f) {
                this.mc.thePlayer.setSprinting(true);
            }
        }

    }
}

