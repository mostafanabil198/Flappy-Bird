package scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yalla.flappy.GameMain;

import bird.Bird;
import collectables.Collectables;
import ground.GroundBody;
import helpers.GameInfo;
import helpers.GameManager;
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
    public float time = 1.5f;
    private boolean firstTouch;
    private Sound scoreSound, dieSound, flyingSound, tickSound;
    private Texture tapToPlay;
    private Array<Collectables> collectables;
    private int targetScore;
    private int winCoins;
    private boolean isLevel;
    private int currentLevel;
    private float timeCount = 0;
    private Actor actor;
    private SequenceAction sa;


    public GamePlay(GameMain game, boolean isLevel, int currentlevel, int targetScore, int winCoins) {
        this.game = game;
        this.targetScore = targetScore;
        this.winCoins = winCoins;
        this.isLevel = isLevel;
        this.currentLevel = currentlevel;
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
        bird.setHasInvisible(false);
        groundBody = new GroundBody(world, grounds.get(0));
        hud = new UiHud(game, this, bird);
        flyingSound = Gdx.audio.newSound(Gdx.files.internal("Flappy Bird Sounds/Fly.mp3"));
        scoreSound = Gdx.audio.newSound(Gdx.files.internal("Flappy Bird Sounds/Score.mp3"));
        tickSound = Gdx.audio.newSound(Gdx.files.internal("Flappy Bird Sounds/Tick.mp3"));
        dieSound = Gdx.audio.newSound(Gdx.files.internal("Flappy Bird Sounds/Dead.mp3"));
        tapToPlay = new Texture("Buttons/Touch To Start.png");
        collectables = new Array<Collectables>();

    }

    void inputHandle() {
        if (Gdx.input.justTouched()) {
            bird.birdFlap();
            flyingSound.play();
        }
    }

    void update(float dt) {
        checkFirstTouch();
        if (bird.isAlive()) {
            if (bird.isHasInvisible()) {
                makePipesSensors(true);
                timeCount += dt;
                if (timeCount >= 1) {
                    hud.setTime(hud.getTime() - 1);
                    tickSound.play();
                    if (!bird.isHasSpeed()) {
                        hud.updateInvisibleTimeLbl();
                    } else {
                        hud.updateSpeedTimeLbl();
                    }
                    timeCount = 0;
                }
            } else {
                makePipesSensors(false);
            }

//            if (bird.isHasSpeed()) {
//                changePipeSpeed(-10f);
//
//            } else {
//                changePipeSpeed(-2.5f);
//            }
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
            float x1 = bg.getX() - 3.5f;
            bg.setPosition(x1, bg.getY());
            if (bg.getX() + bg.getWidth() / 2 + GameInfo.WIDTH < mainCamera.position.x) {
                bg.setPosition(bg.getX() + bg.getWidth() * bgs.size, bg.getY());
            }
        }
    }

    void moveGrounds() {
        for (Sprite ground : grounds) {
            float x1 = ground.getX() - 2f;
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

    void changePipeSpeed(float speed) {
        for (Pipes pipe : pipesArray) {
            pipe.speed = speed;
        }
    }


    void stopPipes() {
        for (Pipes pipe : pipesArray) {
            pipe.stopPipes();
        }
    }

    public void makePipesSensors(boolean sensor) {
        for (Pipes pipe : pipesArray) {
            pipe.makeSensore(sensor);
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

    public void createAllPipes() {
        RunnableAction run = new RunnableAction();
        run.setRunnable(new Runnable() {
            @Override
            public void run() {
                createPipes();
            }
        });
        sa = new SequenceAction();
        sa.addAction(Actions.delay(time));
        sa.addAction(run);
        hud.getStage().addAction(Actions.forever(sa));
    }


    void birdDied() {
        GameManager.getInstance().score = hud.getScore();
        bird.birdDied();
        dieSound.play();
        bird.setAlive(false);
        stopPipes();
//Score
        GameManager.getInstance().checkForNewHighScore();
        GameManager.getInstance().addCoins(hud.getCoins());
        hud.getStage().clear();
        hud.showScore();

        Gdx.input.setInputProcessor(hud.getStage());
    }

    void finishLevel() {
        // Winning sound <<-------------------------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        bird.setAlive(false);
        stopPipes();
        //Open new level
        if (GameManager.getInstance().getGameData().getUserCurrentLevel() == currentLevel && currentLevel < GameManager.getInstance().getGameData().getTotalShownLevels()) {
            GameManager.getInstance().getGameData().setUserCurrentLevel(GameManager.getInstance().getGameData().getUserCurrentLevel() + 1);
            GameManager.getInstance().addCoins(winCoins);
        }
        hud.getStage().clear();
        hud.winLevel();
        Gdx.input.setInputProcessor(hud.getStage());
    }

    private void createPipes() {
        Pipes pipe = new Pipes(world, GameInfo.WIDTH + distanceBetweenPipes, mainCamera, game, this);
        pipesArray.add(pipe);
    }

    @Override
    public void show() {

    }


    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.getBatch().begin();
        drawBackgrounds(game.getBatch());
        drawGrounds(game.getBatch());
        if (!firstTouch) {
            game.getBatch().draw(tapToPlay, GameInfo.WIDTH / 3 - 28, GameInfo.HEIGHT / 2 - 133);
        }
        if (firstTouch) {
            bird.drawBirdIdle(game.getBatch());
        }
        bird.animateBird(game.getBatch());
        drawPipes(game.getBatch());
        game.getBatch().end();
        //debugRenderer.render(world, debugCamera.combined);
        game.getBatch().setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getStage().act();
        hud.getStage().draw();

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
        world.dispose();
        scoreSound.dispose();
        dieSound.dispose();
        flyingSound.dispose();
        for (Sprite bg : bgs) {
            bg.getTexture().dispose();
        }
        for (Sprite g : grounds) {
            g.getTexture().dispose();
        }
        for (Pipes pipe : pipesArray) {
            pipe.dispose();
        }
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
            if (!bird.isHasInvisible()) {
                birdDied();
            }
        }
        if (fix1.getUserData() == "Bird" && fix2.getUserData() == "Ground" && bird.isAlive()) {
            birdDied();
        }
        if (fix1.getUserData() == "Bird" && fix2.getUserData() == "Score" && bird.isAlive()) {
            hud.incrementScore();
            scoreSound.play();
            if (isLevel && hud.getScore() == targetScore) {
                finishLevel();
            }

        }
        if (fix1.getUserData() == "Bird" && fix2.getUserData() == "Coin" && bird.isAlive()) {
            fix2.setUserData("Remove");
            hud.incrementCoins();
            hud.takeCollectables(true, false);
        }
        if (fix1.getUserData() == "Bird" && fix2.getUserData() == "Invisible" && bird.isAlive()) {
            fix2.setUserData("Remove");
            hud.takeCollectables(false, true);
        }
        if (fix1.getUserData() == "Bird" && fix2.getUserData() == "Speed" && bird.isAlive()) {
            fix2.setUserData("Remove");
            hud.takeCollectables(true, false);
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

    public UiHud getHud() {
        return hud;
    }

    public int getTargetScore() {
        return targetScore;
    }

    public int getWinCoins() {
        return winCoins;
    }

    public boolean isLevel() {
        return isLevel;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }
}
