package com.mycompany.sneaksapp.igu.views;
import com.mycompany.sneaksapp.igu.MenuPrincipal;
import com.mycompany.sneaksapp.igu.Work;
import com.mycompany.sneaksapp.igu.WorkSell;
import com.mycompany.sneaksapp.logica.FrameControllable;
import com.mycompany.sneaksapp.logica.GestorZapatos;
import com.mycompany.sneaksapp.logica.ZapatoTableModel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
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


public class PnlSell extends javax.swing.JPanel {

    private Work work;
    private WorkSell workSell;
    private FrameControllable currentFrame;
    
    // Define el archivo para guardar los datos
    private GestorZapatos gestorZapatos;
    
    // Variables para animación
    private Timer animationTimer;
    private final Color startColor = Color.BLACK;
    private final Color hoverColor = Color.decode("#404040");
    private Color currentColor = startColor;
    private boolean toHoverColor = false;
    
    //Grupo para los radiobuttons
    ButtonGroup group = new ButtonGroup();
    
    public PnlSell(FrameControllable frame) {
        this.currentFrame = frame;
        initComponents();
        initButtonAnimation();
        InitStyles();
        gestorZapatos = new GestorZapatos();
        initializeTable();
        setupDeleteKeyBinding();
        
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
    
    private void initializeTable() {
        ZapatoTableModel modelo = new ZapatoTableModel(gestorZapatos.getZapatos());
        modelo.setAllEditable(false);
        tblData.setModel(modelo);
        tblData.getTableHeader().setReorderingAllowed(false);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tblData.getModel());
        tblData.setRowSorter(sorter);
        
        tblData.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblData.setRowSelectionAllowed(true); // Asegura que la selección por fila esté habilitada
        tblData.setColumnSelectionAllowed(false);
        this.tblSubtotals.getTableHeader().setReorderingAllowed(false);
        
        // Mouse listener para detectar doble clics en tblData y añadir a tblSubtotals
        tblData.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {  // Verificar si es un doble clic
                    addSelectedRowToSubtotals();
                }
            }
        });
        
    }
    
    
    private void addSelectedRowToSubtotals() {
        int selectedRowIndex = tblData.getSelectedRow();
        if (selectedRowIndex != -1) {
            Object id = tblData.getValueAt(selectedRowIndex, 0); // ID
            if (!isRowAlreadyAdded((Integer) id)) {
                Object model = tblData.getValueAt(selectedRowIndex, 2); // Model
                Object size = tblData.getValueAt(selectedRowIndex, 3); // Size
                Object color = tblData.getValueAt(selectedRowIndex, 4); // Color
                Object price = tblData.getValueAt(selectedRowIndex, 6); // Price

                DefaultTableModel modelSubtotals = (DefaultTableModel) tblSubtotals.getModel();
                modelSubtotals.addRow(new Object[]{id, model, size, color, price});
                updateTotal();
            } else {
                JOptionPane.showMessageDialog(this, "This item has already been added to the order.", "Duplicate Entry", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private boolean isRowAlreadyAdded(Integer id) {
        DefaultTableModel modelSubtotals = (DefaultTableModel) tblSubtotals.getModel();
        for (int i = 0; i < modelSubtotals.getRowCount(); i++) {
            if (id.equals(modelSubtotals.getValueAt(i, 0))) {
                return true;
            }
        }
        return false;
    }
    
    private void deleteSelectedRowFromSubtotals() {
    int selectedRowIndex = tblSubtotals.getSelectedRow();
    if (selectedRowIndex != -1) {  // Asegura que haya una fila seleccionada
        DefaultTableModel model = (DefaultTableModel) tblSubtotals.getModel();
        model.removeRow(selectedRowIndex);  // Elimina la fila del modelo
        
        updateTotal();
        JOptionPane.showMessageDialog(this, "Selected row has been removed.", "Row Removed", JOptionPane.INFORMATION_MESSAGE);
    } else {
        JOptionPane.showMessageDialog(this, "Please select a row to delete.", "No Selection", JOptionPane.ERROR_MESSAGE);
    }
    }
    
    private void setupDeleteKeyBinding() {
        tblSubtotals.getInputMap(JTable.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "deleteRow");
        tblSubtotals.getActionMap().put("deleteRow", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                deleteSelectedRowFromSubtotals();
            }
        });
    }
    
    private void updateTotal() {
    DefaultTableModel model = (DefaultTableModel) tblSubtotals.getModel();
    double total = 0.0;
    for (int i = 0; i < model.getRowCount(); i++) {
        Double price = Double.valueOf(model.getValueAt(i, 4).toString()); // Asume que el precio está en la columna 4
        total += price;
    }
    lblTotalNum.setText(String.format("$%.2f", total)); // Formatea a dos decimales
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
        
        btnCheckout.setMargin(new Insets(0, 100, 0, 0));
        this.btnAddOrder.setBackground(Color.decode("#f5f5f5"));
        this.btnDelOrder.setBackground(Color.decode("#f5f5f5"));
        this.btnAddOrder.setBorder(new LineBorder(Color.decode("#eaeaea"), (int) 0.5));
        this.btnDelOrder.setBorder(new LineBorder(Color.decode("#eaeaea"), (int) 0.5));
        btnCheckout.setBackground(Color.black);
        
        this.group.add(this.rdbCard);
        this.group.add(this.rdbCash);
        

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
        tblData = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();
        pnlButton = new javax.swing.JPanel();
        btnCheckout = new javax.swing.JButton();
        lblSelPayMeth = new javax.swing.JLabel();
        rdbCard = new javax.swing.JRadioButton();
        rdbCash = new javax.swing.JRadioButton();
        btnAddOrder = new javax.swing.JButton();
        lblEditOrder = new javax.swing.JLabel();
        sepCheckout = new javax.swing.JSeparator();
        btnDelOrder = new javax.swing.JButton();
        lblSearch = new javax.swing.JLabel();
        pnlSubtotals = new javax.swing.JPanel();
        lblSubtotals = new javax.swing.JLabel();
        scrlSubtotals = new javax.swing.JScrollPane();
        tblSubtotals = new javax.swing.JTable();
        pnlTotal = new javax.swing.JPanel();
        lblTotalText = new javax.swing.JLabel();
        lblTotalNum = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        txtSearch = new javax.swing.JTextField();
        sepData = new javax.swing.JSeparator();

        bg.setBackground(new java.awt.Color(255, 255, 255));
        bg.setPreferredSize(new java.awt.Dimension(947, 684));

        lblEditInv.setBackground(new java.awt.Color(0, 0, 0));
        lblEditInv.setFont(new java.awt.Font("Segoe UI Black", 1, 22)); // NOI18N
        lblEditInv.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEditInv.setText("Sell Products");

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
        tblData.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tblDataMouseMoved(evt);
            }
        });
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

        btnCheckout.setFont(new java.awt.Font("Segoe UI Semibold", 1, 36)); // NOI18N
        btnCheckout.setForeground(new java.awt.Color(255, 255, 255));
        btnCheckout.setText("Checkout");
        btnCheckout.setBorder(null);
        btnCheckout.setBorderPainted(false);
        btnCheckout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCheckout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCheckoutMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCheckoutMouseExited(evt);
            }
        });
        btnCheckout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckoutActionPerformed(evt);
            }
        });

        lblSelPayMeth.setFont(new java.awt.Font("Segoe UI Light", 0, 18)); // NOI18N
        lblSelPayMeth.setForeground(new java.awt.Color(0, 0, 0));
        lblSelPayMeth.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSelPayMeth.setText("Select a payment method");

        rdbCard.setFont(new java.awt.Font("Segoe UI Light", 0, 12)); // NOI18N
        rdbCard.setText("Credit/Debit Card");
        rdbCard.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        rdbCash.setFont(new java.awt.Font("Segoe UI Light", 0, 12)); // NOI18N
        rdbCash.setText("Cash");
        rdbCash.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rdbCash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdbCashActionPerformed(evt);
            }
        });

        btnAddOrder.setBackground(java.awt.Color.lightGray);
        btnAddOrder.setFont(new java.awt.Font("Segoe UI Light", 0, 12)); // NOI18N
        btnAddOrder.setForeground(new java.awt.Color(51, 51, 51));
        btnAddOrder.setText("Add");
        btnAddOrder.setBorder(null);
        btnAddOrder.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAddOrder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAddOrderMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAddOrderMouseExited(evt);
            }
        });
        btnAddOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddOrderActionPerformed(evt);
            }
        });

        lblEditOrder.setFont(new java.awt.Font("Segoe UI Light", 0, 18)); // NOI18N
        lblEditOrder.setForeground(new java.awt.Color(0, 0, 0));
        lblEditOrder.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEditOrder.setText("Edit order:");

        sepCheckout.setOrientation(javax.swing.SwingConstants.VERTICAL);

        btnDelOrder.setBackground(java.awt.Color.lightGray);
        btnDelOrder.setFont(new java.awt.Font("Segoe UI Light", 0, 12)); // NOI18N
        btnDelOrder.setForeground(new java.awt.Color(51, 51, 51));
        btnDelOrder.setText("Delete");
        btnDelOrder.setBorder(null);
        btnDelOrder.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDelOrder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDelOrderMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDelOrderMouseExited(evt);
            }
        });
        btnDelOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelOrderActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlButtonLayout = new javax.swing.GroupLayout(pnlButton);
        pnlButton.setLayout(pnlButtonLayout);
        pnlButtonLayout.setHorizontalGroup(
            pnlButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlButtonLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAddOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEditOrder)
                    .addComponent(btnDelOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sepCheckout, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(pnlButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rdbCash)
                    .addGroup(pnlButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(rdbCard)
                        .addComponent(btnCheckout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblSelPayMeth)))
                .addContainerGap())
        );
        pnlButtonLayout.setVerticalGroup(
            pnlButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlButtonLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(sepCheckout, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlButtonLayout.createSequentialGroup()
                        .addGroup(pnlButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblEditOrder, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblSelPayMeth))
                        .addGroup(pnlButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlButtonLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnAddOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDelOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(pnlButtonLayout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(rdbCard)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(rdbCash)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnCheckout, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(39, 39, 39))
        );

        lblSearch.setFont(new java.awt.Font("Segoe UI Light", 0, 27)); // NOI18N
        lblSearch.setText("Search Products:");

        pnlSubtotals.setBackground(new java.awt.Color(255, 255, 255));

        lblSubtotals.setFont(new java.awt.Font("Segoe UI Semibold", 0, 24)); // NOI18N
        lblSubtotals.setText("Your order:");

        tblSubtotals.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Model", "Size", "Color", "Price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrlSubtotals.setViewportView(tblSubtotals);

        pnlTotal.setBackground(new java.awt.Color(255, 255, 255));
        pnlTotal.setForeground(new java.awt.Color(51, 51, 51));

        lblTotalText.setFont(new java.awt.Font("Segoe UI Light", 0, 18)); // NOI18N
        lblTotalText.setForeground(new java.awt.Color(0, 0, 0));
        lblTotalText.setText("Order Total:");

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
                .addContainerGap()
                .addGroup(pnlTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlTotalLayout.createSequentialGroup()
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlTotalLayout.createSequentialGroup()
                        .addGroup(pnlTotalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlTotalLayout.createSequentialGroup()
                                .addComponent(lblTotalText)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTotalNum, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
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
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                            .addComponent(pnlTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addGap(256, 256, 256))
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

        javax.swing.GroupLayout bgLayout = new javax.swing.GroupLayout(bg);
        bg.setLayout(bgLayout);
        bgLayout.setHorizontalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgLayout.createSequentialGroup()
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bgLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(sbtnGoBack, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(bgLayout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(bgLayout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblEditInv, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(bgLayout.createSequentialGroup()
                                            .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(sepData, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(pnlSubtotals, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(pnlButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(pnlTable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(bgLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 579, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblSearch))))
                .addContainerGap(30, Short.MAX_VALUE))
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
                    .addComponent(pnlButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        if (this.currentFrame != null) {
            this.currentFrame.closeFrame();
        }
        MenuPrincipal back = new MenuPrincipal();
        back.setVisible(true);
        back.setLocationRelativeTo(null);
        
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

    private void btnCheckoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckoutActionPerformed
        Checkout();
    }//GEN-LAST:event_btnCheckoutActionPerformed
    
    private void Checkout(){
        DefaultTableModel modelSubtotals = (DefaultTableModel) tblSubtotals.getModel();
        if (modelSubtotals.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "No items in the order. Please add items to proceed.", "Empty Order", JOptionPane.WARNING_MESSAGE);
            return; // Exit the method early if there are no rows
        }

        // Check if a payment method is selected
        if (!rdbCard.isSelected() && !rdbCash.isSelected()) {
            JOptionPane.showMessageDialog(null, "Please select a payment method before proceeding.", "Payment Method Required", JOptionPane.WARNING_MESSAGE);
            return; // Exit the method early if no payment method is selected
        }

        ZapatoTableModel modelData = (ZapatoTableModel) tblData.getModel();
        double total = 0.0;
        List<Integer> idsParaEliminar = new ArrayList<>();
        GestorZapatos gestor = new GestorZapatos();

        // Recopilar IDs y calcular el total
        for (int i = 0; i < modelSubtotals.getRowCount(); i++) {
            double price = ((Number) modelSubtotals.getValueAt(i, 4)).doubleValue();
            total += price;
            idsParaEliminar.add((Integer) modelSubtotals.getValueAt(i, 0));
        }

        // Generate a random 6-digit order number using UUID for uniqueness (as discussed previously)
        Random rand = new Random();
        int orderNumber = 100000 + rand.nextInt(900000);

        // Determine the selected payment method
        String paymentMethod = rdbCard.isSelected() ? "Credit/Debit Card" : "Cash";

        // Mostrar resumen de la compra
        JOptionPane.showMessageDialog(null,
            "Thank you for your purchase!\nOrder Number: " + orderNumber + 
            "\nTotal Amount: $" + String.format("%.2f", total) +
            "\nPayment Method: " + paymentMethod,
            "Order Confirmation",
            JOptionPane.INFORMATION_MESSAGE);

        // Eliminar filas en tblData que coinciden con los IDs recopilados
        modelData.removeRowsByIds(idsParaEliminar);

        // Limpiar tblSubtotals
        modelSubtotals.setRowCount(0);

        //Eliminar datos de datosTabla.csv
        gestor.eliminarZapatos(idsParaEliminar);
        
        // Deselect the radio buttons
        rdbCard.setSelected(false);
        rdbCash.setSelected(false);
    }
    
    private void refreshTable() {
    // Asumiendo que gestorZapatos.getZapatos() devuelve la lista actualizada de zapatos
    ZapatoTableModel modelo = new ZapatoTableModel(gestorZapatos.getZapatos());
    modelo.setAllEditable(false);
    tblData.setModel(modelo); // Establece el modelo actualizado en la tabla
    }
    
    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed

    private void txtSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSearchMouseClicked
        txtSearch.setText("");
        txtSearch.setForeground(Color.black);
    }//GEN-LAST:event_txtSearchMouseClicked

    private void tblDataMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseMoved

    }//GEN-LAST:event_tblDataMouseMoved

    private void rdbCashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbCashActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdbCashActionPerformed

    private void btnDelOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelOrderActionPerformed
        deleteSelectedRowFromSubtotals();
    }//GEN-LAST:event_btnDelOrderActionPerformed

    private void btnAddOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddOrderActionPerformed
        addSelectedRowToSubtotals();
    }//GEN-LAST:event_btnAddOrderActionPerformed

    private void btnCheckoutMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCheckoutMouseEntered
        
    }//GEN-LAST:event_btnCheckoutMouseEntered

    private void btnCheckoutMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCheckoutMouseExited
        
    }//GEN-LAST:event_btnCheckoutMouseExited

    private void btnAddOrderMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddOrderMouseEntered
        this.btnAddOrder.setForeground(Color.decode("#1e6612"));
    }//GEN-LAST:event_btnAddOrderMouseEntered

    private void btnAddOrderMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddOrderMouseExited
        this.btnAddOrder.setForeground(Color.black);
        
    }//GEN-LAST:event_btnAddOrderMouseExited

    private void btnDelOrderMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDelOrderMouseEntered
        this.btnDelOrder.setForeground(Color.decode("#8c0707"));
    }//GEN-LAST:event_btnDelOrderMouseEntered

    private void btnDelOrderMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDelOrderMouseExited
        this.btnDelOrder.setForeground(Color.black);
    }//GEN-LAST:event_btnDelOrderMouseExited
 
    
    private void initButtonAnimation() {
        btnCheckout.setBackground(startColor);
        animationTimer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int r = currentColor.getRed();
                int g = currentColor.getGreen();
                int b = currentColor.getBlue();
                int targetR = toHoverColor ? hoverColor.getRed() : startColor.getRed();
                int targetG = toHoverColor ? hoverColor.getGreen() : startColor.getGreen();
                int targetB = toHoverColor ? hoverColor.getBlue() : startColor.getBlue();

                // Update color step by step towards the target
                r = adjustColorStep(r, targetR);
                g = adjustColorStep(g, targetG);
                b = adjustColorStep(b, targetB);

                currentColor = new Color(r, g, b);
                btnCheckout.setBackground(currentColor);

                // Check if the current color has reached the target
                if (r == targetR && g == targetG && b == targetB) {
                    animationTimer.stop();
                }
            }
        });

        btnCheckout.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                toHoverColor = true;
                if (!animationTimer.isRunning()) {
                    animationTimer.start();
                }
            }

            public void mouseExited(MouseEvent evt) {
                toHoverColor = false;
                if (!animationTimer.isRunning()) {
                    animationTimer.start();
                }
            }
        });
    }

    private int adjustColorStep(int current, int target) {
        if (current < target) {
            return Math.min(current + 15, target);
        } else if (current > target) {
            return Math.max(current - 15, target);
        }
        return current;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bg;
    private javax.swing.JButton btnAddOrder;
    private javax.swing.JButton btnCheckout;
    private javax.swing.JButton btnDelOrder;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel lblEditInv;
    private javax.swing.JLabel lblEditOrder;
    private javax.swing.JLabel lblSearch;
    private javax.swing.JLabel lblSelPayMeth;
    private javax.swing.JLabel lblSubtotals;
    private javax.swing.JLabel lblTotalNum;
    private javax.swing.JLabel lblTotalText;
    private javax.swing.JPanel pnlButton;
    private javax.swing.JPanel pnlSubtotals;
    private javax.swing.JPanel pnlTable;
    private javax.swing.JPanel pnlTotal;
    private javax.swing.JRadioButton rdbCard;
    private javax.swing.JRadioButton rdbCash;
    private com.mycompany.sneaksapp.igu.SVGImage sbtnGoBack;
    private javax.swing.JScrollPane scrlSubtotals;
    private javax.swing.JScrollPane scrlTable;
    private javax.swing.JSeparator sepCheckout;
    private javax.swing.JSeparator sepData;
    private javax.swing.JTable tblData;
    private javax.swing.JTable tblSubtotals;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
    
    public void setWork(Work work) {
    this.work = work;
    }
    
    public void setWorkSell(WorkSell workSell) {
        this.workSell = workSell;
    }

}
