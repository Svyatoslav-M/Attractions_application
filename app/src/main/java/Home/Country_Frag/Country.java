package Home.Country_Frag;

public class Country {
    String Country;
    String CountryImageURL;

    public Country(String Сountry, String CountryImageURL) {
        this.Country = Сountry;
        this.CountryImageURL = CountryImageURL;
    }
    public String getCountry() {
        return Country;
    }
    public String getCountryImageURL() {
        return CountryImageURL;
    }
}
