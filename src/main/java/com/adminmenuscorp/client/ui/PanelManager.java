package com.adminmenuscorp.client.ui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages floating panels in the admin menu interface.
 * Handles panel initialization, rendering, and mouse interactions.
 */
public class PanelManager {
    // Panel dimensions and positioning constants
    private static final int PANEL_WIDTH = 200;
    private static final int PANEL_HEIGHT = 200;
    private static final int PANEL_SPACING = 220;
    private static final int BUTTON_WIDTH = 120;
    private static final int BUTTON_HEIGHT = 20;
    private static final int BUTTON_SPACING = 25;
    private static final int SUB_PANEL_WIDTH = 160;
    private static final int SUB_PANEL_HEIGHT = 140;

    // Player names - could be made configurable
    private static final String PLAYER_NAME = "Player1";

    public static final List<FloatingPanel> panels = new ArrayList<>();

    /**
     * Initializes all floating panels with their respective components.
     * Clears existing panels and creates new ones for inventory, logs, and settings.
     */
    public static void initPanels() {
        panels.clear();

        // Create main panels
        FloatingPanel inventoryPanel = createInventoryPanel();
        FloatingPanel logsPanel = createLogsPanel();
        FloatingPanel settingsPanel = createSettingsPanel();

        // Add panels to the manager
        panels.add(inventoryPanel);
        panels.add(logsPanel);
        panels.add(settingsPanel);
    }

    /**
     * Creates the inventory panel with player management buttons.
     */
    private static FloatingPanel createInventoryPanel() {
        FloatingPanel panel = new FloatingPanel("InV", 10, 10, PANEL_WIDTH, PANEL_HEIGHT);

        Button playerButton = createPlayerButton();
        panel.addButton(playerButton);

        return panel;
    }

    /**
     * Creates the logs panel with log entry functionality.
     */
    private static FloatingPanel createLogsPanel() {
        FloatingPanel panel = new FloatingPanel("Logs", 10 + PANEL_SPACING, 10, PANEL_WIDTH, PANEL_HEIGHT);

        // Create button with relative position (relative to panel origin)
        int buttonX = (PANEL_WIDTH - BUTTON_WIDTH) / 2; // Centered horizontally within panel
        int buttonY = 35; // Positioned vertically within panel

        Button logButton = Button.builder(
            Component.literal("Log Entry"),
            b -> { /* TODO: Implement log entry functionality */ }
        )
        .pos(buttonX, buttonY)
        .size(BUTTON_WIDTH, BUTTON_HEIGHT)
        .build();

        panel.addButton(logButton);
        return panel;
    }

    /**
     * Creates the settings panel with configuration options.
     */
    private static FloatingPanel createSettingsPanel() {
        int panelX = 10 + (PANEL_SPACING * 2);
        int panelY = 10;
        FloatingPanel panel = new FloatingPanel("Settings", panelX, panelY, PANEL_WIDTH, PANEL_HEIGHT);

        // Create slider with relative position (relative to panel origin)
        int sliderWidth = 140;
        int sliderHeight = 20;
        int relativeX = (PANEL_WIDTH - sliderWidth) / 2; // Centered horizontally within panel
        int relativeY = 35; // Positioned vertically within panel

        AbstractSliderButton scaleSlider = new AbstractSliderButton(relativeX, relativeY, sliderWidth, sliderHeight, Component.literal("Scale"), HeadMenu.getScale()) {
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

        panel.addWidget(scaleSlider);

        return panel;
    }

    /**
     * Creates a button that opens a player management sub-panel.
     */
    private static Button createPlayerButton() {
        // Create button with relative position (relative to panel origin)
        int buttonX = (PANEL_WIDTH - BUTTON_WIDTH) / 2; // Centered horizontally within panel
        int buttonY = 35; // Positioned vertically within panel

        return Button.builder(
            Component.literal(PLAYER_NAME),
            b -> createAndShowPlayerSubPanel()
        )
        .pos(buttonX, buttonY)
        .size(BUTTON_WIDTH, BUTTON_HEIGHT)
        .build();
    }

    /**
     * Creates and displays a sub-panel with player-specific actions.
     */
    private static void createAndShowPlayerSubPanel() {
        FloatingPanel subPanel = new FloatingPanel(
            "Player-Name 1",
            400, 100,
            SUB_PANEL_WIDTH, SUB_PANEL_HEIGHT
        );

        // Add player action buttons
        addPlayerActionButton(subPanel, "Inventory", "/view inv " + PLAYER_NAME, 0);
        addPlayerActionButton(subPanel, "Ender Chest", "/view echest " + PLAYER_NAME, 1);
        addPlayerActionButton(subPanel, "Backpack", "/view backpack " + PLAYER_NAME, 2);
        addPlayerActionButton(subPanel, "Curios", "/view curios " + PLAYER_NAME, 3);
        addPlayerActionButton(subPanel, "CuriosCosmetic", "/view curioscosmetic " + PLAYER_NAME, 4);

        panels.add(subPanel);
    }

    /**
     * Adds a player action button to the specified panel.
     *
     * @param panel The panel to add the button to
     * @param buttonText The text to display on the button
     * @param command The command to execute when clicked
     * @param position The vertical position index (0-based)
     */
    private static void addPlayerActionButton(FloatingPanel panel, String buttonText, String command, int position) {
        // Create button with relative position (relative to sub-panel origin)
        int buttonX = (SUB_PANEL_WIDTH - BUTTON_WIDTH) / 2; // Centered horizontally within sub-panel
        int buttonY = 25 + (position * BUTTON_SPACING); // Positioned vertically within sub-panel

        Button button = Button.builder(
            Component.literal(buttonText),
            b -> Minecraft.getInstance().player.connection.sendCommand(command)
        )
        .pos(buttonX, buttonY)
        .size(BUTTON_WIDTH, BUTTON_HEIGHT)
        .build();

        panel.addButton(button);
    }



    public static void renderPanels(GuiGraphics graphics, int mouseX, int mouseY) {
        for (FloatingPanel panel : panels) {
            panel.render(graphics, mouseX, mouseY, 0);
        }
    }

    public static boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Process panels in reverse order so the topmost panel gets priority
        for (int i = panels.size() - 1; i >= 0; i--) {
            FloatingPanel panel = panels.get(i);
            // Check if click is within panel bounds first
            if (mouseX >= panel.x && mouseX <= panel.x + panel.width &&
                mouseY >= panel.y && mouseY <= panel.y + panel.height) {
                if (panel.mouseClicked(mouseX, mouseY, button)) return true;
            }
        }
        return false;
    }

    public static boolean mouseReleased(double mouseX, double mouseY, int button) {
        // Process panels in reverse order so the topmost panel gets priority
        for (int i = panels.size() - 1; i >= 0; i--) {
            FloatingPanel panel = panels.get(i);
            if (panel.mouseReleased(mouseX, mouseY, button)) return true;
        }
        return false;
    }

    public static boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        // Process panels in reverse order so the topmost panel gets priority
        for (int i = panels.size() - 1; i >= 0; i--) {
            FloatingPanel panel = panels.get(i);
            // Only process drag if the panel is currently being dragged
            if (panel.dragging || (mouseX >= panel.x && mouseX <= panel.x + panel.width &&
                                  mouseY >= panel.y && mouseY <= panel.y + panel.height)) {
                if (panel.mouseDragged(mouseX, mouseY, button, dx, dy)) return true;
            }
        }
        return false;
    }
}
