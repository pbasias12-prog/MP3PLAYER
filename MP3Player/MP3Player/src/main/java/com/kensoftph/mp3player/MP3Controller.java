package com.kensoftph.mp3player;

import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


import java.io.File;
import java.net.URL;
import java.util.*;


public class MP3Controller implements Initializable {


    @FXML
    private ImageView image1;

    @FXML
    private ImageView image2;

    @FXML
    private Button nextButton;

    @FXML
    private AnchorPane pane;

    @FXML
    private Button playButton;

    @FXML
    private Button previousButton;

    @FXML
    private Label songLabel;

    @FXML
    private ProgressBar songProgressbar;

    @FXML
    private ComboBox<String> speedBox;

    @FXML
    private Slider volumeSlider;

    private Media media;

    private MediaPlayer mediaPlayer;


    private File directory;
    private File[] files;

    private ArrayList<File> songs;

    private int songNumber;
    private int[] speed = {25, 50, 75, 100, 125, 150, 175, 200};

    private Timer timer;
    private TimerTask Task;
    private boolean isPlayed = false;
    private boolean running;


    @FXML
    void changeSpeed(ActionEvent event) {

        if(speedBox.getValue() == null){
            mediaPlayer.setRate(1);
        }else {
            mediaPlayer.setRate(Integer.parseInt(speedBox.getValue().substring(0, speedBox.getValue().length() - 1)) * 0.01);
        }
    }

    @FXML
    void nextMedia(ActionEvent event) {

        if(songNumber < songs.size()-1){

            songNumber++;
            mediaPlayer.pause();
            if(running) {
                cancelTimer();
            }


            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            playButton.setText("| |");
            setPlayerSettings();


            mediaPlayer.play();

        }else {
            songNumber = 0;
            mediaPlayer.pause();

            if(running) {
                cancelTimer();
            }

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            playButton.setText("| |");

            setPlayerSettings();

            mediaPlayer.play();


        }
    }



    @FXML
    void playMedia(ActionEvent event) {

        beginTimer();

        if(!isPlayed) {
            playButton.setText("| |");
            changeSpeed(null);
            mediaPlayer.setVolume(volumeSlider.getValue()* 0.01);
            beginTimer();
            mediaPlayer.play();
            isPlayed = true;
        } else {
            playButton.setText("|>");
            changeSpeed(null);
            mediaPlayer.setVolume(volumeSlider.getValue()* 0.01);
            beginTimer();
            mediaPlayer.pause();
            isPlayed = false;


        }
    }

    @FXML
    void previousMedia(ActionEvent event) {

        if(songNumber > 0){

            songNumber--;
            mediaPlayer.pause();

            if(running) {
                cancelTimer();
            }

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            playButton.setText("| |");
            setPlayerSettings();
            mediaPlayer.play();

        }else {
            songNumber = songs.size()-1;
            mediaPlayer.pause();
            if(running) {
                cancelTimer();
            }

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            playButton.setText("| |");
            setPlayerSettings();
            mediaPlayer.play();


        }
    }




    public void beginTimer() {

    timer = new Timer();
    Task = new TimerTask() {

        public void run() {
            running = true;
            double current = mediaPlayer.getCurrentTime().toSeconds();
            double end = media.getDuration().toSeconds();
            System.out.println(current/end);
            songProgressbar.setProgress(current/end);

            if (current/end == 1) {
                cancelTimer();
            }
        }
    };
    timer.scheduleAtFixedRate(Task, 0, 1000);
}

public void cancelTimer() {
           running = false;
           timer.cancel();
        }

    private void setPlayerSettings() {

        songLabel.setText(songs.get(songNumber).getName());

        changeSpeed(null);

        mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);

        mediaPlayer.setOnEndOfMedia(() -> {
            nextMedia(null);
        });
    }






    @Override
    public void initialize(URL location, ResourceBundle resources) {

        songs = new ArrayList<File>();
        directory = new File("music");

        files = directory.listFiles();

        if (files != null) {

            for (File file : files) {
                songs.add(file);
            }
        }

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
             setPlayerSettings();



      for(int i = 0; i < speed.length; i++){
          speedBox.getItems().add(Integer.toString(speed[i])+"%");
      }
        speedBox.setOnAction(this::changeSpeed);
        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                mediaPlayer.setVolume(volumeSlider.getValue()* 0.01);
            }
        });



}
}
