package cn.cubegarden.whitelistmirai;

import com.xbaimiao.mirai.config.WebSocketBotConfig;
import com.xbaimiao.mirai.event.group.GroupMessageEvent;
import com.xbaimiao.mirai.packet.impl.websocket.WebSocketBot;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public final class WhiteListMirai extends JavaPlugin {

    private static WebSocketBotConfig webSocketBotConfig;
    public static WebSocketBot bot;
    public static List<Long> adminList = new ArrayList<>();
    public static WhiteListMirai instance;

    public WhiteListMirai() {
        instance = this;
    }

    public static WhiteListMirai getInstance() {
        return instance;
    }

    public void loadConfig() {
        adminList = getConfig().getLongList("adminList");
        webSocketBotConfig = new WebSocketBotConfig(getConfig().getString("url"), getConfig().getLong("qq"), getConfig().getString("authKey"));
    }

    @Override
    public void onEnable() {
        getLogger().info("WhiteListMirai已加载！");
        saveDefaultConfig();
        loadConfig();
        bot = new WebSocketBot(webSocketBotConfig).connect();
        bot.getEventChancel().registerListener(this);
    }

    @Subscribe
    public void event(GroupMessageEvent event) {
        System.out.println(event.getPlainText());
        // 如果发言人不在管理列表 不执行操作
        if (!adminList.contains(event.getSender().getId())) {
            return;
        }
        String message = event.getPlainText();
        if (message.startsWith("cmd /")) {
            Bukkit.getScheduler().runTask(this, () -> {
                Bukkit.dispatchCommand(new QQCommandSender(Bukkit.getServer(), event.getGroup()), message.substring(5));
            });
        }
    }

}
