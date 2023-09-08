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
public class Reclamation {
    private Integer id;
    private String rec_text;
    private Date date_rec;
    private String rec_subject;
    private Integer user_id ;
    private Boolean etat;
    private String reponse;

    public Reclamation(Integer id, String rec_text, Date date_rec, String rec_subject, Integer user_id, Boolean etat, String reponse) {
        this.id = id;
        this.rec_text = rec_text;
        this.date_rec = date_rec;
        this.rec_subject = rec_subject;
        this.user_id = user_id;
        this.etat = etat;
        this.reponse = reponse;
    }

    public Reclamation(String rec_text, Date date_rec, String rec_subject, Integer user_id, Boolean etat, String reponse) {
        this.rec_text = rec_text;
        this.date_rec = date_rec;
        this.rec_subject = rec_subject;
        this.user_id = user_id;
        this.etat = etat;
        this.reponse = reponse;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRec_text() {
        return rec_text;
    }

    public void setRec_text(String rec_text) {
        this.rec_text = rec_text;
    }

    public Date getDate_rec() {
        return date_rec;
    }

    public void setDate_rec(Date date_rec) {
        this.date_rec = date_rec;
    }

    public String getRec_subject() {
        return rec_subject;
    }

    public void setRec_subject(String rec_subject) {
        this.rec_subject = rec_subject;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Boolean getEtat() {
        return etat;
    }

    public void setEtat(Boolean etat) {
        this.etat = etat;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }
    
}
