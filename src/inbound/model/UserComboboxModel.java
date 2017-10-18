/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inbound.model;

import database.IDAO;
import utility.CustomizedComboBoxModel;

/**
 *
 * @author tuan
 */
public class UserComboboxModel extends CustomizedComboBoxModel<User> {
    
    public UserComboboxModel() {
        super(new UserDaoImpl());
    }
    
    public User getUserFromValue(String userName) {
        User result = null;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUsername().equals(userName)) {
                result = list.get(i);
                break;
            }
        }
        return result;
    }
    
}
