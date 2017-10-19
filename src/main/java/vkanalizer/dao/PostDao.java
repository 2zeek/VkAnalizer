package vkanalizer.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import vkanalizer.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


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
                    "SELECT id, text, likes, reposts FROM posts where id = ?",
                    (rs, rowNum) -> {
                        final Post post1 = new Post(
                                rs.getInt("id"),
                                rs.getString("text"),
                                rs.getInt("likes_count"),
                                rs.getInt("reposts"));
                        return post1;
                    }, id);
        } catch (EmptyResultDataAccessException ignored) {}
        return post;
    }

    public void insert(Post post) {
        jdbcTemplate.update("insert into posts (id, text, likes, reposts) values (?, ?, ?, ?)",
                post.getId(), post.getText(), post.getLikes(), post.getReposts());
    }

    public void update(Post post) {
        jdbcTemplate.update("update posts set text = ? , likes = ? , reposts = ? where id = ?",
                post.getText(), post.getLikes(), post.getReposts(), post.getId());
    }

}