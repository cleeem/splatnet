package splatnet.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import splatnet.models.Storage;
import splatnet.s3s.UtilitaryS3S;
import splatnet.s3s.classes.Game;
import splatnet.s3s.classes.Team;
import javafx.scene.control.Label;

import java.io.File;
import java.io.IOException;

public class MatchController extends Controller {

    @FXML
    public HBox header;

    @FXML
    public VBox test;

    @FXML
    public void initialize() {
        System.out.println("MatchController initialized");
        Storage storage = Storage.getInstance();
        Game game = storage.getSelectedGame();
        displayGame(game);
    }

    private void displayGame(Game game) {
        displayHeaderInfos(game);
        displayMyTeam(game.getMyTeam());
        for (Team ennemy : game.getOtherTeam()) {
            displayEnnemyTeam(ennemy);
        }
    }

    private void displayHeaderInfos(Game game) {
        String stageUrl = game.getVsStage().getAsJsonObject("image").get("url").getAsString();
        String stageId = game.getVsStage().get("id").getAsString();
        File stageFile = new File("src/main/resources/splatnet/assets/maps/" + stageId + ".png");

        if (!stageFile.exists()) {
            try {
                UtilitaryS3S.downloadLargeImage(stageUrl, stageId, "maps");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        Image stageImage = new Image(stageFile.toURI().toString());

        ImageView stageImageView = new ImageView(stageImage);

        header.getChildren().add(stageImageView);

    }

    private void displayMyTeam(Team myTeam) {
    }

    private void displayEnnemyTeam(Team ennemy) {
    }

}
