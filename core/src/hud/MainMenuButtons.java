package hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yalla.flappy.GameMain;

import java.util.Random;

import helpers.GameInfo;
import helpers.GameManager;
import scenes.GamePlay;
import scenes.HighScore;
import scenes.Levels;
import scenes.MainMenu;
import scenes.Market;

public class MainMenuButtons {

    private GameMain game;
    private Stage stage;
    private Viewport gameViewport;
    private ImageButton playBtn, highScoreBtn, changeBirdBtn, levelBtn, quitBtn, soundBtn, marketBtn, coinsBtn, closeGiftBtn;
    private Label coinsLbl;
    private Image giftBg, gift;
    private String sound;


    public MainMenuButtons(GameMain game) {
        this.game = game;
        gameViewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, new OrthographicCamera());
        stage = new Stage(gameViewport, game.getBatch());
        if (GameManager.getInstance().getGameData().isSoundOn()) {
            sound = "SoundOn";
        } else {
            sound = "SoundOff";
        }
        createAndPositionBtns();
        createCoinsLbl();
        changeBird();

    }

    void createAndPositionBtns() {
        playBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Play.png"))));
        highScoreBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/High Score.png"))));
        levelBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Levels.png"))));
        quitBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Quit.png"))));
        soundBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/" + sound + ".png"))));
        marketBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Market.png"))));
        playBtn.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f + 65, Align.center);
        levelBtn.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 50, Align.center);
        highScoreBtn.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 165, Align.center);
        quitBtn.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 280, Align.center);
        marketBtn.setPosition(40, GameInfo.HEIGHT - 50, Align.center);
        soundBtn.setPosition(GameInfo.WIDTH - 50, GameInfo.HEIGHT - 50, Align.center);


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
        marketBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new Market(game));
                stage.dispose();
            }
        });
        soundBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (sound == "SoundOn") {
                    GameManager.getInstance().getGameData().setSoundOn(false);
                    sound = "SoundOff";
                    GameManager.getInstance().saveData();
//                    stage.getActors().removeValue(soundBtn, true);
//                    soundBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/" + sound + ".png"))));
//                    soundBtn.setPosition(GameInfo.WIDTH - 50, GameInfo.HEIGHT - 50, Align.center);
//                    stage.addActor(soundBtn);
                    game.setScreen(new MainMenu(game));
                    stage.dispose();
                } else {
                    GameManager.getInstance().getGameData().setSoundOn(true);
                    sound = "SoundOn";
                    GameManager.getInstance().saveData();
//                    stage.getActors().removeValue(soundBtn, true);
//                    soundBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/" + sound + ".png"))));
//                    soundBtn.setPosition(GameInfo.WIDTH - 50, GameInfo.HEIGHT - 50, Align.center);
//                    stage.addActor(soundBtn);
                    game.setScreen(new MainMenu(game));
                    stage.dispose();
                }
            }
        });
        quitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
                System.exit(0);
            }
        });
        stage.addActor(highScoreBtn);
        stage.addActor(playBtn);
        stage.addActor(levelBtn);
        stage.addActor(quitBtn);
        stage.addActor(soundBtn);
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
        coinsLbl.setPosition(GameInfo.WIDTH - 78, GameInfo.HEIGHT - 162);
        coinsBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Show Coins.png"))));
        coinsBtn.setPosition(GameInfo.WIDTH - 75, GameInfo.HEIGHT - 140, Align.center);
        coinsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameManager.getInstance().getAdHandler().openVideo(1);
            }
        });
        coinsLbl.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameManager.getInstance().getAdHandler().openVideo(1);
            }
        });
        stage.addActor(coinsBtn);
        stage.addActor(coinsLbl);
    }

    void changeBird() {
        if (changeBirdBtn != null) {
            changeBirdBtn.remove();
        }
        changeBirdBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Birds/Bird" + GameManager.getInstance().getBird() + ".png"))));
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

    public void updateCoinsLbl() {
        if (GameManager.getInstance().isCoinReward()) {
            coinsLbl.setText(String.valueOf(GameManager.getInstance().getTotalCoins()));
            GameManager.getInstance().setCoinReward(false);
        }
    }


    public void showGift() {
        // gifts = {"10", "50", "100", "200", "Fire", "Inv", "All", "x2"};
        Random rand = new Random();
        int index;
        if (GameManager.getInstance().getGameData().getNumOfTotalGames() > 50) {
            index = rand.nextInt(8);
        } else {
            index = rand.nextInt(6);
        }
        switch (index) {
            case 0:
                GameManager.getInstance().addCoins(10);
                gift = new Image(new Texture("Gifts/10.png"));
                break;
            case 1:
                GameManager.getInstance().addCoins(50);
                gift = new Image(new Texture("Gifts/50.png"));
                break;
            case 2:
                GameManager.getInstance().addCoins(100);
                gift = new Image(new Texture("Gifts/100.png"));
                break;
            case 3:
                GameManager.getInstance().addCoins(200);
                gift = new Image(new Texture("Gifts/200.png"));
                break;
            case 4:
                GameManager.getInstance().getGameData().setHasFireOption(true);
                GameManager.getInstance().getGameData().setNumOfFireOption(GameManager.getInstance().getGameData().getNumOfFireOption() + 1);
                GameManager.getInstance().saveData();
                gift = new Image(new Texture("Gifts/Fire.png"));
                break;
            case 5:
                GameManager.getInstance().getGameData().setHasHideOption(true);
                GameManager.getInstance().getGameData().setNumOfHideOption(GameManager.getInstance().getGameData().getNumOfHideOption() + 1);
                GameManager.getInstance().saveData();
                gift = new Image(new Texture("Gifts/Invisible.png"));
                break;
            case 6:
                GameManager.getInstance().getGameData().setHasFireOption(true);
                GameManager.getInstance().getGameData().setNumOfFireOption(GameManager.getInstance().getGameData().getNumOfFireOption() + 1);
                GameManager.getInstance().getGameData().setHasHideOption(true);
                GameManager.getInstance().getGameData().setNumOfHideOption(GameManager.getInstance().getGameData().getNumOfHideOption() + 1);
                GameManager.getInstance().addCoins(100);
                GameManager.getInstance().saveData();
                gift = new Image(new Texture("Gifts/All.png"));
                break;
            case 7:
                GameManager.getInstance().getGameData().setHasX2Coins(true);
                GameManager.getInstance().saveData();
                break;
        }
        gift.setPosition(GameInfo.WIDTH / 2 - 40, GameInfo.HEIGHT / 2);
        giftBg = new Image(new Texture("Gifts/Gift Bg.png"));
        giftBg.setPosition(50, 70);
        closeGiftBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Gifts/Close.png"))));
        closeGiftBtn.setPosition(GameInfo.WIDTH - 110, GameInfo.HEIGHT - 193);
        closeGiftBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                stage.getActors().removeValue(closeGiftBtn, true);
                stage.getActors().removeValue(gift, true);
                stage.getActors().removeValue(giftBg, true);
            }
        });
        stage.addActor(giftBg);
        stage.addActor(gift);
        stage.addActor(closeGiftBtn);
    }

    public Stage getStage() {
        return stage;
    }
}


//RATE
/*
Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
    // To count with Play market backstack, After pressing back button,
    // to taken back to our application, we need to add following flags to intent.
    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
    try {
        startActivity(goToMarket);
    } catch (ActivityNotFoundException e) {
        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
    }
 */
