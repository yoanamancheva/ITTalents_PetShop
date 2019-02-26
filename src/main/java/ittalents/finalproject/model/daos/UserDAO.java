package ittalents.finalproject.model.daos;

import ittalents.finalproject.model.DBManager;
import ittalents.finalproject.model.pojos.User;

import java.sql.*;



public class UserDAO {
    private static UserDAO ourInstance = new UserDAO();

    public static UserDAO getInstance() {
        return ourInstance;
    }


    public void addUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password, first_name, last_name, email, administrator) values (?,?,?,?,?,?)";

        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ps.setString(1, user.getUsername());
        ps.setString(2, user.getPassword());
        ps.setString(3, user.getFirstName());
        ps.setString(4, user.getLastName());
        ps.setString(5, user.getEmail());
        ps.setBoolean(6, false);
        ps.executeUpdate();

        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            user.setId(generatedKeys.getLong(1));
        }
    }


    public void deleteUser(String username) throws SQLException {
        String sql = "DELETE FROM users WHERE username = ?";

        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, username);
        ps.executeUpdate();
    }


    //not working
//    public User getUserByUsername(String username) throws SQLException{
//        String sql = "SELECT * FROM users WHERE username = ?";
//        Connection con = DBManager.getInstance().getConnection();
//
//        PreparedStatement ps = con.prepareStatement(sql);
//        ps.setString(1, username);
//        ResultSet resultSet =  ps.executeQuery();
//        User u = new User();
//        if(!resultSet.wasNull() && resultSet.next()) {
//            u.setId(resultSet.getLong("id"));
//            u.setUsername(resultSet.getString("username"));
//            u.setPassword(resultSet.getString("password"));
//            u.setFirstName(resultSet.getString("first_name"));
//            u.setLastName(resultSet.getString("last_name"));
//            u.setEmail(resultSet.getString("email"));
//            u.setAdmin(resultSet.getBoolean("administrator"));
//        }
//        return u;
//
//    }
}
