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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yalla.flappy.GameMain;

import helpers.GameInfo;
import helpers.GameManager;

public class Levels implements Screen {
    private OrthographicCamera mainCamera;
    private Viewport gameViewport;
    private GameMain game;
    private Texture bg;
    private Stage stage;
    private ImageButton backBtn, playLevelBtn, closeLevelBtn;
    private ImageButton[] levelsBtns;
    private Label[] levelsLbls;
    private Label scoreLbl, coinsLbl;
    private Image levelInfo;
    int i;
    private boolean levelInfoOpened;

    public Levels(GameMain game) {
        this.game = game;
        mainCamera = new OrthographicCamera();
        mainCamera.setToOrtho(false, GameInfo.WIDTH, GameInfo.HEIGHT);
        mainCamera.position.set(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, 0);
        gameViewport = new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT, mainCamera);
        bg = new Texture("Background/LevelsBg.png");
        stage = new Stage(gameViewport, game.getBatch());
        Gdx.input.setInputProcessor(stage);
        levelsBtns = new ImageButton[GameManager.getInstance().getGameData().getTotalShownLevels()];
        levelsLbls = new Label[GameManager.getInstance().getGameData().getUserCurrentLevel()];
        createBackBtn();
        createLevelsBtns();


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

    void createLevelsBtns() {


        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Flappy Bird Font/04b_19.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;
        parameter.shadowOffsetX = 1;
        parameter.shadowOffsetY = 1;
        parameter.shadowColor = Color.GRAY;
        BitmapFont font = generator.generateFont(parameter);

        for (i = 0; i < GameManager.getInstance().getGameData().getUserCurrentLevel(); i++) {
            levelsBtns[i] = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/LevelOpen.png"))));
            levelsBtns[i].setPosition(50 + 100 * (i % 4), (GameInfo.HEIGHT / 2) - ((int) (.25 * i)) * 100);
            final int a = i;
            levelsBtns[i].addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (!levelInfoOpened) {
                        showLevelInfo(a + 1);
                        levelInfoOpened = true;
                    }
                }
            });

            levelsLbls[i] = new Label(String.valueOf(i + 1), new Label.LabelStyle(font, Color.WHITE));
            if (i >= 9) {
                levelsLbls[i].setPosition(68 + 100 * (i % 4), (GameInfo.HEIGHT / 2 + 20) - ((int) (.25 * i)) * 100);
            } else {
                levelsLbls[i].setPosition(75 + 100 * (i % 4), (GameInfo.HEIGHT / 2 + 20) - ((int) (.25 * i)) * 100);
            }
            levelsLbls[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (!levelInfoOpened) {
                        showLevelInfo(a + 1);
                        levelInfoOpened = true;
                    }
                }
            });

            stage.addActor(levelsBtns[i]);
            stage.addActor(levelsLbls[i]);
        }

        for (i = i; i < GameManager.getInstance().getGameData().getTotalShownLevels(); i++) {
            levelsBtns[i] = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/LevelClose.png"))));
            levelsBtns[i].setPosition(50 + 100 * (i % 4), (GameInfo.HEIGHT / 2) - ((int) (.25 * i)) * 100);
            stage.addActor(levelsBtns[i]);
        }
    }

    void showLevelInfo(final int level) {

        levelInfo = new Image(new Texture("Levels/LevelInfo.png"));
        levelInfo.setPosition(GameInfo.WIDTH / 2 - levelInfo.getWidth() / 2, GameInfo.HEIGHT / 2 - levelInfo.getHeight() / 2);
        stage.addActor(levelInfo);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Flappy Bird Font/ARLRDBD.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;
        parameter.borderColor = Color.ORANGE;
        parameter.borderWidth = 2;
        BitmapFont font = generator.generateFont(parameter);
        scoreLbl = new Label(String.valueOf(GameManager.getInstance().getGameData().getLevelsScores()[level - 1]), new Label.LabelStyle(font, Color.WHITE));
        scoreLbl.setPosition(GameInfo.WIDTH / 2 + 45, GameInfo.HEIGHT / 2 + 15);
        coinsLbl = new Label(String.valueOf(GameManager.getInstance().getGameData().getLevelsCoins()[level - 1]), new Label.LabelStyle(font, Color.WHITE));
        coinsLbl.setPosition(GameInfo.WIDTH / 2 + 45, GameInfo.HEIGHT / 2 - 45);

        closeLevelBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/XBtn.png"))));
        closeLevelBtn.setPosition(GameInfo.WIDTH / 2 + 65, GameInfo.HEIGHT / 2 - 115, Align.center);
        closeLevelBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                stage.getActors().removeValue(levelInfo, true);
                stage.getActors().removeValue(scoreLbl, true);
                stage.getActors().removeValue(coinsLbl, true);
                stage.getActors().removeValue(playLevelBtn, true);
                stage.getActors().removeValue(closeLevelBtn, true);
                levelInfoOpened = false;
            }
        });

        playLevelBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/RightBtn.png"))));
        playLevelBtn.setPosition(GameInfo.WIDTH / 2 - 75, GameInfo.HEIGHT / 2 - 115, Align.center);
        playLevelBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //start game with this level
                GameManager.getInstance().setCanRevive(false);
                game.setScreen(new GamePlay(game, true, level, GameManager.getInstance().getGameData().getLevelsScores()[level - 1], GameManager.getInstance().getGameData().getLevelsCoins()[level - 1]));
                stage.dispose();
            }
        });

        stage.addActor(playLevelBtn);
        stage.addActor(closeLevelBtn);
        stage.addActor(scoreLbl);
        stage.addActor(coinsLbl);

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

    }
}
