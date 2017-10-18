/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.controller;

import service.model.ServiceType;
import service.model.ServiceTypeDAOImpl;
import utility.CustomizedComboBoxModel;

/**
 *
 * @author BonBon
 */
public class ServiceTypeComboBoxModel extends CustomizedComboBoxModel<ServiceType> {

    public ServiceTypeComboBoxModel() {
        super(new ServiceTypeDAOImpl());
    }

    /**
     * Tim type name object tu ServiceType
     *
     * @param sertypename
     *
     * @return
     */
    public ServiceType getServiceTypeNameFromValue(String serTypeName) {
        ServiceType result = null;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getSerTypeName().equals(serTypeName)) {
                result = list.get(i);
                break;
            }
        }
        return result;
    }
    public ServiceType getServiceTypeNameFromValue(int serTypeID) {
        ServiceType result = null;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getSerTypeID()==serTypeID) {
                result = list.get(i);
                break;
            }
        }
        return result;
    }
}
