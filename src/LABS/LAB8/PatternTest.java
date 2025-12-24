package LABS.LAB8;

import java.util.ArrayList;
import java.util.List;

class Song {
    String title;
    String artist;

    public Song(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    @Override
    public String toString() {
        return "Song{" +
                "title=" + title +
                ", artist=" + artist +
                '}';
    }
}

interface IMP3State {
    void Play(int currentSong);

    void Stop(int stoppingSong);

    void FWD();

    void REW();
}

abstract class MP3State implements IMP3State {
    MP3Player mp3Player;

    public MP3State(MP3Player mp3Player) {
        this.mp3Player = mp3Player;
    }
}

class PlayingState extends MP3State {
    PlayingState(MP3Player mp3Player) {
        super(mp3Player);
    }

    @Override
    public void Play(int currentSong) {
        System.out.println("Song is already playing");
    }

    @Override
    public void Stop(int stoppingSong) {
        System.out.println(String.format("Song %d is paused", stoppingSong));
        this.mp3Player.state = new StoppedState(this.mp3Player);
    }

    @Override
    public void FWD() {
        System.out.println("Forward...");
        this.mp3Player.currentSong = (this.mp3Player.currentSong + 1) % this.mp3Player.songs.size();
        this.mp3Player.state = new StoppedState(this.mp3Player);
    }

    @Override
    public void REW() {
        System.out.println("Reward...");
        if (mp3Player.currentSong == 0) {
            mp3Player.currentSong = mp3Player.songs.size() - 1;
        } else {
            mp3Player.currentSong--;
        }
        this.mp3Player.state = new StoppedState(this.mp3Player);
    }
}

class StoppedState extends MP3State {

    StoppedState(MP3Player mp3Player) {
        super(mp3Player);
    }

    @Override
    public void Play(int currentSong) {
        System.out.println(String.format("Song %d is playing", currentSong));
        this.mp3Player.state = new PlayingState(this.mp3Player);
    }

    @Override
    public void Stop(int stoppingSong) {
        System.out.println("Songs are stopped");
        this.mp3Player.currentSong = 0;
        this.mp3Player.state = new StoppedAlreadyState(this.mp3Player);
    }

    @Override
    public void FWD() {
        System.out.println("Forward...");
        this.mp3Player.currentSong = (this.mp3Player.currentSong + 1) % this.mp3Player.songs.size();
    }

    @Override
    public void REW() {
        System.out.println("Reward...");
        if (mp3Player.currentSong == 0) {
            mp3Player.currentSong = mp3Player.songs.size() - 1;
        } else {
            mp3Player.currentSong--;
        }
    }
}

class StoppedAlreadyState extends StoppedState {
    StoppedAlreadyState(MP3Player mp3Player) {
        super(mp3Player);
    }

    @Override
    public void Stop(int stoppingSong) {
        System.out.println("Songs are already stopped");
    }
}

class MP3Player {
    List<Song> songs;
    int currentSong;
    MP3State state;

    public MP3Player(List<Song> songs) {
        this.songs = songs;
        currentSong = 0;
        this.state = new StoppedState(this);
    }

    void pressPlay() {
        this.state.Play(currentSong);
    }

    void pressStop() {
        this.state.Stop(currentSong);
    }

    void pressFWD() {
        this.state.FWD();
    }

    void pressREW() {
        this.state.REW();
    }

    void printCurrentSong() {
        System.out.println(songs.get(currentSong).toString());
    }

    @Override
    public String toString() {
        return "MP3Player{" +
                "currentSong = " + currentSong +
                ", songList = " + songs +
                '}';
    }
}


public class PatternTest {
    public static void main(String args[]) {
        List<Song> listSongs = new ArrayList<Song>();
        listSongs.add(new Song("first-title", "first-artist"));
        listSongs.add(new Song("second-title", "second-artist"));
        listSongs.add(new Song("third-title", "third-artist"));
        listSongs.add(new Song("fourth-title", "fourth-artist"));
        listSongs.add(new Song("fifth-title", "fifth-artist"));
        MP3Player player = new MP3Player(listSongs);


        System.out.println(player.toString());
        System.out.println("First test");


        player.pressPlay();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Second test");


        player.pressStop();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Third test");


        player.pressFWD();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
    }
}

//Vasiot kod ovde