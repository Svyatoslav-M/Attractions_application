package Home.Comments_Frag;

//pojo
public class Comments {
    private String userId;
    private String userName;
    private String comment;
    private long timestamp;
    public Comments() {

    }

    public Comments(String userId, String userName, String comment, long timestamp) {
        this.userId = userId;
        this.userName = userName;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
