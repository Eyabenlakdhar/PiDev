/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pidev_javafx.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.sql.Connection;
import pidev_javafx.Inteface.Service;
import pidev_javafx.Entity.Reclamation;
import pidev_javafx.Utils.DataSource;
import pidev_javafx.Utils.Session;

/**
 *
 * @author MSI
 */
public class ReclamationService implements Service<Reclamation> {
    private  Connection db;

    public ReclamationService() {
        this.db = DataSource.getInstance().getCnx();
    }
    
    @Override
    public HashMap<Integer, Reclamation> findAll() {
        try{
            ResultSet rs = db.createStatement().executeQuery("Select * from reclamation");
            HashMap<Integer, Reclamation> reclamations = new HashMap();
            while(rs.next()){
                reclamations.put(rs.getRow(), new Reclamation(rs.getInt("id"), rs.getString("rec_text"), rs.getDate("date_rec"), rs.getString("rec_subject"), rs.getInt("id_user_id"),rs.getBoolean("etat_reservation"), rs.getString("reponse")));
            }
            return reclamations;
        }catch(Exception e){
            System.out.println(e);
            return null;
        }
    }


    @Override
    public void create(Reclamation t) {
        try{
            PreparedStatement ps = db.prepareStatement("insert into reclamation (rec_text, date_rec, rec_subject, id_user_id, etat_reservation) values (?, CURRENT_DATE(),?,?,0)");
            ps.setString(1, t.getRec_text());
            ps.setString(2, t.getRec_subject());
            ps.setInt(3, Session.getUser().getId());
            ps.executeUpdate();
        }catch(Exception e){
            System.out.println("oops");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(Reclamation t) {
        try{
            PreparedStatement ps = db.prepareStatement("update reclamation set rec_text = ?, rec_subject = ? ,  etat_reservation = ?, reponse = ? where id = ? ");
            ps.setString(1, t.getRec_text());
            ps.setString(2, t.getRec_subject());
            ps.setBoolean(3, t.getEtat());
            ps.setString(4, t.getReponse());
            ps.setInt(5, t.getId());
            ps.executeUpdate();
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Override
    public void delete(Reclamation t) {
        try{
            PreparedStatement ps = db.prepareStatement("DELETE FROM  reclamation  where id = ? ");
            ps.setInt(1, t.getId());
            ps.executeUpdate();
        }catch(Exception e){
            
        }
    }
    
}
