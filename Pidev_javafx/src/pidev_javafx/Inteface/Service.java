/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pidev_javafx.Inteface;

import java.util.HashMap;

/**
 *
 * @author MSI
 */
public interface Service <T> {
    public HashMap<Integer,T> findAll();
    public void create(T t);
    public void update(T t);
    public void delete(T t);
}
