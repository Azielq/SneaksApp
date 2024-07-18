package com.mycompany.sneaksapp.igu.views;

import com.mycompany.sneaksapp.igu.MenuPrincipal;
import com.mycompany.sneaksapp.igu.Work;
import com.mycompany.sneaksapp.igu.WorkInv;
import com.mycompany.sneaksapp.logica.DynamicComboBoxEditor;
import com.mycompany.sneaksapp.logica.FrameControllable;
import com.mycompany.sneaksapp.logica.GestorZapatos;
import com.mycompany.sneaksapp.logica.IntegerEditor;
import com.mycompany.sneaksapp.logica.Zapato;
import com.mycompany.sneaksapp.logica.ZapatoTableModel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class pnlEdit extends javax.swing.JPanel {

    private Work work;
    private WorkInv workInv;
    private FrameControllable currentFrame;
    
    // Define el archivo para guardar los datos
    private GestorZapatos gestorZapatos;
    
    public pnlEdit(FrameControllable frame) {
        this.currentFrame = frame;
        initComponents();
        InitStyles();
        gestorZapatos = new GestorZapatos();
        initializeTable();
        initializeComboBoxColumns();
        actualizarSubtotales();
        actualizarTotalZapatos();
        
        
        
        //Asigna cursor diferente dependiendo de la parte de la table esté
        tblData.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                updateCursor(e);
            }
        });
        tblData.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                updateCursor(e);
            }
        });
        
        
        //Barra de busqueda Funcionalidad
        // Agrega el listener de búsqueda aquí
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filter();
            }

            private void filter() {
                TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>) tblData.getRowSorter();
                String text = txtSearch.getText();
                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(text))); // Filtra ignorando mayúsculas/minúsculas
                }
            }
        });
        
        
    }
    
    public void actualizarTotalZapatos() {
        int totalZapatos = gestorZapatos.getZapatos().size(); // Obtiene el total de zapatos
        lblTotalNum.setText(String.valueOf(totalZapatos)); // Establece el total en lblTotalNum
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
        DefaultTableModel modeloSubtotals = (DefaultTableModel) tblSubtotals.getModel();
        modeloSubtotals.setRowCount(0); // Limpiar la tabla antes de agregar nuevas filas

        for (Map.Entry<String, Integer> entry : subtotales.entrySet()) {
            modeloSubtotals.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }
    }
    
    
    private void initializeTable() {
    ZapatoTableModel modelo = new ZapatoTableModel(gestorZapatos.getZapatos());
    tblData.setModel(modelo);
    tblData.getTableHeader().setReorderingAllowed(false);
    tblData.getColumnModel().getColumn(6).setCellEditor(new IntegerEditor());
    this.tblSubtotals.getTableHeader().setReorderingAllowed(false);
    
    modelo.addTableModelListener(e -> {
        if (e.getType() == TableModelEvent.UPDATE) {
            int row = e.getFirstRow();
            int column = e.getColumn();
            // Reacciona solo si los cambios son en las columnas 'Model' o 'Color'
            if (column == modelo.findColumn("Model") || column == modelo.findColumn("Color")) {
                updateImageCell(row, modelo);
            }
        }
    });
    
    // Crear y establecer el TableRowSorter usando el modelo de la tabla
    TableRowSorter<TableModel> sorter = new TableRowSorter<>(tblData.getModel());
    tblData.setRowSorter(sorter);
   
    }
    
    private void updateImageCell(int row, ZapatoTableModel modelo) {
        String model = (String) modelo.getValueAt(row, modelo.findColumn("Model"));
        String color = (String) modelo.getValueAt(row, modelo.findColumn("Color"));

        String imagePath = generateImagePath(model, color);
        modelo.setValueAt(imagePath, row, modelo.findColumn("Image"));
    }

    private String generateImagePath(String model, String color) {
        if (model != null && color != null) {
            // logica para formatear el path de la imagen
            return model.replace(" ", "_").toLowerCase() + "_" + color.replace(" ", "_").toLowerCase() + ".webp";
        }
        return "default.webp";  // Imagen predeterminada si los datos no están completos
    }
    
    
    private void updateCursor(MouseEvent e) {
        int column = tblData.columnAtPoint(e.getPoint());

        // Verificar si la columna está dentro del rango válido
        if (column >= 0 && column < tblData.getColumnCount()) {
            switch (column) 
            {
                case 1, 2, 3, 4 -> tblData.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                case 5, 6, 7 -> tblData.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                default -> tblData.setCursor(Cursor.getDefaultCursor());
            }
        } else {
            // Si la columna está fuera de los límites, usar el cursor predeterminado
            tblData.setCursor(Cursor.getDefaultCursor());
        }
    }
    
    private void initializeComboBoxColumns() {
    // Configuración de los JComboBox en las columnas
    String[] column2Options = {"Nike", "Adidas"};
    TableColumn column2 = tblData.getColumnModel().getColumn(1);
    column2.setCellEditor(new DefaultCellEditor(new JComboBox<>(column2Options)));

    // Asigna el editor de celda personalizado a la tercera columna
    TableColumn column3 = tblData.getColumnModel().getColumn(2);
    column3.setCellEditor(new DynamicComboBoxEditor(new JComboBox<>(), tblData));

    String[] column4Options = {"38", "39", "40", "41", "42", "43", "44", "45"};
    TableColumn column4 = tblData.getColumnModel().getColumn(3);
    column4.setCellEditor(new DefaultCellEditor(new JComboBox<>(column4Options)));

    String[] column5Options = {"Black", "White", "Blue", "Red"};
    TableColumn column5 = tblData.getColumnModel().getColumn(4);
    column5.setCellEditor(new DefaultCellEditor(new JComboBox<>(column5Options)));
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
        
        btnSave.setMargin(new Insets(0, 100, 0, 0));
        btnAdd.setMargin(new Insets(0, 100, 0, 0));
        btnDel.setMargin(new Insets(0, 100, 0, 0));
        btnSave.setBackground(Color.decode("#4e4e4e"));
        btnAdd.setBackground(Color.decode("#4e4e4e"));
        btnDel.setBackground(Color.decode("#4e4e4e"));
        
        pnlTotal.setBackground(Color.decode("#353535"));
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bg = new javax.swing.JPanel();
        lblEditInv = new javax.swing.JLabel();
        sbtnGoBack = new com.mycompany.sneaksapp.igu.SVGImage();
        pnlTable = new javax.swing.JPanel();
        scrlTable = new javax.swing.JScrollPane();
        tblData = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();
        pnlButton = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        lblSearch = new javax.swing.JLabel();
        pnlSubtotals = new javax.swing.JPanel();
        lblSubtotals = new javax.swing.JLabel();
        scrlSubtotals = new javax.swing.JScrollPane();
        tblSubtotals = new javax.swing.JTable();
        pnlTotal = new javax.swing.JPanel();
        lblTotalText = new javax.swing.JLabel();
        lblTotalNum = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        sepData = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        bg.setBackground(new java.awt.Color(255, 255, 255));
        bg.setPreferredSize(new java.awt.Dimension(947, 684));

        lblEditInv.setBackground(new java.awt.Color(0, 0, 0));
        lblEditInv.setFont(new java.awt.Font("Segoe UI Black", 1, 22)); // NOI18N
        lblEditInv.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEditInv.setText("Edit Inventory");

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

        tblData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblData.setColumnSelectionAllowed(true);
        scrlTable.setViewportView(tblData);
        tblData.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout pnlTableLayout = new javax.swing.GroupLayout(pnlTable);
        pnlTable.setLayout(pnlTableLayout);
        pnlTableLayout.setHorizontalGroup(
            pnlTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTableLayout.createSequentialGroup()
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 582, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(pnlTableLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrlTable, javax.swing.GroupLayout.PREFERRED_SIZE, 844, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlTableLayout.setVerticalGroup(
            pnlTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTableLayout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrlTable, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        pnlButton.setBackground(new java.awt.Color(255, 255, 255));

        btnAdd.setFont(new java.awt.Font("Segoe UI Light", 0, 18)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setText("Add");
        btnAdd.setBorder(null);
        btnAdd.setBorderPainted(false);
        btnAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnDel.setFont(new java.awt.Font("Segoe UI Light", 0, 18)); // NOI18N
        btnDel.setForeground(new java.awt.Color(255, 255, 255));
        btnDel.setText("Delete");
        btnDel.setBorder(null);
        btnDel.setBorderPainted(false);
        btnDel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelActionPerformed(evt);
            }
        });

        btnSave.setFont(new java.awt.Font("Segoe UI Light", 0, 36)); // NOI18N
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setText("Save");
        btnSave.setBorder(null);
        btnSave.setBorderPainted(false);
        btnSave.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlButtonLayout = new javax.swing.GroupLayout(pnlButton);
        pnlButton.setLayout(pnlButtonLayout);
        pnlButtonLayout.setHorizontalGroup(
            pnlButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlButtonLayout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .addGroup(pnlButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDel, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlButtonLayout.setVerticalGroup(
            pnlButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlButtonLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(pnlButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlButtonLayout.createSequentialGroup()
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDel, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)))
                .addContainerGap())
        );

        lblSearch.setFont(new java.awt.Font("Segoe UI Light", 0, 27)); // NOI18N
        lblSearch.setText("Filter to Edit:");

        pnlSubtotals.setBackground(new java.awt.Color(255, 255, 255));

        lblSubtotals.setFont(new java.awt.Font("Segoe UI Light", 0, 24)); // NOI18N
        lblSubtotals.setText("Subtotals:");

        tblSubtotals.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Model", "Units"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrlSubtotals.setViewportView(tblSubtotals);

        lblTotalText.setFont(new java.awt.Font("Segoe UI Light", 0, 24)); // NOI18N
        lblTotalText.setForeground(new java.awt.Color(255, 255, 255));
        lblTotalText.setText("Total shoes on stock:");

        lblTotalNum.setFont(new java.awt.Font("Segoe UI Light", 0, 24)); // NOI18N
        lblTotalNum.setForeground(new java.awt.Color(255, 255, 255));
        lblTotalNum.setText("1234");

        javax.swing.GroupLayout pnlTotalLayout = new javax.swing.GroupLayout(pnlTotal);
        pnlTotal.setLayout(pnlTotalLayout);
        pnlTotalLayout.setHorizontalGroup(
            pnlTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTotalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTotalText)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTotalNum, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(171, 171, 171))
        );
        pnlTotalLayout.setVerticalGroup(
            pnlTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTotalLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotalText, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotalNum, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlSubtotalsLayout = new javax.swing.GroupLayout(pnlSubtotals);
        pnlSubtotals.setLayout(pnlSubtotalsLayout);
        pnlSubtotalsLayout.setHorizontalGroup(
            pnlSubtotalsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSubtotalsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSubtotalsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrlSubtotals, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
                    .addGroup(pnlSubtotalsLayout.createSequentialGroup()
                        .addGroup(pnlSubtotalsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSubtotals)
                            .addComponent(pnlTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlSubtotalsLayout.setVerticalGroup(
            pnlSubtotalsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSubtotalsLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(lblSubtotals, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scrlSubtotals, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(269, 269, 269))
        );

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

        sepData.setForeground(new java.awt.Color(0, 0, 0));

        jLabel1.setFont(new java.awt.Font("Segoe UI Light", 0, 12)); // NOI18N
        jLabel1.setForeground(java.awt.Color.gray);
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Press \"add\" and tap on the cells to");

        jLabel2.setFont(new java.awt.Font("Segoe UI Light", 0, 12)); // NOI18N
        jLabel2.setForeground(java.awt.Color.gray);
        jLabel2.setText("assign and edit product attributes.");

        javax.swing.GroupLayout bgLayout = new javax.swing.GroupLayout(bg);
        bg.setLayout(bgLayout);
        bgLayout.setHorizontalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sbtnGoBack, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblEditInv, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
            .addGroup(bgLayout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bgLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlTable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(bgLayout.createSequentialGroup()
                                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(sepData, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(pnlSubtotals, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 109, Short.MAX_VALUE)
                                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(pnlButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bgLayout.createSequentialGroup()
                                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                                        .addGap(8, 8, 8)))
                                .addContainerGap(42, Short.MAX_VALUE))))
                    .addGroup(bgLayout.createSequentialGroup()
                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(bgLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 579, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblSearch))
                        .addContainerGap())))
        );
        bgLayout.setVerticalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgLayout.createSequentialGroup()
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bgLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(lblEditInv))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bgLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(sbtnGoBack, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSearch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(pnlTable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bgLayout.createSequentialGroup()
                        .addComponent(sepData, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(pnlSubtotals, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(bgLayout.createSequentialGroup()
                        .addComponent(pnlButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addGap(0, 0, 0)
                        .addComponent(jLabel2)))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        this.currentFrame.closeFrame();
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

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Intenta finalizar la edición de cualquier celda que esté siendo editada actualmente
    if (tblData.getCellEditor() != null) {
        tblData.getCellEditor().stopCellEditing();
    }

    // Procede a guardar los datos
    try {
        gestorZapatos.guardarDatosEnArchivo();
        refreshTable();
    } catch(NumberFormatException ex) {
        JOptionPane.showMessageDialog(null, "Error en los datos ingresados.");
    }
    
    actualizarSubtotales();
    actualizarTotalZapatos();
    }//GEN-LAST:event_btnSaveActionPerformed

    
    
    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
    // Detiene cualquier edición actual en la tabla antes de añadir una nueva fila
    if (tblData.isEditing()) {
        tblData.getCellEditor().stopCellEditing();
    }

    try {
        // Asumiendo que Zapato tiene un constructor que inicializa con valores predeterminados
        Zapato zapato = new Zapato();
        gestorZapatos.addZapato(zapato); // Añade el nuevo zapato a la gestión
        refreshTable(); // Actualiza la tabla para reflejar el nuevo zapato añadido

        // Opcional: Selecciona la nueva fila añadida y la hace visible
        int rowIndex = tblData.getRowCount() - 1;
        tblData.setRowSelectionInterval(rowIndex, rowIndex);
        tblData.scrollRectToVisible(tblData.getCellRect(rowIndex, 0, true));
    } catch (Exception e) {
        // Manejo de excepciones específicas o generales según sea necesario
        JOptionPane.showMessageDialog(this, "Error al añadir un nuevo zapato: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    actualizarSubtotales();
    actualizarTotalZapatos();
    }//GEN-LAST:event_btnAddActionPerformed

    private void configureComboBoxForRow(int rowIndex) {
        // Configuración para el JComboBox de "Brand"
        TableColumn brandColumn = tblData.getColumnModel().getColumn(1);
        JComboBox<String> comboBoxBrand = new JComboBox<>(new String[]{"Nike", "Adidas"});
        comboBoxBrand.addActionListener(e -> {
            updateModelComboBox(rowIndex); // Actualiza el JComboBox de "Model" cuando se cambia "Brand"
            updateImageBasedOnSelection(rowIndex);
        });
        brandColumn.setCellEditor(new DefaultCellEditor(comboBoxBrand));

        // Configuración para el JComboBox de "Model" con DynamicComboBoxEditor
        TableColumn modelColumn = tblData.getColumnModel().getColumn(2);
        DynamicComboBoxEditor modelEditor = new DynamicComboBoxEditor(new JComboBox<>(), tblData);
        modelColumn.setCellEditor(modelEditor);
        updateModelComboBox(rowIndex); // Asegúrate de inicializar con los valores correctos

        // Configuración para el JComboBox de "Color"
        TableColumn colorColumn = tblData.getColumnModel().getColumn(4);
        JComboBox<String> comboBoxColor = new JComboBox<>(new String[]{"Black", "White", "Blue", "Red"});
        comboBoxColor.addActionListener(e -> updateImageBasedOnSelection(rowIndex));
        colorColumn.setCellEditor(new DefaultCellEditor(comboBoxColor));

        // Actualiza inmediatamente después de configurar
        updateImageBasedOnSelection(rowIndex);
    }

    private void updateModelComboBox(int rowIndex) {
        Object brandValue = tblData.getValueAt(rowIndex, 1); // Asume que "Brand" está en la columna 1
        DynamicComboBoxEditor modelEditor = (DynamicComboBoxEditor) tblData.getColumnModel().getColumn(2).getCellEditor();
        JComboBox<String> modelComboBox = (JComboBox<String>) modelEditor.getTableCellEditorComponent(tblData, null, true, rowIndex, 2);
        modelComboBox.setSelectedItem(tblData.getValueAt(rowIndex, 2)); // Establece el valor actual como seleccionado en el JComboBox
    }
    
    private void updateImageBasedOnSelection(int row) {
        if (tblData.getRowCount() > row) { // Asegúrate de que la fila exista
            String modelValue = (String) tblData.getValueAt(row, 2); // Asume que Modelo está en columna 2
            String colorValue = (String) tblData.getValueAt(row, 4); // Asume que Color está en columna 4
            String imagePath = generateImagePath(modelValue, colorValue);
            tblData.setValueAt(imagePath, row, 7); // Actualizado para reflejar que la Imagen está en la columna 7
            ((ZapatoTableModel) tblData.getModel()).fireTableCellUpdated(row, 7); // Notifica la actualización de la celda de la imagen
        }
    }
    
    
    private void refreshTable() {
    // Asumiendo que gestorZapatos.getZapatos() devuelve la lista actualizada de zapatos
    ZapatoTableModel modelo = new ZapatoTableModel(gestorZapatos.getZapatos());
    tblData.setModel(modelo); // Establece el modelo actualizado en la tabla
    initializeComboBoxColumns();// Reestablece los comboBox y sus configuraciones
        for (int i = 0; i < tblData.getRowCount(); i++) {
            configureComboBoxForRow(i); // configurar JComboBoxes para cada fila
        }
    tblData.getColumnModel().getColumn(6).setCellEditor(new IntegerEditor());
}
    
    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed

    private void txtSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSearchMouseClicked
        txtSearch.setText("");
        txtSearch.setForeground(Color.black);
    }//GEN-LAST:event_txtSearchMouseClicked

    private void btnDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelActionPerformed
        eliminarZapato();
        actualizarSubtotales();
        actualizarTotalZapatos();
    }//GEN-LAST:event_btnDelActionPerformed
    
    private void eliminarZapato() {
        int selectedRow = tblData.getSelectedRow();
        if (selectedRow >= 0) {
            
        /**Elimina el zapato del modelo. (ZapatoTableModel) es una operación de casting. 
        esto se usa porque se quiere convertir el modelo de tblData a un objeto de ZapatoTableModel*/
        ((ZapatoTableModel) tblData.getModel()).removeZapato(selectedRow);

        // Actualiza la selección
        int rowCount = tblData.getRowCount();
        if (rowCount == 0) {
            // Si no quedan filas, no hay nada que seleccionar
            tblData.clearSelection();
        } else if (selectedRow < rowCount) {
            // Si la fila eliminada no era la última, selecciona la misma posición
            tblData.setRowSelectionInterval(selectedRow, selectedRow);
        } else {
            // Si la fila eliminada era la última, selecciona la nueva última fila
            tblData.setRowSelectionInterval(rowCount - 1, rowCount - 1);
        }
        } else {
        // Manejar el caso de no haber selección o selección inválida
        JOptionPane.showMessageDialog(this, "Select a shoe to delete it.");
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bg;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnSave;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblEditInv;
    private javax.swing.JLabel lblSearch;
    private javax.swing.JLabel lblSubtotals;
    private javax.swing.JLabel lblTotalNum;
    private javax.swing.JLabel lblTotalText;
    private javax.swing.JPanel pnlButton;
    private javax.swing.JPanel pnlSubtotals;
    private javax.swing.JPanel pnlTable;
    private javax.swing.JPanel pnlTotal;
    private com.mycompany.sneaksapp.igu.SVGImage sbtnGoBack;
    private javax.swing.JScrollPane scrlSubtotals;
    private javax.swing.JScrollPane scrlTable;
    private javax.swing.JSeparator sepData;
    private javax.swing.JTable tblData;
    private javax.swing.JTable tblSubtotals;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
    
    public void setWork(Work work) {
    this.work = work;
    }
    
    public void setWorkInv(WorkInv workInv) {
        this.workInv = workInv;
    }

}
