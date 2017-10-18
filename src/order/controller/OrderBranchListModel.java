package order.controller;

import database.IDAO;
import order.model.OrderBranch;
import order.model.OrderBranchDAOImpl;
import utility.CheckBoxListModel;

/**
 *
 * @author Hoang
 */
public class OrderBranchListModel extends CheckBoxListModel<OrderBranch> {
    public OrderBranchListModel() {
        super(new OrderBranchDAOImpl());
    }
}
