/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product.model;

import java.awt.Component;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.EventObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import product.controller.ProductPanel;
import product.dao.ProductDAOImpl;

/**
 *
 * @author tuan
 */
public class ImageRenderer extends DefaultTableCellRenderer {

    private Product product;
    private String s;
    ImageIcon myImage;
    JLabel lbl = new JLabel();
    JFileChooser fileChooser;

    public ImageRenderer(Product product) {
        
        this.product = product;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        if (table.isCellSelected(row, column)) {
            SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                    fileChooser= new JFileChooser();
                    fileChooser.setCurrentDirectory(new File(System.getProperty("user.name")));
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("*.IMAGE", "jpg", "gif", "png");
                    fileChooser.addChoosableFileFilter(filter);
                    int result = fileChooser.showSaveDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        String path = selectedFile.getAbsolutePath();
                        myImage = new ImageIcon(path);

                        s = path;

                        try {
                            InputStream is = new FileInputStream(new File(s));
                            new ProductDAOImpl().updateImage(product, is);
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(ProductPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        lbl.setIcon((ImageIcon) myImage);
                        
                    } 
                    
            }
            
        });
          
            return lbl;
        }
        
           
           
        
        lbl.setIcon((ImageIcon) value);
        return lbl;

    }

}
