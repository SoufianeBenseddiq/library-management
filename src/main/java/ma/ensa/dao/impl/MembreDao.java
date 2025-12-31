package ma.ensa.dao.impl;

import ma.ensa.config.DB_Connection;
import ma.ensa.dao.facade.DAO;
import ma.ensa.model.Livre;
import ma.ensa.model.Membre;
import ma.ensa.model.Result;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MembreDao implements DAO<Membre> {
    @Override
    public Result<Membre> create(Membre membre) {
        Result<Membre> result = new Result<>();
        String sql = "INSERT INTO membres (nom, prenom, email, dateInscription) VALUES (?,?,?,?)";
        try {
            DB_Connection con = new DB_Connection();
            PreparedStatement pst = con.getCon().prepareStatement(sql);
            pst.setString(1, membre.getNom());
            pst.setString(2, membre.getPrenom());
            pst.setString(3, membre.getEmail());
            pst.setDate(4, Date.valueOf(membre.getDateInscription()));
            pst.executeUpdate();
            ResultSet generatedKeys = pst.getGeneratedKeys();
            if (generatedKeys.next()) {
                membre.setId(generatedKeys.getInt(1));
            }
            pst.close();
            result.setData(membre);
            result.setSuccess(true);
            result.setMessage("Membre created");
        } catch (SQLException e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            result.setData(null);
        }
        return result;
    }

    @Override
    public Result<Membre> findById(int id) {
        String sql = "SELECT * FROM membres WHERE id = ?";
        Result<Membre> result = new Result<>();
        try {
            DB_Connection con = new DB_Connection();
            PreparedStatement pst = con.getCon().prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Membre membre = new Membre();
                membre.setId(rs.getInt("id"));
                membre.setNom(rs.getString("nom"));
                membre.setPrenom(rs.getString("prenom"));
                membre.setEmail(rs.getString("email"));
                membre.setDateInscription(rs.getDate("dateInscription").toLocalDate());

                result.setData(membre);
                result.setSuccess(true);
                result.setMessage("Membre found");
            } else {
                result.setSuccess(false);
                result.setMessage("Membre not found");
                result.setData(null);
            }
            pst.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            result.setData(null);
        }
        return result;
    }

    @Override
    public Result<List<Membre>> findAll() {
        String sql = "SELECT * FROM membres";
        Result<List<Membre>> result = new Result<>();
        try {
            ArrayList<Membre> all = new ArrayList<>();
            DB_Connection con = new DB_Connection();
            PreparedStatement stmt = con.getCon().prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Membre membre = new Membre();
                membre.setId(rs.getInt("id"));
                membre.setNom(rs.getString("nom"));
                membre.setPrenom(rs.getString("prenom"));
                membre.setEmail(rs.getString("email"));
                membre.setDateInscription(rs.getDate("dateInscription").toLocalDate());
                all.add(membre);
            }
            result.setData(all);
            result.setSuccess(true);
            result.setMessage("List of membres found");
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            result.setData(null);
        }
        return result;
    }

    public Result<Set<Membre>> findAllasSet() {
        Set<Membre> set = new HashSet<>();
        Result<Set<Membre>> result = new Result<>();
        try {
            DB_Connection con = new DB_Connection();
            PreparedStatement stmt = con.getCon().prepareStatement("SELECT * FROM membres");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Membre membre = new Membre();
                membre.setId(rs.getInt("id"));
                membre.setNom(rs.getString("nom"));
                membre.setPrenom(rs.getString("prenom"));
                membre.setEmail(rs.getString("email"));
                membre.setDateInscription(rs.getDate("dateInscription").toLocalDate());
                set.add(membre);
            }
            stmt.close();
            rs.close();
            result.setData(set);
            result.setSuccess(true);
            result.setMessage("Set of membres found");
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
        String sql = "DELETE FROM membres WHERE id = ?";
        Result<Boolean> result = new Result<>();
        try {
            DB_Connection con = new DB_Connection();
            PreparedStatement stmt = con.getCon().prepareStatement(sql);
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                result.setSuccess(true);
                result.setMessage("Membre deleted");
                result.setData(true);
            } else {
                result.setSuccess(false);
                result.setMessage("Membre not found");
                result.setData(false);
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            result.setData(false);
        }
        return result;
    }

    public Result<Livre> update(Livre livre) {
        // This method doesn't apply to MembreDao
        return null;
    }

    @Override
    public Result<Membre> update(Membre membre) {
        String sql = "UPDATE membres SET nom = ?, prenom = ?, email = ?, dateInscription = ? WHERE id = ?";
        Result<Membre> result = new Result<>();
        try {
            DB_Connection con = new DB_Connection();
            PreparedStatement stmt = con.getCon().prepareStatement(sql);
            stmt.setString(1, membre.getNom());
            stmt.setString(2, membre.getPrenom());
            stmt.setString(3, membre.getEmail());
            stmt.setDate(4, Date.valueOf(membre.getDateInscription()));
            stmt.setInt(5, membre.getId());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                result.setData(membre);
                result.setSuccess(true);
                result.setMessage("Membre updated");
            } else {
                result.setSuccess(false);
                result.setMessage("Membre not found");
                result.setData(null);
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            result.setData(null);
        }
        return result;
    }

    public Result<Membre> findByEmail(String email) {
        String sql = "SELECT * FROM membres WHERE email = ?";
        Result<Membre> result = new Result<>();
        try {
            DB_Connection con = new DB_Connection();
            PreparedStatement pst = con.getCon().prepareStatement(sql);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Membre membre = new Membre();
                membre.setId(rs.getInt("id"));
                membre.setNom(rs.getString("nom"));
                membre.setPrenom(rs.getString("prenom"));
                membre.setEmail(rs.getString("email"));
                membre.setDateInscription(rs.getDate("dateInscription").toLocalDate());

                result.setData(membre);
                result.setSuccess(true);
                result.setMessage("Membre found by email");
            } else {
                result.setSuccess(false);
                result.setMessage("Membre not found");
                result.setData(null);
            }
            pst.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            result.setData(null);
        }
        return result;
    }

    public Result<Set<Membre>> findMembresActifs(){
        Result<Set<Membre>> result = new Result<>();
        String sql = "SELECT * FROM membres WHERE id IN (SELECT id_membre FROM empreintes)";
        try {
            DB_Connection con = new DB_Connection();
            PreparedStatement pst = con.getCon().prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            Set<Membre> membres = new HashSet<>();
            while (rs.next()) {
                Membre membre = new Membre();
                membre.setId(rs.getInt("id"));
                membre.setNom(rs.getString("nom"));
                membre.setPrenom(rs.getString("prenom"));
                membre.setEmail(rs.getString("email"));
                membre.setDateInscription(rs.getDate("dateInscription").toLocalDate());
                membres.add(membre);
            }
            result.setData(membres);
            result.setSuccess(true);
            result.setMessage("Membres actifs found");
        }catch (SQLException e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            result.setData(null);
        }
        return result;
    }
}