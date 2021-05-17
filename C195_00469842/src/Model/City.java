package Model;

public class City {
    private int cityID;
    private String city;

    //Constructors
    public City() {
        cityID = 0;
        city = null;
    }

    public City(int cityID, String city, int countryID) {
        this.cityID = cityID;
        this.city = city;
    }

    //Getters and Setters
    public int getCityID() {
        return cityID;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
