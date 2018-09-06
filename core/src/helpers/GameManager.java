package helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;

public class GameManager {
    private static final GameManager ourInstance = new GameManager();

    String[] birds = {"Blue", "Green", "Red"};
    int birdIndex = 0;
    public int score;
    private GameData gameData;
    private Json json = new Json();
    private FileHandle fileHandle = Gdx.files.local("bin/gameData.json");
    public Array<Sprite> collectables = new Array<Sprite>();
    private Boolean canRevive;

    private GameManager() {
    }

    public void initializeGameData() {
        if (!fileHandle.exists()) {
            gameData = new GameData();
            gameData.setHighScore(0);
            gameData.setCoins(0);
            gameData.setUserCurrentLevel(1);
            saveData();
        } else {
            loadData();
        }
    }

    private void loadData() {
        gameData = json.fromJson(GameData.class, Base64Coder.decodeString(fileHandle.readString()));
    }

    private void saveData() {
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


    public void incrementIndex() {
        birdIndex++;
        if (birdIndex == birds.length) {
            birdIndex = 0;
        }

    }

    public int getTotalCoins() {
        return gameData.getCoins();
    }


    public GameData getGameData() {
        return gameData;
    }

    public int getHighScore() {
        return gameData.getHighScore();
    }

    public String getBird() {
        return birds[birdIndex];
    }


    public Boolean getCanRevive() {
        return canRevive;
    }

    public void setCanRevive(Boolean canRevive) {
        this.canRevive = canRevive;
    }



    public static GameManager getInstance() {
        return ourInstance;
    }


}
