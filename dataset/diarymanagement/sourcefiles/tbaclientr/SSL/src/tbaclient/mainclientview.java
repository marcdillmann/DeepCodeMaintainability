package tbaclient;


import TBA.CLIENT.GUI.LogOnWindow;
import TBA.Communications.ServerCom;
import TBA.Data.*;
import TBA.Exceptions.ServerComException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;


/**
 *This is the main client view class
 * @author Joe
 * @author Dan McGrath
 *
 * @version $Rev::               $ $Date::             $
 */
public class mainclientview extends javax.swing.JFrame
{
   public User currentUser;
   public ServerCom server;
   LogOnWindow lWindow;


    /** Creates new form mainclientview */
   public mainclientview()
   {
      System.setProperty("javax.net.ssl.trustStore", "tbaKeyStore");
      System.setProperty("javax.net.ssl.trustStorePassword", "Rgr4j9");

      initComponents();
      initPanelPos();
      server = new ServerCom();
      lWindow = new LogOnWindow();
      try
      {
         server.Start("localhost", 9999);
         lWindow.server = server;
      }
      catch(ServerComException ex)
      {

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

        jPanel2 = new javax.swing.JPanel();
        displayName = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        jPanel1 = new javax.swing.JLayeredPane();
        jPanel3 = new tbaclient.DayPanel();
        jPanel4 = new tbaclient.DayPanel();
        jPanel5 = new tbaclient.DayPanel();
        jPanel6 = new tbaclient.DayPanel();
        jPanel7 = new tbaclient.DayPanel();
        jPanel8 = new tbaclient.DayPanel();
        jPanel9 = new tbaclient.DayPanel();
        jPanel10 = new tbaclient.DayPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });

        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 4, true));

        displayName.setText("[No logged on]");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(displayName, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(displayName)
                .addContainerGap(438, Short.MAX_VALUE))
        );

        jToolBar1.setRollover(true);

        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jPanel1.setAutoscrolls(true);
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel1MousePressed(evt);
            }
        });
        jPanel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanel1MouseDragged(evt);
            }
        });
        jPanel3.setBounds(240, 10, 145, 436);
        jPanel1.add(jPanel3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jPanel4.setBounds(240, 10, 145, 436);
        jPanel1.add(jPanel4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jPanel5.setBounds(240, 10, 145, 436);
        jPanel1.add(jPanel5, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jPanel6.setBounds(240, 10, 145, 436);
        jPanel1.add(jPanel6, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jPanel7.setBounds(240, 10, 145, 436);
        jPanel1.add(jPanel7, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jPanel8.setBounds(240, 10, 145, 436);
        jPanel1.add(jPanel8, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jPanel9.setBounds(240, 10, 145, 436);
        jPanel1.add(jPanel9, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jPanel10.setBounds(240, 10, 145, 436);
        jPanel1.add(jPanel10, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jMenu1.setText("File");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Log On");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(35, 35, 35)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 858, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 1069, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem1ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem1ActionPerformed
       lWindow.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void formFocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_formFocusGained
    {//GEN-HEADEREND:event_formFocusGained
      if(lWindow.loggedOn)
         displayName.setText(lWindow.thisUser.getDisplayName());
      else
         displayName.setText("[No logged on]");
    }//GEN-LAST:event_formFocusGained

    private void jPanel1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseDragged
        offsetPanels();
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel1MouseDragged

    private void jPanel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MousePressed
        getMousePos();
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel1MousePressed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                  mainclientview Main = new mainclientview();
                  Main.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel displayName;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JLayeredPane jPanel1;
    private tbaclient.DayPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private tbaclient.DayPanel jPanel3;
    private tbaclient.DayPanel jPanel4;
    private tbaclient.DayPanel jPanel5;
    private tbaclient.DayPanel jPanel6;
    private tbaclient.DayPanel jPanel7;
    private tbaclient.DayPanel jPanel8;
    private tbaclient.DayPanel jPanel9;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
    private int prevPos = 0;

    private void getMousePos() {
        PointerInfo a = MouseInfo.getPointerInfo();
        Point b = a.getLocation();
        int x = (int) b.getX();
        prevPos = x;
    }

    private void offsetPanels(){
        int offset;

        PointerInfo a = MouseInfo.getPointerInfo();
        Point b = a.getLocation();
        int x = (int) b.getX();

        if (x>prevPos){
            offset = x - prevPos;
        }
        else{
            offset = x - prevPos;
        }

        Point tmp = new Point();
        
        tmp = jPanel3.getLocation();
        tmp.x += offset;
        jPanel3.setLocation(tmp);

        tmp = jPanel4.getLocation();
        tmp.x += offset;
        jPanel4.setLocation(tmp);

        tmp = jPanel5.getLocation();
        tmp.x += offset;
        jPanel5.setLocation(tmp);

        tmp = jPanel6.getLocation();
        tmp.x += offset;
        jPanel6.setLocation(tmp);

        tmp = jPanel7.getLocation();
        tmp.x += offset;
        jPanel7.setLocation(tmp);

        tmp = jPanel8.getLocation();
        tmp.x += offset;
        jPanel8.setLocation(tmp);

        tmp = jPanel9.getLocation();
        tmp.x += offset;
        jPanel9.setLocation(tmp);

        tmp = jPanel10.getLocation();
        tmp.x += offset;
        jPanel10.setLocation(tmp);
        checkPanelPos();

        prevPos = x;

    }

    private void checkPanelPos(){
        int width, panel1Width;

        panel1Width = jPanel1.getWidth();
        width = jPanel3.getWidth();

        Point tmp = new Point();
        Point tmp1 = new Point();

        tmp = jPanel3.getLocation();
        if (tmp.x+width/2 < -138 ){
            tmp1 = jPanel10.getLocation();
            tmp.x = tmp1.x + 158;
            jPanel3.setLocation(tmp);
            //set the new pos coming from the right
        }
        else if (tmp.x > panel1Width + 90 ){
            tmp1 = jPanel4.getLocation();
            tmp.x = tmp1.x - 158;
            jPanel3.setLocation(tmp);
            //set the new pos coming from the left
        }

        tmp = jPanel4.getLocation();
        if (tmp.x+width/2 < -138 ){
            tmp1 = jPanel3.getLocation();
            tmp.x = tmp1.x + 158;
            jPanel4.setLocation(tmp);
            //set the new pos coming from the right
        }
        else if (tmp.x > panel1Width + 90 ){
            tmp1 = jPanel5.getLocation();
            tmp.x = tmp1.x - 158;
            jPanel4.setLocation(tmp);
            //set the new pos coming from the left
        }

        tmp = jPanel5.getLocation();
        if (tmp.x+width/2 < -138 ){
            tmp1 = jPanel4.getLocation();
            tmp.x = tmp1.x + 158;
            jPanel5.setLocation(tmp);
            //set the new pos coming from the right
        }
        else if (tmp.x > panel1Width + 90 ){
            tmp1 = jPanel6.getLocation();
            tmp.x = tmp1.x - 158;
            jPanel5.setLocation(tmp);
            //set the new pos coming from the left
        }

        tmp = jPanel6.getLocation();
        if (tmp.x+width/2 < -138 ){
            tmp1 = jPanel5.getLocation();
            tmp.x = tmp1.x + 158;
            jPanel6.setLocation(tmp);
            //set the new pos coming from the right
        }
        else if (tmp.x > panel1Width + 90 ){
            tmp1 = jPanel7.getLocation();
            tmp.x = tmp1.x - 158;
            jPanel6.setLocation(tmp);
            //set the new pos coming from the left
        }

        tmp = jPanel7.getLocation();
        if (tmp.x+width/2 < -138 ){
            tmp1 = jPanel6.getLocation();
            tmp.x = tmp1.x + 158;
            jPanel7.setLocation(tmp);
            //set the new pos coming from the right
        }
        else if (tmp.x > panel1Width + 90 ){
            tmp1 = jPanel8.getLocation();
            tmp.x = tmp1.x - 158;
            jPanel7.setLocation(tmp);
            //set the new pos coming from the left
        }

        tmp = jPanel8.getLocation();
        if (tmp.x+width/2 < -138 ){
            tmp1 = jPanel7.getLocation();
            tmp.x = tmp1.x + 158;
            jPanel8.setLocation(tmp);
            //set the new pos coming from the right
        }
        else if (tmp.x > panel1Width + 90 ){
            tmp1 = jPanel9.getLocation();
            tmp.x = tmp1.x - 158;
            jPanel8.setLocation(tmp);
            //set the new pos coming from the left
        }

        tmp = jPanel9.getLocation();
        if (tmp.x+width/2 < -138 ){
            tmp1 = jPanel8.getLocation();
            tmp.x = tmp1.x + 158;
            jPanel9.setLocation(tmp);
            //set the new pos coming from the right
        }
        //else if (tmp.x > panel1Width - width/2 ){
        else if (tmp.x > panel1Width + 90 ){
            tmp1 = jPanel10.getLocation();
            tmp.x = tmp1.x - 158;
            jPanel9.setLocation(tmp);
            //set the new pos coming from the left
        }

        tmp = jPanel10.getLocation();
        if (tmp.x+width/2 < -138 ){
            tmp1 = jPanel9.getLocation();
            tmp.x = tmp1.x + 158;
            jPanel10.setLocation(tmp);
            //set the new pos coming from the right
        }
        //else if (tmp.x > panel1Width - width/2 ){
        else if (tmp.x > panel1Width + 90 ){
            tmp1 = jPanel3.getLocation();
            tmp.x = tmp1.x - 158;
            jPanel10.setLocation(tmp);
        }
    }

    private void initPanelPos() {
        Point tmp = new Point();

        tmp = jPanel3.getLocation();

        tmp.x = -115;
        jPanel3.setLocation(tmp);
        tmp.x = tmp.x + 158;
        jPanel4.setLocation(tmp);
        tmp.x = tmp.x + 158;
        jPanel5.setLocation(tmp);
        tmp.x = tmp.x + 158;
        jPanel6.setLocation(tmp);
        tmp.x = tmp.x + 158;
        jPanel7.setLocation(tmp);
        tmp.x = tmp.x + 158;
        jPanel8.setLocation(tmp);
        tmp.x = tmp.x + 158;
        jPanel9.setLocation(tmp);
        tmp.x = tmp.x + 158;
        jPanel10.setLocation(tmp);

    }
    //[235,233,237] = original color
}