package application;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class Audio {

    private MediaPlayer mainSong;
    private MediaPlayer introPlayer;

    public void playSound(String fileName){
        URL resource = getClass().getResource("/audio/" + fileName);
        Media m = new Media(resource.toString());
        MediaPlayer player = new MediaPlayer(m);
        player.play();
    }

    public void quietSong() {
        mainSong.setVolume(0.1);
    }

    public void changeSong(String fileName) {
        stopSong();
        URL resource1 = getClass().getResource("/audio/" + fileName);
        Media loop = new Media(resource1.toString());
        mainSong = new MediaPlayer(loop);
        mainSong.setCycleCount(MediaPlayer.INDEFINITE);
        mainSong.play();
    }

    public void startGame() {
        URL resource1 = getClass().getResource("/audio/intro.mp3");
        URL resource2 = getClass().getResource("/audio/loop.mp3");
        Media intro = new Media(resource1.toString());
        Media loop = new Media(resource2.toString());
        introPlayer = new MediaPlayer(intro);
        introPlayer.play();
        introPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mainSong = new MediaPlayer(loop);
                if (introPlayer.isMute()) {
                    mainSong.setMute(true);
                }
                mainSong.setCycleCount(MediaPlayer.INDEFINITE);
                mainSong.play();
            }
        });
    }

    public void muteSong() {
        if (!introPlayer.isMute()) {
            if (mainSong != null) {
                mainSong.setMute(true);
            }
            introPlayer.setMute(true);
        } else {
            if (mainSong != null) {
                mainSong.setMute(false);
            }
            introPlayer.setMute(false);
        }
    }

    public void stopSong() {
        if (mainSong!= null) {
            mainSong.stop();
        }
        introPlayer.stop();
    }

    void playMain() {
        mainSong.play();
    }
}
