/*
 * Decompiled with CFR 0_132.
 */
package Lifeline.wtf.module.modules.world;

import Lifeline.wtf.events.EventTarget;
import Lifeline.wtf.events.misc.EventPacket;
import Lifeline.wtf.module.Category;
import Lifeline.wtf.module.Module;
import Lifeline.wtf.module.value.Numbers;
import Lifeline.wtf.utils.TimeHelper;
import Lifeline.wtf.utils.move.MoveUtils;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

import java.util.ArrayList;

public class AntiVoid
extends Module {
    public double[] lastGroundPos = new double[3];
    public static Numbers<Double> pullbackTime = new Numbers<>("PullbackTime", "PullbackTime", 1500.0, 1000.0, 2000.0, 1500.0);
    public static TimeHelper timer = new TimeHelper();
    public static ArrayList<C03PacketPlayer> packets = new ArrayList<>();

    public AntiVoid() {
        super("AntiVoid", new String[]{}, Category.World);
        this.addValues(pullbackTime);
        setSuffix("Hypixel");
    }

    public static boolean isInVoid() {
        for (int i = 0; i <= 128; i++) {
            if (MoveUtils.isOnGround(i)) {
                return false;
            }
        }
        return true;
    }

    @EventTarget
    public void onPacket(EventPacket e) {

        if (!packets.isEmpty() && mc.thePlayer.ticksExisted < 100)
            packets.clear();

        if (e.getPacket() instanceof C03PacketPlayer) {
            C03PacketPlayer packet = ((C03PacketPlayer) e.getPacket());
            if (isInVoid()) {
                e.setCancelled(true);
                packets.add(packet);

                if (timer.isDelayComplete(pullbackTime.getValue())) {
                    mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(lastGroundPos[0], lastGroundPos[1] - 1, lastGroundPos[2], true));
                }
            } else {
                lastGroundPos[0] = mc.thePlayer.posX;
                lastGroundPos[1] = mc.thePlayer.posY;
                lastGroundPos[2] = mc.thePlayer.posZ;

                if (!packets.isEmpty()) {
                    for (C03PacketPlayer p : packets)
                        mc.getNetHandler().getNetworkManager().sendPacketNoEvent(p);
                    packets.clear();
                }
                timer.reset();
            }
        }
    }

    @EventTarget
    public void onRevPacket(EventPacket e) {
        if (e.getPacket() instanceof S08PacketPlayerPosLook && packets.size() > 1) {
            packets.clear();
        }
    }

    public static boolean isPullbacking() {
        return AntiVoid.isPullbacking();
    }
}
