package org.auioc.mods.ahutils.server.command;

import java.util.Collection;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.auioc.mods.ahutils.common.command.argument.DamageSourceArgument;
import org.auioc.mods.ahutils.common.network.PacketHandler;
import org.auioc.mods.ahutils.utils.LogUtil;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.GameProfileArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;

public abstract class ServerCommandHandler {

    public static int triggerClientCrash(CommandContext<CommandSource> ctx, int mode) throws CommandSyntaxException {
        Collection<GameProfile> targets = GameProfileArgument.getGameProfiles(ctx, "targets");
        CommandSource source = ctx.getSource();
        String sourceName = source.getTextName() + ((source.getEntity() != null) ? "(" + source.getEntity().getStringUUID() + ")" : "");

        for (GameProfile gameprofile : targets) {
            ServerPlayerEntity player = source.getServer().getPlayerList().getPlayer(gameprofile.getId());
            PacketHandler.sendTo(player, new org.auioc.mods.ahutils.client.network.TriggerClientCrashPacket(mode));

            LogUtil.warn(
                LogUtil.getMarker("TriggerClientCrash"), String.format(
                    "Send packet with mode %d to %s(%s) by %s",
                    mode, player.getName().getString(), player.getStringUUID(), sourceName
                )
            );
        }

        return Command.SINGLE_SUCCESS;
    }

    public static int hurtEntity(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        Collection<? extends Entity> targets = EntityArgument.getEntities(ctx, "targets");
        DamageSource source = DamageSourceArgument.getDamageSource(ctx, "source");
        float damage = FloatArgumentType.getFloat(ctx, "damage");

        for (Entity entity : targets) {
            LogUtil.info(
                String.format(
                    "Entity %s has been hurt by the hurt command, %s, damage: %f",
                    entity.toString(), source.toString(), damage
                )
            );

            entity.hurt(source, damage);
        }

        return Command.SINGLE_SUCCESS;
    }

}
