package splatnet;

import javafx.fxml.FXML;
import splatnet.controllers.Controller;
import splatnet.models.Storage;
import splatnet.s3s.Iksm;
import splatnet.s3s.UtilitaryS3S;

import java.io.IOException;

public class MainController extends Controller {

    private int needToLogin = 0;

    @FXML
    public void initialize() {
        System.out.println("MainController initialized");
        try {
            UtilitaryS3S.setup();
            UtilitaryS3S.checkTokens();
            // no error so we don't need to login
            needToLogin = 1;
            System.out.println("No need to login");
        } catch (RuntimeException e) {

            if (e.getMessage().equals("gtoken expired")) {
                UtilitaryS3S.writeConfig(Iksm.getTokens(UtilitaryS3S.sessionToken));
                System.out.println("generated new tokens");
                needToLogin = 1;
            } else if (e.getMessage().equals("internet error")) {
                needToLogin = 2;
                System.out.println("internet error");
            } else {
                needToLogin = 0;
                System.out.println("Need to login");

            }

        }
    }

    public void startApplication() throws IOException {
        Storage storage = Storage.getInstance();
        System.out.println("Start button clicked");
        if (needToLogin == 0) {
            loadNewFxml("config");
        } else if (needToLogin == 1) {
            loadNewFxml("homePage");
        } else if (needToLogin == 2) {
            loadNewFxml("internetError");
        }


    }

}
