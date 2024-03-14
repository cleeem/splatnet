package splatnet.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import splatnet.Main;
import splatnet.models.Storage;
import splatnet.s3s.S3SMain;
import splatnet.s3s.classes.game.Game;
import splatnet.s3s.classes.game.Player;
import splatnet.s3s.classes.game.Team;
import splatnet.s3s.classes.weapons.MainWeapon;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class HistoryController extends Controller {

    private static final String ASSETS_URL = "assets/";

    private boolean showingGames = false;

    private Stage stage;

    @FXML
    public VBox matchList;

    @FXML
    public Button buttonAll;

    @FXML
    public Button buttonTurf;

    @FXML
    public Button buttonAnarchy;

    @FXML
    public Button buttonX;

    @FXML
    public Button buttonPrivate;

    @FXML
    public Button buttonChallenge;

    @FXML
    public Label count;

    private Button lastClicked;

    private Label loadingLabel = new Label("Fetching latest data, please wait... (about 20 seconds)");

    @FXML
    public void initialize() {
        lastClicked = buttonAll;

        loadingLabel.setFont(Font.font("Splatoon2", 20));
        loadingLabel.setStyle("-fx-text-fill: white;");

        matchList.getChildren().add(
                loadingLabel
        );


        Storage storage = Storage.getInstance();
        if (storage.getTurfWarGames().isEmpty()) {
            fetchData(buttonAll);
        } else {
            displayLatestMatches();
        }

    }

    private void fetchData(Button btn) {
        Storage storage = Storage.getInstance();

        Thread allMatches = new Thread(() -> {
            try {
                ArrayList<Game> all = S3SMain.fetchLattestBattles();
                storage.setLatestGames(all);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (btn.equals(buttonAll)) {
                Platform.runLater(this::displayLatestMatches);
            }
        });

        Thread turfMatches = new Thread(() -> {
            try {
                ArrayList<Game> turf = S3SMain.fetchTurfWarBattles();
                storage.setTurfWarGames(turf);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (btn.equals(buttonTurf)) {
                Platform.runLater(this::displayTurfWarMatches);
            }
        });

        Thread anarchyMatches = new Thread(() -> {
            try {
                ArrayList<Game> anarchy = S3SMain.fetchAnarchyBattles();
                storage.setAnarchyGames(anarchy);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (btn.equals(buttonAnarchy)) {
                Platform.runLater(this::displayAnarchyMatches);
            }
        });

        Thread xMatches = new Thread(() -> {
            try {
                ArrayList<Game> x = S3SMain.fetchXBattles();
                storage.setxGames(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (btn.equals(buttonX)) {
                Platform.runLater(this::displayXMatches);
            }
        });

        Thread privateMatches = new Thread(() -> {
            try {
                ArrayList<Game> privateBattles = S3SMain.fetchPrivateBattles();
                storage.setPrivateGames(privateBattles);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (btn.equals(buttonPrivate)) {
                Platform.runLater(this::displayPrivateMatches);
            }
        });

        Thread challengeMatches = new Thread(() -> {
            try {
                ArrayList<Game> challenge = S3SMain.fetchChallengeBattles();
                storage.setChallengeGames(challenge);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (btn.equals(buttonChallenge)) {
                Platform.runLater(this::displayChallengeMatches);
            }
        });

        allMatches.start();
        turfMatches.start();
        anarchyMatches.start();
        xMatches.start();
        privateMatches.start();
        challengeMatches.start();
    }

    @FXML
    public void displayLatestMatches() {
        Storage storage = Storage.getInstance();
        ArrayList<Game> games = storage.getLatestGames();
        displayGames(games);
        lastClicked.getStyleClass().remove("selected");
        lastClicked = buttonAll;
        buttonAll.getStyleClass().add("selected");

    }

    @FXML
    public void displayTurfWarMatches() {
        Storage storage = Storage.getInstance();
        ArrayList<Game> games = storage.getTurfWarGames();
        displayGames(games);
        lastClicked.getStyleClass().remove("selected");
        lastClicked = buttonTurf;
        buttonTurf.getStyleClass().add("selected");
    }

    @FXML
    public void displayAnarchyMatches() {
        Storage storage = Storage.getInstance();
        ArrayList<Game> games = storage.getAnarchyGames();
        displayGames(games);
        lastClicked.getStyleClass().remove("selected");
        lastClicked = buttonAnarchy;
        buttonAnarchy.getStyleClass().add("selected");
    }

    @FXML
    public void displayXMatches() {
        Storage storage = Storage.getInstance();
        ArrayList<Game> games = storage.getxGames();
        displayGames(games);
        lastClicked.getStyleClass().remove("selected");
        lastClicked = buttonX;
        buttonX.getStyleClass().add("selected");
    }

    @FXML
    public void displayPrivateMatches() {
        Storage storage = Storage.getInstance();
        ArrayList<Game> games = storage.getPrivateGames();
        displayGames(games);
        lastClicked.getStyleClass().remove("selected");
        lastClicked = buttonPrivate;
        buttonPrivate.getStyleClass().add("selected");
    }

    @FXML
    public void displayChallengeMatches() {
        Storage storage = Storage.getInstance();
        ArrayList<Game> games = storage.getChallengeGames();
        displayGames(games);
        lastClicked.getStyleClass().remove("selected");
        lastClicked = buttonChallenge;
        buttonChallenge.getStyleClass().add("selected");
    }

    @FXML
    public void refresh() {
        matchList.getChildren().clear();

        String filePath = ASSETS_URL + "other/loading.gif";
        File loading = null;

        InputStream inputStream = Main.class.getResourceAsStream(filePath);

        // Si la ressource est trouvée, créez un fichier temporaire pour la stocker localement
        try {
            loading = File.createTempFile("loading", ".png");
            // Copiez les données du flux d'entrée vers le fichier temporaire
            Files.copy(inputStream, loading.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Fermez le flux d'entrée
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ImageView loadingView = new ImageView(loading.toURI().toString());
        loadingView.setFitHeight(100);
        loadingView.setFitWidth(100);
        matchList.getChildren().add(loadingView);

        matchList.getChildren().add(
                loadingLabel
        );

        Storage storage = Storage.getInstance();
        storage.setLatestGames(null);
        storage.setTurfWarGames(null);
        storage.setAnarchyGames(null);
        storage.setxGames(null);
        storage.setPrivateGames(null);
        storage.setChallengeGames(null);

        fetchData(lastClicked);

    }


    private void displayGames(ArrayList<Game> games) {
        matchList.getChildren().clear();

        int wins = 0;
        int loses = 0;
        int draws = 0;

        for (Game game : games) {
            HBox gameDisplay = getMatchDisplay(game);

            if (game.getStatus().equals("WIN")) {
                wins++;
            } else if (game.getStatus().equals("LOSE")) {
                loses++;
            } else {
                draws++;
            }

            gameDisplay.setOnMouseClicked(event -> {
                try {
                    openMatch(game);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            matchList.getChildren().add(gameDisplay);
        }

        count.setText("" + wins + " - " + loses + " - " + draws);

    }

    private void openMatch(Game game) {
        if (showingGames) {
            stage.close();
            showingGames = false;
        }
        Storage storage = Storage.getInstance();
        storage.setSelectedGame(game);
        try {
            stage = newWindowLoad("match");
            stage.show();
            showingGames = true;
        } catch (IOException e) {
            showingGames = false;
            throw new RuntimeException(e);
        }
    }

    private HBox getMatchDisplay(Game game) {

        HBox matchDisplay = new HBox();
        // set css class to the matchDisplay
        matchDisplay.getStyleClass().add("game");
        matchDisplay.setMinHeight(100);
        matchDisplay.setSpacing(10);
        matchDisplay.setMinWidth(750);
        matchDisplay.setMaxWidth(900);
        matchDisplay.setAlignment(Pos.CENTER_LEFT);

        Team myTeam = game.getMyTeam();

        String mode = getMode(game.getVsMode());

        if (mode.equals("x")) {
            mode = "xBattles";
        }

        if (mode == null) {
            System.out.println(game.getVsMode());
            System.out.println("mode is null");
            return matchDisplay;
        }

        ImageView modeIconView = new ImageView(
                String.valueOf(Main.class.getResource("assets/battles/" + mode + ".png"))
        );

        modeIconView.setFitHeight(64);
        modeIconView.setFitWidth(64);

        MainWeapon weapon = null;
        Storage storage = Storage.getInstance();
        for (Player player : myTeam.getPlayers()) {
            if (player.isMyself()) {
                if (storage.getPlayerData() == null) {
                    storage.setPlayerData(player);
                }
                weapon = player.getWeapon();
            }
        }
        ImageView weaponIconView = weapon.getImage();
        weaponIconView.setFitHeight(64);
        weaponIconView.setFitWidth(64);

        String teamScore;
        if (mode.equals("turf")) {
            if (!myTeam.getScore().equals("DRAW")) {
                teamScore = "" + Double.parseDouble(myTeam.getScore()) * 100;
                teamScore = teamScore.substring(0, teamScore.indexOf(".") + 2);
            } else {
                teamScore = myTeam.getScore();
            }
        } else {
            teamScore = myTeam.getScore();
        }

        Label gameStatus = new Label(game.getStatus());
        String textColor;
        if (game.getStatus().equals("WIN")) {
            textColor = "#a40840";
        } else if (game.getStatus().equals("LOSE")) {
            textColor = "#00933f";
        } else {
            textColor = "#ffffff";
        }


        gameStatus.setStyle("-fx-text-fill:" + textColor);
        gameStatus.setFont(Font.font("Splatoon2", 40));
        gameStatus.setAlignment(Pos.CENTER);

        gameStatus.setMinWidth(100);

        Label labelScore = new Label(game.getVsRule() + ", PROGRESSION : " + teamScore);
        labelScore.setStyle("-fx-text-fill: white;");
        labelScore.setFont(Font.font("Splatoon2", 25));


        Line line = new Line();
        line.setStartY(0);
        line.setEndY(100);
        line.setStyle("-fx-stroke: white;");

        Line line2 = new Line();
        line2.setStartY(0);
        line2.setEndY(100);
        line2.setStyle("-fx-stroke: white;");

        Line line3 = new Line();
        line3.setStartY(0);
        line3.setEndY(100);
        line3.setStyle("-fx-stroke: white;");


        matchDisplay.getChildren().add(modeIconView);
        matchDisplay.getChildren().add(line);
        matchDisplay.getChildren().add(weaponIconView);
        matchDisplay.getChildren().add(line2);
        matchDisplay.getChildren().add(gameStatus);
        matchDisplay.getChildren().add(line3);
        matchDisplay.getChildren().add(labelScore);

        return matchDisplay;
    }

    private String getMode(String mode) {
        switch (mode) {
            case "PRIVATE":
                return "private";
            case "REGULAR":
                return "turf";
            case "BANKARA":
                return "anarchy";
            case "X_MATCH":
                return "x";
            case "LEAGUE":
                return "challenge";
            case "FEST":
                return "splatfest";
            default:
                return null;
        }
    }

}
