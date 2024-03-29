package Lifeline.wtf.utils.move;


import Lifeline.wtf.events.world.EventMove;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;

public class MovementUtils1 {
	public static Minecraft mc = Minecraft.getMinecraft();

	public static float getSpeed() {
		return (float) Math
				.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
	}

	public static void strafe() {
		strafe((double) getSpeed());
	}
	
	
	public static boolean isMoving() {
		return mc.thePlayer != null
				&& (mc.thePlayer.movementInput.moveForward != 0F || mc.thePlayer.movementInput.moveStrafe != 0F);
	}

	public static boolean hasMotion() {
		return mc.thePlayer.motionX != 0D && mc.thePlayer.motionZ != 0D && mc.thePlayer.motionY != 0D;
	}

	public static void strafe(final Double double1) {
		if (!isMoving())
			return;
		final double yaw = getDirection();
		mc.thePlayer.motionX = -Math.sin(yaw) * double1;
		mc.thePlayer.motionZ = Math.cos(yaw) * double1;
	}

	public static void forward(final double length) {
		final double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
		mc.thePlayer.setPosition(mc.thePlayer.posX + (-Math.sin(yaw) * length), mc.thePlayer.posY,
				mc.thePlayer.posZ + (Math.cos(yaw) * length));
	}

	public static double getDirection() {
		float rotationYaw = mc.thePlayer.rotationYaw;
		if (mc.thePlayer.moveForward < 0F)
			rotationYaw += 180F;
		float forward = 1F;
		if (mc.thePlayer.moveForward < 0F)
			forward = -0.5F;
		else if (mc.thePlayer.moveForward > 0F)
			forward = 0.5F;
		if (mc.thePlayer.moveStrafing > 0F)
			rotationYaw -= 90F * forward;
		if (mc.thePlayer.moveStrafing < 0F)
			rotationYaw += 90F * forward;
		return Math.toRadians(rotationYaw);
	}



	public static void setSpeed(double speed) {
		Minecraft.getMinecraft();
		mc.thePlayer.motionX = -Math.sin(PlayerUtils.getDirection()) * speed;
		Minecraft.getMinecraft();
		mc.thePlayer.motionZ = Math.cos(PlayerUtils.getDirection()) * speed;
	}

	public static void setSpeed(EventMove moveEvent, double moveSpeed, float pseudoYaw, double pseudoStrafe,
                                double pseudoForward) {
		double forward = pseudoForward;
		double strafe = pseudoStrafe;
		float yaw = pseudoYaw;
		if (forward != 0.0) {
			if (strafe > 0.0) {
				yaw += (float) (forward > 0.0 ? -45 : 45);
			} else if (strafe < 0.0) {
				yaw += (float) (forward > 0.0 ? 45 : -45);
			}
			strafe = 0.0;
			if (forward > 0.0) {
				forward = 1.0;
			} else if (forward < 0.0) {
				forward = -1.0;
			}
		}
		if (strafe > 0.0) {
			strafe = 1.0;
		} else if (strafe < 0.0) {
			strafe = -1.0;
		}
		double mx = Math.cos(Math.toRadians(yaw + 90.0f));
		double mz = Math.sin(Math.toRadians(yaw + 90.0f));
		moveEvent.x = forward * moveSpeed * mx + strafe * moveSpeed * mz;
		moveEvent.z = forward * moveSpeed * mz - strafe * moveSpeed * mx;
	}

	public static double getBaseMoveSpeed() {
		double baseSpeed = 0.2875;
		if (MovementUtils1.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			baseSpeed *= 1.0 + 0.2
					* (double) (MovementUtils1.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
		}
		return baseSpeed;
	}

	public static double getJumpBoostModifier(double baseJumpHeight) {
		if (MovementUtils1.mc.thePlayer.isPotionActive(Potion.jump)) {
			int amplifier = MovementUtils1.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier();
			baseJumpHeight += (double) ((float) (amplifier + 1) * 0.1f);
		}
		return baseJumpHeight;
	}

	public static int getSpeedEffect() {
		if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed)) {
			return Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
		}
		return 0;
	}
}
