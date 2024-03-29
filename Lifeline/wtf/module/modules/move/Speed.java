package Lifeline.wtf.module.modules.move;

import Lifeline.wtf.events.EventTarget;
import Lifeline.wtf.events.misc.EventPacket;
import Lifeline.wtf.events.world.EventUpdate;
import Lifeline.wtf.module.Category;
import Lifeline.wtf.module.Module;
import Lifeline.wtf.module.modules.combat.KillAura;
import Lifeline.wtf.module.value.Mode;
import Lifeline.wtf.module.value.Numbers;
import Lifeline.wtf.module.value.Option;
import Lifeline.wtf.utils.TimerUtil;
import Lifeline.wtf.utils.move.MoveUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;

public class Speed extends Module {

    public Mode<Enum> mode = new Mode<Enum>("Mode", "Mode", SMode.values(), SMode.Hypixel1);
    public Numbers<Double> timerSpeed = new Numbers("Timer", "Timer", 1.0, 0.1, 1.3, 0.05);
    public Option<Boolean> noAtkTimer = new Option<Boolean>("NoAttackTimer", "NoAttackTimer", true);
    TimerUtil timer = new TimerUtil();
    int counter;

    public static boolean strafeDirection;
    public Speed() {
        super("Speed", new String[]{}, Category.Move);
        addValues(mode, timerSpeed, noAtkTimer);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        counter = 0;
    }

    @Override
    public void onDisable() {
        net.minecraft.util.Timer.timerSpeed = 1.0f;
        super.onDisable();
        timer.reset();
    }

    @EventTarget
    public void onMotion(EventUpdate e) {
        this.setSuffix(mode.getModeAsString());
        if (mode.getValue().equals(SMode.Hypixel1)) {
            if (mc.thePlayer.onGround && !mc.gameSettings.keyBindJump.isKeyDown()) {
                if (MoveUtils.isMoving()) {
                    if (mc.thePlayer.moveForward > 0.0f) {
                        MoveUtils.strafe(MoveUtils.getBaseMoveSpeed() * 1.6 + Math.random() / 100);
                    }
                    else {
                        MoveUtils.strafe(MoveUtils.getBaseMoveSpeed() * 1 + Math.random() / 100);
                    }
                    Minecraft.thePlayer.motionY = 0.418;
                }
            }
            if (MoveUtils.isMoving()) {
                if (!noAtkTimer.getValue() || KillAura.target == null) {
                    net.minecraft.util.Timer.timerSpeed = timerSpeed.getValue().floatValue();
                }
                else {
                    Timer.timerSpeed = 1f;
                }
            }
            MoveUtils.strafe();
        }
        if (mode.getValue().equals(SMode.CSGO)) {
            if (mc.thePlayer.onGround && MoveUtils.isMoving()) {
                counter++;
                mc.thePlayer.motionY = 0.41999999999999999999999;
                MoveUtils.strafe(MoveUtils.getBaseMoveSpeed() + counter * 0.05);
            }
            if (!MoveUtils.isMoving() || mc.thePlayer.isCollidedHorizontally) {
                counter = 0;
            }
            MoveUtils.strafe();
        }
        strafeDirection = !strafeDirection;
    }

    @EventTarget
    void onPacket(EventPacket e) {
    }

    enum SMode {
        Hypixel1, CSGO
    }
}