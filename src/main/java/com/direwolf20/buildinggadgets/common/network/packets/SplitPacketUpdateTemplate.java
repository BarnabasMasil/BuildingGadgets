package com.direwolf20.buildinggadgets.common.network.packets;

import com.direwolf20.buildinggadgets.client.ClientProxy;
import com.direwolf20.buildinggadgets.common.tainted.save.SaveManager;
import com.direwolf20.buildinggadgets.common.tainted.template.TemplateKey;
import com.direwolf20.buildinggadgets.common.tainted.template.Template;
import com.direwolf20.buildinggadgets.common.tainted.template.TemplateIO;
import com.direwolf20.buildinggadgets.common.util.exceptions.TemplateReadException;
import com.direwolf20.buildinggadgets.common.util.exceptions.TemplateWriteException;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent.Context;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.UUID;
import java.util.function.Supplier;

public final class SplitPacketUpdateTemplate extends UUIDPacket {
    private final Template template;

    public SplitPacketUpdateTemplate(PacketBuffer buffer) {
        super(buffer);
        byte[] bytes = new byte[buffer.readableBytes()];
        buffer.readBytes(bytes);
        try {
            template = TemplateIO.readTemplate(new ByteArrayInputStream(bytes), null);
        } catch (TemplateReadException e) {
            throw new RuntimeException("Failed to read TemplateItem from buffer!", e);
        }
    }

    public SplitPacketUpdateTemplate(UUID id, Template template) {
        super(id);
        this.template = template;
    }

    public void encode(PacketBuffer buffer) {
        super.encode(buffer);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            TemplateIO.writeTemplate(template, stream);
            buffer.writeBytes(stream.toByteArray());
        } catch (TemplateWriteException e) {
            throw new RuntimeException("Failed to write TemplateItem during Packet Encoding!", e);
        }
    }

    public void handle(Supplier<Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            if (contextSupplier.get().getDirection().getReceptionSide() == LogicalSide.CLIENT)
                ClientProxy.CACHE_TEMPLATE_PROVIDER.setTemplate(new TemplateKey(getId()), template);
            else
                SaveManager.INSTANCE.getTemplateProvider().setTemplate(new TemplateKey(getId()), template);
        });
    }
}
