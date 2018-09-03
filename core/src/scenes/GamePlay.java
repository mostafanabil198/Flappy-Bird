package scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yalla.flappy.GameMain;

import bird.Bird;
import ground.GroundBody;
import helpers.GameInfo;
import hud.UiHud;
import pipes.Pipes;

public class GamePlay implements Screen, ContactListener {
    private GameMain game;
    private World world;
    private OrthographicCamera mainCamera;
    private Viewport gameViewPort;
    private Array<Sprite> bgs = new Array<Sprite>();
    private Array<Sprite> grounds = new Array<Sprite>();
    private Bird bird;
    private OrthographicCamera debugCamera;
    private Box2DDebugRenderer debugRenderer;
    private GroundBody groundBody;
    private Array<Pipes> pipesArray = new Array<Pipes>();
    private int distanceBetweenPipes = 180;
    private UiHud hud;
    private float time = 1.5f;
    private boolean firstTouch;

    public GamePlay(GameMain game) {
        this.game = game;
        mainCamera = new OrthographicCamera(GameInfo.WIDTH, GameInfo.HEIGHT);
        mainCamera.position.set(GameInfo.WIDTH / 2, GameInfo.HEIGHT / 2, 0);
        gameViewPort = new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT, mainCamera);
        debugCamera = new OrthographicCamera();
        debugCamera.setToOrtho(false, GameInfo.WIDTH / GameInfo.PPM, GameInfo.HEIGHT / GameInfo.PPM);
        debugCamera.position.set(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, 0);
        debugRenderer = new Box2DDebugRenderer();
        createBackgrounds();
        createGrounds();

        world = new World(new Vector2(0, -12.8f), true);
        world.setContactListener(this);
        bird = new Bird(world, GameInfo.WIDTH / 2f - 80, GameInfo.HEIGHT / 2f);
        groundBody = new GroundBody(world, grounds.get(0));
        hud = new UiHud(game);


    }

    void inputHandle() {
        if (Gdx.input.justTouched()) {
            bird.birdFlap();
        }
    }

    void update(float dt) {
        checkFirstTouch();
        if (bird.isAlive()) {
            moveBackgrounds();
            moveGrounds();
            inputHandle();
            updatePipes();
            movePipe();
        }

    }


    void createBackgrounds() {
        for (int i = 0; i < 3; i++) {
            Sprite bg = new Sprite(new Texture("Background/Day.jpg"));
            bg.setPosition(i * bg.getWidth(), 0);
            bgs.add(bg);
        }
    }

    void createGrounds() {
        for (int i = 0; i < 3; i++) {
            Sprite ground = new Sprite(new Texture("Background/Ground.png"));
            ground.setPosition(i * ground.getWidth(), -ground.getHeight() / 2 - 55);
            grounds.add(ground);
        }
    }

    void drawBackgrounds(SpriteBatch batch) {
        for (Sprite s : bgs) {
            batch.draw(s, s.getX(), s.getY());
        }
    }

    void drawGrounds(SpriteBatch batch) {
        for (Sprite s : grounds) {
            batch.draw(s, s.getX(), s.getY());
        }
    }

    void moveBackgrounds() {
        for (Sprite bg : bgs) {
            float x1 = bg.getX() - 2f;
            bg.setPosition(x1, bg.getY());
            if (bg.getX() + bg.getWidth() / 2 + GameInfo.WIDTH < mainCamera.position.x) {
                bg.setPosition(bg.getX() + bg.getWidth() * bgs.size, bg.getY());
            }
        }
    }

    void moveGrounds() {
        for (Sprite ground : grounds) {
            float x1 = ground.getX() - 1f;
            ground.setPosition(x1, ground.getY());
            if (ground.getX() + ground.getWidth() / 2 + GameInfo.WIDTH < mainCamera.position.x) {
                ground.setPosition(ground.getX() + ground.getWidth() * grounds.size, ground.getY());
            }
        }
    }

    void drawPipes(SpriteBatch batch) {
        for (Pipes pipe : pipesArray) {
            pipe.drawPipes(batch);
        }
    }

    void updatePipes() {
        for (Pipes pipe : pipesArray) {
            pipe.updatePipes();
        }
    }

    void movePipe() {
        for (Pipes pipe : pipesArray) {
            pipe.movePipes();
        }
    }

    void stopPipes() {
        for (Pipes pipe : pipesArray) {
            pipe.stopPipes();
        }
    }

    void checkFirstTouch() {
        if (!firstTouch) {
            if (Gdx.input.justTouched()) {
                firstTouch = true;
                bird.activateBird();
                createAllPipes();
            }
        }
    }

    void createAllPipes() {
        RunnableAction run = new RunnableAction();
        run.setRunnable(new Runnable() {
            @Override
            public void run() {
                createPipes();
            }
        });
        SequenceAction sa = new SequenceAction();
        sa.addAction(Actions.delay(time));
        sa.addAction(run);
        hud.getStage().addAction(Actions.forever(sa));
    }

    void birdDied() {
        bird.setAlive(false);
        stopPipes();
        hud.getStage().clear();
        hud.showScore();
        hud.createButtons();
        Gdx.input.setInputProcessor(hud.getStage());
    }

    private void createPipes() {
        Pipes pipe = new Pipes(world, GameInfo.WIDTH + distanceBetweenPipes, mainCamera);
        pipesArray.add(pipe);
    }

    @Override
    public void show() {

    }

    int i = 1;

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.getBatch().begin();
        drawBackgrounds(game.getBatch());
        drawGrounds(game.getBatch());
        bird.drawBirdIdle(game.getBatch());
        drawPipes(game.getBatch());
        game.getBatch().end();
        debugRenderer.render(world, debugCamera.combined);
        game.getBatch().setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getStage().draw();
        hud.getStage().act();
        bird.updateBird();
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);

    }

    @Override
    public void resize(int width, int height) {
        gameViewPort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fix1;
        Fixture fix2;
        if (contact.getFixtureA().getUserData() == "Bird") {
            fix1 = contact.getFixtureA();
            fix2 = contact.getFixtureB();
        } else {
            fix1 = contact.getFixtureB();
            fix2 = contact.getFixtureA();
        }

        if (fix1.getUserData() == "Bird" && fix2.getUserData() == "Pipe" && bird.isAlive()) {
            birdDied();
        }
        if (fix1.getUserData() == "Bird" && fix2.getUserData() == "Ground" && bird.isAlive()) {
            birdDied();
        }
        if (fix1.getUserData() == "Bird" && fix2.getUserData() == "Score" && bird.isAlive()) {
            hud.incrementScore();
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
