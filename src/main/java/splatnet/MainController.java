package splatnet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.jsoup.Jsoup;
import splatnet.controllers.Controller;
import splatnet.s3s.Iksm;
import splatnet.s3s.S3SMain;
import splatnet.s3s.UtilitaryS3S;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MainController extends Controller {

    @FXML
    public void initialize() {
        System.out.println("MainController initialized");
    }

    public void startApplication() {
        System.out.println("Button clicked");
        try {
            loadNewFxml("homePage");
        } catch (Exception e) {
            System.out.println("Error while loading new fxml");
            System.out.println(e.getMessage());
            System.out.println(e.getCause());

        }


    }

}
