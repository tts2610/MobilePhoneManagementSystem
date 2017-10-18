/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package outbound.model;




import branch.model.Branch;
import database.IDAO;
import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import utility.CustomizedTableModel;

/**
 *
 * @author tuan
 */
public class ProductTableModel extends CustomizedTableModel<Product> {

    public ProductTableModel() {
        super(new ProductDAOImpl(), new String[]{"ID", "Branch", "Product Name", "Stock", "Price", "Description","Enable","SaleOff","Image"});
    }
    

    public Class getColumnClass(int column){
        //image
        if(column == 8){
            
            return Icon.class;
        }
        //salesoff
        else if(column==7){
            return String.class;
        }
        //enabled
        else if(column==6){
            return Boolean.class;
        }
        //descrip
        else if(column==5){
            return String.class;
        }
        //price
        else if(column==4){
            return Float.class;
        }
        //stock
        else if(column==3){
            return Integer.class;
        }
        //name
        else if(column==2){
            return String.class;
        }
        //braid
        else if(column==1){
            return Integer.class;
        }
        //proid
        else if(column==0) {
            return Integer.class;
        }
        return Integer.class;
    }
   
   @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Product product = list.get(rowIndex);
        
         switch(columnIndex){
             case 0 : return product.getProId();
             case 1 : return product.getBraname();
             case 2 : return product.getProName();
             case 3 : return product.getProStock();
             case 4 : return product.getProPrice();
             case 5 : return product.getProDesc();
             case 6 : return product.getProEnabled();
             case 7 : return product.getSaleoffname();
                 
             case 8 : {
                 if(product.getProImage()!=null)
                 return new ImageIcon(new ImageIcon(product.getProImage()).getImage().getScaledInstance(150, 120, Image.SCALE_SMOOTH));
                 else{
                  return new ImageIcon(new ImageIcon(getClass().getResource("/image/product/1.png")).getImage().getScaledInstance(150, 120, Image.SCALE_SMOOTH));   
                 }
             }
             case 9 :return product.getBraId();
         }
         return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
         Product product = list.get(rowIndex);
         switch(columnIndex){
             case 0 : product.setProId((int) aValue);break;
             case 1 : product.setBraname(((Branch) aValue).getBraName());break;
             case 2 : product.setProName((String) aValue);break;
             case 3 : product.setProStock((int) aValue);break;
             case 4 : product.setProPrice((float) aValue);break;
             case 5 : product.setProDesc((String) aValue);break;
             case 6 : product.setProEnabled((boolean) aValue);break;
             case 7 : product.setSaleoffname((String) aValue);break;
             case 8 : product.setProImage((byte[]) aValue);break;
                 
                 
             
         }
         fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
       
            return false;
        
    }
    
   
    
    
}
