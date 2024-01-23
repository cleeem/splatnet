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

    private boolean needToLogin = true;

    @FXML
    public void initialize() {
        System.out.println("MainController initialized");
        try {
            UtilitaryS3S.checkTokens();
            needToLogin = false;
        } catch (Exception e) {
            // config error so we redirect to the config page
            needToLogin = true;
        }
    }

    public void startApplication() throws IOException {
        System.out.println("Start button clicked");
//        try {
            if (needToLogin) {
                System.out.println("Need to login");
                loadNewFxml("config");
            } else {
                System.out.println("No need to login");
                loadNewFxml("homePage");
            }
//        } catch (Exception e) {
//            System.out.println("Error while loading new fxml");
//            System.out.println(e);
//
//        }


    }

}
