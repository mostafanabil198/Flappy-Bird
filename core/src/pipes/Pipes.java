package pipes;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

import helpers.GameInfo;

public class Pipes {

    private World world;
    private Body body1, body2, body3;
    private Sprite pipe1, pipe2;
    private final float DISTANCE_BETWEEN_PIPES = 395f;
    private Random rand = new Random();
    private OrthographicCamera camera;
    public static float speed = -2.5f;

    public Pipes(World world, float x, OrthographicCamera camera) {
        this.world = world;
        this.camera = camera;
        createPipes(x, getRandomY());

    }

    void createPipes(float x, float y) {
        pipe1 = new Sprite(new Texture("Pipes/Pipe 1.png"));
        pipe2 = new Sprite(new Texture("Pipes/Pipe 2.png"));
        pipe1.setPosition(x, y + DISTANCE_BETWEEN_PIPES);
        pipe2.setPosition(x, y - DISTANCE_BETWEEN_PIPES);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;

        //creating body fore pipe1
        bodyDef.position.set(pipe1.getX() / GameInfo.PPM, pipe1.getY() / GameInfo.PPM);
        body1 = world.createBody(bodyDef);
        body1.setFixedRotation(false);
        //creating body for pipe2
        bodyDef.position.set(pipe2.getX() / GameInfo.PPM, pipe2.getY() / GameInfo.PPM);
        body2 = world.createBody(bodyDef);
        body2.setFixedRotation(false);
        //create body for score
        bodyDef.position.set(pipe1.getX() / GameInfo.PPM, y / GameInfo.PPM);
        body3 = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(pipe1.getWidth() / GameInfo.PPM / 2f, pipe1.getHeight() / GameInfo.PPM / 2f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = GameInfo.PIPE;
        Fixture fixture1 = body1.createFixture(fixtureDef);
        fixture1.setUserData("Pipe");
        Fixture fixture2 = body2.createFixture(fixtureDef);
        fixture2.setUserData("Pipe");

        shape.setAsBox(3 / GameInfo.PPM, pipe1.getHeight() / 2 / GameInfo.PPM);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = GameInfo.SCORE;
        fixtureDef.isSensor = true;
        Fixture fixture3 = body3.createFixture(fixtureDef);
        fixture3.setUserData("Score");
        shape.dispose();

    }

    float getRandomY() {
        float max = GameInfo.HEIGHT / 2 + 230f;
        float min = GameInfo.HEIGHT / 2 - 230f;
        return rand.nextFloat() * (max - min) + min;
    }

    public void drawPipes(SpriteBatch batch) {
        batch.draw(pipe1, pipe1.getX() - pipe1.getWidth() / 2f, pipe1.getY() - pipe1.getHeight() / 2);
        batch.draw(pipe2, pipe2.getX() - pipe2.getWidth() / 2f, pipe2.getY() - pipe2.getHeight() / 2);
    }

    public void updatePipes() {
        pipe1.setPosition(body1.getPosition().x * GameInfo.PPM, body1.getPosition().y * GameInfo.PPM);
        pipe2.setPosition(body2.getPosition().x * GameInfo.PPM, body2.getPosition().y * GameInfo.PPM);
    }

    public void movePipes() {
        body2.setLinearVelocity(speed, 0);
        body1.setLinearVelocity(speed, 0);
        body3.setLinearVelocity(speed, 0);
        if (pipe1.getX() + GameInfo.WIDTH / 2f + 60 < camera.position.x) {
            body1.setActive(false);
            body2.setActive(false);
            body3.setActive(false);
        }
    }

    public void stopPipes() {
        body2.setLinearVelocity(0, 0);
        body1.setLinearVelocity(0, 0);
        body3.setLinearVelocity(0, 0);
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}