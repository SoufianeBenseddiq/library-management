package ma.ensa.dao.impl;

import ma.ensa.config.DB_Connection;
import ma.ensa.dao.facade.DAO;
import ma.ensa.model.Empreinte;
import ma.ensa.model.Livre;
import ma.ensa.model.Membre;
import ma.ensa.model.Result;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EmpreinteDao implements DAO<Empreinte> {
    private LivreDao livreDao = new LivreDao();
    private MembreDao membreDao = new MembreDao();

    @Override
    public Result<Empreinte> create(Empreinte empreinte) {
        Result<Empreinte> result = new Result<>();
        String sql = "INSERT INTO empreintes (livre_id, membre_id, dateEmpreinte, dateRetourPrevue, dateRetourReelle) VALUES (?,?,?,?,?)";
        try {
            DB_Connection con = new DB_Connection();
            PreparedStatement pst = con.getCon().prepareStatement(sql);
            pst.setInt(1, empreinte.getLivre().getId());
            pst.setInt(2, empreinte.getMembre().getId());
            pst.setDate(3, Date.valueOf(empreinte.getDateEmpreinte()));
            pst.setDate(4, Date.valueOf(empreinte.getDateRetourPrevue()));

            if (empreinte.getDateRetourReelle() != null) {
                pst.setDate(5, Date.valueOf(empreinte.getDateRetourReelle()));
            } else {
                pst.setNull(5, java.sql.Types.DATE);
            }

            pst.executeUpdate();
            ResultSet generatedKeys = pst.getGeneratedKeys();
            if (generatedKeys.next()) {
                empreinte.setId(generatedKeys.getInt(1));
            }
            pst.close();
            result.setData(empreinte);
            result.setSuccess(true);
            result.setMessage("Empreinte created");
        } catch (SQLException e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            result.setData(null);
        }
        return result;
    }

    @Override
    public Result<Empreinte> findById(int id) {
        String sql = "SELECT * FROM emprunts WHERE id = ?";
        Result<Empreinte> result = new Result<>();
        try {
            DB_Connection con = new DB_Connection();
            PreparedStatement pst = con.getCon().prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Empreinte empreinte = mapResultSetToEmpreinte(rs);
                result.setData(empreinte);
                result.setSuccess(true);
                result.setMessage("Empreinte found");
            } else {
                result.setSuccess(false);
                result.setMessage("Empreinte not found");
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
    public Result<List<Empreinte>> findAll() {
        String sql = "SELECT * FROM emprunts";
        Result<List<Empreinte>> result = new Result<>();
        try {
            LinkedList<Empreinte> all = new LinkedList<>();
            DB_Connection con = new DB_Connection();
            PreparedStatement stmt = con.getCon().prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Empreinte empreinte = mapResultSetToEmpreinte(rs);
                all.add(empreinte);
            }
            result.setData(all);
            result.setSuccess(true);
            result.setMessage("List of empreintes found");
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

    @Override
    public Result<Boolean> delete(int id) {
        String sql = "DELETE FROM emprunts WHERE id = ?";
        Result<Boolean> result = new Result<>();
        try {
            DB_Connection con = new DB_Connection();
            PreparedStatement stmt = con.getCon().prepareStatement(sql);
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                result.setSuccess(true);
                result.setMessage("Empreinte deleted");
                result.setData(true);
            } else {
                result.setSuccess(false);
                result.setMessage("Empreinte not found");
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

    public Result<Empreinte> update(Empreinte empreinte) {
        String sql = "UPDATE emprunts SET livre_id = ?, membre_id = ?, dateEmpreinte = ?, dateRetourPrevue = ?, dateRetourReelle = ? WHERE id = ?";
        Result<Empreinte> result = new Result<>();
        try {
            DB_Connection con = new DB_Connection();
            PreparedStatement stmt = con.getCon().prepareStatement(sql);
            stmt.setInt(1, empreinte.getLivre().getId());
            stmt.setInt(2, empreinte.getMembre().getId());
            stmt.setDate(3, Date.valueOf(empreinte.getDateEmpreinte()));
            stmt.setDate(4, Date.valueOf(empreinte.getDateRetourPrevue()));

            if (empreinte.getDateRetourReelle() != null) {
                stmt.setDate(5, Date.valueOf(empreinte.getDateRetourReelle()));
            } else {
                stmt.setNull(5, java.sql.Types.DATE);
            }

            stmt.setInt(6, empreinte.getId());
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                result.setData(empreinte);
                result.setSuccess(true);
                result.setMessage("Empreinte updated");
            } else {
                result.setSuccess(false);
                result.setMessage("Empreinte not found");
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

    private Empreinte mapResultSetToEmpreinte(ResultSet rs) throws SQLException {
        int livreId = rs.getInt("livre_id");
        int membreId = rs.getInt("membre_id");
        Result<Livre> livre = livreDao.findById(livreId);
        Result<Membre> membre = membreDao.findById(membreId);
        if (livre == null || membre == null) {
            return null;
        }
        int id = rs.getInt("id");
        LocalDate dateEmpreinte = rs.getDate("date_emprunt").toLocalDate();
        LocalDate dateRetourPrevue = rs.getDate("date_retourPrevue").toLocalDate();
        LocalDate dateRetourReelle = rs.getDate("date_retour_reel").toLocalDate();
        Empreinte empreinte = new Empreinte(id,livre.getData(),membre.getData(),dateEmpreinte,dateRetourPrevue,dateRetourReelle);

        return empreinte;
    }

    public Result<List<Empreinte>> findByMembre(int membreId) {
        String sql = "SELECT * FROM emprunts WHERE membre_id = ?";
        Result<List<Empreinte>> result = new Result<>();
        try {
            ArrayList<Empreinte> all = new ArrayList<>();
            DB_Connection con = new DB_Connection();
            PreparedStatement stmt = con.getCon().prepareStatement(sql);
            stmt.setInt(1, membreId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Empreinte empreinte = mapResultSetToEmpreinte(rs);
                all.add(empreinte);
            }
            result.setData(all);
            result.setSuccess(true);
            result.setMessage("Empreintes for membre found");
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

    public Result<List<Empreinte>> findByLivre(int livreId) {
        String sql = "SELECT * FROM emprunts WHERE livre_id = ?";
        Result<List<Empreinte>> result = new Result<>();
        try {
            ArrayList<Empreinte> all = new ArrayList<>();
            DB_Connection con = new DB_Connection();
            PreparedStatement stmt = con.getCon().prepareStatement(sql);
            stmt.setInt(1, livreId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Empreinte empreinte = mapResultSetToEmpreinte(rs);
                all.add(empreinte);
            }
            result.setData(all);
            result.setSuccess(true);
            result.setMessage("Empreintes for livre found");
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

    public Result<List<Empreinte>> findEmpruntsActifs() {
        String sql = "SELECT * FROM emprunts WHERE dateRetourReelle IS NULL";
        Result<List<Empreinte>> result = new Result<>();
        try {
            ArrayList<Empreinte> all = new ArrayList<>();
            DB_Connection con = new DB_Connection();
            PreparedStatement stmt = con.getCon().prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Empreinte empreinte = mapResultSetToEmpreinte(rs);
                all.add(empreinte);
            }
            result.setData(all);
            result.setSuccess(true);
            result.setMessage("Active empreintes found");
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

    public Result<List<Empreinte>> findEmpruntsEnRetard() {
        String sql = "SELECT * FROM emprunts WHERE dateRetourReelle IS NULL AND dateRetourPrevue < CURRENT_DATE";
        Result<List<Empreinte>> result = new Result<>();
        try {
            ArrayList<Empreinte> all = new ArrayList<>();
            DB_Connection con = new DB_Connection();
            PreparedStatement stmt = con.getCon().prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Empreinte empreinte = mapResultSetToEmpreinte(rs);
                all.add(empreinte);
            }
            result.setData(all);
            result.setSuccess(true);
            result.setMessage("Overdue empreintes found");
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

    public Result<List<Empreinte>> findEmpruntsEnCours() {
        String sql = "SELECT * FROM emprunts WHERE dateRetourReelle IS NULL";
        Result<List<Empreinte>> result = new Result<>();
        try {
            ArrayList<Empreinte> all = new ArrayList<>();
            DB_Connection con = new DB_Connection();
            PreparedStatement stmt = con.getCon().prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Empreinte empreinte = mapResultSetToEmpreinte(rs);
                all.add(empreinte);
            }
            result.setData(all);
            result.setSuccess(true);
            result.setMessage("Emprunts en cours found");
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
}