package server;

import java.sql.*;
import java.util.HashMap;

public class SqliteService {
    private static Connection connection;
    private static Statement stmt;
    private static PreparedStatement psSelect;
    private static PreparedStatement psSelectNick;
    private static PreparedStatement psInsert;
    private static PreparedStatement psUpdate;
    private static PreparedStatement psSelectMsg;
    private static PreparedStatement psInsertMsg;

    public static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:server/bd.db");
        stmt = connection.createStatement();
        prepareStatements();
    }

    public static void disconnect() {
        try {
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void prepareStatements() throws SQLException {
        psSelect = connection.prepareStatement("SELECT u.id, u.name, u.login, u.password, g.privilege FROM users u LEFT JOIN groups g ON u.group_id = g.id " +
                "WHERE u.login = ? AND u.password = ? AND u.archive = false;");
        psSelectNick = connection.prepareStatement("SELECT name FROM users WHERE name = ? AND archive = false;");
        psInsert = connection.prepareStatement("INSERT INTO users(login,password,name) VALUES (?,?,?);");
        psUpdate = connection.prepareStatement("UPDATE users SET name = ? WHERE id = ?;");
        psInsertMsg = connection.prepareStatement("INSERT INTO message(user_id,receiving_id,text) VALUES (?,?,?);");
        psSelectMsg = connection.prepareStatement("SELECT ms.id, ms.text FROM message ms " +
                // "LEFT JOIN users u ON ms.receiving_id = u.id LEFT JOIN users u2 ON ms.user_id = u2.id " +
                "WHERE (ms.receiving_id IS NULL OR ms.receiving_id = ? OR ms.user_id = ?) AND ms.archive = false;");
    }

    public static boolean checkNickname(String nickname) throws SQLException {
        boolean checked = false;
        psSelectNick.setString(1, nickname);
        ResultSet rs = psSelectNick.executeQuery();
        while (rs.next()) {
            checked = true;
        }
        rs.close();
        return checked;
    }

    public static String[] getUser(String login, String password) throws SQLException {
        String[] result = new String[4];
        result[0] = "Not ok";

        psSelect.setString(1, login);
        psSelect.setString(2, password);
        ResultSet rs = psSelect.executeQuery();
        while (rs.next()) {
            result[0] = "ok";
            result[1] = rs.getString("name");
            result[2] = rs.getString("id");
            result[3] = rs.getString("privilege");
        }
        rs.close();
        return result;
    }

    public static void insUser(String login, String password, String nickname) throws SQLException {
        psInsert.setString(1, login);
        psInsert.setString(2, password);
        psInsert.setString(3, nickname);
        psInsert.executeUpdate();
    }

    public static void updNickname(int id, String nickname) throws SQLException {
        psUpdate.setInt(2, id);
        psUpdate.setString(1, nickname);
        psUpdate.executeUpdate();
    }

    public static void insMsg(int user_id, int receiving_id, String text) throws SQLException {
        psInsertMsg.setInt(1, user_id);
        if(receiving_id !=0) {
            psInsertMsg.setInt(2, receiving_id);
        } else {
            psInsertMsg.setNull(2,Types.INTEGER);
        }
        psInsertMsg.setString(3, text);
        psInsertMsg.executeUpdate();
    }

    public static HashMap<Integer,String> getMsg(int id) throws SQLException {
        HashMap<Integer, String> msg = new HashMap();
        psSelectMsg.setInt(1, id);
        psSelectMsg.setInt(2, id);
        ResultSet rs = psSelectMsg.executeQuery();
        while (rs.next()) {
            msg.put(rs.getInt("id"),rs.getString("text"));
        }
        rs.close();
        return msg;
    }

}
