package dao.impl;

import dao.PostDao;
import model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Nikolay V. Petrov on 24.08.2017.
 */

@Component
public class PostDaoImpl implements PostDao {

    private DataSource dataSource;

    @Autowired
    public PostDaoImpl(@Qualifier("dataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Post> findAll() {
        List<Post> result = new ArrayList<Post>();
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Post post = new Post();
                post.setId(rs.getInt(Post.ID_COLUMN));
                post.setText(rs.getString(Post.TEXT_COLUMN));
                post.setLikes(rs.getInt(Post.LIKES_COLUMN));
                post.setReposts(rs.getInt(Post.REPOSTS_COLUMN));
                result.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public Post findById(Integer id) {
        Post post = null;
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID);
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                post = new Post();
                post.setId(rs.getInt(Post.ID_COLUMN));
                post.setText(rs.getString(Post.TEXT_COLUMN));
                post.setLikes(rs.getInt(Post.LIKES_COLUMN));
                post.setReposts(rs.getInt(Post.REPOSTS_COLUMN));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return post;
    }

    public void insert(Post post) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, post.getId());
            statement.setString(2, post.getText());
            statement.setLong(3, post.getLikes());
            statement.setLong(4, post.getReposts());
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void update(Post post) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE);
            statement.setString(1, post.getText());
            statement.setLong(2, post.getLikes());
            statement.setLong(3, post.getReposts());
            statement.setLong(4, post.getId());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void delete(Post post) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_DELETE);
            statement.setLong(1, post.getId());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}