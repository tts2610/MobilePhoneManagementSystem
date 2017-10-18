package order.controller;

import order.model.OrderCustomer;
import order.model.OrderCustomerDAOImpl;
import utility.CustomizedComboBoxModel;

/**
 *
 * @author Hoang
 */
public class OrderCustomerComboBoxModel extends CustomizedComboBoxModel<OrderCustomer> {

    public OrderCustomerComboBoxModel() {
        super(new OrderCustomerDAOImpl());
    }

    /**
     * Tim customer object tu customer ID
     *
     * @param cusID
     * @return customer
     */
    public OrderCustomer getCustomerFromID(int cusID) {
        OrderCustomer result = null;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getCusID()==cusID) {
                result = list.get(i);
                break;
            }
        }
        return result;
    }
}
