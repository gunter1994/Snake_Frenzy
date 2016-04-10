package application;

/**
 * Created by gunter on 4/10/16.
 */
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
}
