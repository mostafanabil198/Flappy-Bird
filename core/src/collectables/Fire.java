package collectables;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.yalla.flappy.GameMain;

import bird.Bird;
import helpers.GameInfo;

public class Fire extends Sprite {
    private World world;
    private GameMain game;
    private Body body;
    private Fixture fixture;
    private Bird bird;

    public Fire(GameMain game, World world, Bird bird) {
        super(new Texture("Collectables/Rocket.png"));
        this.game = game;
        this.world = world;
        this.bird = bird;
        createBody();
    }

    void createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((bird.getX() + bird.getWidth() + 2) / GameInfo.PPM, (bird.getY() - 10) / GameInfo.PPM);
        body = world.createBody(bodyDef);
        body.setFixedRotation(false);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth() / GameInfo.PPM / 2f, getHeight() / GameInfo.PPM / 2f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = GameInfo.FIRE;
        fixtureDef.filter.maskBits = GameInfo.PIPE;
        fixture = body.createFixture(fixtureDef);
        fixture.setUserData("Fire");
        shape.dispose();
    }

    public void fireAndUpdate() {
        if (fixture.getUserData() == "Fire") {
            body.setLinearVelocity(10, 0);
            setPosition(body.getPosition().x * GameInfo.PPM, body.getPosition().y * GameInfo.PPM);
        }
    }

    public void drawFire(SpriteBatch batch) {
        if (fixture.getUserData() == "Fire") {
            batch.draw(this, getX() - getWidth() / 2f, getY() - getHeight() / 2);
        }
    }

    public void removeFire() {
        getTexture().dispose();
        fixture.setSensor(true);
        body.setLinearVelocity(0, 0);
        body.setActive(false);
        if (!world.isLocked()) {
            world.destroyBody(body);
        }
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Fixture getFixture() {
        return fixture;
    }

    public void setFixture(Fixture fixture) {
        this.fixture = fixture;
    }

}
