package com.adminmenuscorp.client.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
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
    private final List<WidgetPosition> widgetPositions = new ArrayList<>();

    // Helper class to store relative widget positions
    private static class WidgetPosition {
        AbstractWidget widget;
        int relativeX, relativeY;

        WidgetPosition(AbstractWidget widget, int relativeX, int relativeY) {
            this.widget = widget;
            this.relativeX = relativeX;
            this.relativeY = relativeY;
        }
    }

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
        // Store the button's current position as relative position
        // (assuming button was created with relative coordinates)
        int relativeX = button.getX();
        int relativeY = button.getY();
        widgetPositions.add(new WidgetPosition(button, relativeX, relativeY));

        // Immediately update the button's position to be absolute
        button.setX(x + relativeX);
        button.setY(y + relativeY);
    }

    public void addWidget(AbstractWidget widget) {
        widgets.add(widget);
        // Store the widget's current position as relative position
        // (assuming widget was created with relative coordinates)
        int relativeX = widget.getX();
        int relativeY = widget.getY();
        widgetPositions.add(new WidgetPosition(widget, relativeX, relativeY));

        // Immediately update the widget's position to be absolute
        widget.setX(x + relativeX);
        widget.setY(y + relativeY);
    }

    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        if (!expanded) return;

        // Update widget positions to follow the panel
        updateWidgetPositions();

        graphics.fill(x, y, x + width, y + height, 0x88000000);
        graphics.fill(x, y, x + width, y + 20, 0xFF6A00FF);
        graphics.drawString(Minecraft.getInstance().font, title, x + width / 2 - Minecraft.getInstance().font.width(title) / 2, y + 6, 0xFFFFFF, false);

        for (AbstractWidget widget : widgets) {
            widget.render(graphics, mouseX, mouseY, delta);
        }
    }

    /**
     * Updates widget positions to be relative to the panel's current position.
     */
    private void updateWidgetPositions() {
        for (WidgetPosition widgetPos : widgetPositions) {
            int newX = x + widgetPos.relativeX;
            int newY = y + widgetPos.relativeY;
            widgetPos.widget.setX(newX);
            widgetPos.widget.setY(newY);
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
