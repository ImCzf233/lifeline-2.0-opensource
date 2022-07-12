/*
 * Decompiled with CFR 0.136.
 */
package Lifeline.wtf.module.modules.visual;

import Lifeline.wtf.gui.clickgui.GuiClickUI;
import Lifeline.wtf.gui.clickgui.TomoClickGui;
import Lifeline.wtf.module.Category;
import Lifeline.wtf.module.Module;
import Lifeline.wtf.module.value.Mode;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClickGui extends Module {

	private Mode<Enum> mode = new Mode<>("Mode", "Mode", ClickGuiMode.values(), ClickGuiMode.Tomo);



	public ClickGui() {
		super("ClickGui", new String[] { "clickui" }, Category.Visual);
		this.setRemoved(true);
		this.addValues(mode);
	}
	static enum ClickGuiMode{
		Client,Tomo
	}

	public static int memoriseX = 30;
	public static int memoriseY = 30;
	public static int memoriseWheel = 0;
	public static List<Module> memoriseML = new CopyOnWriteArrayList<>();
	public static Category memoriseCatecory = null;

	@Override
	public void onEnable() {
		if (this.mode.getValue() == ClickGuiMode.Client) {
			mc.displayGuiScreen(new GuiClickUI());
			GuiClickUI.setX(memoriseX);
			GuiClickUI.setY(memoriseY);
			GuiClickUI.setWheel(memoriseWheel);
			GuiClickUI.setInSetting(memoriseML);
			if (memoriseCatecory != null)
				GuiClickUI.setCategory(memoriseCatecory);
			this.setEnabled(false);
		}else {
			mc.displayGuiScreen(new TomoClickGui());
			GuiClickUI.setX(memoriseX);
			GuiClickUI.setY(memoriseY);
			GuiClickUI.setWheel(memoriseWheel);
			GuiClickUI.setInSetting(memoriseML);
			if (memoriseCatecory != null)
				GuiClickUI.setCategory(memoriseCatecory);
			this.setEnabled(false);
		}


	}
}
