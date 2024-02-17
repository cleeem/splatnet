package splatnet.s3s.classes.misc;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.swing.plaf.basic.BasicDesktopIconUI;
import java.util.ArrayList;

public class NamePlate {

    private ArrayList<Badge> badges = new ArrayList<Badge>();

    private Banner banner;

    public NamePlate(ArrayList<Badge> badges, Banner banner) {
        this.badges = badges;
        this.banner = banner;
    }

    public NamePlate(JsonObject data) {
        JsonArray badges = data.getAsJsonArray("badges");
        for (JsonElement badge : badges) {
            if (!badge.isJsonNull()) {
                Badge bdg = Badge.findBadge(badge.getAsJsonObject().get("id").getAsString());
                if (bdg == null) {
                    this.badges.add(new Badge(badge.getAsJsonObject()));
                } else {
                    this.badges.add(bdg);
                }
            } else {
                this.badges.add(null);
            }
        }
        this.banner = new Banner(data.getAsJsonObject("background"));
    }


    public ArrayList<Badge> getBadges() {
        return badges;
    }

    public Banner getBanner() {
        return banner;
    }
}
