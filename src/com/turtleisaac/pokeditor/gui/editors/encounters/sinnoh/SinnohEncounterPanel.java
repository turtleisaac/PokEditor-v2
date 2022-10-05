/*
 * Created by JFormDesigner on Mon Mar 01 11:16:57 EST 2021
 */

package com.turtleisaac.pokeditor.gui.editors.encounters.sinnoh;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import com.jidesoft.swing.ComboBoxSearchable;
import com.turtleisaac.pokeditor.editors.encounters.sinnoh.SinnohEncounterData;
import com.turtleisaac.pokeditor.editors.encounters.sinnoh.SinnohEncounterEditor;
import com.turtleisaac.pokeditor.editors.encounters.sinnoh.SinnohEncounterReturn;
import com.turtleisaac.pokeditor.editors.text.TextEditor;
import com.turtleisaac.pokeditor.framework.narctowl.Narctowl;
import com.turtleisaac.pokeditor.gui.ComboBoxItem;
import com.turtleisaac.pokeditor.gui.editors.encounters.SearchResultsFrame;
import com.turtleisaac.pokeditor.project.Project;
import com.turtleisaac.pokeditor.utilities.TableLocator;
import com.turtleisaac.pokeditor.utilities.TablePointers;
import com.turtleisaac.pokeditor.utilities.images.ImageBase;
import net.miginfocom.swing.*;
import turtleisaac.GoogleSheetsAPI;

/**
 * @author turtleisaac
 */
public class SinnohEncounterPanel extends JPanel
{
    private Project project;
    private GoogleSheetsAPI api;

    private static String unpackedFolderPath;

    public ArrayList<File> toDelete;

    private ArrayList<SinnohEncounterData> encounterData;


    private final JComboBox<ComboBoxItem>[] fieldSlots;
    private final JComboBox<ComboBoxItem>[] swarmSlots;
    private final JComboBox<ComboBoxItem>[] daySlots;
    private final JComboBox<ComboBoxItem>[] nightSlots;
    private final JComboBox<ComboBoxItem>[] radarSlots;
    private final JComboBox<ComboBoxItem>[] rubySlots;
    private final JComboBox<ComboBoxItem>[] sapphireSlots;
    private final JComboBox<ComboBoxItem>[] emeraldSlots;
    private final JComboBox<ComboBoxItem>[] fireRedSlots;
    private final JComboBox<ComboBoxItem>[] leafGreenSlots;

    private final JComboBox<ComboBoxItem>[] surfSlots;
    private final JComboBox<ComboBoxItem>[] oldRodSlots;
    private final JComboBox<ComboBoxItem>[] goodRodSlots;
    private final JComboBox<ComboBoxItem>[] superRodSlots;


    private final JSpinner[] fieldLevels;


    private final JSpinner[] surfMinLevels;
    private final JSpinner[] surfMaxLevels;

    private final JSpinner[] oldRodMinLevels;
    private final JSpinner[] oldRodMaxLevels;

    private final JSpinner[] goodRodMinLevels;
    private final JSpinner[] goodRodMaxLevels;

    private final JSpinner[] superRodMinLevels;
    private final JSpinner[] superRodMaxLevels;

    private static byte[][] probabilityTable;

    private static final String[] columnNames= new String[] {"20%", "20%", "10%", "10%", "10%", "10%", "5%", "5%", "4%", "4%", "1%", "1%"};
    private Object[][] tableData;

    int dualSlotSelectedGame;

    private static String[] nameData;

    private static byte[] paletteGuideTable;
//    private static

    public SinnohEncounterPanel()
    {
        initComponents();
        toDelete= new ArrayList<>();
        encounterSlotTable.setShowVerticalLines(true);

        fieldSlots= new JComboBox[] {field20ComboBox1,field20ComboBox2,field10ComboBox1,field10ComboBox2,field10ComboBox3,field10ComboBox4,field5ComboBox1,field5ComboBox2,field4ComboBox1,field4ComboBox2,field1ComboBox1,field1ComboBox2};

        swarmSlots= new JComboBox[] {swarm20ComboBox1,swarm20ComboBox2};
        daySlots= new JComboBox[] {day10ComboBox1,day10ComboBox2};
        nightSlots= new JComboBox[] {night10ComboBox1,night10ComboBox2};

        radarSlots= new JComboBox[] {radar10ComboBox1,radar10ComboBox2,radar10ComboBox3,radar10ComboBox4};

        rubySlots= new JComboBox[] {rubyComboBox1,rubyComboBox2};
        sapphireSlots= new JComboBox[] {sapphireComboBox1,sapphireComboBox2};
        emeraldSlots= new JComboBox[] {emeraldComboBox1,emeraldComboBox2};
        fireRedSlots= new JComboBox[] {fireRedComboBox1,fireRedComboBox2};
        leafGreenSlots= new JComboBox[] {leafGreenComboBox1,leafGreenComboBox2};

        surfSlots= new JComboBox[] {surf60ComboBox,surf30ComboBox,surf5ComboBox,surf4ComboBox,surf1ComboBox};
        oldRodSlots= new JComboBox[] {oldRod60ComboBox,oldRod30ComboBox,oldRod5ComboBox,oldRod4ComboBox,oldRod1ComboBox};
        goodRodSlots= new JComboBox[] {goodRod60ComboBox,goodRod30ComboBox,goodRod5ComboBox,goodRod4ComboBox,goodRod1ComboBox};
        superRodSlots= new JComboBox[] {superRod60ComboBox,superRod30ComboBox,superRod5ComboBox,superRod4ComboBox,superRod1ComboBox};


        fieldLevels= new JSpinner[] {field20LevelSpinner1,field20LevelSpinner2,field10LevelSpinner1,field10LevelSpinner2,field10LevelSpinner3,field10LevelSpinner4,field5LevelSpinner1,field5LevelSpinner2,field4LevelSpinner1,field4LevelSpinner2,field1LevelSpinner1,field1LevelSpinner2};

        surfMinLevels= new JSpinner[] {surf60MinLevelSpinner,surf30MinLevelSpinner,surf5MinLevelSpinner,surf4MinLevelSpinner,surf1MinLevelSpinner};
        oldRodMinLevels= new JSpinner[] {oldRod60MinLevelSpinner,oldRod30MinLevelSpinner,oldRod5MinLevelSpinner,oldRod4MinLevelSpinner,oldRod1MinLevelSpinner};
        goodRodMinLevels= new JSpinner[] {goodRod60MinLevelSpinner,goodRod30MinLevelSpinner,goodRod5MinLevelSpinner,goodRod4MinLevelSpinner,goodRod1MinLevelSpinner};
        superRodMinLevels= new JSpinner[] {superRod60MinLevelSpinner,superRod30MinLevelSpinner,superRod5MinLevelSpinner,superRod4MinLevelSpinner,superRod1MinLevelSpinner};

        surfMaxLevels= new JSpinner[] {surf60MaxLevelSpinner,surf30MaxLevelSpinner,surf5MaxLevelSpinner,surf4MaxLevelSpinner,surf1MaxLevelSpinner};
        oldRodMaxLevels= new JSpinner[] {oldRod60MaxLevelSpinner,oldRod30MaxLevelSpinner,oldRod5MaxLevelSpinner,oldRod4MaxLevelSpinner,oldRod1MaxLevelSpinner};
        goodRodMaxLevels= new JSpinner[] {goodRod60MaxLevelSpinner,goodRod30MaxLevelSpinner,goodRod5MaxLevelSpinner,goodRod4MaxLevelSpinner,goodRod1MaxLevelSpinner};
        superRodMaxLevels= new JSpinner[] {superRod60MaxLevelSpinner,superRod30MaxLevelSpinner,superRod5MaxLevelSpinner,superRod4MaxLevelSpinner,superRod1MaxLevelSpinner};

        tableData= new Object[1][12];
    }

    //region EncounterDisplayPanel
    private void morningRadioButtonActionPerformed(ActionEvent e)
    {
        if(morningRadioButton.isSelected())
        {
            dayRadioButton.setSelected(false);
            nightRadioButton.setSelected(false);
        }
        else if(!dayRadioButton.isSelected() && !nightRadioButton.isSelected())
        {
            morningRadioButton.setSelected(true);
        }

        displayEncounterPreviewAction(e);
    }

    private void dayRadioButtonActionPerformed(ActionEvent e)
    {
        if(dayRadioButton.isSelected())
        {
            morningRadioButton.setSelected(false);
            nightRadioButton.setSelected(false);
        }
        else if(!morningRadioButton.isSelected() && !nightRadioButton.isSelected())
        {
            dayRadioButton.setSelected(true);
        }

        displayEncounterPreviewAction(e);
    }

    private void nightRadioButtonActionPerformed(ActionEvent e)
    {
        if(nightRadioButton.isSelected())
        {
            morningRadioButton.setSelected(false);
            dayRadioButton.setSelected(false);
        }
        else if(!morningRadioButton.isSelected() && !dayRadioButton.isSelected())
        {
            nightRadioButton.setSelected(true);
        }

        displayEncounterPreviewAction(e);
    }

    private void dualSlotCheckboxActionPerformed(ActionEvent e)
    {
        if(dualSlotCheckbox.isSelected())
        {
            String[] gameList= new String[] {"LeafGreen","FireRed","Emerald","Sapphire","Ruby"};
            dualSlotSelectedGame = JOptionPane.showOptionDialog(this, "Which cartridge is in Slot-2?", "PokEditor", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, new ImageIcon(SinnohEncounterPanel.class.getResource("/icons/dualslot_encounters.png")), gameList, "");
        }
        displayEncounterPreviewAction(e);
    }

    private void swarmCheckboxActionPerformed(ActionEvent e) {
        displayEncounterPreviewAction(e);
    }

    private void radarCheckboxActionPerformed(ActionEvent e) {
        displayEncounterPreviewAction(e);
    }

    private void displayEncounterPreviewAction(ActionEvent e)
    {
        int[] selectedSpecies= new int[12];

        //region setIndexes
        if(swarmCheckbox.isSelected())
        {
            selectedSpecies[0]= swarmSlots[0].getSelectedIndex();
            selectedSpecies[1]= swarmSlots[1].getSelectedIndex();
        }
        else
        {
            selectedSpecies[0]= fieldSlots[0].getSelectedIndex();
            selectedSpecies[1]= fieldSlots[1].getSelectedIndex();
        }

        if(radarCheckbox.isSelected()) //Poke Radar Slots
        {
            selectedSpecies[2]= radarSlots[0].getSelectedIndex();
            selectedSpecies[3]= radarSlots[1].getSelectedIndex();
            selectedSpecies[4]= radarSlots[2].getSelectedIndex();
            selectedSpecies[5]= radarSlots[3].getSelectedIndex();
        }
        else if(dayRadioButton.isSelected()) //Day Slots (10 am - 8 pm)
        {
            selectedSpecies[2]= daySlots[0].getSelectedIndex();
            selectedSpecies[3]= daySlots[1].getSelectedIndex();
            selectedSpecies[4]= fieldSlots[4].getSelectedIndex();
            selectedSpecies[5]= fieldSlots[5].getSelectedIndex();
        }
        else if(nightRadioButton.isSelected()) //Night Slots (8 pm - 4 am)
        {
            selectedSpecies[2]= nightSlots[0].getSelectedIndex();
            selectedSpecies[3]= nightSlots[1].getSelectedIndex();
            selectedSpecies[4]= fieldSlots[4].getSelectedIndex();
            selectedSpecies[5]= fieldSlots[5].getSelectedIndex();
        }
        else //Morning Slots (4 am - 10 am)
        {
            selectedSpecies[2]= fieldSlots[2].getSelectedIndex();
            selectedSpecies[3]= fieldSlots[3].getSelectedIndex();
            selectedSpecies[4]= fieldSlots[4].getSelectedIndex();
            selectedSpecies[5]= fieldSlots[5].getSelectedIndex();
        }

        selectedSpecies[6]= fieldSlots[6].getSelectedIndex();
        selectedSpecies[7]= fieldSlots[7].getSelectedIndex();


        if(dualSlotCheckbox.isSelected())
        {
            switch (dualSlotSelectedGame)
            {
                case 0: //LeafGreen
                    selectedSpecies[8]= leafGreenSlots[0].getSelectedIndex();
                    selectedSpecies[9]= leafGreenSlots[1].getSelectedIndex();
                    break;

                case 1: //FireRed
                    selectedSpecies[8]= fireRedSlots[0].getSelectedIndex();
                    selectedSpecies[9]= fireRedSlots[1].getSelectedIndex();
                    break;

                case 2: //Emerald
                    selectedSpecies[8]= emeraldSlots[0].getSelectedIndex();
                    selectedSpecies[9]= emeraldSlots[1].getSelectedIndex();
                    break;

                case 3: //Sapphire
                    selectedSpecies[8]= sapphireSlots[0].getSelectedIndex();
                    selectedSpecies[9]= sapphireSlots[1].getSelectedIndex();
                    break;

                case 4: //Ruby
                    selectedSpecies[8]= rubySlots[0].getSelectedIndex();
                    selectedSpecies[9]= rubySlots[1].getSelectedIndex();
                    break;

                default:
                    selectedSpecies[8]= fieldSlots[8].getSelectedIndex();
                    selectedSpecies[9]= fieldSlots[9].getSelectedIndex();
                    break;
            }
        }
        else
        {
            selectedSpecies[8]= fieldSlots[8].getSelectedIndex();
            selectedSpecies[9]= fieldSlots[9].getSelectedIndex();
        }

        selectedSpecies[10]= fieldSlots[10].getSelectedIndex();
        selectedSpecies[11]= fieldSlots[11].getSelectedIndex();
        //endregion
        setTableIcons(selectedSpecies);

        oceanPreviewPanel.setImages(project,surfSlots,oldRodSlots,goodRodSlots,superRodSlots);
    }

    private void setTableIcons(int[] selectedSpecies)
    {
        String dataPath= project.getDataPath();
        try
        {
            String narcPath= Project.isDPPT(project) ? File.separator + "poketool" + File.separator + "icongra" + File.separator + (Project.isPlatinum(project) ? "pl_poke_icon.narc" : "poke_icon.narc") : File.separator + "a" + File.separator + "0" + File.separator + "5" + File.separator + "8";
            String folderPath= unpackedFolderPath + "poke_icon";
            new File(folderPath).deleteOnExit();
            toDelete.add(new File(folderPath));


            if(!new File(folderPath).exists())
            {
                Narctowl narctowl= new Narctowl(true);
                narctowl.unpack(dataPath + narcPath, folderPath);
            }

            ImageBase imageBase;
            for(int i= 0; i < selectedSpecies.length; i++)
            {
                int species= selectedSpecies[i];
                imageBase= new ImageBase(project,folderPath + File.separator + (7 + species) + ".bin", folderPath + File.separator  + "0.bin");
                BufferedImage base= imageBase.getImageTransparent(encounterSlotTable.getBackground(),32,32,paletteGuideTable[selectedSpecies[i]]);

                BufferedImage image= new BufferedImage(32,24,BufferedImage.TYPE_INT_ARGB);
                Graphics g2= image.getGraphics();
                g2.drawImage(base.getSubimage(0,8,32,24),0,0,null);


                tableData[0][i]= new ImageIcon(image);
            }
        } catch (IOException exception)
        {
            exception.printStackTrace();
        }

        DefaultTableModel tableModel= new DefaultTableModel(tableData,columnNames);
        encounterSlotTable.setModel(tableModel);
    }

    private void selectedSpeciesChangeActionPerformed(ActionEvent e)
    {
        if(encounterData != null)
        {
            if(((JComboBox<ComboBoxItem>) e.getSource()).getSelectedIndex() != -1)
                displayEncounterPreviewAction(e);
        }
    }
    //endregion

    //region Header
    private void encounterFileComboBoxActionPerformed(ActionEvent e)
    {
        // TODO add support for alt form data editing
        if(encounterData != null)
        {
            SinnohEncounterData encounterData= this.encounterData.get(encounterFileComboBox.getSelectedIndex());

            encounterRateSpinner.setValue(encounterData.getFieldRate());
            surfEncounterRateSpinner.setValue(encounterData.getSurfRate());
            oldRodEncounterRateSpinner.setValue(encounterData.getOldRate());
            goodRodEncounterRateSpinner.setValue(encounterData.getGoodRate());
            superRodEncounterRateSpinner.setValue(encounterData.getSuperRate());

            for(int i= 0; i < fieldSlots.length; i++)
            {
                fieldSlots[i].setSelectedIndex(encounterData.getFieldEncounters()[i]);
                fieldLevels[i].setValue(encounterData.getFieldLevels()[i]);
            }

            for(int i= 0; i < swarmSlots.length; i++)
            {
                swarmSlots[i].setSelectedIndex(encounterData.getSwarmEncounters()[i]);
            }

            for(int i= 0; i < daySlots.length; i++)
            {
                daySlots[i].setSelectedIndex(encounterData.getDayEncounters()[i]);
            }

            for(int i= 0; i < nightSlots.length; i++)
            {
                nightSlots[i].setSelectedIndex(encounterData.getNightEncounters()[i]);
            }

            for(int i= 0; i < radarSlots.length; i++)
            {
                radarSlots[i].setSelectedIndex(encounterData.getRadarEncounters()[i]);
            }

            for(int i= 0; i < rubySlots.length; i++)
            {
                rubySlots[i].setSelectedIndex(encounterData.getRuby()[i]);
            }

            for(int i= 0; i < sapphireSlots.length; i++)
            {
                sapphireSlots[i].setSelectedIndex(encounterData.getSapphire()[i]);
            }

            for(int i= 0; i < emeraldSlots.length; i++)
            {
                emeraldSlots[i].setSelectedIndex(encounterData.getEmerald()[i]);
            }

            for(int i= 0; i < fireRedSlots.length; i++)
            {
                fireRedSlots[i].setSelectedIndex(encounterData.getFireRed()[i]);
            }

            for(int i= 0; i < leafGreenSlots.length; i++)
            {
                leafGreenSlots[i].setSelectedIndex(encounterData.getLeafGreen()[i]);
            }

            for(int i= 0; i < surfSlots.length; i++)
            {
                surfSlots[i].setSelectedIndex(encounterData.getSurfEncounters()[i]);
                surfMinLevels[i].setValue(encounterData.getSurfMins()[i]);
                surfMaxLevels[i].setValue(encounterData.getSurfMaxs()[i]);
            }

            for(int i= 0; i < oldRodSlots.length; i++)
            {
                oldRodSlots[i].setSelectedIndex(encounterData.getOldEncounters()[i]);
                oldRodMinLevels[i].setValue(encounterData.getOldMins()[i]);
                oldRodMaxLevels[i].setValue(encounterData.getOldMaxs()[i]);
            }

            for(int i= 0; i < goodRodSlots.length; i++)
            {
                goodRodSlots[i].setSelectedIndex(encounterData.getGoodEncounters()[i]);
                goodRodMinLevels[i].setValue(encounterData.getGoodMins()[i]);
                goodRodMaxLevels[i].setValue(encounterData.getGoodMaxs()[i]);
            }

            for(int i= 0; i < superRodSlots.length; i++)
            {
                superRodSlots[i].setSelectedIndex(encounterData.getSuperEncounters()[i]);
                superRodMinLevels[i].setValue(encounterData.getSuperMins()[i]);
                superRodMaxLevels[i].setValue(encounterData.getSuperMaxs()[i]);
            }

            probabilityTable = encounterData.getFormProbabilities();
        }

        displayEncounterPreviewAction(e);
    }

    private void saveButtonActionPerformed(ActionEvent e) // TODO finish
    {
        System.out.println("Saving encounters");
        int fieldRate= (int) encounterRateSpinner.getValue();
        int surfRate= (int) surfEncounterRateSpinner.getValue();
        int oldRodRate= (int) oldRodEncounterRateSpinner.getValue();
        int goodRodRate= (int) goodRodEncounterRateSpinner.getValue();
        int superRodRate= (int) superRodEncounterRateSpinner.getValue();

        int[] fieldLevelsNew= new int[fieldSlots.length];
        int[] fieldSlotsNew= new int[fieldSlots.length];
        for(int i= 0; i < fieldSlots.length; i++)
        {
            fieldLevelsNew[i]= (int) fieldLevels[i].getValue();
            fieldSlotsNew[i]= fieldSlots[i].getSelectedIndex();
        }

        int[] swarmEncountersNew= new int[swarmSlots.length];
        int[] dayEncountersNew= new int[daySlots.length];
        int[] nightEncountersNew= new int[nightSlots.length];
        for(int i= 0; i < swarmEncountersNew.length; i++)
        {
            swarmEncountersNew[i]= swarmSlots[i].getSelectedIndex();
            dayEncountersNew[i]= daySlots[i].getSelectedIndex();
            nightEncountersNew[i]= nightSlots[i].getSelectedIndex();
        }

        int[] radarEncountersNew= new int[radarSlots.length];
        for(int i= 0; i < radarSlots.length; i++)
        {
            radarEncountersNew[i]= radarSlots[i].getSelectedIndex();
        }

        int[] rubyEncountersNew= new int[rubySlots.length];
        int[] sapphireEncountersNew= new int[sapphireSlots.length];
        int[] emeraldEncountersNew= new int[emeraldSlots.length];
        int[] fireRedEncountersNew= new int[fireRedSlots.length];
        int[] leafGreenEncountersNew= new int[leafGreenSlots.length];
        for(int i= 0; i < rubySlots.length; i++)
        {
            rubyEncountersNew[i]= rubySlots[i].getSelectedIndex();
            sapphireEncountersNew[i]= sapphireSlots[i].getSelectedIndex();
            emeraldEncountersNew[i]= emeraldSlots[i].getSelectedIndex();
            fireRedEncountersNew[i]= fireRedSlots[i].getSelectedIndex();
            leafGreenEncountersNew[i]= leafGreenSlots[i].getSelectedIndex();
        }

        int[] surfEncountersNew= new int[surfSlots.length];
        int[] surfMinNew= new int[surfMinLevels.length];
        int[] surfMaxNew= new int[surfMaxLevels.length];
        for(int i= 0; i < surfSlots.length; i++)
        {
            surfEncountersNew[i]= surfSlots[i].getSelectedIndex();
            surfMinNew[i]= (int) surfMinLevels[i].getValue();
            surfMaxNew[i]= (int) surfMaxLevels[i].getValue();
        }

        int[] oldRodEncountersNew= new int[oldRodSlots.length];
        int[] oldRodMinNew= new int[oldRodMinLevels.length];
        int[] oldRodMaxNew= new int[oldRodMaxLevels.length];
        for(int i= 0; i < oldRodSlots.length; i++)
        {
            oldRodEncountersNew[i]= oldRodSlots[i].getSelectedIndex();
            oldRodMinNew[i]= (int) oldRodMinLevels[i].getValue();
            oldRodMaxNew[i]= (int) oldRodMaxLevels[i].getValue();
        }

        int[] goodRodEncountersNew= new int[goodRodSlots.length];
        int[] goodRodMinNew= new int[goodRodMinLevels.length];
        int[] goodRodMaxNew= new int[goodRodMaxLevels.length];
        for(int i= 0; i < goodRodSlots.length; i++)
        {
            goodRodEncountersNew[i]= goodRodSlots[i].getSelectedIndex();
            goodRodMinNew[i]= (int) goodRodMinLevels[i].getValue();
            goodRodMaxNew[i]= (int) goodRodMaxLevels[i].getValue();
        }

        int[] superRodEncountersNew= new int[superRodSlots.length];
        int[] superRodMinNew= new int[superRodMinLevels.length];
        int[] superRodMaxNew= new int[superRodMaxLevels.length];
        for(int i= 0; i < superRodSlots.length; i++)
        {
            superRodEncountersNew[i]= superRodSlots[i].getSelectedIndex();
            superRodMinNew[i]= (int) superRodMinLevels[i].getValue();
            superRodMaxNew[i]= (int) superRodMaxLevels[i].getValue();
        }

        //TODO replace with data from editor table
        byte[][] probabilityTableNew = probabilityTable;
        SinnohEncounterData thisEncounterFileData= new SinnohEncounterData()
        {
            @Override
            public int getFieldRate()
            {
                return fieldRate;
            }

            @Override
            public int[] getFieldLevels()
            {
                return fieldLevelsNew;
            }

            @Override
            public int[] getFieldEncounters()
            {
                return fieldSlotsNew;
            }

            @Override
            public int[] getSwarmEncounters()
            {
                return swarmEncountersNew;
            }

            @Override
            public int[] getDayEncounters()
            {
                return dayEncountersNew;
            }

            @Override
            public int[] getNightEncounters()
            {
                return nightEncountersNew;
            }

            @Override
            public int[] getRadarEncounters()
            {
                return radarEncountersNew;
            }

            @Override
            public byte[][] getFormProbabilities()
            {
                return probabilityTableNew;
            }
            // TODO replace with data from editor table

            @Override
            public int[] getRuby()
            {
                return rubyEncountersNew;
            }

            @Override
            public int[] getSapphire()
            {
                return sapphireEncountersNew;
            }

            @Override
            public int[] getEmerald()
            {
                return emeraldEncountersNew;
            }

            @Override
            public int[] getFireRed()
            {
                return fireRedEncountersNew;
            }

            @Override
            public int[] getLeafGreen()
            {
                return leafGreenEncountersNew;
            }

            @Override
            public int getSurfRate()
            {
                return surfRate;
            }

            @Override
            public int[] getSurfMaxs()
            {
                return surfMaxNew;
            }

            @Override
            public int[] getSurfMins()
            {
                return surfMinNew;
            }

            @Override
            public int[] getSurfEncounters()
            {
                return surfEncountersNew;
            }

            @Override
            public int getOldRate()
            {
                return oldRodRate;
            }

            @Override
            public int[] getOldMaxs()
            {
                return oldRodMaxNew;
            }

            @Override
            public int[] getOldMins()
            {
                return oldRodMinNew;
            }

            @Override
            public int[] getOldEncounters()
            {
                return oldRodEncountersNew;
            }

            @Override
            public int getGoodRate()
            {
                return goodRodRate;
            }

            @Override
            public int[] getGoodMaxs()
            {
                return goodRodMaxNew;
            }

            @Override
            public int[] getGoodMins()
            {
                return goodRodMinNew;
            }

            @Override
            public int[] getGoodEncounters()
            {
                return goodRodEncountersNew;
            }

            @Override
            public int getSuperRate()
            {
                return superRodRate;
            }

            @Override
            public int[] getSuperMaxs()
            {
                return superRodMaxNew;
            }

            @Override
            public int[] getSuperMins()
            {
                return superRodMinNew;
            }

            @Override
            public int[] getSuperEncounters()
            {
                return superRodEncountersNew;
            }
        };

        encounterData.set(encounterFileComboBox.getSelectedIndex(), thisEncounterFileData);

        // TODO write code for actually saving to google sheets
        try
        {
            SinnohEncounterEditor encounterEditor = new SinnohEncounterEditor(project,project.getDataPath());
            SinnohEncounterReturn sheets = encounterEditor.produceSheets(encounterData);

            api.updateSheet("Field Encounters", sheets.getField());
            api.updateSheet("Water Encounters", sheets.getWater());
            api.updateSheet("Swarm/ Day/ Night Encounters", sheets.getSwarm());
            api.updateSheet("Poke Radar Encounters", sheets.getRadar());
            api.updateSheet("Dual-Slot Mode Encounters", sheets.getDualSlot());
            api.updateSheet("Alt Form Encounters", sheets.getFormProbabilityTable());
        }
        catch(IOException exception)
        {
            System.err.println("Encounter saving failed");
            exception.printStackTrace();
        }

    }

    private void speciesSearchButtonActionPerformed(ActionEvent e)
    {
        // TODO add your code here

        String searchTerm= speciesSearchTextField.getText();
        int searchId= -1;

        try
        {
            searchId= Integer.parseInt(searchTerm);
        }
        catch(NumberFormatException ignored)
        {

        }

        ArrayList<String> searchResults= new ArrayList<>();

        for(int i= 0; i < encounterData.size(); i++)
        {
            for(JComboBox<ComboBoxItem> fieldSlot : fieldSlots)
            {
                if(searchId != - 1)
                {
                    if(fieldSlot.getSelectedIndex() == searchId)
                    {
                        searchResults.add(i + ": LOCATION NAME");
                        break;
                    }
                }
                else
                {
                    if(nameData[fieldSlot.getSelectedIndex()].equalsIgnoreCase(searchTerm))
                    {
                        searchResults.add(i + ": LOCATION NAME");
                        break;
                    }
                }
            }
        }

        SearchResultsFrame searchResultsFrame= new SearchResultsFrame(true,this, searchResults);
    }

    private void locationSearchButtonActionPerformed(ActionEvent e)
    {
        // TODO add your code here
        JOptionPane.showMessageDialog(this,"Not implemented yet","Error",JOptionPane.ERROR_MESSAGE);
    }

    public void changeSelectedEncounterData(int idx)
    {
        encounterFileComboBox.setSelectedIndex(idx);
    }
    //endregion

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        encounterFileLabel = new JLabel();
        speciesSearchLabel = new JLabel();
        locationSearchLabel = new JLabel();
        encounterFileComboBox = new JComboBox();
        saveButton = new JButton();
        hSpacer1 = new JPanel(null);
        speciesSearchTextField = new JTextField();
        speciesSearchButton = new JButton();
        hSpacer2 = new JPanel(null);
        locationSearchTextField = new JTextField();
        locationSearchButton = new JButton();
        separator1 = new JSeparator();
        label4 = new JLabel();
        label3 = new JLabel();
        tabbedPane1 = new JTabbedPane();
        fieldEncounterPanel = new JPanel();
        fieldPanel = new JPanel();
        label31 = new JLabel();
        encounterRateSpinner = new JSpinner();
        separator8 = new JSeparator();
        label48 = new JLabel();
        label47 = new JLabel();
        label33 = new JLabel();
        label6 = new JLabel();
        field20ComboBox1 = new JComboBox<>();
        field20LevelSpinner1 = new JSpinner();
        label7 = new JLabel();
        field20ComboBox2 = new JComboBox<>();
        field20LevelSpinner2 = new JSpinner();
        label8 = new JLabel();
        field10ComboBox1 = new JComboBox<>();
        field10LevelSpinner1 = new JSpinner();
        label9 = new JLabel();
        field10ComboBox2 = new JComboBox<>();
        field10LevelSpinner2 = new JSpinner();
        label10 = new JLabel();
        field10ComboBox3 = new JComboBox<>();
        field10LevelSpinner3 = new JSpinner();
        label11 = new JLabel();
        field10ComboBox4 = new JComboBox<>();
        field10LevelSpinner4 = new JSpinner();
        label12 = new JLabel();
        field5ComboBox1 = new JComboBox<>();
        field5LevelSpinner1 = new JSpinner();
        label13 = new JLabel();
        field5ComboBox2 = new JComboBox<>();
        field5LevelSpinner2 = new JSpinner();
        label14 = new JLabel();
        field4ComboBox1 = new JComboBox<>();
        field4LevelSpinner1 = new JSpinner();
        label15 = new JLabel();
        field4ComboBox2 = new JComboBox<>();
        field4LevelSpinner2 = new JSpinner();
        label16 = new JLabel();
        field1ComboBox1 = new JComboBox<>();
        field1LevelSpinner1 = new JSpinner();
        label17 = new JLabel();
        field1ComboBox2 = new JComboBox<>();
        field1LevelSpinner2 = new JSpinner();
        swarmDayNightPanel = new JPanel();
        label18 = new JLabel();
        label19 = new JLabel();
        swarm20ComboBox1 = new JComboBox<>();
        label20 = new JLabel();
        label21 = new JLabel();
        swarm20ComboBox2 = new JComboBox<>();
        separator2 = new JSeparator();
        label45 = new JLabel();
        label22 = new JLabel();
        day10ComboBox1 = new JComboBox<>();
        label23 = new JLabel();
        day10ComboBox2 = new JComboBox<>();
        separator3 = new JSeparator();
        label24 = new JLabel();
        label46 = new JLabel();
        label25 = new JLabel();
        night10ComboBox1 = new JComboBox<>();
        label26 = new JLabel();
        night10ComboBox2 = new JComboBox<>();
        dualSlotPanel = new JPanel();
        label5 = new JLabel();
        label1 = new JLabel();
        rubyComboBox1 = new JComboBox<>();
        label2 = new JLabel();
        rubyComboBox2 = new JComboBox<>();
        separator4 = new JSeparator();
        label36 = new JLabel();
        label37 = new JLabel();
        sapphireComboBox1 = new JComboBox<>();
        label38 = new JLabel();
        sapphireComboBox2 = new JComboBox<>();
        separator5 = new JSeparator();
        label32 = new JLabel();
        label39 = new JLabel();
        emeraldComboBox1 = new JComboBox<>();
        label40 = new JLabel();
        emeraldComboBox2 = new JComboBox<>();
        separator6 = new JSeparator();
        label34 = new JLabel();
        label41 = new JLabel();
        fireRedComboBox1 = new JComboBox<>();
        label42 = new JLabel();
        fireRedComboBox2 = new JComboBox<>();
        separator7 = new JSeparator();
        label35 = new JLabel();
        label43 = new JLabel();
        leafGreenComboBox1 = new JComboBox<>();
        label44 = new JLabel();
        leafGreenComboBox2 = new JComboBox<>();
        pokeRadarPanel = new JPanel();
        label27 = new JLabel();
        radar10ComboBox1 = new JComboBox<>();
        label28 = new JLabel();
        radar10ComboBox2 = new JComboBox<>();
        label29 = new JLabel();
        radar10ComboBox3 = new JComboBox<>();
        label30 = new JLabel();
        radar10ComboBox4 = new JComboBox<>();
        encounterCalculatorPanel = new JPanel();
        morningRadioButton = new JRadioButton();
        dayRadioButton = new JRadioButton();
        nightRadioButton = new JRadioButton();
        hSpacer3 = new JPanel(null);
        swarmCheckbox = new JCheckBox();
        radarCheckbox = new JCheckBox();
        dualSlotCheckbox = new JCheckBox();
        scrollPane1 = new JScrollPane();
        encounterSlotTable = new SinnohEncounterPanel.ModifiedTable();
        waterEncounterPanel = new JPanel();
        surfPanel = new JPanel();
        label49 = new JLabel();
        surfEncounterRateSpinner = new JSpinner();
        separator9 = new JSeparator();
        label58 = new JLabel();
        label57 = new JLabel();
        label55 = new JLabel();
        label56 = new JLabel();
        label50 = new JLabel();
        surf60ComboBox = new JComboBox<>();
        surf60MinLevelSpinner = new JSpinner();
        surf60MaxLevelSpinner = new JSpinner();
        label51 = new JLabel();
        surf30ComboBox = new JComboBox<>();
        surf30MinLevelSpinner = new JSpinner();
        surf30MaxLevelSpinner = new JSpinner();
        label52 = new JLabel();
        surf5MaxLevelSpinner = new JSpinner();
        surf5ComboBox = new JComboBox<>();
        surf5MinLevelSpinner = new JSpinner();
        label53 = new JLabel();
        surf4MaxLevelSpinner = new JSpinner();
        surf4ComboBox = new JComboBox<>();
        surf4MinLevelSpinner = new JSpinner();
        label54 = new JLabel();
        surf1MaxLevelSpinner = new JSpinner();
        surf1ComboBox = new JComboBox<>();
        surf1MinLevelSpinner = new JSpinner();
        oldRodPanel = new JPanel();
        label59 = new JLabel();
        oldRodEncounterRateSpinner = new JSpinner();
        separator10 = new JSeparator();
        label60 = new JLabel();
        label61 = new JLabel();
        label62 = new JLabel();
        label63 = new JLabel();
        label64 = new JLabel();
        oldRod60ComboBox = new JComboBox<>();
        oldRod60MinLevelSpinner = new JSpinner();
        oldRod60MaxLevelSpinner = new JSpinner();
        label65 = new JLabel();
        oldRod30ComboBox = new JComboBox<>();
        oldRod30MinLevelSpinner = new JSpinner();
        oldRod30MaxLevelSpinner = new JSpinner();
        label66 = new JLabel();
        oldRod5MaxLevelSpinner = new JSpinner();
        oldRod5ComboBox = new JComboBox<>();
        oldRod5MinLevelSpinner = new JSpinner();
        label67 = new JLabel();
        oldRod4MaxLevelSpinner = new JSpinner();
        oldRod4ComboBox = new JComboBox<>();
        oldRod4MinLevelSpinner = new JSpinner();
        label68 = new JLabel();
        oldRod1MaxLevelSpinner = new JSpinner();
        oldRod1ComboBox = new JComboBox<>();
        oldRod1MinLevelSpinner = new JSpinner();
        goodRodPanel = new JPanel();
        label69 = new JLabel();
        goodRodEncounterRateSpinner = new JSpinner();
        separator11 = new JSeparator();
        label70 = new JLabel();
        label71 = new JLabel();
        label72 = new JLabel();
        label73 = new JLabel();
        label74 = new JLabel();
        goodRod60ComboBox = new JComboBox<>();
        goodRod60MinLevelSpinner = new JSpinner();
        goodRod60MaxLevelSpinner = new JSpinner();
        label75 = new JLabel();
        goodRod30ComboBox = new JComboBox<>();
        goodRod30MinLevelSpinner = new JSpinner();
        goodRod30MaxLevelSpinner = new JSpinner();
        label76 = new JLabel();
        goodRod5MaxLevelSpinner = new JSpinner();
        goodRod5ComboBox = new JComboBox<>();
        goodRod5MinLevelSpinner = new JSpinner();
        label77 = new JLabel();
        goodRod4MaxLevelSpinner = new JSpinner();
        goodRod4ComboBox = new JComboBox<>();
        goodRod4MinLevelSpinner = new JSpinner();
        label78 = new JLabel();
        goodRod1MaxLevelSpinner = new JSpinner();
        goodRod1ComboBox = new JComboBox<>();
        goodRod1MinLevelSpinner = new JSpinner();
        superRodPanel = new JPanel();
        label79 = new JLabel();
        superRodEncounterRateSpinner = new JSpinner();
        separator12 = new JSeparator();
        label80 = new JLabel();
        label81 = new JLabel();
        label82 = new JLabel();
        label83 = new JLabel();
        label84 = new JLabel();
        superRod60ComboBox = new JComboBox<>();
        superRod60MinLevelSpinner = new JSpinner();
        superRod60MaxLevelSpinner = new JSpinner();
        label85 = new JLabel();
        superRod30ComboBox = new JComboBox<>();
        superRod30MinLevelSpinner = new JSpinner();
        superRod30MaxLevelSpinner = new JSpinner();
        label86 = new JLabel();
        superRod5MaxLevelSpinner = new JSpinner();
        superRod5ComboBox = new JComboBox<>();
        superRod5MinLevelSpinner = new JSpinner();
        label87 = new JLabel();
        superRod4MaxLevelSpinner = new JSpinner();
        superRod4ComboBox = new JComboBox<>();
        superRod4MinLevelSpinner = new JSpinner();
        label88 = new JLabel();
        superRod1MaxLevelSpinner = new JSpinner();
        superRod1ComboBox = new JComboBox<>();
        superRod1MinLevelSpinner = new JSpinner();
        oceanPreviewPanel = new SinnohEncounterPanel.OceanPanel();
        alternateFormDataPanel = new JPanel();

        //======== this ========
        setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[fill]" +
            "[124,fill]" +
            "[80,fill]" +
            "[45,grow,fill]" +
            "[150,fill]" +
            "[fill]" +
            "[45,grow,fill]" +
            "[154,fill]" +
            "[fill]",
            // rows
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]"));

        //---- encounterFileLabel ----
        encounterFileLabel.setText("Encounter FIle");
        add(encounterFileLabel, "cell 1 0,alignx center,growx 0");

        //---- speciesSearchLabel ----
        speciesSearchLabel.setText("Search by Species");
        add(speciesSearchLabel, "cell 4 0,alignx center,growx 0");

        //---- locationSearchLabel ----
        locationSearchLabel.setText("Search by Location");
        add(locationSearchLabel, "cell 7 0,alignx center,growx 0");

        //---- encounterFileComboBox ----
        encounterFileComboBox.addActionListener(e -> encounterFileComboBoxActionPerformed(e));
        add(encounterFileComboBox, "cell 1 1");

        //---- saveButton ----
        saveButton.setText("Save");
        saveButton.addActionListener(e -> saveButtonActionPerformed(e));
        add(saveButton, "cell 2 1");
        add(hSpacer1, "cell 3 1");
        add(speciesSearchTextField, "cell 4 1");

        //---- speciesSearchButton ----
        speciesSearchButton.setText("Search");
        speciesSearchButton.addActionListener(e -> speciesSearchButtonActionPerformed(e));
        add(speciesSearchButton, "cell 5 1");
        add(hSpacer2, "cell 6 1");
        add(locationSearchTextField, "cell 7 1");

        //---- locationSearchButton ----
        locationSearchButton.setText("Search");
        locationSearchButton.addActionListener(e -> locationSearchButtonActionPerformed(e));
        add(locationSearchButton, "cell 8 1");
        add(separator1, "cell 1 2 8 1");

        //---- label4 ----
        label4.setText("This encounter file is used in:");
        add(label4, "cell 1 3 8 1,alignx center,growx 0");

        //---- label3 ----
        label3.setText("MYSTERY ZONE");
        add(label3, "cell 1 4 8 1,alignx center,growx 0");

        //======== tabbedPane1 ========
        {
            tabbedPane1.setBorder(new TitledBorder(""));

            //======== fieldEncounterPanel ========
            {
                fieldEncounterPanel.setLayout(new MigLayout(
                    "hidemode 3",
                    // columns
                    "[grow,fill]" +
                    "[274,grow,fill]" +
                    "[grow,fill]",
                    // rows
                    "[]" +
                    "[]" +
                    "[grow]" +
                    "[]"));

                //======== fieldPanel ========
                {
                    fieldPanel.setBorder(new TitledBorder("Field"));
                    fieldPanel.setLayout(new MigLayout(
                        "hidemode 3",
                        // columns
                        "[fill]" +
                        "[140,grow,fill]" +
                        "[right]",
                        // rows
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]"));

                    //---- label31 ----
                    label31.setText("Encounter Rate:");
                    fieldPanel.add(label31, "cell 0 0 2 1");

                    //---- encounterRateSpinner ----
                    encounterRateSpinner.setModel(new SpinnerNumberModel(0, 0, 255, 1));
                    fieldPanel.add(encounterRateSpinner, "cell 2 0");
                    fieldPanel.add(separator8, "cell 0 1 3 1");

                    //---- label48 ----
                    label48.setText("Rate");
                    label48.setFont(new Font(".SF NS Text", Font.PLAIN, 11));
                    fieldPanel.add(label48, "cell 0 2,alignx center,growx 0");

                    //---- label47 ----
                    label47.setText("Species");
                    label47.setFont(new Font(".SF NS Text", Font.PLAIN, 11));
                    fieldPanel.add(label47, "cell 1 2,alignx center,growx 0");

                    //---- label33 ----
                    label33.setText("Level");
                    label33.setFont(new Font(".SF NS Text", Font.PLAIN, 11));
                    fieldPanel.add(label33, "cell 2 2,alignx center,growx 0");

                    //---- label6 ----
                    label6.setText("20%");
                    fieldPanel.add(label6, "cell 0 3,alignx right,growx 0");
                    fieldPanel.add(field20ComboBox1, "cell 1 3");

                    //---- field20LevelSpinner1 ----
                    field20LevelSpinner1.setMaximumSize(new Dimension(70, 30));
                    field20LevelSpinner1.setMinimumSize(new Dimension(70, 30));
                    field20LevelSpinner1.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    fieldPanel.add(field20LevelSpinner1, "cell 2 3");

                    //---- label7 ----
                    label7.setText("20%");
                    fieldPanel.add(label7, "cell 0 4,alignx right,growx 0");
                    fieldPanel.add(field20ComboBox2, "cell 1 4");

                    //---- field20LevelSpinner2 ----
                    field20LevelSpinner2.setMaximumSize(new Dimension(70, 30));
                    field20LevelSpinner2.setMinimumSize(new Dimension(70, 30));
                    field20LevelSpinner2.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    fieldPanel.add(field20LevelSpinner2, "cell 2 4");

                    //---- label8 ----
                    label8.setText("10%");
                    fieldPanel.add(label8, "cell 0 5,alignx right,growx 0");
                    fieldPanel.add(field10ComboBox1, "cell 1 5");

                    //---- field10LevelSpinner1 ----
                    field10LevelSpinner1.setMaximumSize(new Dimension(70, 30));
                    field10LevelSpinner1.setMinimumSize(new Dimension(70, 30));
                    field10LevelSpinner1.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    fieldPanel.add(field10LevelSpinner1, "cell 2 5");

                    //---- label9 ----
                    label9.setText("10%");
                    fieldPanel.add(label9, "cell 0 6,alignx right,growx 0");
                    fieldPanel.add(field10ComboBox2, "cell 1 6");

                    //---- field10LevelSpinner2 ----
                    field10LevelSpinner2.setMaximumSize(new Dimension(70, 30));
                    field10LevelSpinner2.setMinimumSize(new Dimension(70, 30));
                    field10LevelSpinner2.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    fieldPanel.add(field10LevelSpinner2, "cell 2 6");

                    //---- label10 ----
                    label10.setText("10%");
                    fieldPanel.add(label10, "cell 0 7,alignx right,growx 0");
                    fieldPanel.add(field10ComboBox3, "cell 1 7");

                    //---- field10LevelSpinner3 ----
                    field10LevelSpinner3.setMaximumSize(new Dimension(70, 30));
                    field10LevelSpinner3.setMinimumSize(new Dimension(70, 30));
                    field10LevelSpinner3.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    fieldPanel.add(field10LevelSpinner3, "cell 2 7");

                    //---- label11 ----
                    label11.setText("10%");
                    fieldPanel.add(label11, "cell 0 8,alignx right,growx 0");
                    fieldPanel.add(field10ComboBox4, "cell 1 8");

                    //---- field10LevelSpinner4 ----
                    field10LevelSpinner4.setMaximumSize(new Dimension(70, 30));
                    field10LevelSpinner4.setMinimumSize(new Dimension(70, 30));
                    field10LevelSpinner4.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    fieldPanel.add(field10LevelSpinner4, "cell 2 8");

                    //---- label12 ----
                    label12.setText("5%");
                    fieldPanel.add(label12, "cell 0 9,alignx right,growx 0");
                    fieldPanel.add(field5ComboBox1, "cell 1 9");

                    //---- field5LevelSpinner1 ----
                    field5LevelSpinner1.setMaximumSize(new Dimension(70, 30));
                    field5LevelSpinner1.setMinimumSize(new Dimension(70, 30));
                    field5LevelSpinner1.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    fieldPanel.add(field5LevelSpinner1, "cell 2 9");

                    //---- label13 ----
                    label13.setText("5%");
                    fieldPanel.add(label13, "cell 0 10,alignx right,growx 0");
                    fieldPanel.add(field5ComboBox2, "cell 1 10");

                    //---- field5LevelSpinner2 ----
                    field5LevelSpinner2.setMaximumSize(new Dimension(70, 30));
                    field5LevelSpinner2.setMinimumSize(new Dimension(70, 30));
                    field5LevelSpinner2.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    fieldPanel.add(field5LevelSpinner2, "cell 2 10");

                    //---- label14 ----
                    label14.setText("4%");
                    fieldPanel.add(label14, "cell 0 11,alignx right,growx 0");
                    fieldPanel.add(field4ComboBox1, "cell 1 11");

                    //---- field4LevelSpinner1 ----
                    field4LevelSpinner1.setMaximumSize(new Dimension(70, 30));
                    field4LevelSpinner1.setMinimumSize(new Dimension(70, 30));
                    field4LevelSpinner1.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    fieldPanel.add(field4LevelSpinner1, "cell 2 11");

                    //---- label15 ----
                    label15.setText("4%");
                    fieldPanel.add(label15, "cell 0 12,alignx right,growx 0");
                    fieldPanel.add(field4ComboBox2, "cell 1 12");

                    //---- field4LevelSpinner2 ----
                    field4LevelSpinner2.setMaximumSize(new Dimension(70, 30));
                    field4LevelSpinner2.setMinimumSize(new Dimension(70, 30));
                    field4LevelSpinner2.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    fieldPanel.add(field4LevelSpinner2, "cell 2 12");

                    //---- label16 ----
                    label16.setText("1%");
                    fieldPanel.add(label16, "cell 0 13,alignx right,growx 0");
                    fieldPanel.add(field1ComboBox1, "cell 1 13");

                    //---- field1LevelSpinner1 ----
                    field1LevelSpinner1.setMaximumSize(new Dimension(70, 30));
                    field1LevelSpinner1.setMinimumSize(new Dimension(70, 30));
                    field1LevelSpinner1.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    fieldPanel.add(field1LevelSpinner1, "cell 2 13");

                    //---- label17 ----
                    label17.setText("1%");
                    fieldPanel.add(label17, "cell 0 14,alignx right,growx 0");
                    fieldPanel.add(field1ComboBox2, "cell 1 14");

                    //---- field1LevelSpinner2 ----
                    field1LevelSpinner2.setMaximumSize(new Dimension(70, 30));
                    field1LevelSpinner2.setMinimumSize(new Dimension(70, 30));
                    field1LevelSpinner2.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    fieldPanel.add(field1LevelSpinner2, "cell 2 14");
                }
                fieldEncounterPanel.add(fieldPanel, "cell 0 0 1 2,grow");

                //======== swarmDayNightPanel ========
                {
                    swarmDayNightPanel.setBorder(new TitledBorder("Swarm/ Day/ Night"));
                    swarmDayNightPanel.setLayout(new MigLayout(
                        "hidemode 3",
                        // columns
                        "[fill]" +
                        "[140,grow,fill]",
                        // rows
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]"));

                    //---- label18 ----
                    label18.setText("Swarm");
                    swarmDayNightPanel.add(label18, "cell 0 0,alignx right,growx 0");

                    //---- label19 ----
                    label19.setText("20%");
                    swarmDayNightPanel.add(label19, "cell 0 1,alignx right,growx 0");
                    swarmDayNightPanel.add(swarm20ComboBox1, "cell 1 1,growx");

                    //---- label20 ----
                    label20.setText("20%");
                    swarmDayNightPanel.add(label20, "cell 0 2,alignx right,growx 0");

                    //---- label21 ----
                    label21.setText("Day");
                    swarmDayNightPanel.add(label21, "cell 0 4,alignx right,growx 0");
                    swarmDayNightPanel.add(swarm20ComboBox2, "cell 1 2,growx");
                    swarmDayNightPanel.add(separator2, "cell 0 3 2 1");

                    //---- label45 ----
                    label45.setText("(10 am - 8 pm)");
                    swarmDayNightPanel.add(label45, "cell 1 4,alignx center,growx 0");

                    //---- label22 ----
                    label22.setText("10%");
                    swarmDayNightPanel.add(label22, "cell 0 5,alignx right,growx 0");
                    swarmDayNightPanel.add(day10ComboBox1, "cell 1 5");

                    //---- label23 ----
                    label23.setText("10%");
                    swarmDayNightPanel.add(label23, "cell 0 6,alignx right,growx 0");
                    swarmDayNightPanel.add(day10ComboBox2, "cell 1 6");
                    swarmDayNightPanel.add(separator3, "cell 0 7 2 1");

                    //---- label24 ----
                    label24.setText("Night");
                    swarmDayNightPanel.add(label24, "cell 0 8,alignx right,growx 0");

                    //---- label46 ----
                    label46.setText("(8 pm - 4 am)");
                    swarmDayNightPanel.add(label46, "cell 1 8,alignx center,growx 0");

                    //---- label25 ----
                    label25.setText("10%");
                    swarmDayNightPanel.add(label25, "cell 0 9,alignx right,growx 0");
                    swarmDayNightPanel.add(night10ComboBox1, "cell 1 9");

                    //---- label26 ----
                    label26.setText("10%");
                    swarmDayNightPanel.add(label26, "cell 0 10,alignx right,growx 0");
                    swarmDayNightPanel.add(night10ComboBox2, "cell 1 10");
                }
                fieldEncounterPanel.add(swarmDayNightPanel, "cell 1 0");

                //======== dualSlotPanel ========
                {
                    dualSlotPanel.setBorder(new TitledBorder("Dual-Slot Mode"));
                    dualSlotPanel.setLayout(new MigLayout(
                        "hidemode 3",
                        // columns
                        "[fill]" +
                        "[grow,fill]",
                        // rows
                        "[0]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]"));

                    //---- label5 ----
                    label5.setText("Ruby");
                    dualSlotPanel.add(label5, "cell 0 0,alignx right,growx 0");

                    //---- label1 ----
                    label1.setText("4%");
                    dualSlotPanel.add(label1, "cell 0 1,alignx right,growx 0");
                    dualSlotPanel.add(rubyComboBox1, "cell 1 1");

                    //---- label2 ----
                    label2.setText("4%");
                    dualSlotPanel.add(label2, "cell 0 2,alignx right,growx 0");
                    dualSlotPanel.add(rubyComboBox2, "cell 1 2");
                    dualSlotPanel.add(separator4, "cell 0 3 2 1");

                    //---- label36 ----
                    label36.setText("Sapphire");
                    dualSlotPanel.add(label36, "cell 0 4,alignx right,growx 0");

                    //---- label37 ----
                    label37.setText("4%");
                    dualSlotPanel.add(label37, "cell 0 5,alignx right,growx 0");
                    dualSlotPanel.add(sapphireComboBox1, "cell 1 5");

                    //---- label38 ----
                    label38.setText("4%");
                    dualSlotPanel.add(label38, "cell 0 6,alignx right,growx 0");
                    dualSlotPanel.add(sapphireComboBox2, "cell 1 6");
                    dualSlotPanel.add(separator5, "cell 0 7 2 1");

                    //---- label32 ----
                    label32.setText("Emerald");
                    dualSlotPanel.add(label32, "cell 0 8,alignx right,growx 0");

                    //---- label39 ----
                    label39.setText("4%");
                    dualSlotPanel.add(label39, "cell 0 9,alignx right,growx 0");
                    dualSlotPanel.add(emeraldComboBox1, "cell 1 9");

                    //---- label40 ----
                    label40.setText("4%");
                    dualSlotPanel.add(label40, "cell 0 10,alignx right,growx 0");
                    dualSlotPanel.add(emeraldComboBox2, "cell 1 10");
                    dualSlotPanel.add(separator6, "cell 0 11 2 1");

                    //---- label34 ----
                    label34.setText("FireRed");
                    dualSlotPanel.add(label34, "cell 0 12,alignx right,growx 0");

                    //---- label41 ----
                    label41.setText("4%");
                    dualSlotPanel.add(label41, "cell 0 13,alignx right,growx 0");
                    dualSlotPanel.add(fireRedComboBox1, "cell 1 13");

                    //---- label42 ----
                    label42.setText("4%");
                    dualSlotPanel.add(label42, "cell 0 14,alignx right,growx 0");
                    dualSlotPanel.add(fireRedComboBox2, "cell 1 14");
                    dualSlotPanel.add(separator7, "cell 0 15 2 1");

                    //---- label35 ----
                    label35.setText("LeafGreen");
                    label35.setFont(new Font(".SF NS Text", Font.PLAIN, 12));
                    dualSlotPanel.add(label35, "cell 0 16,alignx right,growx 0");

                    //---- label43 ----
                    label43.setText("4%");
                    dualSlotPanel.add(label43, "cell 0 17,alignx right,growx 0");
                    dualSlotPanel.add(leafGreenComboBox1, "cell 1 17");

                    //---- label44 ----
                    label44.setText("4%");
                    dualSlotPanel.add(label44, "cell 0 18,alignx right,growx 0");
                    dualSlotPanel.add(leafGreenComboBox2, "cell 1 18");
                }
                fieldEncounterPanel.add(dualSlotPanel, "cell 2 0 1 2,grow");

                //======== pokeRadarPanel ========
                {
                    pokeRadarPanel.setBorder(new TitledBorder("Pok\u00e9 Radar"));
                    pokeRadarPanel.setLayout(new MigLayout(
                        "hidemode 3",
                        // columns
                        "[fill]" +
                        "[grow,fill]",
                        // rows
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]"));

                    //---- label27 ----
                    label27.setText("10%");
                    pokeRadarPanel.add(label27, "cell 0 0");
                    pokeRadarPanel.add(radar10ComboBox1, "cell 1 0");

                    //---- label28 ----
                    label28.setText("10%");
                    pokeRadarPanel.add(label28, "cell 0 1");
                    pokeRadarPanel.add(radar10ComboBox2, "cell 1 1");

                    //---- label29 ----
                    label29.setText("10%");
                    pokeRadarPanel.add(label29, "cell 0 2");
                    pokeRadarPanel.add(radar10ComboBox3, "cell 1 2");

                    //---- label30 ----
                    label30.setText("10%");
                    pokeRadarPanel.add(label30, "cell 0 3");
                    pokeRadarPanel.add(radar10ComboBox4, "cell 1 3");
                }
                fieldEncounterPanel.add(pokeRadarPanel, "cell 1 1,aligny top,growy 0");

                //======== encounterCalculatorPanel ========
                {
                    encounterCalculatorPanel.setBorder(new TitledBorder("Encounter Slot Display"));
                    encounterCalculatorPanel.setLayout(new MigLayout(
                        "hidemode 3",
                        // columns
                        "[fill]" +
                        "[86,fill]" +
                        "[110,grow,fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]",
                        // rows
                        "[]" +
                        "[]0" +
                        "[]"));

                    //---- morningRadioButton ----
                    morningRadioButton.setText("Morning");
                    morningRadioButton.setSelected(true);
                    morningRadioButton.addActionListener(e -> morningRadioButtonActionPerformed(e));
                    encounterCalculatorPanel.add(morningRadioButton, "cell 0 0");

                    //---- dayRadioButton ----
                    dayRadioButton.setText("Day");
                    dayRadioButton.addActionListener(e -> dayRadioButtonActionPerformed(e));
                    encounterCalculatorPanel.add(dayRadioButton, "cell 1 0");

                    //---- nightRadioButton ----
                    nightRadioButton.setText("Night");
                    nightRadioButton.addActionListener(e -> nightRadioButtonActionPerformed(e));
                    encounterCalculatorPanel.add(nightRadioButton, "cell 1 0 2 1");
                    encounterCalculatorPanel.add(hSpacer3, "cell 2 0");

                    //---- swarmCheckbox ----
                    swarmCheckbox.setText("Swarm");
                    swarmCheckbox.addActionListener(e -> swarmCheckboxActionPerformed(e));
                    encounterCalculatorPanel.add(swarmCheckbox, "cell 3 0");

                    //---- radarCheckbox ----
                    radarCheckbox.setText("Pok\u00e9 Radar");
                    radarCheckbox.addActionListener(e -> radarCheckboxActionPerformed(e));
                    encounterCalculatorPanel.add(radarCheckbox, "cell 4 0");

                    //---- dualSlotCheckbox ----
                    dualSlotCheckbox.setText("Dual-Slot Mode");
                    dualSlotCheckbox.addActionListener(e -> dualSlotCheckboxActionPerformed(e));
                    encounterCalculatorPanel.add(dualSlotCheckbox, "cell 5 0");

                    //======== scrollPane1 ========
                    {
                        scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
                        scrollPane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                        scrollPane1.setEnabled(false);

                        //---- encounterSlotTable ----
                        encounterSlotTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
                        encounterSlotTable.setModel(new DefaultTableModel(
                            new Object[][] {
                                {null, null, null, null, "", null, "", null, "", "", null, null},
                            },
                            new String[] {
                                "20%", "20%", "10%", "10%", "10%", "10%", "5%", "5%", "4%", "4%", "1%", "1%"
                            }
                        ) {
                            boolean[] columnEditable = new boolean[] {
                                false, true, true, true, true, true, true, true, true, true, true, true
                            };
                            @Override
                            public boolean isCellEditable(int rowIndex, int columnIndex) {
                                return columnEditable[columnIndex];
                            }
                        });
                        encounterSlotTable.setRowSelectionAllowed(false);
                        encounterSlotTable.setToolTipText("Displays what the encounters in this area will look like with these parameters active");
                        encounterSlotTable.setRowHeight(54);
                        scrollPane1.setViewportView(encounterSlotTable);
                    }
                    encounterCalculatorPanel.add(scrollPane1, "cell 0 1 6 1,aligny top,grow 100 0");
                }
                fieldEncounterPanel.add(encounterCalculatorPanel, "cell 0 2 3 1,growy");
            }
            tabbedPane1.addTab("Field Encounters", fieldEncounterPanel);

            //======== waterEncounterPanel ========
            {
                waterEncounterPanel.setLayout(new MigLayout(
                    "hidemode 3",
                    // columns
                    "[389:389,grow,fill]" +
                    "[389,grow,fill]",
                    // rows
                    "[]" +
                    "[]" +
                    "[grow]"));

                //======== surfPanel ========
                {
                    surfPanel.setBorder(new TitledBorder("Surf Encounters"));
                    surfPanel.setLayout(new MigLayout(
                        "hidemode 3",
                        // columns
                        "[fill]" +
                        "[125:88,grow,fill]" +
                        "[fill]" +
                        "[fill]",
                        // rows
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]"));

                    //---- label49 ----
                    label49.setText("Encounter Rate:");
                    surfPanel.add(label49, "cell 0 0 3 1");

                    //---- surfEncounterRateSpinner ----
                    surfEncounterRateSpinner.setModel(new SpinnerNumberModel(0, 0, 255, 1));
                    surfPanel.add(surfEncounterRateSpinner, "cell 3 0");
                    surfPanel.add(separator9, "cell 0 1 4 1");

                    //---- label58 ----
                    label58.setText("Rate");
                    label58.setFont(new Font(".SF NS Text", Font.PLAIN, 11));
                    surfPanel.add(label58, "cell 0 2,alignx center,growx 0");

                    //---- label57 ----
                    label57.setText("Species");
                    label57.setFont(new Font(".SF NS Text", Font.PLAIN, 11));
                    surfPanel.add(label57, "cell 1 2,alignx center,growx 0");

                    //---- label55 ----
                    label55.setText("Min Level");
                    label55.setFont(new Font(".SF NS Text", Font.PLAIN, 11));
                    surfPanel.add(label55, "cell 2 2,alignx center,growx 0");

                    //---- label56 ----
                    label56.setText("Max Level");
                    label56.setFont(new Font(".SF NS Text", Font.PLAIN, 11));
                    surfPanel.add(label56, "cell 3 2,alignx center,growx 0");

                    //---- label50 ----
                    label50.setText("60%");
                    surfPanel.add(label50, "cell 0 3,alignx right,growx 0");
                    surfPanel.add(surf60ComboBox, "cell 1 3,growx");

                    //---- surf60MinLevelSpinner ----
                    surf60MinLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    surfPanel.add(surf60MinLevelSpinner, "cell 2 3,alignx center,growx 0");

                    //---- surf60MaxLevelSpinner ----
                    surf60MaxLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    surfPanel.add(surf60MaxLevelSpinner, "cell 3 3,alignx center,growx 0");

                    //---- label51 ----
                    label51.setText("30%");
                    surfPanel.add(label51, "cell 0 4,alignx right,growx 0");
                    surfPanel.add(surf30ComboBox, "cell 1 4,growx");

                    //---- surf30MinLevelSpinner ----
                    surf30MinLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    surfPanel.add(surf30MinLevelSpinner, "cell 2 4,alignx center,growx 0");

                    //---- surf30MaxLevelSpinner ----
                    surf30MaxLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    surfPanel.add(surf30MaxLevelSpinner, "cell 3 4,alignx center,growx 0");

                    //---- label52 ----
                    label52.setText("5%");
                    surfPanel.add(label52, "cell 0 5,alignx right,growx 0");

                    //---- surf5MaxLevelSpinner ----
                    surf5MaxLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    surfPanel.add(surf5MaxLevelSpinner, "cell 3 5,alignx center,growx 0");
                    surfPanel.add(surf5ComboBox, "cell 1 5,growx");

                    //---- surf5MinLevelSpinner ----
                    surf5MinLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    surfPanel.add(surf5MinLevelSpinner, "cell 2 5,alignx center,growx 0");

                    //---- label53 ----
                    label53.setText("4%");
                    surfPanel.add(label53, "cell 0 6,alignx right,growx 0");

                    //---- surf4MaxLevelSpinner ----
                    surf4MaxLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    surfPanel.add(surf4MaxLevelSpinner, "cell 3 6,alignx center,growx 0");
                    surfPanel.add(surf4ComboBox, "cell 1 6,growx");

                    //---- surf4MinLevelSpinner ----
                    surf4MinLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    surfPanel.add(surf4MinLevelSpinner, "cell 2 6,alignx center,growx 0");

                    //---- label54 ----
                    label54.setText("1%");
                    surfPanel.add(label54, "cell 0 7,alignx right,growx 0");

                    //---- surf1MaxLevelSpinner ----
                    surf1MaxLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    surfPanel.add(surf1MaxLevelSpinner, "cell 3 7,alignx center,growx 0");
                    surfPanel.add(surf1ComboBox, "cell 1 7,growx");

                    //---- surf1MinLevelSpinner ----
                    surf1MinLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    surfPanel.add(surf1MinLevelSpinner, "cell 2 7,alignx center,growx 0");
                }
                waterEncounterPanel.add(surfPanel, "cell 0 0,aligny top,grow 100 0");

                //======== oldRodPanel ========
                {
                    oldRodPanel.setBorder(new TitledBorder("Old Rod Encounters"));
                    oldRodPanel.setLayout(new MigLayout(
                        "hidemode 3",
                        // columns
                        "[fill]" +
                        "[125:88,grow,fill]" +
                        "[fill]" +
                        "[fill]",
                        // rows
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]"));

                    //---- label59 ----
                    label59.setText("Encounter Rate:");
                    oldRodPanel.add(label59, "cell 0 0 3 1");

                    //---- oldRodEncounterRateSpinner ----
                    oldRodEncounterRateSpinner.setModel(new SpinnerNumberModel(0, 0, 255, 1));
                    oldRodPanel.add(oldRodEncounterRateSpinner, "cell 3 0");
                    oldRodPanel.add(separator10, "cell 0 1 4 1");

                    //---- label60 ----
                    label60.setText("Rate");
                    label60.setFont(new Font(".SF NS Text", Font.PLAIN, 11));
                    oldRodPanel.add(label60, "cell 0 2,alignx center,growx 0");

                    //---- label61 ----
                    label61.setText("Species");
                    label61.setFont(new Font(".SF NS Text", Font.PLAIN, 11));
                    oldRodPanel.add(label61, "cell 1 2,alignx center,growx 0");

                    //---- label62 ----
                    label62.setText("Min Level");
                    label62.setFont(new Font(".SF NS Text", Font.PLAIN, 11));
                    oldRodPanel.add(label62, "cell 2 2,alignx center,growx 0");

                    //---- label63 ----
                    label63.setText("Max Level");
                    label63.setFont(new Font(".SF NS Text", Font.PLAIN, 11));
                    oldRodPanel.add(label63, "cell 3 2,alignx center,growx 0");

                    //---- label64 ----
                    label64.setText("60%");
                    oldRodPanel.add(label64, "cell 0 3,alignx right,growx 0");
                    oldRodPanel.add(oldRod60ComboBox, "cell 1 3,growx");

                    //---- oldRod60MinLevelSpinner ----
                    oldRod60MinLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    oldRodPanel.add(oldRod60MinLevelSpinner, "cell 2 3,alignx center,growx 0");

                    //---- oldRod60MaxLevelSpinner ----
                    oldRod60MaxLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    oldRodPanel.add(oldRod60MaxLevelSpinner, "cell 3 3,alignx center,growx 0");

                    //---- label65 ----
                    label65.setText("30%");
                    oldRodPanel.add(label65, "cell 0 4,alignx right,growx 0");
                    oldRodPanel.add(oldRod30ComboBox, "cell 1 4,growx");

                    //---- oldRod30MinLevelSpinner ----
                    oldRod30MinLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    oldRodPanel.add(oldRod30MinLevelSpinner, "cell 2 4,alignx center,growx 0");

                    //---- oldRod30MaxLevelSpinner ----
                    oldRod30MaxLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    oldRodPanel.add(oldRod30MaxLevelSpinner, "cell 3 4,alignx center,growx 0");

                    //---- label66 ----
                    label66.setText("5%");
                    oldRodPanel.add(label66, "cell 0 5,alignx right,growx 0");

                    //---- oldRod5MaxLevelSpinner ----
                    oldRod5MaxLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    oldRodPanel.add(oldRod5MaxLevelSpinner, "cell 3 5,alignx center,growx 0");
                    oldRodPanel.add(oldRod5ComboBox, "cell 1 5,growx");

                    //---- oldRod5MinLevelSpinner ----
                    oldRod5MinLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    oldRodPanel.add(oldRod5MinLevelSpinner, "cell 2 5,alignx center,growx 0");

                    //---- label67 ----
                    label67.setText("4%");
                    oldRodPanel.add(label67, "cell 0 6,alignx right,growx 0");

                    //---- oldRod4MaxLevelSpinner ----
                    oldRod4MaxLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    oldRodPanel.add(oldRod4MaxLevelSpinner, "cell 3 6,alignx center,growx 0");
                    oldRodPanel.add(oldRod4ComboBox, "cell 1 6,growx");

                    //---- oldRod4MinLevelSpinner ----
                    oldRod4MinLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    oldRodPanel.add(oldRod4MinLevelSpinner, "cell 2 6,alignx center,growx 0");

                    //---- label68 ----
                    label68.setText("1%");
                    oldRodPanel.add(label68, "cell 0 7,alignx right,growx 0");

                    //---- oldRod1MaxLevelSpinner ----
                    oldRod1MaxLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    oldRodPanel.add(oldRod1MaxLevelSpinner, "cell 3 7,alignx center,growx 0");
                    oldRodPanel.add(oldRod1ComboBox, "cell 1 7,growx");

                    //---- oldRod1MinLevelSpinner ----
                    oldRod1MinLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    oldRodPanel.add(oldRod1MinLevelSpinner, "cell 2 7,alignx center,growx 0");
                }
                waterEncounterPanel.add(oldRodPanel, "cell 1 0,aligny top,grow 100 0");

                //======== goodRodPanel ========
                {
                    goodRodPanel.setBorder(new TitledBorder("Good Rod Encounters"));
                    goodRodPanel.setLayout(new MigLayout(
                        "hidemode 3",
                        // columns
                        "[fill]" +
                        "[125:88,grow,fill]" +
                        "[fill]" +
                        "[fill]",
                        // rows
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]"));

                    //---- label69 ----
                    label69.setText("Encounter Rate:");
                    goodRodPanel.add(label69, "cell 0 0 3 1");

                    //---- goodRodEncounterRateSpinner ----
                    goodRodEncounterRateSpinner.setModel(new SpinnerNumberModel(0, 0, 255, 1));
                    goodRodPanel.add(goodRodEncounterRateSpinner, "cell 3 0");
                    goodRodPanel.add(separator11, "cell 0 1 4 1");

                    //---- label70 ----
                    label70.setText("Rate");
                    label70.setFont(new Font(".SF NS Text", Font.PLAIN, 11));
                    goodRodPanel.add(label70, "cell 0 2,alignx center,growx 0");

                    //---- label71 ----
                    label71.setText("Species");
                    label71.setFont(new Font(".SF NS Text", Font.PLAIN, 11));
                    goodRodPanel.add(label71, "cell 1 2,alignx center,growx 0");

                    //---- label72 ----
                    label72.setText("Min Level");
                    label72.setFont(new Font(".SF NS Text", Font.PLAIN, 11));
                    goodRodPanel.add(label72, "cell 2 2,alignx center,growx 0");

                    //---- label73 ----
                    label73.setText("Max Level");
                    label73.setFont(new Font(".SF NS Text", Font.PLAIN, 11));
                    goodRodPanel.add(label73, "cell 3 2,alignx center,growx 0");

                    //---- label74 ----
                    label74.setText("60%");
                    goodRodPanel.add(label74, "cell 0 3,alignx right,growx 0");
                    goodRodPanel.add(goodRod60ComboBox, "cell 1 3,growx");

                    //---- goodRod60MinLevelSpinner ----
                    goodRod60MinLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    goodRodPanel.add(goodRod60MinLevelSpinner, "cell 2 3,alignx center,growx 0");

                    //---- goodRod60MaxLevelSpinner ----
                    goodRod60MaxLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    goodRodPanel.add(goodRod60MaxLevelSpinner, "cell 3 3,alignx center,growx 0");

                    //---- label75 ----
                    label75.setText("30%");
                    goodRodPanel.add(label75, "cell 0 4,alignx right,growx 0");
                    goodRodPanel.add(goodRod30ComboBox, "cell 1 4,growx");

                    //---- goodRod30MinLevelSpinner ----
                    goodRod30MinLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    goodRodPanel.add(goodRod30MinLevelSpinner, "cell 2 4,alignx center,growx 0");

                    //---- goodRod30MaxLevelSpinner ----
                    goodRod30MaxLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    goodRodPanel.add(goodRod30MaxLevelSpinner, "cell 3 4,alignx center,growx 0");

                    //---- label76 ----
                    label76.setText("5%");
                    goodRodPanel.add(label76, "cell 0 5,alignx right,growx 0");

                    //---- goodRod5MaxLevelSpinner ----
                    goodRod5MaxLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    goodRodPanel.add(goodRod5MaxLevelSpinner, "cell 3 5,alignx center,growx 0");
                    goodRodPanel.add(goodRod5ComboBox, "cell 1 5,growx");

                    //---- goodRod5MinLevelSpinner ----
                    goodRod5MinLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    goodRodPanel.add(goodRod5MinLevelSpinner, "cell 2 5,alignx center,growx 0");

                    //---- label77 ----
                    label77.setText("4%");
                    goodRodPanel.add(label77, "cell 0 6,alignx right,growx 0");

                    //---- goodRod4MaxLevelSpinner ----
                    goodRod4MaxLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    goodRodPanel.add(goodRod4MaxLevelSpinner, "cell 3 6,alignx center,growx 0");
                    goodRodPanel.add(goodRod4ComboBox, "cell 1 6,growx");

                    //---- goodRod4MinLevelSpinner ----
                    goodRod4MinLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    goodRodPanel.add(goodRod4MinLevelSpinner, "cell 2 6,alignx center,growx 0");

                    //---- label78 ----
                    label78.setText("1%");
                    goodRodPanel.add(label78, "cell 0 7,alignx right,growx 0");

                    //---- goodRod1MaxLevelSpinner ----
                    goodRod1MaxLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    goodRodPanel.add(goodRod1MaxLevelSpinner, "cell 3 7,alignx center,growx 0");
                    goodRodPanel.add(goodRod1ComboBox, "cell 1 7,growx");

                    //---- goodRod1MinLevelSpinner ----
                    goodRod1MinLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    goodRodPanel.add(goodRod1MinLevelSpinner, "cell 2 7,alignx center,growx 0");
                }
                waterEncounterPanel.add(goodRodPanel, "cell 0 1,aligny top,grow 100 0");

                //======== superRodPanel ========
                {
                    superRodPanel.setBorder(new TitledBorder("Super Rod Encounters"));
                    superRodPanel.setLayout(new MigLayout(
                        "hidemode 3",
                        // columns
                        "[fill]" +
                        "[125:88,grow,fill]" +
                        "[fill]" +
                        "[fill]",
                        // rows
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]"));

                    //---- label79 ----
                    label79.setText("Encounter Rate:");
                    superRodPanel.add(label79, "cell 0 0 3 1");

                    //---- superRodEncounterRateSpinner ----
                    superRodEncounterRateSpinner.setModel(new SpinnerNumberModel(0, 0, 255, 1));
                    superRodPanel.add(superRodEncounterRateSpinner, "cell 3 0");
                    superRodPanel.add(separator12, "cell 0 1 4 1");

                    //---- label80 ----
                    label80.setText("Rate");
                    label80.setFont(new Font(".SF NS Text", Font.PLAIN, 11));
                    superRodPanel.add(label80, "cell 0 2,alignx center,growx 0");

                    //---- label81 ----
                    label81.setText("Species");
                    label81.setFont(new Font(".SF NS Text", Font.PLAIN, 11));
                    superRodPanel.add(label81, "cell 1 2,alignx center,growx 0");

                    //---- label82 ----
                    label82.setText("Min Level");
                    label82.setFont(new Font(".SF NS Text", Font.PLAIN, 11));
                    superRodPanel.add(label82, "cell 2 2,alignx center,growx 0");

                    //---- label83 ----
                    label83.setText("Max Level");
                    label83.setFont(new Font(".SF NS Text", Font.PLAIN, 11));
                    superRodPanel.add(label83, "cell 3 2,alignx center,growx 0");

                    //---- label84 ----
                    label84.setText("60%");
                    superRodPanel.add(label84, "cell 0 3,alignx right,growx 0");
                    superRodPanel.add(superRod60ComboBox, "cell 1 3,growx");

                    //---- superRod60MinLevelSpinner ----
                    superRod60MinLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    superRodPanel.add(superRod60MinLevelSpinner, "cell 2 3,alignx center,growx 0");

                    //---- superRod60MaxLevelSpinner ----
                    superRod60MaxLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    superRodPanel.add(superRod60MaxLevelSpinner, "cell 3 3,alignx center,growx 0");

                    //---- label85 ----
                    label85.setText("30%");
                    superRodPanel.add(label85, "cell 0 4,alignx right,growx 0");
                    superRodPanel.add(superRod30ComboBox, "cell 1 4,growx");

                    //---- superRod30MinLevelSpinner ----
                    superRod30MinLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    superRodPanel.add(superRod30MinLevelSpinner, "cell 2 4,alignx center,growx 0");

                    //---- superRod30MaxLevelSpinner ----
                    superRod30MaxLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    superRodPanel.add(superRod30MaxLevelSpinner, "cell 3 4,alignx center,growx 0");

                    //---- label86 ----
                    label86.setText("5%");
                    superRodPanel.add(label86, "cell 0 5,alignx right,growx 0");

                    //---- superRod5MaxLevelSpinner ----
                    superRod5MaxLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    superRodPanel.add(superRod5MaxLevelSpinner, "cell 3 5,alignx center,growx 0");
                    superRodPanel.add(superRod5ComboBox, "cell 1 5,growx");

                    //---- superRod5MinLevelSpinner ----
                    superRod5MinLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    superRodPanel.add(superRod5MinLevelSpinner, "cell 2 5,alignx center,growx 0");

                    //---- label87 ----
                    label87.setText("4%");
                    superRodPanel.add(label87, "cell 0 6,alignx right,growx 0");

                    //---- superRod4MaxLevelSpinner ----
                    superRod4MaxLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    superRodPanel.add(superRod4MaxLevelSpinner, "cell 3 6,alignx center,growx 0");
                    superRodPanel.add(superRod4ComboBox, "cell 1 6,growx");

                    //---- superRod4MinLevelSpinner ----
                    superRod4MinLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    superRodPanel.add(superRod4MinLevelSpinner, "cell 2 6,alignx center,growx 0");

                    //---- label88 ----
                    label88.setText("1%");
                    superRodPanel.add(label88, "cell 0 7,alignx right,growx 0");

                    //---- superRod1MaxLevelSpinner ----
                    superRod1MaxLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    superRodPanel.add(superRod1MaxLevelSpinner, "cell 3 7,alignx center,growx 0");
                    superRodPanel.add(superRod1ComboBox, "cell 1 7,growx");

                    //---- superRod1MinLevelSpinner ----
                    superRod1MinLevelSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
                    superRodPanel.add(superRod1MinLevelSpinner, "cell 2 7,alignx center,growx 0");
                }
                waterEncounterPanel.add(superRodPanel, "cell 1 1,aligny top,grow 100 0");

                //---- oceanPreviewPanel ----
                oceanPreviewPanel.setBorder(new TitledBorder("text"));
                waterEncounterPanel.add(oceanPreviewPanel, "cell 0 2 2 1,grow");
            }
            tabbedPane1.addTab("Water Encounters", waterEncounterPanel);

            //======== alternateFormDataPanel ========
            {
                alternateFormDataPanel.setLayout(new MigLayout(
                    "hidemode 3",
                    // columns
                    "[fill]" +
                    "[fill]",
                    // rows
                    "[]" +
                    "[]" +
                    "[]"));
            }
            tabbedPane1.addTab("Alternate Form Data", alternateFormDataPanel);
        }
        add(tabbedPane1, "cell 1 5 8 1,grow");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JLabel encounterFileLabel;
    private JLabel speciesSearchLabel;
    private JLabel locationSearchLabel;
    private JComboBox encounterFileComboBox;
    private JButton saveButton;
    private JPanel hSpacer1;
    private JTextField speciesSearchTextField;
    private JButton speciesSearchButton;
    private JPanel hSpacer2;
    private JTextField locationSearchTextField;
    private JButton locationSearchButton;
    private JSeparator separator1;
    private JLabel label4;
    private JLabel label3;
    private JTabbedPane tabbedPane1;
    private JPanel fieldEncounterPanel;
    private JPanel fieldPanel;
    private JLabel label31;
    private JSpinner encounterRateSpinner;
    private JSeparator separator8;
    private JLabel label48;
    private JLabel label47;
    private JLabel label33;
    private JLabel label6;
    private JComboBox<ComboBoxItem> field20ComboBox1;
    private JSpinner field20LevelSpinner1;
    private JLabel label7;
    private JComboBox<ComboBoxItem> field20ComboBox2;
    private JSpinner field20LevelSpinner2;
    private JLabel label8;
    private JComboBox<ComboBoxItem> field10ComboBox1;
    private JSpinner field10LevelSpinner1;
    private JLabel label9;
    private JComboBox<ComboBoxItem> field10ComboBox2;
    private JSpinner field10LevelSpinner2;
    private JLabel label10;
    private JComboBox<ComboBoxItem> field10ComboBox3;
    private JSpinner field10LevelSpinner3;
    private JLabel label11;
    private JComboBox<ComboBoxItem> field10ComboBox4;
    private JSpinner field10LevelSpinner4;
    private JLabel label12;
    private JComboBox<ComboBoxItem> field5ComboBox1;
    private JSpinner field5LevelSpinner1;
    private JLabel label13;
    private JComboBox<ComboBoxItem> field5ComboBox2;
    private JSpinner field5LevelSpinner2;
    private JLabel label14;
    private JComboBox<ComboBoxItem> field4ComboBox1;
    private JSpinner field4LevelSpinner1;
    private JLabel label15;
    private JComboBox<ComboBoxItem> field4ComboBox2;
    private JSpinner field4LevelSpinner2;
    private JLabel label16;
    private JComboBox<ComboBoxItem> field1ComboBox1;
    private JSpinner field1LevelSpinner1;
    private JLabel label17;
    private JComboBox<ComboBoxItem> field1ComboBox2;
    private JSpinner field1LevelSpinner2;
    private JPanel swarmDayNightPanel;
    private JLabel label18;
    private JLabel label19;
    private JComboBox<ComboBoxItem> swarm20ComboBox1;
    private JLabel label20;
    private JLabel label21;
    private JComboBox<ComboBoxItem> swarm20ComboBox2;
    private JSeparator separator2;
    private JLabel label45;
    private JLabel label22;
    private JComboBox<ComboBoxItem> day10ComboBox1;
    private JLabel label23;
    private JComboBox<ComboBoxItem> day10ComboBox2;
    private JSeparator separator3;
    private JLabel label24;
    private JLabel label46;
    private JLabel label25;
    private JComboBox<ComboBoxItem> night10ComboBox1;
    private JLabel label26;
    private JComboBox<ComboBoxItem> night10ComboBox2;
    private JPanel dualSlotPanel;
    private JLabel label5;
    private JLabel label1;
    private JComboBox<ComboBoxItem> rubyComboBox1;
    private JLabel label2;
    private JComboBox<ComboBoxItem> rubyComboBox2;
    private JSeparator separator4;
    private JLabel label36;
    private JLabel label37;
    private JComboBox<ComboBoxItem> sapphireComboBox1;
    private JLabel label38;
    private JComboBox<ComboBoxItem> sapphireComboBox2;
    private JSeparator separator5;
    private JLabel label32;
    private JLabel label39;
    private JComboBox<ComboBoxItem> emeraldComboBox1;
    private JLabel label40;
    private JComboBox<ComboBoxItem> emeraldComboBox2;
    private JSeparator separator6;
    private JLabel label34;
    private JLabel label41;
    private JComboBox<ComboBoxItem> fireRedComboBox1;
    private JLabel label42;
    private JComboBox<ComboBoxItem> fireRedComboBox2;
    private JSeparator separator7;
    private JLabel label35;
    private JLabel label43;
    private JComboBox<ComboBoxItem> leafGreenComboBox1;
    private JLabel label44;
    private JComboBox<ComboBoxItem> leafGreenComboBox2;
    private JPanel pokeRadarPanel;
    private JLabel label27;
    private JComboBox<ComboBoxItem> radar10ComboBox1;
    private JLabel label28;
    private JComboBox<ComboBoxItem> radar10ComboBox2;
    private JLabel label29;
    private JComboBox<ComboBoxItem> radar10ComboBox3;
    private JLabel label30;
    private JComboBox<ComboBoxItem> radar10ComboBox4;
    private JPanel encounterCalculatorPanel;
    private JRadioButton morningRadioButton;
    private JRadioButton dayRadioButton;
    private JRadioButton nightRadioButton;
    private JPanel hSpacer3;
    private JCheckBox swarmCheckbox;
    private JCheckBox radarCheckbox;
    private JCheckBox dualSlotCheckbox;
    private JScrollPane scrollPane1;
    private SinnohEncounterPanel.ModifiedTable encounterSlotTable;
    private JPanel waterEncounterPanel;
    private JPanel surfPanel;
    private JLabel label49;
    private JSpinner surfEncounterRateSpinner;
    private JSeparator separator9;
    private JLabel label58;
    private JLabel label57;
    private JLabel label55;
    private JLabel label56;
    private JLabel label50;
    private JComboBox<ComboBoxItem> surf60ComboBox;
    private JSpinner surf60MinLevelSpinner;
    private JSpinner surf60MaxLevelSpinner;
    private JLabel label51;
    private JComboBox<ComboBoxItem> surf30ComboBox;
    private JSpinner surf30MinLevelSpinner;
    private JSpinner surf30MaxLevelSpinner;
    private JLabel label52;
    private JSpinner surf5MaxLevelSpinner;
    private JComboBox<ComboBoxItem> surf5ComboBox;
    private JSpinner surf5MinLevelSpinner;
    private JLabel label53;
    private JSpinner surf4MaxLevelSpinner;
    private JComboBox<ComboBoxItem> surf4ComboBox;
    private JSpinner surf4MinLevelSpinner;
    private JLabel label54;
    private JSpinner surf1MaxLevelSpinner;
    private JComboBox<ComboBoxItem> surf1ComboBox;
    private JSpinner surf1MinLevelSpinner;
    private JPanel oldRodPanel;
    private JLabel label59;
    private JSpinner oldRodEncounterRateSpinner;
    private JSeparator separator10;
    private JLabel label60;
    private JLabel label61;
    private JLabel label62;
    private JLabel label63;
    private JLabel label64;
    private JComboBox<ComboBoxItem> oldRod60ComboBox;
    private JSpinner oldRod60MinLevelSpinner;
    private JSpinner oldRod60MaxLevelSpinner;
    private JLabel label65;
    private JComboBox<ComboBoxItem> oldRod30ComboBox;
    private JSpinner oldRod30MinLevelSpinner;
    private JSpinner oldRod30MaxLevelSpinner;
    private JLabel label66;
    private JSpinner oldRod5MaxLevelSpinner;
    private JComboBox<ComboBoxItem> oldRod5ComboBox;
    private JSpinner oldRod5MinLevelSpinner;
    private JLabel label67;
    private JSpinner oldRod4MaxLevelSpinner;
    private JComboBox<ComboBoxItem> oldRod4ComboBox;
    private JSpinner oldRod4MinLevelSpinner;
    private JLabel label68;
    private JSpinner oldRod1MaxLevelSpinner;
    private JComboBox<ComboBoxItem> oldRod1ComboBox;
    private JSpinner oldRod1MinLevelSpinner;
    private JPanel goodRodPanel;
    private JLabel label69;
    private JSpinner goodRodEncounterRateSpinner;
    private JSeparator separator11;
    private JLabel label70;
    private JLabel label71;
    private JLabel label72;
    private JLabel label73;
    private JLabel label74;
    private JComboBox<ComboBoxItem> goodRod60ComboBox;
    private JSpinner goodRod60MinLevelSpinner;
    private JSpinner goodRod60MaxLevelSpinner;
    private JLabel label75;
    private JComboBox<ComboBoxItem> goodRod30ComboBox;
    private JSpinner goodRod30MinLevelSpinner;
    private JSpinner goodRod30MaxLevelSpinner;
    private JLabel label76;
    private JSpinner goodRod5MaxLevelSpinner;
    private JComboBox<ComboBoxItem> goodRod5ComboBox;
    private JSpinner goodRod5MinLevelSpinner;
    private JLabel label77;
    private JSpinner goodRod4MaxLevelSpinner;
    private JComboBox<ComboBoxItem> goodRod4ComboBox;
    private JSpinner goodRod4MinLevelSpinner;
    private JLabel label78;
    private JSpinner goodRod1MaxLevelSpinner;
    private JComboBox<ComboBoxItem> goodRod1ComboBox;
    private JSpinner goodRod1MinLevelSpinner;
    private JPanel superRodPanel;
    private JLabel label79;
    private JSpinner superRodEncounterRateSpinner;
    private JSeparator separator12;
    private JLabel label80;
    private JLabel label81;
    private JLabel label82;
    private JLabel label83;
    private JLabel label84;
    private JComboBox<ComboBoxItem> superRod60ComboBox;
    private JSpinner superRod60MinLevelSpinner;
    private JSpinner superRod60MaxLevelSpinner;
    private JLabel label85;
    private JComboBox<ComboBoxItem> superRod30ComboBox;
    private JSpinner superRod30MinLevelSpinner;
    private JSpinner superRod30MaxLevelSpinner;
    private JLabel label86;
    private JSpinner superRod5MaxLevelSpinner;
    private JComboBox<ComboBoxItem> superRod5ComboBox;
    private JSpinner superRod5MinLevelSpinner;
    private JLabel label87;
    private JSpinner superRod4MaxLevelSpinner;
    private JComboBox<ComboBoxItem> superRod4ComboBox;
    private JSpinner superRod4MinLevelSpinner;
    private JLabel label88;
    private JSpinner superRod1MaxLevelSpinner;
    private JComboBox<ComboBoxItem> superRod1ComboBox;
    private JSpinner superRod1MinLevelSpinner;
    private SinnohEncounterPanel.OceanPanel oceanPreviewPanel;
    private JPanel alternateFormDataPanel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public void setProject(Project project, GoogleSheetsAPI api) throws IOException
    {
        this.project= project;
        this.api= api;

        unpackedFolderPath = project.getProjectPath().toString() + File.separator + "temp" + File.separator;

        switch(project.getBaseRom())
        {
            case Diamond:
            case Pearl:
                nameData= TextEditor.getBank(project,362);
                break;

            case Platinum:
                nameData= TextEditor.getBank(project,412);
                break;

            case HeartGold:
            case SoulSilver:
                nameData= TextEditor.getBank(project,237);
                break;
            default:
                throw new RuntimeException("Unexpected value: " + project.getBaseRom());
        }

        TableLocator tableLocator= new TableLocator(project);
        TablePointers.TablePointer pokemonIconPalettePointer;
        boolean success= true;
        try
        {
            pokemonIconPalettePointer = TablePointers.getPointers().get(project.getBaseRomGameCode()).get("pokemonIconPalette");
        }
        catch (Exception e)
        {
            System.err.println("Error loading palette pointer table");
            success= false;
            pokemonIconPalettePointer= null;
            e.printStackTrace();
        }

        if (success)
        {
            paletteGuideTable= tableLocator.obtainTableArr(pokemonIconPalettePointer,nameData.length, 1);
        }

        TablePointers.TablePointer mapHeadersPointer;
        success = true;
        try
        {
            mapHeadersPointer = TablePointers.getPointers().get(project.getBaseRomGameCode()).get("mapHeaders");
        }
        catch (Exception e)
        {
            System.err.println("Error loading map headers table");
            success= false;
            mapHeadersPointer= null;
            e.printStackTrace();
        }

        if (success)
        {

        }

        fillComboBoxes(fieldSlots,nameData);

        fillComboBoxes(swarmSlots,nameData);
        fillComboBoxes(daySlots,nameData);
        fillComboBoxes(nightSlots,nameData);

        fillComboBoxes(radarSlots,nameData);

        fillComboBoxes(rubySlots,nameData);
        fillComboBoxes(sapphireSlots,nameData);
        fillComboBoxes(emeraldSlots,nameData);
        fillComboBoxes(fireRedSlots,nameData);
        fillComboBoxes(leafGreenSlots,nameData);

        fillComboBoxes(surfSlots,nameData);
        fillComboBoxes(oldRodSlots,nameData);
        fillComboBoxes(goodRodSlots,nameData);
        fillComboBoxes(superRodSlots,nameData);

        Object[][] fieldTable;
        Object[][] waterTable;
        Object[][] swarmTable;
        Object[][] radarTable;
        Object[][] dualSlotTable;
        Object[][] formProbabilityTable;


        if(api != null)
        {
            fieldTable= api.getSpecifiedSheetArr("Field Encounters");
            waterTable= api.getSpecifiedSheetArr("Water Encounters");
            swarmTable= api.getSpecifiedSheetArr("Swarm/ Day/ Night Encounters");
            radarTable= api.getSpecifiedSheetArr("Poke Radar Encounters");
            dualSlotTable= api.getSpecifiedSheetArr("Dual-Slot Mode Encounters");
            formProbabilityTable= api.getSpecifiedSheetArr("Alt Form Encounters");

            for(int i= 0; i < fieldTable.length/13; i++)
            {
                encounterFileComboBox.addItem(new ComboBoxItem(i));
            }

            obtainEncounterData(fieldTable,waterTable,swarmTable,radarTable,dualSlotTable,formProbabilityTable);
        }
        else
        {
            setEnabled(false);
        }
    }

    public void setApi(GoogleSheetsAPI api) throws IOException
    {
        this.api= api;

        Object[][] fieldTable;
        Object[][] waterTable;
        Object[][] swarmTable;
        Object[][] radarTable;
        Object[][] dualSlotTable;
        Object[][] formProbabilityTable;

        if(api != null)
        {
            fieldTable= api.getSpecifiedSheetArr("Field Encounters");
            waterTable= api.getSpecifiedSheetArr("Water Encounters");
            swarmTable= api.getSpecifiedSheetArr("Swarm/ Day/ Night Encounters");
            radarTable= api.getSpecifiedSheetArr("Poke Radar Encounters");
            dualSlotTable= api.getSpecifiedSheetArr("Dual-Slot Mode Encounters");
            formProbabilityTable= api.getSpecifiedSheetArr("Alt Form Encounters");

            for(int i= 0; i < fieldTable.length/13; i++)
            {
                encounterFileComboBox.addItem(new ComboBoxItem(i));
            }

            obtainEncounterData(fieldTable,waterTable,swarmTable,radarTable,dualSlotTable,formProbabilityTable);
        }
        else
        {
            setEnabled(false);
        }
    }

    private void obtainEncounterData(Object[][] fieldTable, Object[][] waterTable, Object[][] swarmTable, Object[][] radarTable, Object[][] dualSlotTable, Object[][] formProbabilityTable) throws IOException
    {
        SinnohEncounterEditor encounterEditor= new SinnohEncounterEditor(project,project.getDataPath());
        encounterData= encounterEditor.parseEncounterData(fieldTable,waterTable,swarmTable,radarTable,dualSlotTable,formProbabilityTable);
    }

    private void fillComboBoxes(JComboBox<ComboBoxItem>[] arr, String[] entries)
    {
        for (JComboBox<ComboBoxItem> comboBox : arr)
        {
            ComboBoxSearchable searchable= new ComboBoxSearchable(comboBox);
            comboBox.removeAllItems();
            comboBox.addActionListener(this::selectedSpeciesChangeActionPerformed);
            for (String name : entries)
            {
                comboBox.addItem(new ComboBoxItem(name));
            }
        }
    }

    static class ModifiedTable extends JTable
    {
        public ModifiedTable()
        {
            super();
        }

        @Override
        public Class getColumnClass(int column) {
            return Icon.class;
        }

        @Override
        public void paint(Graphics g)
        {
            g.setColor(Color.BLACK);
            super.paint(g);
        }
    }

    static class OceanPanel extends JPanel
    {
        Image background;

        private BufferedImage[] surfIcons;
        private BufferedImage[] oldRodIcons;
        private BufferedImage[] goodRodIcons;
        private BufferedImage[] superRodIcons;


        public OceanPanel()
        {
            super();
            background= new ImageIcon(OceanPanel.class.getResource("/icons/underwater_background.jpg")).getImage();
        }

        public void setImages(Project project, JComboBox<ComboBoxItem>[] surfSlots,JComboBox<ComboBoxItem>[] oldRodSlots,JComboBox<ComboBoxItem>[] goodRodSlots,JComboBox<ComboBoxItem>[] superRodSlots)
        {
            surfIcons= new BufferedImage[5];
            oldRodIcons= new BufferedImage[5];
            goodRodIcons= new BufferedImage[5];
            superRodIcons= new BufferedImage[5];
            String dataPath= project.getDataPath();

            try
            {
                String narcPath= Project.isDPPT(project) ? File.separator + "poketool" + File.separator + "icongra" + File.separator + (Project.isPlatinum(project) ? "pl_poke_icon.narc" : "poke_icon.narc") : File.separator + "a" + File.separator + "0" + File.separator + "5" + File.separator + "8";
                String folderPath= unpackedFolderPath + "poke_icon";
                new File(folderPath).deleteOnExit();

                if(!new File(folderPath).exists())
                {
                    Narctowl narctowl= new Narctowl(true);
                    narctowl.unpack(dataPath + narcPath,folderPath);
                }

                ImageBase imageBase;
                for(int i= 0; i < surfSlots.length; i++)
                {
                    int species= surfSlots[i].getSelectedIndex();
                    imageBase= new ImageBase(project,folderPath + File.separator + (7 + species) + ".bin", folderPath + File.separator  + "0.bin");
                    BufferedImage base= imageBase.getImageTransparent(imageBase.getPaletteArr()[paletteGuideTable[species]][0],32,32,paletteGuideTable[species]);

                    BufferedImage image= new BufferedImage(32,24,BufferedImage.TYPE_INT_ARGB);
                    Graphics g2= image.getGraphics();
                    g2.drawImage(base.getSubimage(0,8,32,24),0,0,null);
                    surfIcons[i]= image;
                }

                for(int i= 0; i < oldRodSlots.length; i++)
                {
                    int species= oldRodSlots[i].getSelectedIndex();
                    imageBase= new ImageBase(project,folderPath + File.separator + (7 + species) + ".bin", folderPath + File.separator  + "0.bin");
                    BufferedImage base= imageBase.getImageTransparent(imageBase.getPaletteArr()[paletteGuideTable[species]][0],32,32,paletteGuideTable[species]);

                    BufferedImage image= new BufferedImage(32,24,BufferedImage.TYPE_INT_ARGB);
                    Graphics g2= image.getGraphics();
                    g2.drawImage(base.getSubimage(0,8,32,24),0,0,null);
                    oldRodIcons[i]= image;
                }

                for(int i= 0; i < goodRodSlots.length; i++)
                {
                    int species= goodRodSlots[i].getSelectedIndex();
                    imageBase= new ImageBase(project,folderPath + File.separator + (7 + species) + ".bin", folderPath + File.separator  + "0.bin");
                    BufferedImage base= imageBase.getImageTransparent(imageBase.getPaletteArr()[paletteGuideTable[species]][0],32,32,paletteGuideTable[species]);

                    BufferedImage image= new BufferedImage(32,24,BufferedImage.TYPE_INT_ARGB);
                    Graphics g2= image.getGraphics();
                    g2.drawImage(base.getSubimage(0,8,32,24),0,0,null);
                    goodRodIcons[i]= image;
                }

                for(int i= 0; i < superRodSlots.length; i++)
                {
                    int species= superRodSlots[i].getSelectedIndex();
                    imageBase= new ImageBase(project,folderPath + File.separator + (7 + species) + ".bin", folderPath + File.separator  + "0.bin");
                    BufferedImage base= imageBase.getImageTransparent(imageBase.getPaletteArr()[paletteGuideTable[species]][0],32,32,paletteGuideTable[species]);

                    BufferedImage image= new BufferedImage(32,24,BufferedImage.TYPE_INT_ARGB);
                    Graphics g2= image.getGraphics();
                    g2.drawImage(base.getSubimage(0,8,32,24),0,0,null);
                    superRodIcons[i]= image;
                }
            } catch (IOException exception)
            {
                exception.printStackTrace();
            }




            repaint();
        }

        @Override
        public void paint(Graphics g)
        {
            g.drawImage(background,0,0,getWidth(),background.getHeight(null)*2,null);
            g.setColor(Color.RED);
            int iconHeight= surfIcons[0].getHeight()*2;
            int iconWidth= surfIcons[0].getWidth()*2;

            if(surfIcons != null)
            {
                for(int i= 0; i < surfIcons.length; i++)
                {
                    g.drawImage(surfIcons[i],10 + 64*i,10,iconWidth,iconHeight,null);
                }
                g.drawString("SURF",10 + 64*5,10 + iconHeight/2);
            }

            if(oldRodIcons != null)
            {
                for(int i= 0; i < oldRodIcons.length; i++)
                {
                    g.drawImage(oldRodIcons[i],530 + 64*i,40,iconWidth,iconHeight,null);
                }
                g.drawString("OLD ROD",540 - 74,40 + iconHeight/2);
            }

            if(goodRodIcons != null)
            {
                for(int i= 0; i < goodRodIcons.length; i++)
                {
                    g.drawImage(goodRodIcons[i],10 + 64*i,70,iconWidth,iconHeight,null);
                }
                g.drawString("GOOD ROD",10 + 64*5,70 + iconHeight/2);
            }

            if(superRodIcons != null)
            {
                for(int i= 0; i < superRodIcons.length; i++)
                {
                    g.drawImage(superRodIcons[i],540 + 64*i,100,iconWidth,iconHeight,null);
                }
                g.drawString("SUPER ROD",540 - 74,100 + iconHeight/2);
            }
        }
    }

}
