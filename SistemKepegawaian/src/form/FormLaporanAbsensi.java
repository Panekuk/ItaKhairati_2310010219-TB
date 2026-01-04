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

public class FormLaporanAbsensi extends javax.swing.JFrame {

    Connection conn = Koneksi.getKoneksi();
    DefaultTableModel model;

    public FormLaporanAbsensi() {
        initComponents();
        setLocationRelativeTo(null);
        tampilAbsensi();
    }

    // ================= LAPORAN ABSENSI =================
    private void tampilAbsensi() {
        model = new DefaultTableModel();
        model.addColumn("Nama Pegawai");
        model.addColumn("Tanggal");
        model.addColumn("Status");

        try {
        String sql = "SELECT p.nama, a.tanggal, a.status "
               + "FROM absensi a "
               + "JOIN pegawai p ON a.id_pegawai = p.id_pegawai";


            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("nama"),
                    rs.getDate("tanggal"),
                    rs.getString("status")
                });
            }

            tableLaporanAbsensi.setModel(model);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ================= EXPORT =================
    private void exportData() {
        if (model == null || model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "Tampilkan laporan absensi terlebih dahulu!");
            return;
        }

        String[] opsi = {"PDF", "Word (.doc)", "Excel (.xls)", "TXT"};
        int pilih = JOptionPane.showOptionDialog(
                this, "Pilih format file", "Export",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null, opsi, opsi[0]);

        if (pilih == -1) return;

        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        File file = fc.getSelectedFile();

        String kop = "LAPORAN ABSENSI PEGAWAI";
        String tanggal = "Tanggal: " + LocalDate.now();

        try {
    switch (pilih) {
        case 0:
            exportPDF(file, kop, tanggal);
            break;
        case 1:
            exportHTML(file, kop, tanggal, ".doc");
            break;
        case 2:
            exportHTML(file, kop, tanggal, ".xls");
            break;
        case 3:
            exportTXT(file, kop, tanggal);
            break;
        default:
            JOptionPane.showMessageDialog(this, "Pilihan tidak valid");
    }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ================= PDF =================
    private void exportPDF(File file, String kop, String tanggal) throws Exception {
        Document doc = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(doc, new FileOutputStream(file + ".pdf"));
        doc.open();

        doc.add(new Paragraph(kop,
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
        doc.add(new Paragraph(tanggal));
        doc.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(model.getColumnCount());
        for (int i = 0; i < model.getColumnCount(); i++) {
            table.addCell(model.getColumnName(i));
        }

        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                table.addCell(String.valueOf(model.getValueAt(i, j)));
            }
        }

        doc.add(table);
        doc.close();

        JOptionPane.showMessageDialog(this, "Export PDF berhasil");
    }

    // ================= WORD & EXCEL =================
    private void exportHTML(File file, String kop, String tanggal, String ext) throws Exception {
        BufferedWriter bw = new BufferedWriter(new FileWriter(file + ext));

        bw.write("<html><body>");
        bw.write("<h3 align='center'>" + kop + "</h3>");
        bw.write("<p align='center'>" + tanggal + "</p>");
        bw.write("<table border='1' width='100%'>");

        bw.write("<tr>");
        for (int i = 0; i < model.getColumnCount(); i++) {
            bw.write("<th>" + model.getColumnName(i) + "</th>");
        }
        bw.write("</tr>");

        for (int i = 0; i < model.getRowCount(); i++) {
            bw.write("<tr>");
            for (int j = 0; j < model.getColumnCount(); j++) {
                bw.write("<td>" + model.getValueAt(i, j) + "</td>");
            }
            bw.write("</tr>");
        }

        bw.write("</table></body></html>");
        bw.close();

        JOptionPane.showMessageDialog(this, "Export " + ext + " berhasil");
    }

    // ================= TXT =================
    private void exportTXT(File file, String kop, String tanggal) throws Exception {
        BufferedWriter bw = new BufferedWriter(new FileWriter(file + ".txt"));

        bw.write(kop);
        bw.newLine();
        bw.write(tanggal);
        bw.newLine();
        bw.newLine();

        for (int i = 0; i < model.getColumnCount(); i++) {
            bw.write(model.getColumnName(i) + "\t");
        }
        bw.newLine();

        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                bw.write(model.getValueAt(i, j) + "\t");
            }
            bw.newLine();
        }

        bw.close();
        JOptionPane.showMessageDialog(this, "Export TXT berhasil");
    }

    // ================= IMPORT =================
    private void importDataSimple() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File file = fc.getSelectedFile();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            model = new DefaultTableModel();
            boolean header = true;
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()
                        || line.startsWith("LAPORAN")
                        || line.startsWith("Tanggal")) continue;

                String[] data = line.split("\t|,");
                if (header) {
                    for (String col : data) model.addColumn(col);
                    header = false;
                } else {
                    model.addRow(data);
                }
            }

            tableLaporanAbsensi.setModel(model);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblLaporanAbsensi = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnLaporanAbsensi = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableLaporanAbsensi = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        btnImportAbsensi = new javax.swing.JButton();
        btnExportAbsensi = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lblLaporanAbsensi.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblLaporanAbsensi.setText("LAPORAN ABSENSI ");

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnLaporanAbsensi.setBackground(new java.awt.Color(204, 255, 204));
        btnLaporanAbsensi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnLaporanAbsensi.setText("Laporan Absensi ");
        btnLaporanAbsensi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLaporanAbsensiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(btnLaporanAbsensi, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnLaporanAbsensi)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tableLaporanAbsensi.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tableLaporanAbsensi);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnImportAbsensi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnImportAbsensi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/form/Icons/import.png"))); // NOI18N
        btnImportAbsensi.setText("Import");
        btnImportAbsensi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportAbsensiActionPerformed(evt);
            }
        });

        btnExportAbsensi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnExportAbsensi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/form/Icons/export.png"))); // NOI18N
        btnExportAbsensi.setText("Export");
        btnExportAbsensi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportAbsensiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(btnImportAbsensi)
                .addGap(43, 43, 43)
                .addComponent(btnExportAbsensi)
                .addContainerGap(46, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnImportAbsensi)
                    .addComponent(btnExportAbsensi))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/form/Icons/exit.png"))); // NOI18N
        btnExit.setText("Exit");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(151, 151, 151)
                                .addComponent(lblLaporanAbsensi))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(61, 61, 61)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnExit)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblLaporanAbsensi)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(btnExit)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnImportAbsensiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportAbsensiActionPerformed
        importDataSimple();
    }//GEN-LAST:event_btnImportAbsensiActionPerformed

    private void btnExportAbsensiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportAbsensiActionPerformed
        exportData();
    }//GEN-LAST:event_btnExportAbsensiActionPerformed

    private void btnLaporanAbsensiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLaporanAbsensiActionPerformed
      tampilAbsensi();
    }//GEN-LAST:event_btnLaporanAbsensiActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
         dispose(); // tutup form
    }//GEN-LAST:event_btnExitActionPerformed

    /**
     * @param args the command line arguments
     */
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnExportAbsensi;
    private javax.swing.JButton btnImportAbsensi;
    private javax.swing.JButton btnLaporanAbsensi;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblLaporanAbsensi;
    private javax.swing.JTable tableLaporanAbsensi;
    // End of variables declaration//GEN-END:variables
}
