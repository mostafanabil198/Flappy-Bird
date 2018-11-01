package scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yalla.flappy.GameMain;

import helpers.GameInfo;
import helpers.GameManager;
import hud.MainMenuButtons;

public class MainMenu implements Screen {

    private GameMain game;
    private MainMenuButtons btns;
    private OrthographicCamera mainCamera;
    private Viewport gameViewport;
    private Array<Sprite> bgs = new Array<Sprite>();

    public MainMenu(GameMain game) {
        this.game = game;
        mainCamera = new OrthographicCamera();
        mainCamera.setToOrtho(false, GameInfo.WIDTH, GameInfo.HEIGHT);
        mainCamera.position.set(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, 0);
        gameViewport = new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT, mainCamera);
        createBackgrounds();
        btns = new MainMenuButtons(game);
        Gdx.input.setInputProcessor(btns.getStage());
        if (GameManager.getInstance().isFirstDay() || GameManager.getInstance().dailyPrize()) {
            btns.showGift();
            GameManager.getInstance().setFirstDay(false);
        }


    }

    void createBackgrounds() {
        for (int i = 0; i < 3; i++) {
            Sprite bg = new Sprite(new Texture("Background/Night.jpg"));
            bg.setPosition(i * bg.getWidth(), 0);
            bgs.add(bg);
        }
    }

    void moveBackgrounds() {
        for (Sprite bg : bgs) {
            float x1 = bg.getX() - 1f;
            bg.setPosition(x1, bg.getY());
            if (bg.getX() + bg.getWidth() / 2 + GameInfo.WIDTH < mainCamera.position.x) {
                bg.setPosition(bg.getX() + bg.getWidth() * bgs.size, bg.getY());
            }
        }
    }

    void drawBackgrounds(SpriteBatch batch) {
        for (Sprite s : bgs) {
            batch.draw(s, s.getX(), s.getY());
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        moveBackgrounds();
        btns.updateCoinsLbl();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.getBatch().begin();
        drawBackgrounds(game.getBatch());
        game.getBatch().end();
        game.getBatch().setProjectionMatrix(btns.getStage().getCamera().combined);
        btns.getStage().draw();
        btns.getStage().act();
    }


    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height);
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
        for (Sprite bg : bgs) {
            bg.getTexture().dispose();
        }


    }
}
