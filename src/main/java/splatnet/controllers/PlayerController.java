package splatnet.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.*;
import splatnet.models.Storage;
import splatnet.s3s.classes.game.Player;
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

        


    }

    private void displayWeapon() {
    }

    private void displayGear() {
    }


}
