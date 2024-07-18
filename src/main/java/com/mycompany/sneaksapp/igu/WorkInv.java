package com.mycompany.sneaksapp.igu;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.mycompany.sneaksapp.igu.views.pnlEdit;
import com.mycompany.sneaksapp.igu.views.pnlPrincipal;
import com.mycompany.sneaksapp.igu.views.pnlView;
import com.mycompany.sneaksapp.logica.FrameControllable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import javax.swing.SwingConstants;


public class WorkInv extends javax.swing.JFrame implements FrameControllable {

    
    private boolean EditTrueBtn = false;
    private boolean ViewTrueBtn = false;
    private boolean SellTrueBtn = false;
    

    public WorkInv() 
    { 
        initComponents();
        InitStyles();
        InitContent();
    }
    
    @Override
        public void closeFrame() {
        this.dispose();
        }

    
    private void InitStyles()
    {
        
        //Poner icono para windows 
        FlatSVGIcon icon = new FlatSVGIcon("isotipo.svg");
        setIconImage(icon.getImage());
        
        //Inicializa los estilos de JLabbel
        
        lblWelcome.setBackground(Color.decode("#cbcbcb"));
        lblSnkscom.setText("<html><body style='letter-spacing: -20px;'>Sneaks.com</body></html>");
        
        //Inicializa los estilos de los JPanel
        pnlMenuL.setBackground(Color.decode("#cbcbcb"));
        
        //Inicializa las imagenes SVG
        svgLogo.setSvgImage("svg.svg", 166, 37);
        
        
        
        //Inicializa los estilos de los JButton
        btnViewInv.setHorizontalAlignment(SwingConstants.LEFT);
        btnViewInv.setMargin(new Insets(0, 50, 0, 0));
        btnViewInv.setText("<html><body style='padding-left: 10px;'>View Inventory</body></html>");
        btnEditInv.setHorizontalAlignment(SwingConstants.LEFT);
        btnEditInv.setMargin(new Insets(0, 50, 0, 0));
        btnEditInv.setText("<html><body style='padding-left: 10px;'>Edit Inventory</body></html>");
        
        btnViewInv.setBackground(Color.decode("#4e4e4e"));
        btnEditInv.setBackground(Color.decode("#4e4e4e"));
        
        btnViewInv.setForeground(Color.white);
        btnEditInv.setForeground(Color.white); 
    }
    
    private void InitContent()
    {
        pnlPrincipal prin = new pnlPrincipal(this);
        prin.setSize(947, 684);
        prin.setLocation(0, 0);
        prin.setWorkInv(this);
        
        pnlContent.removeAll();
        pnlContent.add(prin, BorderLayout.CENTER);
        pnlContent.revalidate();
        pnlContent.repaint();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlBackground = new javax.swing.JPanel();
        pnlMenuL = new javax.swing.JPanel();
        svgLogo = new com.mycompany.sneaksapp.igu.SVGImage();
        btnViewInv = new javax.swing.JButton();
        btnEditInv = new javax.swing.JButton();
        lblSnkscom = new javax.swing.JLabel();
        lblWelcome = new javax.swing.JLabel();
        pnlContent = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(0, 0));

        pnlBackground.setBackground(new java.awt.Color(255, 255, 255));

        svgLogo.setText("sVGImage1");

        btnViewInv.setFont(new java.awt.Font("Segoe UI Light", 0, 31)); // NOI18N
        btnViewInv.setText("View Inventory");
        btnViewInv.setBorder(null);
        btnViewInv.setBorderPainted(false);
        btnViewInv.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnViewInv.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnViewInvMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnViewInvMouseExited(evt);
            }
        });
        btnViewInv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewInvActionPerformed(evt);
            }
        });

        btnEditInv.setFont(new java.awt.Font("Segoe UI Light", 0, 31)); // NOI18N
        btnEditInv.setText("Edit Inventory");
        btnEditInv.setBorder(null);
        btnEditInv.setBorderPainted(false);
        btnEditInv.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditInv.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEditInvMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEditInvMouseExited(evt);
            }
        });
        btnEditInv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditInvActionPerformed(evt);
            }
        });

        lblSnkscom.setFont(new java.awt.Font("Segoe UI Light", 0, 18)); // NOI18N
        lblSnkscom.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lblWelcome.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblWelcome.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblWelcome.setText("Welcome Manager");
        lblWelcome.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout pnlMenuLLayout = new javax.swing.GroupLayout(pnlMenuL);
        pnlMenuL.setLayout(pnlMenuLLayout);
        pnlMenuLLayout.setHorizontalGroup(
            pnlMenuLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnViewInv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnEditInv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlMenuLLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlMenuLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlMenuLLayout.createSequentialGroup()
                        .addComponent(lblSnkscom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMenuLLayout.createSequentialGroup()
                        .addGap(0, 39, Short.MAX_VALUE)
                        .addComponent(svgLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44))))
            .addComponent(lblWelcome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlMenuLLayout.setVerticalGroup(
            pnlMenuLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMenuLLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(svgLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(btnEditInv, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnViewInv, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblWelcome, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSnkscom, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45))
        );

        pnlContent.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pnlContentLayout = new javax.swing.GroupLayout(pnlContent);
        pnlContent.setLayout(pnlContentLayout);
        pnlContentLayout.setHorizontalGroup(
            pnlContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 947, Short.MAX_VALUE)
        );
        pnlContentLayout.setVerticalGroup(
            pnlContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 684, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlBackgroundLayout = new javax.swing.GroupLayout(pnlBackground);
        pnlBackground.setLayout(pnlBackgroundLayout);
        pnlBackgroundLayout.setHorizontalGroup(
            pnlBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBackgroundLayout.createSequentialGroup()
                .addComponent(pnlMenuL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlBackgroundLayout.setVerticalGroup(
            pnlBackgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMenuL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlBackground, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlBackground, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEditInvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditInvActionPerformed
        EditTrueBtn = true;
        SellTrueBtn = false;
        ViewTrueBtn = false;
        btnEditInv.setBackground(Color.decode("#353535"));
        btnViewInv.setBackground(Color.decode("#4e4e4e"));
        
        
        pnlEdit edit = new pnlEdit(this);
        edit.setSize(947, 684);
        edit.setLocation(0, 0);
        edit.setWorkInv(this);
        
        pnlContent.removeAll();
        pnlContent.add(edit, BorderLayout.CENTER);
        pnlContent.revalidate();
        pnlContent.repaint();
        
    }//GEN-LAST:event_btnEditInvActionPerformed

    private void btnViewInvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewInvActionPerformed
        ViewTrueBtn = true;
        EditTrueBtn = false;
        SellTrueBtn = false;
        btnViewInv.setBackground(Color.decode("#353535"));
        btnEditInv.setBackground(Color.decode("#4e4e4e"));
        
        pnlView view = new pnlView(this);
        view.setSize(947, 684);
        view.setLocation(0, 0);
        view.setWorkInv(this);
        
        pnlContent.removeAll();
        pnlContent.add(view, BorderLayout.CENTER);
        pnlContent.revalidate();
        pnlContent.repaint();
    }//GEN-LAST:event_btnViewInvActionPerformed

    private void btnEditInvMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditInvMouseEntered
        if (!EditTrueBtn)
        {
           btnEditInv.setBackground(Color.decode("#484848")); 
        }
        
    }//GEN-LAST:event_btnEditInvMouseEntered

    private void btnEditInvMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditInvMouseExited
        if (!EditTrueBtn)
        {
           btnEditInv.setBackground(Color.decode("#4e4e4e"));
        }
        
    }//GEN-LAST:event_btnEditInvMouseExited

    private void btnViewInvMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnViewInvMouseEntered
        if (!ViewTrueBtn)
        {
           btnViewInv.setBackground(Color.decode("#484848")); 
        }
    }//GEN-LAST:event_btnViewInvMouseEntered

    private void btnViewInvMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnViewInvMouseExited
        if (!ViewTrueBtn)
        {
           btnViewInv.setBackground(Color.decode("#4e4e4e"));
        }
    }//GEN-LAST:event_btnViewInvMouseExited

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEditInv;
    private javax.swing.JButton btnViewInv;
    private javax.swing.JLabel lblSnkscom;
    private javax.swing.JLabel lblWelcome;
    private javax.swing.JPanel pnlBackground;
    private javax.swing.JPanel pnlContent;
    private javax.swing.JPanel pnlMenuL;
    private com.mycompany.sneaksapp.igu.SVGImage svgLogo;
    // End of variables declaration//GEN-END:variables
}
