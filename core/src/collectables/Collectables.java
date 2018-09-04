package collectables;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.yalla.flappy.GameMain;

import helpers.GameInfo;

public class Collectables extends Sprite {
    private World world;
    private GameMain game;
    private Body body;
    private Fixture fixture;
    private String name;

    public Collectables(GameMain game, World world, String name, float x, float y) {
        super(new Texture("Collectables/" + name + ".png"));
        this.game = game;
        this.world = world;
        this.name = name;
        setPosition(x, y);
        createBody();

    }

    private void createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((getX() - getWidth() / 2 - 15) / GameInfo.PPM, (getY() + 5 + getWidth() / 2) / GameInfo.PPM);
        body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth() / 2 / GameInfo.PPM, getHeight() / 2 / GameInfo.PPM);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = GameInfo.COLLECTABLES;
        fixtureDef.isSensor = true;
        fixture = body.createFixture(fixtureDef);
        fixture.setUserData(name);
        shape.dispose();
    }

    public void updateCollectablePositions() {
        setPosition(body.getPosition().x * GameInfo.PPM, (body.getPosition().y - .2f) * GameInfo.PPM);
    }


    public void changeFilter() {
        Filter filter = new Filter();
        filter.categoryBits = GameInfo.DESTROYED;
        fixture.setFilterData(filter);
    }

    public void moveCollectable(float speed) {
        body.setLinearVelocity(speed, 0);
    }

    public void drawCollectable(SpriteBatch batch) {
        batch.draw(this, getX() - getWidth() / 2, getY() - getHeight() / 2);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
