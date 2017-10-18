/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user.controller;

import user.model.UserEmployee;
import user.model.UserEmployeeDAOImpl;
import utility.CustomizedComboBoxModel;

/**
 *
 * @author BonBon
 */
public class UserEmployeeComboBoxModel extends CustomizedComboBoxModel<UserEmployee> {

    public UserEmployeeComboBoxModel() {
        super(new UserEmployeeDAOImpl());
    }

    
    public UserEmployee getUserEmployeeNameFromValue(String empName) {
        UserEmployee result = null;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getEmpName().equals(empName)) {
                result = list.get(i);
                break;
            }
        }
        return result;
    }
    public UserEmployee getUserEmployeeIDFromValue(int empID) {
        UserEmployee result = null;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getEmpID()==empID) {
                result = list.get(i);
                break;
            }
        }
        return result;
    }
}
