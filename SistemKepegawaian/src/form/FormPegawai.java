package form;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;
import javax.swing.border.Border;
import java.awt.Color;
import javax.swing.BorderFactory;
import koneksi.Koneksi;

public class FormPegawai extends javax.swing.JFrame {

    public FormPegawai() {
        initComponents();
        loadJabatan();
        setUnifiedInputValidation();
        tampilData();
    }

    // ================== VALIDASI INPUT ==================
    private void setUnifiedInputValidation() {

        Border normal = txtNip.getBorder();
        Border error  = BorderFactory.createLineBorder(Color.RED);

        ((AbstractDocument) txtNip.getDocument())
                .setDocumentFilter(new RegexFilter("\\d*", 20, txtNip, normal, error));

        ((AbstractDocument) txtNama.getDocument())
                .setDocumentFilter(new RegexFilter("[a-zA-Z ]*", 100, txtNama, normal, error));

        ((AbstractDocument) txtNoKontak.getDocument())
                .setDocumentFilter(new RegexFilter("\\d*", 15, txtNoKontak, normal, error));
    }

    // ================== LOAD JABATAN ==================
    private void loadJabatan() {
        try {
            cbJabatan.removeAllItems();
            String sql = "SELECT id_jabatan, nama_jabatan FROM jabatan";
            Statement st = Koneksi.getKoneksi().createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                cbJabatan.addItem(
                        rs.getInt("id_jabatan") + " - " + rs.getString("nama_jabatan")
                );
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private int getSelectedIdJabatan() {
        String item = cbJabatan.getSelectedItem().toString();
        return Integer.parseInt(item.split(" - ")[0]);
    }

    // ================== TAMPIL DATA ==================
    private void tampilData() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("No");
        model.addColumn("NIP");
        model.addColumn("Nama");
        model.addColumn("Alamat");
        model.addColumn("No Kontak");

        try {
            int no = 1;
            String sql = "SELECT * FROM pegawai";
            Statement st = Koneksi.getKoneksi().createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                model.addRow(new Object[]{
                        no++,
                        rs.getString("nip"),
                        rs.getString("nama"),
                        rs.getString("alamat"),
                        rs.getString("no_kontak")
                });
            }
            tablePegawai.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void resetForm() {
        txtNip.setText("");
        txtNama.setText("");
        areaAlamat.setText("");
        txtNoKontak.setText("");
        txtSearch.setText("");
        cbJabatan.setSelectedIndex(0);
        txtNip.requestFocus();
        tampilData();
    }
                                             
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lblNip = new javax.swing.JLabel();
        lblNama = new javax.swing.JLabel();
        txtNip = new javax.swing.JTextField();
        txtNama = new javax.swing.JTextField();
        lblNoKontak = new javax.swing.JLabel();
        txtNoKontak = new javax.swing.JTextField();
        cbJabatan = new javax.swing.JComboBox<>();
        lblJabatan = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnSave = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablePegawai = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        btnRefresh = new javax.swing.JButton();
        btnSearch = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        cbKategori = new javax.swing.JComboBox<>();
        txtSearch = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        lblAlamat = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        areaAlamat = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setText("Form Pegawai");

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblNip.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblNip.setText("NIP");

        lblNama.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblNama.setText("Nama");

        txtNip.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtNama.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        lblNoKontak.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblNoKontak.setText("No. Kontak");

        txtNoKontak.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        cbJabatan.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbJabatan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbJabatanActionPerformed(evt);
            }
        });

        lblJabatan.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblJabatan.setText("Jabatan");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNoKontak)
                    .addComponent(lblNama)
                    .addComponent(lblNip)
                    .addComponent(lblJabatan))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbJabatan, 0, 178, Short.MAX_VALUE)
                    .addComponent(txtNip, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtNama, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtNoKontak, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNip)
                    .addComponent(txtNip, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblNama)
                        .addGap(14, 14, 14)
                        .addComponent(lblNoKontak)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNoKontak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbJabatan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblJabatan))
                        .addGap(24, 24, 24))))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/form/Icons/save.png"))); // NOI18N
        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        jPanel2.add(btnSave);

        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/form/Icons/update.png"))); // NOI18N
        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        jPanel2.add(btnUpdate);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/form/Icons/delete.png"))); // NOI18N
        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        jPanel2.add(btnDelete);

        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/form/Icons/exit.png"))); // NOI18N
        btnExit.setText("Exit");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });
        jPanel2.add(btnExit);

        tablePegawai.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tablePegawai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablePegawaiMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablePegawai);

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/form/Icons/refresh.png"))); // NOI18N
        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/form/Icons/search_icon-icons.com_52389.png"))); // NOI18N
        btnSearch.setText("Search");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        jLabel6.setText("Kategori");

        cbKategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "NIP", "Nama", "Alamat", "No. Kontak", "Jabatan", " " }));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(btnSearch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRefresh)
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbKategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(btnSearch)
                    .addComponent(btnRefresh))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblAlamat.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblAlamat.setText("Alamat");

        areaAlamat.setColumns(20);
        areaAlamat.setRows(5);
        jScrollPane2.setViewportView(areaAlamat);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblAlamat)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAlamat)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(20, 20, 20))))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGap(218, 218, 218)
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
    // Validasi NIP angka saja
if (!txtNip.getText().matches("\\d+")) {
    JOptionPane.showMessageDialog(this, "NIP hanya boleh angka!");
    txtNip.requestFocus();
    return;
}

// Validasi Nama hanya huruf
if (!txtNama.getText().matches("[a-zA-Z ]+")) {
    JOptionPane.showMessageDialog(this, "Nama hanya boleh huruf!");
    txtNama.requestFocus();
    return;
}

// Validasi No Kontak angka saja
if (!txtNoKontak.getText().matches("\\d+")) {
    JOptionPane.showMessageDialog(this, "No Kontak hanya boleh angka tanpa titik atau koma!");
    txtNoKontak.requestFocus();
    return;
}
        if (txtNip.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "NIP tidak boleh kosong!");
        txtNip.requestFocus();
        return;
    }

    if (txtNama.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Nama tidak boleh kosong!");
        txtNama.requestFocus();
        return;
    }

    try {
        String sql = "INSERT INTO pegawai (nip, nama, alamat, no_kontak, id_jabatan) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pst = Koneksi.getKoneksi().prepareStatement(sql);

        pst.setString(1, txtNip.getText());
        pst.setString(2, txtNama.getText());
        pst.setString(3, areaAlamat.getText());
        pst.setString(4, txtNoKontak.getText());
        pst.setInt(5, getSelectedIdJabatan());

        pst.executeUpdate();
        JOptionPane.showMessageDialog(this, "Data pegawai berhasil disimpan");

        tampilData();
        resetForm();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage());
    }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
    // Validasi NIP angka saja
if (!txtNip.getText().matches("\\d+")) {
    JOptionPane.showMessageDialog(this, "NIP hanya boleh angka!");
    txtNip.requestFocus();
    return;
}

// Validasi Nama hanya huruf
if (!txtNama.getText().matches("[a-zA-Z ]+")) {
    JOptionPane.showMessageDialog(this, "Nama hanya boleh huruf!");
    txtNama.requestFocus();
    return;
}

// Validasi No Kontak angka saja
if (!txtNoKontak.getText().matches("\\d+")) {
    JOptionPane.showMessageDialog(this, "No Kontak hanya boleh angka tanpa titik atau koma!");
    txtNoKontak.requestFocus();
    return;
}    
        try {
        String sql = "UPDATE pegawai SET nama=?, alamat=?, no_kontak=?, id_jabatan=? WHERE nip=?";
        Connection conn = Koneksi.getKoneksi();
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, txtNama.getText());
        pst.setString(2, areaAlamat.getText());
        pst.setString(3, txtNoKontak.getText());
        pst.setInt(4, getSelectedIdJabatan());
        pst.setString(5, txtNip.getText()); 
        pst.execute();
        JOptionPane.showMessageDialog(null, "Data Berhasil Diubah");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Perubahan Data Gagal " + e.getMessage());
    }
    tampilData();
    resetForm();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
       int jawab = JOptionPane.showConfirmDialog(null, "Hapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
    if(jawab == JOptionPane.YES_OPTION){
        try {
            String sql = "DELETE FROM pegawai WHERE nip=?";
            Connection conn = Koneksi.getKoneksi();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, txtNip.getText());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        tampilData();
        resetForm();
    }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
      dispose();
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        DefaultTableModel model = new DefaultTableModel();
    model.addColumn("No"); 
    model.addColumn("NIP"); 
    model.addColumn("Nama");
    model.addColumn("Alamat"); 
    model.addColumn("No. Kontak");

    String kategori = cbKategori.getSelectedItem().toString();
    String kolomDB = "";
    
    // Mapping kategori ComboBox ke nama kolom di Database
    switch (kategori) {
        case "NIP": kolomDB = "nip"; break;
        case "Nama": kolomDB = "nama"; break;
        case "No. Kontak": kolomDB = "no_kontak"; break;
        case "Alamat": kolomDB = "alamat"; break;
        default: kolomDB = "nama";
    }

    try {
        int no = 1;
        // Menggunakan PreparedStatement untuk keamanan
        String sql = "SELECT * FROM pegawai WHERE " + kolomDB + " LIKE ?";
        Connection conn = Koneksi.getKoneksi();
        PreparedStatement pst = conn.prepareStatement(sql);
        
        // Mengisi parameter LIKE dengan format %nilai%
        pst.setString(1, "%" + txtSearch.getText() + "%");
        
        ResultSet res = pst.executeQuery();
        while(res.next()){
            model.addRow(new Object[]{
                no++, 
                res.getString("nip"), 
                res.getString("nama"), 
                res.getString("alamat"), 
                res.getString("no_kontak")
            });
        }
        tablePegawai.setModel(model);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Pencarian Gagal: " + e.getMessage());
    }
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
    resetForm();
    txtSearch.setText("");
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void tablePegawaiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablePegawaiMouseClicked
    int baris = tablePegawai.rowAtPoint(evt.getPoint());
        txtNip.setText(tablePegawai.getValueAt(baris, 1).toString());
        txtNama.setText(tablePegawai.getValueAt(baris, 2).toString());
        areaAlamat.setText(tablePegawai.getValueAt(baris, 3).toString());
        txtNoKontak.setText(tablePegawai.getValueAt(baris, 4).toString());
    }//GEN-LAST:event_tablePegawaiMouseClicked

    private void cbJabatanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbJabatanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbJabatanActionPerformed

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea areaAlamat;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cbJabatan;
    private javax.swing.JComboBox<String> cbKategori;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblAlamat;
    private javax.swing.JLabel lblJabatan;
    private javax.swing.JLabel lblNama;
    private javax.swing.JLabel lblNip;
    private javax.swing.JLabel lblNoKontak;
    private javax.swing.JTable tablePegawai;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtNip;
    private javax.swing.JTextField txtNoKontak;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
