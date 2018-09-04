package helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
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

    public Boolean getCanRevive() {
        return canRevive;
    }

    public void setCanRevive(Boolean canRevive) {
        this.canRevive = canRevive;
    }

    private Boolean canRevive;

    private GameManager() {
    }

    public void initializeGameData() {
        if (!fileHandle.exists()) {
            gameData = new GameData();
            gameData.setHighScore(0);
            gameData.setPoints(0);
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

    public void checkForNewHighScore() {
        int oldHighScore = gameData.getHighScore();

        if (oldHighScore < score) {
            gameData.setHighScore(score);
            saveData();
        }

    }

    public int getHighScore() {
        return gameData.getHighScore();
    }

    public String getBird() {
        return birds[birdIndex];
    }

    public void incrementIndex() {
        birdIndex++;
        if (birdIndex == birds.length) {
            birdIndex = 0;
        }

    }

    public static GameManager getInstance() {
        return ourInstance;
    }
}
