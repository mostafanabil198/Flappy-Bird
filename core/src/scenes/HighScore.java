package scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yalla.flappy.GameMain;

import helpers.GameInfo;
import helpers.GameManager;

public class HighScore implements Screen {
    private OrthographicCamera mainCamera;
    private Viewport gameViewport;
    private GameMain game;
    private Texture bg;
    private Stage stage;
    private Label scoreLbl;
    private Image crown;
    private ImageButton backBtn;

    public HighScore(GameMain game) {
        this.game = game;
        mainCamera = new OrthographicCamera();
        mainCamera.setToOrtho(false, GameInfo.WIDTH, GameInfo.HEIGHT);
        mainCamera.position.set(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, 0);
        gameViewport = new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT, mainCamera);
        bg = new Texture("Background/Night.jpg");
        stage = new Stage(gameViewport, game.getBatch());
        Gdx.input.setInputProcessor(stage);
        showScore();
        createBackBtn();
    }

    void showScore() {
        if (scoreLbl != null) {
            return;
        }
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Flappy Bird Font/04b_19.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 100;
        parameter.shadowColor = Color.GRAY;
        parameter.shadowOffsetX = 1;
        parameter.shadowOffsetY = 1;
        BitmapFont font = generator.generateFont(parameter);
        scoreLbl = new Label(String.valueOf(GameManager.getInstance().getHighScore()), new Label.LabelStyle(font, Color.WHITE));
        scoreLbl.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2, Align.center);
        crown = new Image(new Texture("Score/Crown.png"));
        crown.setPosition(GameInfo.WIDTH / 2f - 70, GameInfo.HEIGHT / 2 + 50, Align.center);
        stage.addActor(scoreLbl);
        stage.addActor(crown);
    }

    void createBackBtn() {
        backBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Back.png"))));
        backBtn.setPosition(GameInfo.WIDTH - 65, GameInfo.HEIGHT - 65, Align.center);
        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenu(game));
                stage.dispose();
            }
        });
        stage.addActor(backBtn);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.getBatch().begin();
        game.getBatch().draw(bg, 0, 0);
        game.getBatch().end();
        game.getBatch().setProjectionMatrix(stage.getCamera().combined);
        stage.draw();
        stage.act();
    }

    @Override
    public void resize(int width, int height) {

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
}
