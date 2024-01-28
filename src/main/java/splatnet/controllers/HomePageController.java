package splatnet.controllers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import splatnet.Main;
import splatnet.models.Storage;
import splatnet.s3s.S3SMain;
import splatnet.s3s.classes.Friend;
import splatnet.s3s.classes.Game;
import splatnet.s3s.classes.Player;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class HomePageController extends Controller {

    private Player myLastGamePlayer;

    private HashMap<String, String> xPowers;

    @FXML
    public HBox powerHolder;

    @FXML
    public VBox bannerHolder;

    @FXML
    public VBox weaponHolder;

    @FXML
    public HBox gearHolder;

    @FXML
    public VBox background;


    @FXML
    public void initialize() {

        System.out.println("HomePageController initialized");

        fetchData();
    }

    private void fetchData() {
        // use a thread to fetch last game data and another to call fetchXPowers
        Thread fetchLastGameData = new Thread(() -> {
            myLastGamePlayer = S3SMain.fetchMyLastGameData();
            Platform.runLater(this::displayLastGameInfos);
        });
        System.out.println("Starting fetchLastGameData thread");
        fetchLastGameData.start();

        Thread fetchXPowersData = new Thread(() -> {
            xPowers = S3SMain.fetchXPowers();
            Platform.runLater(this::displayXPowers);
        });
        System.out.println("Starting fetchXPowersData thread");
        fetchXPowersData.start();

        Storage storage = Storage.getInstance();
        storage.setPlayerData(myLastGamePlayer);
        storage.setxPowers(xPowers);
    }
    private void displayXPowers() {

        VBox zonesVBox = new VBox();
        zonesVBox.setAlignment(Pos.CENTER);
        ImageView zonesImage = new ImageView(String.valueOf(Main.class.getResource("assets/modes/S3_icon_Splat_Zones.png")));
        zonesImage.setFitHeight(128);
        zonesImage.setFitWidth(128);
        zonesVBox.getChildren().add(zonesImage);
        Label zonesLabel = new Label(xPowers.get("zones").substring(0, 7));
        zonesLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 20px");
        zonesVBox.getChildren().add(zonesLabel);

        VBox towerVBox = new VBox();
        towerVBox.setAlignment(Pos.CENTER);
        ImageView towerImage = new ImageView(String.valueOf(Main.class.getResource("assets/modes/S3_icon_Tower_Control.png")));
        towerImage.setFitHeight(128);
        towerImage.setFitWidth(128);
        towerVBox.getChildren().add(towerImage);
        Label towerLabel = new Label(xPowers.get("tower").substring(0, 7));
        towerLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 20px");
        towerVBox.getChildren().add(towerLabel);

        VBox rainmakerVBox = new VBox();
        rainmakerVBox.setAlignment(Pos.CENTER);
        ImageView rainImage = new ImageView(String.valueOf(Main.class.getResource("assets/modes/S3_icon_Rainmaker.png")));
        rainImage.setFitHeight(128);
        rainImage.setFitWidth(128);
        rainmakerVBox.getChildren().add(rainImage);
        Label rainmakerLabel = new Label(xPowers.get("rainmaker").substring(0, 7));
        rainmakerLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 20px");
        rainmakerVBox.getChildren().add(rainmakerLabel);

        VBox clamVBox = new VBox();
        clamVBox.setAlignment(Pos.CENTER);
        ImageView clamImage = new ImageView(String.valueOf(Main.class.getResource("assets/modes/S3_icon_Clam_Blitz.png")));
        clamImage.setFitHeight(128);
        clamImage.setFitWidth(128);
        clamVBox.getChildren().add(clamImage);
        Label clamLabel = new Label(xPowers.get("clam").substring(0, 7));
        clamLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 20px");
        clamVBox.getChildren().add(clamLabel);

        powerHolder.getChildren().add(zonesVBox);
        powerHolder.getChildren().add(towerVBox);
        powerHolder.getChildren().add(rainmakerVBox);
        powerHolder.getChildren().add(clamVBox);


    }

    private void displayLastGameInfos() {

        displayWeapon();
        displayStuffs();

        System.out.println("Displaying last game infos");

        JsonObject bannerObject = myLastGamePlayer.getBanner();

        String bannerID = bannerObject.get("id").getAsString();

        File bannerFile = new File("src/main/resources/splatnet/assets/banners/" + bannerID + ".png");

        if (!bannerFile.exists()) {
            System.out.println("Banner file doesn't exist, downloading it");
            S3SMain.downloadBanner(bannerObject);
        }

        ImageView banner = new ImageView(bannerFile.toURI().toString());
        banner.setFitHeight(200);
        banner.setFitWidth(668);

        Label quoteLabel = new Label(myLastGamePlayer.getQuote());
        quoteLabel.setStyle("-fx-text-fill: #FFFFFF");

        // place the quote in the upper left corner of the banner
        HBox quoteHolder = new HBox();
        quoteHolder.setAlignment(Pos.TOP_LEFT);
        quoteHolder.setStyle("-fx-padding: 0 0 0 20");
        quoteLabel.setFont(Font.font("Splatoon2", 20));

        Label nameLabel = new Label(myLastGamePlayer.getName());
        nameLabel.setStyle("-fx-text-fill: #FFFFFF");

        // place the name in center
        HBox nameHolder = new HBox();
        nameHolder.setAlignment(Pos.CENTER);
        nameLabel.setFont(javafx.scene.text.Font.font("Splatoon2", 50));


        Label idLabel = new Label("#" + myLastGamePlayer.getNameId());
        idLabel.setStyle("-fx-text-fill: #FFFFFF");

        // place the id in the lower left corner of the banner
        HBox idHolder = new HBox();
        idHolder.setAlignment(Pos.TOP_LEFT);
        idHolder.setStyle("-fx-padding: 0 0 0 20");
        idLabel.setFont(javafx.scene.text.Font.font("Splatoon2", 20));

        bannerHolder.getChildren().clear();

        // no repeat
        BackgroundImage test = new BackgroundImage(
                new Image(bannerFile.toURI().toString()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                null
//                new BackgroundSize(668, 200, false, false, false, false)
        );

        bannerHolder.setBackground(new Background(test));

        quoteHolder.getChildren().add(quoteLabel);
        nameHolder.getChildren().add(nameLabel);
        idHolder.getChildren().add(idLabel);

        bannerHolder.getChildren().add(quoteHolder);
        bannerHolder.getChildren().add(nameHolder);
        bannerHolder.getChildren().add(idHolder);

        background.setStyle("-fx-background-color: #2c2c2c");


    }

    private void displayStuffs() {

        JsonObject headGear = myLastGamePlayer.getHeadGear();
        JsonObject clothes = myLastGamePlayer.getClothingGear();
        JsonObject shoes = myLastGamePlayer.getShoesGear();

        VBox headGearVBox = new VBox();
        headGearVBox.setAlignment(Pos.CENTER);
        HBox headGearHBox = new HBox();
        headGearHBox.setAlignment(Pos.BASELINE_CENTER);
        ImageView headGearImage = new ImageView(new Image(headGear.get("image").getAsJsonObject()
                                                                  .get("url").getAsString()));
        headGearImage.setFitHeight(128);
        headGearImage.setFitWidth(128);

        ImageView headGearMain = new ImageView(new Image(headGear.get("primaryGearPower").getAsJsonObject()
                                                                          .get("image").getAsJsonObject()
                                                                          .get("url").getAsString()));
        headGearMain.setFitHeight(60);
        headGearMain.setFitWidth(60);
        headGearHBox.getChildren().add(headGearMain);

        for (JsonElement sub : headGear.get("additionalGearPowers").getAsJsonArray()) {
            ImageView subAbilityImage = new ImageView(new Image(sub.getAsJsonObject()
                                                                    .get("image").getAsJsonObject()
                                                                    .get("url").getAsString()));
            subAbilityImage.setFitHeight(35);
            subAbilityImage.setFitWidth(35);
            headGearHBox.getChildren().add(subAbilityImage);
        }

        headGearVBox.getChildren().add(headGearImage);
        headGearVBox.getChildren().add(headGearHBox);


        VBox clothesVBox = new VBox();
        clothesVBox.setAlignment(Pos.CENTER);
        HBox clothesHBox = new HBox();
        clothesHBox.setAlignment(Pos.BASELINE_CENTER);
        ImageView clothesImage = new ImageView(new Image(clothes.get("image").getAsJsonObject()
                                                                  .get("url").getAsString()));
        clothesImage.setFitHeight(128);
        clothesImage.setFitWidth(128);

        ImageView clothesGearMain = new ImageView(new Image(clothes.get("primaryGearPower").getAsJsonObject()
                                                                           .get("image").getAsJsonObject()
                                                                           .get("url").getAsString()));
        clothesGearMain.setFitHeight(60);
        clothesGearMain.setFitWidth(60);
        clothesHBox.getChildren().add(clothesGearMain);

        for (JsonElement sub : clothes.get("additionalGearPowers").getAsJsonArray()) {
            ImageView subAbilityImage = new ImageView(new Image(sub.getAsJsonObject()
                                                                    .get("image").getAsJsonObject()
                                                                    .get("url").getAsString()));
            subAbilityImage.setFitHeight(35);
            subAbilityImage.setFitWidth(35);
            clothesHBox.getChildren().add(subAbilityImage);
        }

        clothesVBox.getChildren().add(clothesImage);
        clothesVBox.getChildren().add(clothesHBox);


        VBox shoesVBox = new VBox();
        shoesVBox.setAlignment(Pos.CENTER);
        HBox shoesHBox = new HBox();
        shoesHBox.setAlignment(Pos.BASELINE_CENTER);
        ImageView shoesImage = new ImageView(new Image(shoes.get("image").getAsJsonObject()
                                                                  .get("url").getAsString()));
        shoesImage.setFitHeight(128);
        shoesImage.setFitWidth(128);

        ImageView shoesGearMain = new ImageView(new Image(shoes.get("primaryGearPower").getAsJsonObject()
                                                                           .get("image").getAsJsonObject()
                                                                           .get("url").getAsString()));
        shoesGearMain.setFitHeight(60);
        shoesGearMain.setFitWidth(60);
        shoesHBox.getChildren().add(shoesGearMain);

        for (JsonElement sub : shoes.get("additionalGearPowers").getAsJsonArray()) {
            ImageView subAbilityImage = new ImageView(new Image(sub.getAsJsonObject()
                                                                    .get("image").getAsJsonObject()
                                                                    .get("url").getAsString()));
            subAbilityImage.setFitHeight(35);
            subAbilityImage.setFitWidth(35);
            shoesHBox.getChildren().add(subAbilityImage);
        }

        shoesVBox.getChildren().add(shoesImage);
        shoesVBox.getChildren().add(shoesHBox);

        gearHolder.getChildren().add(headGearVBox);
        gearHolder.getChildren().add(clothesVBox);
        gearHolder.getChildren().add(shoesVBox);


    }

    private void displayWeapon() {

        JsonObject weaponData = myLastGamePlayer.getWeapon();

        String mainWeaponId = weaponData.get("id").getAsString();

        File mainWeaponFile = new File("src/main/resources/splatnet/assets/weapons/" + mainWeaponId + ".png");

        if (!mainWeaponFile.exists()) {
            System.out.println("Main weapon file doesn't exist, downloading it");
            S3SMain.downloadWeapon(weaponData, "weapons");
        }

        String specialWeaponId = weaponData.get("specialWeapon").getAsJsonObject()
                                            .get("id").getAsString();

        File specialWeaponFile = new File("src/main/resources/splatnet/assets/specials/" + specialWeaponId + ".png");

        String subWeaponId = weaponData.get("subWeapon").getAsJsonObject()
                                       .get("id").getAsString();

        File subWeaponFile = new File("src/main/resources/splatnet/assets/subs/" + subWeaponId + ".png");


        ImageView weaponImage = new ImageView(mainWeaponFile.toURI().toString());
        weaponImage.setFitHeight(150);
        weaponImage.setFitWidth(150);

        ImageView specialWeaponImage = new ImageView(specialWeaponFile.toURI().toString());
        specialWeaponImage.setFitHeight(75);
        specialWeaponImage.setFitWidth(75);

        ImageView subWeaponImage = new ImageView(subWeaponFile.toURI().toString());
        subWeaponImage.setFitHeight(75);
        subWeaponImage.setFitWidth(75);

        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().add(subWeaponImage);
        hbox.getChildren().add(specialWeaponImage);

        weaponHolder.getChildren().add(weaponImage);
        weaponHolder.getChildren().add(hbox);

    }



}