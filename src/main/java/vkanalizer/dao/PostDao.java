package vkanalizer.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import vkanalizer.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by Nikolay V. Petrov on 24.08.2017.
 */

@Component
public class PostDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Post findById(Integer id) {
        Post post = null;
        try {
            post = jdbcTemplate.queryForObject(
                    "SELECT id, text, likes, reposts,comments FROM posts where id = ?",
                    (rs, rowNum) -> {
                        final Post post1 = new Post(
                                rs.getInt("id"),
                                rs.getString("text"),
                                rs.getInt("likes"),
                                rs.getInt("reposts"),
                                rs.getInt("comments"));
                        return post1;
                    }, id);
        } catch (EmptyResultDataAccessException ignored) {}
        return post;
    }

    public void insert(Post post) {
        jdbcTemplate.update("insert into posts (id, text, likes, reposts, comments) values (?, ?, ?, ?)",
                post.getId(), post.getText(), post.getLikes(), post.getReposts(), post.getComments());
    }

    public void update(Post post) {
        jdbcTemplate.update("update posts set text = ? , likes = ? , reposts = ?, comments = ? where id = ?",
                post.getText(), post.getLikes(), post.getReposts(), post.getComments(), post.getId());
    }

}