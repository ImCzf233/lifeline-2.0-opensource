package Lifeline.wtf.module.modules.move;

import Lifeline.wtf.Client;
import Lifeline.wtf.events.EventTarget;
import Lifeline.wtf.events.world.EventUpdate;
import Lifeline.wtf.module.Category;
import Lifeline.wtf.module.Module;
import Lifeline.wtf.utils.PlayerUtil;

public class Strafe extends Module {

    public Strafe() {
        super("Strafe",new String[]{}, Category.Move);
    }

    @EventTarget
    void onUpdate(EventUpdate event) {
        if (
                Client.instance.getModuleManager().getModuleByName("Speed").isEnabled()) return;
        PlayerUtil.doStrafe();
    }
}
