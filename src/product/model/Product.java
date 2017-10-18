/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product.model;

/**
 *
 * @author tuan
 */
public class Product {
    private int proId;
    private String braname;
    private String proName;
    private int proStock;
    private float proPrice;
    private String proDesc;
    private boolean proEnabled;
    private String saleoffname;
    private byte[] proImage;
    private int braId;
    private int salesoffid;

    public static final String COL_ID = "proid";
    public static final String COL_BraName = "braname";
    public static final String COL_ProName = "proname";
    public static final String COL_Stock = "prostock";
    public static final String COL_Price = "proprice";
    public static final String COL_DESC = "ProDescr";
    public static final String COL_Status ="proEnabled";
    public static final String COL_SaleOffName = "SalesOffName";
    public static final String COL_Image = "ProImage";
    public static final String COL_BraID = "BraID";
    public static final String COL_SaleOffID = "SalesOffID";

    public Product(int proId, String braname, String proName, int proStock, float proPrice, String proDesc, boolean proEnabled,String salesoffname, byte[] proImage,int branchid,int salesoffid) {
        this.proId = proId;
        this.braname = braname;
        this.proName = proName;
        this.proStock = proStock;
        this.proPrice = proPrice;
        this.proDesc = proDesc;
        this.proEnabled = proEnabled;
        this.saleoffname = salesoffname;
        this.proImage = proImage;
        this.braId = branchid;
        this.salesoffid = salesoffid;
    }
    
    
    public static final String Query_Show = "SELECT "
                    +Product.COL_ID+", "
                    +Product.COL_BraName+", "
                    +Product.COL_ProName+", "
                    +Product.COL_Stock+", "
                    +Product.COL_Price+", "
                    +Product.COL_DESC+", "
                    +Product.COL_Status+", "
                    +Product.COL_SaleOffName+", "
                    +Product.COL_Image+", "
                    +"products."+Product.COL_BraID+", "
                    +"salesoff.salesoffID"
                    +" from products join branches on "+"branches."+COL_BraID+"=products."+COL_BraID
                    +" left join salesoff on salesoff.salesoffid=products.salesoffid"
                    +" Order by ProID DESC";
    public static final String Query_Insert = "Insert into products values(?,?,?,?,?,?,?,?)";
    public static final String Query_Update = "Update products set "
                    +Product.COL_BraID+"=?, "
                    +Product.COL_ProName+"=?, "
                    +Product.COL_Stock+"=?, "
                    +Product.COL_Price+"=?, "
                    +Product.COL_DESC+"=?, "
                    +Product.COL_Status+"=?, "
                    +Product.COL_SaleOffID+"=?, "
                    +Product.COL_Image+"=? "+"where"+Product.COL_ID+"=?";
    public static final String Query_delete = "delete from product where "+COL_ID+"=?";

    
   
   

    public String getBraname() {
        return braname;
    }

    public void setBraname(String braname) {
        this.braname = braname;
    }

    public String getSaleoffname() {
        return saleoffname;
    }

    public void setSaleoffname(String saleoffname) {
        this.saleoffname = saleoffname;
    }

    public int getSalesoffid() {
        return salesoffid;
    }

    public void setSalesoffid(int salesoffid) {
        this.salesoffid = salesoffid;
    }
    
    public int getSaleofid() {
        return salesoffid;
    }

    public void setSaleofid(int saleofid) {
        this.salesoffid = saleofid;
    }

    

    public void setProId(int proId) {
        this.proId = proId;
    }

    public void setBraId(int braId) {
        this.braId = braId;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public void setProStock(int proStock) {
        this.proStock = proStock;
    }

    public void setProPrice(float proPrice) {
        this.proPrice = proPrice;
    }

    public void setProDesc(String proDesc) {
        this.proDesc = proDesc;
    }

    public void setProImage(byte[] proImage) {
        this.proImage = proImage;
    }

    public void setProEnabled(boolean proEnabled) {
        this.proEnabled = proEnabled;
    }
    
    public boolean getProEnabled(){
        return proEnabled;
    }

    public int getProId() {
        return proId;
    }

    public int getBraId() {
        return braId;
    }

    public String getProName() {
        return proName;
    }

    public int getProStock() {
        return proStock;
    }

    public float getProPrice() {
        return proPrice;
    }

    public String getProDesc() {
        return proDesc;
    }

    public byte[] getProImage() {
        return proImage;
    }

    public Product(int proId, int braId, String proName, int proStock, float proPrice, String proDesc, boolean proEnabled, String salesoffname, byte[] proImage) {
        this.proId = proId;
        this.braId = braId;
        this.proName = proName;
        this.proStock = proStock;
        this.proPrice = proPrice;
        this.proDesc = proDesc;
        this.proEnabled = proEnabled;
        this.saleoffname = salesoffname;
        this.proImage = proImage;
        
    }
    

    public Product() {
    }
    
    
    
    
}
