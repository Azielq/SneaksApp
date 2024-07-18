package com.mycompany.sneaksapp.igu;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.mycompany.sneaksapp.logica.Usuario;
import java.awt.Color;
import java.awt.Cursor;
import javax.swing.JOptionPane;


public class Login extends javax.swing.JFrame 
{

    protected final String user = "Admin";
    protected final String pass = "Admin1234";
    
    public Login() 
    {
        initComponents();
        initStyles();

    }
    
    private void initStyles()
    {
        //Poner icono para windows 
        FlatSVGIcon icon = new FlatSVGIcon("isotipo.svg");
        setIconImage(icon.getImage());
        
        pnlLogIn.setBackground(Color.white);
        pnlSVG.setBackground(Color.decode("#eeece8"));
        svgLogImg.setSvgImage("login.svgz", svgLogImg.getWidth(), svgLogImg.getHeight());
        btnLogin.setBackground(Color.decode("#222222"));
        btnLogin.setForeground(Color.white);
        btnLogin.setText("<html><body style='padding-top: 10px; padding-bottom: 15px;'>Log In</body></html>");
        
        lblUsername.requestFocusInWindow();
        sbtnPwView.setSvgImage("eyeclosed.svg", sbtnPwView.getWidth(), sbtnPwView.getHeight());
        sbtnPwView.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        sbtnGoBack.setSvgImage("back arrow.svg", sbtnGoBack.getWidth(), sbtnGoBack.getHeight());
        sbtnGoBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlBG = new javax.swing.JPanel();
        pnlLogIn = new javax.swing.JPanel();
        lblPassword = new javax.swing.JLabel();
        lblUsername = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        sptUsername = new javax.swing.JSeparator();
        sptPassword = new javax.swing.JSeparator();
        btnLogin = new javax.swing.JButton();
        lblSneaks = new javax.swing.JLabel();
        pwPassword = new javax.swing.JPasswordField();
        sbtnPwView = new com.mycompany.sneaksapp.igu.SVGImage();
        sbtnGoBack = new com.mycompany.sneaksapp.igu.SVGImage();
        pnlSVG = new javax.swing.JPanel();
        svgLogImg = new com.mycompany.sneaksapp.igu.SVGImage();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlBG.setBackground(new java.awt.Color(255, 255, 255));

        pnlLogIn.setBackground(new java.awt.Color(204, 204, 255));

        lblPassword.setFont(new java.awt.Font("Segoe UI Light", 0, 36)); // NOI18N
        lblPassword.setText("Type your password:");

        lblUsername.setFont(new java.awt.Font("Segoe UI Light", 0, 36)); // NOI18N
        lblUsername.setText("Type your username:");

        txtUsername.setFont(new java.awt.Font("Segoe UI Light", 0, 14)); // NOI18N
        txtUsername.setForeground(java.awt.Color.lightGray);
        txtUsername.setText("Username here...");
        txtUsername.setBorder(null);
        txtUsername.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txtUsername.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtUsernameMouseClicked(evt);
            }
        });
        txtUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsernameActionPerformed(evt);
            }
        });

        sptUsername.setBackground(new java.awt.Color(0, 0, 0));
        sptUsername.setForeground(new java.awt.Color(0, 0, 0));
        sptUsername.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        sptPassword.setBackground(new java.awt.Color(0, 0, 0));
        sptPassword.setForeground(new java.awt.Color(0, 0, 0));
        sptPassword.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        btnLogin.setBackground(new java.awt.Color(51, 51, 51));
        btnLogin.setFont(new java.awt.Font("Segoe UI Light", 0, 35)); // NOI18N
        btnLogin.setText("Log In");
        btnLogin.setBorder(null);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogin.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLogin.setIconTextGap(5);
        btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLoginMouseClicked(evt);
            }
        });

        lblSneaks.setFont(new java.awt.Font("Segoe UI Light", 0, 21)); // NOI18N
        lblSneaks.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSneaks.setText("Sneaks.com");

        pwPassword.setFont(new java.awt.Font("Segoe UI Light", 0, 14)); // NOI18N
        pwPassword.setForeground(java.awt.Color.lightGray);
        pwPassword.setText("•••••••••••••");
        pwPassword.setToolTipText("");
        pwPassword.setBorder(null);
        pwPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                pwPasswordFocusGained(evt);
            }
        });
        pwPassword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pwPasswordMouseClicked(evt);
            }
        });
        pwPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pwPasswordActionPerformed(evt);
            }
        });

        sbtnPwView.setText("sVGImage1");
        sbtnPwView.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                sbtnPwViewMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                sbtnPwViewMouseReleased(evt);
            }
        });

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

        javax.swing.GroupLayout pnlLogInLayout = new javax.swing.GroupLayout(pnlLogIn);
        pnlLogIn.setLayout(pnlLogInLayout);
        pnlLogInLayout.setHorizontalGroup(
            pnlLogInLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLogInLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlLogInLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLogInLayout.createSequentialGroup()
                        .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(210, 210, 210))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLogInLayout.createSequentialGroup()
                        .addComponent(lblSneaks, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(257, 257, 257))))
            .addGroup(pnlLogInLayout.createSequentialGroup()
                .addGroup(pnlLogInLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlLogInLayout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addGroup(pnlLogInLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnlLogInLayout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addGroup(pnlLogInLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(pnlLogInLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnlLogInLayout.createSequentialGroup()
                                            .addComponent(pwPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(sbtnPwView, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(sptPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 501, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlLogInLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(sptUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 501, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 501, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(lblUsername, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 509, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlLogInLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(sbtnGoBack, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(57, Short.MAX_VALUE))
        );
        pnlLogInLayout.setVerticalGroup(
            pnlLogInLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLogInLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sbtnGoBack, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(119, 119, 119)
                .addComponent(lblUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sptUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(lblPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlLogInLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(sbtnPwView, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pwPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sptPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(lblSneaks, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(116, 116, 116))
        );

        pnlSVG.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pnlSVGLayout = new javax.swing.GroupLayout(pnlSVG);
        pnlSVG.setLayout(pnlSVGLayout);
        pnlSVGLayout.setHorizontalGroup(
            pnlSVGLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(svgLogImg, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlSVGLayout.setVerticalGroup(
            pnlSVGLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(svgLogImg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlBGLayout = new javax.swing.GroupLayout(pnlBG);
        pnlBG.setLayout(pnlBGLayout);
        pnlBGLayout.setHorizontalGroup(
            pnlBGLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBGLayout.createSequentialGroup()
                .addComponent(pnlLogIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(558, Short.MAX_VALUE))
            .addGroup(pnlBGLayout.createSequentialGroup()
                .addGap(648, 648, 648)
                .addComponent(pnlSVG, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlBGLayout.setVerticalGroup(
            pnlBGLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlSVG, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlLogIn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlBG, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlBG, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsernameActionPerformed
        LoginAttempt1();
    }//GEN-LAST:event_txtUsernameActionPerformed

    private void sbtnPwViewMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sbtnPwViewMousePressed
        sbtnPwView.setSvgImage("eyeopen.svg", sbtnPwView.getWidth(), sbtnPwView.getHeight());
        pwPassword.setEchoChar((char) 0);
        
        
    }//GEN-LAST:event_sbtnPwViewMousePressed

    private void sbtnPwViewMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sbtnPwViewMouseReleased
        sbtnPwView.setSvgImage("eyeclosed.svg", sbtnPwView.getWidth(), sbtnPwView.getHeight());
        pwPassword.setEchoChar('•');
        //Punto de bala ('•'): Unicode U+2022
    }//GEN-LAST:event_sbtnPwViewMouseReleased

    private void sbtnGoBackMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sbtnGoBackMouseEntered
        sbtnGoBack.setSvgImage("hvrBackArrow.svg", sbtnGoBack.getWidth(), sbtnGoBack.getHeight());
    }//GEN-LAST:event_sbtnGoBackMouseEntered

    private void sbtnGoBackMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sbtnGoBackMouseExited
        sbtnGoBack.setSvgImage("back arrow.svg", sbtnGoBack.getWidth(), sbtnGoBack.getHeight());
    }//GEN-LAST:event_sbtnGoBackMouseExited

    private void sbtnGoBackMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sbtnGoBackMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_sbtnGoBackMousePressed

    private void sbtnGoBackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sbtnGoBackMouseClicked
        sbtnGoBack.setSvgImage("clkBackArrow.svg", sbtnGoBack.getWidth(), sbtnGoBack.getHeight());
        MenuPrincipal back = new MenuPrincipal();
        back.setVisible(true);
        back.setLocationRelativeTo(null);
        dispose();
    }//GEN-LAST:event_sbtnGoBackMouseClicked

    private void pwPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pwPasswordActionPerformed
        LoginAttempt1();
    }//GEN-LAST:event_pwPasswordActionPerformed

    private void txtUsernameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtUsernameMouseClicked
        if (txtUsername.getText().equals("Username here..."))
        {
            txtUsername.setText("");
            txtUsername.setForeground(Color.black);
        }
        if (String.valueOf(pwPassword.getPassword()).isEmpty())
        {
            pwPassword.setText("•••••••••••••");
            pwPassword.setForeground(Color.lightGray);    
        }
        
        
    }//GEN-LAST:event_txtUsernameMouseClicked

    private void pwPasswordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pwPasswordMouseClicked
        if (txtUsername.getText().isEmpty())
        {
            txtUsername.setText("Username here...");
            txtUsername.setForeground(Color.lightGray);  
        }
        
        if (String.valueOf(pwPassword.getPassword()).equals("•••••••••••••"))
        {
            pwPassword.setText("");
            pwPassword.setForeground(Color.black); 
        }
        
    }//GEN-LAST:event_pwPasswordMouseClicked

    private void btnLoginMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLoginMouseClicked
        LoginAttempt1();
    }//GEN-LAST:event_btnLoginMouseClicked

    private void pwPasswordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pwPasswordFocusGained
        this.pwPassword.setText("");
        this.pwPassword.setForeground(Color.black);
    }//GEN-LAST:event_pwPasswordFocusGained


    
    private void LoginAttempt1() {
        
    //Verificar que el usuario ponga texto en los dos fields
        if (txtUsername.getText().equals("Username here...") && String.valueOf(pwPassword.getPassword()).equals("•••••••••••••")) {
            javax.swing.JOptionPane.showMessageDialog(null, "Please type your user and password to continue. \nTry Again.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        
        //verificar que el usuario ponga el usuario
        else if (txtUsername.getText().equals("Username here...") || txtUsername.getText().isEmpty())
        {
            javax.swing.JOptionPane.showMessageDialog(null, "Please type your user to continue. \nTry Again.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        //verificar que el usuario ponga contraseña
        else if (String.valueOf(pwPassword.getPassword()).equals("•••••••••••••") || String.valueOf(pwPassword.getPassword()).isEmpty()) 
        {
            javax.swing.JOptionPane.showMessageDialog(null, "Please type your password to continue. \nTry Again.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        
    String username = txtUsername.getText();
    String password = String.valueOf(pwPassword.getPassword());

    // Verifica que el usuario y contraseña coincidan con el tipo seleccionado en MenuPrincipal
    Usuario user = Usuario.verifyLogin(username, password);
    if (user != null && user.getRole().equals(MenuPrincipal.selectedRole)) {
        openAppropriateWindow(user.getRole());
        dispose(); // Cierra la ventana de login
    } else {
        JOptionPane.showMessageDialog(this, "Invalid username or password for selected role.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void openAppropriateWindow(String role) {
    switch (role) {
        case "admin":
            Work adminWindow = new Work();
            adminWindow.setVisible(true);
            adminWindow.setLocationRelativeTo(null);
            break;
        case "seller":
            WorkSell sellwindow = new WorkSell();
            sellwindow.setVisible(true);
            sellwindow.setLocationRelativeTo(null);
            break;
        case "inventory":
            WorkInv editwindow = new WorkInv();
            editwindow.setVisible(true);
            editwindow.setLocationRelativeTo(null);
            break;
    }
}
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLogin;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblSneaks;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JPanel pnlBG;
    private javax.swing.JPanel pnlLogIn;
    private javax.swing.JPanel pnlSVG;
    private javax.swing.JPasswordField pwPassword;
    private com.mycompany.sneaksapp.igu.SVGImage sbtnGoBack;
    private com.mycompany.sneaksapp.igu.SVGImage sbtnPwView;
    private javax.swing.JSeparator sptPassword;
    private javax.swing.JSeparator sptUsername;
    private com.mycompany.sneaksapp.igu.SVGImage svgLogImg;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
