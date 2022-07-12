/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package Lifeline.wtf.module.modules.move;

import Lifeline.wtf.events.EventTarget;
import Lifeline.wtf.events.world.EventPacketSend;
import Lifeline.wtf.events.world.EventUpdate;
import Lifeline.wtf.gui.clickgui.GuiClickUI;
import Lifeline.wtf.module.Category;
import Lifeline.wtf.module.Module;
import Lifeline.wtf.module.value.Numbers;
import Lifeline.wtf.module.value.Option;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;


public class GuiMove extends Module {

	private final Numbers<Double> slowdown = new Numbers("SlowDown", "SlowDown", 1, 0.1, 1, 0.1);
	private final Option<Boolean> packet = new Option("Packet", "Packet", false);

	private final KeyBinding[] affectedBindings = new KeyBinding[]{
			mc.gameSettings.keyBindForward,
			mc.gameSettings.keyBindBack,
			mc.gameSettings.keyBindRight,
			mc.gameSettings.keyBindLeft,
			mc.gameSettings.keyBindJump
	};

	public GuiMove() {
		super("GuiMove",new String[]{}, Category.Move);
		addValues(slowdown,packet);
	}

	@EventTarget
	public void onPreMotion(final EventUpdate event) {
		if(event.isPre()) {
			if (mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat)) {
				for (final KeyBinding a : affectedBindings) {
					a.setKeyPressed(GameSettings.isKeyDown(a));
				}
				if (mc.currentScreen != null) {
					final double s = slowdown.getValue();
					if (!(mc.currentScreen instanceof GuiClickUI)) {
						mc.thePlayer.motionX *= s;
						mc.thePlayer.motionZ *= s;
					}
				}
			}
		}
	}

	@EventTarget
	public void onPacketSend(final EventPacketSend event) {
		final Packet<?> p = event.getPacket();

		if (packet.getValue() == true) {
			if (p instanceof C0DPacketCloseWindow)
				event.setCancelled(true);

			if (p instanceof C0EPacketClickWindow) {
				event.setCancelled(true);

				mc.getNetHandler().sendQueueWithoutEvent(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
				mc.getNetHandler().sendQueueWithoutEvent(event.getPacket());
				mc.getNetHandler().sendQueueWithoutEvent(new C0DPacketCloseWindow(mc.thePlayer.inventoryContainer.windowId));
			}

			if (p instanceof C16PacketClientStatus) {
				final C16PacketClientStatus packetClientStatus = (C16PacketClientStatus) event.getPacket();
				if (packetClientStatus.getStatus() == C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT)
					event.setCancelled(true);
			}
		}
	}
}