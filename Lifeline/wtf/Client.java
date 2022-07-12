/*
 * Decompiled with CFR 0_132.
 */
package Lifeline.wtf;

import Lifeline.wtf.command.CommandManager;
import Lifeline.wtf.events.EventManager;
import Lifeline.wtf.gui.login.AltManager;
import Lifeline.wtf.gui.notifications.NotificationManager;
import Lifeline.wtf.management.FileManager;
import Lifeline.wtf.management.FriendManager;
import Lifeline.wtf.module.Module;
import Lifeline.wtf.module.ModuleManager;
import Lifeline.wtf.module.modules.visual.UI.TabUI;
import Lifeline.wtf.module.value.Value;
import org.lwjgl.opengl.Display;

import java.awt.*;

public class Client {
    public static final String name = "Lifeline";
    public static final String version = "2.0";
    public static final String dev = "Lifeline Devs Group";
    public static boolean publicMode = false;
    public static Client instance = new Client();
    private static ModuleManager modulemanager;
    private CommandManager commandmanager;
    private AltManager altmanager;
    private FriendManager friendmanager;
    private final NotificationManager notificationManager = new NotificationManager();
    private TabUI tabui;
    public static long playTimeStart = 0;
    //public static ResourceLocation CLIENT_CAPE = new ResourceLocation("ETB/cape.png");

    public void initiate() {
        this.commandmanager = new CommandManager();
        this.commandmanager.init();
        this.friendmanager = new FriendManager();
        this.friendmanager.init();
        EventManager.register(this);
        this.modulemanager = new ModuleManager();
        this.modulemanager.init();
        this.tabui = new TabUI();
        this.tabui.init();
        this.altmanager = new AltManager();
        AltManager.init();
        AltManager.setupAlts();
        FileManager.init();
        Display.setTitle(name + " " + version + " " + "By " + dev);
    }

    public static ModuleManager getModuleManager() {
        return modulemanager;
    }

    public CommandManager getCommandManager() {
        return this.commandmanager;
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public AltManager getAltManager() {
        return this.altmanager;
    }
    public final Color getClientColor() {
        return new Color(236, 133, 209);
    }
    public final Color getAlternateClientColor() {
        return new Color(28, 167, 222);
    }

    public void shutDown() {
        String values = "";
        instance.getModuleManager();
        for (Module m : ModuleManager.getModules()) {
            for (Value v : m.getValues()) {
                values = String.valueOf(values) + String.format("%s:%s:%s%s", m.getName(), v.getName(), v.getValue(), System.lineSeparator());
            }
        }
        FileManager.save("Values.txt", values, false);
        String enabled = "";
        instance.getModuleManager();
        for (Module m : ModuleManager.getModules()) {
            if (!m.isEnabled()) continue;
            enabled = String.valueOf(enabled) + String.format("%s%s", m.getName(), System.lineSeparator());
        }
        FileManager.save("Enabled.txt", enabled, false);
    }
}

