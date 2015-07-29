package app.operatorclient.xtxt.Requestmanager;

/**
 * Created by kiran on 29/7/15.
 */
public class Persona {

    String distance, id, longitude, profile_pic_100, latitude, name;

    public Persona(String distance, String id, String longitude, String profile_pic_100, String latitude, String name) {
        this.distance = distance;
        this.id = id;
        this.longitude = longitude;
        this.profile_pic_100 = profile_pic_100;
        this.latitude = latitude;
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getProfile_pic_100() {
        return profile_pic_100;
    }

    public void setProfile_pic_100(String profile_pic_100) {
        this.profile_pic_100 = profile_pic_100;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
