package TBA.Client.Main;

import TBA.Client.GUI.*;
import TBA.Communications.ServerCom;
import TBA.Data.*;
import TBA.Events.TBAEvent;
import TBA.Events.TBAEventListener;
import TBA.Exceptions.ServerComException;
import TBA.Images.ImageResources;
import TBA.Logging.TBALogger;
import TBA.MouseGestures.Gesture;
import TBA.MouseGestures.GesturesResource;
import TBA.Sounds.SoundResources;
import com.smardec.mousegestures.MouseGestures;
import com.smardec.mousegestures.MouseGesturesListener;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Calendar;
import java.util.Vector;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *This is the main client view class
 * @author Joe
 * @author Dan McGrath
 *
 * @version $Rev:: 271           $ $Date:: 2009-10-28 #$
 */
public class mainclientview extends javax.swing.JFrame
{
   private final static Logger LOGIT = Logger.getLogger(mainclientview.class.getName());
   //private static int port = 9999;
   //private static String serverAddress = "localhost";

   private static int port;
   private static String serverAddress = new String();

   private static mainclientview Main;
   MouseGestures mouseGestures = new MouseGestures();
   private ImageResources imageResources = new ImageResources();
   /** server to connect to */
   public ServerCom server;
   private int prevPos = 0;
   private Vector<DayPanel> dayPanelVector = new Vector<DayPanel>();
   private SoundResources soundResources = new SoundResources();
   private GesturesResource gesturesResource = new GesturesResource();
   private SessionState session = new SessionState();
   private Vector<GestureMethod> gestureMethods = new Vector<GestureMethod>();
   private GestureMethod cancelGesture = new GestureMethod();
   private SettingsDialog settingsDialog;


    /** Creates new form mainclientview */
   public mainclientview()
   {
      //so we can grab settings when it starts
      settingsDialog = new SettingsDialog(this, true, soundResources);
      settingsDialog.setGesturesResources(gesturesResource);
      settingsDialog.LoadSettings();

      soundResources.setNowMute(settingsDialog.IsNowMute());
      soundResources.setNowLocation(settingsDialog.getNowLocation());
      //soundResources.Reload();

      ServerCom.extractKeyStore();
      System.setProperty("javax.net.ssl.trustStore", "tbaKeyStore");
      System.setProperty("javax.net.ssl.trustStorePassword", "Rgr4j9");

      initComponents();
      setPanelVector();
      initPanelPos();
      this.setTitle("TBA Diary");
      this.setLocationRelativeTo(null);
      refreshPanels();
      server = new ServerCom();


      //let command line have precedent
      if(serverAddress.isEmpty() == true)
      {
            serverAddress = settingsDialog.getServer();
      }
      else {
            settingsDialog.setServer(serverAddress);
      }
      //let command line have precedent here too
      if(!(port > 1024 && port < 65536))
      {
            port = settingsDialog.getPort();
      } else {
          settingsDialog.setPort(port);
      }


      try
      {
         server.Start(serverAddress, port);
      }
      catch(ServerComException ex)
      { }
      finally
      {
         LogOnDialog.server = server;
      }

        soundResources.setNowMute(settingsDialog.IsNowMute());
        soundResources.setNowLocation(settingsDialog.getNowLocation());
      // Start the mouse gesture engine
      mouseGestureEngine();

   }
   /**
    * This function us used to clear (set to zero) the time section of
    * a Calendar object
    *
    * @param panelDate The Calendar object you wish the time cleared from
    *
    * @see java.util.Calendar
\    */
   private void clearTime(Calendar panelDate)
   {
      panelDate.set(Calendar.AM_PM, Calendar.AM);
      panelDate.set(Calendar.HOUR, 0);
      panelDate.set(Calendar.MINUTE, 0);
      panelDate.set(Calendar.SECOND, 0);
      panelDate.set(Calendar.MILLISECOND, 0);
   }

   /**
    * Display the login dialog box, checking the login status once the dialog
    * is closed.
    *
    * @see #actionCheckLogin()
    */
   private void displayLogin()
   {
      if (session.getActiveUser() == null)
      {
         LogOnDialog logon = new LogOnDialog(Main, true);
         logon.addComponentListener(new java.awt.event.ComponentAdapter()
         {

            @Override
            public void componentHidden(java.awt.event.ComponentEvent e)
            {
               actionCheckLogin();
            }
         });
         logon.setSession(session);

         if(settingsDialog.getUser() != null )
         {
             logon.setUser(settingsDialog.getUser());
         }

         logon.setVisible(true);

         if(logon.isRemembered())
         {
             settingsDialog.setUser(logon.getUser());
             settingsDialog.Update();
         }
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

        leftContainer = new jImagePanel();
        displayName = new javax.swing.JLabel();
        diaryList = new javax.swing.JComboBox();
        datePicker = new DatePicker.JDatePanel();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel1 = new javax.swing.JLabel();
        MouseGestureLight = new javax.swing.JLabel();
        mouseGestureText = new javax.swing.JLabel();
        mainContainer = new jImageLayeredPane();
        fadeRight = new javax.swing.JLabel();
        fadeLeft = new javax.swing.JLabel();
        dayPanel1 = new TBA.Client.Main.DayPanel();
        dayPanel2 = new TBA.Client.Main.DayPanel();
        dayPanel3 = new TBA.Client.Main.DayPanel();
        dayPanel4 = new TBA.Client.Main.DayPanel();
        dayPanel5 = new TBA.Client.Main.DayPanel();
        dayPanel6 = new TBA.Client.Main.DayPanel();
        dayPanel7 = new TBA.Client.Main.DayPanel();
        dayPanel8 = new TBA.Client.Main.DayPanel();
        dragPanel = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        logOnMenuItem = new javax.swing.JMenuItem();
        logOffMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        newEntryMenuItem = new javax.swing.JMenuItem();
        settingsMenu = new javax.swing.JMenu();
        SettingsMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        leftContainer.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        displayName.setText("[Not logged on]");
        displayName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                displayNameMouseClicked(evt);
            }
        });

        diaryList.setMaximumRowCount(16);
        diaryList.setActionCommand("");
        diaryList.setFocusTraversalPolicyProvider(true);
        diaryList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                diaryListMouseClicked(evt);
            }
        });
        diaryList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                diaryListActionPerformed(evt);
            }
        });

        datePicker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                datePickerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout leftContainerLayout = new javax.swing.GroupLayout(leftContainer);
        leftContainer.setLayout(leftContainerLayout);
        leftContainerLayout.setHorizontalGroup(
            leftContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, leftContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(leftContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(diaryList, javax.swing.GroupLayout.Alignment.LEADING, 0, 210, Short.MAX_VALUE)
                    .addComponent(displayName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                    .addComponent(datePicker, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE))
                .addContainerGap())
        );
        leftContainerLayout.setVerticalGroup(
            leftContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(displayName)
                .addGap(18, 18, 18)
                .addComponent(diaryList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(201, Short.MAX_VALUE))
        );

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jLabel1.setText("   ");
        jToolBar1.add(jLabel1);

        MouseGestureLight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/TBA/Images/MGWait.png"))); // NOI18N
        MouseGestureLight.setText("  ");
        jToolBar1.add(MouseGestureLight);
        jToolBar1.add(mouseGestureText);

        mainContainer.setAutoscrolls(true);
        mainContainer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mainContainerMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                mainContainerMousePressed(evt);
            }
        });
        mainContainer.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                mainContainerMouseDragged(evt);
            }
        });

        fadeRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/TBA/Images/backgroundRightFade.png"))); // NOI18N
        fadeRight.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                fadeRightMouseWheelMoved(evt);
            }
        });
        fadeRight.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                fadeRightMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                fadeRightMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                fadeRightMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                fadeRightMouseReleased(evt);
            }
        });
        fadeRight.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                fadeRightMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                fadeRightMouseMoved(evt);
            }
        });
        fadeRight.setBounds(630, 0, 130, 470);
        mainContainer.add(fadeRight, javax.swing.JLayeredPane.DEFAULT_LAYER);

        fadeLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/TBA/Images/backgroundLeftFade.png"))); // NOI18N
        fadeLeft.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                fadeLeftMouseWheelMoved(evt);
            }
        });
        fadeLeft.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fadeLeftMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                fadeLeftMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                fadeLeftMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                fadeLeftMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                fadeLeftMouseReleased(evt);
            }
        });
        fadeLeft.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                fadeLeftMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                fadeLeftMouseMoved(evt);
            }
        });
        fadeLeft.setBounds(0, 0, 150, 470);
        mainContainer.add(fadeLeft, javax.swing.JLayeredPane.DEFAULT_LAYER);

        dayPanel1.setFadeLeft(fadeLeft, fadeRight);
        dayPanel1.setImageResources(imageResources);
        dayPanel1.setSoundResources(soundResources);
        dayPanel1.setOpaque(false);
        dayPanel1.setBounds(330, 10, 145, 460);
        mainContainer.add(dayPanel1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        dayPanel2.setFadeLeft(fadeLeft, fadeRight);
        dayPanel2.setImageResources(imageResources);
        dayPanel2.setSoundResources(soundResources);
        dayPanel2.setOpaque(false);
        dayPanel2.setBounds(330, 10, 145, 460);
        mainContainer.add(dayPanel2, javax.swing.JLayeredPane.DEFAULT_LAYER);

        dayPanel3.setFadeLeft(fadeLeft, fadeRight);
        dayPanel3.setImageResources(imageResources);
        dayPanel3.setSoundResources(soundResources);
        dayPanel3.setOpaque(false);
        dayPanel3.setBounds(330, 10, 145, 460);
        mainContainer.add(dayPanel3, javax.swing.JLayeredPane.DEFAULT_LAYER);

        dayPanel4.setFadeLeft(fadeLeft, fadeRight);
        dayPanel4.setImageResources(imageResources);
        dayPanel4.setSoundResources(soundResources);
        dayPanel4.setOpaque(false);
        dayPanel4.setBounds(330, 10, 145, 460);
        mainContainer.add(dayPanel4, javax.swing.JLayeredPane.DEFAULT_LAYER);

        dayPanel5.setFadeLeft(fadeLeft, fadeRight);
        dayPanel5.setImageResources(imageResources);
        dayPanel5.setSoundResources(soundResources);
        dayPanel5.setOpaque(false);
        dayPanel5.setBounds(330, 10, 145, 460);
        mainContainer.add(dayPanel5, javax.swing.JLayeredPane.DEFAULT_LAYER);

        dayPanel6.setFadeLeft(fadeLeft, fadeRight);
        dayPanel6.setImageResources(imageResources);
        dayPanel6.setSoundResources(soundResources);
        dayPanel6.setOpaque(false);
        dayPanel6.setBounds(330, 10, 145, 460);
        mainContainer.add(dayPanel6, javax.swing.JLayeredPane.DEFAULT_LAYER);

        dayPanel7.setFadeLeft(fadeLeft, fadeRight);
        dayPanel7.setImageResources(imageResources);
        dayPanel7.setSoundResources(soundResources);
        dayPanel7.setOpaque(false);
        dayPanel7.setBounds(330, 10, 145, 460);
        mainContainer.add(dayPanel7, javax.swing.JLayeredPane.DEFAULT_LAYER);

        dayPanel8.setImageResources(imageResources);
        dayPanel8.setSoundResources(soundResources);
        dayPanel8.setFadeLeft(fadeLeft, fadeRight);
        dayPanel8.setOpaque(false);
        dayPanel8.setBounds(330, 10, 145, 460);
        mainContainer.add(dayPanel8, javax.swing.JLayeredPane.DEFAULT_LAYER);

        dragPanel.setOpaque(false);
        dragPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                dragPanelMousePressed(evt);
            }
        });
        dragPanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                dragPanelMouseDragged(evt);
            }
        });

        javax.swing.GroupLayout dragPanelLayout = new javax.swing.GroupLayout(dragPanel);
        dragPanel.setLayout(dragPanelLayout);
        dragPanelLayout.setHorizontalGroup(
            dragPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 770, Short.MAX_VALUE)
        );
        dragPanelLayout.setVerticalGroup(
            dragPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );

        dragPanel.setBounds(-20, 0, 770, 500);
        mainContainer.add(dragPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        fileMenu.setMnemonic('F');
        fileMenu.setText("File");

        logOnMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        logOnMenuItem.setText("Log On");
        logOnMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logOnMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(logOnMenuItem);

        logOffMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        logOffMenuItem.setText("Log Off");
        logOffMenuItem.setEnabled(false);
        logOffMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logOffMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(logOffMenuItem);

        exitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        jMenuBar1.add(fileMenu);

        editMenu.setMnemonic('E');
        editMenu.setText("Edit");

        newEntryMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        newEntryMenuItem.setText("New Entry");
        newEntryMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newEntryMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(newEntryMenuItem);

        jMenuBar1.add(editMenu);

        settingsMenu.setMnemonic('S');
        settingsMenu.setText("Setting");

        SettingsMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        SettingsMenuItem.setText("Preferences");
        SettingsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SettingsMenuItemActionPerformed(evt);
            }
        });
        settingsMenu.add(SettingsMenuItem);

        jMenuBar1.add(settingsMenu);

        helpMenu.setMnemonic('H');
        helpMenu.setText("Help");

        aboutMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        aboutMenuItem.setText("About TBA");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenuItem);

        jMenuBar1.add(helpMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(leftContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainContainer, javax.swing.GroupLayout.PREFERRED_SIZE, 752, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 992, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(leftContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(mainContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
    * Parse the command line arguments
    *<p>
    * @param args Command line arguments. Currently it supports a handful of
    * switches.
    * <p>
    * Firstly, '-p x' or '--port x', where x is the port number you
    * wish the server to listen on.
    * <p>
    * Next is '-l level' or '--log-level level'
    * where 'level' is one of SEVERE, WARNING, INFO, FINE, FINER or FINEST
    * <p>
    * Lastly is '-c' or '--console' which enables logging to the console
    */
   private static void parseArgs(String args[])
   {
      String arg = null;
      for (int argnum = 0; argnum < args.length; argnum++)
      {
         arg = args[argnum];
         if (arg.compareTo("-l") == 0 || arg.compareTo("--log-level") == 0)
         {
            argnum++;
            if (argnum < args.length)
            {
               arg = args[argnum];
               try
               {
                  Level loglevel = Level.parse(arg);
                  TBALogger.setLevel(loglevel);
                  LOGIT.setLevel(loglevel);

                  // Setup this classes logger to write to the console
                  for(Handler logHandler : LOGIT.getHandlers())
                  {
                     logHandler.setLevel(loglevel);
                  }

                  LOGIT.info("Change Log level to: " + arg);
               }
               catch (IllegalArgumentException ex)
               {
                  LOGIT.warning("Invalid Log Level set via parameter: " + arg);
               }
               catch (IOException ex)
               {
                  LOGIT.warning("Error setting log level");
                  LOGIT.info(ex.getLocalizedMessage());
               }
            }
            else
            {
               LOGIT.warning("Missing detail for Log Level argument");
            }
         }
         else if (arg.compareTo("-p") == 0 || arg.compareTo("--port") == 0)
         {
            argnum++;
            if (argnum < args.length)
            {
               arg = args[argnum];
               try
               {
                  int portnum = Integer.parseInt(arg);
                  if(portnum > 1024 && portnum < 65536)
                  {
                     port = portnum;
                  }
                  else
                  {
                     LOGIT.warning("Out of range Port number set via parameter: " + arg);
                  }
               }
               catch (NumberFormatException ex)
               {
                  LOGIT.warning("Invalid Port number set via parameter: " + arg);
               }
            }
            else
            {
               LOGIT.warning("Missing detail for Port Number argument");
            }
         }
         else if (arg.compareTo("-s") == 0 || arg.compareTo("--server") == 0)
         {
            argnum++;
            if (argnum < args.length)
            {
               serverAddress = args[argnum];
               LOGIT.info("Change Server name to: " + arg);
            }
            else
            {
               LOGIT.warning("Missing detail for Server argument");
            }
         }
      }
   }

   /**
    * Called when the "Login" option is selected from the "File" menu
    *
    * @see #displayName
    */
    private void logOnMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_logOnMenuItemActionPerformed
    {//GEN-HEADEREND:event_logOnMenuItemActionPerformed
      displayLogin();
    }//GEN-LAST:event_logOnMenuItemActionPerformed

    /**
     * Called when dragging the mouse on the background panel under
     * the "Day Panels". Performs a check to only action this if not
     * done with the right mouse button
     *
     * @see #offsetPanels()
     */
    private void mainContainerMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mainContainerMouseDragged
       if((evt.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) != MouseEvent.BUTTON3_DOWN_MASK)
       {
         offsetPanels();
       }
    }//GEN-LAST:event_mainContainerMouseDragged

    /**
     * Called when a mouse is clicked on the background panel behind the
     * "Day Panels". Saves the mouse position to be used if the mouse is dragged.
     *
     * @see #getMousePos()
     */
    private void mainContainerMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mainContainerMousePressed
        getMousePos();
    }//GEN-LAST:event_mainContainerMousePressed

    /**
     * Quite simply, exit the application.
     */
    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_exitMenuItemActionPerformed
    {//GEN-HEADEREND:event_exitMenuItemActionPerformed
       LOGIT.severe("Client closed - User exit");
       System.exit(0);
}//GEN-LAST:event_exitMenuItemActionPerformed

    /**
     * This event opens up the AboutDialog, which is static and instantiated in
     * its own main
     */
    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_aboutMenuItemActionPerformed
    {//GEN-HEADEREND:event_aboutMenuItemActionPerformed
        AboutDialog.main(null);
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    /**
     * Performs diary switching from the multiple diary combo box
     */
    private void diaryListActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_diaryListActionPerformed
    {//GEN-HEADEREND:event_diaryListActionPerformed
       Diary selectedDiary = (Diary)diaryList.getSelectedItem();
       if(selectedDiary == null)
       {
          session.setActiveDiary(null);
          refreshPanels();
       }
       else if(selectedDiary.getID() != session.getActiveDiary().getID())
       {
         try
         {
            Diary activeDiary = server.GetDiary(selectedDiary.getID(), session.getActiveUser().getSessionID());
            session.getActiveUser().replaceDiary(activeDiary);
            session.setActiveDiary(activeDiary);
            this.setTitle("TBA Diary - "+displayName.getText()+" - "+diaryList.getSelectedItem().toString());
            refreshPanels();
            LOGIT.fine("User switched diary");
         }
         catch (ServerComException ex)
         {
            LOGIT.severe("Unable to retrieve a diary");
            LOGIT.fine(ex.getLocalizedMessage());
         }
       }
    }//GEN-LAST:event_diaryListActionPerformed

    /**
     * Called when a user selects "Log Off" from the "File" menu
     */
    private void logOffMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_logOffMenuItemActionPerformed
    {//GEN-HEADEREND:event_logOffMenuItemActionPerformed
      try
      {
         actionLogout();
         server.Logout(session.getActiveUser().getSessionID());
         LOGIT.info("User logged out");
      }
      catch (ServerComException ex)
      {
         // Ignore as we can't do anything about this at the moment...
      }
      finally
      {
         // Make sure this happens!
         session.setActiveUser(null);
      }
    }//GEN-LAST:event_logOffMenuItemActionPerformed

    private void mainContainerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mainContainerMouseClicked

    }//GEN-LAST:event_mainContainerMouseClicked

    private void fadeLeftMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_fadeLeftMouseClicked
    {//GEN-HEADEREND:event_fadeLeftMouseClicked
}//GEN-LAST:event_fadeLeftMouseClicked

    private void fadeLeftMouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_fadeLeftMouseEntered
    {//GEN-HEADEREND:event_fadeLeftMouseEntered
}//GEN-LAST:event_fadeLeftMouseEntered

    private void fadeLeftMouseExited(java.awt.event.MouseEvent evt)//GEN-FIRST:event_fadeLeftMouseExited
    {//GEN-HEADEREND:event_fadeLeftMouseExited
}//GEN-LAST:event_fadeLeftMouseExited

    private void fadeLeftMouseMoved(java.awt.event.MouseEvent evt)//GEN-FIRST:event_fadeLeftMouseMoved
    {//GEN-HEADEREND:event_fadeLeftMouseMoved
}//GEN-LAST:event_fadeLeftMouseMoved

    /**
     * Called when a mouse is clicked on the left transparency panel behind the
     * "Day Panels". Saves the mouse position to be used if the mouse is dragged.
     *
     * @see #getMousePos()
     */
    private void fadeLeftMousePressed(java.awt.event.MouseEvent evt)//GEN-FIRST:event_fadeLeftMousePressed
    {//GEN-HEADEREND:event_fadeLeftMousePressed
        getMousePos();
}//GEN-LAST:event_fadeLeftMousePressed

    private void fadeLeftMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_fadeLeftMouseReleased
    {//GEN-HEADEREND:event_fadeLeftMouseReleased
}//GEN-LAST:event_fadeLeftMouseReleased

    private void fadeLeftMouseWheelMoved(java.awt.event.MouseWheelEvent evt)//GEN-FIRST:event_fadeLeftMouseWheelMoved
    {//GEN-HEADEREND:event_fadeLeftMouseWheelMoved
}//GEN-LAST:event_fadeLeftMouseWheelMoved

    /**
     * Called when dragging the mouse on the left transparency panel under
     * the "Day Panels". Performs a check to only action this if not
     * done with the right mouse button
     *
     * @see #offsetPanels()
     */
    private void fadeLeftMouseDragged(java.awt.event.MouseEvent evt)//GEN-FIRST:event_fadeLeftMouseDragged
    {//GEN-HEADEREND:event_fadeLeftMouseDragged
       if((evt.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) != MouseEvent.BUTTON3_DOWN_MASK)
       {
         offsetPanels();
       }
}//GEN-LAST:event_fadeLeftMouseDragged

    /**
     * Called when dragging the mouse on the right transparency panel under
     * the "Day Panels". Performs a check to only action this if not
     * done with the right mouse button
     *
     * @see #offsetPanels()
     */
    private void fadeRightMouseDragged(java.awt.event.MouseEvent evt)//GEN-FIRST:event_fadeRightMouseDragged
    {//GEN-HEADEREND:event_fadeRightMouseDragged
       if((evt.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) != MouseEvent.BUTTON3_DOWN_MASK)
       {
         offsetPanels();
       }
}//GEN-LAST:event_fadeRightMouseDragged

    private void fadeRightMouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_fadeRightMouseEntered
    {//GEN-HEADEREND:event_fadeRightMouseEntered
}//GEN-LAST:event_fadeRightMouseEntered

    private void fadeRightMouseExited(java.awt.event.MouseEvent evt)//GEN-FIRST:event_fadeRightMouseExited
    {//GEN-HEADEREND:event_fadeRightMouseExited
}//GEN-LAST:event_fadeRightMouseExited

    private void fadeRightMouseMoved(java.awt.event.MouseEvent evt)//GEN-FIRST:event_fadeRightMouseMoved
    {//GEN-HEADEREND:event_fadeRightMouseMoved
}//GEN-LAST:event_fadeRightMouseMoved

    /**
     * Called when a mouse is clicked on the left transparency panel behind the
     * "Day Panels". Saves the mouse position to be used if the mouse is dragged.
     *
     * @see #getMousePos()
     */
    private void fadeRightMousePressed(java.awt.event.MouseEvent evt)//GEN-FIRST:event_fadeRightMousePressed
    {//GEN-HEADEREND:event_fadeRightMousePressed
        getMousePos();
}//GEN-LAST:event_fadeRightMousePressed

    private void fadeRightMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_fadeRightMouseReleased
    {//GEN-HEADEREND:event_fadeRightMouseReleased
}//GEN-LAST:event_fadeRightMouseReleased

    private void fadeRightMouseWheelMoved(java.awt.event.MouseWheelEvent evt)//GEN-FIRST:event_fadeRightMouseWheelMoved
    {//GEN-HEADEREND:event_fadeRightMouseWheelMoved
}//GEN-LAST:event_fadeRightMouseWheelMoved

    private void dragPanelMouseDragged(java.awt.event.MouseEvent evt)//GEN-FIRST:event_dragPanelMouseDragged
    {//GEN-HEADEREND:event_dragPanelMouseDragged
       if((evt.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) != MouseEvent.BUTTON3_DOWN_MASK)
       {
         offsetPanels();
       }
    }//GEN-LAST:event_dragPanelMouseDragged

    private void dragPanelMousePressed(java.awt.event.MouseEvent evt)//GEN-FIRST:event_dragPanelMousePressed
    {//GEN-HEADEREND:event_dragPanelMousePressed
        getMousePos();
    }//GEN-LAST:event_dragPanelMousePressed

    private void datePickerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_datePickerActionPerformed
      Point tmp = new Point();
      Calendar panelDate;
      tmp = dayPanel1.getLocation();
      tmp.x = -450;

      panelDate = datePicker.getCalendarClone();
      panelDate.add(Calendar.DAY_OF_YEAR, -4);

      for(DayPanel aDay : dayPanelVector)
      {
         clearTime(panelDate);

         aDay.setDate((Calendar)panelDate.clone());
         panelDate.add(Calendar.DAY_OF_YEAR, 1);
         aDay.setLocation(tmp);

         tmp.x += 158;
      }
      refreshPanels();
    }//GEN-LAST:event_datePickerActionPerformed

    private void newEntryMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_newEntryMenuItemActionPerformed
    {//GEN-HEADEREND:event_newEntryMenuItemActionPerformed
       addNewEntry();
    }//GEN-LAST:event_newEntryMenuItemActionPerformed

    private void SettingsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SettingsMenuItemActionPerformed
        openSettings();
    }//GEN-LAST:event_SettingsMenuItemActionPerformed

    private void displayNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_displayNameMouseClicked
    {//GEN-HEADEREND:event_displayNameMouseClicked
      if (evt.getClickCount() == 2 && session.getActiveUser() == null)
      {
         displayLogin();
      }
    }//GEN-LAST:event_displayNameMouseClicked

    private void diaryListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_diaryListMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_diaryListMouseClicked

    /**
    * @param args the command line arguments
    */
    public static void main(String args[])
    {
      try
      {
         TBALogger.setup("TBAClient.log");
         TBALogger.removeConsole();
         LOGIT.info("Starting Client");
         parseArgs(args);
      }
      catch (IOException ex)
      {
         // Don't let logging stop our client...
         // Just log it to console if possible.
         LOGIT.warning("Error: Could not create the log file");
         LOGIT.info(ex.getLocalizedMessage());
      }

      java.awt.EventQueue.invokeLater(new Runnable()
      {
         public void run()
         {
            Main = new mainclientview();
            Main.setVisible(true);
         }
      });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel MouseGestureLight;
    private javax.swing.JMenuItem SettingsMenuItem;
    private javax.swing.JMenuItem aboutMenuItem;
    private DatePicker.JDatePanel datePicker;
    private TBA.Client.Main.DayPanel dayPanel1;
    private TBA.Client.Main.DayPanel dayPanel2;
    private TBA.Client.Main.DayPanel dayPanel3;
    private TBA.Client.Main.DayPanel dayPanel4;
    private TBA.Client.Main.DayPanel dayPanel5;
    private TBA.Client.Main.DayPanel dayPanel6;
    private TBA.Client.Main.DayPanel dayPanel7;
    private TBA.Client.Main.DayPanel dayPanel8;
    private javax.swing.JComboBox diaryList;
    private javax.swing.JLabel displayName;
    private javax.swing.JPanel dragPanel;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JLabel fadeLeft;
    private javax.swing.JLabel fadeRight;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPanel leftContainer;
    private javax.swing.JMenuItem logOffMenuItem;
    private javax.swing.JMenuItem logOnMenuItem;
    private javax.swing.JLayeredPane mainContainer;
    private javax.swing.JLabel mouseGestureText;
    private javax.swing.JMenuItem newEntryMenuItem;
    private javax.swing.JMenu settingsMenu;
    // End of variables declaration//GEN-END:variables

   private void actionCheckLogin()
   {
      LOGIT.fine("Checking if the a user is logged in");
      if(session.getActiveUser() != null)
      {
         if(diaryList.getSelectedIndex() == -1)
         {
            actionLogin();
         }
      }
      else
      {
         actionLogout();
      }
   }

   private void actionLogin()
   {
      displayName.setText(session.getActiveUser().getDisplayName());
      diaryList.removeAllItems();
      for(Diary aDiary : session.getActiveUser().getDiaries())
      {
         diaryList.addItem(aDiary);
         if(aDiary.getID() == session.getActiveUser().getDefaultDiaryID())
         {
            diaryList.setSelectedItem(aDiary);
         }
      }
      logOnMenuItem.setEnabled(false);
      logOffMenuItem.setEnabled(true);
      this.setTitle("TBA Diary - "+displayName.getText()+" - "+diaryList.getSelectedItem().toString());
      refreshPanels();
   }

    private void actionLogout()
    {
      logOffMenuItem.setEnabled(false);
      logOnMenuItem.setEnabled(true);
      displayName.setText("[Not logged on]");
      diaryList.removeAllItems();
      session.setActiveDiary(null);
      refreshPanels();
    }

    private void getMousePos()
    {
        PointerInfo a = MouseInfo.getPointerInfo();
        Point b = a.getLocation();
        int x = (int) b.getX();
        prevPos = x;
    }

  /**
   * This initialise Mouse Gestures and handles all the processing
   */
    //gesture = shake - methods = login
   private void mouseGestureEngine()
   {
      // Setup defaults
      for(Gesture nextGesture : gesturesResource.getGestures())
      {
         GestureMethod newMethod = null;

         if(nextGesture.getGestureName().equalsIgnoreCase(settingsDialog.getMethodName("Logon")))
         {
            newMethod = new GestureMethod();
            newMethod.setMethodNum(0);
            newMethod.setDisplayName("Logon");
            newMethod.setAssignedGesture(nextGesture);
         }

         else if (nextGesture.getGestureName().equalsIgnoreCase(settingsDialog.getMethodName("Add New Entry")))
         {
            newMethod = new GestureMethod();
            newMethod.setMethodNum(1);
            newMethod.setDisplayName("Add New Entry");
            newMethod.setAssignedGesture(nextGesture);
         }

         else if (nextGesture.getGestureName().equalsIgnoreCase(settingsDialog.getMethodName("Settings")))
         {
            newMethod = new GestureMethod();
            newMethod.setMethodNum(2);
            newMethod.setDisplayName("Settings");
            newMethod.setAssignedGesture(nextGesture);
         }

         if (newMethod != null)
         {
            gestureMethods.add(newMethod);
         }

         if (nextGesture.getGestureName().equalsIgnoreCase("Shake Vertically"))
         {
            cancelGesture.setMethodNum(-1);
            cancelGesture.setDisplayName("Cancelled");
            cancelGesture.setAssignedGesture(nextGesture);
         }
      }

      mouseGestures.setMouseButton(MouseEvent.BUTTON3_MASK);
      mouseGestures.addMouseGesturesListener(new MouseGesturesListener()
      {
         public void gestureMovementRecognized(String currentGesture)
         {
            String displayText;

            if(!cancelGesture.getAssignedGesture().isCancelled(currentGesture))
            {
               for(GestureMethod method : gestureMethods)
               {
                  if(method.getAssignedGesture().isActive(currentGesture))
                  {
                     displayText = "Mouse Gesture: "+method.getDisplayName();
                     mouseGestureText.setText(displayText);
                     MouseGestureLight.setIcon(imageResources.mouseGestureOn);
                  }
               }
            }
            else
            {
               displayText = "Mouse Gesture: "+cancelGesture.getDisplayName();
               mouseGestureText.setText(displayText);
               MouseGestureLight.setIcon(imageResources.mouseGestureOff);
            }
         }

         //This method is called when the user releases the mouse button finally
         //Just display the current message for a few milliseconds then
         //redisplay the original text
         public void processGesture(String gesture)
         {
            String displayText;
            mouseGestureText.setText("");
            MouseGestureLight.setIcon(imageResources.mouseGestureWait);
            // Follow this format for all processing all mouse gestures.
            // 1) Test if the gesture was cancelled
            // 2) If it wasn't, identify which gesture it was
            if(!cancelGesture.getAssignedGesture().isCancelled(gesture))
            {
               for(GestureMethod method : gestureMethods)
               {
                  if(method.getAssignedGesture().isActive(gesture))
                  {
                     switch(method.getMethodNum())
                     {
                        case 0: // Log On
                           displayLogin();
                           break;
                        case 1: // Add Entry
                           addNewEntry();
                           break;
                        case 2: // Open Settings Dialog
                           openSettings();
                           break;
                        case 3:
                           break;
                        case 4:
                           break;
                     }
                  }
               }
            }
         }
      });
      mouseGestures.start();
   }

    private void offsetPanels()
    {
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

        for(DayPanel aDay : dayPanelVector)
        {
           tmp = aDay.getLocation();
           tmp.x += offset;
           aDay.setLocation(tmp);

        }
        checkPanelPos();
        prevPos = x;
    }

    private void checkPanelPos()
    {
      int panel1Width;
      panel1Width = mainContainer.getWidth();

      Point tmp = new Point();
      Point tmp1 = new Point();

      for(DayPanel aDay : dayPanelVector)
      {
           tmp = aDay.getLocation();
           if (tmp.x < -500 ){
               tmp1 = aDay.getPreviousPanel().getLocation();
               tmp.x = tmp1.x + 158;
               aDay.setLocation(tmp);
               aDay.setDate((Calendar)aDay.getPreviousPanel().getDate().clone());
               aDay.increaseDate();
               setPanelEntries(aDay);
               Calendar updateCal = (Calendar) aDay.getDate().clone();
               updateCal.add(Calendar.DAY_OF_YEAR, -3);
               datePicker.setCal(updateCal);
           }
           else if (tmp.x > panel1Width +50 ){
               tmp1 = aDay.getNextPanel().getLocation();
               tmp.x = tmp1.x - 158;
               aDay.setLocation(tmp);
               aDay.setDate((Calendar)aDay.getNextPanel().getDate().clone());
               aDay.decreaseDate();
               setPanelEntries(aDay);
               Calendar updateCal = (Calendar) aDay.getDate().clone();
               updateCal.add(Calendar.DAY_OF_YEAR, 5);
               datePicker.setCal(updateCal);

           }
      }

      refreshPanels();
    }

   private void initPanelPos()
   {
      Point tmp = new Point();
      Calendar panelDate;
      tmp = dayPanel1.getLocation();
      tmp.x = -450;

      panelDate = Calendar.getInstance();
      panelDate.add(Calendar.DAY_OF_YEAR, -5);

      for(DayPanel aDay : dayPanelVector)
      {
         clearTime(panelDate);

         aDay.setDate((Calendar)panelDate.clone());
         panelDate.add(Calendar.DAY_OF_YEAR, 1);
         aDay.setLocation(tmp);

         tmp.x += 158;
      }
   }

   private void refreshPanels()
   {
      for(DayPanel aDay : dayPanelVector)
      {
         setPanelEntries(aDay);
      }
   }

   private void refreshServer()
   {
      try
      {
         server.UpdateDiary(session.getActiveDiary(), session.getActiveUser().getSessionID());
         //TODO: The following is a temporary measure until UpdateDiary is completed.
         Diary activeDiary = server.GetDiary(session.getActiveDiary().getID(), session.getActiveUser().getSessionID());
         session.getActiveUser().replaceDiary(activeDiary);
         session.setActiveDiary(activeDiary);
      }
      catch (ServerComException ex)
      {
         LOGIT.severe("Error updating the server");
         LOGIT.info(ex.getLocalizedMessage());
      }
   }

   private void addNewEntry()
   {
      if (session.getActiveUser() != null)
      {
         AddEntryDialog entryForm = new AddEntryDialog(null, true);
         entryForm.addComponentListener(new java.awt.event.ComponentAdapter()
         {
            @Override
            public void componentHidden(java.awt.event.ComponentEvent e)
            {
               updateClient();
            }
         });

         entryForm.setEntry(null);
         entryForm.setSession(session);
         entryForm.setCurrentDay(Calendar.getInstance());
         entryForm.setFromToDate();
         entryForm.setVisible(true);
      }
   }

   private void openSettings()
   {
        settingsDialog.setLocationRelativeTo(null);
        settingsDialog.LoadSettings();
        settingsDialog.setVisible(true);

        soundResources.setNowMute(settingsDialog.IsNowMute());
        soundResources.setNowLocation(settingsDialog.getNowLocation());
        //soundResources.Reload();

        gestureMethods.clear();

        for(Gesture nextGesture : gesturesResource.getGestures())
        {
             GestureMethod newMethod = null;

             if(nextGesture.getGestureName().equalsIgnoreCase(settingsDialog.getMethodName("Logon")))
             {
                newMethod = new GestureMethod();
                newMethod.setMethodNum(0);
                newMethod.setDisplayName("Logon");
                newMethod.setAssignedGesture(nextGesture);
             }
             else if (nextGesture.getGestureName().equalsIgnoreCase(settingsDialog.getMethodName("Add New Entry")))
             {
                newMethod = new GestureMethod();
                newMethod.setMethodNum(1);
                newMethod.setDisplayName("Add New Entry");
                newMethod.setAssignedGesture(nextGesture);
             }
             else if (nextGesture.getGestureName().equalsIgnoreCase(settingsDialog.getMethodName("Settings")))
             {
                newMethod = new GestureMethod();
                newMethod.setMethodNum(2);
                newMethod.setDisplayName("Settings");
                newMethod.setAssignedGesture(nextGesture);
             }

             if (newMethod != null)
             {
                gestureMethods.add(newMethod);
             }

             if (nextGesture.getGestureName().equalsIgnoreCase("Shake Vertically"))
             {
                cancelGesture.setMethodNum(-1);
                cancelGesture.setDisplayName("Cancelled");
                cancelGesture.setAssignedGesture(nextGesture);
             }

          }

   }

   private void setPanelEntries(DayPanel aDay)
   {
      if (session.getActiveDiary() != null)
      {
         if (session.getActiveDiary().getEntries() != null)
         {
            aDay.setEntries(session.getActiveDiary().getEntriesByDate(aDay.getDate()));
            return;
         }
      }

      aDay.setEntries(null);
   }


   private void updateClient()
   {
      // Check Session State
      if (session.shouldServerUpdate())
      {
         refreshServer();
         session.serverUpdated();
      }
      if (session.shouldDisplayUpdate())
      {
         refreshPanels();
         session.displayUpdated();
      }
   }

   private void setPanelVector()
   {
      dayPanel1.setPreviousPanel(dayPanel8);
      dayPanel1.setNextPanel(dayPanel2);
      dayPanelVector.add(dayPanel1);

      dayPanel2.setPreviousPanel(dayPanel1);
      dayPanel2.setNextPanel(dayPanel3);
      dayPanelVector.add(dayPanel2);

      dayPanel3.setPreviousPanel(dayPanel2);
      dayPanel3.setNextPanel(dayPanel4);
      dayPanelVector.add(dayPanel3);

      dayPanel4.setPreviousPanel(dayPanel3);
      dayPanel4.setNextPanel(dayPanel5);
      dayPanelVector.add(dayPanel4);

      dayPanel5.setPreviousPanel(dayPanel4);
      dayPanel5.setNextPanel(dayPanel6);
      dayPanelVector.add(dayPanel5);

      dayPanel6.setPreviousPanel(dayPanel5);
      dayPanel6.setNextPanel(dayPanel7);
      dayPanelVector.add(dayPanel6);

      dayPanel7.setPreviousPanel(dayPanel6);
      dayPanel7.setNextPanel(dayPanel8);
      dayPanelVector.add(dayPanel7);

      dayPanel8.setPreviousPanel(dayPanel7);
      dayPanel8.setNextPanel(dayPanel1);
      dayPanelVector.add(dayPanel8);

      for(DayPanel aDay : dayPanelVector)
      {
         aDay.setSession(session);
         aDay.addTBAEventListener(new TBAEventListener()
         {
            public void TBAEventOccurred(TBAEvent evt)
            {
               updateClient();
            }
         });
      }
   }
}

/**
 * This is a dirty hack as a way around having to fake callbacks in Java.
 * If you add a new method that a mouse gesture can call, you need to add a
 * instance of this class to cover it, plus update the switch statement above in
 * {@link mainclientview#mouseGestureEngine()}
 *
 * @author Dan McGrath
 */
class GestureMethod
{
   private int methodNum = -1;
   private String displayName = "";
   private Gesture assignedGesture = null;

   /**
    * @return the methodNum
    */
   public int getMethodNum()
   {
      return methodNum;
   }

   /**
    * @param methodNum the methodNum to set
    */
   public void setMethodNum(int methodNum)
   {
      this.methodNum = methodNum;
   }

   /**
    * @return the displayName
    */
   public String getDisplayName()
   {
      return displayName;
   }

   /**
    * @param displayName the displayName to set
    */
   public void setDisplayName(String displayName)
   {
      this.displayName = displayName;
   }

   /**
    * @return the assignedGesture
    */
   public Gesture getAssignedGesture()
   {
      return assignedGesture;
   }

   /**
    * @param assignedGesture the assignedGesture to set
    */
   public void setAssignedGesture(Gesture assignedGesture)
   {
      this.assignedGesture = assignedGesture;
   }
}
