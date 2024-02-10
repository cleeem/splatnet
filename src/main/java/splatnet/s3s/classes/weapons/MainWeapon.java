package splatnet.s3s.classes.weapons;

import com.google.gson.JsonObject;

public class MainWeapon extends Weapon {

    private SubWeapon subWeapon;

    private SpecialWeapon specialWeapon;

    private int paint = 0;

    public MainWeapon() {}

    public MainWeapon(String id, String name, String type, SubWeapon subWeapon, SpecialWeapon specialWeapon) {
        super(id, name, type);
        this.subWeapon = subWeapon;
        this.specialWeapon = specialWeapon;
    }

    public MainWeapon(JsonObject data) {
        super(data, "weapons");
        this.subWeapon = new SubWeapon(data.get("subWeapon").getAsJsonObject());
        this.specialWeapon = new SpecialWeapon(data.get("specialWeapon").getAsJsonObject());

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
}
