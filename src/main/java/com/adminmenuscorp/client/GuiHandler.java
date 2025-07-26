
package com.adminmenuscorp.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;
import com.adminmenuscorp.client.ui.AdminMenuScreen;

public class GuiHandler {
    private final KeyMapping openGuiKey;

    public GuiHandler() {
        openGuiKey = new KeyMapping("key.adminmenuscorp.open_gui", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G, "key.categories.misc");
        Minecraft.getInstance().options.keyMappings = append(Minecraft.getInstance().options.keyMappings, openGuiKey);
    }

    private static KeyMapping[] append(KeyMapping[] arr, KeyMapping key) {
        KeyMapping[] newArr = new KeyMapping[arr.length + 1];
        System.arraycopy(arr, 0, newArr, 0, arr.length);
        newArr[arr.length] = key;
        return newArr;
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event) {
        if (openGuiKey.consumeClick() && Minecraft.getInstance().screen == null)
            Minecraft.getInstance().setScreen(new AdminMenuScreen());
    }
}
