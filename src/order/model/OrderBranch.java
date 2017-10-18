package order.model;

/**
 *
 * @author Hoang
 */
public class OrderBranch {
    private int braID;
    private String braName;
    private boolean braEnabled;

    public static final String COL_ID = "BraID";
    public static final String COL_NAME = "BraName";
    public static final String COL_ENABLED = "BraEnabled";
    
    public OrderBranch() {
    }

    public OrderBranch(int braID, String braName, boolean braEnabled) {
        this.braID = braID;
        this.braName = braName;
        this.braEnabled = braEnabled;
    }

    public int getBraID() {
        return braID;
    }

    public void setBraID(int braID) {
        this.braID = braID;
    }

    public String getBraName() {
        return braName;
    }

    public void setBraName(String braName) {
        this.braName = braName;
    }

    public boolean isBraEnabled() {
        return braEnabled;
    }

    public void setBraEnabled(boolean braEnabled) {
        this.braEnabled = braEnabled;
    }
}
