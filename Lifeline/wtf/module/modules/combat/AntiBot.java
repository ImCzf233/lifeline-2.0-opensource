/*
 * Decompiled with CFR 0_132.
 */
package Lifeline.wtf.module.modules.combat;

import Lifeline.wtf.events.EventTarget;
import Lifeline.wtf.events.world.EventUpdate;
import Lifeline.wtf.module.Category;
import Lifeline.wtf.module.Module;
import Lifeline.wtf.module.value.Mode;
import Lifeline.wtf.utils.TimeHelper;
import Lifeline.wtf.utils.TimerUtil;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AntiBot extends Module {
    private TimerUtil Packettimer = new TimerUtil();
    public Mode mode;
    public static ArrayList Bots = new ArrayList();
    private TimerUtil timer;
    static int bots;
    private TimeHelper timert = new TimeHelper();
    private static List invalid = new ArrayList();
    private List<Packet> packetList = new CopyOnWriteArrayList();
    public static ArrayList bot = new ArrayList();
    private static ArrayList playersInGame = new ArrayList();
    private static List removed = new ArrayList();
    public TimeHelper lastRemoved = new TimeHelper();
    public TimeHelper timer2 = new TimeHelper();
    public TimeHelper timer3 = new TimeHelper();
    private final HashMap<Packet<?>, Long> packetsMap = new HashMap<>();

    public static List onAirInvalid = new ArrayList();

    public AntiBot() {
        super("AntiBot", new String[]{"nobot","botkiller"}, Category.Combat);
        this.mode = new Mode("Mode", "Mode", AntiMode.values(), AntiMode.Hypixel);

        this.addValues(this.mode);
    }
    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix(this.mode.getValue());
        final List<String> names = new ArrayList<>();
        switch (this.mode.getValue().toString()) {
        case "Hypixel": {
            List playerEntities = mc.theWorld.playerEntities;
            int playerEntitiesSize = playerEntities.size();
            for (int i = 0; i < playerEntitiesSize; ++i) {
                EntityPlayer player = (EntityPlayer)playerEntities.get(i);
                if ((!player.getName().startsWith("\u00a7") || !player.getName().contains("\u00a7c")) && (!this.isEntityBot(player) || player.getDisplayName().getFormattedText().contains("NPC"))) continue;
                mc.theWorld.removeEntity(player);
            }
            break;
        }
        case "Mineplex": {
            for (Entity e :mc.theWorld.getLoadedEntityList()) {
                if (!(e instanceof EntityPlayer)) continue;
                EntityPlayer bot = (EntityPlayer)e;
                if (e.ticksExisted >= 2 || !(bot.getHealth() < 20.0f) || !(bot.getHealth() > 0.0f) || e == mc.thePlayer) continue;
                mc.theWorld.removeEntity(e);
            }
            break;
        }
    }
    }
    
    
    public static boolean isEntityBot(Entity entity) {
        double distance = entity.getDistanceSqToEntity(mc.thePlayer);
        if (!(entity instanceof EntityPlayer)) {
            return false;
        }
        if (mc.getCurrentServerData() == null) {
            return false;
        }
        return mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel") && entity.getDisplayName().getFormattedText().startsWith("\u0e22\u0e07") || !isOnTab(entity) && mc.thePlayer.ticksExisted > 100;
    }

    private static boolean isOnTab(Entity entity) {
        for (NetworkPlayerInfo info : mc.getNetHandler().getPlayerInfoMap()) {
            if (!info.getGameProfile().getName().equals(entity.getName())) continue;
            return true;
        }
        return false;
    }

    enum AntiMode {
    	Hypixel,
        Mineplex;
    }

}