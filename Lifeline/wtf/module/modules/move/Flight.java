package Lifeline.wtf.module.modules.move;

import Lifeline.wtf.events.world.EventMove;
import Lifeline.wtf.events.world.EventPacketSend;
import Lifeline.wtf.events.world.EventUpdate;
import Lifeline.wtf.gui.notification.Notification;
import Lifeline.wtf.gui.notification.NotificationManager;
import Lifeline.wtf.gui.notification.NotificationType;
import Lifeline.wtf.module.Category;
import Lifeline.wtf.module.Module;
import Lifeline.wtf.events.EventTarget;
import Lifeline.wtf.module.value.Mode;
import Lifeline.wtf.utils.DamageUtils;
import Lifeline.wtf.utils.Helper;
import Lifeline.wtf.utils.PacketUtils;
import Lifeline.wtf.utils.TimerUtil;
import Lifeline.wtf.utils.move.MoveUtils;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.status.client.C01PacketPing;

import java.awt.*;

public class Flight
		extends Module {
	public Mode mode = new Mode("Mode", "mode", (Enum[])FlightMode.values(), (Enum)FlightMode.Hypixel);
	private TimerUtil timer = new TimerUtil();
	public Flight() {
		super("Flight",new String[]{"fly"}, Category.Move);
		this.setColor(new Color(158, 114, 243).getRGB());
		this.addValues(this.mode);
	}

	int ticks, stages;

	@EventTarget
	public void onPreUpdate(EventUpdate e) {
		ticks++;
		if (stages == 2 || ticks >= 11) {
			mc.timer.timerSpeed = 1;

			mc.thePlayer.motionY = 0;
			mc.thePlayer.lastReportedPosY = 0;
			mc.thePlayer.jumpMovementFactor = 0;

			e.setOnGround(true);
		}

		if (stages == 0) {
			if (ticks == 1) {
				DamageUtils.hypixelDamage();
				mc.thePlayer.motionY = 0.2469883648888012144;
			} else if (ticks == 12) {
				mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.38, mc.thePlayer.posZ);
			} else if (ticks == 13) {
				mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.46, mc.thePlayer.posZ);
			} else if (ticks == 14) {
				mc.thePlayer.motionY = -0.52;
				stages = 2;
			}
		}
	}

	@EventTarget
	public void onMove(EventMove e) {
		MoveUtils.setMotion( ticks > 12 ? MoveUtils.defaultSpeed() : 0);
	}

	@Override
	public void onEnable() {
		ticks = stages = 0;
		mc.timer.timerSpeed = 100;
		super.onEnable();
	}

	@Override
	public void onDisable() {
		mc.timer.timerSpeed = 1;
		super.onDisable();
	}


	public static enum FlightMode {
		Hypixel
	}

}
