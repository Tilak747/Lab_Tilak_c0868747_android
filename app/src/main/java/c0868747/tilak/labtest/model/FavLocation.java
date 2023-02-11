package c0868747.tilak.labtest.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "fav_places")
public class FavLocation implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private double lat;
    private double lng;
    private String title;
    private String description;
    private long createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public FavLocation(int id, double lat, double lng, String title, String description, long createTime) {
        this.id = id;
        this.lat = lat;
        this.lng = lng;
        this.title = title;
        this.description = description;
        this.createTime = createTime;
    }
}
