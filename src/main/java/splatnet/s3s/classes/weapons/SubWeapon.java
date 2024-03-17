package splatnet.s3s.classes.weapons;

import com.google.gson.JsonObject;

import java.util.TreeSet;

public class SubWeapon extends Weapon implements Comparable<SubWeapon> {

    private static TreeSet<SubWeapon> subWeapons = new TreeSet<>();

    public static SubWeapon getSubWeaponById(String id) {
        for (SubWeapon subWeapon : subWeapons) {
            if (subWeapon.getId().equals(id)) {
                return subWeapon;
            }
        }
        return null;
    }

    public static SubWeapon getSubWeaponByName(String name) {
        for (SubWeapon subWeapon : subWeapons) {
            if (subWeapon.getName().equals(name)) {
                return subWeapon;
            }
        }
        System.out.println("SubWeapon not found: " + name);
        return null;
    }

    public static void addSubWeapon(SubWeapon subWeapon) {
        subWeapons.add(subWeapon);
    }

    public SubWeapon() {}

    public SubWeapon(String id, String name, String type) {
        super(id, name, type);
    }

    public SubWeapon(JsonObject data) {
        super(data, "subs");
    }

    @Override
    public int compareTo(SubWeapon o) {
        return this.getName().compareTo(o.getName());
    }
}
