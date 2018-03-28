package de.dominikwieners.working.data.local;

/**
 * Created by dominikwieners on 28.03.18.
 */

public class SettingsType {

    private int id;

    private int drawableId;

    private String settingsDescription;

    public SettingsType(int id, int drawableId, String settingsDescription) {
        this.id = id;
        this.drawableId = drawableId;
        this.settingsDescription = settingsDescription;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public String getSettingsDescription() {
        return settingsDescription;
    }

    public void setSettingsDescription(String settingsDescription) {
        this.settingsDescription = settingsDescription;
    }
}
