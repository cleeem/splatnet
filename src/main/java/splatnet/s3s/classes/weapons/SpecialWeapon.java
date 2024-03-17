package splatnet.s3s.classes.weapons;

import com.google.gson.JsonObject;

import java.util.TreeSet;

public class SpecialWeapon extends Weapon implements Comparable<SpecialWeapon>{

    private static TreeSet<SpecialWeapon> specialWeapons = new TreeSet<>();

    public static SpecialWeapon getSpecialWeaponById(String id) {
        for (SpecialWeapon specialWeapon : specialWeapons) {
            if (specialWeapon.getId().equals(id)) {
                return specialWeapon;
            }
        }
        return null;
    }

    public static SpecialWeapon getSpecialWeaponByName(String name) {
        for (SpecialWeapon specialWeapon : specialWeapons) {
            if (specialWeapon.getName().equals(name)) {
                return specialWeapon;
            }
        }
        System.out.println("SpecialWeapon not found: " + name);
        return null;
    }

    public static void addSpecialWeapon(SpecialWeapon specialWeapon) {
        specialWeapons.add(specialWeapon);
    }

    public SpecialWeapon() {}

    public SpecialWeapon(String id, String name, String type) {
        super(id, name, type);
    }

    public SpecialWeapon(JsonObject data) {
        super(data, "specials");
    }


    @Override
    public int compareTo(SpecialWeapon o) {
        return this.getName().compareTo(o.getName());
    }
}
