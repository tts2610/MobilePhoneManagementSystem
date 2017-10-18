package service.controller.backup;

import service.controller.*;
import service.model.ServiceStatus;
import service.model.ServiceStatusDAOImpl;
import utility.CustomizedComboBoxModel;

/**
 *
 * @author BonBon
 */
public class ServiceStatusComboBoxModel extends CustomizedComboBoxModel<ServiceStatus> {

    public ServiceStatusComboBoxModel() {
        super(new ServiceStatusDAOImpl());
    }

    /**
     * Tim status object tu status name
     *
     * @param sttName
     * @return
     */
    public ServiceStatus getStatusFromValue(String sttName) {
        ServiceStatus result = null;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getSttName().equals(sttName)) {
                result = list.get(i);
                break;
            }
        }
        return result;
    }
}
