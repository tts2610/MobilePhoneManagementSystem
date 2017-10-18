package utility;

import com.jtattoo.plaf.hifi.HiFiLookAndFeel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

public class SwingUtils {

    // Define some string sentences
    public static final String INSERT_SUCCESS = "Inserted successfully !";
    public static final String INSERT_FAIL = "Insertion has failed !";
    public static final String UPDATE_SUCCESS = "Updated successfully !";
    public static final String UPDATE_FAIL = "Updation has failed !";
    public static final String DELETE_SUCCESS = "Deleted successfully !";
    public static final String DELETE_FAIL = "Deletion has failed !";
    public static final String DB_REFRESH = "Data have been refreshed !";
    public static final String DB_RESET = "Data have been reset !";

    // Declare some regex constants
    public static final String PATTERN_DBNAME = "[A-Za-z0-9_]+";
    public static final String PATTERN_NAMENOSPACE = "[A-Za-z0-9]+";
    public static final String PATTERN_NAMEWITHSPACE = "[A-Za-z0-9 ]+";
    public static final String PATTERN_CUSNAME = "[A-Za-z ]+";
    public static final String PATTERN_NUMBER = "\\d+";
    public static final String PATTERN_ADDRESS = "[A-Za-z0-9 .,\\/-]+";
    public static final String PATTERN_DATE = "MMM dd, yyyy";
    public static final String PATTERN_HOST = "[A-Za-z0-9.]+";
    public static final String PATTERN_SERVICECONTENT = "[A-Za-z0-9.,-\\/<>% ]+";

    public enum FormatType {
        DATE, PERCENT, CURRENCY
    }

    public static String formatString(Object object, FormatType format) {
        String result = "";
        switch (format) {
            case DATE:
                SimpleDateFormat dateFormat = new SimpleDateFormat(PATTERN_DATE);
                result = dateFormat.format(object);
                break;
            case PERCENT:
                NumberFormat percentFormat = NumberFormat.getPercentInstance();
                result = percentFormat.format(object);
                break;
            case CURRENCY:
                result = String.format("%,.0f Đ", object);
                break;
        }
        return result;
    }

    public static Object unFormatString(String str, FormatType format) {
        switch (format) {
            case DATE:

                SimpleDateFormat dateFormat = new SimpleDateFormat(PATTERN_DATE);
                try {
                    return dateFormat.parse(str);
                } catch (ParseException ex) {
                    Logger.getLogger(SwingUtils.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case PERCENT:
                NumberFormat percentFormat = NumberFormat.getPercentInstance();
                try {
                    return percentFormat.parse(str);
                } catch (ParseException ex) {
                    Logger.getLogger(SwingUtils.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case CURRENCY:
                return Float.parseFloat(str.replaceAll(",", "").replace(" Đ", ""));
        }
        return null;
    }

    public static int showExitConfirmDialog(String message) {
        return JOptionPane.showConfirmDialog(null, message, "Confirm:", JOptionPane.YES_NO_CANCEL_OPTION);
    }
    
    public static int showConfirmDialog(String message) {
        return JOptionPane.showConfirmDialog(null, message, "Confirm:", JOptionPane.YES_NO_OPTION);
    }

    public static void showInfoDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Info:", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Error:", JOptionPane.ERROR_MESSAGE);
    }

    public static void showErrorDialog(Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage(), "Error:", JOptionPane.ERROR_MESSAGE);
    }

    public static int showInputValidationDialog(String message) {
        String[] options = {"Reinput", "Revert"};
        return JOptionPane.showOptionDialog(
                null,
                message,
                "Error:",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null,
                options,
                options[1]);    //Mac dinh la NO (or Revert)
    }

    public static void validateIntegerInput(JTextField tf) {
        AbstractDocument doc = new PlainDocument() {

            private static final long serialVersionUID = 1L;

            @Override
            public void setDocumentFilter(DocumentFilter filter) {

                if (filter instanceof IntegerDocumentFilter) {
                    super.setDocumentFilter(filter);
                }
            }
        };
        doc.setDocumentFilter(new IntegerDocumentFilter());
        tf.setDocument(doc);
    }

    public static void validateStringInput(JTextField tf, int minLength, int maxLength, String regex) {
        AbstractDocument abstractDocument = (AbstractDocument) tf.getDocument();
        abstractDocument.setDocumentFilter(new StringDocumentFilter(maxLength, regex));
        if (minLength > 0) {
            tf.setInputVerifier(new InputVerifier() {
                @Override
                public boolean verify(JComponent input) {
                    if (tf.getText().length() < minLength) {
                        SwingUtils.showErrorDialog("At least " + minLength + " character(s) !");
                        return false;
                    }
                    return true;
                }
            });
        }
    }
    
    public static void validateStringInput2(JTextField tf, int minLength, int maxLength, String regex) {
        AbstractDocument abstractDocument = (AbstractDocument) tf.getDocument();
        abstractDocument.setDocumentFilter(new StringDocumentFilter(maxLength, regex));
    }

    public static class IntegerDocumentFilter extends DocumentFilter {

        @Override
        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text.matches(PATTERN_NUMBER)) {
                super.replace(fb, offset, length, text, attrs);
            } else {
                SwingUtils.showErrorDialog("Invalid input!");
            }
        }
    }

    public static class StringDocumentFilter extends DocumentFilter {

        private int maxLength;
        private String regex;

        public StringDocumentFilter(int maxLength, String regex) {
            this.maxLength = maxLength;
            this.regex = regex;
        }

        @Override
        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text != null) {
                if (text.matches(regex)) {
                    int totalLength = fb.getDocument().getLength() + text.length();
                    if (totalLength <= maxLength) {
                        super.replace(fb, offset, length, text, attrs);
                    } else {
                        SwingUtils.showErrorDialog("Maximum length: " + maxLength + " characters!");
                    }
                } else {
                    Toolkit.getDefaultToolkit().beep();
                    SwingUtils.showErrorDialog("Invalid input!");
                }
            }
        }
    }

    public static void createLookAndFeel() {
        Properties props = new Properties();
        props.put("logoString", "");
        props.put("macStyleWindowDecoration", "on");
        props.put("macStyleScrollBar", "on");
        HiFiLookAndFeel.setTheme(props);
        try {
            UIManager.setLookAndFeel(new HiFiLookAndFeel());
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(SwingUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        UIManager.getLookAndFeelDefaults().put("TableHeader.foreground", Color.ORANGE);
        UIManager.getLookAndFeelDefaults().put("TableHeader.font", new Font("Calibri", Font.BOLD, 15));
    }
}
