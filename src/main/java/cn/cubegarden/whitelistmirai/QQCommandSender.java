package cn.cubegarden.whitelistmirai;

import com.xbaimiao.mirai.entity.MiraiMessageTransmittable;
import com.xbaimiao.mirai.message.component.collections.ComponentList;
import com.xbaimiao.mirai.message.component.impl.PlainText;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class QQCommandSender implements CommandSender {

    private static int tasked;
    private final Server server;
    private final MiraiMessageTransmittable messageTransmittable;
    private final ArrayList<String> output = new ArrayList<>();

    public QQCommandSender(Server server, MiraiMessageTransmittable messageTransmittable) {
        this.server = server;
        this.messageTransmittable = messageTransmittable;
    }

    private void send() {
        Bukkit.getScheduler().cancelTask(tasked);
        tasked = Bukkit.getScheduler().runTaskLaterAsynchronously(WhiteListMirai.getInstance(), () -> {
            StringBuilder output = new StringBuilder();
            for (String s : this.output) {
                output.append(s.replaceAll("§\\S", ""));
                if (output.indexOf(s) != output.length() - 1) {
                    output.append("\n");
                }
            }
            messageTransmittable.sendMessage(new PlainText(output.toString(), new ComponentList()));
            System.out.println(output);
            this.output.clear();
        }, 4).getTaskId();
    }

    private Optional<ConsoleCommandSender> get() {
        return Optional.ofNullable(this.server.getConsoleSender());
    }

    @Override
    public Server getServer() {
        return this.server;
    }

    @Override
    public String getName() {
        return "CONSOLE";
    }

    @Override
    public void sendMessage(String message) {
        this.output.add(message);
        send();
    }

    @Override
    public void sendMessage(String[] messages) {
        for (String msg : messages) {
            sendMessage(msg);
        }
    }

    public void sendMessage(@Nullable UUID uuid, @NotNull String s) {

    }

    public void sendMessage(@Nullable UUID uuid, @NotNull String[] strings) {

    }

    @Override
    public boolean isPermissionSet(String s) {
        return get().map(c -> c.isPermissionSet(s)).orElse(true);
    }

    @Override
    public boolean isPermissionSet(Permission permission) {
        return get().map(c -> c.isPermissionSet(permission)).orElse(true);
    }

    @Override
    public boolean hasPermission(String s) {
        return get().map(c -> c.hasPermission(s)).orElse(true);
    }

    @Override
    public boolean hasPermission(Permission permission) {
        return get().map(c -> c.hasPermission(permission)).orElse(true);
    }

    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public void setOp(boolean b) {
        throw new UnsupportedOperationException();
    }

    // just throw UnsupportedOperationException - we never use any of these methods
    @Override
    public CommandSender.Spigot spigot() {
        return new CommandSender.Spigot() {
            public void sendMessage(BaseComponent component) {
                QQCommandSender.this.output.add(component.toPlainText());
                QQCommandSender.this.send();
            }

            public void sendMessage(BaseComponent... components) {
                for (BaseComponent baseComponent : components)
                    sendMessage(baseComponent);
            }
        };
    }

    public void sendRawMessage(@Nullable UUID uuid, @NotNull String s) {

    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b, int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeAttachment(PermissionAttachment permissionAttachment) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void recalculatePermissions() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        throw new UnsupportedOperationException();
    }
}
