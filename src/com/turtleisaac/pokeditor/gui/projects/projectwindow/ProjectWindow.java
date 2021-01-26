/*
 * Created by JFormDesigner on Wed Dec 30 13:51:19 EST 2020
 */

package com.turtleisaac.pokeditor.gui.projects.projectwindow;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

import com.jackhack96.jNdstool.main.JNdstool;
import com.jidesoft.swing.ComboBoxSearchable;
import com.turtleisaac.pokeditor.editors.narctowl.Narctowl;
import com.turtleisaac.pokeditor.editors.personal.gen4.PersonalEditor;
import com.turtleisaac.pokeditor.editors.personal.gen4.PersonalReturnGen4;
import com.turtleisaac.pokeditor.gui.MyFilter;
import com.turtleisaac.pokeditor.gui.main.PokEditor;
import com.turtleisaac.pokeditor.gui.projects.projectwindow.console.ConsoleWindow;
import com.turtleisaac.pokeditor.gui.projects.projectwindow.editors.trainers.TrainerPanel;
import com.turtleisaac.pokeditor.gui.projects.projectwindow.sheets.RomApplier;
import com.turtleisaac.pokeditor.gui.projects.projectwindow.sheets.SheetApplier;
import com.turtleisaac.pokeditor.gui.projects.projectwindow.sheets.SheetsSetup;
import com.turtleisaac.pokeditor.project.Game;
import com.turtleisaac.pokeditor.project.Project;
import net.miginfocom.swing.*;
import turtleisaac.GoogleSheetsAPI;

/**
 * @author turtleisaac
 */
public class ProjectWindow extends JFrame
{

    private Project project;
    private GoogleSheetsAPI api;
    private String projectPath;
    private Game baseRom;

    private String sheetName;
    private String sheetType;
    private List<List<Object>> sheetData;

    private ConsoleWindow console;

    public ProjectWindow(String xmlPath) throws IOException
    {
        initComponents();

        console= new ConsoleWindow();

        project= Project.readFromXml(xmlPath);
        projectPath= project.getProjectPath().toString();
        baseRom= project.getBaseRom();
        setTitle(project.getName() + " (PokEditor)");
        menuBar.setVisible(true);
        openProjectButton.setIcon(new ImageIcon(ProjectWindow.class.getResource("/icons/open_file.png")));
        exportRomButton.setIcon(new ImageIcon(ProjectWindow.class.getResource("/icons/save_rom.png")));

        if(new File(project.getProjectPath().toString() + "/api.ser").exists())
        {
            try
            {
                ObjectInputStream in= new ObjectInputStream(new FileInputStream(projectPath + "/api.ser"));
                String link= (String) in.readObject();
                api= new GoogleSheetsAPI(link, projectPath);
                linkTextField.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                linkTextField.setEnabled(true);
                linkTextField.setText(link);
                linkTextField.setForeground(new Color(27, 148, 255, 255));
                sheetChooserComboBox.setEnabled(true);
                applyToRomButton.setEnabled(true);
                applyToSheetButton.setEnabled(true);
                sheetPreviewTable.setEnabled(true);
                sheetRefreshChangesButton.setEnabled(true);
                sheetUploadChangesButton.setEnabled(true);
                setSheetChooserComboBox(api.getSheetNames());
            } catch (ClassNotFoundException | IOException | GeneralSecurityException e)
            {
                JOptionPane.showMessageDialog(this,"Unable to load stored API credentials. Please redo configuration.","Error",JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        switch(baseRom)
        {
            case Diamond:
                baseRomButton.setIcon(new ImageIcon(ProjectWindow.class.getResource("/icons/diamond.png")));
                break;

            case Pearl:
                baseRomButton.setIcon(new ImageIcon(ProjectWindow.class.getResource("/icons/pearl.png")));
                break;

            case Platinum:
                baseRomButton.setIcon(new ImageIcon(ProjectWindow.class.getResource("/icons/platinum.png")));
                break;

            case HeartGold:
                baseRomButton.setIcon(new ImageIcon(ProjectWindow.class.getResource("/icons/heartgold.png")));
                break;

            case SoulSilver:
                baseRomButton.setIcon(new ImageIcon(ProjectWindow.class.getResource("/icons/soulsilver.png")));
                break;

            case Black:
                baseRomButton.setIcon(new ImageIcon(ProjectWindow.class.getResource("/icons/black.png")));
                break;

            case White:
                baseRomButton.setIcon(new ImageIcon(ProjectWindow.class.getResource("/icons/white.png")));
                break;

            case Black2:
                baseRomButton.setIcon(new ImageIcon(ProjectWindow.class.getResource("/icons/black2.png")));
                break;

            case White2:
                baseRomButton.setIcon(new ImageIcon(ProjectWindow.class.getResource("/icons/white2.png")));
                break;
        }

        setPreferredSize(new Dimension(950, 1000));

        pack();
        toFront();
        setVisible(true);

        File resourceDir= new File(projectPath + File.separator + "Program Files");
        if(!resourceDir.exists())
        {
            InputStream in= ProjectWindow.class.getResourceAsStream("/Program Files.zip");
            byte[] buffer= new byte[1024];
            ZipInputStream zipIn= new ZipInputStream(in);
            ZipEntry zipEntry= zipIn.getNextEntry();

            while(zipEntry != null)
            {
                if(!zipEntry.getName().contains("__"))
                {
                    File outputFile= newFile(resourceDir.getParentFile(),zipEntry);
                    if(zipEntry.isDirectory())
                    {
                        if(!outputFile.isDirectory() && !outputFile.mkdirs())
                        {
                            throw new IOException("Failed to create directory " + outputFile);
                        }
                    }
                    else
                    {
                        // fix for Windows-created archives
                        File parent = outputFile.getParentFile();
                        if(!parent.isDirectory() && !parent.mkdirs())
                        {
                            throw new IOException("Failed to create directory " + parent);
                        }

                        // write file content
                        if(!outputFile.getName().contains("._"))
                        {
                            System.out.println("Entry: " + zipEntry.getName());
                            System.out.println("    " + outputFile.getName());
                            FileOutputStream outputStream= new FileOutputStream(outputFile);
                            int len;
                            while ((len = zipIn.read(buffer)) > 0)
                            {
                                outputStream.write(buffer, 0, len);
                            }
                            outputStream.close();
                        }
                    }
                }
                zipEntry= zipIn.getNextEntry();
            }
            zipIn.closeEntry();
            zipIn.close();
        }

        ComboBoxSearchable sheetComboBoxSearchable= new ComboBoxSearchable(sheetChooserComboBox);
        trainerPanel1.setProjectPath(projectPath);
        trainerPanel1.setApi(api);
    }

    private void sheetsSetupButtonActionPerformed(ActionEvent e)
    {
        switch(JOptionPane.showConfirmDialog(this,"PokEditor is an open source tool developed by Turtleisaac. By continuing, I authorize PokEditor to gain full viewing and editing access to any Google Sheet document that I provide it the link for. Continue?","Confirmation",JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE))
        {
            case 1: //no
                JOptionPane.showMessageDialog(this,"You can manually download your modified sheets as csv files or use Excel to continue without Google Sheets Integration. If security is such a big concern to you, it is recommended that you make a new Google account just for this or other throwaway purposes. Thank you.","PokEditor",JOptionPane.INFORMATION_MESSAGE);
                break;

            case 0: //yes
                SheetsSetup setup= new SheetsSetup(project.getBaseRom(), this);
                try
                {
                    Thread.sleep(1);
                }
                catch(InterruptedException exception)
                {
                    exception.printStackTrace();
                }

                setEnabled(false);
                setup.toFront();

                break;
        }



    }

    private void thisWindowClosing(WindowEvent e)
    {
        switch(JOptionPane.showConfirmDialog(this,"Any local changes that have not been uploaded or applied to your ROM will be lost. Are you sure you want to exit?","Warning",JOptionPane.YES_NO_OPTION))
        {
            case 0: //yes
                dispose();

                Point location= getLocation();
                String xLocation= "" + location.getX();
                String yLocation= "" + location.getY();
                PokEditor.main(new String[] {xLocation, yLocation});
                break;

            case 1: //no
                setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                break;
        }
    }

    private void applyToSheetButtonActionPerformed(ActionEvent e) {
        // TODO add your code here
        if(JOptionPane.showConfirmDialog(this,"Any changes that you have made to the sheet that do not exist in the ROM will be lost. Continue?","Alert",JOptionPane.YES_NO_OPTION) == 0)
        {
            RomApplier editApplier= new RomApplier(project, projectPath, api, this);
            editApplier.setLocationRelativeTo(this);
            setEnabled(false);
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException | IllegalMonitorStateException exception)
            {
                exception.printStackTrace();
            }

            editApplier.toFront();
        }

//        JOptionPane.showMessageDialog(this,"Not implemented yet","Alert",JOptionPane.ERROR_MESSAGE);
    }

    private void applyToRomButtonActionPerformed(ActionEvent e)
    {
        // TODO add your code here

        List<List<Object>> onlineSheetValues;

        try
        {
            onlineSheetValues= api.getSpecifiedSheet(sheetName);
        }
        catch(IOException | NullPointerException exception)
        {
            onlineSheetValues= null;
        }

        if(!getTableData().equals(onlineSheetValues))
        {
            if(JOptionPane.showOptionDialog(this, "The data contained in the online version of this sheet differs from your local version. Which is the most recent?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[]{"Local", "Online"}, "Local") == 1)
            {
                System.out.println("Most recent data changed to online");
                sheetChooserComboBoxActionPerformed(e);
            }
            else
            {
                try
                {
                    api.updateSheet((String) sheetChooserComboBox.getSelectedItem(),getTableData());
                }
                catch (IOException exception)
                {
                    JOptionPane.showMessageDialog(this,"Upload failed","Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        SheetApplier editApplier= new SheetApplier(project, projectPath, api, this);
        editApplier.setLocationRelativeTo(this);
        setEnabled(false);
        editApplier.toFront();

//        if(sheetType == null || sheetType.equals(""))
//        {
//            switch(sheetName)
//            {
//                case "Personal":
//                    api.setPokeditorSheetType(sheetName,baseRom.sheetList[0]);
//                        break;
//
//                    case "TM Learnsets":
//                        api.setPokeditorSheetType(sheetName,baseRom.sheetList[0]);
//                        break;
//
//                    case "Level-Up Learnsets":
//                        break;
//
//                    case "Evolutions":
//                        break;
//
//                default:
//                    break;
//            }
//
//            switch(JOptionPane.showOptionDialog(this,"Not a PokEditor-compatible sheet. If this is a PokEditor sheet and it is not being recognized as such, please press \"Continue\".","Error",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE,null,new String[] {"Cancel","Continue"},"Cancel"))
//            {
//                case 0:
//                    JOptionPane.showMessageDialog(this,"Not implemented yet","Alert",JOptionPane.ERROR_MESSAGE);
//                    break;
//
//                case 1:
//                    break;
//            }
//        }

//        switch(baseRom)
//        {
//            case Diamond:
//                switch (sheetName)
//                {
//                    case "Personal":
//                        break;
//
//                    case "TM Learnsets":
//                        break;
//
//                    case "Level-Up Learnsets":
//                        break;
//
//                    case "Evolutions":
//                        break;
//                }
//                break;
//
//            case Pearl:
//
//                break;
//
//            case Platinum:
//
//                break;
//
//            case HeartGold:
//
//                break;
//
//            case SoulSilver:
//
//                break;
//
//            case Black:
//
//                break;
//
//            case White:
//
//                break;
//
//            case Black2:
//
//                break;
//
//            case White2:
//
//                break;
//        }

//        JOptionPane.showMessageDialog(this,"Not implemented yet","Alert",JOptionPane.ERROR_MESSAGE);
    }

    private void setSheetChooserComboBox(String... arr)
    {
        for(String str : arr)
        {
            sheetChooserComboBox.addItem(str);
        }
    }

    public void sheetChooserComboBoxActionPerformed(ActionEvent e)
    {
        sheetName= (String) sheetChooserComboBox.getSelectedItem();
        List<List<Object>> values;

        try
        {
            values= api.getSpecifiedSheet(sheetName);
        }
        catch(IOException exception)
        {
            JOptionPane.showMessageDialog(this,"Download failed","Error",JOptionPane.ERROR_MESSAGE);
            values= null;
        }

        try
        {
            sheetType= api.getPokeditorSheetType(sheetChooserComboBox.getSelectedIndex());
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
        catch (NullPointerException exception)
        {
            sheetType= null;
        }

        sheetData= values;


        Object[] header;

        if(sheetType != null || contains(baseRom.sheetList,sheetName))
        {
            header= values.remove(0).toArray(new Object[0]);
        }
        else
        {
            header= new Object[values.get(0).size()];
        }


        Object[][] table= new Object[values.size()][];


        for(int i= 0; i < values.size(); i++)
        {
            table[i]= values.get(i).toArray(new Object[0]);
        }
        int numColumns= table[1].length;

        DefaultTableModel model= new DefaultTableModel(table,header);
        sheetPreviewTable.setModel(model);
        sheetPreviewTable.setGridColor(Color.black);
        sheetPreviewTable.setShowVerticalLines(true);
        sheetPreviewTable.setShowHorizontalLines(true);
        sheetPreviewTable.getTableHeader().setReorderingAllowed(false);

        if(numColumns >= 9)
            sheetPreviewTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        else
            sheetPreviewTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    private void sheetRefreshChangesButtonActionPerformed(ActionEvent e)
    {
        switch(JOptionPane.showConfirmDialog(this,"Any changes you have made to this sheet locally will be lost when pulling the current online version. Continue?","Warning",JOptionPane.YES_NO_OPTION))
        {
            case 0: //yes
                sheetChooserComboBoxActionPerformed(e);
                break;

            case 1: //no
                break;
        }
    }

    private void sheetUploadChangesButtonActionPerformed(ActionEvent e)
    {
        switch(JOptionPane.showConfirmDialog(this,"Any changes you have made to this sheet online will be lost when uploading the current local version. Continue?","Warning",JOptionPane.YES_NO_OPTION))
        {
            case 0: //yes
                try
                {
                    api.updateSheet((String) sheetChooserComboBox.getSelectedItem(),getTableData());
                }
                catch (IOException exception)
                {
                    JOptionPane.showMessageDialog(this,"Upload failed","Error",JOptionPane.ERROR_MESSAGE);
                }

                break;

            case 1: //no
                break;
        }
    }

    private void consoleItemActionPerformed(ActionEvent e)
    {
        console.setLocationRelativeTo(this);
        console.setVisible(true);
    }

    private void narctowlItemActionPerformed(ActionEvent e)
    {
        PokEditor pokeditor= new PokEditor();
        pokeditor.setVisible(false);

        JFrame frame= new JFrame();
        frame.setPreferredSize(new Dimension(500,250));
        frame.setTitle("Narctowl");
        frame.setLocationRelativeTo(this);
        frame.setContentPane(pokeditor.getNarctowlPanel());
        frame.setVisible(true);
        frame.pack();
    }

    private void blzCoderItemActionPerformed(ActionEvent e)
    {
        PokEditor pokeditor= new PokEditor();
        pokeditor.setVisible(false);

        JFrame frame= new JFrame();
        frame.setPreferredSize(new Dimension(500,150));
        frame.setTitle("BLZ Coder");
        frame.setLocationRelativeTo(this);
        frame.setContentPane(pokeditor.getCompressionPanel());
        frame.setVisible(true);
        frame.pack();
    }

    private void linkTextFieldMouseClicked(MouseEvent e)
    {
        try
        {
            Desktop.getDesktop().browse(new URI(linkTextField.getText()));
        } catch (URISyntaxException | IOException exception)
        {
            exception.printStackTrace();
        }
    }

    private void exportRomButtonActionPerformed(ActionEvent e)
    {
        JOptionPane.showMessageDialog(this,"Make sure that you have applied the data that you want applied to your ROM with the \"Apply to ROM\" button.","Warning",JOptionPane.WARNING_MESSAGE);

        JFileChooser fc= new JFileChooser(System.getProperty("user.dir"));
        fc.addChoosableFileFilter(new MyFilter("Nintendo DS ROM",".nds"));
        fc.setAcceptAllFileFilterUsed(false);

        if (e.getSource() == exportRomButton) {
            fc.setDialogTitle("Export ROM");
            int returnVal = fc.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                JNdstool.main("-b",projectPath + File.separator + project.getName(),fc.getSelectedFile().toString());
            }
        }
    }

    private void openProjectButtonActionPerformed(ActionEvent e)
    {
        JFileChooser fc= new JFileChooser(System.getProperty("user.dir"));
        fc.setDialogTitle("Open Project");
        fc.setAcceptAllFileFilterUsed(false);

        fc.addChoosableFileFilter(new MyFilter("PokEditor Projects",".pokeditor"));
        fc.addChoosableFileFilter(new MyFilter("DS Pokemon Rom Editor Projects",".dspre"));

        if (e.getSource() == openProjectButton) {
            int returnVal = fc.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try
                {
                    ProjectWindow projectWindow= new ProjectWindow(fc.getSelectedFile().getAbsolutePath());
                    projectWindow.setLocationRelativeTo(this);
                }
                catch (IOException ignored)
                {
                }

            }
        }
    }

    private void baseRomButtonActionPerformed(ActionEvent e)
    {
        // TODO add your code here
        JOptionPane.showMessageDialog(this,"Not implemented yet.","Error",JOptionPane.ERROR_MESSAGE);
    }

    private void projectInfoButtonActionPerformed(ActionEvent e)
    {
        // TODO add your code here
        JOptionPane.showMessageDialog(this,"Not implemented yet.","Error",JOptionPane.ERROR_MESSAGE);
    }

    private void exportRomItemActionPerformed(ActionEvent e) {
        exportRomButtonActionPerformed(e);
    }

    private void openProjectItemActionPerformed(ActionEvent e) {
        openProjectButtonActionPerformed(e);
    }

    private void aboutItemActionPerformed(ActionEvent e)
    {
        // TODO add your code here
        JOptionPane.showMessageDialog(this,"Not implemented yet.","Error",JOptionPane.ERROR_MESSAGE);
    }

    private void randomizerItemActionPerformed(ActionEvent e)
    {
        // TODO add your code here
        JOptionPane.showMessageDialog(this,"Not implemented yet.","Error",JOptionPane.ERROR_MESSAGE);
    }

    private void trainerSelectionComboBoxActionPerformed(ActionEvent e)
    {
        // TODO add your code here
    }

    private void newTrainerButtonActionPerformed(ActionEvent e)
    {
        // TODO add your code here
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        menuBar = new JMenuBar();
        fileMenu = new JMenu();
        openProjectItem = new JMenuItem();
        openRecentItem = new JMenuItem();
        exportRomItem = new JMenuItem();
        toolsMenu = new JMenu();
        randomizerItem = new JMenuItem();
        narctowlItem = new JMenuItem();
        blzCoderItem = new JMenuItem();
        debugMenu = new JMenu();
        consoleItem = new JMenuItem();
        helpMenu = new JMenu();
        aboutItem = new JMenuItem();
        separator2 = new JSeparator();
        tabbedPane1 = new JTabbedPane();
        mainPanel = new JPanel();
        sheetsSetupButton = new JButton();
        linkTextField = new JLabel();
        sheetChooserComboBox = new JComboBox();
        applyToSheetButton = new JButton();
        applyToRomButton = new JButton();
        sheetPreviewScrollPane = new JScrollPane();
        sheetPreviewTable = new JTable();
        sheetRefreshChangesButton = new JButton();
        sheetUploadChangesButton = new JButton();
        warningLabel = new JLabel();
        trainerPanel1 = new TrainerPanel();
        starterPanel = new JPanel();
        label1 = new JLabel();
        introPanel = new JPanel();
        label2 = new JLabel();
        openingPanel = new JPanel();
        label3 = new JLabel();
        spritePanel = new JPanel();
        label4 = new JLabel();
        jtbMain = new JToolBar();
        openProjectButton = new JButton();
        exportRomButton = new JButton();
        toolBarHorizontalSeparator = new JPanel(null);
        baseRomButton = new JButton();
        projectInfoButton = new JButton();

        //======== this ========
        setTitle("PokEditor");
        setMinimumSize(new Dimension(600, 500));
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                thisWindowClosing(e);
            }
        });
        Container contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[853,grow,fill]",
            // rows
            "[]" +
            "[63,grow,top]"));

        //======== menuBar ========
        {

            //======== fileMenu ========
            {
                fileMenu.setText("File");

                //---- openProjectItem ----
                openProjectItem.setText("Open Project");
                openProjectItem.addActionListener(e -> openProjectItemActionPerformed(e));
                fileMenu.add(openProjectItem);

                //---- openRecentItem ----
                openRecentItem.setText("Open Recent");
                fileMenu.add(openRecentItem);
                fileMenu.addSeparator();

                //---- exportRomItem ----
                exportRomItem.setText("Export ROM");
                exportRomItem.addActionListener(e -> exportRomItemActionPerformed(e));
                fileMenu.add(exportRomItem);
            }
            menuBar.add(fileMenu);

            //======== toolsMenu ========
            {
                toolsMenu.setText("Tools");

                //---- randomizerItem ----
                randomizerItem.setText("Randomizer");
                randomizerItem.addActionListener(e -> randomizerItemActionPerformed(e));
                toolsMenu.add(randomizerItem);

                //---- narctowlItem ----
                narctowlItem.setText("Narctowl");
                narctowlItem.addActionListener(e -> narctowlItemActionPerformed(e));
                toolsMenu.add(narctowlItem);

                //---- blzCoderItem ----
                blzCoderItem.setText("BLZ Coder");
                blzCoderItem.addActionListener(e -> blzCoderItemActionPerformed(e));
                toolsMenu.add(blzCoderItem);
            }
            menuBar.add(toolsMenu);

            //======== debugMenu ========
            {
                debugMenu.setText("Debug");

                //---- consoleItem ----
                consoleItem.setText("Console");
                consoleItem.addActionListener(e -> consoleItemActionPerformed(e));
                debugMenu.add(consoleItem);
            }
            menuBar.add(debugMenu);

            //======== helpMenu ========
            {
                helpMenu.setText("Help");

                //---- aboutItem ----
                aboutItem.setText("About");
                aboutItem.addActionListener(e -> aboutItemActionPerformed(e));
                helpMenu.add(aboutItem);
            }
            menuBar.add(helpMenu);
        }
        setJMenuBar(menuBar);

        //---- separator2 ----
        separator2.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        contentPane.add(separator2, "cell 0 0,grow");

        //======== tabbedPane1 ========
        {

            //======== mainPanel ========
            {
                mainPanel.setLayout(new MigLayout(
                    "hidemode 3",
                    // columns
                    "[grow,fill]" +
                    "[grow,fill]",
                    // rows
                    "[54]" +
                    "[]" +
                    "[]" +
                    "[]" +
                    "[grow,fill]" +
                    "[]" +
                    "[bottom]"));

                //---- sheetsSetupButton ----
                sheetsSetupButton.setText("Google Sheets Integration Setup");
                sheetsSetupButton.setToolTipText("This is how you configure a link between Google Sheets and PokEditor.");
                sheetsSetupButton.addActionListener(e -> sheetsSetupButtonActionPerformed(e));
                mainPanel.add(sheetsSetupButton, "cell 0 0 2 1,grow");

                //---- linkTextField ----
                linkTextField.setEnabled(false);
                linkTextField.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        linkTextFieldMouseClicked(e);
                    }
                });
                mainPanel.add(linkTextField, "cell 0 1 2 1,alignx center,growx 0");

                //---- sheetChooserComboBox ----
                sheetChooserComboBox.setEnabled(false);
                sheetChooserComboBox.addActionListener(e -> sheetChooserComboBoxActionPerformed(e));
                mainPanel.add(sheetChooserComboBox, "cell 0 2 2 1");

                //---- applyToSheetButton ----
                applyToSheetButton.setText("Apply ROM to Sheets");
                applyToSheetButton.setEnabled(false);
                applyToSheetButton.setToolTipText("Data from ROM is copied into Google Sheets.");
                applyToSheetButton.addActionListener(e -> applyToSheetButtonActionPerformed(e));
                mainPanel.add(applyToSheetButton, "cell 0 3");

                //---- applyToRomButton ----
                applyToRomButton.setText("Apply Sheets to ROM");
                applyToRomButton.setEnabled(false);
                applyToRomButton.setToolTipText("Data from Google Sheets is applied to ROM.");
                applyToRomButton.addActionListener(e -> applyToRomButtonActionPerformed(e));
                mainPanel.add(applyToRomButton, "cell 1 3");

                //======== sheetPreviewScrollPane ========
                {
                    sheetPreviewScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
                    sheetPreviewScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

                    //---- sheetPreviewTable ----
                    sheetPreviewTable.setEnabled(false);
                    sheetPreviewTable.setGridColor(Color.black);
                    sheetPreviewTable.setMaximumSize(new Dimension(2147483647, 2147483647));
                    sheetPreviewScrollPane.setViewportView(sheetPreviewTable);
                }
                mainPanel.add(sheetPreviewScrollPane, "cell 0 4 2 1,growx");

                //---- sheetRefreshChangesButton ----
                sheetRefreshChangesButton.setText("Refresh Sheet from Google Sheets");
                sheetRefreshChangesButton.setEnabled(false);
                sheetRefreshChangesButton.setToolTipText("Online changes made since last download are reloaded.");
                sheetRefreshChangesButton.addActionListener(e -> sheetRefreshChangesButtonActionPerformed(e));
                mainPanel.add(sheetRefreshChangesButton, "cell 0 5");

                //---- sheetUploadChangesButton ----
                sheetUploadChangesButton.setText("Upload Changes to Google Sheets");
                sheetUploadChangesButton.setEnabled(false);
                sheetUploadChangesButton.setToolTipText("Applies local changes to Google Sheets.");
                sheetUploadChangesButton.addActionListener(e -> sheetUploadChangesButtonActionPerformed(e));
                mainPanel.add(sheetUploadChangesButton, "cell 1 5");

                //---- warningLabel ----
                warningLabel.setText("No warnings to display");
                mainPanel.add(warningLabel, "cell 0 6 2 1,alignx center,growx 0");
            }
            tabbedPane1.addTab("Main", mainPanel);
            tabbedPane1.addTab("Trainer Editor", trainerPanel1);

            //======== starterPanel ========
            {
                starterPanel.setLayout(new MigLayout(
                    "hidemode 3",
                    // columns
                    "[fill]" +
                    "[fill]",
                    // rows
                    "[]" +
                    "[]" +
                    "[]"));

                //---- label1 ----
                label1.setText("You were expecting a starter editor, but it was me, Dio!");
                starterPanel.add(label1, "cell 0 0");
            }
            tabbedPane1.addTab("Starters", starterPanel);

            //======== introPanel ========
            {
                introPanel.setLayout(new MigLayout(
                    "hidemode 3",
                    // columns
                    "[fill]" +
                    "[fill]",
                    // rows
                    "[]" +
                    "[]" +
                    "[]"));

                //---- label2 ----
                label2.setText("You were expecting an intro cutscene editor, but it was me, Dio!");
                introPanel.add(label2, "cell 0 0");
            }
            tabbedPane1.addTab("Intro Cutscene", introPanel);

            //======== openingPanel ========
            {
                openingPanel.setLayout(new MigLayout(
                    "hidemode 3",
                    // columns
                    "[fill]" +
                    "[fill]",
                    // rows
                    "[]" +
                    "[]" +
                    "[]"));

                //---- label3 ----
                label3.setText("You were expecting an opening cutscene editor, but it was me, Dio!");
                openingPanel.add(label3, "cell 0 0");
            }
            tabbedPane1.addTab("Opening Cutscene", openingPanel);

            //======== spritePanel ========
            {
                spritePanel.setLayout(new MigLayout(
                    "hidemode 3",
                    // columns
                    "[fill]" +
                    "[fill]",
                    // rows
                    "[]" +
                    "[]" +
                    "[]"));

                //---- label4 ----
                label4.setText("You were expecting a sprite editor, but it was me, Dio!");
                spritePanel.add(label4, "cell 0 0");
            }
            tabbedPane1.addTab("Sprites", spritePanel);
        }
        contentPane.add(tabbedPane1, "cell 0 1");

        //======== jtbMain ========
        {
            jtbMain.setBorderPainted(false);
            jtbMain.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            jtbMain.setFloatable(false);

            //---- openProjectButton ----
            openProjectButton.setMaximumSize(new Dimension(40, 40));
            openProjectButton.setMinimumSize(new Dimension(40, 40));
            openProjectButton.setPreferredSize(new Dimension(40, 40));
            openProjectButton.setIcon(UIManager.getIcon("Tree.openIcon"));
            openProjectButton.setToolTipText("Open Project");
            openProjectButton.addActionListener(e -> openProjectButtonActionPerformed(e));
            jtbMain.add(openProjectButton);
            jtbMain.addSeparator();

            //---- exportRomButton ----
            exportRomButton.setPreferredSize(new Dimension(40, 40));
            exportRomButton.setMaximumSize(new Dimension(40, 40));
            exportRomButton.setMinimumSize(new Dimension(40, 40));
            exportRomButton.setIcon(UIManager.getIcon("FileView.hardDriveIcon"));
            exportRomButton.setToolTipText("Export ROM");
            exportRomButton.addActionListener(e -> exportRomButtonActionPerformed(e));
            jtbMain.add(exportRomButton);
            jtbMain.addSeparator();
            jtbMain.add(toolBarHorizontalSeparator);
            jtbMain.addSeparator();

            //---- baseRomButton ----
            baseRomButton.setMaximumSize(new Dimension(110, 40));
            baseRomButton.setMinimumSize(new Dimension(110, 40));
            baseRomButton.setPreferredSize(new Dimension(110, 40));
            baseRomButton.setIcon(UIManager.getIcon("FileView.fileIcon"));
            baseRomButton.setText("ROM Info");
            baseRomButton.setToolTipText("ROM Info");
            baseRomButton.addActionListener(e -> baseRomButtonActionPerformed(e));
            jtbMain.add(baseRomButton);
            jtbMain.addSeparator();

            //---- projectInfoButton ----
            projectInfoButton.setText("Project Info");
            projectInfoButton.setMaximumSize(new Dimension(110, 40));
            projectInfoButton.setMinimumSize(new Dimension(110, 40));
            projectInfoButton.setPreferredSize(new Dimension(110, 40));
            projectInfoButton.setToolTipText("Project Info");
            projectInfoButton.addActionListener(e -> projectInfoButtonActionPerformed(e));
            jtbMain.add(projectInfoButton);
        }
        contentPane.add(jtbMain, "north");
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem openProjectItem;
    private JMenuItem openRecentItem;
    private JMenuItem exportRomItem;
    private JMenu toolsMenu;
    private JMenuItem randomizerItem;
    private JMenuItem narctowlItem;
    private JMenuItem blzCoderItem;
    private JMenu debugMenu;
    private JMenuItem consoleItem;
    private JMenu helpMenu;
    private JMenuItem aboutItem;
    private JSeparator separator2;
    private JTabbedPane tabbedPane1;
    private JPanel mainPanel;
    private JButton sheetsSetupButton;
    private JLabel linkTextField;
    private JComboBox sheetChooserComboBox;
    private JButton applyToSheetButton;
    private JButton applyToRomButton;
    private JScrollPane sheetPreviewScrollPane;
    private JTable sheetPreviewTable;
    private JButton sheetRefreshChangesButton;
    private JButton sheetUploadChangesButton;
    private JLabel warningLabel;
    private TrainerPanel trainerPanel1;
    private JPanel starterPanel;
    private JLabel label1;
    private JPanel introPanel;
    private JLabel label2;
    private JPanel openingPanel;
    private JLabel label3;
    private JPanel spritePanel;
    private JLabel label4;
    private JToolBar jtbMain;
    private JButton openProjectButton;
    private JButton exportRomButton;
    private JPanel toolBarHorizontalSeparator;
    private JButton baseRomButton;
    private JButton projectInfoButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables



    private boolean isDP(String gameCode)
    {
        return isPearl(gameCode) || isDiamond(gameCode);
    }

    private boolean isDiamond(String gameCode)
    {
        return gameCode.startsWith("ADA");
    }

    private boolean isPearl(String gameCode)
    {
        return gameCode.startsWith("APA");
    }

    private boolean isPlatinum(String gameCode)
    {
        return gameCode.startsWith("CPU");
    }



    private boolean isHGSS(String gameCode)
    {
        return isHeartGold(gameCode) || isSoulSilver(gameCode);
    }

    private boolean isHeartGold(String gameCode)
    {
        return gameCode.startsWith("IPK");
    }

    private boolean isSoulSilver(String gameCode)
    {
        return gameCode.startsWith("IPG");
    }



    public Project getProject()
    {
        return project;
    }

    public void setApi(GoogleSheetsAPI api)
    {
        this.api= api;
        sheetChooserComboBox.setEnabled(true);
        linkTextField.setEnabled(true);
        linkTextField.setText(api.getSPREADSHEET_LINK());
        linkTextField.setForeground(new Color(27, 148, 255, 255));
        applyToRomButton.setEnabled(true);
        applyToSheetButton.setEnabled(true);
        sheetPreviewTable.setEnabled(true);
        sheetRefreshChangesButton.setEnabled(true);
        sheetUploadChangesButton.setEnabled(true);
        linkTextField.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        toFront();
        setEnabled(true);

        try
        {
            ObjectOutputStream out= new ObjectOutputStream(new FileOutputStream(projectPath + "/api.ser"));
            out.writeObject(api.getSPREADSHEET_LINK());
            out.close();
        }
        catch (IOException e)
        {
            System.err.println("Attempt failed: " + projectPath + "/api.ser");
            JOptionPane.showMessageDialog(this,"Unable to store API and credentials","Error",JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            new File(projectPath + "/api.ser").delete();
        }

//        try
//        {
//            api.pokeditorSheetMetadataUpdater(baseRom.sheetList);
//        }
//        catch (IOException ignored)
//        {
//        }


        if (JOptionPane.showConfirmDialog(this, "Would you like to update the sheets using data from your ROM? (This will overwrite existing data in the sheets, and you can choose which data you want to apply specifically)", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == 0)
        {
            RomApplier editApplier = new RomApplier(project, projectPath, api, this);
            editApplier.setLocationRelativeTo(this);
        }


        String[] sheetNames= new String[0];

        try
        {
            sheetNames= api.getSheetNames();
            setSheetChooserComboBox(sheetNames);
        }
        catch (IOException ignored)
        {
        }


        sheetChooserComboBox.setSelectedIndex(indexOf("Personal",sheetNames));

    }

    private void initialLinkDataUpdate() throws IOException
    {
        System.out.println("Performing initial sheet updates");
        Narctowl narctowl= new Narctowl(true);
        String dataPath= projectPath + File.separator + project.getName() + "/data";

        switch(baseRom)
        {
            case Diamond:
            case Pearl:


                break;

            case Platinum:

                //Personal and TM Learnsets
                PersonalEditor editor= new PersonalEditor(dataPath, baseRom);
                narctowl.unpack(dataPath + "/poketool/personal/pl_personal.narc",dataPath + "/poketool/personal/pl_personal");
                new File(dataPath + "/poketool/personal/pl_personal").deleteOnExit();
                PersonalReturnGen4 personalReturn= editor.personalToSheet("/poketool/personal/pl_personal");
                api.updateSheet("Personal",personalReturn.getPersonalData());
                api.updateSheet("TM Learnsets",personalReturn.getTMData());

                //Level-Up Learnsets
                break;

            case HeartGold:

                break;

            case SoulSilver:

                break;

            case Black:

                break;

            case White:

                break;

            case Black2:

                break;

            case White2:

                break;
        }
    }

    private List<List<Object>> getTableData()
    {
        List<List<Object>> ret= new ArrayList<>();

        List<Object> header= new ArrayList<>();
        for(int col= 0; col < sheetPreviewTable.getColumnCount(); col++)
        {
            header.add(sheetPreviewTable.getColumnName(col));
        }
        ret.add(header);

        for(int row= 0; row < sheetPreviewTable.getRowCount(); row++)
        {
            List<Object> sub= new ArrayList<>();
            for(int col= 0; col < sheetPreviewTable.getColumnCount(); col++)
            {
                sub.add(sheetPreviewTable.getValueAt(row,col));
            }
            ret.add(sub);
        }

        return ret;
    }

    private boolean contains(String[] arr, String str)
    {
//        System.out.println("Comparing " + str + " against " + Arrays.toString(arr));
        for(String s : arr)
        {
//            System.out.println("Comparing " + str + " to " + s);
            if(s.equals(str))
                return true;
        }
        return false;
    }

    private int indexOf(String str, String[] arr)
    {
        for(int i= 0; i < arr.length; i++)
        {
            if(arr[i].equals(str))
                return i;
        }
        return -1;
    }

    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException
    {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    public String getProjectPath()
    {
        return projectPath;
    }

    public GoogleSheetsAPI getApi()
    {
        return api;
    }
}
