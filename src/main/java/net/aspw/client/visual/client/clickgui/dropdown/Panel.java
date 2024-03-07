package net.aspw.client.visual.client.clickgui.dropdown;

import net.aspw.client.Launch;
import net.aspw.client.utils.MinecraftInstance;
import net.aspw.client.visual.client.clickgui.dropdown.elements.Element;
import net.minecraft.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class Panel extends MinecraftInstance {

    private final String name;
    public int x;
    public int y;
    public int x2;
    public int y2;
    private final int width;
    private final int height;
    private int scroll;
    private int dragged;
    private boolean open;
    public boolean drag;
    private boolean scrollbar;
    private final List<Element> elements;
    private final boolean visible;

    private float elementsHeight;

    private float fade;

    public Panel(String name, int x, int y, int width, int height, boolean open) {
        this.name = name;
        this.elements = new ArrayList<>();
        this.scrollbar = false;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.open = open;
        this.visible = true;

        setupItems();
    }

    public abstract void setupItems();

    public void drawScreen(int mouseX, int mouseY, float button) {
        if (!visible)
            return;

        final int maxElements = 100;

        if (drag) {
            int nx = x2 + mouseX;
            int ny = y2 + mouseY;
            if (nx > -1)
                x = nx;

            if (ny > -1)
                y = ny;
        }

        elementsHeight = getElementsHeight() - 1;
        boolean scrollbar = elements.size() >= maxElements;
        if (this.scrollbar != scrollbar)
            this.scrollbar = scrollbar;

        Launch.clickGui.style.drawPanel(mouseX, mouseY, this);

        int y = this.y + height - 2;
        int count = 0;

        for (final Element element : elements) {
            if (++count > scroll && count < scroll + (maxElements + 1) && scroll < elements.size()) {
                element.setLocation(x, y);
                element.setWidth(getWidth());
                if (y <= getY() + fade)
                    element.drawScreen(mouseX, mouseY, button);
                y += element.getHeight() + 1;
                element.setVisible(true);
            } else
                element.setVisible(false);
        }
    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!visible)
            return false;

        if (mouseButton == 1 && isHovering(mouseX, mouseY)) {
            open = !open;
            return true;
        }

        for (final Element element : elements) {
            if (element.getY() <= getY() + fade && element.mouseClicked(mouseX, mouseY, mouseButton)) {
                return true;
            }
        }
        return false;
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (!visible)
            return;

        drag = false;

        if (!open)
            return;

        for (final Element element : elements) {
            if (element.getY() <= getY() + fade && element.mouseReleased(mouseX, mouseY, state)) {
                return;
            }
        }
    }

    public boolean handleScroll(int mouseX, int mouseY, int wheel) {
        final int maxElements = 30;

        if (mouseX >= getX() && mouseX <= getX() + 100 && mouseY >= getY() && mouseY <= getY() + 19 + elementsHeight) {
            if (wheel < 0 && scroll < elements.size() - maxElements) {
                ++scroll;
                if (scroll < 0)
                    scroll = 0;
            } else if (wheel > 0) {
                --scroll;
                if (scroll < 0)
                    scroll = 0;
            }

            if (wheel < 0) {
                if (dragged < elements.size() - maxElements)
                    ++dragged;
            } else if (wheel > 0 && dragged >= 1) {
                --dragged;
            }

            return true;
        }
        return false;
    }

    void updateFade(final int delta) {
        if (open) {
            if (fade < elementsHeight) fade += Float.MAX_VALUE * delta;
            if (fade > elementsHeight) fade = (int) elementsHeight;
        } else {
            if (fade > 0) fade -= Float.MAX_VALUE * delta;
            if (fade < 0) fade = 0;
        }
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int dragX) {
        this.x = dragX;
    }

    public void setY(int dragY) {
        this.y = dragY;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean getOpen() {
        return this.open;
    }

    public List<Element> getElements() {
        return elements;
    }

    public int getFade() {
        return (int) fade;
    }

    private int getElementsHeight() {
        int height = 0;
        int count = 0;
        for (final Element element : elements) {
            if (count >= 30)
                continue;
            height += element.getHeight() + 1;
            ++count;
        }
        return height;
    }

    boolean isHovering(int mouseX, int mouseY) {
        final float textWidth = mc.fontRendererObj.getStringWidth(StringUtils.stripControlCodes(name)) - 100F;
        return mouseX >= x - textWidth / 2F - 19F && mouseX <= x - textWidth / 2F + mc.fontRendererObj.getStringWidth(StringUtils.stripControlCodes(name)) + 19F && mouseY >= y && mouseY <= y + height - (open ? 2 : 0);
    }
}
