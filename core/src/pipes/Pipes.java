package pipes;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.yalla.flappy.GameMain;

import java.util.Random;

import collectables.Collectables;
import helpers.GameInfo;
import helpers.GameManager;
import scenes.GamePlay;

public class Pipes {

    private World world;
    private Body body1, body2, body3, body4;
    private Sprite pipe1, pipe2, collectable;
    private float DISTANCE_BETWEEN_PIPES = 395f;
    private Random rand = new Random();
    private OrthographicCamera camera;
    public float speed = -2.5f;
    private GameMain game;
    private String colName = "";
    public Fixture fixture4, fixture2, fixture1;
    public static int numCoins = 0, numSpeed = 0;
    private GamePlay gamePlay;

    public Pipes(World world, float x, OrthographicCamera camera, GameMain game, GamePlay gamePlay) {
        this.game = game;
        this.gamePlay = gamePlay;
        this.world = world;
        this.camera = camera;
        createPipes(x, getRandomY());

    }

    void createPipes(float x, float y) {
        GameManager.getInstance().setNumOfPipes(GameManager.getInstance().getNumOfPipes() + 1);
        if (GameManager.getInstance().getNumOfPipes() < 5) {
            DISTANCE_BETWEEN_PIPES = 410f;
        } else if (GameManager.getInstance().getNumOfPipes() < 10) {
            DISTANCE_BETWEEN_PIPES = 403f;
        } else if (GameManager.getInstance().getNumOfPipes() < 15) {
            DISTANCE_BETWEEN_PIPES = 398f;
        } else if (GameManager.getInstance().getNumOfPipes() > 40) {
            DISTANCE_BETWEEN_PIPES = 390f;

        } else {
            DISTANCE_BETWEEN_PIPES = 395f;
        }

        if (GameManager.getInstance().getNumOfPipes() < 5 && GameManager.getInstance().getBirdIndex() == 0) {
            DISTANCE_BETWEEN_PIPES = 400f;
        } else if (GameManager.getInstance().getNumOfPipes() < 10 && GameManager.getInstance().getBirdIndex() == 0) {
            DISTANCE_BETWEEN_PIPES = 395f;
        } else if (GameManager.getInstance().getNumOfPipes() < 15 && GameManager.getInstance().getBirdIndex() == 0) {
            DISTANCE_BETWEEN_PIPES = 392f;
        } else if (GameManager.getInstance().getNumOfPipes() >= 40 && GameManager.getInstance().getBirdIndex() == 0) {
            DISTANCE_BETWEEN_PIPES = 382f;
        } else if (GameManager.getInstance().getNumOfPipes() > 15 && GameManager.getInstance().getBirdIndex() == 0) {
            DISTANCE_BETWEEN_PIPES = 387f;
        }

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
        bodyDef.position.set(pipe1.getX() / GameInfo.PPM, GameInfo.HEIGHT / 2 / GameInfo.PPM);
        body3 = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(pipe1.getWidth() / GameInfo.PPM / 2f, pipe1.getHeight() / GameInfo.PPM / 2f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = GameInfo.PIPE;
        fixture1 = body1.createFixture(fixtureDef);
        fixture1.setUserData("Pipe");
        fixture2 = body2.createFixture(fixtureDef);
        fixture2.setUserData("Pipe");
        shape.setAsBox(3 / GameInfo.PPM, GameInfo.HEIGHT / 2 / GameInfo.PPM);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = GameInfo.SCORE;
        fixtureDef.isSensor = true;
        Fixture fixture3 = body3.createFixture(fixtureDef);
        fixture3.setUserData("Score");
        shape.dispose();

        //Create Collectables
        randomCollectable();
        if (colName != "") {
            collectable = new Sprite(new Texture("Collectables/" + colName + ".png"));
            collectable.setPosition(x, y);
            bodyDef.position.set(collectable.getX() / GameInfo.PPM, collectable.getY() / GameInfo.PPM);
            body4 = world.createBody(bodyDef);
            body4.setFixedRotation(false);
            CircleShape shape1 = new CircleShape();
            shape1.setRadius(collectable.getHeight() / 2 / GameInfo.PPM);
            fixtureDef.shape = shape1;
            fixtureDef.filter.categoryBits = GameInfo.COLLECTABLES;
            fixtureDef.isSensor = true;
            fixture4 = body4.createFixture(fixtureDef);
            fixture4.setUserData(colName);
            shape1.dispose();
        }


    }

    void randomCollectable() {
        int r = rand.nextInt(150);
        if ((r == 10 || r == 63 || r == 50 || r == 31 || r == 37) && numSpeed < 3) {
            colName = "Invisible";
        } else if (((r > 1 && r < 7) || (r > 120 && r < 130) || (r < 113 && r > 118) || (r > 22 && r < 31) || (r > 53 && r < 58) || (r > 89 && r < 97) || (r > 142 && r < 146) || (r < 67 && r < 69)) && numCoins < 40) {
            colName = "Coin";
        } else if (r == 12 || r == 110 || r == 132) {
            colName = "Fire";
        } else {
            colName = "";
        }

        if (gamePlay.isLevel() || GameManager.getInstance().isInvisible() || GameManager.getInstance().isSpeed()) {
            colName = "";
        }
    }

    float getRandomY() {
        float max = GameInfo.HEIGHT / 2 + 230f;
        float min = GameInfo.HEIGHT / 2 - 230f;
        return rand.nextFloat() * (max - min) + min;
    }

    public void drawPipes(SpriteBatch batch) {
        if (fixture1.getUserData() == "Pipe") {
            batch.draw(pipe1, pipe1.getX() - pipe1.getWidth() / 2f, pipe1.getY() - pipe1.getHeight() / 2);
        }
        if (fixture2.getUserData() == "Pipe") {
            batch.draw(pipe2, pipe2.getX() - pipe2.getWidth() / 2f, pipe2.getY() - pipe2.getHeight() / 2);
        }
        if (colName != "") {
            batch.draw(collectable, collectable.getX() - collectable.getWidth() / 2f, collectable.getY() - collectable.getHeight() / 2);
        }
    }

    public void updatePipes() {
        if (fixture1.getUserData() == "Pipe") {
            pipe1.setPosition(body1.getPosition().x * GameInfo.PPM, body1.getPosition().y * GameInfo.PPM);
        }
        if (fixture2.getUserData() == "Pipe") {
            pipe2.setPosition(body2.getPosition().x * GameInfo.PPM, body2.getPosition().y * GameInfo.PPM);
        }
        if (colName != "") {
            collectable.setPosition(body4.getPosition().x * GameInfo.PPM, body4.getPosition().y * GameInfo.PPM);
        }
    }

    public void movePipes() {
        if (fixture2.getUserData() == "Pipe") {
            body2.setLinearVelocity(speed, 0);
        } else {
            pipe2.getTexture().dispose();
            fixture2.setSensor(true);
            body2.setActive(false);
        }
        if (fixture1.getUserData() == "Pipe") {
            body1.setLinearVelocity(speed, 0);
        } else {
            pipe1.getTexture().dispose();
            fixture1.setSensor(true);
            body1.setActive(false);
        }
        body3.setLinearVelocity(speed, 0);
        if (colName != "") {
            body4.setLinearVelocity(speed, 0);
        }
        if (pipe1.getX() + GameInfo.WIDTH / 2f + 60 < camera.position.x) {
            body1.setActive(false);
            body2.setActive(false);
            body3.setActive(false);
            if (colName != "") {
                body4.setActive(false);
            }
        }
        if (colName != "" && fixture4.getUserData() == "Remove") {
            colName = "";
            collectable.getTexture().dispose();
        }
    }

    public void stopPipes() {
        body2.setLinearVelocity(0, 0);
        body1.setLinearVelocity(0, 0);
        body3.setLinearVelocity(0, 0);
        if (colName != "") {
            body4.setLinearVelocity(0, 0);
        }
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void dispose() {
        pipe1.getTexture().dispose();
        pipe2.getTexture().dispose();
    }

    public Sprite getPipe1() {
        return pipe1;
    }

    public Sprite getPipe2() {
        return pipe2;
    }

    public void makeSensore(boolean sensor) {
        fixture1.setSensor(sensor);
        fixture2.setSensor(sensor);
    }


}
