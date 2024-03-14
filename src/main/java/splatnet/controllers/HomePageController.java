package splatnet.controllers;

import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import splatnet.Main;
import splatnet.models.Storage;
import splatnet.s3s.S3SMain;
import splatnet.s3s.UtilitaryS3S;
import splatnet.s3s.classes.game.Player;
import splatnet.s3s.classes.misc.Ability;
import splatnet.s3s.classes.misc.NamePlate;
import splatnet.s3s.classes.misc.Stuff;
import splatnet.s3s.classes.weapons.MainWeapon;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;

public class HomePageController extends Controller {

    private static final String BANNER_URL = "assets/banners/";

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

        bannerHolder.getChildren().clear();
        bannerHolder.getChildren().add(myLastGamePlayer.getBannerBuild());

//        NamePlate namePlate = myLastGamePlayer.getNamePlate();
//
//        ImageView banner = myLastGamePlayer.getNamePlate().getBanner().getImage();
//        banner.setFitHeight(200);
//        banner.setFitWidth(668);
//
//        Label quoteLabel = new Label(myLastGamePlayer.getQuote());
//        quoteLabel.setStyle("-fx-text-fill: #FFFFFF");
//
//        // place the quote in the upper left corner of the banner
//        HBox quoteHolder = new HBox();
//        quoteHolder.setAlignment(Pos.TOP_LEFT);
//        quoteHolder.setStyle("-fx-padding: 0 0 0 20");
//        quoteLabel.setFont(Font.font("Splatoon2", 20));
//
//        Label nameLabel = new Label(myLastGamePlayer.getName());
//        nameLabel.setStyle("-fx-text-fill: #FFFFFF");
//
//        // place the name in center
//        HBox nameHolder = new HBox();
//        nameHolder.setAlignment(Pos.CENTER);
//        nameLabel.setFont(javafx.scene.text.Font.font("Splatoon2", 50));
//
//
//        Label idLabel = new Label("#" + myLastGamePlayer.getNameId());
//        idLabel.setStyle("-fx-text-fill: #FFFFFF");
//
//        // place the id in the lower left corner of the banner
//        HBox idHolder = new HBox();
//        idHolder.setAlignment(Pos.TOP_LEFT);
//        idHolder.setStyle("-fx-padding: 0 0 0 20");
//        idLabel.setFont(javafx.scene.text.Font.font("Splatoon2", 20));
//
//        bannerHolder.getChildren().clear();
//
//        // no repeat
//        BackgroundImage test = new BackgroundImage(
//                banner.getImage(),
//                BackgroundRepeat.NO_REPEAT,
//                BackgroundRepeat.NO_REPEAT,
//                BackgroundPosition.DEFAULT,
//                null
////                new BackgroundSize(668, 200, false, false, false, false)
//        );
//
//        bannerHolder.setBackground(new Background(test));
//
//        quoteHolder.getChildren().add(quoteLabel);
//        nameHolder.getChildren().add(nameLabel);
//        idHolder.getChildren().add(idLabel);
//
//        bannerHolder.getChildren().add(quoteHolder);
//        bannerHolder.getChildren().add(nameHolder);
//        bannerHolder.getChildren().add(idHolder);
//
//        background.setStyle("-fx-background-color: #2c2c2c");


    }

    private void displayStuffs() {

        Stuff headGear = myLastGamePlayer.getHeadGear();
        Stuff clothes = myLastGamePlayer.getClothingGear();
        Stuff shoes = myLastGamePlayer.getShoesGear();

        VBox headGearVBox = new VBox();
        headGearVBox.setAlignment(Pos.CENTER);
        HBox headGearHBox = new HBox();
        headGearHBox.setAlignment(Pos.BASELINE_CENTER);
        ImageView headGearImage = headGear.getImage();
        headGearImage.setFitHeight(128);
        headGearImage.setFitWidth(128);

        ImageView headGearMain = headGear.getMainAbility().getImage();
        headGearMain.setFitHeight(60);
        headGearMain.setFitWidth(60);
        headGearHBox.getChildren().add(headGearMain);

        for (Ability sub : headGear.getSubAbilities()) {
            ImageView subAbilityImage = sub.getImage();
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
        ImageView clothesImage = clothes.getImage();
        clothesImage.setFitHeight(128);
        clothesImage.setFitWidth(128);

        ImageView clothesGearMain = clothes.getMainAbility().getImage();
        clothesGearMain.setFitHeight(60);
        clothesGearMain.setFitWidth(60);
        clothesHBox.getChildren().add(clothesGearMain);

        for (Ability sub : clothes.getSubAbilities()) {
            ImageView subAbilityImage = sub.getImage();
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
        ImageView shoesImage = shoes.getImage();
        shoesImage.setFitHeight(128);
        shoesImage.setFitWidth(128);

        ImageView shoesGearMain = shoes.getMainAbility().getImage();
        shoesGearMain.setFitHeight(60);
        shoesGearMain.setFitWidth(60);
        shoesHBox.getChildren().add(shoesGearMain);

        for (Ability sub : shoes.getSubAbilities()) {
            ImageView subAbilityImage = sub.getImage();
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

        MainWeapon weaponData = myLastGamePlayer.getWeapon();

        ImageView weaponImage = weaponData.getImage();
        weaponImage.setFitHeight(150);
        weaponImage.setFitWidth(150);

        ImageView specialWeaponImage = weaponData.getSpecialWeapon().getImage();
        specialWeaponImage.setFitHeight(75);
        specialWeaponImage.setFitWidth(75);

        ImageView subWeaponImage = weaponData.getSubWeapon().getImage();
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