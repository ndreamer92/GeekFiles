package Server;

import zGBFCommon.Encryptor;

import java.sql.*;
import java.util.ArrayList;

public class BaseAuthService implements AuthService{
    private class Entry {
        private String login;
        private String pass;
        private String nick;
        public Entry(String login, String pass, String nick) {
            this.login = login;
            this.pass = pass;
            this.nick = nick;
        }
    }

    private Connection connection;
    private Encryptor encryptor;

    private ArrayList<Entry> entries;
    @Override
    public void start() { }
    @Override
    public void stop() { }
    public BaseAuthService() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:main.db");
            //createStructure(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        entries = new ArrayList<>();
    }
    @Override
    public String authorizeByLogPass(String login, String pass) {
        try {
            Statement req = connection.createStatement();
            ResultSet rs = req.executeQuery("SELECT * FROM users WHERE login = " + "\'" + login + "\' AND password = \'" + pass + "\'" + ";" );
            String ts = rs.getString("hash");
            req.close();
            return ts;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean regAttempt(String login, String pass){
        Boolean iReq=false;
        try {
            Statement req = connection.createStatement();
            iReq = req.execute("INSERT INTO users (login,password,hash) VALUES (\"" + login + "\",\"" + pass + "\",\"" + login+pass +  "\");");
            System.out.println("new user created!");
            req.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return iReq;
    }

    public void createStructure(Connection conn){
        try {
            Statement stmt = conn.createStatement();
            stmt.execute("DROP TABLE users");

            stmt.execute("CREATE TABLE IF NOT EXISTS users (uid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "login TEXT NOT NULL," +
                    "password TEXT NOT NULL," +
                    "hash TEXT NOT NULL)");
            //System.out.println("INSERT INTO users (login,password,hash) VALUES (\"" + Encryptor.encrypt("login1") + "\",\"" + Encryptor.encrypt("pass1") + "\",\"" + Encryptor.encrypt("login1pass1") +  "\");");
            stmt.execute("INSERT INTO users (login,password,hash) VALUES (\"" + Encryptor.encrypt("login1") + "\",\"" + Encryptor.encrypt("pass1") + "\",\"" + Encryptor.encrypt("login1pass1") +  "\");");
            stmt.execute("INSERT INTO users (login,password,hash) VALUES (\"" + Encryptor.encrypt("login2") + "\",\"" + Encryptor.encrypt("pass2") + "\",\"" + Encryptor.encrypt("login2pass2") +  "\");");
            stmt.execute("INSERT INTO users (login,password,hash) VALUES (\"" + Encryptor.encrypt("login3") + "\",\"" + Encryptor.encrypt("pass3") + "\",\"" + Encryptor.encrypt("login3pass3") +  "\");");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
