package Lifeline.wtf.module.modules.visual;

import Lifeline.wtf.events.EventTarget;
import Lifeline.wtf.events.rendering.EventRender2D;
import Lifeline.wtf.gui.notification.NotificationManager;
import Lifeline.wtf.module.Category;
import Lifeline.wtf.module.Module;
import Lifeline.wtf.module.value.Numbers;
import Lifeline.wtf.module.value.Option;

public class Notifications extends Module {

    public Notifications() {
        super("Notifications", new String[0], Category.Visual);
    }

    @EventTarget
    public void onRender2D(EventRender2D e) {
        NotificationManager.render();
    }

}
