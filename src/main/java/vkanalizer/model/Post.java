package vkanalizer.model;

import java.util.List;
import java.util.Objects;

public class Post {

    private Integer id;
    private String text;
    private Integer likes;
    private Integer reposts;
    private List<Integer> likesList;
    private List<Integer> repostsList;

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

    public List<Integer> getLikesList() {
        return likesList;
    }

    public void setLikesList(List<Integer> likesList) {
        this.likesList = likesList;
    }

    public List<Integer> getRepostsList() {
        return repostsList;
    }

    public void setRepostsList(List<Integer> repostsList) {
        this.repostsList = repostsList;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", likes=" + likes +
                ", reposts=" + reposts +
                ", likesList=" + likesList +
                ", repostsList=" + repostsList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id) &&
                Objects.equals(text, post.text) &&
                Objects.equals(likes, post.likes) &&
                Objects.equals(reposts, post.reposts) &&
                Objects.equals(likesList, post.likesList) &&
                Objects.equals(repostsList, post.repostsList);
    }
}
