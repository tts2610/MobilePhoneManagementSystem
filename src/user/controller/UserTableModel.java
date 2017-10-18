package user.controller;

import user.model.User;
import user.model.UserDAOImpl;
import user.model.UserEmployee;
import utility.CustomizedTableModel;

/**
 *
 * @author BonBon
 */
public class UserTableModel extends CustomizedTableModel<User> {

    public UserTableModel() {
        super(new UserDAOImpl(), new String[]{"ID", "User Name", "Employee Name", "Status", "Password","EmpID"});
    }
   
    @Override
    public Class<?> getColumnClass(int column) {
        Class[] columnClasses = {Integer.class, String.class, String.class, Boolean.class, String.class,Integer.class};
        return columnClasses[column];
    }
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 2 || columnIndex == 3 ; //Chi sua empname va stt
    }
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        item = list.get(rowIndex);
        Object result = null;
        switch (columnIndex) {
            case 0:
                result = item.getUserID();
                break;
            case 1:
                result = item.getUserName();
                break;
            case 2:
                result = item.getEmpName();
                break;
            case 3:
                result = item.isUserEnable();
                break;
            case 4:
                result = item.getPassword();
                break;
            case 5:
                result = item.getEmpID();
                break;
        }
        return result;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        User user = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                user.setUserID((int) aValue);
                break;
            case 1:
                user.setUserName((String) aValue);
                break;
            case 2:
                if(aValue!=null)
                user.setEmpName(((UserEmployee)aValue).getEmpName());
//                 user.setEmpName((String) aValue);
              break;
            case 3:
                user.setUserEnable((boolean) aValue);
                break;
            case 4:
                user.setPassword((String) aValue);
                break;
            case 5:
                user.setEmpID((int) aValue);
                break;
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}
