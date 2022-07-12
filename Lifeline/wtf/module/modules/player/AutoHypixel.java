package Lifeline.wtf.module.modules.player;

import Lifeline.wtf.events.EventListener;
import Lifeline.wtf.events.world.ChatReceivedEvent;
import Lifeline.wtf.gui.notification.NoticeNotification;
import Lifeline.wtf.module.Category;
import Lifeline.wtf.module.Module;
import Lifeline.wtf.module.value.Numbers;
import Lifeline.wtf.module.value.Option;
import Lifeline.wtf.utils.ChatUtils;
import Lifeline.wtf.utils.Multithreading;
import net.minecraft.util.StringUtils;

import java.util.concurrent.TimeUnit;

public class AutoHypixel extends Module {

    private final Option<Boolean> autoGG = new Option<Boolean>("AutoGG", "AutoGG",true);
    private final Option<Boolean> autoGGMessage = new Option<Boolean>("AutoGG Message", "AutoGG Message", true);
    private final Option<Boolean> autoPlay = new Option<Boolean>("AutoPlay", "AutoPlay",true);
    private final Numbers<Double> autoPlayDelay = new Numbers<Double>("AutoPlay Delay","AutoPlay Delay", 2.5, 8.0, 2.0, 0.5);
    private final Option<Boolean> autoHubOnBan = new Option<Boolean>("Auto /l on ban", "Auto /l on ban",false);

    public AutoHypixel() {
        super("AutoHypixel", new String[]{}, Category.Player);
        this.addValues(autoGG, autoGGMessage, autoPlay, autoPlayDelay, autoHubOnBan);
    }

    private final EventListener<ChatReceivedEvent> onChatReceived = e -> {
        String message = e.message.getUnformattedText(), strippedMessage = StringUtils.stripControlCodes(message);
        if (autoHubOnBan.getValue() == true && strippedMessage.equals("A player has been removed from your game.")) {
            ChatUtils.send("/lobby");
            NoticeNotification.send("AutoHypixel", "A player in your lobby got banned.",NoticeNotification.Type.WARNING);
        }
        String m = e.message.toString();
        if (m.contains("ClickEvent{action=RUN_COMMAND, value='/play ")) {
            if (autoGG.getValue() == true && !strippedMessage.startsWith("You died!")) {
                ChatUtils.send("/ac " + "GG");
            }
            if (autoPlay.getValue() == true) {
                sendToGame(m.split("action=RUN_COMMAND, value='")[1].split("'}")[0]);
            }
        }
    };

    private void sendToGame(String mode) {
        float delay = autoPlayDelay.getValue().floatValue();
        NoticeNotification.send( "AutoPlay",
                "Sending you to a new game" + (delay > 0 ? " in " + delay + "s" : "") + "!", NoticeNotification.Type.INFO);
        Multithreading.schedule(() -> ChatUtils.send(mode), (long) (delay * 1000), TimeUnit.MILLISECONDS);
    }

}