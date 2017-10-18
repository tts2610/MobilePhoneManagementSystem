/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product.dao;

import database.DBProvider;
import database.IDAO;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;
import main.controller.LoginFrame;
import product.model.Product;
import utility.SwingUtils;





/**
 *
 * @author tuan
 */
public class ProductDAOImpl implements IDAO<Product> {
    private CachedRowSet crs;           //CRS for update database
    

    public ProductDAOImpl() {
        this.crs = getCRS(Product.Query_Show);
         
    }
    
    
    @Override
    public List<Product> getList() {
        List<Product> productList = new ArrayList<>();
        try {
            if (crs.first()) {
                do {
                    productList.add(new Product(
                            crs.getInt(Product.COL_ID),
                            crs.getString(Product.COL_BraName),
                            crs.getString(Product.COL_ProName),
                            crs.getInt(Product.COL_Stock),
                            crs.getFloat(Product.COL_Price),
                            crs.getString(Product.COL_DESC),
                            crs.getBoolean(Product.COL_Status),
                            crs.getString(Product.COL_SaleOffName),
                            crs.getBytes("ProImage"),
                            crs.getInt(Product.COL_BraID),
                            crs.getInt(Product.COL_SaleOffID)));
                            
                } while (crs.next());
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return productList;
    }

    
    @Override
    public boolean insert(Product product) {
        boolean result = false;
        product.setBraname("HTC");
        product.setProName("Default Product"+System.currentTimeMillis());
        product.setProStock(0);
        product.setProPrice(0);
        product.setProDesc("");
        product.setProEnabled(true);
        
        try {
            CachedRowSet crs1 = getCRS("SELECT * from salesoff");
            if(!crs1.first()){
                SwingUtils.showErrorDialog("Input at least 1 sale off event!");
                return false;
            }
            //lay saleoffid nho nhat
            
           
            runPS("INSERT INTO products (BraID,ProName,ProStock,ProPrice,ProDescr,ProEnabled) values ((select BraID from Branches where BraName=?),?,?,?,?,?)",product.getBraname(),product.getProName(),product.getProStock(),product.getProPrice(),product.getProDesc(),product.getProEnabled()); 
              
                    
            
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    @Override
    public boolean update(Product product) {
        boolean result = false;
        try{
            CachedRowSet crs1 = getCRS("SELECT * from products where proName="+"'"+product.getProName()+"'"+"AND products.SalesOffID IS NOT NULL AND ProID!="+product.getProId());
                if(crs1.first()){
                    
                SwingUtils.showErrorDialog("Product name cannot be duplicated !");
                
                   
                }
               
                else{
            runPS("Update products set BraID=(select braid from branches where braName=?),ProName=?,ProStock=?,ProPrice=?,ProDescr=?,ProEnabled=? where ProID=?", 
                    product.getBraname(),
                    product.getProName(),
                    product.getProStock(),
                    product.getProPrice(),
                    product.getProDesc(),
                    product.getProEnabled(),
                    product.getProId());
            
            
            crs.execute();

            
            result = true;
                }
            
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
        
    }

    @Override
    public boolean delete(Product product) {
        boolean result = false;
        //kiem tra product co trong order details chua
        CachedRowSet crs1 = getCRS("select * from OrderDetails where ProID=" + product.getProId());
        CachedRowSet crs2 = getCRS("SELECT * FROM InDetails where ProID="+product.getProId());
        CachedRowSet crs3 = getCRS("SELECT * FROM OutDetails where ProID="+product.getProId());
        try {
            if (crs1.first()) {
                SwingUtils.showErrorDialog("Product is now in ORDER! Delete failed");
            }
            else if(crs2.first()){
                SwingUtils.showErrorDialog("Product is now in inbound! Delete failed");
            }
            else if(crs3.first()){
                SwingUtils.showErrorDialog("Product is now in outbound! Delete failed");
            }
            else {
                runPS("delete from Products where ProID=?", product.getProId());
                crs.execute();
                result = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    public boolean updateImage(Product product,InputStream is){
        boolean result = false;
        
        try {
           DBProvider db = new DBProvider(LoginFrame.config.host, LoginFrame.config.port, LoginFrame.config.DBName, LoginFrame.config.name, LoginFrame.config.password);
             db.start();
            PreparedStatement ps = db.getPreparedStatement("Update Products set ProImage=? where ProID=?");
            ps.setBlob(1, is);
            ps.setInt(2, product.getProId());
            result = true;
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public int getSelectingIndex(int idx) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setSelectingIndex(int idx) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
