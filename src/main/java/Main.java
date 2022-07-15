import frame.TableBukuViewFrame;
import helpers.Koneksi;

public class Main {
    public static void main (String[] args) {
        Koneksi.getConnection();
        TableBukuViewFrame viewFrame = new TableBukuViewFrame();
        viewFrame.setVisible(true);
    }
}
