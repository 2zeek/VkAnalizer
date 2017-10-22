package vkanalizer.dao;

import org.apache.http.cookie.SM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import vkanalizer.model.SmallUser;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Nikolay V. Petrov on 22.10.2017.
 */
public class SmallUserDao {

    private static Logger log = LoggerFactory.getLogger(SmallUserDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public SmallUser findById(Integer id) {
        log.info("Fetch user with id = " + id);
        SmallUser user = null;
        try {
            user = jdbcTemplate.queryForObject(
                    "SELECT id, first_name, last_name FROM users where id = ?",
                    (rs, rowNum) -> {
                        SmallUser user1 = new SmallUser();
                        user1.setId(rs.getInt("id"));
                        user1.setFirstName(rs.getString("first_name"));
                        user1.setLastName(rs.getString("last_name"));
                        return user1;
                    }, id);
        } catch (EmptyResultDataAccessException ignored) {}
        return user;
    }

    public List<SmallUser> findByIds(List<Integer> ids) {
        log.info("Fetch users with ids = " + ids);
        List<SmallUser> list = null;
        try {
            list = jdbcTemplate.queryForObject(
                    "SELECT id, first_name, last_name FROM users where id in (?)",
                    (rs, rowNum) -> {
                        List<SmallUser> list1 = new ArrayList<>();
                        while (rs.next()) {
                            SmallUser user1 = new SmallUser();
                            user1.setId(rs.getInt("id"));
                            user1.setFirstName(rs.getString("first_name"));
                            user1.setLastName(rs.getString("last_name"));
                            list1.add(user1);
                        }
                        return list1;
                    }, createSqlArray(ids));
        } catch (EmptyResultDataAccessException ignored) {}
        return list;
    }

    public void insert(SmallUser smallUser) {
        jdbcTemplate.update("insert into users (id, first_name, last_name) values (?, ?, ?)",
                smallUser.getId(), smallUser.getFirstName(), smallUser.getLastName());
    }

    public void update(SmallUser smallUser) {
        jdbcTemplate.update("update users set first_name = ? , last_name = ? where id = ?",
               smallUser.getFirstName(), smallUser.getLastName(), smallUser.getId());
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
