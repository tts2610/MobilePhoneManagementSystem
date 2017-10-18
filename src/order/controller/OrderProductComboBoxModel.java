package order.controller;

import java.util.ArrayList;
import java.util.List;
import order.model.OrderBranch;
import order.model.OrderProduct;
import order.model.OrderProductDAOImplComboBox;
import utility.CustomizedComboBoxModel;

/**
 * Dung cho combo box column product trong table product list cua OrderDialog.
 *
 * @author Hoang
 */
public class OrderProductComboBoxModel extends CustomizedComboBoxModel<OrderProduct> {

    // For filtering
    private List<OrderBranch> braList;
    private String proName;
    private List<OrderProduct> origin;
    private List<OrderProduct> filterByBranch;
    private List<OrderProduct> filterByName;

    public OrderProductComboBoxModel() {
        super(new OrderProductDAOImplComboBox());
        filterByBranch = new ArrayList();
        filterByName = new ArrayList();
        origin = new ArrayList(list);
        braList = new ArrayList();
    }

    public OrderProduct getOrderProductFromName(String name) {
        List<OrderProduct> tmp = new ArrayList();
        OrderProduct result = new OrderProduct();
        origin.stream().filter(op -> op.getProName().equals(name)).forEach(op -> tmp.add(op));
        if (tmp.size() > 0) {
            result = tmp.get(0);
        }
        return result;
    }

    private OrderProduct getOrderProductFromIndex(int index) {
        return origin.get(index);
    }

    /**
     * Filter cho cai combo box ba dao Product trong table cua Order dialog
     */
    public void filter() {
        filterByBranch.clear();
        filterByName.clear();

        if (braList.size() > 0) {
            braList.stream().forEach(branch -> origin.stream().filter(product -> product.getBraID() == branch.getBraID()).forEach(product -> filterByBranch.add(product)));
        } else {
            filterByBranch = new ArrayList(origin);
        }

        if (proName != null && !proName.isEmpty()) {
            filterByBranch.stream().filter(op -> op.getProName().toUpperCase().contains(proName.toUpperCase())).forEach(op -> filterByName.add(op));
        } else {
            filterByName = new ArrayList(filterByBranch);
        }

        list = new ArrayList(filterByName);
    }

    public List<OrderBranch> getBraList() {
        return braList;
    }

    public void setBraList(List<OrderBranch> braList) {
        this.braList = new ArrayList(braList);
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    /**
     * Tim product object tu product ID
     *
     * @param proID
     * @return
     */
    public OrderProduct getProductFromID(int proID) {
        OrderProduct result = null;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getProID() == proID) {
                result = list.get(i);
                break;
            }
        }
        return result;
    }

    public OrderProduct getProductFromName(String proName) {
        OrderProduct result = null;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getProName().equals(proName)) {
                result = list.get(i);
                break;
            }
        }
        return result;
    }
}
