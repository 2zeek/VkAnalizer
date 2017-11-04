package vkanalizer.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import vkanalizer.model.Repost;

import static vkanalizer.utils.Utils.listToString;
import static vkanalizer.utils.Utils.stringToList;

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
                            stringToList(rs.getString("reposts_list"))), id);
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

}
