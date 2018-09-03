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
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yalla.flappy.GameMain;


import helpers.GameInfo;
import scenes.GamePlay;
import scenes.MainMenu;

public class UiHud {
    private Stage stage;
    private GameMain game;
    private Viewport gameViewPort;
    private Label scoreLbl;
    private ImageButton retryBtn, quitBtn;
    private int score;


    public UiHud(GameMain game) {
        this.game = game;
        gameViewPort = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, new OrthographicCamera());
        stage = new Stage(gameViewPort, game.getBatch());
        createLable();
        stage.addActor(scoreLbl);
    }

    private void createLable() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Flappy Bird Font/04b_19.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 100;
        BitmapFont font = generator.generateFont(parameter);
        scoreLbl = new Label(String.valueOf(score), new Label.LabelStyle(font, Color.WHITE));
        scoreLbl.setPosition(GameInfo.WIDTH / 2 - 20, GameInfo.HEIGHT / 2 + 250);

    }

    public Stage getStage() {
        return stage;
    }

    public void incrementScore() {
        score++;
        scoreLbl.setText(String.valueOf(score));
    }

    public void showScore() {
        scoreLbl.setText(String.valueOf(score));
        stage.addActor(scoreLbl);
    }

    public void createButtons() {
        retryBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Retry.png"))));
        quitBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Quit.png"))));
        retryBtn.setPosition(GameInfo.WIDTH / 2 - 100, GameInfo.HEIGHT / 2 - 50, Align.center);
        quitBtn.setPosition(GameInfo.WIDTH / 2 + 100, GameInfo.HEIGHT / 2 - 50, Align.center);
        retryBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GamePlay(game));
                stage.dispose();
            }
        });

        quitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenu(game));
                stage.dispose();
            }
        });

        stage.addActor(retryBtn);
        stage.addActor(quitBtn);
    }
}
