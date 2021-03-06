package ground;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

import helpers.GameInfo;

public class GroundBody {
    private World world;
    private Body body;


    public GroundBody(World world, Sprite ground) {
        this.world = world;
        createGroundBody(ground);
        createSkyBody();
    }

    public void createGroundBody(Sprite ground) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(ground.getWidth() / GameInfo.PPM, (-ground.getHeight() / 2 - 55f) / GameInfo.PPM);
        body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(ground.getWidth() / GameInfo.PPM, ground.getHeight() / GameInfo.PPM);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 4;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = GameInfo.GROUND;
        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData("Ground");
        shape.dispose();

    }

    void createSkyBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0, GameInfo.HEIGHT / GameInfo.PPM);
        body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(GameInfo.WIDTH / GameInfo.PPM, 1 / GameInfo.PPM);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 4;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = GameInfo.SKY;
        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData("Sky");
        shape.dispose();

    }
}
