package form;

import koneksi.Koneksi;
import java.sql.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;

// PDF (OpenPDF)
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

public class FormLaporanCuti extends javax.swing.JFrame {

    Connection conn = Koneksi.getKoneksi();
    DefaultTableModel model;

    public FormLaporanCuti() {
        initComponents();
        setLocationRelativeTo(null);
        tampilCuti(); // tampil otomatis
    }

    // ================= LAPORAN CUTI =================
    private void tampilCuti() {
        model = new DefaultTableModel();
        model.addColumn("Nama Pegawai");
        model.addColumn("Tanggal Mulai");
        model.addColumn("Tanggal Selesai");
        model.addColumn("Jenis Cuti");
        model.addColumn("Keterangan");

        try {
            String sql =
                "SELECT p.nama, c.tgl_mulai, c.tgl_selesai, c.jenis_cuti, c.keterangan " +
                "FROM cuti c JOIN pegawai p ON c.id_pegawai = p.id_pegawai";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("nama"),
                    rs.getDate("tgl_mulai"),
                    rs.getDate("tgl_selesai"),
                    rs.getString("jenis_cuti"),
                    rs.getString("keterangan")
                });
            }

            tableLaporanCuti.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal menampilkan data: " + e.getMessage());
        }
    }

    // ================= EXPORT =================
    private void exportData() {
        String[] opsi = {"PDF", "Excel (.xls)", "TXT"};
        int pilih = JOptionPane.showOptionDialog(
                this, "Pilih format", "Export",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null, opsi, opsi[0]);

        if (pilih == -1) return;

        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        File file = fc.getSelectedFile();

        String judul = "LAPORAN CUTI PEGAWAI";
        String tanggal = "Tanggal: " + LocalDate.now();

        try {
            switch (pilih) {
                case 0:
                    exportPDF(file, judul, tanggal);
                    break;
                case 1:
                    exportHTML(file, judul, tanggal, ".xls");
                    break;
                case 2:
                    exportTXT(file, judul, tanggal);
                    break;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ================= PDF =================
    private void exportPDF(File file, String judul, String tanggal) throws Exception {
        Document doc = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(doc, new FileOutputStream(file + ".pdf"));
        doc.open();

        doc.add(new Paragraph(judul, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
        doc.add(new Paragraph(tanggal));
        doc.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(model.getColumnCount());

        for (int i = 0; i < model.getColumnCount(); i++)
            table.addCell(model.getColumnName(i));

        for (int i = 0; i < model.getRowCount(); i++)
            for (int j = 0; j < model.getColumnCount(); j++)
                table.addCell(String.valueOf(model.getValueAt(i, j)));

        doc.add(table);
        doc.close();

        JOptionPane.showMessageDialog(this, "PDF berhasil dibuat");
    }

    // ================= EXCEL (HTML) =================
    private void exportHTML(File file, String judul, String tanggal, String ext) throws Exception {
        BufferedWriter bw = new BufferedWriter(new FileWriter(file + ext));

        bw.write("<html><body>");
        bw.write("<h3 align='center'>" + judul + "</h3>");
        bw.write("<p align='center'>" + tanggal + "</p>");
        bw.write("<table border='1'>");

        bw.write("<tr>");
        for (int i = 0; i < model.getColumnCount(); i++)
            bw.write("<th>" + model.getColumnName(i) + "</th>");
        bw.write("</tr>");

        for (int i = 0; i < model.getRowCount(); i++) {
            bw.write("<tr>");
            for (int j = 0; j < model.getColumnCount(); j++)
                bw.write("<td>" + model.getValueAt(i, j) + "</td>");
            bw.write("</tr>");
        }

        bw.write("</table></body></html>");
        bw.close();

        JOptionPane.showMessageDialog(this, "Export Excel berhasil");
    }

    // ================= TXT =================
    private void exportTXT(File file, String judul, String tanggal) throws Exception {
        BufferedWriter bw = new BufferedWriter(new FileWriter(file + ".txt"));
        bw.write(judul); bw.newLine();
        bw.write(tanggal); bw.newLine(); bw.newLine();

        for (int i = 0; i < model.getColumnCount(); i++)
            bw.write(model.getColumnName(i) + "\t");
        bw.newLine();

        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++)
                bw.write(model.getValueAt(i, j) + "\t");
            bw.newLine();
        }

        bw.close();
        JOptionPane.showMessageDialog(this, "TXT berhasil dibuat");
    }
    
private void importDataSimple() {
    JFileChooser fc = new JFileChooser();
    if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;

    File file = fc.getSelectedFile();

    try {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;

        model = new DefaultTableModel();
        boolean header = true;

        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;

            String[] data = line.split("\t|,");

            if (header) {
                for (String col : data) {
                    model.addColumn(col);
                }
                header = false;
            } else {
                model.addRow(data);
            }
        }

        br.close();

        // ⭐ INI YANG WAJIB
        tableLaporanCuti.setModel(model);

        JOptionPane.showMessageDialog(this, "Import berhasil");

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Import gagal: " + e.getMessage());
    }
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tableLaporanCuti = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        btnImportCuti = new javax.swing.JButton();
        btnExportCuti = new javax.swing.JButton();
        lblLaporanCuti = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnLaporanCuti = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tableLaporanCuti.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tableLaporanCuti);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnImportCuti.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnImportCuti.setIcon(new javax.swing.ImageIcon(getClass().getResource("/form/Icons/import.png"))); // NOI18N
        btnImportCuti.setText("Import");
        btnImportCuti.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportCutiActionPerformed(evt);
            }
        });

        btnExportCuti.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnExportCuti.setIcon(new javax.swing.ImageIcon(getClass().getResource("/form/Icons/export.png"))); // NOI18N
        btnExportCuti.setText("Export");
        btnExportCuti.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportCutiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(btnImportCuti)
                .addGap(43, 43, 43)
                .addComponent(btnExportCuti)
                .addContainerGap(46, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnImportCuti)
                    .addComponent(btnExportCuti))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblLaporanCuti.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblLaporanCuti.setText("LAPORAN CUTI");

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnLaporanCuti.setBackground(new java.awt.Color(204, 255, 204));
        btnLaporanCuti.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnLaporanCuti.setText("Laporan Absensi ");
        btnLaporanCuti.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLaporanCutiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(btnLaporanCuti, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnLaporanCuti)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/form/Icons/exit.png"))); // NOI18N
        jButton1.setText("Exit");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(151, 151, 151)
                        .addComponent(lblLaporanCuti))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(19, 19, 19))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblLaporanCuti)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnImportCutiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportCutiActionPerformed
        importDataSimple();
    }//GEN-LAST:event_btnImportCutiActionPerformed

    private void btnExportCutiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportCutiActionPerformed
        exportData();
    }//GEN-LAST:event_btnExportCutiActionPerformed

    private void btnLaporanCutiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLaporanCutiActionPerformed
        tampilCuti();
    }//GEN-LAST:event_btnLaporanCutiActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExportCuti;
    private javax.swing.JButton btnImportCuti;
    private javax.swing.JButton btnLaporanCuti;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblLaporanCuti;
    private javax.swing.JTable tableLaporanCuti;
    // End of variables declaration//GEN-END:variables
}
