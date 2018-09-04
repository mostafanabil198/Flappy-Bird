package hud;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
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
import pipes.Pipes;
import scenes.GamePlay;
import scenes.MainMenu;

public class UiHud {
    private Stage stage;
    private GameMain game;
    private Viewport gameViewPort;
    private Label scoreLbl, highScoreLbl, coinLbl;
    private ImageButton retryBtn, menuBtn, reviveBtn;
    private Image gameover, crown, coinImg;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    private int score;

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    private int coins;

    public UiHud(GameMain game) {
        this.game = game;
        gameViewPort = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, new OrthographicCamera());
        stage = new Stage(gameViewPort, game.getBatch());
        createLable();
        stage.addActor(scoreLbl);
        stage.addActor(coinLbl);
        stage.addActor(coinImg);
    }

    private void createLable() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Flappy Bird Font/04b_19.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 100;
        parameter.shadowColor = Color.GRAY;
        parameter.shadowOffsetX = 1;
        parameter.shadowOffsetY = 1;
        BitmapFont font = generator.generateFont(parameter);
        scoreLbl = new Label(String.valueOf(score), new Label.LabelStyle(font, Color.WHITE));
        scoreLbl.setPosition(GameInfo.WIDTH / 2 - 20, GameInfo.HEIGHT / 2 + 250);
        parameter.size = 30;
        font = generator.generateFont(parameter);
        coinLbl = new Label(String.valueOf(coins), new Label.LabelStyle(font, Color.WHITE));
        coinLbl.setPosition(50, GameInfo.HEIGHT / 2 + 250);
        coinImg = new Image(new Texture("Collectables/Coin.png"));
        coinImg.setPosition(12, GameInfo.HEIGHT / 2 + 250);
        coinImg.setSize(coinImg.getWidth() / 2, coinImg.getHeight() / 2);

    }

    private void createHighScoreLbl() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Flappy Bird Font/04b_19.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        parameter.shadowColor = Color.BLACK;
        parameter.shadowOffsetX = 1;
        parameter.shadowOffsetY = 1;
        BitmapFont font = generator.generateFont(parameter);
        highScoreLbl = new Label(String.valueOf(GameManager.getInstance().getHighScore()), new Label.LabelStyle(font, Color.WHITE));
        highScoreLbl.setPosition(GameInfo.WIDTH / 2 + 10, GameInfo.HEIGHT / 2 + 220);
        crown = new Image(new Texture("Score/Crown.png"));
        crown.setPosition(GameInfo.WIDTH / 2 - 20, GameInfo.HEIGHT / 2 + 230);
    }

    public Stage getStage() {
        return stage;
    }

    public void incrementScore() {
        score++;
        scoreLbl.setText(String.valueOf(score));
    }

    public void incrementCoins() {
        coins++;
        coinLbl.setText(String.valueOf(coins));
    }

    public void showScore() {
        if (gameover != null) {
            gameover.clear();
        }
        gameover = new Image(new Texture("Buttons/Game over.png"));
        final RunnableAction gameoverAction = new RunnableAction();
        gameoverAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                gameover.setPosition(GameInfo.WIDTH / 2, GameInfo.HEIGHT / 2 + 110, Align.center);
                stage.addActor(gameover);
            }
        });

        RunnableAction btns = new RunnableAction();
        btns.setRunnable(new Runnable() {
            @Override
            public void run() {
                createButtons();
                scoreLbl.setText(String.valueOf(score));
                stage.addActor(scoreLbl);
                createHighScoreLbl();
                stage.addActor(highScoreLbl);
                stage.addActor(crown);
            }
        });
        SequenceAction sa = new SequenceAction();
        sa.addAction(Actions.fadeOut(.5f));
        sa.addAction(Actions.delay(.5f));
        sa.addAction(Actions.fadeIn(.5f));
        sa.addAction(gameoverAction);
        sa.addAction(Actions.delay(.5f));
        //sa.addAction(Actions.delay(.5f));
        sa.addAction(btns);
        stage.addAction(sa);

    }

    public void createButtons() {
        retryBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Again.png"))));
        menuBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Menu.png"))));
        reviveBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Revive.png"))));
        if (GameManager.getInstance().getCanRevive()) {
            retryBtn.setPosition(85, GameInfo.HEIGHT / 2 - 60, Align.center);
            menuBtn.setPosition(GameInfo.WIDTH / 2 + 150, GameInfo.HEIGHT / 2 - 60, Align.center);
            reviveBtn.setPosition(GameInfo.WIDTH / 2, GameInfo.HEIGHT / 2 - 60, Align.center);
            stage.addActor(reviveBtn);
        } else {
            retryBtn.setPosition(GameInfo.WIDTH / 2 - 100, GameInfo.HEIGHT / 2 - 60, Align.center);
            menuBtn.setPosition(GameInfo.WIDTH / 2 + 100, GameInfo.HEIGHT / 2 - 60, Align.center);
        }
        retryBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GamePlay(game));
                stage.dispose();
                GameManager.getInstance().setCanRevive(true);
            }
        });

        menuBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenu(game));
                stage.dispose();
                GameManager.getInstance().setCanRevive(false);
            }
        });

        reviveBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GamePlay g = new GamePlay(game);
                game.setScreen(g);
                stage.dispose();
                g.getHud().setScore(--score);
                g.getHud().incrementScore();
                GameManager.getInstance().reviveCoin(coins);
                g.getHud().setCoins(--coins);
                g.getHud().incrementCoins();
                GameManager.getInstance().setCanRevive(false);
            }
        });

        stage.addActor(retryBtn);
        stage.addActor(menuBtn);
    }

}




