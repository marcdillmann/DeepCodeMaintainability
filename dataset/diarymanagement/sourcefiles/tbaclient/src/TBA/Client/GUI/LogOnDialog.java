/*
 * LogOnDialog.java
 *
 * Created on 4/09/2009, 13:37:54
 */

package TBA.Client.GUI;

import TBA.Communications.ServerCom;
import TBA.Exceptions.ServerComException;
import TBA.Data.*;
import java.awt.Component;
import javax.swing.JOptionPane;

/**
 * This class is the GUI Form for logging in.
 * @author Dan McGrath
 */
public class LogOnDialog extends javax.swing.JDialog
{
   /** server is isntance of ServerCom which has login and otehr server functions */
   public static ServerCom server;
   private SessionState session;
   private String user;

   /** Creates new form LogOnDialog
    * @param parent
    * @param modal
    */
   public LogOnDialog(java.awt.Frame parent, boolean modal)
   {
      super(parent, modal);
      initComponents();
      this.setLocationRelativeTo(null);
      this.setTitle("Login");
   }

   /**
    * This class attempts to log in with the supply credentials
    */
   private void attemptLogon()
   {
      try
      {
         User newUser = server.Login(UsernameField.getText(), PasswordField.getText());
         session.setActiveUser(newUser);
         if (session.getActiveUser() == null)
         {
            PasswordField.setText("");
            String errorMsg = server.getLastError();
            JOptionPane.showConfirmDialog((Component)null, errorMsg, "Logon Failed", JOptionPane.DEFAULT_OPTION);
         }
         else
         {
            Diary newDiary = server.GetDiary(newUser.getDefaultDiaryID(), newUser.getSessionID());
            if(newDiary == null)
            {
               String errorMsg = server.getLastError();
               JOptionPane.showConfirmDialog((Component)null, errorMsg, "Diary Error", JOptionPane.DEFAULT_OPTION);

               // Attempt to retrieve another one of the user's diaries
               for(Diary nextDiary : newUser.getDiaries())
               {
                  newDiary = server.GetDiary(nextDiary.getID(), newUser.getSessionID());
                  if(newDiary != null)
                  { // Diary found
                     break;
                  }

                  // Diary not retrieved
                  errorMsg = server.getLastError();
                  JOptionPane.showConfirmDialog((Component)null, errorMsg, "Diary Error", JOptionPane.DEFAULT_OPTION);
               }

               // Check if no diaries where retrieved
               if(newDiary == null)
               {
                  PasswordField.setText("");
                  session.setActiveUser(null);
                  JOptionPane.showConfirmDialog((Component)null, "User has no valid diaries", "Logon Failed", JOptionPane.DEFAULT_OPTION);

               }
            }

            // Set the active diary for this user.
            session.getActiveUser().replaceDiary(newDiary);
            session.setActiveDiary(newDiary);
            session.setUpdate();
            setVisible(false);
         }
      }
      catch (ServerComException ex)
      {
         String errorMsg = "Sorry, TBA Client experienced an unexcepted error logging on.\nPlease try again. You may need to restart the program.";
         JOptionPane.showConfirmDialog((Component)null, errorMsg, "Unexpected Error", JOptionPane.DEFAULT_OPTION);
         setVisible(false);
      }
   }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        UsernameField = new javax.swing.JTextField();
        PasswordField = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        LogOnButton = new javax.swing.JButton();
        CancelButton = new javax.swing.JButton();
        chkRememberUser = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        PasswordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PasswordFieldActionPerformed(evt);
            }
        });

        jLabel1.setText("Username:");

        jLabel2.setText("Password:");

        LogOnButton.setText("Log On");
        LogOnButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogOnButtonActionPerformed(evt);
            }
        });

        CancelButton.setText("Cancel");
        CancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(PasswordField)
                            .addComponent(UsernameField, javax.swing.GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(LogOnButton, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CancelButton, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(UsernameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LogOnButton)
                    .addComponent(CancelButton))
                .addContainerGap())
        );

        chkRememberUser.setText("Remember User");
        chkRememberUser.setFocusCycleRoot(true);
        chkRememberUser.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(chkRememberUser)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkRememberUser))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void LogOnButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_LogOnButtonActionPerformed
    {//GEN-HEADEREND:event_LogOnButtonActionPerformed
      attemptLogon();
      if(chkRememberUser.isSelected())
      {
          UsernameField.getText();
      }
}//GEN-LAST:event_LogOnButtonActionPerformed

    private void CancelButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_CancelButtonActionPerformed
    {//GEN-HEADEREND:event_CancelButtonActionPerformed
       setVisible(false);
}//GEN-LAST:event_CancelButtonActionPerformed

    private void PasswordFieldActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_PasswordFieldActionPerformed
    {//GEN-HEADEREND:event_PasswordFieldActionPerformed
      attemptLogon();
    }//GEN-LAST:event_PasswordFieldActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CancelButton;
    private javax.swing.JButton LogOnButton;
    private javax.swing.JTextField PasswordField;
    private javax.swing.JTextField UsernameField;
    private javax.swing.JCheckBox chkRememberUser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables

   /**
    * Give this class a pointer to the SessionState object.
    * @param session The SessionState
    */
   public void setSession(SessionState session)
   {
      this.session = session;
   }

   /**
    * Getter for the User
    * @return UsermnameField
    */
   public String getUser() {
        return UsernameField.getText();
    }

   /**
    * Setter for username and checkbox fields
    * @param user
    */
   public void setUser(String user) {
        UsernameField.setText(user);
        chkRememberUser.setSelected(true);

    }

    /**
     * This method returns whether or not the user wishes to be remebered or not
     * @return checkbox value
     */
    public boolean isRemembered()
    {
        return chkRememberUser.isSelected();
    }
}
