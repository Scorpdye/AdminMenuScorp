package com.adminmenuscorp.client.ui;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.NbtIo;

import java.io.*;
import java.util.List;

public class SettingsStorage {
    private static final File SETTINGS_FILE = new File("config/admin_menu_settings.dat");

    public static void savePositions(List<FloatingPanel> panels) {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();

        for (FloatingPanel panel : panels) {
            CompoundTag panelTag = new CompoundTag();
            panelTag.putString("title", panel.title);
            panelTag.putInt("x", panel.x);
            panelTag.putInt("y", panel.y);
            panelTag.putBoolean("expanded", panel.expanded);
            list.add(panelTag);
        }

        tag.put("panels", list);
        tag.putFloat("scale", HeadMenu.getScale());

        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(SETTINGS_FILE))) {
            NbtIo.writeCompressed(tag, out); // ✅
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadPositions(List<FloatingPanel> panels) {
        if (!SETTINGS_FILE.exists()) return;

        try (DataInputStream in = new DataInputStream(new FileInputStream(SETTINGS_FILE))) {
            CompoundTag tag = NbtIo.readCompressed(in);
            HeadMenu.setScale(tag.getFloat("scale"));

            ListTag list = tag.getList("panels", 10);
            for (int i = 0; i < list.size() && i < panels.size(); i++) {
                CompoundTag panelTag = list.getCompound(i);
                FloatingPanel panel = panels.get(i);
                panel.x = panelTag.getInt("x");
                panel.y = panelTag.getInt("y");
                panel.expanded = panelTag.getBoolean("expanded");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setScale(float scale) {
        HeadMenu.setScale(scale);
    }
}
