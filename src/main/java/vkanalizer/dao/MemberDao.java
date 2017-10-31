package vkanalizer.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import vkanalizer.model.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class MemberDao {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Member findById(Integer id) {
        Member member = null;
        try {
            member = jdbcTemplate.queryForObject(
                    "SELECT id, first_name, last_name FROM members where id = ?",
                    (rs, rowNum) -> new Member(
                            rs.getInt("id"),
                            rs.getString("first_name"),
                            rs.getString("last_name")), 
                    id);
        } catch (EmptyResultDataAccessException ignored) {}
        return member;
    }

    public List<Member> getAllMembers() {
        List<Member> list = new ArrayList<>();
        try {
            List<Map<String, Object>> response = jdbcTemplate.queryForList("SELECT id, first_name, last_name FROM members");
            for (Map map : response) {
                list.add(new Member(
                        (Integer) map.get("id"),
                        (String) map.get("first_name"),
                        (String) map.get("last_name"))
                );
            }
        } catch (EmptyResultDataAccessException ignored) {}
        return list;
    }

    public void insert(Member member) {
        jdbcTemplate.update("INSERT INTO members (id, first_name, last_name) VALUES (?, ?, ?)",
                member.getId(), member.getFirstName(), member.getLastName());
    }

    public void update(Member member) {
        jdbcTemplate.update("UPDATE members SET first_name = ?, last_name = ? WHERE id = ?",
                member.getFirstName(), member.getLastName(), member.getId());
    }

    public void delete(Integer id) {
        jdbcTemplate.update("DELETE FROM members WHERE id = ?", id);
    }

}
