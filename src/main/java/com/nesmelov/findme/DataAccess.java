package com.nesmelov.findme;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONArray;

public class DataAccess {  
    private static final String HOST = "mysql://mysql";
    private static final String PORT = "3306";
    private static final String USERNAME = "userXFS";
    private static final String PASSWORD = "1N5INCYl05wdUpxA";
    private static final String DATABASE = "findmedb";
    private static final String URL = String.format("jdbc:mysql://%s:%s/%s", HOST, PORT, DATABASE);
    private static final DataAccess instance_ = new DataAccess();
    private final StringBuilder mErrorMessage = new StringBuilder();
    
    public static DataAccess getInstance() {
        return instance_;
    }
    
    public String getErrorMessage() {
        return mErrorMessage.toString();
    }
    
    public boolean init() {
        String query = "CREATE TABLE IF NOT EXISTS User (VkId INT, Lat DOUBLE, Lon DOUBLE, Visible BOOLEAN);";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = null;
            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                final Statement statement = connection.createStatement();
                statement.executeQuery(query);              
                return true;
            } catch (SQLException e) {
                mErrorMessage.append("\n").append(e.toString());
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException ex) {
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            mErrorMessage.append("\n").append(e.toString());
        }
        return false;
    }
    
    public boolean addUser(final Integer VkId) {
        String query = "select count(Id) from User WHERE VkId='" + VkId + "';";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = null;
            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                final Statement statement = connection.createStatement();
                final ResultSet resultSet = statement.executeQuery(query);
                resultSet.next();
                if (resultSet.getInt(1) == 0) {
                    query = "INSERT INTO User (VkId)" + " VALUES ('" + VkId + "');";
                    statement.executeUpdate(query);
                    return true;
                }
            } catch (SQLException e) {
                mErrorMessage.append("\n").append(e.toString());
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException ex) {
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            mErrorMessage.append("\n").append(e.toString());
        }
        return false;
    }
    
    public JSONArray checkUsers(final JSONArray vkIds) {
        final JSONArray existsUsers = new JSONArray();
        if (vkIds.length() > 0) {
            final StringBuilder query = new StringBuilder("select VkId from User WHERE ");
            StringBuilder condition = new StringBuilder();
            for (int i = 0; i < vkIds.length(); i++) {
                final Integer vkId = vkIds.getInt(i);
                condition.append("VkId='").append(vkId).append("' OR ");
            }
            condition = condition.delete(condition.length() - 4, condition.length());
            query.append(condition).append(";");
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = null;
                try {
                    connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                    final Statement statement = connection.createStatement();
                    final ResultSet resultSet = statement.executeQuery(query.toString());
                    while (resultSet.next()) {
                        existsUsers.put(resultSet.getInt(1));
                    }
                } catch (SQLException e) {
                    mErrorMessage.append("\n").append(e.toString());
                } finally {
                    if (connection != null) {
                        try {
                            connection.close();
                        } catch (SQLException ex) {
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                mErrorMessage.append("\n").append(e.toString());
            }
        }
        return existsUsers;
    }
    
    public Map<Integer, Position> getAllRecords() {
        final Map<Integer, Position> result = new ConcurrentHashMap<Integer, Position>();
        final String query = "select VkId, Lat, Lon from User WHERE Visible=true;";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = null;
            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                final Statement statement = connection.createStatement();
                final ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    result.put(resultSet.getInt(1), new Position(resultSet.getDouble(2), resultSet.getDouble(3)));
                }
            } catch (SQLException e) {
                mErrorMessage.append("\n").append(e.toString());
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException ex) {
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            mErrorMessage.append("\n").append(e.toString());
        }
        return result;
    }
    
    public void refreshRecords(final Map<Integer, Position> input) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = null;
            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                for (final Integer key : input.keySet()) {
                    final Position pos = input.get(key);
                    final String query = "update User set Lat=" + pos.getLat() +  
                        ", Lon=" + pos.getLon() + ", Visible=true WHERE VkId='" + key + "';";
                    final Statement statement = connection.createStatement();
                    statement.executeUpdate(query);
                }
            } catch (SQLException e) {
                mErrorMessage.append("\n").append(e.toString());
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException ex) {
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            mErrorMessage.append("\n").append(e.toString());
        }
    }
    
    private DataAccess() { }
}
