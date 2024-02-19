package splatnet.models.schedule;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import splatnet.s3s.classes.game.Stage;

import java.util.ArrayList;

public class Rotation {

    private String startTime;

    private String endTime;

    private ArrayList<String> mode;

    private String type;

    private ArrayList<Stage> stages;

    public Rotation(String startTime, String endTime, ArrayList mode, String type, ArrayList<Stage> stages) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.mode = mode;
        this.type = type;
        this.stages = stages;
    }

    public Rotation(JsonObject data, String typeKey) {
        if (!typeKey.equals("bankaraMatchSettings")) {
            getNormalRotation(data, typeKey);
        } else {
            getBankaraRotation(data, typeKey);
        }
    }

    private void getNormalRotation(JsonObject data, String typeKey) {
        this.startTime = data.get("startTime").getAsString();
        this.endTime = data.get("endTime").getAsString();
        this.mode = new ArrayList<>();
        this.mode.add(
                data.get(typeKey).getAsJsonObject()
                .getAsJsonObject("vsRule")
                .get("name").getAsString()
        );

        this.type = data.get(typeKey).getAsJsonObject()
                .get("__typename").getAsString();

        this.stages = new ArrayList<>();
        for (JsonElement stage : data.get(typeKey).getAsJsonObject().getAsJsonArray("vsStages")) {
            Stage s = Stage.findStage(stage.getAsJsonObject().get("id").getAsString());
            if (s != null) {
                this.stages.add(s);
            } else {
                s = new Stage(stage.getAsJsonObject());
                this.stages.add(s);
                Stage.addStage(s);
            }
        }
    }

    private void getBankaraRotation(JsonObject data, String typeKey) {
        this.startTime = data.get("startTime").getAsString();
        this.endTime = data.get("endTime").getAsString();

        JsonArray bankaraMatchSettings = data.get(typeKey).getAsJsonArray();
        this.mode = new ArrayList<>();
        this.stages = new ArrayList<>();
        for (JsonElement element : bankaraMatchSettings) {
            this.mode.add(
                    element.getAsJsonObject()
                            .getAsJsonObject("vsRule")
                            .get("name").getAsString()
            );

            for (JsonElement stage : element.getAsJsonObject().getAsJsonArray("vsStages")) {
                Stage s = Stage.findStage(stage.getAsJsonObject().get("id").getAsString());
                if (s != null) {
                    this.stages.add(s);
                } else {
                    s = new Stage(stage.getAsJsonObject());
                    this.stages.add(s);
                    Stage.addStage(s);
                }
            }
        }

    }


    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public ArrayList<String> getMode() {
        return mode;
    }

    public String getType() {
        return type;
    }

    public ArrayList<Stage> getStages() {
        return stages;
    }

    @Override
    public String toString() {
        return "Rotation{" + "\n" +
                "\tstartTime='" + startTime + '\'' + ",\n" +
                "\tendTime='" + endTime + '\'' + ",\n" +
                "\tmode=" + mode + ",\n" +
                "\ttype='" + type + '\'' + ",\n" +
                "\tstages=" + stages + ",\n" +
                '}';
    }

}
