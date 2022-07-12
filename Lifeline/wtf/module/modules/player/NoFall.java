
package Lifeline.wtf.module.modules.player;

import Lifeline.wtf.events.EventTarget;
import Lifeline.wtf.events.world.EventPacketSend;
import Lifeline.wtf.events.world.EventUpdate;
import Lifeline.wtf.module.Category;
import Lifeline.wtf.module.Module;
import Lifeline.wtf.module.ModuleManager;
import Lifeline.wtf.module.modules.move.Flight;
import Lifeline.wtf.utils.move.PlayerUtils;
import net.minecraft.network.play.client.C03PacketPlayer;

public class NoFall
extends Module {

    public NoFall() {
        super("NoFall", new String[]{}, Category.Player);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        setSuffix("Hypixel");
    }

    @EventTarget
    public void onPacket(EventPacketSend e) {
        if (e.getPacket() instanceof C03PacketPlayer && mc.thePlayer.motionY < 0 && PlayerUtils.isBlockUnder() && mc.thePlayer.fallDistance >= 2.5
                && !ModuleManager.getModuleByClass(Flight.class).isEnabled() && !mc.thePlayer.capabilities.isFlying) {
            ((C03PacketPlayer) e.getPacket()).onGround = true;
        }
    }
}




