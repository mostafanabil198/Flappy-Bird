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


import bird.Bird;
import helpers.GameInfo;
import helpers.GameManager;
import scenes.GamePlay;
import scenes.Levels;
import scenes.MainMenu;

public class UiHud {
    private Stage stage;
    private GameMain game;
    private Viewport gameViewPort;
    private Label scoreLbl, highScoreLbl, coinLbl, invisibleTime;
    private ImageButton retryBtn, menuBtn, reviveBtn, backToLevelsBtn, nextLevelBtn, useFireBtn, useHideBtn;
    private Image gameover, crown, coinImg, invisibleImg;
    private Bird bird;
    private int coins;
    private GamePlay gamePlay;
    private int score;
    private BitmapFont font;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private int time;
    private FreeTypeFontGenerator generator;
    private Label speedTime;
    private Image speedImg;
    private String x2;

    public UiHud(GameMain game, GamePlay gamePlay, Bird bird) {
        this.game = game;
        this.gamePlay = gamePlay;
        this.bird = bird;
        if (GameManager.getInstance().getGameData().isHasX2Coins()) {
            x2 = " x2";
        } else {
            x2 = "";
        }
        gameViewPort = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, new OrthographicCamera());
        stage = new Stage(gameViewPort, game.getBatch());
        createLable();
        stage.addActor(scoreLbl);
        stage.addActor(coinLbl);
        stage.addActor(coinImg);

        createUseBtns();
    }

    private void createLable() {
        generator = new FreeTypeFontGenerator(Gdx.files.internal("Flappy Bird Font/04b_19.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 100;
        parameter.shadowColor = Color.GRAY;
        parameter.shadowOffsetX = 1;
        parameter.shadowOffsetY = 1;
        font = generator.generateFont(parameter);
        if (gamePlay.isLevel()) {
            parameter.size = 50;
            font = generator.generateFont(parameter);
            scoreLbl = new Label(String.valueOf(score) + "/" + String.valueOf(gamePlay.getTargetScore()), new Label.LabelStyle(font, Color.WHITE));
            scoreLbl.setPosition(GameInfo.WIDTH / 2 - 50, GameInfo.HEIGHT / 2 + 285);
        } else {
            scoreLbl = new Label(String.valueOf(score), new Label.LabelStyle(font, Color.WHITE));
            scoreLbl.setPosition(GameInfo.WIDTH / 2 - 20, GameInfo.HEIGHT / 2 + 250);
        }

        parameter.size = 30;
        font = generator.generateFont(parameter);
        coinLbl = new Label(String.valueOf(coins) + x2, new Label.LabelStyle(font, Color.WHITE));
        coinLbl.setPosition(50, GameInfo.HEIGHT / 2 + 250);

        coinImg = new Image(new Texture("Collectables/Coin.png"));
        coinImg.setPosition(12, GameInfo.HEIGHT / 2 + 250);
        coinImg.setSize(coinImg.getWidth() / 2, coinImg.getHeight() / 2);

    }

    private void createHighScoreLbl() {
        generator = new FreeTypeFontGenerator(Gdx.files.internal("Flappy Bird Font/04b_19.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        parameter.shadowColor = Color.BLACK;
        parameter.shadowOffsetX = 1;
        parameter.shadowOffsetY = 1;
        font = generator.generateFont(parameter);
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
        if (gamePlay.isLevel()) {
            scoreLbl.setText(String.valueOf(score) + "/" + String.valueOf(gamePlay.getTargetScore()));
        } else {
            scoreLbl.setText(String.valueOf(score));
        }
    }

    public void incrementCoins() {
        coins++;
        coinLbl.setText(String.valueOf(coins) + x2);

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
                if (gamePlay.isLevel()) {
                    scoreLbl.setText(String.valueOf(score) + "/" + String.valueOf(gamePlay.getTargetScore()));
                } else {
                    scoreLbl.setText(String.valueOf(score));
                }
                stage.addActor(scoreLbl);
                createHighScoreLbl();
                if (!gamePlay.isLevel()) {
                    stage.addActor(highScoreLbl);
                    stage.addActor(crown);
                }
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


    public void winLevel() {
        if (gameover != null) {
            gameover.clear();
        }
        gameover = new Image(new Texture("Buttons/LevelCompleted.png"));
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
                createWinLevelButtons();
                scoreLbl.setText(String.valueOf(score) + "/" + String.valueOf(gamePlay.getTargetScore()));
                stage.addActor(scoreLbl);
                //createHighScoreLbl();
                //stage.addActor(highScoreLbl);
                //stage.addActor(crown);
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

    public void createUseBtns() {
        if (GameManager.getInstance().getGameData().isHasHideOption()) {
            useHideBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Collectables/Invisible.png"))));
            useHideBtn.setSize(useHideBtn.getWidth(), useHideBtn.getHeight());
            useHideBtn.setPosition(10, 20);
            useHideBtn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    takeCollectables(false, true);
                    GameManager.getInstance().getGameData().setNumOfHideOption(GameManager.getInstance().getGameData().getNumOfHideOption() - 1);
                    if (GameManager.getInstance().getGameData().getNumOfHideOption() == 0) {
                        GameManager.getInstance().getGameData().setHasHideOption(false);
                    }
                    GameManager.getInstance().saveData();
                    stage.getActors().removeValue(useHideBtn, true);
                }
            });
            stage.addActor(useHideBtn);
        }

        if (GameManager.getInstance().getGameData().isHasFireOption()) {
            useFireBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Collectables/Fire.png"))));
            useFireBtn.setSize(useFireBtn.getWidth(), useFireBtn.getHeight());
            useFireBtn.setPosition(100, 20);
            useFireBtn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    fire();
                    GameManager.getInstance().getGameData().setNumOfFireOption(GameManager.getInstance().getGameData().getNumOfFireOption() - 1);
                    if (GameManager.getInstance().getGameData().getNumOfFireOption() == 0) {
                        GameManager.getInstance().getGameData().setHasFireOption(false);
                    }
                    GameManager.getInstance().saveData();
                    stage.getActors().removeValue(useFireBtn, true);

                }
            });
            stage.addActor(useFireBtn);
        }
    }

    private void createWinLevelButtons() {
        backToLevelsBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/LevelsSmall.png"))));
        nextLevelBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/NextLevel.png"))));
        backToLevelsBtn.setPosition(GameInfo.WIDTH / 2 - 100, GameInfo.HEIGHT / 2 - 60, Align.center);
        nextLevelBtn.setPosition(GameInfo.WIDTH / 2 + 100, GameInfo.HEIGHT / 2 - 60, Align.center);

        nextLevelBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GamePlay(game, true, gamePlay.getCurrentLevel() + 1, GameManager.getInstance().getGameData().getLevelsScores()[gamePlay.getCurrentLevel()], GameManager.getInstance().getGameData().getLevelsCoins()[gamePlay.getCurrentLevel()]));
                stage.dispose();
            }
        });

        backToLevelsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new Levels(game));
                stage.dispose();
                GameManager.getInstance().setCanRevive(true);
            }
        });

        stage.addActor(backToLevelsBtn);
        stage.addActor(nextLevelBtn);
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
                game.setScreen(new GamePlay(game, gamePlay.isLevel(), gamePlay.getCurrentLevel(), gamePlay.getTargetScore(), gamePlay.getWinCoins()));
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
                GamePlay g = new GamePlay(game, gamePlay.isLevel(), gamePlay.getCurrentLevel(), gamePlay.getTargetScore(), gamePlay.getWinCoins());
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


    public void takeCollectables(final boolean speed, final boolean invisible) {
        final RunnableAction start = new RunnableAction();
        start.setRunnable(new Runnable() {
            @Override
            public void run() {
                bird.setHasInvisible(invisible || speed);
                bird.setHasSpeed(speed);
                GameManager.getInstance().setInvisible(invisible || speed);
                GameManager.getInstance().setSpeed(speed);
                gamePlay.makePipesSensors(speed || invisible);
                parameter.size = 30;
                font = generator.generateFont(parameter);
                if (time != 5) {
                    time += 5;
                }
                if (invisible) {
                    bird.createAnimation(" Inv");
                    invisibleTime = new Label("", new Label.LabelStyle(font, Color.WHITE));
                    invisibleTime.setPosition(50, GameInfo.HEIGHT / 2 + 210);
                    invisibleImg = new Image(new Texture("Collectables/Invisible.png"));
                    invisibleImg.setPosition(12, GameInfo.HEIGHT / 2 + 200);
                    invisibleImg.setSize(invisibleImg.getWidth() / 2, invisibleImg.getHeight() / 2);
                    updateInvisibleTimeLbl();
                    stage.addActor(invisibleImg);
                    stage.addActor(invisibleTime);
                } else if (speed) {
//                    gamePlay.time = 0.8f;
//                    gamePlay.removeSequance();
//                    gamePlay.createAllPipes();
                    speedTime = new Label("", new Label.LabelStyle(font, Color.WHITE));
                    speedTime.setPosition(50, GameInfo.HEIGHT / 2 + 160);
                    speedImg = new Image(new Texture("Collectables/Speed.png"));
                    speedImg.setPosition(12, GameInfo.HEIGHT / 2 + 150);
                    speedImg.setSize(speedImg.getWidth() / 2, speedImg.getHeight() / 2);
                    updateSpeedTimeLbl();
                    stage.addActor(speedImg);
                    stage.addActor(speedTime);
                }


            }
        });

        RunnableAction stop = new RunnableAction();
        stop.setRunnable(new Runnable() {
            @Override
            public void run() {
//                gamePlay.time = 1.5f;
//                gamePlay.removeSequance();
//                gamePlay.createAllPipes();
                bird.setHasInvisible(false);
                bird.setHasSpeed(false);
                GameManager.getInstance().setInvisible(false);
                GameManager.getInstance().setSpeed(false);
                bird.createAnimation("");
                gamePlay.makePipesSensors(false);
                if (time <= 1) {
                    time = 5;
                }
                if (speed) {
                    stage.getActors().removeValue(speedImg, true);
                    stage.getActors().removeValue(speedTime, true);
                }
                if (invisible) {
                    stage.getActors().removeValue(invisibleImg, true);
                    stage.getActors().removeValue(invisibleTime, true);
                }
            }
        });

        SequenceAction sa = new SequenceAction();
        sa.addAction(start);
        sa.addAction(Actions.delay(5));
        sa.addAction(stop);
        stage.addAction(sa);

    }

    public void fire() {
        RunnableAction run = new RunnableAction();
        run.setRunnable(new Runnable() {
            @Override
            public void run() {
                bird.setHasFire(true);
                gamePlay.createFire();
            }
        });

        SequenceAction sa = new SequenceAction();
        sa.addAction(run);
        sa.addAction(Actions.delay(.1f));
        stage.addAction(Actions.repeat(20, sa));
    }


    public void updateInvisibleTimeLbl() {
        invisibleTime.setText(String.valueOf(time));
    }

    public void updateSpeedTimeLbl() {
        speedTime.setText(String.valueOf(time));
    }


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }


}


//    public double subtractDays(String mailDate) throws ParseException {
//        String todayDate = h.todayDate();
//        Date d1 = new SimpleDateFormat("yyyy-MM-dd").parse(mailDate);
//        Date d2 = new SimpleDateFormat("yyyy-MM-dd").parse(todayDate);
//        double difference = (double) Math.abs(d2.getTime() - d1.getTime());
//        double dif = difference / (60 * 60 * 1000);
//        return dif;
//    }
//
//
//    public String todayDate() {
//        Date d = new Date();
//        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
//        return ft.format(d);
//    }