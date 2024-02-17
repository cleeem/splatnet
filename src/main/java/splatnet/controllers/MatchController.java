package splatnet.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import splatnet.Main;
import splatnet.models.Storage;
import splatnet.s3s.UtilitaryS3S;
import splatnet.s3s.classes.game.Game;
import splatnet.s3s.classes.game.Player;
import splatnet.s3s.classes.game.Team;
import javafx.scene.control.Label;
import splatnet.s3s.classes.misc.Ability;
import splatnet.s3s.classes.misc.Stuff;
import splatnet.s3s.classes.weapons.MainWeapon;
import splatnet.s3s.classes.weapons.Weapon;

import java.io.File;
import java.io.IOException;

public class MatchController extends Controller {

    private static final Font SPLATOON2_FONT = new Font("Splatoon2", 18);

    private static final int LARGE_IMAGE_SIZE = 100;
    private static final int GEAR_IMAGE_SIZE = 40;
    private static final int PRIMARY_ABILITY_IMAGE_SIZE = 35;
    private static final int SUB_IMAGE_SIZE = 25;
    private static final int MAIN_WEAPON_IMAGE_SIZE = 80;
    private static final int SUB_WEAPON_IMAGE_SIZE = 40;


    @FXML
    public VBox modeContainer;

    @FXML
    public VBox stageContainer;

    @FXML
    public VBox infosContainer;

    @FXML
    public GridPane myTeamBox;

    @FXML
    public GridPane ennemyTeamBox;

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
        System.out.println("Mode: " + game.getVsRule());
        ImageView modeImageView = new ImageView(String.valueOf(Main.class.getResource("assets/modes/" + "S3_icon_" + game.getVsRule().replace(" ", "_") + ".png")));
        modeImageView.setFitHeight(LARGE_IMAGE_SIZE);
        modeImageView.setFitWidth(LARGE_IMAGE_SIZE);

        Label modeLabel = new Label(game.getVsRule());
        modeLabel.setFont(SPLATOON2_FONT);
        modeLabel.setStyle("-fx-text-fill: white;");

        modeContainer.getChildren().add(modeImageView);
        modeContainer.getChildren().add(modeLabel);

        // affichage de la map


        ImageView stageImageView = game.getVsStage().getImage();
        stageImageView.setFitWidth(650);
        stageImageView.setFitHeight(100);
        stageContainer.getChildren().add(stageImageView);

        // time is in yyyy-MM-dd'T'HH:mm:ss.SSS'Z' format
        // so we get date and time separately
        String date = game.getPlayedTime().split("T")[0];
        String time = game.getPlayedTime().split("T")[1].split("\\.")[0];
        time = time.substring(0, time.length() - 1);

        Label dateLabel = new Label(date + " " + time);
        dateLabel.setFont(SPLATOON2_FONT);
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
        scoresLabel.setFont(SPLATOON2_FONT);
        scoresLabel.setStyle("-fx-text-fill: white;");
        infosContainer.getChildren().add(scoresLabel);

    }

    private void displayMyTeam(Team myTeam) {
        int row = 0;
        int column = 0;
        for (Player player : myTeam.getPlayers()) {
            myTeamBox.add(displayPlayer(player), column, row);
            column++;
            if (column == 2) {
                column = 0;
                row++;
            }
        }
    }

    private void displayEnnemyTeam(Team ennemy) {
        int row = 0;
        int column = 0;
        for (Player player : ennemy.getPlayers()) {
            ennemyTeamBox.add(displayPlayer(player), column, row);
            column++;
            if (column == 2) {
                column = 0;
                row++;
            }
        }
    }

    private VBox displayPlayer(Player player) {

        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(10, 10, 10, 10));

        GridPane playerBox = new GridPane();
        playerBox.setAlignment(Pos.CENTER);
        playerBox.setHgap(10);
        playerBox.setVgap(10);
        playerBox.setStyle("-fx-background-color: #2c2b2b;");

        Label playerName = new Label(player.getName());
        playerName.setFont(SPLATOON2_FONT);
        playerName.setStyle("-fx-text-fill: white;");

        Label paintLabel = new Label(player.getPaint() + "p");
        paintLabel.setFont(SPLATOON2_FONT);
        paintLabel.setStyle("-fx-text-fill: white;");

        Label kdaLabel = new Label(
                player.getKills() + "(" + player.getAssists() + ")/"
                 + player.getDeath() + "/"
                 + player.getSpecial()
        );
        kdaLabel.setFont(SPLATOON2_FONT);
        kdaLabel.setStyle("-fx-text-fill: white;");

        VBox statsBox = new VBox();
        statsBox.getChildren().add(playerName);
        statsBox.getChildren().add(paintLabel);
        statsBox.getChildren().add(kdaLabel);

        VBox weaponHolder = displayWeapon(player);
        VBox gearHolder = displayStuffs(player);

        // the display should be stats and weapon on the first row
        // and gear on the second row
        playerBox.add(statsBox, 0, 0);
        playerBox.add(weaponHolder, 1, 0);
        playerBox.add(gearHolder, 0, 1, 2, 1);

        container.getChildren().add(playerBox);

        return container;

    }

    private VBox displayStuffs(Player player) {

        VBox gearHolder = new VBox();
        gearHolder.setAlignment(Pos.CENTER);

        HBox headGearHolder = new HBox();
        headGearHolder.setAlignment(Pos.BOTTOM_CENTER);
        ImageView gearImageView = player.getHeadGear().getImage();
        gearImageView.setFitHeight(GEAR_IMAGE_SIZE);
        gearImageView.setFitWidth(GEAR_IMAGE_SIZE);
        headGearHolder.getChildren().add(gearImageView);
        ImageView primaryAbilityImageView = player.getHeadGear().getMainAbility().getImage();
        primaryAbilityImageView.setFitHeight(PRIMARY_ABILITY_IMAGE_SIZE);
        primaryAbilityImageView.setFitWidth(PRIMARY_ABILITY_IMAGE_SIZE);
        headGearHolder.getChildren().add(primaryAbilityImageView);

        for (Ability ability : player.getHeadGear().getSubAbilities()) {
            ImageView subAbilityImageView = ability.getImage();
            subAbilityImageView.setFitHeight(SUB_IMAGE_SIZE);
            subAbilityImageView.setFitWidth(SUB_IMAGE_SIZE);
            headGearHolder.getChildren().add(subAbilityImageView);
        }

        HBox clothesHolder = new HBox();
        clothesHolder.setAlignment(Pos.BOTTOM_CENTER);
        gearImageView = player.getClothingGear().getImage();
        gearImageView.setFitHeight(GEAR_IMAGE_SIZE);
        gearImageView.setFitWidth(GEAR_IMAGE_SIZE);
        clothesHolder.getChildren().add(gearImageView);
        primaryAbilityImageView = player.getClothingGear().getMainAbility().getImage();
        primaryAbilityImageView.setFitHeight(PRIMARY_ABILITY_IMAGE_SIZE);
        primaryAbilityImageView.setFitWidth(PRIMARY_ABILITY_IMAGE_SIZE);
        clothesHolder.getChildren().add(primaryAbilityImageView);

        for (Ability ability : player.getClothingGear().getSubAbilities()) {
            ImageView subAbilityImageView = ability.getImage();
            subAbilityImageView.setFitHeight(SUB_IMAGE_SIZE);
            subAbilityImageView.setFitWidth(SUB_IMAGE_SIZE);
            clothesHolder.getChildren().add(subAbilityImageView);
        }

        HBox shoesHolder = new HBox();
        shoesHolder.setAlignment(Pos.BOTTOM_CENTER);
        gearImageView = player.getShoesGear().getImage();
        gearImageView.setFitHeight(GEAR_IMAGE_SIZE);
        gearImageView.setFitWidth(GEAR_IMAGE_SIZE);
        shoesHolder.getChildren().add(gearImageView);
        primaryAbilityImageView = player.getShoesGear().getMainAbility().getImage();
        primaryAbilityImageView.setFitHeight(PRIMARY_ABILITY_IMAGE_SIZE);
        primaryAbilityImageView.setFitWidth(PRIMARY_ABILITY_IMAGE_SIZE);
        shoesHolder.getChildren().add(primaryAbilityImageView);

        for (Ability ability : player.getShoesGear().getSubAbilities()) {
            ImageView subAbilityImageView = ability.getImage();
            subAbilityImageView.setFitHeight(SUB_IMAGE_SIZE);
            subAbilityImageView.setFitWidth(SUB_IMAGE_SIZE);
            shoesHolder.getChildren().add(subAbilityImageView);
        }

        gearHolder.getChildren().add(headGearHolder);
        gearHolder.getChildren().add(clothesHolder);
        gearHolder.getChildren().add(shoesHolder);

        return gearHolder;
    }

    private VBox displayWeapon(Player player) {

        VBox weaponHolder = new VBox();

        MainWeapon mainWeapon = player.getWeapon();

        ImageView mainWeaponImageView = mainWeapon.getImage();
        mainWeaponImageView.setFitHeight(MAIN_WEAPON_IMAGE_SIZE);
        mainWeaponImageView.setFitWidth(MAIN_WEAPON_IMAGE_SIZE);

        ImageView subWeaponImageView = mainWeapon.getSubWeapon().getImage();
        subWeaponImageView.setFitHeight(SUB_WEAPON_IMAGE_SIZE);
        subWeaponImageView.setFitWidth(SUB_WEAPON_IMAGE_SIZE);

        ImageView specialWeaponImageView = mainWeapon.getSpecialWeapon().getImage();
        specialWeaponImageView.setFitHeight(SUB_WEAPON_IMAGE_SIZE);
        specialWeaponImageView.setFitWidth(SUB_WEAPON_IMAGE_SIZE);

        weaponHolder.getChildren().add(mainWeaponImageView);

        HBox subSpecialHolder = new HBox();
        subSpecialHolder.getChildren().add(subWeaponImageView);
        subSpecialHolder.getChildren().add(specialWeaponImageView);

        weaponHolder.getChildren().add(subSpecialHolder);

        return weaponHolder;
    }


}
