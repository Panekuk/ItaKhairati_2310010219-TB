package form;

import koneksi.Koneksi;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;

// OpenPDF
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

// Apache POI
import org.apache.poi.xwpf.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

public class FormLaporan extends javax.swing.JFrame {
    
     Connection conn = Koneksi.getKoneksi();

    public FormLaporan() {
        initComponents();
        setLocationRelativeTo(null);
    }
// ================= LAPORAN ABSENSI =================
    private void tampilAbsensi() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Nama");
        model.addColumn("Tanggal");
        model.addColumn("Status");

        try {
            String sql = "SELECT nama, tanggal, status "
                       + "FROM absensi a JOIN pegawai p "
                       + "ON a.id_pegawai = p.id_pegawai";

            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3)
                });
            }
            tableLaporan.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }
    
// ================= LAPORAN CUTI =================
    private void tampilCuti() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Nama");
        model.addColumn("Mulai");
        model.addColumn("Selesai");
        model.addColumn("Jenis");

        try {
            String sql = "SELECT nama, tgl_mulai, tgl_selesai, jenis_cuti "
                       + "FROM cuti c JOIN pegawai p "
                       + "ON c.id_pegawai = p.id_pegawai";

            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4)
                });
            }
            tableLaporan.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }
// ================= EXPORT =================
    private void exportData() {
    String[] pilihan = {"PDF", "Word", "Excel", "TXT"};
    int pilih = JOptionPane.showOptionDialog(
            this,
            "Pilih format export",
            "Export Laporan",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            pilihan,
            pilihan[0]
    );

    if (pilih == -1) return; // batal

    JFileChooser fc = new JFileChooser();
    int hasil = fc.showSaveDialog(this);
    if (hasil != JFileChooser.APPROVE_OPTION) return;
    File file = fc.getSelectedFile();

    try {
        DefaultTableModel model = (DefaultTableModel) tableLaporan.getModel();
        String kop = "LAPORAN ABSENSI & CUTI PEGAWAI";
        String tanggal = "Tanggal Export: " + java.time.LocalDate.now();

        switch (pilih) {
           // ================= PDF =================
case 0:
    com.lowagie.text.Document document =
            new com.lowagie.text.Document(PageSize.A4, 36, 36, 54, 36);
    PdfWriter.getInstance(document, new FileOutputStream(file + ".pdf"));
    document.open();

    // ===== FONT PDF (SATU KALI, AMAN) =====
    com.lowagie.text.Font fontKop =
            FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);

    com.lowagie.text.Font fontTanggal =
            FontFactory.getFont(FontFactory.HELVETICA, 12);

    com.lowagie.text.Font fontHeader =
            FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);

    com.lowagie.text.Font fontData =
            FontFactory.getFont(FontFactory.HELVETICA, 11);

    // ===== KOP =====
    Paragraph header = new Paragraph(kop, fontKop);
    header.setAlignment(Element.ALIGN_CENTER);
    document.add(header);

    // ===== TANGGAL =====
    Paragraph subHeader = new Paragraph(tanggal, fontTanggal);
    subHeader.setAlignment(Element.ALIGN_CENTER);
    subHeader.setSpacingAfter(15f);
    document.add(subHeader);

    // ===== TABEL =====
    PdfPTable pdfTable = new PdfPTable(model.getColumnCount());
    pdfTable.setWidthPercentage(100);
    pdfTable.setSpacingBefore(10f);

    // Header tabel
    for (int i = 0; i < model.getColumnCount(); i++) {
        PdfPCell cell =
                new PdfPCell(new Phrase(model.getColumnName(i), fontHeader));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(6f);
        pdfTable.addCell(cell);
    }

    // Data tabel
    for (int i = 0; i < model.getRowCount(); i++) {
        for (int j = 0; j < model.getColumnCount(); j++) {
            PdfPCell cell =
                    new PdfPCell(new Phrase(
                            model.getValueAt(i, j).toString(),
                            fontData
                    ));
            cell.setPadding(6f);
            pdfTable.addCell(cell);
        }
    }

    document.add(pdfTable);
    document.close();

    JOptionPane.showMessageDialog(this, "Export PDF berhasil!");
    break;

            // ================= Word =================
            case 1:
                XWPFDocument doc = new XWPFDocument();

                // Kop
                XWPFParagraph pKop = doc.createParagraph();
                pKop.setAlignment(ParagraphAlignment.CENTER);
                XWPFRun runKop = pKop.createRun();
                runKop.setBold(true);
                runKop.setFontSize(16);
                runKop.setText(kop);

                // Tanggal
                XWPFParagraph pTanggal = doc.createParagraph();
                pTanggal.setAlignment(ParagraphAlignment.CENTER);
                XWPFRun runTanggal = pTanggal.createRun();
                runTanggal.setFontSize(12);
                runTanggal.setText(tanggal);
                pTanggal.setSpacingAfter(200);

                // Tabel
                XWPFTable table = doc.createTable();

                // Header tabel
                XWPFTableRow headerRow = table.getRow(0);
                for (int i = 0; i < model.getColumnCount(); i++) {
                    XWPFTableCell cell;
                    if (i == 0) {
                        cell = headerRow.getCell(0);
                    } else {
                        cell = headerRow.addNewTableCell();
                    }
                    cell.setText(model.getColumnName(i));
                    XWPFParagraph para = cell.getParagraphs().get(0);
                    para.setAlignment(ParagraphAlignment.CENTER);
                    XWPFRun run = para.createRun();
                    run.setBold(true);
                    cell.setColor("DCDCDC"); // shading abu-abu
                }

                // Data tabel
                for (int i = 0; i < model.getRowCount(); i++) {
                    XWPFTableRow row = table.createRow();
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        XWPFTableCell cell = row.getCell(j);
                        if (cell == null) cell = row.createCell();
                        cell.setText(model.getValueAt(i, j).toString());
                        XWPFParagraph para = cell.getParagraphs().get(0);
                        para.setAlignment(ParagraphAlignment.LEFT);
                    }
                }

                FileOutputStream fosWord = new FileOutputStream(file + ".docx");
                doc.write(fosWord);
                fosWord.close();
                doc.close();
                JOptionPane.showMessageDialog(this, "Export Word berhasil!");
                break;

            // ================= Excel =================
           case 2:
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Laporan");
            int rowIndex = 0;

            // ===== STYLE KOP =====
             XSSFCellStyle styleKop = workbook.createCellStyle();
             XSSFFont fontKopExcel = workbook.createFont();
             fontKopExcel.setBold(true);
             fontKopExcel.setFontHeightInPoints((short)16);
             styleKop.setFont(fontKopExcel);
             styleKop.setAlignment(HorizontalAlignment.CENTER);

             // ===== STYLE TANGGAL =====
             XSSFCellStyle styleTanggal = workbook.createCellStyle();
             XSSFFont fontTanggalExcel = workbook.createFont();
             fontTanggalExcel.setFontHeightInPoints((short)12);
             styleTanggal.setFont(fontTanggalExcel);
             styleTanggal.setAlignment(HorizontalAlignment.CENTER);

            // ===== STYLE HEADER =====
             XSSFCellStyle styleHeader = workbook.createCellStyle();
             XSSFFont fontHeaderExcel = workbook.createFont();
            fontHeaderExcel.setBold(true);
            styleHeader.setFont(fontHeaderExcel);
            styleHeader.setAlignment(HorizontalAlignment.CENTER);
            styleHeader.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            styleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            styleHeader.setBorderBottom(BorderStyle.THIN);
            styleHeader.setBorderTop(BorderStyle.THIN);
            styleHeader.setBorderLeft(BorderStyle.THIN);
            styleHeader.setBorderRight(BorderStyle.THIN);

            // ===== KOP =====
            XSSFRow kopRow = sheet.createRow(rowIndex++);
            XSSFCell kopCell = kopRow.createCell(0);
            kopCell.setCellValue(kop);
            kopCell.setCellStyle(styleKop);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, model.getColumnCount() - 1));

             // ===== TANGGAL =====
            XSSFRow tanggalRow = sheet.createRow(rowIndex++);
            XSSFCell tanggalCell = tanggalRow.createCell(0);
            tanggalCell.setCellValue(tanggal);
            tanggalCell.setCellStyle(styleTanggal);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, model.getColumnCount() - 1));

             rowIndex++;
    
             // ===== HEADER TABEL =====
            XSSFRow rowHeader = sheet.createRow(rowIndex++);
             for (int i = 0; i < model.getColumnCount(); i++) {
             XSSFCell cell = rowHeader.createCell(i);
             cell.setCellValue(model.getColumnName(i));
             cell.setCellStyle(styleHeader);
             }

            // ===== DATA =====
            for (int i = 0; i < model.getRowCount(); i++) {
             XSSFRow row = sheet.createRow(rowIndex++);
             for (int j = 0; j < model.getColumnCount(); j++) {
            row.createCell(j).setCellValue(model.getValueAt(i, j).toString());
                 }
            }

            for (int i = 0; i < model.getColumnCount(); i++) {
             sheet.autoSizeColumn(i);
             }

            FileOutputStream fosExcel = new FileOutputStream(file + ".xlsx");
             workbook.write(fosExcel);
             fosExcel.close();
            workbook.close();

            JOptionPane.showMessageDialog(this, "Export Excel berhasil!");
         break;
            
            // ================= TXT =================
            case 3:
                BufferedWriter bw = new BufferedWriter(new FileWriter(file + ".txt"));

                // Kop dan tanggal
                bw.write(kop);
                bw.newLine();
                bw.write(tanggal);
                bw.newLine();
                bw.newLine();

                // Header tabel
                for (int i = 0; i < model.getColumnCount(); i++) {
                    bw.write(model.getColumnName(i) + "\t");
                }
                bw.newLine();

                // Garis pemisah
                for (int i = 0; i < model.getColumnCount(); i++) {
                    bw.write("--------\t");
                }
                bw.newLine();

                // Data tabel
                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        bw.write(model.getValueAt(i, j).toString() + "\t");
                    }
                    bw.newLine();
                }

                bw.close();
                JOptionPane.showMessageDialog(this, "Export TXT berhasil!");
                break;
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
}


    // ================= IMPORT =================
      private void importData() {
    JFileChooser fc = new JFileChooser();
    int hasil = fc.showOpenDialog(this);
    if (hasil != JFileChooser.APPROVE_OPTION) return;
    File file = fc.getSelectedFile();

    String fileName = file.getName().toLowerCase();
    DefaultTableModel model = new DefaultTableModel();

    try {
        if (fileName.endsWith(".txt")) {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            boolean headerDone = false;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue; // skip baris kosong
                if (line.startsWith("LAPORAN") || line.startsWith("Tanggal Export")) continue; // skip kop & tanggal
                if (line.startsWith("--------")) continue; // skip garis pemisah

                String[] data = line.split("\t");

                if (!headerDone) {
                    // baris pertama yang tersisa dianggap header
                    for (String col : data) model.addColumn(col);
                    headerDone = true;
                } else {
                    // baris data
                    model.addRow(data);
                }
            }
            br.close();

        } else if (fileName.endsWith(".xlsx")) {
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            boolean headerDone = false;

            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                XSSFRow row = sheet.getRow(i);
                if (row == null) continue;

                // Lewati baris kop & tanggal (asumsikan 2 baris pertama)
                if (i < 2) continue;

                // Baris header tabel
                if (!headerDone) {
                    for (int j = 0; j < row.getLastCellNum(); j++) {
                        model.addColumn(row.getCell(j).toString());
                    }
                    headerDone = true;
                } else {
                    Object[] rowData = new Object[row.getLastCellNum()];
                    for (int j = 0; j < row.getLastCellNum(); j++) {
                        XSSFCell cell = row.getCell(j);
                        rowData[j] = (cell == null ? "" : cell.toString());
                    }
                    model.addRow(rowData);
                }
            }
            workbook.close();

        } else {
            JOptionPane.showMessageDialog(this, "Format file tidak didukung!");
            return;
        }

        tableLaporan.setModel(model);
        JOptionPane.showMessageDialog(this, "Import berhasil dari file: " + file.getName());

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblLaporan = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnLaporanAbsensi = new javax.swing.JButton();
        btnLaporanCuti = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableLaporan = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        btnImport = new javax.swing.JButton();
        btnExport = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lblLaporan.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblLaporan.setText("LAPORAN ABSENSI & CUTI");

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnLaporanAbsensi.setBackground(new java.awt.Color(204, 255, 204));
        btnLaporanAbsensi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnLaporanAbsensi.setText("Laporan Absensi ");
        btnLaporanAbsensi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLaporanAbsensiActionPerformed(evt);
            }
        });

        btnLaporanCuti.setBackground(new java.awt.Color(255, 102, 102));
        btnLaporanCuti.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnLaporanCuti.setText("Laporan Cuti");
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
                .addGap(44, 44, 44)
                .addComponent(btnLaporanAbsensi)
                .addGap(50, 50, 50)
                .addComponent(btnLaporanCuti)
                .addContainerGap(95, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLaporanAbsensi)
                    .addComponent(btnLaporanCuti))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tableLaporan.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tableLaporan);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnImport.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnImport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/form/Icons/import.png"))); // NOI18N
        btnImport.setText("Import");
        btnImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportActionPerformed(evt);
            }
        });

        btnExport.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnExport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/form/Icons/export.png"))); // NOI18N
        btnExport.setText("Export");
        btnExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(btnImport)
                .addGap(43, 43, 43)
                .addComponent(btnExport)
                .addContainerGap(46, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnImport)
                    .addComponent(btnExport))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(93, 93, 93)
                        .addComponent(lblLaporan))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblLaporan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(41, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportActionPerformed
        importData();
    }//GEN-LAST:event_btnImportActionPerformed

    private void btnExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportActionPerformed
        exportData();
    }//GEN-LAST:event_btnExportActionPerformed

    private void btnLaporanAbsensiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLaporanAbsensiActionPerformed
      tampilAbsensi();
    }//GEN-LAST:event_btnLaporanAbsensiActionPerformed

    private void btnLaporanCutiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLaporanCutiActionPerformed
        tampilCuti();
    }//GEN-LAST:event_btnLaporanCutiActionPerformed

    /**
     * @param args the command line arguments
     */
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExport;
    private javax.swing.JButton btnImport;
    private javax.swing.JButton btnLaporanAbsensi;
    private javax.swing.JButton btnLaporanCuti;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblLaporan;
    private javax.swing.JTable tableLaporan;
    // End of variables declaration//GEN-END:variables
}
