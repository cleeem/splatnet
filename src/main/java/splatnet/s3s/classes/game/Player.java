package splatnet.s3s.classes.game;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import splatnet.s3s.classes.misc.Badge;
import splatnet.s3s.classes.misc.NamePlate;
import splatnet.s3s.classes.misc.Stuff;
import splatnet.s3s.classes.weapons.MainWeapon;

import java.awt.*;

public class Player {

    private String id;

    private boolean isMyself;

    private String nameId;

    private String name;

    private String quote;

    private MainWeapon weapon;

    /**
     * Inkling, Octoling
     */
    private String species;

    private NamePlate namePlate;

    private int kills;
    private int death;

    private int assists;

    private int special;

    private int paint;

    /**
     * if the player is in top 500
     */
    private boolean crown;

    private Stuff headGear;

    private Stuff clothingGear;

    private Stuff shoesGear;

    private HBox bannerBuild;



    public Player(String jsonData) {
        this(new JsonParser().parse(jsonData).getAsJsonObject());
    }

    public Player(JsonObject jsonData) {

        if (jsonData.has("isMyself") && !jsonData.get("isMyself").isJsonNull()) {
            isMyself = jsonData.get("isMyself").getAsBoolean();
        }

        if (jsonData.has("id") && !jsonData.get("id").isJsonNull()) {
            id = jsonData.get("id").getAsString();
        }

        name = jsonData.get("name").getAsString();

        nameId = jsonData.get("nameId").getAsString();

        if (jsonData.has("byname")) {
            quote = jsonData.get("byname").getAsString();
        }

        weapon = new MainWeapon(jsonData.get("weapon").getAsJsonObject());

        if (jsonData.has("species") && !jsonData.get("species").isJsonNull()) {
            species = jsonData.get("species").getAsString();
        }

        namePlate = new NamePlate(jsonData.get("nameplate").getAsJsonObject());

        JsonObject results;
        if (jsonData.has("result") && !jsonData.get("result").isJsonNull()) {
            results = jsonData.get("result").getAsJsonObject();
            kills = results.get("kill").getAsInt();
            death = results.get("death").getAsInt();
            assists = results.get("assist").getAsInt();
            special = results.get("special").getAsInt();
        }


        if (jsonData.has("crown")) {
            crown = jsonData.get("crown").getAsBoolean();
        }

        headGear = new Stuff(jsonData.get("headGear").getAsJsonObject());

        clothingGear = new Stuff(jsonData.get("clothingGear").getAsJsonObject());

        shoesGear = new Stuff(jsonData.get("shoesGear").getAsJsonObject());

        if (jsonData.has("paint") && !jsonData.get("paint").isJsonNull()) {
            paint = jsonData.get("paint").getAsInt();
        }

        buildBanner();
    }

    private void buildBanner() {
        Font font = new Font("Splatoon2", 25);

        bannerBuild = new HBox();
        bannerBuild.setAlignment(Pos.CENTER);

        BackgroundImage backgroundImage = new BackgroundImage(
                namePlate.getBanner().getImage().getImage(),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                null,
                null
        );

        bannerBuild.setBackground(new Background(backgroundImage));

        Color color = Color.rgb(
                namePlate.getBanner().getColors().get("r"),
                namePlate.getBanner().getColors().get("g"),
                namePlate.getBanner().getColors().get("b")
        );

        Label nameLabel = new Label(name);
        nameLabel.setFont(new Font("Splatoon2", 45));
        nameLabel.setTextFill(color);

        Label nameIdLabel = new Label(nameId);
        nameIdLabel.setFont(font);
        nameIdLabel.setTextFill(color);

        Label quoteLabel = new Label(quote);
        quoteLabel.setFont(font);
        quoteLabel.setTextFill(color);

        HBox badges = new HBox();
        badges.setAlignment(Pos.CENTER_RIGHT);
        badges.setSpacing(5);

        for (Badge badge : namePlate.getBadges()) {

            if (badge != null) {
                ImageView imageView = badge.getImage();
                imageView.setFitHeight(40);
                imageView.setFitWidth(40);
                badges.getChildren().add(imageView);
            } else {
                Label space = new Label(" ");
                space.setPrefSize(40, 40);

                // we add a blank label to keep the spacing
                badges.getChildren().add(space);
            }
        }

        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);
        container.setPadding(
                new Insets(0, 0, 0, 10)
        );

        // set the container to the same size as the banner
        container.setPrefSize(
                namePlate.getBanner().getImage().getImage().getWidth(),
                namePlate.getBanner().getImage().getImage().getHeight()
        );

        HBox quoteHbox = new HBox();
        quoteHbox.setAlignment(Pos.CENTER_LEFT);
        quoteHbox.getChildren().add(quoteLabel);

        HBox nameHbox = new HBox();
        nameHbox.setAlignment(Pos.CENTER);
        nameHbox.getChildren().add(nameLabel);

        // set the nameId to the very left and the badges to the very right
        HBox bottomHbox = new HBox();
        bottomHbox.setAlignment(Pos.CENTER);

        VBox nameIdVbox = new VBox();
        nameIdVbox.setAlignment(Pos.CENTER_LEFT);
        nameIdVbox.getChildren().add(nameIdLabel);

        HBox.setHgrow(nameIdVbox, Priority.ALWAYS);

        VBox badgesVbox = new VBox();
        badgesVbox.setAlignment(Pos.CENTER_LEFT);
        badgesVbox.getChildren().add(badges);

        HBox.setHgrow(badgesVbox, Priority.ALWAYS);

        bottomHbox.setPadding(new Insets(0, 15, 0, 0));

        bottomHbox.getChildren().addAll(nameIdVbox, badgesVbox);

        container.getChildren().addAll(quoteHbox, nameHbox, bottomHbox);

        bannerBuild.getChildren().add(container);
    }

    public String getId() {
        return id;
    }

    public boolean isMyself() {return isMyself;}

    public String getName() {
        return name;
    }

    public String getQuote() {
        return quote;
    }

    public MainWeapon getWeapon() {
        return weapon;
    }

    public String getSpecies() {
        return species;
    }

    public NamePlate getNamePlate() {
        return namePlate;
    }

    public int getKills() {
        return kills;
    }

    public int getDeath() {
        return death;
    }

    public int getAssists() {
        return assists;
    }

    public int getSpecial() {
        return special;
    }

    public int getPaint() {
        return paint;
    }

    public boolean isCrown() {
        return crown;
    }

    public Stuff getHeadGear() {
        return headGear;
    }

    public Stuff getClothingGear() {
        return clothingGear;
    }

    public Stuff getShoesGear() {
        return shoesGear;
    }

    public String getNameId() {
        return nameId;
    }

    public HBox getBannerBuild() {
        return bannerBuild;
    }
}
