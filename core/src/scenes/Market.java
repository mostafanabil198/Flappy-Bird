package scenes;

import com.badlogic.gdx.Game;
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

public class Market implements Screen {
    private OrthographicCamera mainCamera;
    private Viewport gameViewport;
    private GameMain game;
    private Texture bg;
    private Stage stage;
    private ImageButton backBtn;
    private ImageButton[] birdsBtns;
    private Label[] birdsLbls;
    private Label coinsLbl;
    int i;

    public Market(GameMain game) {
        this.game = game;
        mainCamera = new OrthographicCamera();
        mainCamera.setToOrtho(false, GameInfo.WIDTH, GameInfo.HEIGHT);
        mainCamera.position.set(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, 0);
        gameViewport = new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT, mainCamera);
        bg = new Texture("Background/Market.png");
        stage = new Stage(gameViewport, game.getBatch());
        Gdx.input.setInputProcessor(stage);
        birdsBtns = new ImageButton[GameManager.getInstance().getGameData().getAllBirds().length];
        birdsLbls = new Label[GameManager.getInstance().getGameData().getAllBirds().length];
        createBackBtn();
        createBirdsBtns();

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


    void createBirdsBtns() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Flappy Bird Font/04b_19.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        parameter.shadowOffsetX = 1;
        parameter.shadowOffsetY = 1;
        parameter.shadowColor = Color.GRAY;
        BitmapFont font = generator.generateFont(parameter);
        coinsLbl = new Label(String.valueOf(GameManager.getInstance().getGameData().getCoins()), new Label.LabelStyle(font, Color.WHITE));
        coinsLbl.setPosition(75, GameInfo.HEIGHT - 70);
        stage.addActor(coinsLbl);
        parameter.shadowColor = Color.BLUE;
        parameter.size = 20;
        font = generator.generateFont(parameter);

        for (i = 0; i < GameManager.getInstance().getGameData().getAllBirds().length; i++) {

            if (GameManager.getInstance().getGameData().getAllBirds()[i]) {
                //I have this bird  USEEEE
                birdsBtns[i] = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Use.png"))));
                final int a = i;
                birdsBtns[i].addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        GameManager.getInstance().setBirdIndex(a);
                    }
                });
            } else {
                //I DON'T have this bird  BUYYYYYY
                birdsBtns[i] = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Buy.png"))));
                final int a = i;
                birdsBtns[i].addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        if (GameManager.getInstance().getGameData().getCoins() >= GameManager.getInstance().getGameData().getBirdsPrices()[a]) {
                            GameManager.getInstance().getGameData().setCoins(GameManager.getInstance().getGameData().getCoins() - GameManager.getInstance().getGameData().getBirdsPrices()[a]);
                            GameManager.getInstance().getGameData().getAllBirds()[a] = true;
//                            GameManager.getInstance().getGameData().getMyBirds().add(a);
                            GameManager.getInstance().saveData();
                            game.setScreen(new Market(game));
                            stage.dispose();
                        }
                    }
                });

                birdsLbls[i] = new Label(String.valueOf(GameManager.getInstance().getGameData().getBirdsPrices()[i]), new Label.LabelStyle(font, Color.WHITE));
                if (i >= 2) {
                    birdsLbls[i].setPosition(65 + 100 * (i % 4), (GameInfo.HEIGHT / 2 + 60) - ((int) (.25 * i)) * 170);
                } else {
                    birdsLbls[i].setPosition(73 + 100 * (i % 4), (GameInfo.HEIGHT / 2 + 60) - ((int) (.25 * i)) * 170);
                }
                birdsLbls[i].addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (GameManager.getInstance().getGameData().getCoins() >= GameManager.getInstance().getGameData().getBirdsPrices()[a]) {
                            GameManager.getInstance().getGameData().setCoins(GameManager.getInstance().getGameData().getCoins() - GameManager.getInstance().getGameData().getBirdsPrices()[a]);
                            GameManager.getInstance().getGameData().getAllBirds()[a] = true;
                            GameManager.getInstance().getGameData().getMyBirds().add(a);
                            game.setScreen(new Market(game));
                            stage.dispose();
                        }
                    }
                });
            }
            birdsBtns[i].setPosition(50 + 100 * (i % 4), (GameInfo.HEIGHT / 2 + 45) - ((int) (.25 * i)) * 170);
            stage.addActor(birdsBtns[i]);
            if (!GameManager.getInstance().getGameData().getAllBirds()[i]) {
                stage.addActor(birdsLbls[i]);
            }
        }

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
