package hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yalla.flappy.GameMain;

import helpers.GameInfo;
import helpers.GameManager;
import scenes.GamePlay;
import scenes.HighScore;
import scenes.Levels;

public class MainMenuButtons {

    private GameMain game;
    private Stage stage;
    private Viewport gameViewport;
    private ImageButton playBtn, highScoreBtn, changeBirdBtn, levelBtn, rankBtn, settingsBtn, marketBtn, coinsBtn;
    private Label coinsLbl;
    private Image crown;


    public MainMenuButtons(GameMain game) {
        this.game = game;
        gameViewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, new OrthographicCamera());
        stage = new Stage(gameViewport, game.getBatch());
        createAndPositionBtns();
        createCoinsLbl();
        changeBird();

    }

    void createAndPositionBtns() {
        playBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Play.png"))));
        highScoreBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/High Score.png"))));
        levelBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Levels.png"))));
        rankBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Rank.png"))));
        settingsBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Settings.png"))));
        marketBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Market.png"))));
        playBtn.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f + 65, Align.center);
        levelBtn.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 50, Align.center);
        highScoreBtn.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 165, Align.center);
        rankBtn.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 280, Align.center);
        marketBtn.setPosition(40, GameInfo.HEIGHT - 50, Align.center);
        settingsBtn.setPosition(GameInfo.WIDTH - 50, GameInfo.HEIGHT - 50, Align.center);


        playBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GamePlay(game, false, 0, 0, 0));
                stage.dispose();
                GameManager.getInstance().setCanRevive(true);
            }
        });

        highScoreBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new HighScore(game));
                stage.dispose();
            }
        });

        levelBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new Levels(game));
                stage.dispose();
            }
        });
        stage.addActor(highScoreBtn);
        stage.addActor(playBtn);
        stage.addActor(levelBtn);
        stage.addActor(rankBtn);
        stage.addActor(settingsBtn);
        stage.addActor(marketBtn);
    }

    void createCoinsLbl() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Flappy Bird Font/04b_19.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        parameter.shadowColor = Color.GRAY;
        parameter.shadowOffsetX = 1;
        parameter.shadowOffsetY = 1;
        BitmapFont font = generator.generateFont(parameter);
        coinsLbl = new Label(String.valueOf(GameManager.getInstance().getTotalCoins()), new Label.LabelStyle(font, Color.WHITE));
        coinsLbl.setPosition(GameInfo.WIDTH - 60, GameInfo.HEIGHT - 157);
        coinsBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Show Coins.png"))));
        coinsBtn.setPosition(GameInfo.WIDTH - 60, GameInfo.HEIGHT - 140, Align.center);
        stage.addActor(coinsBtn);
        stage.addActor(coinsLbl);
    }

    void changeBird() {
        if (changeBirdBtn != null) {
            changeBirdBtn.remove();
        }
        changeBirdBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Birds/" + GameManager.getInstance().getBird() + "/Idle.png"))));
        changeBirdBtn.setPosition(GameInfo.WIDTH / 2, GameInfo.HEIGHT / 2 + 210, Align.center);
        changeBirdBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameManager.getInstance().incrementIndex();
                changeBird();
            }
        });
        stage.addActor(changeBirdBtn);

    }


    public Stage getStage() {
        return stage;
    }
}
