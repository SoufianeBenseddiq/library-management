package ma.ensa.config;

import java.sql.*;

public class DB_Connection {
    private Connection con;
    private Statement st;

    public DB_Connection() throws SQLException {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        con = DriverManager.getConnection("jdbc://mysql/localhost:3306/bibliotheque", "root", "");
        st = con.createStatement();
    }

    public Connection getCon() {
        return con;
    }

    public void setCon(Connection con) {
        this.con = con;
    }

    public Statement getSt() {
        return st;
    }

    public void setSt(Statement st) {
        this.st = st;
    }
}
