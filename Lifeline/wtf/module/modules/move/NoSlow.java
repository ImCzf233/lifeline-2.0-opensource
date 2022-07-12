package Lifeline.wtf.module.modules.move;


import Lifeline.wtf.events.EventTarget;
import Lifeline.wtf.events.world.EventUpdate;
import Lifeline.wtf.module.Category;
import Lifeline.wtf.module.Module;
import Lifeline.wtf.module.modules.combat.KillAura;
import Lifeline.wtf.module.value.Mode;
import Lifeline.wtf.utils.PacketUtils;
import Lifeline.wtf.utils.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class NoSlow extends Module {

    private Mode<Enum> mode = new Mode("Mode", "Mode",  JMode.values(),  JMode.Hypixel);
    public TimerUtil timer = new TimerUtil();

    boolean nextTemp, lastBlockingStat, waitC03;
    List<Packet> packetBuf = new LinkedList<>();

    public NoSlow() {
        super("NoSlow", new String[]{}, Category.Move);
        this.setColor(new Color(188, 233, 248).getRGB());
        this.addValues(this.mode);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @EventTarget
    void onUpdate(EventUpdate e) {
        setSuffix(mode.getModeAsString());
        if (mode.getValue().equals(JMode.Hypixel)) {
            if ((e.isPre() && mc.thePlayer.getItemInUse() != null && mc.thePlayer.getItemInUse().getItem() != null) || KillAura.isBlocking && Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
                if (mc.thePlayer.isUsingItem() && mc.thePlayer.getItemInUseCount() >= 1 || KillAura.isBlocking && Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
                    PacketUtils.sendPacketNoEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                }
            }
        }
    }
    enum JMode {
        Vanilla, Hypixel
    }
}