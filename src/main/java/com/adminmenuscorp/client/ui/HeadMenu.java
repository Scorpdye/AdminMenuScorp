package com.adminmenuscorp.client.ui;

public class HeadMenu {
    private static float scale = 0.5f;

    public static float getScale() {
        return scale;
    }

    public static void setScale(float newScale) {
        scale = Math.max(0.1f, Math.min(1.0f, newScale));
    }
}
