package objects;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.powerpong.game.Options;
import com.powerpong.game.PowerPong;
import screens.PlayScreen;

import java.util.Random;

public class Wall {
    protected NinePatchDrawable ninePatch;
    protected Body body;

    protected Options options;

    private boolean needsNewLocation =  false;

    //constructor for invisible wall of specified size
    //pass angle as degrees
    public Wall(float x, float y, float width, float height, float angle, World world) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.fixedRotation = true;
        bodyDef.position.set(x, y); //note that the origin for bodys is at the center; so the wall will initially be centered at the passed x and y coordinates

        body = world.createBody(bodyDef);
        angle = (float)(angle / 180 * Math.PI);
        body.setTransform(body.getPosition(), angle);
        body.setUserData(this);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PowerPong.PPM, height / 2 / PowerPong.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0f; //set friction to 0 so that moving into a wall while falling will not slow the player
        fixtureDef.restitution = 1f; //1 restitution so that the ball rebounds perfectly

        body.createFixture(fixtureDef);
        shape.dispose();
    }

    //constructor for textured wall of the texture's size
    public Wall(String textureName, float x, float y, float angle, World world, Options options) {
        this.options = options;
        ninePatch = new NinePatchDrawable(new NinePatch(new Texture(textureName)));

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.fixedRotation = true;
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);
        angle = (float)(angle / 180 * Math.PI);
        body.setTransform(body.getPosition(), angle);
        body.setUserData(this);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(options.targetWidth / 2 / PowerPong.PPM, ninePatch.getMinHeight() / 2 / PowerPong.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 1f;

        body.createFixture(fixtureDef);
        shape.dispose();
    }

    public void randomizeLocation(){
        Random rand = new Random();
        float max, min;
        max = (PowerPong.NATIVE_WIDTH - options.targetWidth - 20);
        min = -(PowerPong.NATIVE_WIDTH - options.targetWidth - 20);
        float x = rand.nextInt((int)(max - min) + 1) + min;
        body.setTransform(x / PowerPong.PPM / 2, 1100 / PowerPong.PPM, 0);
        this.needsNewLocation(false);
    }

    //DO NOT CALL DRAW ON INVISIBLE WALLS, IT WILL THROW NULLPOINTEREXCEPTION
    public void draw(SpriteBatch sb) {
        ninePatch.draw(sb,
                body.getPosition().x - options.targetWidth / 2 / PowerPong.PPM,
                body.getPosition().y - ninePatch.getMinHeight() / 2 / PowerPong.PPM,
                options.targetWidth / PowerPong.PPM,
                ninePatch.getMinHeight() / PowerPong.PPM);
    }

    public boolean needsNewLocation() {
        return needsNewLocation;
    }

    public void needsNewLocation(boolean bool) {
        needsNewLocation = bool;
    }

    public NinePatchDrawable getNinePatch() {
        return ninePatch;
    }

    public void dispose() {

    }
}
