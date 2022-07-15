package frame;

import helpers.Koneksi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Statement;
import javax.swing.table.TableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;


public class TableBukuViewFrame extends JFrame {
    private JPanel mainPanel;
    private JPanel cariPanel;
    private JScrollPane viewScrollTable;
    private JPanel buttonPanel;
    private JTextField cariTextField;
    private JButton cariButton;
    private JTable viewTable;
    private JButton tambahButton;
    private JButton ubahButton;
    private JButton hapusButton;
    private JButton batalButton;
    private JButton cetakButton;
    private JButton tutupButton;

    public TableBukuViewFrame(){
        tutupButton.addActionListener(e -> {
            dispose();
        });
        batalButton.addActionListener(e -> {
            isiTable();
        });
        tambahButton.addActionListener(e -> {
            TableBukuInputFrame inputFrame = new TableBukuInputFrame();
            inputFrame.setVisible(true);
        });
        addWindowListener(new WindowAdapter(){
            @Override
            public void windowActivated(WindowEvent e) {
                isiTable();
                init();
            }
        });
        cariButton.addActionListener(e -> {

            if (cariTextField.getText().equals("")) {
                JOptionPane.showMessageDialog(
                        null,
                        "Isi Kata Kunci Pencarian",
                        "Validasi Kata Kunci kosong",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            Connection c = Koneksi.getConnection();
            String keyword = "%" + cariTextField.getText() + "%";
            String searchSQL = "SELECT * FROM tbl_buku WHERE kode_buku like ?";
            try {
                PreparedStatement ps = c.prepareStatement(searchSQL);
                ps.setString(1, keyword);
                ResultSet rs = ps.executeQuery();
                DefaultTableModel dtm = (DefaultTableModel) viewTable.getModel();
                dtm.setRowCount(0);
                viewTable.setModel(dtm);
                Object[] row = new Object[4];
                while (rs.next()) {
                    row[0] = rs.getInt("kode_buku");
                    row[1] = rs.getString("judul");
                    row[2] = rs.getString("penulis");
                    row[3] = rs.getString("penerbit");
                    row[4] = rs.getString("harga");
                    dtm.addRow(row);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        hapusButton.addActionListener(e -> {
            int barisTerpilih = viewTable.getSelectedRow();
            if(barisTerpilih < 0){
                JOptionPane.showMessageDialog(null, "Pilih data dulu");
                return;
            }
            int pilihan = JOptionPane.showConfirmDialog( null, "Yakin mau hapus?", "Konfirmasi Hapus",JOptionPane.YES_NO_OPTION
            );
            if(pilihan == 0){
                TableModel tm = viewTable.getModel();
                int id = Integer.parseInt(tm.getValueAt(barisTerpilih, 0).toString());
                Connection c = Koneksi.getConnection();
                String deleteSQL = "DELETE FROM tbl_buku WHERE kode_buku = ?";
                try {
                    PreparedStatement ps = c.prepareStatement(deleteSQL);
                    ps.setInt( 1, id);
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        ubahButton.addActionListener(e -> {
            int barisTerpilih = viewTable.getSelectedRow();
            if(barisTerpilih < 0){
                JOptionPane.showMessageDialog(null,"Pilih data dulu");
                return;
            }
            TableModel tm = viewTable.getModel();
            int id = Integer.parseInt(tm.getValueAt(barisTerpilih,0).toString());
            TableBukuInputFrame inputFrame = new TableBukuInputFrame();
            inputFrame.setId(id);
            inputFrame.isiKomponen();
            inputFrame.setVisible(true);
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                isiTable();
            }
        });
        isiTable();
        init();
    }

    private void init() {
        setContentPane(mainPanel);
        setTitle("Data Buku");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }


        public void isiTable() {
            Connection c = Koneksi.getConnection();
            String selectSQL = "SELECT * FROM tbl_buku";
            try {
                Statement s = c.createStatement();
                ResultSet rs = s.executeQuery(selectSQL);

                String[] header = {"kode_buku","judul","penulis","penerbit","harga"};
                DefaultTableModel dtm = new DefaultTableModel(header, 0);
                viewTable.setModel(dtm);
                
                Object[] row = new Object[5];
                while (rs.next()) {
                    row[0] = rs.getInt("kode_buku");
                    row[1] = rs.getString("judul");
                    row[2] = rs.getString("penulis");
                    row[3] = rs.getString("penerbit");
                    row[4] = rs.getString("harga");
                    dtm.addRow(row);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

