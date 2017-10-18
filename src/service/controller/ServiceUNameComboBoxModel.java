package service.controller;


import user.model.User;
import user.model.UserDAOImpl;
import utility.CustomizedComboBoxModel;

/**
 *
 * @author BonBon
 */
public class ServiceUNameComboBoxModel extends CustomizedComboBoxModel<User> {

    public ServiceUNameComboBoxModel() {
        super(new UserDAOImpl());
    }

   

    /**
     * Tim user name object tu users
     *
     * @param userName
     
     * @return
     */
    public User getUserNameFromValue(String userName) {
        User result = null;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUserName().equals(userName)) {
                result = list.get(i);
                break;
            }
        }
        return result;
    }
}
