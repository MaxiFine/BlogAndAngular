package learn.blogfiles.blog.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCConfig {

    public static void main(String[] args) {
        String sql = "SELECT id, username FROM users WHERE id = ?";
//        String url = "jdbc:sqlite:path-to-db-file/chinook/chinook.db";
        String url = "jdbc:postgresql://host.docker.internal:5432/blogs";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            try {
                ResultSet rs = stmt.executeQuery("select * from albums");
                while (rs.next()) {
                    String name = rs.getString("title");
                    System.out.println(name);
                }
            } catch (SQLException e ) {
                throw new Error("Problem", e);
            }
        } catch (SQLException e) {
            throw new Error("Problem", e);
        }
    }

}
