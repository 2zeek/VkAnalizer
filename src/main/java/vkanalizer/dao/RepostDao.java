package vkanalizer.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import vkanalizer.model.Repost;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class RepostDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RepostDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Repost findById(Integer id) {
        Repost repost = null;
        try {
            repost = jdbcTemplate.queryForObject(
                    "SELECT id, reposts_list FROM reposts WHERE id = ?",
                    (rs, rowNum) -> new Repost(
                            rs.getInt("id"),
                            stringToList(rs.getString("repost_list"))), id);
        } catch (EmptyResultDataAccessException ignored) {}
        return repost;
    }

    public void insert(Repost repost) {
        jdbcTemplate.update("INSERT INTO reposts (id, reposts_list) VALUES (?, ?)",
                repost.getId(), listToString(repost.getReposts()));
    }

    public void update(Repost repost) {
        jdbcTemplate.update("UPDATE reposts SET reposts_list = ? WHERE id = ?",
                listToString(repost.getReposts()), repost.getId());
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
