package dao;

import model.Post;

import java.util.List;

/**
 * Created by Nikolay V. Petrov on 24.08.2017.
 */

public interface PostDao {

        public static final String SQL_FIND_ALL = "select * from " + Post.TABLE_NAME;
        public static final String SQL_FIND_BY_ID = SQL_FIND_ALL + " where " + Post.ID_COLUMN + " = ?";
        public static final String SQL_INSERT = "insert into " + Post.TABLE_NAME + " (" + Post.ID_COLUMN + ", " +
                Post.TEXT_COLUMN + ", " + Post.LIKES_COLUMN + ", " + Post.REPOSTS_COLUMN + ") values (?, ?, ?, ?)";
        public static final String SQL_UPDATE = "update " + Post.TABLE_NAME
                + " set " + Post.TEXT_COLUMN + " = ?"
                + " set " + Post.LIKES_COLUMN + " = ?"
                + " set " + Post.REPOSTS_COLUMN + " = ?"
                + " where " + Post.ID_COLUMN + " = ?";
        public static final String SQL_DELETE = "delete from " + Post.TABLE_NAME + " where " + Post.ID_COLUMN + " = ?";

        public List<Post> findAll();
        public Post findById(Integer id);
        public void insert(Post post);
        public void update(Post post);
        public void delete(Post post);
}
