package splatnet.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import splatnet.models.Storage;
import splatnet.s3s.classes.game.Player;
import splatnet.s3s.classes.misc.Ability;
import splatnet.s3s.classes.misc.Badge;
import splatnet.s3s.classes.misc.Banner;
import splatnet.s3s.classes.misc.NamePlate;

import java.util.ArrayList;

public class PlayerController {

    @FXML
    private HBox bannerHolder;

    @FXML
    private VBox gearHolder;

    @FXML
    private HBox statsHolder;

    @FXML
    private VBox weaponHolder;

    private Player player;

    @FXML
    public void initialize() {

        Storage storage = Storage.getInstance();
        player = storage.getPlayer();

        displayBanner();
        displayStats();
        displayWeapon();
        displayGear();
    }


    private void displayBanner() {

        bannerHolder.getChildren().add(player.getBannerBuild());

    }

    private void displayStats() {
        Font font = new Font("Splatoon2", 20);

        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);
        container.setSpacing(10);

        Label title = new Label();
        title.setFont(new Font("Splatoon2", 30));
        title.setText("Stats");
        title.setTextFill(Color.WHITE);

        container.getChildren().add(title);

        Label stats = new Label();
        stats.setFont(font);
        stats.setText("Kills: " + player.getKills() + " | Deaths: " + player.getDeath() + " | Assists: " + player.getAssists() + " | Special: " + player.getSpecial() + " | Paint: " + player.getPaint());
        stats.setTextFill(Color.WHITE);

        container.getChildren().add(stats);

        statsHolder.getChildren().add(container);

    }

    private void displayWeapon() {

        Font font = new Font("Splatoon2", 20);

        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);
        container.setSpacing(10);

        Label title = new Label();
        title.setFont(new Font("Splatoon2", 30));
        title.setText("Weapon");
        title.setTextFill(Color.WHITE);

        container.getChildren().add(title);

        HBox weapon = new HBox();
        weapon.setAlignment(Pos.CENTER);
        weapon.setSpacing(20);

        int weaponSize = 50;

        ImageView weaponImage = player.getWeapon().getImage();
        weaponImage.setFitHeight(weaponSize * 2);
        weaponImage.setFitWidth(weaponSize * 2);

        ImageView subImage = player.getWeapon().getSubWeapon().getImage();
        subImage.setFitHeight(weaponSize);
        subImage.setFitWidth(weaponSize);

        ImageView specialImage = player.getWeapon().getSpecialWeapon().getImage();
        specialImage.setFitHeight(weaponSize);
        specialImage.setFitWidth(weaponSize);

        weapon.getChildren().add(weaponImage);
        VBox subSpecial = new VBox();
        subSpecial.getChildren().add(subImage);
        subSpecial.getChildren().add(specialImage);
        weapon.getChildren().add(subSpecial);

        container.getChildren().add(weapon);

        weaponHolder.getChildren().add(container);

    }

    private void displayGear() {

        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);
        container.setSpacing(10);

        Label title = new Label();
        title.setFont(new Font("Splatoon2", 30));
        title.setText("Gear");
        title.setTextFill(Color.WHITE);

        container.getChildren().add(title);

        HBox head = new HBox();
        head.setAlignment(Pos.CENTER);
        head.setSpacing(10);

        int gearSize = 60;
        int abilitySize = 35;

        ImageView headImage = player.getHeadGear().getImage();
        headImage.setFitHeight(gearSize);
        headImage.setFitWidth(gearSize);

        head.getChildren().add(headImage);

        ImageView mainImage = player.getHeadGear().getMainAbility().getImage();
        mainImage.setFitHeight(abilitySize * 1.5);
        mainImage.setFitWidth(abilitySize * 1.5);
        head.getChildren().add(mainImage);

        for (Ability sub : player.getHeadGear().getSubAbilities()) {
            ImageView subImage = sub.getImage();
            subImage.setFitHeight(abilitySize);
            subImage.setFitWidth(abilitySize);
            head.getChildren().add(subImage);
        }

        container.getChildren().add(head);

        HBox clothes = new HBox();
        clothes.setAlignment(Pos.CENTER);
        clothes.setSpacing(10);

        ImageView clothesImage = player.getClothingGear().getImage();
        clothesImage.setFitHeight(gearSize);
        clothesImage.setFitWidth(gearSize);

        clothes.getChildren().add(clothesImage);

        mainImage = player.getClothingGear().getMainAbility().getImage();
        mainImage.setFitHeight(abilitySize * 1.5);
        mainImage.setFitWidth(abilitySize * 1.5);
        clothes.getChildren().add(mainImage);

        for (Ability sub : player.getClothingGear().getSubAbilities()) {
            ImageView subImage = sub.getImage();
            subImage.setFitHeight(abilitySize);
            subImage.setFitWidth(abilitySize);
            clothes.getChildren().add(subImage);
        }

        container.getChildren().add(clothes);

        HBox shoes = new HBox();
        shoes.setAlignment(Pos.CENTER);
        shoes.setSpacing(10);

        ImageView shoesImage = player.getShoesGear().getImage();
        shoesImage.setFitHeight(gearSize);
        shoesImage.setFitWidth(gearSize);

        shoes.getChildren().add(shoesImage);

        mainImage = player.getShoesGear().getMainAbility().getImage();
        mainImage.setFitHeight(abilitySize * 1.5);
        mainImage.setFitWidth(abilitySize * 1.5);

        shoes.getChildren().add(mainImage);

        for (Ability sub : player.getShoesGear().getSubAbilities()) {
            ImageView subImage = sub.getImage();
            subImage.setFitHeight(abilitySize);
            subImage.setFitWidth(abilitySize);
            shoes.getChildren().add(subImage);
        }

        container.getChildren().add(shoes);

        gearHolder.getChildren().add(container);
    }


}
