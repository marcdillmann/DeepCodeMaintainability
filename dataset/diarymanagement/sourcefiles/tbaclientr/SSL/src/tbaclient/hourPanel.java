/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * hourPanel.java
 *
 * Created on 28/08/2009, 16:37:34
 */

package tbaclient;

import java.awt.Color;
import javax.swing.border.LineBorder;

/**
 *
 * @author cs321tx2
 */
public class hourPanel extends javax.swing.JPanel {

    /** Creates new form hourPanel */
    public hourPanel() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseEntered(java.awt.event.MouseEvent evt) {
            formMouseEntered(evt);
         }
         public void mouseExited(java.awt.event.MouseEvent evt) {
            formMouseExited(evt);
         }
      });

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
      this.setLayout(layout);
      layout.setHorizontalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGap(0, 400, Short.MAX_VALUE)
      );
      layout.setVerticalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGap(0, 300, Short.MAX_VALUE)
      );
   }// </editor-fold>//GEN-END:initComponents

    private void formMouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_formMouseEntered
    {//GEN-HEADEREND:event_formMouseEntered
        this.setBorder(new LineBorder(Color.BLACK, 2));
        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseEntered

    private void formMouseExited(java.awt.event.MouseEvent evt)//GEN-FIRST:event_formMouseExited
    {//GEN-HEADEREND:event_formMouseExited
        this.setBorder(new LineBorder(Color.BLACK, 1));
        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseExited


   // Variables declaration - do not modify//GEN-BEGIN:variables
   // End of variables declaration//GEN-END:variables

}