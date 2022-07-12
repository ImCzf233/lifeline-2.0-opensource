package Lifeline.wtf.module.modules.combat;

import Lifeline.wtf.Client;
import Lifeline.wtf.events.EventTarget;
import Lifeline.wtf.events.rendering.Render3DEvent;
import Lifeline.wtf.events.world.EventUpdate;
import Lifeline.wtf.module.Category;
import Lifeline.wtf.module.Module;
import Lifeline.wtf.module.modules.move.Speed;
import Lifeline.wtf.module.value.Numbers;
import Lifeline.wtf.module.value.Option;
import Lifeline.wtf.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TargetStrafe
        extends Module {

    public static EntityLivingBase target;
    private List<EntityLivingBase> targets = new ArrayList<>();
    private Option<Boolean> players = new Option<Boolean>("Players", "Target Players", true);
    private Option<Boolean> monsters = new Option<Boolean>("Monsters", "Target Monsters", false);
    private Option<Boolean> invisibles = new Option<Boolean>("Invisibles", "Target Invisibles", false);
    private Option<Boolean> teams = new Option<Boolean>("Teams", "Teams Mode", false);
    private Numbers<Float> range = new Numbers<>("Range", "Range",5.2F, 1.0F, 7.0F, 0.1F);
    public static Numbers<Float> distance = new Numbers<>("Distance", "Distance",3.0f, 0.1f, 5.0f, 0.1f);
    public static Option<Boolean> spaceOnly = new Option<Boolean>("Press Space only", "Press Space only", false);
    public static Vec3 indexPos;
    public static int index, arraySize;
    private boolean set, changeDir;

    public TargetStrafe() {
        super("TargetStrafe", new String[]{}, Category.Combat);
        addValues(players,monsters,invisibles,teams,range,distance,spaceOnly);
    }

    @Override
    public void onEnable() {
        set = false;
    }

    @Override
    public void onDisable() {
        if (Minecraft.getMinecraft().thePlayer == null) return;
        Minecraft.getMinecraft().timer.timerSpeed = 1f;
        set = false;
    }

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        if (Minecraft.getMinecraft().theWorld == null || Minecraft.getMinecraft().thePlayer == null) return;
        for (Entity entity : Minecraft.getMinecraft().theWorld.getLoadedEntityList()) {
            if (entity instanceof EntityLivingBase) {
                final EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
                if (entityLivingBase.isDead || entityLivingBase == Minecraft.getMinecraft().thePlayer || !isTargetable(entityLivingBase, Minecraft.getMinecraft().thePlayer))
                    continue;
                drawCirle(entity, target != null && entity == target ? new Color(255, 72, 67) : new Color(255, 255, 255), event.getPartialTicks());
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (!Client.instance.getModuleManager().getModule("speed").isEnabled() || (!Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown() && TargetStrafe.spaceOnly.isEnabled())) {target = null; set = false;return;}
        target = getTarget();
        if (target != null) {
            final ArrayList<Vec3> posArrayList = new ArrayList<>();
            for (float rotation = 0; rotation < (3.141592f * 2.0); rotation += 3.141592f * 2.0f / 27f) {
                final Vec3 pos = new Vec3(distance.getValue() * Math.cos(rotation) + target.posX, target.posY, distance.getValue() * Math.sin(rotation) + target.posZ);
                posArrayList.add(pos);
            }
            arraySize = posArrayList.size();
            if (!set) {
                final ArrayList<Vec3> posBuffer = new ArrayList<>(posArrayList);
                posBuffer.sort(Comparator.comparingDouble(vec3 -> Minecraft.getMinecraft().thePlayer.getDistance(vec3.xCoord, vec3.yCoord, vec3.zCoord)));
                index = posArrayList.indexOf(posBuffer.get(0));
                set = true;
            } else {
                final BlockPos blockPos = new BlockPos(posArrayList.get(index).xCoord, posArrayList.get(index).yCoord, posArrayList.get(index).zCoord);
                indexPos = new Vec3(blockPos.getX() + 0.5f, posArrayList.get(index).yCoord, blockPos.getZ());
                if (!(!inVoid(indexPos) && Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(indexPos.xCoord, Minecraft.getMinecraft().thePlayer.posY, indexPos.zCoord)).getBlock().getCollisionBoundingBox(Minecraft.getMinecraft().theWorld, new BlockPos(indexPos.xCoord, Minecraft.getMinecraft().thePlayer.posY, indexPos.zCoord), Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(indexPos.xCoord, Minecraft.getMinecraft().thePlayer.posY, indexPos.zCoord))) == null && Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(indexPos.xCoord, Minecraft.getMinecraft().thePlayer.posY + 1, indexPos.zCoord)).getBlock().getCollisionBoundingBox(Minecraft.getMinecraft().theWorld, new BlockPos(indexPos.xCoord, Minecraft.getMinecraft().thePlayer.posY + 1, indexPos.zCoord), Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(indexPos.xCoord, Minecraft.getMinecraft().thePlayer.posY + 1, indexPos.zCoord))) == null && Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(indexPos.xCoord, Minecraft.getMinecraft().thePlayer.posY + 2, indexPos.zCoord)).getBlock().getCollisionBoundingBox(Minecraft.getMinecraft().theWorld, new BlockPos(indexPos.xCoord, Minecraft.getMinecraft().thePlayer.posY + 2, indexPos.zCoord), Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(indexPos.xCoord, Minecraft.getMinecraft().thePlayer.posY + 2, indexPos.zCoord))) == null)) {
                    Speed.strafeDirection = !Speed.strafeDirection;
                    if (!Speed.strafeDirection) {
                        if (index + 1 > posArrayList.size() - 1) index = 0;
                        else index++;
                    } else {
                        if (index - 1 < 0) index = posArrayList.size() - 1;
                        else index--;
                    }
                } else {
                    if (Minecraft.getMinecraft().thePlayer.isCollidedHorizontally) {
                        if (!changeDir) {
                            Speed.strafeDirection = !Speed.strafeDirection;
                            changeIndex(posArrayList);
                            changeDir = true;
                        }
                    } else changeDir = false;
                    if (Minecraft.getMinecraft().gameSettings.keyBindRight.isPressed()) {
                        Speed.strafeDirection = true;
                    } else if (Minecraft.getMinecraft().gameSettings.keyBindLeft.isPressed()) {
                        Speed.strafeDirection = false;
                    }
                    if (Minecraft.getMinecraft().thePlayer.getDistance(indexPos.xCoord, Minecraft.getMinecraft().thePlayer.posY, indexPos.zCoord) <= Minecraft.getMinecraft().thePlayer.getDistance(Minecraft.getMinecraft().thePlayer.prevPosX, Minecraft.getMinecraft().thePlayer.prevPosY, Minecraft.getMinecraft().thePlayer.prevPosZ) * 2) {
                        changeIndex(posArrayList);
                    }
                }
            }
        } else {
            set = false;
            index = 0;
            indexPos = null;
        }
    }

    private void changeIndex(final ArrayList<Vec3> posArrayList) {
        if (!Speed.strafeDirection) {
            if (index + 1 > posArrayList.size() - 1) index = 0;
            else index++;
        } else {
            if (index - 1 < 0) index = posArrayList.size() - 1;
            else index--;
        }
    }

    private void drawCirle(Entity entity, Color color, float partialTicks) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        final double x = RenderUtil.interpolate(entity.posX, entity.lastTickPosX, partialTicks) - Minecraft.getMinecraft().getRenderManager().getRenderPosX();
        final double y = RenderUtil.interpolate(entity.posY, entity.lastTickPosY, partialTicks) - Minecraft.getMinecraft().getRenderManager().getRenderPosY();
        final double z = RenderUtil.interpolate(entity.posZ, entity.lastTickPosZ, partialTicks) - Minecraft.getMinecraft().getRenderManager().getRenderPosZ();
        GL11.glLineWidth(4.0f);
        final ArrayList<Vec3> posArrayList = new ArrayList<>();
        for (float rotation = 0; rotation < (3.141592f * 2.0); rotation += 3.141592f * 2.0f / 27f) {
            final Vec3 pos = new Vec3(distance.getValue() * Math.cos(rotation) + x, y, distance.getValue() * Math.sin(rotation) + z);
            posArrayList.add(pos);
        }
        GL11.glEnable(GL11.GL_LINE_STIPPLE);
        GL11.glLineStipple(4, (short) 0xAAAA);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        {
            final float r = ((float) 1 / 255) * color.getRed();
            final float g = ((float) 1 / 255) * color.getGreen();
            final float b = ((float) 1 / 255) * color.getBlue();
            for (Vec3 pos : posArrayList) {
                GL11.glColor3d(Client.instance.getModuleManager().getModule("speed").isEnabled() && posArrayList.indexOf(pos) == index ? 0.15f : r, Client.instance.getModuleManager().getModule("speed").isEnabled() && posArrayList.indexOf(pos) == index ? 0.15f : g, Client.instance.getModuleManager().getModule("speed").isEnabled() && posArrayList.indexOf(pos) == index ? 1 : b);
                GL11.glVertex3d(pos.xCoord, pos.yCoord, pos.zCoord);
            }
        }
        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_STIPPLE);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glLineWidth(1);
        GL11.glPopMatrix();
    }

    private EntityLivingBase getTarget() {
        targets.clear();
        double Dist = Double.MAX_VALUE;
        if (Minecraft.getMinecraft().theWorld != null) {
            for (Object object : Minecraft.getMinecraft().theWorld.loadedEntityList) {
                if ((object instanceof EntityLivingBase)) {
                    EntityLivingBase e = (EntityLivingBase) object;
                    if ((Minecraft.getMinecraft().thePlayer.getDistanceToEntity(e) < Dist)) {
                        if (isTargetable(e, Minecraft.getMinecraft().thePlayer)) {
                            targets.add(e);
                        }
                    }
                }
            }
        }
        if (targets.isEmpty()) return null;
        targets.sort(Comparator.comparingDouble(target -> Minecraft.getMinecraft().thePlayer.getDistanceToEntity(target)));

        return targets.get(0);
    }

    public boolean isTeammate(EntityPlayer target) {
        if (!teams.isEnabled()) return false;
        boolean teamChecks = false;
        EnumChatFormatting myCol = null;
        EnumChatFormatting enemyCol = null;
        if (target != null) {
            for (EnumChatFormatting col : EnumChatFormatting.values()) {
                if (col == EnumChatFormatting.RESET)
                    continue;
                if (Minecraft.getMinecraft().thePlayer.getDisplayName().getFormattedText().contains(col.toString()) && myCol == null) {
                    myCol = col;
                }
                if (target.getDisplayName().getFormattedText().contains(col.toString()) && enemyCol == null) {
                    enemyCol = col;
                }
            }
            try {
                if (myCol != null && enemyCol != null) {
                    teamChecks = myCol != enemyCol;
                } else {
                    if (Minecraft.getMinecraft().thePlayer.getTeam() != null) {
                        teamChecks = !Minecraft.getMinecraft().thePlayer.isOnSameTeam(target);
                    } else {
                        if (Minecraft.getMinecraft().thePlayer.inventory.armorInventory[3].getItem() instanceof ItemBlock) {
                            teamChecks = !ItemStack.areItemStacksEqual(Minecraft.getMinecraft().thePlayer.inventory.armorInventory[3], target.inventory.armorInventory[3]);
                        }
                    }
                }
            } catch (Exception ignored) {
            }
        }
        return teamChecks;
    }

    public boolean inVoid(Vec3 vec3) {
        for (int i = (int) Math.ceil(vec3.yCoord); i >= 0; i--) {
            if (Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(vec3.xCoord, i, vec3.zCoord)).getBlock() != Blocks.air) {
                return false;
            }
        }
        return true;
    }

    private boolean isTargetable(EntityLivingBase entity, EntityPlayerSP clientPlayer) {
        return entity.getUniqueID() != clientPlayer.getUniqueID() && entity.isEntityAlive() && !(entity instanceof EntityPlayer && isTeammate((EntityPlayer) entity)) && !AntiBot.isEntityBot(entity) && !(entity.isInvisible() && !invisibles.isEnabled()) && clientPlayer.getDistanceToEntity(entity) <= range.getValue() && (entity instanceof EntityPlayer && players.isEnabled() || (entity instanceof EntityMob || entity instanceof EntityGolem) && monsters.isEnabled() || (entity instanceof EntityAnimal));
    }
}




