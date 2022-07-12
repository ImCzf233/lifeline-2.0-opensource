/*
Author:SuMuGod
Date:2022/7/10 4:31
Project:ETB Reborn
*/
package Lifeline.wtf.module.modules.world;

import Lifeline.wtf.Client;
import Lifeline.wtf.events.EventTarget;
import Lifeline.wtf.events.rendering.EventRender2D;
import Lifeline.wtf.events.world.EventUpdate;
import Lifeline.wtf.gui.font.FontLoaders;
import Lifeline.wtf.module.Category;
import Lifeline.wtf.module.Module;
import Lifeline.wtf.module.modules.visual.HUD;
import Lifeline.wtf.module.value.Option;
import Lifeline.wtf.utils.PlayerUtil;
import Lifeline.wtf.utils.move.MoveUtils;
import Lifeline.wtf.utils.sub.Rotation;
import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.List;

public class Scaffold extends Module {
    private BlockData data;
    private int slot;
    private int towerTick;
public static boolean Sprint;
    private static FloatBuffer colorBuffer;
    private static final Vec3 LIGHT0_POS;
    private static final Vec3 LIGHT1_POS;
    private ItemStack currentblock;
    private static final Rotation rotation = new Rotation(999.0f, 999.0f);
    public final static List<Block> invalidBlocks = Arrays.asList(Blocks.enchanting_table, Blocks.furnace,
            Blocks.carpet, Blocks.crafting_table, Blocks.trapped_chest, Blocks.chest, Blocks.dispenser, Blocks.air,
            Blocks.water, Blocks.lava, Blocks.flowing_water, Blocks.flowing_lava, Blocks.sand, Blocks.snow_layer,
            Blocks.torch, Blocks.anvil, Blocks.jukebox, Blocks.stone_button, Blocks.wooden_button, Blocks.lever,
            Blocks.noteblock, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate,
            Blocks.wooden_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_slab, Blocks.wooden_slab,
            Blocks.stone_slab2, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.yellow_flower, Blocks.red_flower,
            Blocks.anvil, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.cactus, Blocks.ladder,
            Blocks.web);
    static final Option<Boolean> sprint = new Option<>("Sprint", "Sprint", false);
    private final Option<Boolean> tower = new Option<>("Tower", "Tower", false);
    private final Option<Boolean> towerboost = new Option<>("TowerBoost", "TowerBoost", false);

    public Scaffold() {
        super("Scaffold",  new String[]{"magiccarpet", "blockplacer", "airwalk"}, Category.World);
        this.setColor((new Color(244, 119, 194)).getRGB());
        addValues(sprint,tower,towerboost);
    }

    @EventTarget
    public void onPreUpdate(EventUpdate event) {
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            mc.thePlayer.motionX = mc.thePlayer.motionX * 0.8F;
            mc.thePlayer.motionZ = mc.thePlayer.motionZ * 0.8F;
        }

        if (!sprint.getValue()) {
            mc.thePlayer.setSprinting(false);
            Sprint = false;
        }else{
            Sprint = true;
        }



        EntityPlayerSP player = mc.thePlayer;
        WorldClient world = mc.theWorld;
        if (this.getBlockCount() <= 0 && this.getallBlockCount() <= 0) {
            return;
        }
        if (this.getBlockCount() <= 0) {
            int spoofSlot = this.getBestSpoofSlot();
            this.getBlock(spoofSlot);
        }
        this.data = this.getBlockData(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0,
                mc.thePlayer.posZ)) == null
                ? this.getBlockData(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0,
                mc.thePlayer.posZ).down(1))
                : this.getBlockData(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0,
                mc.thePlayer.posZ));
        this.slot = this.getBlockSlot();
        this.currentblock = mc.thePlayer.inventoryContainer.getSlot(slot + 36).getStack();
        if (this.data == null || this.slot == -1 || this.getBlockCount() <= 0
                || !(MoveUtils.isMoving() || mc.gameSettings.keyBindJump.isKeyDown())) {
            return;
        }
        if (mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX,
                mc.thePlayer.posY - 0.5, mc.thePlayer.posZ)).getBlock() == Blocks.air) {
            float rot = 0.0f;
            if (mc.thePlayer.movementInput.moveForward > 0.0f) {
                rot = 180.0f;
                if (mc.thePlayer.movementInput.moveStrafe > 0.0f) {
                    rot = -120.0f;
                } else if (mc.thePlayer.movementInput.moveStrafe < 0.0f) {
                    rot = 120.0f;
                }
            } else if (mc.thePlayer.movementInput.moveForward == 0.0f) {
                rot = 180.0f;
                if (mc.thePlayer.movementInput.moveStrafe > 0.0f) {
                    rot = -90.0f;
                } else if (mc.thePlayer.movementInput.moveStrafe < 0.0f) {
                    rot = 90.0f;
                }
            } else if (mc.thePlayer.movementInput.moveForward < 0.0f) {
                if (mc.thePlayer.movementInput.moveStrafe > 0.0f) {
                    rot = -45.0f;
                } else if (mc.thePlayer.movementInput.moveStrafe < 0.0f) {
                    rot = 45.0f;
                }
            }
            if (PlayerUtil.isAirUnder((Entity) mc.thePlayer) && mc.gameSettings.keyBindJump.isKeyDown()
                    && !PlayerUtil.MovementInput() && ((Boolean) this.tower.getValue()).booleanValue()) {
                rot = 180.0f;
            }
            rotation.setYaw(MathHelper.wrapAngleTo180_float((float) mc.thePlayer.rotationYaw) - rot);
            rotation.setPitch(87.5f);
        }
        if (rotation.getYaw() != 999.0f) {
            mc.thePlayer.rotationYawHead = rotation.getYaw();
            mc.thePlayer.renderYawOffset = rotation.getYaw();
            event.setYaw(rotation.getYaw());
        }
        if (rotation.getPitch() != 999.0f) {
//            mc.thePlayer.rotationPitchHead = rotation.getPitch();
            event.setPitch(rotation.getPitch());
        }
        if (PlayerUtil.isAirUnder((Entity) mc.thePlayer) && MoveUtils.isOnGround((double) 1.15)
                && mc.gameSettings.keyBindJump.isKeyDown() && !PlayerUtil.MovementInput()
                && ((Boolean) this.tower.getValue()).booleanValue()) {
            if (this.towerboost.getValue().booleanValue())
                mc.timer.timerSpeed = 2.1078f;
            mc.thePlayer.motionX = 0.0;
            mc.thePlayer.motionZ = 0.0;
            mc.thePlayer.movementInput.moveForward = 0.0f;
            mc.thePlayer.movementInput.moveStrafe = 0.0f;
            if (++this.towerTick < 10) {
                mc.thePlayer.jump();
            } else {
                this.towerTick = 0;
            }
        }
        if (MoveUtils.isOnGround((double) 1.15) && mc.gameSettings.keyBindJump.isKeyDown()
                && !PlayerUtil.MovementInput() && ((Boolean) this.tower.getValue()).booleanValue()) {

        } else if (mc.timer.timerSpeed == 2.1078f) {
            mc.timer.timerSpeed = 1.0f;
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        int last = mc.thePlayer.inventory.currentItem;
        mc.thePlayer.inventory.currentItem = this.slot;

        if (data != null) {
            if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld,
                    mc.thePlayer.getCurrentEquippedItem(), this.data.getBlockPos(), this.data.getEnumFacing(),
                    getVec3(this.data.getBlockPos(), this.data.getEnumFacing()))) {
                if (mc.thePlayer.inventory.getStackInSlot(mc.thePlayer.inventory.currentItem).getItem() != null
                        && (mc.thePlayer.inventory.getStackInSlot(mc.thePlayer.inventory.currentItem)
                        .getItem() instanceof ItemBlock)
                        && !mc.isSingleplayer()) {
                    ItemBlock itemblock = (ItemBlock) mc.thePlayer.inventory
                            .getStackInSlot(mc.thePlayer.inventory.currentItem).getItem();
                }
                mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
            }
            //袋子月大神写的离谱bug，物品栏会多出一格
            //mc.thePlayer.inventory.currentItem = last;
        }
    }

    @Override
    public void onEnable() {
        this.data = null;
        this.slot = -1;
        rotation.setYaw(999.0f);
        rotation.setPitch(999.0f);
        this.towerTick = 0;
    }

    @Override
    public void onDisable() {
        rotation.setYaw(999.0f);
        rotation.setPitch(999.0f);
        mc.timer.timerSpeed = 1.0f;
    }

    private int getBlocksCount() {
        int result = 0;
        int i = 9;
        while (i < 45) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (isScaffoldBlock(stack)) {
                result += stack.stackSize;
            }
            ++i;
        }
        return result;
    }


    public static boolean isScaffoldBlock(ItemStack itemStack) {
        if (itemStack == null)
            return false;

        if (itemStack.stackSize <= 0)
            return false;

        if (!(itemStack.getItem() instanceof ItemBlock))
            return false;

        ItemBlock itemBlock = (ItemBlock) itemStack.getItem();

        // whitelist
        if (itemBlock.getBlock() == Blocks.glass)
            return true;

        if (invalidBlocks.contains(Block.getBlockFromItem(itemStack.getItem()))) return false;

        // only fullblock
        return itemBlock.getBlock().isFullBlock();
    }

    @EventTarget
    public void on2D(EventRender2D event) {
        if (!Client.instance.getModuleManager().getModuleByClass(HUD.class).isEnabled())
            return;
        ScaledResolution sr = new ScaledResolution(mc);
        int width = sr.getScaledWidth();
        int height = sr.getScaledHeight();
        int middleX = width / 2;
        int middleY = height / 2;
        FontLoaders.J18.drawString(String.valueOf(getBlockCount() + getallBlockCount()),
                middleX + 10
                        - FontLoaders.J18.getStringWidth(String.valueOf(getBlockCount() + getallBlockCount())) / 2,
                middleY + 21, new Color(255, 255, 255).getRGB());
        GL11.glPushMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        if (this.mc.theWorld != null) {
            GlStateManager.pushMatrix();
            GlStateManager.rotate(-30.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(165.0f, 1.0f, 0.0f, 0.0f);

            GlStateManager.enableLighting();
            GlStateManager.enableLight(0);
            GlStateManager.enableLight(1);
            GlStateManager.enableColorMaterial();
            GlStateManager.colorMaterial(1032, 5634);
            final float n = 0.4f;
            final float n2 = 0.6f;
            final float n3 = 0.0f;
            GL11.glLight(16384, 4611, setColorBuffer(LIGHT0_POS.xCoord, LIGHT0_POS.yCoord, LIGHT0_POS.zCoord, 0.0));
            GL11.glLight(16384, 4609, setColorBuffer(n2, n2, n2, 1.0f));
            GL11.glLight(16384, 4608, setColorBuffer(0.0f, 0.0f, 0.0f, 1.0f));
            GL11.glLight(16384, 4610, setColorBuffer(n3, n3, n3, 1.0f));
            GL11.glLight(16385, 4611, setColorBuffer(LIGHT1_POS.xCoord, LIGHT1_POS.yCoord, LIGHT1_POS.zCoord, 0.0));
            GL11.glLight(16385, 4609, setColorBuffer(n2, n2, n2, 1.0f));
            GL11.glLight(16385, 4608, setColorBuffer(0.0f, 0.0f, 0.0f, 1.0f));
            GL11.glLight(16385, 4610, setColorBuffer(n3, n3, n3, 1.0f));
            GlStateManager.shadeModel(7424);
            GL11.glLightModel(2899, setColorBuffer(n, n, n, 1.0f));

            GlStateManager.popMatrix();
        }
        GlStateManager.pushMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.clear(256);
        this.mc.getRenderItem().zLevel = -150.0f;
        final RenderItem renderItem = this.mc.getRenderItem();
        renderItem.renderItemAndEffectIntoGUI(currentblock, width / 2 - 20, height / 2 + 16);
        this.mc.getRenderItem().zLevel = 0.0f;
        GlStateManager.disableBlend();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
        GL11.glPopMatrix();
    }

    public static Vec3 getVec3(BlockPos pos, EnumFacing face) {
        double x = (double) pos.getX() + 0.5;
        double y = (double) pos.getY() + 0.5;
        double z = (double) pos.getZ() + 0.5;
        if (face == EnumFacing.UP || face == EnumFacing.DOWN) {
            x += randomNumber((double) 0.3, (double) -0.3);
            z += randomNumber((double) 0.3, (double) -0.3);
        } else {
            y += randomNumber((double) 0.3, (double) -0.3);
        }
        if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
            z += randomNumber((double) 0.3, (double) -0.3);
        }
        if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
            x += randomNumber((double) 0.3, (double) -0.3);
        }
        return new Vec3(x, y, z);
    }

    public static int getBlockSlot() {
        for (int i = 0; i < 9; ++i) {
            if (!mc.thePlayer.inventoryContainer.getSlot((int) (i + 36)).getHasStack()
                    || !(mc.thePlayer.inventoryContainer.getSlot((int) (i + 36)).getStack()
                    .getItem() instanceof ItemBlock))
                continue;
            return i;
        }
        return -1;
    }

    private BlockData getBlockData(BlockPos pos) {
        if (this.isPosSolid(pos.add(0, -1, 0))) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos.add(-1, 0, 0))) {
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos.add(1, 0, 0))) {
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos.add(0, 0, 1))) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos.add(0, 0, -1))) {
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos1 = pos.add(-1, 0, 0);
        if (this.isPosSolid(pos1.add(0, -1, 0))) {
            return new BlockData(pos1.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos1.add(-1, 0, 0))) {
            return new BlockData(pos1.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos1.add(1, 0, 0))) {
            return new BlockData(pos1.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos1.add(0, 0, 1))) {
            return new BlockData(pos1.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos1.add(0, 0, -1))) {
            return new BlockData(pos1.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos2 = pos.add(1, 0, 0);
        if (this.isPosSolid(pos2.add(0, -1, 0))) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos2.add(-1, 0, 0))) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos2.add(1, 0, 0))) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos2.add(0, 0, 1))) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos2.add(0, 0, -1))) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos3 = pos.add(0, 0, 1);
        if (this.isPosSolid(pos3.add(0, -1, 0))) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos3.add(-1, 0, 0))) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos3.add(1, 0, 0))) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos3.add(0, 0, 1))) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos3.add(0, 0, -1))) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos4 = pos.add(0, 0, -1);
        if (this.isPosSolid(pos4.add(0, -1, 0))) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos4.add(-1, 0, 0))) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos4.add(1, 0, 0))) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos4.add(0, 0, 1))) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos4.add(0, 0, -1))) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos19 = pos.add(-2, 0, 0);
        if (this.isPosSolid(pos1.add(0, -1, 0))) {
            return new BlockData(pos1.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos1.add(-1, 0, 0))) {
            return new BlockData(pos1.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos1.add(1, 0, 0))) {
            return new BlockData(pos1.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos1.add(0, 0, 1))) {
            return new BlockData(pos1.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos1.add(0, 0, -1))) {
            return new BlockData(pos1.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos29 = pos.add(2, 0, 0);
        if (this.isPosSolid(pos2.add(0, -1, 0))) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos2.add(-1, 0, 0))) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos2.add(1, 0, 0))) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos2.add(0, 0, 1))) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos2.add(0, 0, -1))) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos39 = pos.add(0, 0, 2);
        if (this.isPosSolid(pos3.add(0, -1, 0))) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos3.add(-1, 0, 0))) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos3.add(1, 0, 0))) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos3.add(0, 0, 1))) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos3.add(0, 0, -1))) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos49 = pos.add(0, 0, -2);
        if (this.isPosSolid(pos4.add(0, -1, 0))) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos4.add(-1, 0, 0))) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos4.add(1, 0, 0))) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos4.add(0, 0, 1))) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos4.add(0, 0, -1))) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos5 = pos.add(0, -1, 0);
        if (this.isPosSolid(pos5.add(0, -1, 0))) {
            return new BlockData(pos5.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos5.add(-1, 0, 0))) {
            return new BlockData(pos5.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos5.add(1, 0, 0))) {
            return new BlockData(pos5.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos5.add(0, 0, 1))) {
            return new BlockData(pos5.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos5.add(0, 0, -1))) {
            return new BlockData(pos5.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos6 = pos5.add(1, 0, 0);
        if (this.isPosSolid(pos6.add(0, -1, 0))) {
            return new BlockData(pos6.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos6.add(-1, 0, 0))) {
            return new BlockData(pos6.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos6.add(1, 0, 0))) {
            return new BlockData(pos6.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos6.add(0, 0, 1))) {
            return new BlockData(pos6.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos6.add(0, 0, -1))) {
            return new BlockData(pos6.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos7 = pos5.add(-1, 0, 0);
        if (this.isPosSolid(pos7.add(0, -1, 0))) {
            return new BlockData(pos7.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos7.add(-1, 0, 0))) {
            return new BlockData(pos7.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos7.add(1, 0, 0))) {
            return new BlockData(pos7.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos7.add(0, 0, 1))) {
            return new BlockData(pos7.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos7.add(0, 0, -1))) {
            return new BlockData(pos7.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos8 = pos5.add(0, 0, 1);
        if (this.isPosSolid(pos8.add(0, -1, 0))) {
            return new BlockData(pos8.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos8.add(-1, 0, 0))) {
            return new BlockData(pos8.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos8.add(1, 0, 0))) {
            return new BlockData(pos8.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos8.add(0, 0, 1))) {
            return new BlockData(pos8.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos8.add(0, 0, -1))) {
            return new BlockData(pos8.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos9 = pos5.add(0, 0, -1);
        if (this.isPosSolid(pos9.add(0, -1, 0))) {
            return new BlockData(pos9.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos9.add(-1, 0, 0))) {
            return new BlockData(pos9.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos9.add(1, 0, 0))) {
            return new BlockData(pos9.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos9.add(0, 0, 1))) {
            return new BlockData(pos9.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos9.add(0, 0, -1))) {
            return new BlockData(pos9.add(0, 0, -1), EnumFacing.SOUTH);
        }
        return null;
    }

    private boolean isPosSolid(BlockPos pos) {
        Block block = mc.theWorld.getBlockState(pos).getBlock();
        return (block.getMaterial().isSolid() || !block.isTranslucent() || block.isVisuallyOpaque()
                || block instanceof BlockLadder || block instanceof BlockCarpet || block instanceof BlockSnow
                || block instanceof BlockSkull) && !block.getMaterial().isLiquid()
                && !(block instanceof BlockContainer);
    }

    public int getBlockCount() {
        int n = 0;
        int i = 36;
        while (i < 45) {
            if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack stack = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                final Item item = stack.getItem();
                if (stack.getItem() instanceof ItemBlock && this.isValid(item)) {
                    n += stack.stackSize;
                }
            }
            ++i;
        }
        return n;
    }

    public int getallBlockCount() {
        int n = 0;
        int i = 0;
        while (i < 36) {
            if (this.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack stack = this.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                final Item item = stack.getItem();
                if (stack.getItem() instanceof ItemBlock && this.isValid(item)) {
                    n += stack.stackSize;
                }
            }
            ++i;
        }
        return n;
    }

    private boolean isValid(final Item item) {
        return item instanceof ItemBlock && !invalidBlocks.contains(((ItemBlock) item).getBlock());
    }

    public void swap(int slot1, int hotbarSlot) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot1, hotbarSlot, 2, mc.thePlayer);
    }

    void getBlock(int hotbarSlot) {
        for (int i = 9; i < 45; ++i) {
            Minecraft var10000 = mc;
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()
                    && (mc.currentScreen == null || mc.currentScreen instanceof GuiInventory)) {
                var10000 = mc;
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (is.getItem() instanceof ItemBlock) {
                    ItemBlock block = (ItemBlock) is.getItem();
                    if (isValid(block)) {
                        if (36 + hotbarSlot != i) {
                            this.swap(i, hotbarSlot);
                        }
                        break;
                    }
                }
            }
        }

    }

    int getBestSpoofSlot() {
        int spoofSlot = 5;

        for (int i = 36; i < 45; ++i) {
            if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                spoofSlot = i - 36;
                break;
            }
        }

        return spoofSlot;
    }

    public boolean isAirBlock(Block block) {
        return block.getMaterial().isReplaceable()
                && (!(block instanceof BlockSnow) || block.getBlockBoundsMaxY() <= 0.125);
    }

    public static double randomNumber(double max, double min) {
        return Math.random() * (max - min) + min;
    }
    public static boolean getSprint(){
        return sprint.getValue();
    }
    private static class BlockData {
        private Vec3 vec;
        private final BlockPos pos;
        private final EnumFacing facing;

        public BlockData(final BlockPos pos, final EnumFacing facing) {
            this.pos = pos;
            this.facing = facing;
        }

        public Vec3 getVec() {
            return this.vec;
        }

        public void setVec(final Vec3 vec) {
            this.vec = vec;
        }

        public BlockPos getBlockPos() {
            return this.pos;
        }

        public EnumFacing getEnumFacing() {
            return this.facing;
        }
    }

    private static FloatBuffer setColorBuffer(final double p_setColorBuffer_0_, final double p_setColorBuffer_2_,
                                              final double p_setColorBuffer_4_, final double p_setColorBuffer_6_) {
        return setColorBuffer((float) p_setColorBuffer_0_, (float) p_setColorBuffer_2_, (float) p_setColorBuffer_4_,
                (float) p_setColorBuffer_6_);
    }

    private static FloatBuffer setColorBuffer(final float p_setColorBuffer_0_, final float p_setColorBuffer_1_,
                                              final float p_setColorBuffer_2_, final float p_setColorBuffer_3_) {
        colorBuffer.clear();
        colorBuffer.put(p_setColorBuffer_0_).put(p_setColorBuffer_1_).put(p_setColorBuffer_2_).put(p_setColorBuffer_3_);
        colorBuffer.flip();
        return colorBuffer;
    }

    static {
        colorBuffer = GLAllocation.createDirectFloatBuffer(16);
        LIGHT0_POS = new Vec3(0.20000000298023224, 1.0, -0.699999988079071).normalize();
        LIGHT1_POS = new Vec3(-0.20000000298023224, 1.0, 0.699999988079071).normalize();
    }
}

