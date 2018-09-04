package bird;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import helpers.GameInfo;
import helpers.GameManager;

public class Bird extends Sprite {

    private World world;
    private Body body;
    private boolean isAlive;
    private Texture birdDead;
    private TextureAtlas birdAtlas;
    private Animation<TextureRegion> animation;
    private float elapsedTime;
    private boolean hasSpeed;

    public Bird(World world, float x, float y) {
        super(new Texture("Birds/" + GameManager.getInstance().getBird() + "/IdleS.png"));
        birdDead = new Texture("Birds/" + GameManager.getInstance().getBird() + "/Dead.png");
        setPosition(x, y);
        this.world = world;
        createBody();
        createAnimation();
    }

    void createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX() / GameInfo.PPM, getY() / GameInfo.PPM);
        body = world.createBody(bodyDef);
        body.setFixedRotation(false);
        CircleShape shape = new CircleShape();
        shape.setRadius(getHeight() / 2 / GameInfo.PPM);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1;
        fixtureDef.filter.categoryBits = GameInfo.BIRD;
        fixtureDef.filter.maskBits =  GameInfo.SCORE | GameInfo.COLLECTABLES;
        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData("Bird");
        shape.dispose();
        body.setActive(false);

    }

    public void birdFlap() {
        body.setLinearVelocity(0, 4.8f);
    }

    public void drawBirdIdle(SpriteBatch batch) {
        if (!isAlive) {
            batch.draw(this, getX() - getWidth() / 2f, getY() - getHeight() / 2f);
        }
    }

    public void updateBird() {
        setPosition(body.getPosition().x * GameInfo.PPM, body.getPosition().y * GameInfo.PPM);
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public void died() {
        setAlive(false);
    }

    public void activateBird() {
        isAlive = true;
        body.setActive(true);
    }

    public void birdDied() {
        this.setTexture(birdDead);
    }

    public void createAnimation() {
        birdAtlas = new TextureAtlas("Birds/" + GameManager.getInstance().getBird() + "/" + GameManager.getInstance().getBird() + " Bird.atlas");
        animation = new Animation(1f / 7f, birdAtlas.getRegions());
    }

    public void animateBird(SpriteBatch batch) {
        if (isAlive) {
            elapsedTime += Gdx.graphics.getDeltaTime();
            batch.draw(animation.getKeyFrame(elapsedTime, true), getX() - getWidth() / 2f, getY() - getHeight() / 2f);
        }
    }

    public boolean isHasSpeed() {
        return hasSpeed;
    }

    public void setHasSpeed(boolean hasSpeed) {
        this.hasSpeed = hasSpeed;
    }
}
