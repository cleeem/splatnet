package splatnet.s3s.classes.weapons;

import com.google.gson.JsonObject;

public class SpecialWeapon extends Weapon {

    public SpecialWeapon() {}

    public SpecialWeapon(String id, String name, String type) {
        super(id, name, type);
    }

    public SpecialWeapon(JsonObject data) {
        super(data, "specials");
    }


}
