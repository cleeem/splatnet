package splatnet.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import splatnet.models.schedule.Rotation;
import splatnet.models.schedule.Schedule;

import java.util.ArrayList;

public class ScheduleController {

    private static final int MAP_SIZE_WIDTH = 175;
    private static final int MAP_SIZE_HEIGHT = 100;

    private static final Font FONT = new Font("Splatoon2", 18);

    @FXML
    public VBox turfContainer;

    @FXML
    public VBox bankaraContainer;

    @FXML
    public VBox xmatchContainer;

    @FXML
    public void initialize() {
        System.out.println("ScheduleController initialized");
        Schedule schedule = new Schedule();

        displayTurfwars(schedule.getTurfWarRotations());
        displayBankaras(schedule.getBankaraRotations());
        displayXSchedules(schedule.getxRotations());
    }

    private void displayTurfwars(ArrayList<Rotation> turfWarRotations) {
        for (Rotation rotation : turfWarRotations) {
            turfContainer.getChildren().add(
                getRotationDisplay(rotation)
            );
        }
    }

    private void displayBankaras(ArrayList<Rotation> bankaraRotations) {
    }

    private void displayXSchedules(ArrayList<Rotation> rotations) {
    }

    private VBox getRotationDisplay(Rotation rotation) {
        VBox rotationDisplay = new VBox();
        rotationDisplay.setStyle("-fx-background-color: #6b6b6b");
        rotationDisplay.setSpacing(10);
        rotationDisplay.setAlignment(Pos.CENTER);

        ImageView map1 = rotation.getStages().get(0).getImage();
        ImageView map2 = rotation.getStages().get(1).getImage();

        map1.fitWidthProperty().setValue(rotationDisplay.getWidth());
        map1.fitHeightProperty().setValue(rotationDisplay.getHeight());
        map2.fitWidthProperty().setValue(rotationDisplay.getWidth());
        map2.fitHeightProperty().setValue(rotationDisplay.getHeight());

        VBox maps = new VBox();
        maps.setSpacing(10);
        maps.getChildren().add(map1);
        maps.getChildren().add(map2);

        Label modeLabel = new Label(rotation.getMode().get(0));
        modeLabel.setFont(FONT);
        modeLabel.setStyle("-fx-text-fill: #ffffff");


        // the format is yyyy-MM-ddTHH:mm:ss
        String startDay = rotation.getStartTime().substring(0, 10);
        String startTime = rotation.getStartTime().substring(11, 16);

        Label timeLabel = new Label(startDay + " " + startTime);
        timeLabel.setFont(FONT);
        timeLabel.setStyle("-fx-text-fill: #ffffff");

        String endDay = rotation.getEndTime().substring(0, 10);
        String endTime = rotation.getEndTime().substring(11, 16);

        Label endTimeLabel = new Label(endDay + " " + endTime);
        endTimeLabel.setFont(FONT);
        endTimeLabel.setStyle("-fx-text-fill: #ffffff");

        VBox textContainer = new VBox();
        textContainer.setSpacing(5);
        textContainer.setAlignment(Pos.CENTER_LEFT);
        textContainer.getChildren().add(modeLabel);
        textContainer.getChildren().add(timeLabel);
        textContainer.getChildren().add(endTimeLabel);

        rotationDisplay.getChildren().add(textContainer);
        rotationDisplay.getChildren().add(maps);

        return rotationDisplay;
    }

}
