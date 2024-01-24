package splatnet.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import splatnet.s3s.Iksm;
import splatnet.s3s.S3SMain;
import splatnet.s3s.UtilitaryS3S;

import java.awt.Desktop;
import java.lang.reflect.Array;
import java.util.ArrayList;


public class LoginController extends Controller {

    private String url;
    private String authCode;

    @FXML
    public TextField textInput;

    @FXML
    public void initialize() {
        System.out.println("LoginController initialized");

        String version = Iksm.getNsoappVersion();
        String userAgent = UtilitaryS3S.APP_USER_AGENT;
        ArrayList<String> data = Iksm.getConnectionUrl(version, userAgent);

        url = data.get(0);
        authCode = data.get(1);

    }

    @FXML
    public void openInBrowser() {
        System.out.println("Open in browser");

        // open url in browser
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.browse(java.net.URI.create(url));
        } catch (Exception e) {
            System.out.println("Error while opening url in browser");
            System.out.println(e);
        }

    }

    @FXML
    public void confirm() {
        String token = textInput.getText();

        try {
            System.out.println(token);
            S3SMain.createTokens(token, authCode);

            UtilitaryS3S.checkTokens();

            loadNewFxml("homePage");

        } catch (Exception e) {
            System.out.println("Error while initializing UtilitaryS3S");
            System.out.println(e);
        }

    }

}
