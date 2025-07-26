package com.adminmenuscorp.client.ui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class PanelManager {
    public static final List<FloatingPanel> panels = new ArrayList<>();

    public static void initPanels() {
        panels.clear();

        FloatingPanel invPanel = new FloatingPanel("InV", 10, 10, 200, 200);
        FloatingPanel logsPanel = new FloatingPanel("Logs", 230, 10, 200, 200);
        FloatingPanel settingsPanel = new FloatingPanel("Settings", 460, 10, 200, 200);

        Button playerButton = Button.builder(Component.literal("Player1"), b -> {
            FloatingPanel subPanel = new FloatingPanel("Player-Name 1", 400, 100, 160, 140);
            subPanel.addButton(Button.builder(Component.literal("Inventory"), bb -> Minecraft.getInstance().player.connection.sendCommand("/view inv Player1")).pos(0, 0).size(120, 20).build());
            subPanel.addButton(Button.builder(Component.literal("Ender Chest"), bb -> Minecraft.getInstance().player.connection.sendCommand("/view echest Player1")).pos(0, 25).size(120, 20).build());
            subPanel.addButton(Button.builder(Component.literal("Backpack"), bb -> Minecraft.getInstance().player.connection.sendCommand("/view backpack Player1")).pos(0, 50).size(120, 20).build());
            subPanel.addButton(Button.builder(Component.literal("Curios"), bb -> Minecraft.getInstance().player.connection.sendCommand("/view curios Player1")).pos(0, 75).size(120, 20).build());
            subPanel.addButton(Button.builder(Component.literal("CuriosCosmetic"), bb -> Minecraft.getInstance().player.connection.sendCommand("/view curioscosmetic Player1")).pos(0, 100).size(120, 20).build());
            panels.add(subPanel);
        }).pos(10, 35).size(120, 20).build();
        invPanel.addButton(playerButton);

        Button logButton = Button.builder(Component.literal("Log Entry"), b -> {}).pos(10, 35).size(120, 20).build();
        logsPanel.addButton(logButton);

        AbstractSliderButton scaleSlider = new AbstractSliderButton(20, 40, 140, 20, Component.literal("Scale"), HeadMenu.getScale()) {
            @Override
            protected void updateMessage() {
                setMessage(Component.literal("Scale: " + String.format("%.2f", value)));
            }

            @Override
            protected void applyValue() {
                HeadMenu.setScale((float) value);
                updateMessage();
                SettingsStorage.setScale((float) value);
            }
        };
        settingsPanel.addWidget(scaleSlider);

        panels.add(invPanel);
        panels.add(logsPanel);
        panels.add(settingsPanel);
    }

    public static void renderPanels(GuiGraphics graphics, int mouseX, int mouseY) {
        for (FloatingPanel panel : panels) {
            panel.render(graphics, mouseX, mouseY, 0);
        }
    }

    public static boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (FloatingPanel panel : panels) {
            if (panel.mouseClicked(mouseX, mouseY, button)) return true;
        }
        return false;
    }

    public static boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (FloatingPanel panel : panels) {
            if (panel.mouseReleased(mouseX, mouseY, button)) return true;
        }
        return false;
    }

    public static boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        for (FloatingPanel panel : panels) {
            if (panel.mouseDragged(mouseX, mouseY, button, dx, dy)) return true;
        }
        return false;
    }
}
