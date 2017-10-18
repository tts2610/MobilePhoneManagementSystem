package order.controller;

import order.model.OrderStatus;
import order.model.OrderStatusDAOImpl;
import utility.CustomizedComboBoxModel;

/**
 *
 * @author Hoang
 */
public class OrderStatusComboBoxModel extends CustomizedComboBoxModel<OrderStatus> {

    public OrderStatusComboBoxModel() {
        super(new OrderStatusDAOImpl());
    }

    /**
     * Tim status object tu status name
     *
     * @param sttName
     * @return
     */
    public OrderStatus getStatusFromValue(String sttName) {
        OrderStatus result = null;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getSttName().equals(sttName)) {
                result = list.get(i);
                break;
            }
        }
        return result;
    }
}
