package Home.AttractionsRV_Frag;

//pojo
public class Attraction {

    String Id;
    String Address;
    String Category;
    String Image_URL;
    String Page_URL;
    String Rating;
    String Reviews;
    String Title;


    public Attraction(String Id, String Title, String Image_URL) {
        this.Id = Id;
        this.Title = Title;
        this.Image_URL = Image_URL;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getImage_URL() {
        return Image_URL;
    }

    public void setImage_URL(String image_URL) {
        Image_URL = image_URL;
    }

    public String getPage_URL() {
        return Page_URL;
    }

    public void setPage_URL(String page_URL) {
        Page_URL = page_URL;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getReviews() {
        return Reviews;
    }

    public void setReviews(String reviews) {
        Reviews = reviews;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getWeb_page() {
        return Web_page;
    }

    public void setWeb_page(String web_page) {
        Web_page = web_page;
    }

    String Web_page;



}
