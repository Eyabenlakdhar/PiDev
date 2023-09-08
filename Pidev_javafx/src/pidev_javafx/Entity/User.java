/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pidev_javafx.Entity;

import java.util.ArrayList;

/**
 *
 * @author MSI
 */
public class User {
    private Integer id;
    private String email;
    private String password;
    private String roles;
    private Integer age;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
    
    public User(Integer id, String email, String password, String roles, Integer age) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.age = age;
    }

    public User(String email, String password, String roles, Integer age) {
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.age = age;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", roles=" + roles + '}';
    }
    
}
