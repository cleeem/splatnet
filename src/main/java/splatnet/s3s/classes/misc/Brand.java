package splatnet.s3s.classes.misc;

import com.google.gson.JsonObject;
import splatnet.Main;
import splatnet.s3s.UtilitaryS3S;

import javafx.scene.image.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeSet;

public class Brand implements Comparable<Brand> {
    private String id;

    private String name;

    private String imagePath;


    private static TreeSet<Brand> brands = new TreeSet<>();

    public static Brand getBrand(String id) {
        for (Brand brand : brands) {
            if (brand.id.equals(id)) {
                return brand;
            }
        }
        return null;
    }

    public static void addBrand(Brand brand) {
        brands.add(brand);
    }

    public Brand(String id, String name, String path) {
        this.id = id;
        this.name = name;
        this.imagePath = path;
    }

    public Brand(JsonObject data) {
        this.id = data.get("id").getAsString();
        this.name = data.get("name").getAsString();

        String path = String.valueOf(Main.class.getResource("assets/brands/" + this.id + ".png"));
        if (path.equals("null")) {
            File temp = new File(String.valueOf(Main.class.getResource("assets/brands/" + this.id + ".png")));
            if (!temp.exists()) {
                try {
                    UtilitaryS3S.downloadImage(
                            data.get("image").getAsJsonObject().get("url").getAsString(),
                            this.id + "",
                            "brands"
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        this.imagePath = path;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ImageView getImage() {
        return new ImageView(imagePath);
    }

    public static TreeSet<Brand> getBrands() {
        return brands;
    }

    @Override
    public int compareTo(Brand o) {
        return getId().compareTo(o.getId());
    }
}
