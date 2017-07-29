package org.dead_running_on_tray.prepare_4_ludum_dare.game.objects;

import org.dead_running_on_tray.prepare_4_ludum_dare.game.objects.route.Point;
import org.dead_running_on_tray.prepare_4_ludum_dare.game.objects.texture.Texture;

import static org.dead_running_on_tray.prepare_4_ludum_dare.game.GameConstants.SCREEN_HEIGHT;
import static org.dead_running_on_tray.prepare_4_ludum_dare.game.GameConstants.SCREEN_WIDTH;
import static org.lwjgl.opengl.GL11.*;

public abstract class GameObject {
    private int id;
    protected Point coordinates;
    private Texture sprite;
    private boolean inversedX = false;

    /**
     * For inner using.
     */
    private float unit_width, unit_height, normX, normY;

    GameObject(int x, int y, int id, String spritePath) {
        coordinates = new Point(x, y);
        this.id = id;

        try {
            sprite = new Texture(spritePath);
        } catch (Exception e) {
            System.err.println("Error while reading sprite for game object!");
            sprite = null;
        }

        unit_width = 4 * getWidth() / SCREEN_WIDTH;
        unit_height = 4 * getHeight() / SCREEN_HEIGHT;
        normX = (float) x / SCREEN_WIDTH;
        normY = (float) y / SCREEN_HEIGHT;
    }

    public int getId() {
        return id;
    }

    public Point getCoordinates() {
        return coordinates;
    }

    public int getX() {
        return (int) coordinates.getX();
    }

    public int getY() {
        return (int) coordinates.getY();
    }

    public float getWidth() {
        return sprite.getWidth();
    }

    public float getHeight() {
        return sprite.getHeight();
    }

    public Texture getSprite() {
        return sprite;
    }

    public void move(float dx, float dy) {
        if (inversedX && dx > 0 || dx < 0 && !inversedX) {
            inversedX = !inversedX;
        }

        coordinates.move(dx, dy);
        normX = coordinates.getX() / SCREEN_WIDTH;
        normY = coordinates.getY() / SCREEN_HEIGHT;
    }

    public void draw() {
        sprite.bind();

        glBegin(GL_POLYGON);
        glTexCoord2f(inversedX ? 1f : 0f, 1f);
        glVertex2f(normX, normY);
        glTexCoord2f(inversedX ? 0f : 1f, 1f);
        glVertex2f(normX + unit_width, normY);
        glTexCoord2f(inversedX ? 0f : 1f, 0f);
        glVertex2f(normX + unit_width, normY + unit_height);
        glTexCoord2f(inversedX ? 1f : 0f, 0f);
        glVertex2f(normX, normY + unit_height);
        glEnd();
    }
}
