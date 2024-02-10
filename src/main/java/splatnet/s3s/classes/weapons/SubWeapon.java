package splatnet.s3s.classes.weapons;

import com.google.gson.JsonObject;

public class SubWeapon extends Weapon {

    public SubWeapon() {}

    public SubWeapon(String id, String name, String type) {
        super(id, name, type);
    }

    public SubWeapon(JsonObject data) {
        super(data, "subs");
    }

}
