package db;

import java.sql.*;

public class ConnectionClass {

    static final String URL = "jdbc:mysql://localhost:3308/jampee_api?useSSL=false";
    static final String USERNAME = "root";
    static final String PASSWORD = "";

    public static void main(String[] args) {

        try (
                Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                Statement statement = connection.createStatement();
        ) {

            String sql = "select * from org_custom_user_fields";
            ResultSet result = statement.executeQuery(sql);
            while(result.next()){
                System.out.println(result.getString("custom_user_fields"));
            }
        } catch (SQLException e) {

            e.printStackTrace();

        }

    }

}
