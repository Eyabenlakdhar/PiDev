/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pidev_javafx.Entity;

import java.sql.Date;

/**
 *
 * @author MSI
 */
public class Reservation {
    private Integer id;
    private Date date_res;
    private String type;
    private String comment;
    private Double price;
    private Integer user_id;

    public Reservation(Integer id, Date date_res, String type, String comment, Double price, Integer user_id) {
        this.id = id;
        this.date_res = date_res;
        this.type = type;
        this.comment = comment;
        this.price = price;
        this.user_id = user_id;
    }

    public Reservation(Date date_res, String type, String comment, Double price, Integer user_id) {
        this.date_res = date_res;
        this.type = type;
        this.comment = comment;
        this.price = price;
        this.user_id = user_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate_res() {
        return date_res;
    }

    public void setDate_res(Date date_res) {
        this.date_res = date_res;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }
    
}
