package com.direwolf20.buildinggadgets.common.network.packets;

import com.direwolf20.buildinggadgets.common.config.Config;
import com.direwolf20.buildinggadgets.common.items.GadgetBuilding;
import com.direwolf20.buildinggadgets.common.items.GadgetDestruction;
import com.direwolf20.buildinggadgets.common.items.GadgetExchanger;
import com.direwolf20.buildinggadgets.common.items.AbstractGadget;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketToggleFuzzy {

    public static void encode(PacketToggleFuzzy msg, PacketBuffer buffer) {}
    public static PacketToggleFuzzy decode(PacketBuffer buffer) { return new PacketToggleFuzzy(); }

    public static class Handler {
        public static void handle(final PacketToggleFuzzy msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity player = ctx.get().getSender();
                if (player == null)
                    return;

                ItemStack stack = AbstractGadget.getGadget(player);
                if (stack.getItem() instanceof GadgetExchanger || stack.getItem() instanceof GadgetBuilding
                        || (stack.getItem() instanceof GadgetDestruction && Config.GADGETS.GADGET_DESTRUCTION.nonFuzzyEnabled.get()))
                    AbstractGadget.toggleFuzzy(player, stack);
            });

            ctx.get().setPacketHandled(true);
        }
    }
}