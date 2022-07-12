package Lifeline.wtf.module.modules.move;

import Lifeline.wtf.events.EventTarget;
import Lifeline.wtf.events.world.EventUpdate;
import Lifeline.wtf.module.Category;
import Lifeline.wtf.module.Module;
import Lifeline.wtf.module.value.Numbers;

public class Timer extends Module {
    public Timer() {
        super("Timer", new String[]{"GameSpeed"}, Category.Move);
        addValues(this.timerSpee);
    }

    public static Numbers<Float> timerSpee = new Numbers<Float>("TimerSpeed", "TimerSpeed", 1f, 0.5f, 2f, 0.25f);

    @EventTarget
    public void onUpdate(EventUpdate e) {
        mc.timer.timerSpeed = timerSpee.getValue();
    }

    public void onDisable() {
        mc.timer.timerSpeed = 1f;
    }
}
