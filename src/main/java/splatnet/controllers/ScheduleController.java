package splatnet.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import splatnet.Main;
import splatnet.models.schedule.Rotation;
import splatnet.models.schedule.Schedule;

import java.util.ArrayList;

public class ScheduleController {

    private static final int MAP_SIZE_WIDTH = 177;
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
        for (Rotation rotation : rotations) {
            xmatchContainer.getChildren().add(
                getRotationDisplay(rotation)
            );
        }
    }

    private VBox getRotationDisplay(Rotation rotation) {
        VBox rotationDisplay = new VBox();
        rotationDisplay.setStyle("-fx-background-color: #5a5a5a");
        rotationDisplay.setPadding(
                new Insets(10, 10, 10, 10)
        );
        rotationDisplay.setSpacing(10);
        rotationDisplay.setAlignment(Pos.CENTER);

        ImageView map1 = rotation.getStages().get(0).getSmallImage();
        ImageView map2 = rotation.getStages().get(1).getSmallImage();

        HBox maps = new HBox();
        maps.setSpacing(10);
        maps.setAlignment(Pos.CENTER);
        // we want to make the images scale to the size of the HBox

        map1.setPreserveRatio(true);
        map1.fitWidthProperty().bind(maps.widthProperty().divide(2.5));

        map2.setPreserveRatio(true);
        map2.fitWidthProperty().bind(maps.widthProperty().divide(2.5));

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

        HBox infoContainer = new HBox();
        infoContainer.setSpacing(10);

        VBox textContainer = new VBox();
        textContainer.setSpacing(5);
        textContainer.setAlignment(Pos.CENTER_LEFT);
        textContainer.getChildren().add(modeLabel);
        textContainer.getChildren().add(timeLabel);
        textContainer.getChildren().add(endTimeLabel);

        String fullModeName = "S3_icon_" + rotation.getMode().get(0).replace(" ", "_") + ".png";

        ImageView modeImage = new ImageView(
                String.valueOf(Main.class.getResource("assets/modes/" + fullModeName))
        );

        modeImage.setFitWidth(MAP_SIZE_HEIGHT);
        modeImage.setFitHeight(MAP_SIZE_HEIGHT);

        infoContainer.getChildren().add(textContainer);
        infoContainer.getChildren().add(modeImage);

        rotationDisplay.getChildren().add(infoContainer);
        rotationDisplay.getChildren().add(maps);

        return rotationDisplay;
    }

}
