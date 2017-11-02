package vkanalizer.model;

import java.util.List;
import java.util.Objects;

public class Repost {

    private Integer id;
    private List<Integer> repostsList;

    public Repost(Integer id, List<Integer> repostsList) {
        this.id = id;
        this.repostsList = repostsList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Integer> getReposts() {
        return repostsList;
    }

    public void setRepostsList(List<Integer> repostsList) {
        this.repostsList = repostsList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Repost repost = (Repost) o;
        return Objects.equals(repost.id, id) &&
                Objects.equals(repost.repostsList, repostsList);
    }

    @Override
    public String toString() {
        return "Repost{" +
                "id=" + id +
                ", repostsList=" + repostsList +
                '}';
    }
}
