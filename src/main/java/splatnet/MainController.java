package splatnet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.jsoup.Jsoup;
import splatnet.controllers.Controller;
import splatnet.s3s.Iksm;
import splatnet.s3s.S3SMain;
import splatnet.s3s.UtilitaryS3S;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class MainController extends Controller {

    private boolean needToLogin = true;

    @FXML
    public void initialize() {
        System.out.println("MainController initialized");
        try {
            UtilitaryS3S.setup();
            UtilitaryS3S.checkTokens();
            // no error so we don't need to login
            needToLogin = false;
            System.out.println("No need to login");
        } catch (RuntimeException e) {

            if (e.getMessage().equals("gtoken expired")) {
                UtilitaryS3S.writeConfig(Iksm.getTokens(UtilitaryS3S.sessionToken));
                System.out.println("generated new tokens");
                needToLogin = false;
            } else if (e.getMessage().equals("internet error")) {
                try {
                    System.out.println("loading internet error");
                    loadNewFxml("internetError");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } else {
                needToLogin = true;
                System.out.println("Need to login");

            }

        }
    }

    public void startApplication() throws IOException {
        System.out.println("Start button clicked");
        if (needToLogin) {
            loadNewFxml("config");
        } else {
            loadNewFxml("homePage");
        }


    }

}
