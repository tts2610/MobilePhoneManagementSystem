/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user.controller;

import user.model.NewUserEmployeeDAOImpl;
import user.model.UserEmployee;
import utility.CustomizedComboBoxModel;

/**
 *
 * @author BonBon
 */
public class NewUserComboBoxModel extends CustomizedComboBoxModel<UserEmployee> {

    public NewUserComboBoxModel() {
        super(new NewUserEmployeeDAOImpl());
    }

    /**
     * Tim type name object tu ServiceType
     * @param serTypeName    
     
     * @return
     */
    public UserEmployee getEmpNameFromValue(String empName) {
        UserEmployee result = null;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getEmpName().equals(empName)) {
                result = list.get(i);
                break;
            }
        }
        return result;
    }

    public UserEmployee getEmpIDFromValue(int empID) {
        UserEmployee result = null;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getEmpID()== empID) {
                result = list.get(i);
                break;
            }
        }
        return result;
    }
}
