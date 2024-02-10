package splatnet.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import splatnet.models.Storage;
import splatnet.s3s.UtilitaryS3S;
import splatnet.s3s.classes.game.Game;
import splatnet.s3s.classes.game.Player;
import splatnet.s3s.classes.game.Team;
import javafx.scene.control.Label;

import java.io.File;
import java.io.IOException;

public class MatchController extends Controller {

    private static final int LARGE_IMAGE_SIZE = 100;

    private static final int MEDIUM_IMAGE_SIZE = 75;

    private static final int SMALL_IMAGE_SIZE = 50;

    private static final int PRIMARY_SUB_IMAGE_SIZE = 35;
    private static final int SUB_IMAGE_SIZE = 20;

    @FXML
    public VBox modeContainer;

    @FXML
    public VBox stageContainer;

    @FXML
    public VBox infosContainer;

    @FXML
    public VBox myTeamBox;

    @FXML
    public VBox ennemyTeamBox;

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

        // affichage du mode
        File modeFile = new File("src/main/resources/splatnet/assets/modes/" + "S3_icon_" + game.getVsRule().replace(" ", "_") + ".png");
        ImageView modeImageView = new ImageView(new Image(modeFile.toURI().toString()));
        modeImageView.setFitHeight(LARGE_IMAGE_SIZE);
        modeImageView.setFitWidth(LARGE_IMAGE_SIZE);

        Label modeLabel = new Label(game.getVsRule());
        modeLabel.setFont(new Font("Splatoon2", 20));
        modeLabel.setStyle("-fx-text-fill: white;");

        modeContainer.getChildren().add(modeImageView);
        modeContainer.getChildren().add(modeLabel);

        // affichage de la map
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
        ImageView stageImageView = new ImageView(new Image(stageFile.toURI().toString()));
        stageImageView.setFitWidth(650);
        stageImageView.setFitHeight(100);
        stageContainer.getChildren().add(stageImageView);

        // time is in yyyy-MM-dd'T'HH:mm:ss.SSS'Z' format
        // so we get date and time separately
        String date = game.getPlayedTime().split("T")[0];
        String time = game.getPlayedTime().split("T")[1].split("\\.")[0];

        Label dateLabel = new Label(date + " " + time);
        dateLabel.setFont(new Font("Splatoon2", 20));
        dateLabel.setStyle("-fx-text-fill: white;");

        stageContainer.getChildren().add(dateLabel);

        // affichage des infos

        String myTeamScore;
        String ennemyTeamScore;

        if (!game.isKO() && game.getOtherTeam().size() == 2) {
            // c'est un match tricolor
            if (game.getOtherTeam().get(0).getPlayers().size() == 4
                || game.getOtherTeam().get(1).getPlayers().size() == 4) {
                // ma team en attaquant
                int myTeam = Integer.parseInt(game.getMyTeam().getScore());
                int otherTeam = 0;
                if (game.getOtherTeam().get(0).getPlayers().size() == 4)  {
                    otherTeam = Integer.parseInt(game.getOtherTeam().get(1).getScore());
                } else {
                    otherTeam = Integer.parseInt(game.getOtherTeam().get(0).getScore());
                }

                myTeamScore = String.valueOf(myTeam + otherTeam);
                ennemyTeamScore = game.getOtherTeam().get(0).getScore();

            } else {
                // ma team en défense
                myTeamScore = game.getMyTeam().getScore();
                int score1 = Integer.parseInt(game.getOtherTeam().get(0).getScore());
                int score2 = Integer.parseInt(game.getOtherTeam().get(1).getScore());
                ennemyTeamScore = String.valueOf(score1 + score2);
            }
        } else {
            if (game.isKO()) {
                myTeamScore = game.getMyTeam().getScore().equals("100") ? "KO" : "0";
                ennemyTeamScore = game.getOtherTeam().get(0).getScore().equals("100") ? "KO" : "0";
            } else {
                myTeamScore = game.getMyTeam().getScore();
                ennemyTeamScore = game.getOtherTeam().get(0).getScore();
            }
        }
        Label scoresLabel;

        if (game.getStatus().equals("DRAW")) {
            scoresLabel = new Label("DRAW");
        } else {
            scoresLabel = new Label(myTeamScore + " - " + ennemyTeamScore);
        }
        scoresLabel.setFont(new Font("Splatoon2", 20));
        scoresLabel.setStyle("-fx-text-fill: white;");
        infosContainer.getChildren().add(scoresLabel);

    }

    private void displayMyTeam(Team myTeam) {
        for (Player player : myTeam.getPlayers()) {
            myTeamBox.getChildren().add(displayPlayer(player));
        }
    }

    private void displayEnnemyTeam(Team ennemy) {
    }

    private VBox displayPlayer(Player player) {

        VBox playerBox = new VBox();
        playerBox.setAlignment(Pos.CENTER);

        Label playerName = new Label(player.getName());
        playerName.setFont(new Font("Splatoon2", 20));
        playerName.setStyle("-fx-text-fill: white;");

        Label paintLabel = new Label(player.getPaint() + "p");
        paintLabel.setFont(new Font("Splatoon2", 20));
        paintLabel.setStyle("-fx-text-fill: white;");

        playerBox.getChildren().add(playerName);
        playerBox.getChildren().add(paintLabel);
        playerBox.getChildren().add(displayStuffs(player));
        playerBox.getChildren().add(displayWeapon(player));

        return playerBox;

    }

    private HBox displayStuffs(Player player) {

        HBox gearHolder = new HBox();

        return gearHolder;
    }

    private VBox displayWeapon(Player player) {

        VBox weaponHolder = new VBox();


        return weaponHolder;
    }


}
