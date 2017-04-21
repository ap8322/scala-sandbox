package db;

import java.sql.*;

public class ConnectionClass {

    //static final String URL = "jdbc:mysql://localhost:3308/jampee_api?useSSL=false";
    static final String URL = "jdbc:mysql://localhost:3308/jampee_api";
    static final String USERNAME = "root";
    static final String PASSWORD = "";

    public static void main(String[] args) {
        // String sql = "select * from org_custom_employee_fields";
        String sql = "select * from BILLABLE_ACCOUNT_NUM_HSTR";
        String target = "created_at";

        try (
                Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                Statement statement = connection.createStatement();
        ) {

            ResultSet result = statement.executeQuery(sql);
            while(result.next()){
                System.out.println(result.getString(target));
            }
        } catch (SQLException e) {

            e.printStackTrace();

        }

    }

}
