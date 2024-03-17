package splatnet.s3s.classes.weapons;

import com.google.gson.JsonObject;

import java.util.TreeSet;

public class MainWeapon extends Weapon implements Comparable<MainWeapon> {

    private SubWeapon subWeapon;

    private SpecialWeapon specialWeapon;

    private int paint = 0;

    private static TreeSet<MainWeapon> mainWeapons = new TreeSet<>();

    public static MainWeapon getMainWeaponById(String id) {
        for (MainWeapon mainWeapon : mainWeapons) {
            if (mainWeapon.getId().equals(id)) {
                return mainWeapon;
            }
        }
        return null;
    }

    public static MainWeapon getMainWeaponByName(String name) {
        for (MainWeapon mainWeapon : mainWeapons) {
            if (mainWeapon.getName().equals(name)) {
                return mainWeapon;
            }
        }
        System.out.println("MainWeapon not found: " + name);
        return null;
    }

    public static void addMainWeapon(MainWeapon mainWeapon) {
        mainWeapons.add(mainWeapon);
    }

    public static TreeSet<MainWeapon> getMainWeapons() {
        return mainWeapons;
    }

    public MainWeapon() {}

    public MainWeapon(String id, String name, String type, SubWeapon subWeapon, SpecialWeapon specialWeapon) {
        super(id, name, type);
        this.subWeapon = subWeapon;
        this.specialWeapon = specialWeapon;
    }

    public MainWeapon(JsonObject data) {
        super(data, "weapons");

        String subKey = "sub";
        if (data.has("subWeapon")) {
            subKey = "subWeapon";
        }

        if (SubWeapon.getSubWeaponById(data.get(subKey).getAsJsonObject().get("id").getAsString()) != null) {
            this.subWeapon = SubWeapon.getSubWeaponById(data.get(subKey).getAsJsonObject().get("id").getAsString());
        } else {
            this.subWeapon = new SubWeapon(data.get(subKey).getAsJsonObject());
            SubWeapon.addSubWeapon(this.subWeapon);
            System.out.println("SubWeapon added: " + this.subWeapon.getName());
        }

        String specialKey = "special";
        if (data.has("specialWeapon")) {
            specialKey = "specialWeapon";
        }

        if (SpecialWeapon.getSpecialWeaponById(data.get(specialKey).getAsJsonObject().get("id").getAsString()) != null) {
            this.specialWeapon = SpecialWeapon.getSpecialWeaponById(data.get(specialKey).getAsJsonObject().get("id").getAsString());
        } else {
            this.specialWeapon = new SpecialWeapon(data.get(specialKey).getAsJsonObject());
            SpecialWeapon.addSpecialWeapon(this.specialWeapon);
            System.out.println("SpecialWeapon added: " + this.specialWeapon.getName());
        }


        if (data.has("stats")
            && data.get("stats").isJsonObject()
            && data.get("stats").getAsJsonObject().has("paint")) {

            this.paint = data.get("stats").getAsJsonObject().get("paint").getAsInt();
        }

    }

    public SubWeapon getSubWeapon() {
        return subWeapon;
    }

    public SpecialWeapon getSpecialWeapon() {
        return specialWeapon;
    }

    public int getPaint() {
        return paint;
    }

    @Override
    public int compareTo(MainWeapon o) {
        return this.getName().compareTo(o.getName());
    }
}
