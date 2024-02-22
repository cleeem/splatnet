package splatnet.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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

    private static final int MODE_ICON_SIZE = 50;


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
        for (Rotation rotation : bankaraRotations) {
            bankaraContainer.getChildren().add(
                getBankaraRotationDisplay(rotation)
            );
        }
    }

    private Node getBankaraRotationDisplay(Rotation rotation) {
        VBox rotationDisplay = new VBox();
        rotationDisplay.setStyle("-fx-background-color: #5a5a5a");
        rotationDisplay.setPadding(
                new Insets(10, 10, 10, 10)
        );
        rotationDisplay.setSpacing(10);

        ImageView map1 = rotation.getStages().get(0).getSmallImage();
        ImageView map2 = rotation.getStages().get(1).getSmallImage();
        ImageView map3 = rotation.getStages().get(2).getSmallImage();
        ImageView map4 = rotation.getStages().get(3).getSmallImage();

        HBox mapsSeries = new HBox();
        mapsSeries.setSpacing(10);
        mapsSeries.setAlignment(Pos.CENTER);

        map1.setPreserveRatio(true);
        map1.fitWidthProperty().bind(mapsSeries.widthProperty().divide(2.75));

        map2.setPreserveRatio(true);
        map2.fitWidthProperty().bind(mapsSeries.widthProperty().divide(2.75));

        HBox mapsOpen = new HBox();
        mapsOpen.setSpacing(10);
        mapsOpen.setAlignment(Pos.CENTER);

        map3.setPreserveRatio(true);
        map3.fitWidthProperty().bind(mapsOpen.widthProperty().divide(2.75));

        map4.setPreserveRatio(true);
        map4.fitWidthProperty().bind(mapsOpen.widthProperty().divide(2.75));

        HBox labelSeries = new HBox();
        labelSeries.setAlignment(Pos.CENTER);
        Label modeLabel = new Label("Series");
        modeLabel.setFont(FONT);
        modeLabel.setStyle("-fx-text-fill: #ffffff");
        labelSeries.getChildren().add(
                modeLabel
        );
        mapsSeries.getChildren().add(
                labelSeries
        );
        mapsSeries.getChildren().add(map1);
        mapsSeries.getChildren().add(map2);

        HBox labelOpen = new HBox();
        labelOpen.setAlignment(Pos.CENTER);
        Label modeLabelOpen = new Label("Open  ");
        modeLabelOpen.setFont(FONT);
        modeLabelOpen.setStyle("-fx-text-fill: #ffffff");
        labelOpen.getChildren().add(
                modeLabelOpen
        );
        mapsOpen.getChildren().add(
                labelOpen
        );
        mapsOpen.getChildren().add(map3);
        mapsOpen.getChildren().add(map4);

        VBox textContainer = new VBox();
        textContainer.setSpacing(5);
        textContainer.setAlignment(Pos.CENTER);

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

        textContainer.getChildren().add(timeLabel);
        textContainer.getChildren().add(endTimeLabel);

        String fullModeNameSerie = "S3_icon_" + rotation.getMode().get(0).replace(" ", "_") + ".png";
        String fullModeNameOpen = "S3_icon_" + rotation.getMode().get(1).replace(" ", "_") + ".png";

        ImageView modeImageSerie = new ImageView(
                String.valueOf(Main.class.getResource("assets/modes/" + fullModeNameSerie))
        );

        ImageView modeImageOpen = new ImageView(
                String.valueOf(Main.class.getResource("assets/modes/" + fullModeNameOpen))
        );

        modeImageSerie.setFitWidth(MODE_ICON_SIZE);
        modeImageSerie.setFitHeight(MODE_ICON_SIZE);

        modeImageOpen.setFitWidth(MODE_ICON_SIZE);
        modeImageOpen.setFitHeight(MODE_ICON_SIZE);

        VBox series = new VBox();
        series.setAlignment(Pos.CENTER);
        Label seriesLabel = new Label("Series");
        seriesLabel.setFont(FONT);
        seriesLabel.setStyle("-fx-text-fill: #ffffff");
        series.setSpacing(10);
        series.getChildren().add(
                seriesLabel
        );
        series.getChildren().add(modeImageSerie);

        VBox open = new VBox();
        open.setAlignment(Pos.CENTER);
        Label openLabel = new Label("Open");
        openLabel.setFont(FONT);
        openLabel.setStyle("-fx-text-fill: #ffffff");
        open.setSpacing(10);
        open.getChildren().add(
                openLabel
        );
        open.getChildren().add(modeImageOpen);

        HBox bankara = new HBox();
        bankara.setAlignment(Pos.CENTER);
        bankara.setSpacing(10);
        bankara.getChildren().add(series);
        bankara.getChildren().add(open);

        HBox infoContainer = new HBox();
        infoContainer.setAlignment(Pos.CENTER);
        infoContainer.setSpacing(10);
        infoContainer.getChildren().add(textContainer);
        infoContainer.getChildren().add(bankara);

        rotationDisplay.getChildren().add(infoContainer);
        rotationDisplay.getChildren().add(mapsSeries);
        rotationDisplay.getChildren().add(mapsOpen);

        return rotationDisplay;
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
        infoContainer.setAlignment(Pos.CENTER);
        infoContainer.setSpacing(10);

        VBox textContainer = new VBox();
        textContainer.setSpacing(5);
        textContainer.setAlignment(Pos.CENTER);
        textContainer.getChildren().add(modeLabel);
        textContainer.getChildren().add(timeLabel);
        textContainer.getChildren().add(endTimeLabel);

        String fullModeName = "S3_icon_" + rotation.getMode().get(0).replace(" ", "_") + ".png";

        ImageView modeImage = new ImageView(
                String.valueOf(Main.class.getResource("assets/modes/" + fullModeName))
        );

        modeImage.setFitWidth(MODE_ICON_SIZE);
        modeImage.setFitHeight(MODE_ICON_SIZE);

        infoContainer.getChildren().add(textContainer);
        infoContainer.getChildren().add(modeImage);

        rotationDisplay.getChildren().add(infoContainer);
        rotationDisplay.getChildren().add(maps);

        return rotationDisplay;
    }

}
