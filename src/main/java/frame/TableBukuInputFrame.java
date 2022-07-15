package frame;

import helpers.Koneksi;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TableBukuInputFrame extends JFrame {
    private JPanel mainPanel;
    private JTextField kodebukuTextField;
    private JTextField judulTextField;
    private JTextField penulisTextField;
    private JTextField penerbitTextField;
    private JTextField hargaTextField;
    private JPanel buttonPanel;
    private JButton batalButton;
    private int kode_buku;
    private JButton simpanButton;

    public TableBukuInputFrame() {
        batalButton.addActionListener(e -> {
            dispose();
        });
        simpanButton.addActionListener(e -> {
            String kode_buku = kodebukuTextField.getText();
            String judul = judulTextField.getText();
            String penulis = penulisTextField.getText();
            String penerbit = penerbitTextField.getText();
            String harga = hargaTextField.getText();
            Connection c = Koneksi.getConnection();
            PreparedStatement ps;
            if (judul.equals("")) {
                JOptionPane.showMessageDialog(
                        null,
                        "Isi data kode buku",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE
                );
                kodebukuTextField.requestFocus();
                return;
            }
            try {
                String cekSQL;
                if (this.kode_buku == 0) { //jika TAMBAH

                    cekSQL = "SELECT * FROM tbl_buku WHERE judul=?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, kode_buku);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) { // kalau ADA
                        JOptionPane.showMessageDialog(
                                null,
                                "judul sama sudah ada",
                                "Validasi data sama",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }
                    String insertSQL = "INSERT INTO tbl_buku (kode_buku,judul,penulis,penerbit,harga) VALUES (NULL, ?, ?, ?)";
                    insertSQL = "INSERT INTO `tbl_buku` (`kode_buku`, `judul`, `penulis`, `penerbit`, `harga`) VALUES (NULL, ?)";
                    insertSQL = "INSERT INTO `tbl_buku` VALUES (NULL, ?)";
                    insertSQL = "INSERT INTO tbl_buku (tbl_buku,tbl_penjualan) VALUES (?)";
                    insertSQL = "INSERT INTO tbl_buku SET judul=?, penulis=?, penerbit=?, harga=?";
                    ps = c.prepareStatement(insertSQL);
                    ps.setString(1, judul);
                    ps.setString(2, penulis);
                    ps.setString(3, penerbit);
                    ps.setString(4, harga);
                    ps.executeUpdate();
                    dispose();
                } else {
                    cekSQL = "SELECT * FROM tbl_buku WHERE judul=? AND penulis=?, AND penerbit=?, AND harga=? AND kode_buku!=?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, judul);
                    ps.setString(2, penulis);
                    ps.setString(3, penerbit);
                    ps.setString(4, harga);
                    ps.setInt(5, Integer.parseInt(kode_buku));
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) { // kalau ADA
                        JOptionPane.showMessageDialog(
                                null,
                                "Judul sama sudah ada",
                                "Validasi data sama",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }
                    String updateSQL = "UPDATE kode_buku SET judul=?,penulis=?, penerbit=?, harga=? WHERE kode_nama=?";
                    ps = c.prepareStatement(updateSQL);
                    ps.setString(1, judul);
                    ps.setString(2, penulis);
                    ps.setString(3, penerbit);
                    ps.setString(4, harga);
                    ps.setInt(5, Integer.parseInt(kode_buku));
                    ps.executeUpdate();
                    dispose();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        init();
    }

    private void init() {
        setContentPane(mainPanel);
        setTitle("input tbl_buku");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void isiKomponen(){
        Connection c = Koneksi.getConnection();
        String findSQL = "SELECT * FROM kasir WHERE id = ?";
        PreparedStatement ps = null;
        try {
            ps = c.prepareStatement(findSQL);
            ps.setInt(1, kode_buku);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                kodebukuTextField.setText(String.valueOf(rs.getInt("Kode Buku")));
                judulTextField.setText(rs.getString("Judul"));
                penulisTextField.setText(rs.getString("Penulis"));
                penerbitTextField.setText(rs.getString("Penerbit"));
                hargaTextField.setText(rs.getString("Harga"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setId(int id) {


}
}



