package ma.ensa.dao.impl;

import ma.ensa.config.DB_Connection;
import ma.ensa.dao.facade.DAO;
import ma.ensa.model.Livre;
import ma.ensa.model.Result;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LivreDao implements DAO<Livre> {
    @Override
    public Result<Livre> create(Livre livre) {
        Result<Livre> result = new Result<>();
        String sql = "INSERT INTO livres (titre, auteur, isbn, anneePublication, disponible) VALUES (?,?,?,?, ?)";
        try {
            DB_Connection con = new DB_Connection();
            PreparedStatement pst = con.getCon().prepareStatement(sql);
            pst.setString(1, livre.getTitre());
            pst.setString(2, livre.getAuteur());
            pst.setString(3, livre.getIsbn());
            pst.setInt(4, livre.getAnneePublication());
            pst.setBoolean(5, livre.isDisponible());
            pst.executeUpdate();
            ResultSet generatedKeys = pst.getGeneratedKeys();
            if (generatedKeys.next()) {
                livre.setId(generatedKeys.getInt(1));
            }
            pst.close();
            result.setData(livre);
            result.setSuccess(true);
            result.setMessage("Livre created");
        }catch (SQLException e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            result.setData(null);
        }
        return result;
    }

    @Override
    public Result<Livre> findById(int id) {
        String sql = "SELECT * FROM livres WHERE id = ?";
        Result<Livre> result = new Result<>();
        try {
            DB_Connection con = new DB_Connection();
            PreparedStatement pst = con.getCon().prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Livre livre = new Livre(rs.getInt(1), rs.getString(2), rs.getString(3),rs.getString(4), rs.getInt(4),rs.getBoolean(5));
                result.setData(livre);
                result.setSuccess(true);
                result.setMessage("Livre found");
            }
            pst.close();
            rs.close();
        }catch (SQLException e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            result.setData(null);
        }
        return result;
    }

    @Override
    public Result<List<Livre>> findAll() {
        String sql = "SELECT * FROM livres";
        Result<List<Livre>> result = new Result<>();
        try {
            ArrayList<Livre> all = new ArrayList<>();
            DB_Connection con = new DB_Connection();
            PreparedStatement stmt = con.getCon().prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                Livre livre = new Livre(rs.getInt(1), rs.getString(2), rs.getString(3),rs.getString(4), rs.getInt(4),rs.getBoolean(5));
                all.add(livre);
            }
            result.setData(all);
            result.setSuccess(true);
            result.setMessage("List of livres found");
            stmt.close();
        }catch (SQLException e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            result.setData(null);
        }
        return result;
    }

    @Override
    public Result<Boolean> delete(int id) {
        String sql = "DELETE FROM livres WHERE id = ?";
        Result<Boolean> result = new Result<>();
        try {
            DB_Connection con = new DB_Connection();
            PreparedStatement stmt = con.getCon().prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if(resultSet.next()){
                result.setSuccess(true);
                result.setMessage("Livre deleted");
                result.setData(null);
            }
            stmt.close();
        }catch (SQLException e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            result.setData(null);
        }
        return result;
    }

    @Override
    public Result<Livre> update(Livre livre) {
        String sql = "UPDATE livres SET titre = ?,auteur = ?,isbn = ?,anneePublication = ?,disponible = ? WHERE id = ? ";
        Result<Livre> result = new Result<>();
        try {
            DB_Connection con = new DB_Connection();
            PreparedStatement stmt = con.getCon().prepareStatement(sql);
            stmt.setString(1, livre.getTitre());
            stmt.setString(2, livre.getAuteur());
            stmt.setString(3, livre.getIsbn());
            stmt.setInt(4, livre.getAnneePublication());
            stmt.setBoolean(5, livre.isDisponible());
            stmt.setInt(6, livre.getId());
            stmt.executeUpdate();
            if (stmt.getGeneratedKeys().next()) {
                livre.setId(stmt.getGeneratedKeys().getInt(1));
            }
            stmt.close();
        }catch (SQLException e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            result.setData(null);
        }
        return  result;
    }

    public Result<List<Livre>> findByAuteur(String auteur ) {
        String sql = "SELECT * FROM livres WHERE auteur = ?";
        Result<List<Livre>> result = new Result<>();
        try{
            DB_Connection con = new DB_Connection();
            PreparedStatement stmt = con.getCon().prepareStatement(sql);
            stmt.setString(1, auteur);
            ResultSet rs = stmt.executeQuery();
            ArrayList<Livre> all = new ArrayList<>();
            while (rs.next()) {
                Livre livre = new Livre(rs.getInt(1), rs.getString(2), rs.getString(3),rs.getString(4), rs.getInt(4),rs.getBoolean(5));
                all.add(livre);
            }
            result.setData(all);
            result.setSuccess(true);
            result.setMessage("Livres for " + auteur + " found");
        }catch (SQLException e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            result.setData(null);
        }
        return result;
    }

    public Result<List<Livre>> findDisponibles() {
        String sql = "SELECT * FROM livres WHERE disponible = ?";
        Result<List<Livre>> result = new Result<>();
        try{
            DB_Connection con = new DB_Connection();
            PreparedStatement stmt = con.getCon().prepareStatement(sql);
            stmt.setBoolean(1, true);
            ResultSet rs = stmt.executeQuery();
            ArrayList<Livre> all = new ArrayList<>();
            while (rs.next()) {
                Livre livre = new Livre(rs.getInt(1), rs.getString(2), rs.getString(3),rs.getString(4), rs.getInt(4),rs.getBoolean(5));
                all.add(livre);
            }
            result.setData(all);
            result.setSuccess(true);
            result.setMessage("Disponible livres found succesfully");
        }catch (SQLException e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            result.setData(null);
        }
        return result;
    }
}
