package utility;

import database.IDAO;
import java.util.List;
import javax.swing.AbstractListModel;

/**
 *
 * @author Hoang
 */
public class CheckBoxListModel<M> extends AbstractListModel {

    protected List<M> list;
    protected M item;
    protected IDAO<M> daoImpl;
    protected int selectingIndex;

    public CheckBoxListModel(IDAO<M> daoImpl) {
        this.daoImpl = daoImpl;
        this.list = daoImpl.getList();
    }

    public int getIndexOfElement(M item) {
        return list.indexOf(item);
    }

    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    public M getElementAt(int index) {
        return list.get(index);
    }
}
