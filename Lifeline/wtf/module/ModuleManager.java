/*
 * Decompiled with CFR 0_132.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package Lifeline.wtf.module;

import Lifeline.wtf.events.EventManager;
import Lifeline.wtf.events.EventTarget;
import Lifeline.wtf.events.misc.EventKey;
import Lifeline.wtf.events.rendering.EventRender2D;
import Lifeline.wtf.events.rendering.EventRender3D;
import Lifeline.wtf.management.FileManager;
import Lifeline.wtf.management.Manager;
import Lifeline.wtf.module.modules.combat.*;
import Lifeline.wtf.module.modules.exploit.Disabler;
import Lifeline.wtf.module.modules.move.Timer;
import Lifeline.wtf.module.modules.move.*;
import Lifeline.wtf.module.modules.player.*;
import Lifeline.wtf.module.modules.visual.*;
import Lifeline.wtf.module.modules.world.*;
import Lifeline.wtf.module.value.Mode;
import Lifeline.wtf.module.value.Numbers;
import Lifeline.wtf.module.value.Option;
import Lifeline.wtf.module.value.Value;
import Lifeline.wtf.utils.render.gl.GLUtils;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;

public class ModuleManager
implements Manager {
    public static List<Module> modules = new ArrayList<Module>();
    private boolean enabledNeededMod = true;
    public boolean nicetry = true;
    private Map<String, Module> map = new HashMap<>();
    public boolean loaded = false;

    private final List<Class<? extends Module>> hiddenModules = new ArrayList<>(Arrays.asList(Notifications.class));

    @Override
    public void init() {
        modules.add(new Timer());
        modules.add(new HUD());
        modules.add(new AutoClicker());
        modules.add(new Sprint());
        modules.add(new KillAura());
        modules.add(new Velocity());
        modules.add(new Criticals());
        modules.add(new Speed());
        modules.add(new Longjump());
        modules.add(new AutoArmor());
        modules.add(new Flight());
        modules.add(new AutoHypixel());
        modules.add(new Notifications());
        modules.add(new Disabler());
        modules.add(new Strafe());
        modules.add(new ItemPhysic());
        modules.add(new NoFall());
        modules.add(new ClickGui());
        modules.add(new Animations());
        modules.add(new Scoreboard());
        modules.add(new NoSlow());
        modules.add(new AntiBot());
        modules.add(new Nametags());
        modules.add(new Tracers());
        modules.add(new ESP());
        modules.add(new FastPlace());
        modules.add(new NoRender());
        modules.add(new TargetStrafe());
        modules.add(new Saver());
        modules.add(new FullBright());
        modules.add(new ChestStealer());
        modules.add(new AntiVoid());
        modules.add(new ChinaHat());
        modules.add(new Scaffold());
        modules.add(new SafeWalk());
        modules.add(new Jesus());
        modules.add(new Phase());
        modules.add(new Chams());
        modules.add(new InventoryHUD());
        modules.add(new Blink());
        modules.add(new FastUse());
        modules.add(new AutoTools());
        modules.add(new Xray());
        modules.add(new ChestESP());
        modules.add(new Lagback());
        modules.add(new InvCleaner());
        modules.add(new Step());
        modules.add(new Teleport());
        modules.add(new GuiMove());
        modules.add(new Teams());

        this.readSettings();
        for (Module m : modules) {
            m.makeCommand();
        }

        Collections.sort(modules, (o1, o2) -> {
            int flag = o1.getName().compareTo(o2.getName());
            return flag;
        });


        EventManager.register(this);
        loaded = true;
    }


    public Map<String, Module> getModuleMap() {
        return map;
    }
    public static List<Module> getModules() {
        return modules;
    }
    public Module getModule(String name) {
        return getModuleMap().get(name.toLowerCase());
    }

    public static Module getModuleByClass(Class<? extends Module> cls) {
        for (Module m : modules) {
            if (m.getClass() != cls) continue;
            return m;
        }
        return null;
    }

    public static Module getModuleByName(String name) {
        for (Module m : modules) {
            if (!m.getName().equalsIgnoreCase(name)) continue;
            return m;
        }
        return null;
    }

    public Module getAlias(String name) {
        for (Module f : modules) {
            if (f.getName().equalsIgnoreCase(name)) {
                return f;
            }
            String[] alias = f.getAlias();
            int length = alias.length;
            int i = 0;
            while (i < length) {
                String s = alias[i];
                if (s.equalsIgnoreCase(name)) {
                    return f;
                }
                ++i;
            }
        }
        return null;
    }


    public List<Module> getModulesInType(Category t) {
        ArrayList<Module> output = new ArrayList<Module>();
        for (Module m : modules) {
            if (m.getType() != t) continue;
            output.add(m);
        }
        return output;
    }

    @EventTarget
    private void onKeyPress(EventKey e) {
        for (Module m : modules) {
            if (m.getKey() != e.getKey()) continue;
            m.setEnabled(!m.isEnabled());
        }
    }

    @EventTarget
    private void onGLHack(EventRender3D e) {
        GlStateManager.getFloat(2982, (FloatBuffer) GLUtils.MODELVIEW.clear());
        GlStateManager.getFloat(2983, (FloatBuffer)GLUtils.PROJECTION.clear());
        GlStateManager.glGetInteger(2978, (IntBuffer)GLUtils.VIEWPORT.clear());
    }

    @EventTarget
    private void on2DRender(EventRender2D e) {
        if (this.enabledNeededMod) {
            this.enabledNeededMod = false;
            for (Module m : modules) {
                if (!m.enabledOnStartup) continue;
                m.setEnabled(true);
            }
        }
    }

    private void setKeyBind(String moudleName, int Key){
        Module moudle = ModuleManager.getModuleByName(moudleName);
        if(moudle.getKey() == Keyboard.KEY_NONE){
            moudle.setKey(Key);
        }
    }

    private void readSettings() {
        List<String> binds = FileManager.read("Binds.txt");
        for (String v : binds) {
            String name = v.split(":")[0];
            String bind = v.split(":")[1];
            Module m = ModuleManager.getModuleByName(name);
            if (m == null) continue;
            m.setKey(Keyboard.getKeyIndex((String)bind.toUpperCase()));

            //Initialize the KeyBind
            setKeyBind("ClickGui",Keyboard.KEY_RSHIFT);
        }
        List<String> enabled = FileManager.read("Enabled.txt");
        for (String v : enabled) {
            Module m = ModuleManager.getModuleByName(v);
            if (m == null) continue;
            m.enabledOnStartup = true;
        }
        List<String> vals = FileManager.read("Values.txt");
        for (String v : vals) {
            String name = v.split(":")[0];
            String values = v.split(":")[1];
            Module m = ModuleManager.getModuleByName(name);
            if (m == null) continue;
            for (Value value : m.getValues()) {
                if (!value.getName().equalsIgnoreCase(values)) continue;
                if (value instanceof Option) {
                    value.setValue(Boolean.parseBoolean(v.split(":")[2]));
                    continue;
                }
                if (value instanceof Numbers) {
                    value.setValue(Double.parseDouble(v.split(":")[2]));
                    continue;
                }
                ((Mode)value).setMode(v.split(":")[2]);
            }
        }
    }
}

