package helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.yalla.flappy.AdHandler;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import scenes.GamePlay;

public class GameManager {
    private static final GameManager ourInstance = new GameManager();
    int birdIndex = 0;
    public int score;
    private GameData gameData;
    private Json json = new Json();
    private FileHandle fileHandle = Gdx.files.local("bin/gameData.json");
    public Array<Sprite> collectables = new Array<Sprite>();
    private Boolean canRevive;
    private boolean isInvisible = false;
    private boolean isSpeed = false;
    private int numOfGames = 0;
    private int numOfTotalGames;
    private int numOfPipes;
    private AdHandler adHandler;
    private boolean coinReward;
    private boolean lifeReward;
    private boolean firstDay;
    GregorianCalendar calendarG = new GregorianCalendar();

    public void setNumOfTotalGames(int numOfTotalGames) {
        this.numOfTotalGames = numOfTotalGames;
        gameData.setNumOfTotalGames(numOfTotalGames);
        saveData();
    }


    private GameManager() {
    }

    public void initializeGameData() {
        if (!fileHandle.exists()) {
            gameData = new GameData();
            gameData.setHighScore(0);
            gameData.setCoins(0);
            gameData.setUserCurrentLevel(1);
            gameData.setAllBirds(new boolean[8]);
            gameData.getAllBirds()[0] = true;
            gameData.setMyBirds(new Array<Integer>());
            gameData.getMyBirds().add(0);
            gameData.setCurrentBird(birdIndex);
            gameData.setNumOfTotalGames(0);
            gameData.setHasX2Coins(false);
            gameData.setHasFireOption(false);
            gameData.setHasHideOption(false);
            calendarG.setTime(new Date());
            gameData.setLastDayIn(Integer.valueOf(String.valueOf(calendarG.get(Calendar.DAY_OF_YEAR))));
            firstDay = true;
            gameData.setSoundOn(true);
            saveData();
        } else {
            loadData();
            firstDay = false;
            birdIndex = gameData.getCurrentBird();
            numOfTotalGames = gameData.getNumOfTotalGames();
        }
    }

    private void loadData() {
        gameData = json.fromJson(GameData.class, Base64Coder.decodeString(fileHandle.readString()));
    }

    public void saveData() {
        if (gameData != null) {
            fileHandle.writeString(Base64Coder.encodeString(json.prettyPrint(gameData)), false);
        }
    }


    public void addCoins(int num) {
        gameData.setCoins(gameData.getCoins() + num);
        saveData();
    }

    public void reviveCoin(int num) {
        gameData.setCoins(gameData.getCoins() - num);
        saveData();
    }


    public void checkForNewHighScore() {
        int oldHighScore = gameData.getHighScore();

        if (oldHighScore < score) {
            gameData.setHighScore(score);
            saveData();
        }

    }

    public void buyBird(int num) {
        gameData.getAllBirds()[num] = true;
        gameData.getMyBirds().add(num);
        saveData();
    }


    public void incrementIndex() {
        birdIndex++;
        if (birdIndex >= gameData.getAllBirds().length) {
            birdIndex = 0;
        }
        while (!gameData.getAllBirds()[birdIndex]) {
            birdIndex++;
            if (birdIndex >= gameData.getAllBirds().length) {
                birdIndex = 0;
                break;
            }
        }
        gameData.setCurrentBird(birdIndex);
        saveData();
    }

    public int getTotalCoins() {
        return gameData.getCoins();
    }

    public int getNumOfPipes() {
        return numOfPipes;
    }

    public void setNumOfPipes(int numOfPipes) {
        this.numOfPipes = numOfPipes;
    }

    public int getNumOfGames() {
        return numOfGames;
    }

    public void setNumOfGames(int numOfGames) {
        this.numOfGames = numOfGames;
    }

    public int getNumOfTotalGames() {
        return numOfTotalGames;
    }

    public GameData getGameData() {
        return gameData;
    }

    public int getHighScore() {
        return gameData.getHighScore();
    }

    public int getBird() {
        return birdIndex;
    }


    public Boolean getCanRevive() {
        return canRevive;
    }

    public void setCanRevive(Boolean canRevive) {
        this.canRevive = canRevive;
    }

    public boolean isInvisible() {
        return isInvisible;
    }

    public void setInvisible(boolean invisible) {
        isInvisible = invisible;
    }


    public boolean isSpeed() {
        return isSpeed;
    }

    public void setSpeed(boolean speed) {
        isSpeed = speed;
    }


    public static GameManager getInstance() {
        return ourInstance;
    }


    public int getBirdIndex() {
        return birdIndex;
    }


    public boolean isCoinReward() {
        return coinReward;
    }

    public void setCoinReward(boolean coinReward) {
        this.coinReward = coinReward;
    }

    public boolean isLifeReward() {
        return lifeReward;
    }

    public void setLifeReward(boolean lifeReward) {
        this.lifeReward = lifeReward;
    }

    public void setBirdIndex(int birdIndex) {
        this.birdIndex = birdIndex;
    }


    public AdHandler getAdHandler() {
        return adHandler;
    }

    public void setAdHandler(AdHandler adHandler) {
        this.adHandler = adHandler;
    }

    public void coinsReward() {
        addCoins(25);
        setCoinReward(true);
    }

    public void coinsReward1() {
        addCoins(1);
        setCoinReward(true);
    }

    public void extraLifeReward() {
        setLifeReward(true);
    }


    public boolean isFirstDay() {
        return firstDay;
    }

    public void setFirstDay(boolean firstDay) {
        this.firstDay = firstDay;
    }

    public boolean dailyPrize() {
        int today = Integer.valueOf((String.valueOf(calendarG.get(Calendar.DAY_OF_YEAR))));
        int lastTime = gameData.getLastDayIn();
        gameData.setLastDayIn(today);
        saveData();
        return (today == lastTime + 1);
    }

//    Preferences preferences=Gdx.app.getPreferences("MyPref");
//    String LAST_LOGIN_DAY="lastloginday";
//
//    GregorianCalendar calendarG = new GregorianCalendar();
//calendarG.setTime(new Date());
//
//
//if(!preferences.contains(LAST_LOGIN_DAY)) {
//        //first day in App
//        preferences.putInteger(LAST_LOGIN_DAY, calendarG.get(Calendar.DAY_OF_YEAR));
//        preferences.flush();
//    }
//
//if(preferences.getInteger(LAST_LOGIN_DAY)-1==calendarG.get(Calendar.DAY_OF_YEAR)){
//        //next loginday up to a year
//
//        updateValue(preferences,calendarG);
//
//    }else{
//
//        if(calendarG.get(Calendar.DAY_OF_YEAR)==1) {
//
//            // check for the 1st day of the year
//
//            boolean isLeap = calendarG.isLeapYear(calendarG.get(Calendar.YEAR));
//            if (isLeap && preferences.getInteger(LAST_LOGIN_DAY)==366 ) {
//
//                updateValue(preferences,calendarG);
//
//            }else  if(preferences.getInteger(LAST_LOGIN_DAY)==365){
//                updateValue(preferences,calendarG);
//
//            }
//            else
//                preferences.putInteger(LAST_LOGIN_DAY,calendarG.get(Calendar.DAY_OF_YEAR));
//        }
//        else
//            preferences.putInteger(LAST_LOGIN_DAY,calendarG.get(Calendar.DAY_OF_YEAR));
//
//    }
//


}
