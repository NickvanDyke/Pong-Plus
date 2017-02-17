package objects.paddles;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.powerpong.game.PowerPong;
import objects.Ball;
import screens.PlayScreen;

public class AIPaddle extends Paddle {
    private static float maxOffset; //Width of paddle is currently 320 so an offset above 160 would cause the AI to miss sometimes.
    private float offset; //current offset for this specific paddle.

    private Ball ball;
    private Vector2 prevVel;

    public AIPaddle(String textureName, float x, float y, World world, Ball ball, PlayScreen.AI difficulty) {
        super(textureName, x, y, world);
        this.prevVel = new Vector2(ball.getBody().getLinearVelocity());
        this.ball = ball;
        switch (difficulty) {
            case EASY:
                maxOffset = 0;
                movespeed = 2;
                break;
            case MEDIUM:
                maxOffset = texture.getWidth() / 4;
                movespeed = 3;
                break;
            case HARD:
                maxOffset = texture.getWidth() / 2;
                movespeed = 5;
                break;
            case SKYNET:
                maxOffset = texture.getWidth() / 2;
                movespeed = 15;
                break;
        }
    }

    public void update(float dt) {
       if (!prevVel.isCollinear(ball.getBody().getLinearVelocity()) &&
               ((this.getY() < 0 && ball.getBody().getLinearVelocity().y < 0) ||
                       (this.getY() > 0 && ball.getBody().getLinearVelocity().y > 0)))
           destination.set(calcFinalDestination(
                    ball.getX(),
                    ball.getY(),
                    ball.getBody().getLinearVelocity().x,
                    ball.getBody().getLinearVelocity().y) + offset,
                    this.getY());
        //move towards the destination
        super.update(dt);
    }

    public float calcFinalDestination(float xPos, float yPos, float xVel, float yVel) {
        float timeToPaddle;
        if (this.getY() > yPos)
            timeToPaddle = (this.getY() - this.getTexture().getHeight() / PowerPong.PPM - yPos) / yVel;
        else
            timeToPaddle = (this.getY() + this.getTexture().getHeight() / PowerPong.PPM - yPos) / yVel;
        float finalDestination = xPos + xVel * timeToPaddle;
        if (finalDestination < -PowerPong.NATIVE_WIDTH / 2 / PowerPong.PPM) {
            yPos = yPos + ((-PowerPong.NATIVE_WIDTH / 2 / PowerPong.PPM - xPos + ball.getTexture().getWidth() / PowerPong.PPM) / xVel) * yVel;
            xPos = -PowerPong.NATIVE_WIDTH / 2 / PowerPong.PPM;
            xVel = -xVel;
            return calcFinalDestination(xPos, yPos, xVel, yVel);
        }
        else if (finalDestination > PowerPong.NATIVE_WIDTH / 2 / PowerPong.PPM) {
            yPos = yPos + ((PowerPong.NATIVE_WIDTH / 2 / PowerPong.PPM - xPos - ball.getTexture().getWidth() / PowerPong.PPM) / xVel) * yVel;
            xPos = PowerPong.NATIVE_WIDTH / 2 / PowerPong.PPM;
            xVel = -xVel;
            return calcFinalDestination(xPos, yPos, xVel, yVel);
        }
        prevVel.set(ball.getBody().getLinearVelocity());
        return finalDestination;
    }

    public void randomizeOffset() {
        offset = (float)Math.floor(Math.random() * (maxOffset * 2 + 1) - maxOffset) / PowerPong.PPM;
    }

    public float getOffset() {
        return offset;
    }
}
