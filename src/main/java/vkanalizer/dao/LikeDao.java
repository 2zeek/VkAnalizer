package vkanalizer.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import vkanalizer.model.Like;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by Nikolay V. Petrov on 27.10.2017.
 */

@Component
public class LikeDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Like findById(Integer id) {
        Like like = null;
        try {
            like = jdbcTemplate.queryForObject(
                    "SELECT id, likes_list FROM likes WHERE id = ?",
                    (rs, rowNum) -> new Like(
                            rs.getInt("id"),
                            stringToList(rs.getString("likes_list"))), id);
        } catch (EmptyResultDataAccessException ignored) {}
        return like;
    }

    public void insert(Like like) {
        jdbcTemplate.update("INSERT INTO likes (id, likes_list) VALUES (?, ?)",
                like.getId(), listToString(like.getLikes()));
    }

    public void update(Like like) {
        jdbcTemplate.update("UPDATE likes SET likes_list = ? WHERE id = ?",
                listToString(like.getLikes()), like.getId());
    }

    private List<Integer> stringToList(String string) {
        List<Integer> resultList = new ArrayList<>();
        Pattern pattern = Pattern.compile(",");
        if (!string.isEmpty())
            resultList =  pattern.splitAsStream(string)
                    .map(Integer::valueOf)
                    .collect(Collectors.toList());
        return resultList;
    }

    private String listToString(List<Integer> list) {
        return list.stream().map(Object::toString)
                .collect(Collectors.joining(","));
    }
}
