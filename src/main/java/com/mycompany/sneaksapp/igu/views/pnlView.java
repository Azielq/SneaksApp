package com.mycompany.sneaksapp.igu.views;
import com.mycompany.sneaksapp.igu.MenuPrincipal;
import com.mycompany.sneaksapp.igu.Work;
import com.mycompany.sneaksapp.igu.WorkInv;
import com.mycompany.sneaksapp.igu.WorkSell;
import com.mycompany.sneaksapp.logica.FrameControllable;
import com.mycompany.sneaksapp.logica.GestorZapatos;
import com.mycompany.sneaksapp.logica.Zapato;
import com.mycompany.sneaksapp.logica.ZapatoTableModel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import net.coobird.thumbnailator.Thumbnails;
import org.imgscalr.Scalr;


public class pnlView extends javax.swing.JPanel {

    private Work work;
    private WorkInv workInv;
    private WorkSell workSell;
    private FrameControllable currentFrame;
    
    
    // Define el archivo para guardar los datos
    private GestorZapatos gestorZapatos;
 
    
    public pnlView(FrameControllable frame) {
        this.currentFrame = frame;
        initComponents();
        InitStyles();
        gestorZapatos = new GestorZapatos();
        initializeTable();
        actualizarSubtotales();
        actualizarTotalZapatos();
        
        
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }

            private void filter() {
                TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>) tblData.getRowSorter();
                String text = txtSearch.getText();
                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(text)));
                }
            }
        });
    }
    
    public void actualizarSubtotales() {
        Map<String, Integer> subtotales = calcularSubtotales();
        actualizarTblSubtotals(subtotales);
    }
    
    private Map<String, Integer> calcularSubtotales() {
        Map<String, Integer> subtotales = new HashMap<>();
        for (Zapato zapato : gestorZapatos.getZapatos()) {
            String modelo = zapato.getModel();
            subtotales.put(modelo, subtotales.getOrDefault(modelo, 0) + 1);
        }
        return subtotales;
    }
    
    private void actualizarTblSubtotals(Map<String, Integer> subtotales) {
        DefaultTableModel modeloSubtotals = (DefaultTableModel) this.tblData2.getModel();
        modeloSubtotals.setRowCount(0); // Limpiar la tabla antes de agregar nuevas filas

        for (Map.Entry<String, Integer> entry : subtotales.entrySet()) {
            modeloSubtotals.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }
    }
    
    public void actualizarTotalZapatos() {
        int totalZapatos = gestorZapatos.getZapatos().size(); // Obtiene el total de zapatos
        lblTotalNum.setText(String.valueOf(totalZapatos)); // Establece el total en lblTotalNum
    }
    
    
    private void initializeTable() {
        // Get the list of shoes from the gestorZapatos and prepare the table model data
        List<Zapato> zapatos = gestorZapatos.getZapatos();
        // Las columnas incluidas en el modelo de datos (incluyendo aquellas que no se mostrarán directamente en la tabla)
        String[] columnNames = {"ID", "Brand", "Model", "Color", "Size", "Price $", "Image", "Location"};
        Object[][] data = new Object[zapatos.size()][8];

        for (int i = 0; i < zapatos.size(); i++) {
            Zapato zapato = zapatos.get(i);
            data[i][0] = zapato.getShoeid();
            data[i][1] = zapato.getBrand();
            data[i][2] = zapato.getModel();
            data[i][3] = zapato.getColor();    // Asumiendo que existe un getColor()
            data[i][4] = zapato.getSize();     // Asumiendo que existe un getSize()
            data[i][5] = zapato.getPrice();
            data[i][6] = zapato.getImage(); // Asumiendo que existe un getImagePath()
            data[i][7] = zapato.getUbication();
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        tblData.setModel(model);
        tblData.getTableHeader().setReorderingAllowed(false);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        tblData.setRowSorter(sorter);
        
        // Oculta las columnas no deseadas en la vista
        hideColumnsInView();

        tblData.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblData.setRowSelectionAllowed(true);
        tblData.setColumnSelectionAllowed(false);
        
        tblData.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int row = tblData.getSelectedRow();
                    displayShoeDetails(row);
                }
            }
        });
        
        tblData2.setModel(new DefaultTableModel(
                new Object[][]{}, // Los datos iniciales estarán vacíos
                new String[]{"Model", "Units"} // Nombres de columnas
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hace todas las celdas no editables
            }
        });

        tblData2.setRowSelectionAllowed(true); // Permite la selección de filas completas
        tblData2.setColumnSelectionAllowed(false); // Deshabilita la selección por columnas
        tblData2.getTableHeader().setReorderingAllowed(false);

        scrlTable.setViewportView(tblData2);
        tblData2.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        
    }
    
    private void hideColumnsInView() {
        // Ocultar columnas "Color", "Size", "Image" que están en las posiciones 3, 4 y 6 respectivamente
        tblData.getColumnModel().getColumn(3).setMinWidth(0);
        tblData.getColumnModel().getColumn(3).setMaxWidth(0);
        tblData.getColumnModel().getColumn(3).setWidth(0);

        tblData.getColumnModel().getColumn(4).setMinWidth(0);
        tblData.getColumnModel().getColumn(4).setMaxWidth(0);
        tblData.getColumnModel().getColumn(4).setWidth(0);

        tblData.getColumnModel().getColumn(6).setMinWidth(0);
        tblData.getColumnModel().getColumn(6).setMaxWidth(0);
        tblData.getColumnModel().getColumn(6).setWidth(0);
        
        tblData.getColumnModel().getColumn(7).setMinWidth(0);
        tblData.getColumnModel().getColumn(7).setMaxWidth(0);
        tblData.getColumnModel().getColumn(7).setWidth(0);
    }

    private void displayShoeDetails(int row) {
        lblShoeId.setText("ID: " + tblData.getValueAt(row, 0).toString());
        lblBrand.setText("Brand: " + tblData.getValueAt(row, 1).toString());
        lblModel.setText(tblData.getValueAt(row, 2).toString());
        lblColor.setText("Color: " + tblData.getValueAt(row, 3).toString());
        lblSize.setText("Size: " + tblData.getValueAt(row, 4).toString());
        lblPrice.setText("Price: $" + tblData.getValueAt(row, 5).toString());
        this.lblBrand1.setText("Location: " + tblData.getValueAt(row, 7).toString());
        
        String imagePath = "src/main/resources/" + tblData.getValueAt(row, 6).toString();
        File imgFile = new File(imagePath);
        if (!imgFile.exists()) {
            System.out.println("File does not exist: " + imgFile.getAbsolutePath());
            lblImage.setIcon(null);
            return;
        }

        try {
            BufferedImage bufferedImage = Thumbnails.of(imgFile)
                    .size(393, 285)
                    .asBufferedImage();
            lblImage.setIcon(new ImageIcon(bufferedImage));
        } catch (IOException e) {
            e.printStackTrace();
            lblImage.setIcon(null);
        }
    }
    
    private void InitStyles()
    {
        sbtnGoBack.setSvgImage("back arrow.svg", 40, 37);
        sbtnGoBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        Map<TextAttribute, Object> attributes = new HashMap<TextAttribute, Object>();
        attributes.put(TextAttribute.TRACKING, -0.06);
        lblEditInv.setFont(lblEditInv.getFont().deriveFont(attributes));
        
        Map<TextAttribute, Object> attributes1 = new HashMap<TextAttribute, Object>();
        attributes1.put(TextAttribute.TRACKING, -0.03);
        this.pnlShoes.setBackground(Color.decode("#b5b5b5"));
        this.lblShoeId.setForeground(Color.decode("#e0e0e0"));
            
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bg = new javax.swing.JPanel();
        lblEditInv = new javax.swing.JLabel();
        sbtnGoBack = new com.mycompany.sneaksapp.igu.SVGImage();
        pnlTable = new javax.swing.JPanel();
        scrlTable = new javax.swing.JScrollPane();
        tblData2 = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();
        scrlTable1 = new javax.swing.JScrollPane();
        tblData = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        pnlButton = new javax.swing.JPanel();
        lblSearch = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        pnlShoes = new javax.swing.JPanel();
        lblShoeId = new javax.swing.JLabel();
        lblModel = new javax.swing.JLabel();
        lblColor = new javax.swing.JLabel();
        lblSize = new javax.swing.JLabel();
        lblPrice = new javax.swing.JLabel();
        lblBrand = new javax.swing.JLabel();
        lblBrand1 = new javax.swing.JLabel();
        lblImage = new javax.swing.JLabel();
        pnlSubtotals = new javax.swing.JPanel();
        pnlTotal = new javax.swing.JPanel();
        lblTotalText = new javax.swing.JLabel();
        lblTotalNum = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();

        bg.setBackground(new java.awt.Color(255, 255, 255));
        bg.setPreferredSize(new java.awt.Dimension(947, 684));

        lblEditInv.setBackground(new java.awt.Color(0, 0, 0));
        lblEditInv.setFont(new java.awt.Font("Segoe UI Black", 1, 22)); // NOI18N
        lblEditInv.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEditInv.setText("View Products");

        sbtnGoBack.setText("sVGImage2");
        sbtnGoBack.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sbtnGoBackMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                sbtnGoBackMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                sbtnGoBackMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                sbtnGoBackMousePressed(evt);
            }
        });

        pnlTable.setBackground(new java.awt.Color(255, 255, 255));

        tblData2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Model", "Units"
            }
        ));
        tblData2.setColumnSelectionAllowed(true);
        tblData2.setGridColor(new java.awt.Color(255, 255, 255));
        tblData2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tblData2MouseMoved(evt);
            }
        });
        scrlTable.setViewportView(tblData2);
        tblData2.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));

        tblData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblData.setGridColor(new java.awt.Color(255, 255, 255));
        tblData.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tblDataMouseMoved(evt);
            }
        });
        scrlTable1.setViewportView(tblData);
        tblData.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        jLabel1.setFont(new java.awt.Font("Segoe UI Light", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Subtotals");

        javax.swing.GroupLayout pnlTableLayout = new javax.swing.GroupLayout(pnlTable);
        pnlTable.setLayout(pnlTableLayout);
        pnlTableLayout.setHorizontalGroup(
            pnlTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTableLayout.createSequentialGroup()
                .addGroup(pnlTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlTableLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(scrlTable1, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(scrlTable, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        pnlTableLayout.setVerticalGroup(
            pnlTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTableLayout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrlTable1, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scrlTable, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pnlButton.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pnlButtonLayout = new javax.swing.GroupLayout(pnlButton);
        pnlButton.setLayout(pnlButtonLayout);
        pnlButtonLayout.setHorizontalGroup(
            pnlButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 322, Short.MAX_VALUE)
        );
        pnlButtonLayout.setVerticalGroup(
            pnlButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 203, Short.MAX_VALUE)
        );

        lblSearch.setFont(new java.awt.Font("Segoe UI Light", 0, 27)); // NOI18N
        lblSearch.setText("Filter:");

        txtSearch.setFont(new java.awt.Font("Segoe UI Light", 0, 14)); // NOI18N
        txtSearch.setForeground(java.awt.Color.lightGray);
        txtSearch.setText("Type an attribute of a product...");
        txtSearch.setBorder(null);
        txtSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtSearchMouseClicked(evt);
            }
        });
        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });

        lblShoeId.setFont(new java.awt.Font("Segoe UI Light", 0, 24)); // NOI18N
        lblShoeId.setForeground(new java.awt.Color(204, 204, 204));
        lblShoeId.setText("ID: 1234");

        lblModel.setFont(new java.awt.Font("Segoe UI Light", 0, 52)); // NOI18N
        lblModel.setForeground(new java.awt.Color(255, 255, 255));
        lblModel.setText("Shoe Model");
        lblModel.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        lblColor.setFont(new java.awt.Font("Segoe UI Light", 0, 26)); // NOI18N
        lblColor.setForeground(new java.awt.Color(255, 255, 255));
        lblColor.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblColor.setText("Color:");

        lblSize.setFont(new java.awt.Font("Segoe UI Light", 0, 26)); // NOI18N
        lblSize.setForeground(new java.awt.Color(255, 255, 255));
        lblSize.setText("Size:");

        lblPrice.setFont(new java.awt.Font("Segoe UI Light", 0, 26)); // NOI18N
        lblPrice.setForeground(new java.awt.Color(255, 255, 255));
        lblPrice.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPrice.setText("Price: $");

        lblBrand.setFont(new java.awt.Font("Segoe UI Light", 0, 26)); // NOI18N
        lblBrand.setForeground(new java.awt.Color(255, 255, 255));
        lblBrand.setText("Brand:");

        lblBrand1.setFont(new java.awt.Font("Segoe UI Light", 0, 18)); // NOI18N
        lblBrand1.setForeground(new java.awt.Color(255, 255, 255));
        lblBrand1.setText("Location: ");

        javax.swing.GroupLayout pnlShoesLayout = new javax.swing.GroupLayout(pnlShoes);
        pnlShoes.setLayout(pnlShoesLayout);
        pnlShoesLayout.setHorizontalGroup(
            pnlShoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlShoesLayout.createSequentialGroup()
                .addGroup(pnlShoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlShoesLayout.createSequentialGroup()
                        .addGroup(pnlShoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlShoesLayout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(lblModel, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlShoesLayout.createSequentialGroup()
                                .addGap(41, 41, 41)
                                .addGroup(pnlShoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblShoeId, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(pnlShoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(pnlShoesLayout.createSequentialGroup()
                                            .addComponent(lblSize, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(lblPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(pnlShoesLayout.createSequentialGroup()
                                            .addComponent(lblBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(lblColor, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(lblBrand1, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 17, Short.MAX_VALUE))
                    .addGroup(pnlShoesLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlShoesLayout.setVerticalGroup(
            pnlShoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlShoesLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblModel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(lblShoeId, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlShoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSize, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(pnlShoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblColor, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addComponent(lblBrand1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblImage, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pnlSubtotals.setBackground(new java.awt.Color(255, 255, 255));

        pnlTotal.setBackground(new java.awt.Color(255, 255, 255));
        pnlTotal.setForeground(new java.awt.Color(51, 51, 51));

        lblTotalText.setFont(new java.awt.Font("Segoe UI Light", 0, 18)); // NOI18N
        lblTotalText.setForeground(new java.awt.Color(0, 0, 0));
        lblTotalText.setText("Total shoes on Stock:");

        lblTotalNum.setFont(new java.awt.Font("Segoe UI Light", 0, 18)); // NOI18N
        lblTotalNum.setForeground(new java.awt.Color(51, 51, 51));
        lblTotalNum.setText("0");

        jSeparator2.setForeground(new java.awt.Color(51, 51, 51));

        jSeparator3.setForeground(new java.awt.Color(51, 51, 51));

        javax.swing.GroupLayout pnlTotalLayout = new javax.swing.GroupLayout(pnlTotal);
        pnlTotal.setLayout(pnlTotalLayout);
        pnlTotalLayout.setHorizontalGroup(
            pnlTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTotalLayout.createSequentialGroup()
                .addGroup(pnlTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlTotalLayout.createSequentialGroup()
                            .addComponent(lblTotalText)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lblTotalNum, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 4, Short.MAX_VALUE))
        );
        pnlTotalLayout.setVerticalGroup(
            pnlTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTotalLayout.createSequentialGroup()
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotalText, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotalNum, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout pnlSubtotalsLayout = new javax.swing.GroupLayout(pnlSubtotals);
        pnlSubtotals.setLayout(pnlSubtotalsLayout);
        pnlSubtotalsLayout.setHorizontalGroup(
            pnlSubtotalsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSubtotalsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(141, Short.MAX_VALUE))
        );
        pnlSubtotalsLayout.setVerticalGroup(
            pnlSubtotalsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSubtotalsLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnlTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout bgLayout = new javax.swing.GroupLayout(bg);
        bg.setLayout(bgLayout);
        bgLayout.setHorizontalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgLayout.createSequentialGroup()
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bgLayout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSearch)
                            .addGroup(bgLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(bgLayout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(pnlSubtotals, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(pnlTable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(bgLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(sbtnGoBack, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bgLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(pnlShoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44)
                        .addComponent(pnlButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bgLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblEditInv, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(345, 345, 345))))
        );
        bgLayout.setVerticalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(sbtnGoBack, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEditInv))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSearch)
                .addGap(6, 6, 6)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bgLayout.createSequentialGroup()
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(pnlTable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlSubtotals, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(bgLayout.createSequentialGroup()
                        .addGap(519, 519, 519)
                        .addComponent(pnlButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnlShoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void sbtnGoBackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sbtnGoBackMouseClicked
        sbtnGoBack.setSvgImage("clkBackArrow.svg", sbtnGoBack.getWidth(), sbtnGoBack.getHeight());
        MenuPrincipal back = new MenuPrincipal();
        back.setVisible(true);
        back.setLocationRelativeTo(null);
        if (this.currentFrame != null) {
            this.currentFrame.closeFrame();
        }
    }//GEN-LAST:event_sbtnGoBackMouseClicked

    private void sbtnGoBackMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sbtnGoBackMouseEntered
        sbtnGoBack.setSvgImage("hvrBackArrow.svg", sbtnGoBack.getWidth(), sbtnGoBack.getHeight());
    }//GEN-LAST:event_sbtnGoBackMouseEntered

    private void sbtnGoBackMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sbtnGoBackMouseExited
        sbtnGoBack.setSvgImage("back arrow.svg", sbtnGoBack.getWidth(), sbtnGoBack.getHeight());
    }//GEN-LAST:event_sbtnGoBackMouseExited

    private void sbtnGoBackMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sbtnGoBackMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_sbtnGoBackMousePressed

    
    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed

    private void txtSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSearchMouseClicked
        txtSearch.setText("");
        txtSearch.setForeground(Color.black);
    }//GEN-LAST:event_txtSearchMouseClicked

    private void tblData2MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblData2MouseMoved

    }//GEN-LAST:event_tblData2MouseMoved

    private void tblDataMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_tblDataMouseMoved

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bg;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel lblBrand;
    private javax.swing.JLabel lblBrand1;
    private javax.swing.JLabel lblColor;
    private javax.swing.JLabel lblEditInv;
    private javax.swing.JLabel lblImage;
    private javax.swing.JLabel lblModel;
    private javax.swing.JLabel lblPrice;
    private javax.swing.JLabel lblSearch;
    private javax.swing.JLabel lblShoeId;
    private javax.swing.JLabel lblSize;
    private javax.swing.JLabel lblTotalNum;
    private javax.swing.JLabel lblTotalText;
    private javax.swing.JPanel pnlButton;
    private javax.swing.JPanel pnlShoes;
    private javax.swing.JPanel pnlSubtotals;
    private javax.swing.JPanel pnlTable;
    private javax.swing.JPanel pnlTotal;
    private com.mycompany.sneaksapp.igu.SVGImage sbtnGoBack;
    private javax.swing.JScrollPane scrlTable;
    private javax.swing.JScrollPane scrlTable1;
    private javax.swing.JTable tblData;
    private javax.swing.JTable tblData2;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
    
    public void setWork(Work work) {
    this.work = work;
    }
    
    public void setWorkInv(WorkInv workInv) {
        this.workInv = workInv;
    }
    
    public void setWorkSell(WorkSell workSell) {
        this.workSell = workSell;
    }

}
