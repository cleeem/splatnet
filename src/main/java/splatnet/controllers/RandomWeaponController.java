package splatnet.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import splatnet.s3s.classes.weapons.MainWeapon;

import java.util.TreeSet;

public class RandomWeaponController {

    @FXML
    private ImageView mainWeaponHolder;

    @FXML
    private ImageView specialWeaponHolder;

    @FXML
    private ImageView subWeaponHolder;

    @FXML
    void generateWeapon(MouseEvent event) {
        TreeSet<MainWeapon> mainWeapons = MainWeapon.getMainWeapons();
        int random = (int) (Math.random() * mainWeapons.size());
        MainWeapon mainWeapon = (MainWeapon) mainWeapons.toArray()[random];
        mainWeaponHolder.setImage(mainWeapon.getImage().getImage());
        specialWeaponHolder.setImage(mainWeapon.getSpecialWeapon().getImage().getImage());
        subWeaponHolder.setImage(mainWeapon.getSubWeapon().getImage().getImage());
    }

}