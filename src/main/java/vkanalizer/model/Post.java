package vkanalizer.model;

import com.vk.api.sdk.objects.wall.WallpostFull;

public class Post {

    public static final String TABLE_NAME = "wallposts";
    public static final String ID_COLUMN = "id";
    public static final String TEXT_COLUMN = "text";
    public static final String LIKES_COLUMN = "likes";
    public static final String REPOSTS_COLUMN = "reposts";

    private Integer id;
    private String text;
    private Integer likes;
    private Integer reposts;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getReposts() {
        return reposts;
    }

    public void setReposts(Integer reposts) {
        this.reposts = reposts;
    }

    public Post(Integer id, String text, Integer likes, Integer reposts) {
        this.id = id;
        this.text = text;
        this.likes = likes;
        this.reposts = reposts;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Post that = (Post) obj;
        return likes.equals(that.likes) && reposts.equals(that.reposts);
    }

    @Override
    public String toString() {
        return String.format(
                "Post[id=%d, text='%s', likes=%d, reposts=%d]",
                id, text, likes, reposts);
    }

    public static Post wallpostToPost (WallpostFull wallpostFull) {
        Post post = new Post(
                wallpostFull.getId(),
                wallpostFull.getText(),
                wallpostFull.getLikes().getCount(),
                wallpostFull.getReposts().getCount()
        );
        return post;
    }
}
