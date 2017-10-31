package vkanalizer.model;

import java.util.List;
import java.util.Objects;

/**
 * Created by Nikolay V. Petrov on 27.10.2017.
 */
public class Like {

    private Integer id;
    private List<Integer> likesList;

    public Like(Integer id, List<Integer> likes) {
        this.id = id;
        this.likesList = likes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Integer> getLikes() {
        return likesList;
    }

    public void setLikes(List<Integer> likes) {
        this.likesList = likes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Like like = (Like) o;
        return Objects.equals(like.id, id) &&
                Objects.equals(like.likesList, likesList);
    }

    @Override
    public String toString() {
        return "Like{" +
                "id=" + id +
                ", likesList=" + likesList +
                '}';
    }
}
