package org.auioc.mods.ahutils.server.addrlimiter;

import static org.auioc.mods.ahutils.utils.game.TextUtils.getStringText;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.google.gson.Gson;
import org.auioc.mods.ahutils.server.config.ServerConfig;
import org.auioc.mods.ahutils.utils.game.TextUtils;
import org.auioc.mods.ahutils.utils.java.NetUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;

public class AddrManager {

    private static AddrManager instance;

    private AddrManager() {}

    public static AddrManager getInstance() {
        if (AddrManager.instance == null) {
            AddrManager.instance = new AddrManager();
        }
        return AddrManager.instance;
    }

    private final Map<String, List<UUID>> map = Collections.synchronizedMap(new HashMap<String, List<UUID>>());
    private static final int maxPlayersPreAddr = ServerConfig.MaxPlayerPreAddr.get();
    private static final boolean bypassLocalAddress = ServerConfig.BypassLocalAddress.get();
    private static final boolean bypassLanAddress = ServerConfig.BypassLanAddress.get();

    public int getmaxPlayersPreAddr() {
        return maxPlayersPreAddr;
    }

    protected void add(String addr, UUID uuid) {
        List<UUID> list = this.map.getOrDefault(addr, new ArrayList<UUID>());
        list.add(uuid);
        this.map.put(addr, list);
    }

    protected void remove(String addr, UUID uuid) {
        if (this.map.containsKey(addr)) {
            List<UUID> list = this.map.get(addr);
            if (list.size() <= 1) {
                this.map.remove(addr);
            } else {
                list.remove(uuid);
                this.map.put(addr, list);
            }
        }
    }

    public boolean check(String addr, UUID uuid) {
        if (this.map.containsKey(addr)) {
            if (bypassLocalAddress && NetUtils.isLocalAddress(addr)) {
                return true;
            }
            if (bypassLanAddress && NetUtils.isLanAddress(addr)) {
                return true;
            }
            if ((this.map.get(addr)).size() > (maxPlayersPreAddr - 1)) {
                return false;
            }
        }
        return true;
    }

    protected void clear() {
        this.map.clear();
    }


    public String toJsonString() {
        return (new Gson()).toJson(this.map);
    }

    public TextComponent toJsonText() {
        return getStringText((new Gson()).toJson(this.map));
    }

    public Component toChatMessage(PlayerList playerList) {
        TextComponent m = getStringText("");

        m.append(getStringText("[AddrLimiter]").withStyle(ChatFormatting.AQUA));
        m.append(getPrefix().append(getI18nText("title")).withStyle(ChatFormatting.DARK_AQUA));

        if (map.isEmpty()) {
            return m.append(getPrefix().append(" ┗ ").append(getI18nText("no_data")).withStyle(ChatFormatting.YELLOW));
        }

        int entryIndex = 0;
        int errorOffline = 0;
        List<UUID> uuidsAll = new ArrayList<UUID>();
        for (Map.Entry<String, List<UUID>> e : this.map.entrySet()) {
            String addr = e.getKey();
            List<UUID> uuids = e.getValue();

            boolean lastEntry = (entryIndex == map.size() - 1);
            entryIndex++;

            TextComponent l = getStringText("\n  " + (lastEntry ? "┗ " : "┣ ") + addr);
            if (NetUtils.isLocalAddress(addr)) {
                l.append(getStringText(" ").append(getI18nText("local_address")).withStyle(ChatFormatting.GRAY));
            } else if (NetUtils.isLanAddress(addr)) {
                l.append(getStringText(" ").append(getI18nText("lan_address")).withStyle(ChatFormatting.GRAY));
            }
            l.append(getStringText(" (" + uuids.size() + ")").withStyle(ChatFormatting.GRAY));

            for (int i = 0; i < uuids.size(); i++) {
                UUID uuid = uuids.get(i);
                uuidsAll.add(uuid);
                ServerPlayer player = playerList.getPlayer(uuid);

                String p = String.format("\n  %s  %s ", (lastEntry ? " " : "┃"), (i == uuids.size() - 1) ? "┗" : "┣");
                if (player != null) {
                    l.append(getStringText(p).append(player.getDisplayName()));
                } else {
                    l.append(getStringText(p).append(getStringText(uuid.toString()).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC)));
                    errorOffline++;
                }
            }

            m.append(l);
        }

        TextComponent e = (TextComponent) getStringText("").withStyle(ChatFormatting.YELLOW);
        if (errorOffline > 0) {
            e.append(getPrefix().append(getI18nText("detected_non_online_players", errorOffline)));
        }
        if (uuidsAll.size() > uuidsAll.stream().distinct().count()) {
            e.append(getPrefix().append(getI18nText("detected_duplicate_players", uuidsAll.size() - uuidsAll.stream().distinct().count())));
        }
        if (!e.getSiblings().isEmpty()) {
            e.append(getPrefix().append(getI18nText("refresh_tip")).withStyle(ChatFormatting.GREEN));
        }
        m.append(e);

        return m;
    }

    private static TranslatableComponent getI18nText(String key) {
        return TextUtils.getI18nText("ahutils.addrlimiter.dump." + key);
    }

    private static TranslatableComponent getI18nText(String key, Object... arguments) {
        return TextUtils.getI18nText("ahutils.addrlimiter.dump." + key, arguments);
    }

    private static TextComponent getPrefix() {
        return getStringText("\n ");
    }

}
