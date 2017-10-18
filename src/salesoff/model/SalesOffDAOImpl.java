package salesoff.model;

import database.IDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;
import utility.SwingUtils;

/**
 *
 * @author Hoang
 */
public class SalesOffDAOImpl implements IDAO<SalesOff> {

    private CachedRowSet crs;  //CRS to update table
    private int filter;

    public SalesOffDAOImpl() {
        crs = getCRS("select * from SalesOff");
    }

    @Override
    public List<SalesOff> getList() {
        List<SalesOff> list = new ArrayList<>();
        try {
            if (crs.first()) {
                do {
                    list.add(new SalesOff(
                            crs.getInt(SalesOff.COL_SALEID),
                            crs.getString(SalesOff.COL_SALENAME),
                            crs.getDate(SalesOff.COL_SALESTART),
                            crs.getDate(SalesOff.COL_SALEEND),
                            crs.getFloat(SalesOff.COL_SALEAMOUNT)));
                } while (crs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalesOffDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public boolean insert(SalesOff salesOff) {
        boolean result = false;
        List<SalesOff> list = getList();

        // Check duplicate amount
//        int tmpAmount = -1;   // Toi thieu bang 0
//        if (list.size() > 0) {
//            for (int i = SalesOff.MIN_SALE; i <= SalesOff.MAX_SALE; i++) {
//                boolean found = false;
//                // Tim phan tu i trong mang list, neu tim thay thi thoat
//                for (SalesOff so : list) {
//                    int amount = (int) (so.getSaleAmount() * 100);
//                    if (i == amount) {
//                        found = true;
//                        break;
//                    }
//                }
//                if (!found) { //Co nghia la khong duplicate
//                    tmpAmount = i;
//                    break;
//                }
//            }
//        } else {
//            tmpAmount = SalesOff.MIN_SALE;
//        }

//        if (tmpAmount == -1) { // Toan bo amount da dung het
//            SwingUtils.showErrorDialog("Sales off has reached maximum amount to add !");
//        } else {
            // Khoi tao tri default de insert vao db
            salesOff.setSaleName("Sales Off " + System.currentTimeMillis());
            salesOff.setSaleStartDate(new Date()); // Current date

            // Mac dinh khuyen mai moi tao la 1 thang
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
            salesOff.setSaleEndDate(calendar.getTime());

            // Lay amount random
            Random random = new Random();
            float amount = (random.nextInt(SalesOff.MAX_SALE-SalesOff.MIN_SALE+1) + SalesOff.MIN_SALE)/100f;
            salesOff.setSaleAmount(amount);
//        }

        try {
            runPS("insert SalesOff values(?,?,?,?)", salesOff.getSaleName(), salesOff.getSaleStartDate(), salesOff.getSaleEndDate(), salesOff.getSaleAmount());

            // Refresh lai cachedrowset hien thi table
            crs.execute();
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(SalesOffDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public boolean update(SalesOff salesOff) {
        boolean result = false;

        try {
            // Check duplicate name
            CachedRowSet crs1 = getCRS("select * from SalesOff where SalesOffName like ? AND SalesOffID!=?", salesOff.getSaleName(), salesOff.getSaleID());
            // Check duplicate amount
//            CachedRowSet crs2 = getCRS("select * from SalesOff where SalesOffAmount=? AND SalesOffID!=?", salesOff.getSaleAmount(), salesOff.getSaleID());
            if (crs1.first()) {
                SwingUtils.showErrorDialog("SalesOff name cannot be duplicated !");
//            } else if (crs2.first()) {
//                SwingUtils.showErrorDialog("SalesOff amount cannot be duplicated !");
            } else if (salesOff.getSaleEndDate().compareTo(salesOff.getSaleStartDate()) < 0) {
                SwingUtils.showErrorDialog("End date must >= start date !");
            } else {
                runPS("update SalesOff set SalesOffName=?, SalesOffStartDate=?, SalesOffEndDate=?, SalesOffAmount=? where SalesOffID=?", salesOff.getSaleName(), salesOff.getSaleStartDate(), salesOff.getSaleEndDate(), salesOff.getSaleAmount(), salesOff.getSaleID());

                // Refresh lai cachedrowset hien thi table
                crs.execute();
                result = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalesOffDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    @Override
    public boolean delete(SalesOff salesOff) {
        boolean result = false;

        try {
            //Check sales off co ap dung cho product nao khong, neu khong thi delete
            CachedRowSet crs1 = getCRS("select * from Products where SalesOffID=?", salesOff.getSaleID());
            if (crs1.first()) {
                SwingUtils.showErrorDialog("Sales off is now in use with some product(s) !");
            } else {
                runPS("delete SalesOff where SalesOffID=?", salesOff.getSaleID());

                // Refresh lai cachedrowset hien thi table
                crs.execute();
                result = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalesOffDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int getSelectingIndex(int idx) {
        return 0;
    }

    @Override
    public void setSelectingIndex(int idx) {
    }

}
