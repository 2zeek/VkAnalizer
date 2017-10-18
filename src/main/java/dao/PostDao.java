package dao;

import model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * Created by Nikolay V. Petrov on 24.08.2017.
 */

@Component
public class PostDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Post> findAll() {
        List<Post> result = jdbcTemplate.query(
                "SELECT id, text, likes, reposts FROM posts",
                (rs, rowNum) -> new Post(rs.getInt("id"),
                        rs.getString("text"), rs.getInt("likes"), rs.getInt("reposts"))
        );
        return result;
    }

    public Post findById(Integer id) {
        Post post = jdbcTemplate.queryForObject(
                "SELECT id, text, likes, reposts FROM posts where id = " + id, Post.class);
        return post;
    }

    public void insert(Post post) {
        jdbcTemplate.update("insert into posts (id, text, likes, reposts) values (?, ?, ?, ?)",
                post.getId(), post.getText(), post.getLikes(), post.getReposts());
    }

    public void update(Post post) {
        jdbcTemplate.update("update posts set text = ? set likes = ? set reposts = ? where id = ?",
               post.getText(), post.getLikes(), post.getReposts(),  post.getId());
    }

}