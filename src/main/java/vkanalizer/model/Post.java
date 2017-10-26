package vkanalizer.model;

import com.vk.api.sdk.objects.wall.WallpostFull;

import java.util.Objects;

public class Post {

    private Integer id;
    private String text;
    private Integer likes;
    private Integer reposts;
    private Integer comments;

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

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public Post(Integer id, String text, Integer likes, Integer reposts, Integer comments) {
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
        return Objects.equals(this.likes, that.likes) &&
                Objects.equals(this.reposts, that.reposts) &&
                Objects.equals(this.comments, that.comments);
    }

    @Override
    public String toString() {
        return String.format(
                "Post[id=%d, text='%s', likes=%d, reposts=%d, comments=%d]",
                id, text, likes, reposts, comments);
    }

    public static Post wallpostToPost (WallpostFull wallpostFull) {
        Post post = new Post(
                wallpostFull.getId(),
                wallpostFull.getText(),
                wallpostFull.getLikes().getCount(),
                wallpostFull.getReposts().getCount(),
                wallpostFull.getComments().getCount()
        );
        return post;
    }
}
