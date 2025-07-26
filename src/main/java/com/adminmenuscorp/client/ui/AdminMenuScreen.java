package com.adminmenuscorp.client.ui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class AdminMenuScreen extends Screen {
    public AdminMenuScreen() {
        super(Component.literal("Admin Menu"));
    }

    @Override
    protected void init() {
        super.init();
        PanelManager.initPanels();
    }

    @Override
    public void onClose() {
        SettingsStorage.savePositions(PanelManager.panels);
        super.onClose();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.renderBackground(graphics);
        graphics.pose().pushPose();
        float scale = HeadMenu.getScale();
        graphics.pose().scale(scale, scale, 1.0f);
        PanelManager.renderPanels(graphics, (int)(mouseX / scale), (int)(mouseY / scale));
        graphics.pose().popPose();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return PanelManager.mouseClicked(mouseX / HeadMenu.getScale(), mouseY / HeadMenu.getScale(), button)
                || super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return PanelManager.mouseReleased(mouseX / HeadMenu.getScale(), mouseY / HeadMenu.getScale(), button)
                || super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        return PanelManager.mouseDragged(mouseX / HeadMenu.getScale(), mouseY / HeadMenu.getScale(), button, dx, dy)
                || super.mouseDragged(mouseX, mouseY, button, dx, dy);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
