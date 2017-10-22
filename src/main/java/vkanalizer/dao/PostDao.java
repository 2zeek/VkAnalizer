package vkanalizer.dao;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import vkanalizer.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Array;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Nikolay V. Petrov on 24.08.2017.
 */

@Component
public class PostDao {

    private static Logger log = LoggerFactory.getLogger(PostDao.class);


    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Post findById(Integer id) {
        log.info("Fetch post with id = " + id);
        Post post = null;
        try {
            post = jdbcTemplate.queryForObject(
                    "SELECT id, text, likes, reposts, likes_list, reposts_list FROM posts where id = ?",
                    (rs, rowNum) -> {
                        Post post1 = new Post();
                        post1.setId(rs.getInt("id"));
                        post1.setText(rs.getString("text"));
                        post1.setLikes(rs.getInt("likes"));
                        post1.setReposts(rs.getInt("reposts"));

                        Array likesArray = rs.getArray("likes_list");
                        Array repostsArray = rs.getArray("reposts_list");
                        List<Integer> likesss = Lists.newArrayList((Integer[])likesArray.getArray());
                        List<Integer> repostsss = Lists.newArrayList((Integer[])repostsArray.getArray());
                        post1.setLikesList(likesss);
                        post1.setRepostsList(repostsss);
                        return post1;
                    }, id);
        } catch (EmptyResultDataAccessException ignored) {}
        return post;
    }

    public void insert(Post post) {
        jdbcTemplate.update("insert into posts (id, text, likes, reposts, likes_list, reposts_list) values (?, ?, ?, ?, ?, ?)",
                post.getId(), post.getText(), post.getLikes(), post.getReposts(), createSqlArray(post.getLikesList()), createSqlArray(post.getRepostsList()));
    }

    public void update(Post post) {
        jdbcTemplate.update("update posts set text = ? , likes = ? , reposts = ?, likes_list = ?, reposts_list = ? where id = ?",
                post.getText(), post.getLikes(), post.getReposts(), createSqlArray(post.getLikesList()),createSqlArray(post.getRepostsList()), post.getId());
    }


    private java.sql.Array createSqlArray(List<Integer> list){
        java.sql.Array intArray = null;
        try {
            intArray = jdbcTemplate.getDataSource().getConnection().createArrayOf("int", list.toArray());
        } catch (SQLException ignore) {
        }
        return intArray;
    }
}