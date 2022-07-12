/*
 * Decompiled with CFR 0_132.
 */
package Lifeline.wtf.module.modules.world;

import Lifeline.wtf.events.EventTarget;
import Lifeline.wtf.events.misc.EventCollideWithBlock;
import Lifeline.wtf.events.world.EventMove;
import Lifeline.wtf.events.world.EventUpdate;
import Lifeline.wtf.module.Category;
import Lifeline.wtf.module.Module;
import Lifeline.wtf.module.value.Mode;
import Lifeline.wtf.utils.math.RotationUtil;
import net.minecraft.block.state.pattern.BlockHelper;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;

import java.awt.*;

public class Phase
extends Module {
    private Mode<Enum> mode = new Mode("Mode", "mode", (Enum[])PhaseMode.values(), (Enum)PhaseMode.NewNCP);

    public Phase() {
        super("Phase", new String[]{"noclip"}, Category.World);
        this.setColor(new Color(255, 166, 25).getRGB());
        this.addValues(this.mode);
    }

    @EventTarget
    private void onBlockCollision(EventCollideWithBlock e) {
        if (e.getBoundingBox() != null && e.getBoundingBox().maxY > this.mc.thePlayer.boundingBox.minY && this.mc.thePlayer.isSneaking() && this.mode.getValue() != PhaseMode.OldNCP) {
            e.setBoundingBox(null);
        }
        if (e.getBoundingBox() != null && e.getBoundingBox().maxY > this.mc.thePlayer.boundingBox.minY && this.mode.getValue() == PhaseMode.OldNCP) {
            e.setBoundingBox(null);
        }
    }

    @EventTarget
    private void onMove(EventMove e) {
        if (BlockHelper.insideBlock() && this.mc.thePlayer.isSneaking() && this.mode.getValue() == PhaseMode.SkipClip) {
            this.mc.thePlayer.boundingBox.offsetAndUpdate((double)this.mc.thePlayer.movementInput.moveForward * 3.6 * Math.cos(Math.toRadians(this.mc.thePlayer.rotationYaw + 90.0f)) + (double)this.mc.thePlayer.movementInput.moveStrafe * 3.6 * Math.sin(Math.toRadians(this.mc.thePlayer.rotationYaw + 90.0f)), 0.0, (double)this.mc.thePlayer.movementInput.moveForward * 3.6 * Math.sin(Math.toRadians(this.mc.thePlayer.rotationYaw + 90.0f)) - (double)this.mc.thePlayer.movementInput.moveStrafe * 3.6 * Math.cos(Math.toRadians(this.mc.thePlayer.rotationYaw + 90.0f)));
        }
    }

    @EventTarget
    private void onUpdate(EventUpdate e) {
        if (BlockHelper.insideBlock()) {
            if (this.mode.getValue() == PhaseMode.NewNCP && this.mc.thePlayer.isSneaking()) {
                this.mc.thePlayer.boundingBox.offsetAndUpdate(0.0524 * Math.cos(Math.toRadians(RotationUtil.yaw() + 90.0f)), 0.0, 0.0524 * Math.sin(Math.toRadians(RotationUtil.yaw() + 90.0f)));
            }
            if (this.mode.getValue() == PhaseMode.OldNCP && this.mc.thePlayer.isCollidedVertically) {
                double x = (double)(- MathHelper.sin(this.mc.thePlayer.getDirection())) * 0.2;
                double z = (double)MathHelper.cos(this.mc.thePlayer.getDirection()) * 0.2;
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX + x, this.mc.thePlayer.posY, this.mc.thePlayer.posZ + z, false));
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX + x, Double.MIN_VALUE, this.mc.thePlayer.posZ + z, true));
                this.mc.thePlayer.setPosition(this.mc.thePlayer.posX + x, this.mc.thePlayer.posY, this.mc.thePlayer.posZ + z);
            }
            if (this.mc.thePlayer.onGround && this.mode.getValue() == PhaseMode.NewNCP) {
                this.mc.thePlayer.jump();
            }
        }
    }

    static enum PhaseMode {
        NewNCP,
        OldNCP,
        SkipClip;
    }

}

