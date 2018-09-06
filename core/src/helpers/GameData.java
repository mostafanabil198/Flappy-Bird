package helpers;

public class GameData {

    private int highScore;
    private int coins;
    private final int levels = 16;
    private int userCurrentLevel;
    private final int[] levelsScores = {5, 10, 15, 20, 25, 35, 50, 60, 70, 85, 100, 110, 125, 150, 175, 200};
    private final int[] levelsCoins = {1, 2, 3, 4, 5, 8, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
    private final int totalShownLevels = 16;

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

    public int getTotalShownLevels() {
        return totalShownLevels;
    }

}
