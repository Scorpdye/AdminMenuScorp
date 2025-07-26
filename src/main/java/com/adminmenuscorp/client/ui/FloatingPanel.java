package com.adminmenuscorp.client.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class FloatingPanel {
    public int x, y, width, height;
    public String title;
    public boolean dragging = false;
    public boolean expanded = true;
    private int dragOffsetX, dragOffsetY;

    private final List<AbstractWidget> widgets = new ArrayList<>();
    private final List<Button> buttons = new ArrayList<>();

    public FloatingPanel(String title, int x, int y, int width, int height) {
        this.title = title;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void addButton(Button button) {
        buttons.add(button);
        widgets.add(button);
    }

    public void addWidget(AbstractWidget widget) {
        widgets.add(widget);
    }

    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        if (!expanded) return;

        graphics.fill(x, y, x + width, y + height, 0x88000000);
        graphics.fill(x, y, x + width, y + 20, 0xFF6A00FF);
        graphics.drawString(Minecraft.getInstance().font, title, x + width / 2 - Minecraft.getInstance().font.width(title) / 2, y + 6, 0xFFFFFF, false);

        for (AbstractWidget widget : widgets) {
            widget.render(graphics, mouseX, mouseY, delta);
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + 20) {
            if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                dragging = true;
                dragOffsetX = (int) mouseX - x;
                dragOffsetY = (int) mouseY - y;
                return true;
            } else if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
                expanded = !expanded;
                return true;
            }
        }

        if (expanded) {
            for (AbstractWidget widget : widgets) {
                if (widget.mouseClicked(mouseX, mouseY, button)) return true;
            }
        }
        return false;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        dragging = false;
        for (AbstractWidget widget : widgets) {
            widget.mouseReleased(mouseX, mouseY, button);
        }
        return false;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        if (dragging) {
            x = (int) mouseX - dragOffsetX;
            y = (int) mouseY - dragOffsetY;
            return true;
        }

        for (AbstractWidget widget : widgets) {
            if (widget.mouseDragged(mouseX, mouseY, button, dx, dy)) return true;
        }
        return false;
    }

    public List<Button> getButtons() {
        return buttons;
    }

    public List<AbstractWidget> getWidgets() {
        return widgets;
    }
}
