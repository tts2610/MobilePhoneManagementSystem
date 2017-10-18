package customer.controller;

import customer.model.CustomerLevel;
import customer.model.CustomerLevelDAOImpl;
import utility.CustomizedComboBoxModel;

/**
 *
 * @author Hoang
 */
public class CustomerLevelComboBoxModel extends CustomizedComboBoxModel<CustomerLevel> {

    public CustomerLevelComboBoxModel() {
        super(new CustomerLevelDAOImpl());
    }

    /**
     * Tim customer level object tu customer level
     *
     * @param cusLevel
     * @return
     */
    public CustomerLevel getCustomerLevelFromValue(int cusLevel) {
        CustomerLevel result = null;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getCusLevel() == cusLevel) {
                result = list.get(i);
                break;
            }
        }
        return result;
    }

    /**
     * Tim customer level object tu level name
     *
     * @param levelName
     * @return
     */
    public CustomerLevel getCustomerLevelFromLevelName(String levelName) {
        CustomerLevel result = null;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getCusLevelName().equals(levelName)) {
                result = list.get(i);
                break;
            }
        }
        return result;
    }
}
