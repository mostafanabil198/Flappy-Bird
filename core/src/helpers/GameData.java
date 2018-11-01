package helpers;

import com.badlogic.gdx.utils.Array;

public class GameData {

    private int highScore;
    private int coins;
    private final int levels = 16;
    private int userCurrentLevel;
    private final int[] levelsScores = {5, 10, 15, 20, 25, 35, 50, 60, 70, 85, 100, 110, 125, 150, 175, 200};
    private final int[] levelsCoins = {1, 2, 3, 4, 5, 8, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
    private final int[] birdsPrices = {0, 75, 100, 120, 150, 200, 250, 300};
    private final int totalShownLevels = 16;
    private Array<Integer> myBirds;
    private boolean[] AllBirds;
    private int currentBird;
    private int numOfTotalGames;
    private boolean hasHideOption;
    private int numOfHideOption;
    private boolean hasX2Coins;
    private boolean hasFireOption;
    private int numOfFireOption;
    private int lastDayIn;
    private boolean soundOn;


    public int getNumOfTotalGames() {
        return numOfTotalGames;
    }

    public void setNumOfTotalGames(int numOfTotalGames) {
        this.numOfTotalGames = numOfTotalGames;
    }

    public int[] getLevelsScores() {
        return levelsScores;
    }

    public int[] getLevelsCoins() {
        return levelsCoins;
    }


    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getLevels() {
        return levels;
    }

    public int getUserCurrentLevel() {
        return userCurrentLevel;
    }

    public void setUserCurrentLevel(int userCurrentLevel) {
        this.userCurrentLevel = userCurrentLevel;
    }

    public Array<Integer> getMyBirds() {
        return myBirds;
    }

    public void setMyBirds(Array<Integer> myBirds) {
        this.myBirds = myBirds;
    }

    public boolean[] getAllBirds() {
        return AllBirds;
    }

    public void setAllBirds(boolean[] allBirds) {
        AllBirds = allBirds;
    }

    public int[] getBirdsPrices() {
        return birdsPrices;
    }

    public int getTotalShownLevels() {
        return totalShownLevels;
    }


    public int getCurrentBird() {
        return currentBird;
    }

    public void setCurrentBird(int currentBird) {
        this.currentBird = currentBird;
    }


    public boolean isHasHideOption() {
        return hasHideOption;
    }

    public void setHasHideOption(boolean hasHideOption) {
        this.hasHideOption = hasHideOption;
    }

    public int getNumOfHideOption() {
        return numOfHideOption;
    }

    public void setNumOfHideOption(int numOfHideOption) {
        this.numOfHideOption = numOfHideOption;
    }

    public boolean isHasX2Coins() {
        return hasX2Coins;
    }

    public void setHasX2Coins(boolean hasX2Coins) {
        this.hasX2Coins = hasX2Coins;
    }

    public boolean isHasFireOption() {
        return hasFireOption;
    }

    public void setHasFireOption(boolean hasFireOption) {
        this.hasFireOption = hasFireOption;
    }

    public int getNumOfFireOption() {
        return numOfFireOption;
    }

    public void setNumOfFireOption(int numOfFireOption) {
        this.numOfFireOption = numOfFireOption;
    }


    public boolean isSoundOn() {
        return soundOn;
    }

    public void setSoundOn(boolean soundOn) {
        this.soundOn = soundOn;
    }


    public int getLastDayIn() {
        return lastDayIn;
    }

    public void setLastDayIn(int lastDayIn) {
        this.lastDayIn = lastDayIn;
    }

}
