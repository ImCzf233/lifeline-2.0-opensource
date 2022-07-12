package Lifeline.wtf.gui;


import Lifeline.wtf.Client;
import Lifeline.wtf.gui.login.AltManager;
import Lifeline.wtf.utils.render.RenderUtil;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;
import Lifeline.wtf.gui.font.CFontRenderer;
import Lifeline.wtf.gui.font.FontLoaders;
import Lifeline.wtf.gui.login.Alt;
import Lifeline.wtf.gui.login.GuiAltManager;
import Lifeline.wtf.utils.ParticleEngine;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public class GuimainMenu extends GuiScreen implements GuiYesNoCallback {


    private GLSLSandboxShader backgroundShader;
    private long initTime = System.currentTimeMillis(); // Initialize as a failsafe
    private static final AtomicInteger field_175373_f = new AtomicInteger(0);
    private static final Logger logger = LogManager.getLogger();
    private static final Random field_175374_h = new Random();
    public static float bracketSlide = 40.0F;
    public static float modeSlide;
    private float updateCounter;
    public static double introTrans;
    private String splashText;
    private GuiButton buttonResetDemo;
    public static boolean originalNames;
    private int panoramaTimer;
    public static double namesFadeOutTimer;
    private DynamicTexture viewportTexture;
    private boolean field_175375_v = true;
    private final Object field_104025_t = new Object();
    private String field_92025_p;
    private String field_146972_A;
    private String field_104024_v;
    private static final ResourceLocation splashTexts = new ResourceLocation("texts/splashes.txt");
    private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[]{new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png")};
    public static final String field_96138_a;
    private int field_92024_r;
    private int field_92023_s;
    private int field_92022_t;
    private int field_92021_u;
    private int field_92020_v;
    private int field_92019_w;
    private ResourceLocation field_110351_G;
    private GuiButton field_175372_K;
    public static float animatedMouseX;
    public static float animatedMouseY;
    public float zoom1 = 1.0F;
    public float zoom2 = 1.0F;
    public float zoom3 = 1.0F;
    public float zoom4 = 1.0F;
    public float zoom5 = 1.0F;
    public ParticleEngine pe = new ParticleEngine();

    public GuimainMenu() {
        this.field_146972_A = field_96138_a;
        this.splashText = "missingno";
        BufferedReader var1 = null;

        try {
            ArrayList var2 = Lists.newArrayList();
            var1 = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(splashTexts).getInputStream(), Charsets.UTF_8));

            String var3;
            while((var3 = var1.readLine()) != null) {
                var3 = var3.trim();
                if (!var3.isEmpty()) {
                    var2.add(var3);
                }
            }

            if (!var2.isEmpty()) {
                do {
                    this.splashText = (String)var2.get(field_175374_h.nextInt(var2.size()));
                } while(this.splashText.hashCode() == 125780783);
            }
        } catch (IOException var12) {
        } finally {
            if (var1 != null) {
                try {
                    var1.close();
                } catch (IOException var11) {
                }
            }

        }

        this.updateCounter = field_175374_h.nextFloat();
        this.field_92025_p = "";
        if (!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.areShadersSupported()) {
            this.field_92025_p = I18n.format("title.oldgl1", new Object[0]);
            this.field_146972_A = I18n.format("title.oldgl2", new Object[0]);
            this.field_104024_v = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
        }
        try {
            this.backgroundShader = new GLSLSandboxShader("/noise.fsh");
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load background shader", e);
        }

    }

    public void updateScreen() {
        ++this.panoramaTimer;
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    }

    public void initGui() {
        this.pe.particles.clear();
        ScaledResolution sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        introTrans = (double)sr.getScaledHeight();
        this.viewportTexture = new DynamicTexture(256, 256);
        this.field_110351_G = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
        Calendar var1 = Calendar.getInstance();
        var1.setTime(new Date());
        if (var1.get(2) + 1 == 11 && var1.get(5) == 9) {
            this.splashText = "Happy birthday, ez!";
        } else if (var1.get(2) + 1 == 6 && var1.get(5) == 1) {
            this.splashText = "Happy birthday, Notch!";
        } else if (var1.get(2) + 1 == 12 && var1.get(5) == 24) {
            this.splashText = "Merry X-mas!";
        } else if (var1.get(2) + 1 == 1 && var1.get(5) == 1) {
            this.splashText = "Happy new year!";
        } else if (var1.get(2) + 1 == 10 && var1.get(5) == 31) {
            this.splashText = "OOoooOOOoooo! Spooky!";
        }

        boolean var2 = true;
        int var3 = this.height / 4 + 48;
        if (this.mc.isDemo()) {
            this.addDemoButtons(var3, 24);
        } else {
            this.addSingleplayerMultiplayerButtons(var3, 24);
        }

        Object var4 = this.field_104025_t;
        synchronized(this.field_104025_t) {
            this.field_92023_s = this.fontRendererObj.getStringWidth(this.field_92025_p);
            this.field_92024_r = this.fontRendererObj.getStringWidth(this.field_146972_A);
            int var5 = Math.max(this.field_92023_s, this.field_92024_r);
            this.field_92022_t = (this.width - var5) / 2;
            this.field_92021_u = 0;
            this.field_92020_v = this.field_92022_t + var5;
            this.field_92019_w = this.field_92021_u + 24;
        }
    }

    private void addSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_) {
    }

    private void addDemoButtons(int p_73972_1_, int p_73972_2_) {
        this.buttonList.add(new GuiButton(11, this.width / 2 - 100, p_73972_1_, I18n.format("menu.playdemo", new Object[0])));
        this.buttonList.add(this.buttonResetDemo = new GuiButton(12, this.width / 2 - 100, p_73972_1_ + p_73972_2_ * 1, I18n.format("menu.resetdemo", new Object[0])));
        ISaveFormat var3 = this.mc.getSaveLoader();
        WorldInfo var4 = var3.getWorldInfo("Demo_World");
        if (var4 == null) {
            this.buttonResetDemo.enabled = false;
        }

    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }

        if (button.id == 5) {
            this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
        }

        if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        }

        if (button.id == 2) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }

        if (button.id == 14 && this.field_175372_K.visible) {
            this.switchToRealms();
        }

        if (button.id == 4) {
            this.mc.shutdown();
        }

        if (button.id == 11) {
            this.mc.launchIntegratedServer("Demo_World", "Demo_World", DemoWorldServer.demoWorldSettings);
        }

        if (button.id == 12) {
            ISaveFormat var2 = this.mc.getSaveLoader();
            WorldInfo var3 = var2.getWorldInfo("Demo_World");
            if (var3 != null) {
                GuiYesNo var4 = GuiSelectWorld.func_152129_a(this, var3.getWorldName(), 12);
                this.mc.displayGuiScreen(var4);
            }
        }

        if (button.id == 1337) {
            this.mc.displayGuiScreen(new GuiAltManager());
        }

    }

    private void switchToRealms() {
        RealmsBridge var1 = new RealmsBridge();
        var1.switchToRealms(this);
    }

    public void confirmClicked(boolean result, int id) {
        if (result && id == 12) {
            ISaveFormat var6 = this.mc.getSaveLoader();
            var6.flushCache();
            var6.deleteWorldDirectory("Demo_World");
            this.mc.displayGuiScreen(this);
        } else if (id == 13) {
            if (result) {
                try {
                    Class var3 = Class.forName("java.awt.Desktop");
                    Object var4 = var3.getMethod("getDesktop").invoke((Object)null);
                    var3.getMethod("browse", URI.class).invoke(var4, new URI(this.field_104024_v));
                } catch (Throwable var5) {
                    logger.error("Couldn't open link", var5);
                }
            }

            this.mc.displayGuiScreen(this);
        }

    }

    public static void Browse(String s) {
        try {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI(s));
                } catch (IOException var2) {
                    var2.printStackTrace();
                }
            }
        } catch (URISyntaxException var3) {
            var3.printStackTrace();
        }

    }

    private void drawPanorama(int p_73970_1_, int p_73970_2_, float p_73970_3_) {
        Tessellator var4 = Tessellator.getInstance();
        WorldRenderer var5 = var4.getWorldRenderer();
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        byte var6 = 8;

        for(int var7 = 0; var7 < var6 * var6; ++var7) {
            GlStateManager.pushMatrix();
            float var8 = ((float)(var7 % var6) / (float)var6 - 0.5F) / 64.0F;
            float var9 = ((float)(var7 / var6) / (float)var6 - 0.5F) / 64.0F;
            float var10 = 0.0F;
            GlStateManager.translate(var8, var9, var10);
            GlStateManager.rotate(MathHelper.sin(((float)this.panoramaTimer + p_73970_3_) / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-((float)this.panoramaTimer + p_73970_3_) * 0.1F, 0.0F, 1.0F, 0.0F);

            for(int var11 = 0; var11 < 6; ++var11) {
                GlStateManager.pushMatrix();
                if (var11 == 1) {
                    GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (var11 == 2) {
                    GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                }

                if (var11 == 3) {
                    GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (var11 == 4) {
                    GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                }

                if (var11 == 5) {
                    GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
                }

                this.mc.getTextureManager().bindTexture(titlePanoramaPaths[var11]);
                var5.startDrawingQuads();
                var5.func_178974_a(16777215, 255 / (var7 + 1));
                float var12 = 0.0F;
                var5.addVertexWithUV(-1.0, -1.0, 1.0, (double)(0.0F + var12), (double)(0.0F + var12));
                var5.addVertexWithUV(1.0, -1.0, 1.0, (double)(1.0F - var12), (double)(0.0F + var12));
                var5.addVertexWithUV(1.0, 1.0, 1.0, (double)(1.0F - var12), (double)(1.0F - var12));
                var5.addVertexWithUV(-1.0, 1.0, 1.0, (double)(0.0F + var12), (double)(1.0F - var12));
                var4.draw();
                GlStateManager.popMatrix();
            }

            GlStateManager.popMatrix();
            GlStateManager.colorMask(true, true, true, false);
        }

        var5.setTranslation(0.0, 0.0, 0.0);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.matrixMode(5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
    }

    private void rotateAndBlurSkybox(float p_73968_1_) {
        this.mc.getTextureManager().bindTexture(this.field_110351_G);
        GL11.glTexParameteri(3553, 10241, 9729);
        GL11.glTexParameteri(3553, 10240, 9729);
        GL11.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, 256, 256);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.colorMask(true, true, true, false);
        Tessellator var2 = Tessellator.getInstance();
        WorldRenderer var3 = var2.getWorldRenderer();
        var3.startDrawingQuads();
        GlStateManager.disableAlpha();
        byte var4 = 3;

        for(int var5 = 0; var5 < var4; ++var5) {
            var3.func_178960_a(1.0F, 1.0F, 1.0F, 1.0F / (float)(var5 + 1));
            int var6 = this.width;
            int var7 = this.height;
            float var8 = (float)(var5 - var4 / 2) / 256.0F;
            var3.addVertexWithUV((double)var6, (double)var7, (double)zLevel, (double)(0.0F + var8), 1.0);
            var3.addVertexWithUV((double)var6, 0.0, (double)zLevel, (double)(1.0F + var8), 1.0);
            var3.addVertexWithUV(0.0, 0.0, (double)zLevel, (double)(1.0F + var8), 0.0);
            var3.addVertexWithUV(0.0, (double)var7, (double)zLevel, (double)(0.0F + var8), 0.0);
        }

        var2.draw();
        GlStateManager.enableAlpha();
        GlStateManager.colorMask(true, true, true, true);
    }

    private void renderSkybox(int p_73971_1_, int p_73971_2_, float p_73971_3_) {
        this.mc.getFramebuffer().unbindFramebuffer();
        GlStateManager.viewport(0, 0, 256, 256);
        this.drawPanorama(p_73971_1_, p_73971_2_, p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        Tessellator var4 = Tessellator.getInstance();
        WorldRenderer var5 = var4.getWorldRenderer();
        var5.startDrawingQuads();
        float var6 = this.width > this.height ? 120.0F / (float)this.width : 120.0F / (float)this.height;
        float var7 = (float)this.height * var6 / 256.0F;
        float var8 = (float)this.width * var6 / 256.0F;
        var5.func_178960_a(1.0F, 1.0F, 1.0F, 1.0F);
        int var9 = this.width;
        int var10 = this.height;
        var5.addVertexWithUV(0.0, (double)var10, (double)zLevel, (double)(0.5F - var7), (double)(0.5F + var8));
        var5.addVertexWithUV((double)var9, (double)var10, (double)zLevel, (double)(0.5F - var7), (double)(0.5F - var8));
        var5.addVertexWithUV((double)var9, 0.0, (double)zLevel, (double)(0.5F + var7), (double)(0.5F - var8));
        var5.addVertexWithUV(0.0, 0.0, (double)zLevel, (double)(0.5F + var7), (double)(0.5F + var8));
        var4.draw();
        initTime = System.currentTimeMillis();
    }



    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.disableCull();
        if (introTrans > 0.0) {
            introTrans -= introTrans / 7.0;
        }
        this.backgroundShader.useShader(mc.displayWidth,mc.displayHeight,mouseX,mouseY,(System.currentTimeMillis() - initTime) / 990f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(-1f, -1f);
        GL11.glVertex2f(-1f, 1f);
        GL11.glVertex2f(1f, 1f);
        GL11.glVertex2f(1f, -1f);

        GL11.glEnd();
        //Unbind shader
         GL20.glUseProgram(0);
        //GlStateManager.enableAlpha();
        Tessellator var4 = Tessellator.getInstance();
        WorldRenderer var5 = var4.getWorldRenderer();
        short var6 = 274;
        int var7 = this.width / 2 - var6 / 2;
        boolean var8 = true;
       // this.mc.getTextureManager().bindTexture(new ResourceLocation("windy/mainmenu/bg.jpg"));
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        ScaledResolution sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        //RenderUtil.drawCustomImage(-mouseX,0,width*2,height,new ResourceLocation("windy/mainmenu/bg.jpg"));
        //drawModalRectWithCustomSizedTexture(-mouseX, -mouseY ,0.0F, 0.0F, 1920.0F, 597.0F, 1920.0F, 1194.0F);

        //drawModalRectWithCustomSizedTexture(-960.0F - animatedMouseX + (float)sr.getScaledWidth(), -9.0F - animatedMouseY / 9.5F + (float)(sr.getScaledHeight() / 19) - 19.0F, 0.0F, 0.0F, 1920.0F, 597.0F, 1920.0F, 1194.0F);
        FontLoaders.GBD30.drawString("", 1.0, 1.0, -1);
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        float offset = (float)(-16 + sr.getScaledWidth() / 2) - 144.5F + 8.0F;
        float height = (float)(sr.getScaledHeight() / 2) + 14.5F - 8.0F + 0.5F;
        FontLoaders.Jello15.drawString("Power By LIFELINE Dev Group", 5.0, (double)(sr.getScaledHeight() - 5 - FontLoaders.GBD30.getHeight() + 1), -1);
        //FontLoaders.Jello15.drawString("Minecraft 1.8.8 / LIFELINE 1.0", (double)((float)(sr.getScaledWidth() - 5) - 0.5F - (float)FontLoaders.GBD30.getStringWidth("Minecraft 1.8.8 / LIFELINE 1.0") + 1.0F), (double)(sr.getScaledHeight() - 5 - FontLoaders.GBD30.getHeight() + 1), -1);
        CFontRenderer font1 = FontLoaders.Jello40;
        FontLoaders.Jello40.drawString("LifeLine Paimon b220710", (double)(this.width / 2 - font1.getStringWidth("LifeLine Paimon b220710") / 2), (double)(height / 2.0F - (float)(font1.getHeight() / 2) + 60.0F), (new Color(255, 255, 255)).getRGB());
        //this.pe.render(animatedMouseX, animatedMouseY);
        GlStateManager.pushMatrix();
        if (this.isMouseHoveringRect1(offset + 4.0F, height + 4.0F, 56.0F, 56.0F, mouseX, mouseY)) {
            if ((double)this.zoom1 < 1.2) {
                this.zoom1 = (float)((double)this.zoom1 + 0.0500001);
            }
        } else if (this.zoom1 > 1.0F) {
            this.zoom1 = (float)((double)this.zoom1 - 0.06666666666666667);
        }

        if (this.zoom1 > 1.0F) {
            GlStateManager.translate(offset + 32.0F, height + 64.0F, 0.0F);
            GlStateManager.scale(Math.min(1.2, (double)this.zoom1), Math.min(1.2, (double)this.zoom1), 1.0);
            GlStateManager.translate(-(offset + 32.0F), -(height + 64.0F), 0.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }

        this.mc.getTextureManager().bindTexture(new ResourceLocation("windy/mainmenu/singleplayer.png"));
        GL11.glTexParameteri(3553, 10241, 9729);
        GL11.glTexParameteri(3553, 10240, 9729);
        drawModalRectWithCustomSizedTexture(offset, height, 0.0F, 0.0F, 64.0F, 64.0F, 64.0F, 64.0F);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        if (this.zoom1 > 1.0F) {
            GlStateManager.translate(offset + 32.0F, height + 64.0F, 0.0F);
            GlStateManager.scale(Math.min(1.0, (double)this.zoom1 - 0.2), Math.min(1.0, (double)this.zoom1 - 0.2), 1.0);
            GlStateManager.translate(-(offset + 32.0F), -(height + 64.0F), 0.0F);
            FontLoaders.GBD30.drawString("Singleplayer", (double)(offset + 32.0F - (float)(FontLoaders.GBD30.getStringWidth("Singleplayer") / 2) + 0.5F), (double)(height + 70.0F + 1.0F - 4.0F), (new Color(0.39215687F, 0.39215687F, 0.39215687F, Math.max(0.0F, Math.min(1.0F, 0.5F + (this.zoom1 - 1.0F) * 2.5F)))).getRGB());
        }

        GlStateManager.popMatrix();
        offset += 61.0F;
        GlStateManager.pushMatrix();
        if (this.isMouseHoveringRect1(offset + 4.0F, height + 4.0F, 56.0F, 56.0F, mouseX, mouseY)) {
            if ((double)this.zoom2 < 1.2) {
                this.zoom2 = (float)((double)this.zoom2 + 0.0500001);
            }
        } else if (this.zoom2 > 1.0F) {
            this.zoom2 = (float)((double)this.zoom2 - 0.06666666666666667);
        }

        if (this.zoom2 > 1.0F) {
            GlStateManager.translate(offset + 32.0F, height + 64.0F, 0.0F);
            GlStateManager.scale(Math.min(1.2, (double)this.zoom2), Math.min(1.2, (double)this.zoom2), 1.0);
            GlStateManager.translate(-(offset + 32.0F), -(height + 64.0F), 0.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }

        this.mc.getTextureManager().bindTexture(new ResourceLocation("windy/mainmenu/multiplayer.png"));
        GL11.glTexParameteri(3553, 10241, 9729);
        GL11.glTexParameteri(3553, 10240, 9729);
        drawModalRectWithCustomSizedTexture(offset, height, 0.0F, 0.0F, 64.0F, 64.0F, 64.0F, 64.0F);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        if (this.zoom2 > 1.0F) {
            GlStateManager.translate(offset + 32.0F, height + 64.0F, 0.0F);
            GlStateManager.scale(Math.min(1.0, (double)this.zoom2 - 0.2), Math.min(1.0, (double)this.zoom2 - 0.2), 1.0);
            GlStateManager.translate(-(offset + 32.0F), -(height + 64.0F), 0.0F);
            FontLoaders.GBD30.drawString("Multiplayer", (double)(offset + 32.0F - (float)(FontLoaders.GBD30.getStringWidth("Multiplayer") / 2) + 0.5F), (double)(height + 70.0F + 1.0F - 4.0F), (new Color(0.39215687F, 0.39215687F, 0.39215687F, Math.max(0.0F, Math.min(1.0F, 0.5F + (this.zoom2 - 1.0F) * 2.5F)))).getRGB());
        }

        GlStateManager.popMatrix();
        offset += 61.0F;
        GlStateManager.pushMatrix();
        if (this.isMouseHoveringRect1(offset + 4.0F, height + 4.0F, 56.0F, 56.0F, mouseX, mouseY)) {
            if ((double)this.zoom3 < 1.2) {
                this.zoom3 = (float)((double)this.zoom3 + 0.0500001);
            }
        } else if (this.zoom3 > 1.0F) {
            this.zoom3 = (float)((double)this.zoom3 - 0.06666666666666667);
        }

        if (this.zoom3 > 1.0F) {
            GlStateManager.translate(offset + 32.0F, height + 64.0F, 0.0F);
            GlStateManager.scale(Math.min(1.2, (double)this.zoom3), Math.min(1.2, (double)this.zoom3), 1.0);
            GlStateManager.translate(-(offset + 32.0F), -(height + 64.0F), 0.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }

        this.mc.getTextureManager().bindTexture(new ResourceLocation("windy/mainmenu/connect.png"));
        GL11.glTexParameteri(3553, 10241, 9729);
        GL11.glTexParameteri(3553, 10240, 9729);
        drawModalRectWithCustomSizedTexture(offset, height, 0.0F, 0.0F, 64.0F, 64.0F, 64.0F, 64.0F);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        if (this.zoom3 > 1.0F) {
            GlStateManager.translate(offset + 32.0F, height + 64.0F, 0.0F);
            GlStateManager.scale(Math.min(1.0, (double)this.zoom3 - 0.2), Math.min(1.0, (double)this.zoom3 - 0.2), 1.0);
            GlStateManager.translate(-(offset + 32.0F), -(height + 64.0F), 0.0F);
            FontLoaders.GBD30.drawString("Quit", (double)(offset + 32.0F - (float)(FontLoaders.GBD30.getStringWidth("Quit") / 2) + 0.5F), (double)(height + 70.0F + 1.0F - 4.0F), (new Color(0.39215687F, 0.39215687F, 0.39215687F, Math.max(0.0F, Math.min(1.0F, 0.5F + (this.zoom3 - 1.0F) * 2.5F)))).getRGB());
        }

        GlStateManager.popMatrix();
        offset += 61.0F;
        GlStateManager.pushMatrix();
        if (this.isMouseHoveringRect1(offset + 4.0F, height + 4.0F, 56.0F, 56.0F, mouseX, mouseY)) {
            if ((double)this.zoom4 < 1.2) {
                this.zoom4 = (float)((double)this.zoom4 + 0.06666666666666667);
            }
        } else if (this.zoom4 > 1.0F) {
            this.zoom4 = (float)((double)this.zoom4 - 0.06666666666666667);
        }

        if (this.zoom4 > 1.0F) {
            GlStateManager.translate(offset + 32.0F, height + 64.0F, 0.0F);
            GlStateManager.scale(Math.min(1.2, (double)this.zoom4), Math.min(1.2, (double)this.zoom4), 1.0);
            GlStateManager.translate(-(offset + 32.0F), -(height + 64.0F), 0.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }

        this.mc.getTextureManager().bindTexture(new ResourceLocation("windy/mainmenu/settings.png"));
        GL11.glTexParameteri(3553, 10241, 9729);
        GL11.glTexParameteri(3553, 10240, 9729);
        drawModalRectWithCustomSizedTexture(offset, height, 0.0F, 0.0F, 64.0F, 64.0F, 64.0F, 64.0F);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        if (this.zoom4 > 1.0F) {
            GlStateManager.translate(offset + 32.0F, height + 64.0F, 0.0F);
            GlStateManager.scale(Math.min(1.0, (double)this.zoom4 - 0.2), Math.min(1.0, (double)this.zoom4 - 0.2), 1.0);
            GlStateManager.translate(-(offset + 32.0F), -(height + 64.0F), 0.0F);
            FontLoaders.GBD30.drawString("Settings", (double)(offset + 32.0F - (float)(FontLoaders.GBD30.getStringWidth("Settings") / 2) + 0.5F), (double)(height + 70.0F + 1.0F - 4.0F), (new Color(0.39215687F, 0.39215687F, 0.39215687F, Math.max(0.0F, Math.min(1.0F, 0.5F + (this.zoom4 - 1.0F) * 2.5F)))).getRGB());
        }

        GlStateManager.popMatrix();
        offset += 61.0F;
        GlStateManager.pushMatrix();
        if (this.isMouseHoveringRect1(offset + 4.0F, height + 4.0F, 56.0F, 56.0F, mouseX, mouseY)) {
            if ((double)this.zoom5 < 1.2) {
                this.zoom5 = (float)((double)this.zoom5 + 0.06666666666666667);
            }
        } else if (this.zoom5 > 1.0F) {
            this.zoom5 = (float)((double)this.zoom5 - 0.06666666666666667);
        }

        if (this.zoom5 > 1.0F) {
            GlStateManager.translate(offset + 32.0F, height + 64.0F, 0.0F);
            GlStateManager.scale(Math.min(1.2, (double)this.zoom5), Math.min(1.2, (double)this.zoom5), 1.0);
            GlStateManager.translate(-(offset + 32.0F), -(height + 64.0F), 0.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }

        this.mc.getTextureManager().bindTexture(new ResourceLocation("windy/mainmenu/altmanager.png"));
        GL11.glTexParameteri(3553, 10241, 9729);
        GL11.glTexParameteri(3553, 10240, 9729);
        drawModalRectWithCustomSizedTexture(offset, height, 0.0F, 0.0F, 64.0F, 64.0F, 64.0F, 64.0F);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        if (this.zoom5 > 1.0F) {
            GlStateManager.translate(offset + 32.0F, height + 64.0F, 0.0F);
            GlStateManager.scale(Math.min(1.0, (double)this.zoom5 - 0.2), Math.min(1.0, (double)this.zoom5 - 0.2), 1.0);
            GlStateManager.translate(-(offset + 32.0F), -(height + 64.0F), 0.0F);
            FontLoaders.GBD30.drawString("Alt Manager", (double)(offset + 32.0F - (float)(FontLoaders.GBD30.getStringWidth("Alt Manager") / 2) + 0.5F), (double)(height + 70.0F + 1.0F - 4.0F), (new Color(0.39215687F, 0.39215687F, 0.39215687F, Math.max(0.0F, Math.min(1.0F, 0.5F + (this.zoom5 - 1.0F) * 2.5F)))).getRGB());
        }

        GlStateManager.popMatrix();
        GlStateManager.enableBlend();
        animatedMouseX = mouseX;
        animatedMouseY = mouseY;

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        Object var4 = this.field_104025_t;
        synchronized(this.field_104025_t) {
            if (this.field_92025_p.length() > 0 && mouseX >= this.field_92022_t && mouseX <= this.field_92020_v && mouseY >= this.field_92021_u && mouseY <= this.field_92019_w) {
                GuiConfirmOpenLink var5 = new GuiConfirmOpenLink(this, this.field_104024_v, 13, true);
                var5.disableSecurityWarning();
                this.mc.displayGuiScreen(var5);
            }
        }

        ScaledResolution sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        if (mouseButton == 0) {
            float offset = (float)(-16 + sr.getScaledWidth() / 2) - 144.5F + 8.0F;
            float height = (float)(sr.getScaledHeight() / 2) + 14.5F - 8.0F;
            if (this.isMouseHoveringRect1(offset + 4.0F, height + 4.0F, 56.0F, 56.0F, mouseX, mouseY)) {
                this.mc.displayGuiScreen(new GuiSelectWorld(this));
            }

            offset += 61.0F;
            if (this.isMouseHoveringRect1(offset + 4.0F, height + 4.0F, 56.0F, 56.0F, mouseX, mouseY)) {
                this.mc.displayGuiScreen(new GuiMultiplayer(this));
            }

            offset += 61.0F;
            if (this.isMouseHoveringRect1(offset + 4.0F, height + 4.0F, 56.0F, 56.0F, mouseX, mouseY)) {
            }

            offset += 61.0F;
            if (this.isMouseHoveringRect1(offset + 4.0F, height + 4.0F, 56.0F, 56.0F, mouseX, mouseY)) {
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
            }

            offset += 61.0F;
            if (this.isMouseHoveringRect1(offset + 4.0F, height + 4.0F, 56.0F, 56.0F, mouseX, mouseY)) {
                Client.instance.getAltManager();

                Alt a;
                for(Iterator var8 = AltManager.getAlts().iterator(); var8.hasNext(); a.slideTrans = 0.0F) {
                    a = (Alt)var8.next();
                    Client.instance.getAltManager();
                    AltManager.selectedAlt = null;
                }

                this.mc.displayGuiScreen(new GuiAltManager());
            }

            offset += 61.0F;
        }

    }

    public boolean isMouseHoveringRect1(float x, float y, float width, float height, int mouseX, int mouseY) {
        return (float)mouseX >= x && (float)mouseY >= y && (float)mouseX <= x + width && (float)mouseY <= y + height;
    }

    public boolean isMouseHoveringRect2(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return (float)mouseX >= x && (float)mouseY >= y && (float)mouseX <= x2 && (float)mouseY <= y2;
    }

    static {
        field_96138_a = "Please click " + EnumChatFormatting.UNDERLINE + "here" + EnumChatFormatting.RESET + " for more information.";
    }
}