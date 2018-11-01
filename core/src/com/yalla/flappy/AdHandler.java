package com.yalla.flappy;

public interface AdHandler {

    public void loadVideo();

    // 1 for coins , 2 for revive
    public void openVideo(int forWhat);

    public void toast(String s);
}
