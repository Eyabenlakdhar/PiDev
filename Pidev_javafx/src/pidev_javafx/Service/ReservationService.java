/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pidev_javafx.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import pidev_javafx.Entity.Reservation;
import pidev_javafx.Inteface.Service;
import pidev_javafx.Utils.DataSource;
import pidev_javafx.Utils.Session;

/**
 *
 * @author MSI
 */
public class ReservationService implements Service<Reservation> {
     private  Connection db;

    public ReservationService() {
        this.db = DataSource.getInstance().getCnx();
    }
    
    @Override
    public HashMap<Integer, Reservation> findAll() {
        try{
            ResultSet rs = db.createStatement().executeQuery("Select * from reservation");
            HashMap<Integer, Reservation> reservations = new HashMap();
            while(rs.next()){
                reservations.put(rs.getRow(), new Reservation(rs.getInt("id"), rs.getDate("date_res"), rs.getString("type"), rs.getString("comment "),rs.getDouble("price"), rs.getInt("id_user_id")));
            }
            return reservations;
        }catch(Exception e){
            return null;
        }
    }


    @Override
    public void create(Reservation t) {
        try{
            PreparedStatement ps = db.prepareStatement("insert into reservation (date_rec, type, comment, price, id_user_id) values (?,?,?,?,?)");
            ps.setDate(1, t.getDate_res());
            ps.setString(2, t.getType());
            ps.setString(3, t.getComment());
            ps.setDouble(4, t.getPrice());
            ps.setInt(5, Session.getUser().getId());
            ps.executeUpdate();
        }catch(Exception e){
        }
    }

    @Override
    public void update(Reservation t) {
        try{
            PreparedStatement ps = db.prepareStatement("update reservation set date_res = ?, type = ? ,  comment = ?, price = ? where id = ? ");
             ps.setDate(1, t.getDate_res());
            ps.setString(2, t.getType());
            ps.setString(3, t.getComment());
            ps.setDouble(4, t.getPrice());
            ps.setInt(5, t.getId());
            ps.executeUpdate();
        }catch(Exception e){
        }
    }

    @Override
    public void delete(Reservation t) {
        try{
            PreparedStatement ps = db.prepareStatement("DELETE FROM  reservation  where id = ? ");
            ps.setInt(1, t.getId());
            ps.executeUpdate();
        }catch(Exception e){
            
        }
    }
}
