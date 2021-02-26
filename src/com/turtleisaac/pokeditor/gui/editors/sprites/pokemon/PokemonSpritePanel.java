/*
 * Created by JFormDesigner on Tue Feb 02 16:47:18 EST 2021
 */

package com.turtleisaac.pokeditor.gui.editors.sprites.pokemon;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.colorchooser.ColorSelectionModel;

import com.jidesoft.swing.ComboBoxSearchable;
import com.turtleisaac.pokeditor.editors.narctowl.Narctowl;
import com.turtleisaac.pokeditor.editors.positions.SpriteData;
import com.turtleisaac.pokeditor.editors.positions.SpriteDataProcessor;
import com.turtleisaac.pokeditor.editors.text.TextEditor;
import com.turtleisaac.pokeditor.gui.ComboBoxItem;
import com.turtleisaac.pokeditor.project.Game;
import com.turtleisaac.pokeditor.project.Project;
import com.turtleisaac.pokeditor.utilities.images.ImageDecrypter;
import com.turtleisaac.pokeditor.utilities.images.ImageEncrypter;
import com.turtleisaac.pokeditor.utilities.images.PokemonSprites;
import net.miginfocom.swing.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.List;

/**
 * @author turtleisaac
 */
public class PokemonSpritePanel extends JPanel
{
    public ArrayList<File> toDelete;

    private PokemonSprites sprites;
    private SpriteData[] spriteData;

    private Project project;
    private Color[] palette;
    private Color[] shinyPalette;

    private final JButton[] normalPaletteButtons;
    private final JButton[] shinyPaletteButtons;


    public PokemonSpritePanel()
    {
        initComponents();
        normalPaletteButtons = new JButton[] {normalPaletteButton0,normalPaletteButton1,normalPaletteButton2,normalPaletteButton3,normalPaletteButton4,normalPaletteButton5,normalPaletteButton6,normalPaletteButton7,normalPaletteButton8,normalPaletteButton9,normalPaletteButton10,normalPaletteButton11,normalPaletteButton12,normalPaletteButton13,normalPaletteButton14,normalPaletteButton15};
        shinyPaletteButtons = new JButton[] {shinyPaletteButton0,shinyPaletteButton1,shinyPaletteButton2,shinyPaletteButton3,shinyPaletteButton4,shinyPaletteButton5,shinyPaletteButton6,shinyPaletteButton7,shinyPaletteButton8,shinyPaletteButton9,shinyPaletteButton10,shinyPaletteButton11,shinyPaletteButton12,shinyPaletteButton13,shinyPaletteButton14,shinyPaletteButton15};
        toDelete= new ArrayList<>();
        mockupPanel.setVisible(true);

        ComboBoxSearchable speciesSearchable= new ComboBoxSearchable(speciesChooserComboBox);
        speciesChooserComboBox.setMaximumRowCount(5);


        normalMaleFrontButton.setTransferHandler(new TransferHandler("icon"));
        normalMaleFrontButton.setDropTarget(new DropTarget() {
            public synchronized void drop(DropTargetDropEvent event) {
                try
                {
                    event.acceptDrop(DnDConstants.ACTION_COPY);
                    List<File> droppedFiles = (List<File>) event.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);

                    BufferedImage image= ImageIO.read(droppedFiles.get(0));
//                    image.
                }
                catch (IOException | UnsupportedFlavorException exception)
                {
                    exception.printStackTrace();
                }
            }
        });
    }

    private void normalPaletteButton0ActionPerformed(ActionEvent e) {
        paletteButtonAction(0,false);
    }

    private void normalPaletteButton1ActionPerformed(ActionEvent e) {
        paletteButtonAction(1,false);
    }

    private void normalPaletteButton2ActionPerformed(ActionEvent e) {
        paletteButtonAction(2,false);
    }

    private void normalPaletteButton3ActionPerformed(ActionEvent e) {
        paletteButtonAction(3,false);
    }

    private void normalPaletteButton4ActionPerformed(ActionEvent e) {
        paletteButtonAction(4,false);
    }

    private void normalPaletteButton5ActionPerformed(ActionEvent e) {
        paletteButtonAction(5,false);
    }

    private void normalPaletteButton6ActionPerformed(ActionEvent e) {
        paletteButtonAction(6,false);
    }

    private void normalPaletteButton7ActionPerformed(ActionEvent e) {
        paletteButtonAction(7,false);
    }

    private void normalPaletteButton8ActionPerformed(ActionEvent e) {
        paletteButtonAction(8,false);
    }

    private void normalPaletteButton9ActionPerformed(ActionEvent e) {
        paletteButtonAction(9,false);
    }

    private void normalPaletteButton10ActionPerformed(ActionEvent e) {
        paletteButtonAction(10,false);
    }

    private void normalPaletteButton11ActionPerformed(ActionEvent e) {
        paletteButtonAction(11,false);
    }

    private void normalPaletteButton12ActionPerformed(ActionEvent e) {
        paletteButtonAction(12,false);
    }

    private void normalPaletteButton13ActionPerformed(ActionEvent e) {
        paletteButtonAction(13,false);
    }

    private void normalPaletteButton14ActionPerformed(ActionEvent e) {
        paletteButtonAction(14,false);
    }

    private void normalPaletteButton15ActionPerformed(ActionEvent e) {
        paletteButtonAction(15,false);
    }

    private void shinyPaletteButton0ActionPerformed(ActionEvent e) {
        paletteButtonAction(0,true);
    }

    private void shinyPaletteButton1ActionPerformed(ActionEvent e) {
        paletteButtonAction(1,true);
    }

    private void shinyPaletteButton2ActionPerformed(ActionEvent e) {
        paletteButtonAction(2,true);
    }

    private void shinyPaletteButton3ActionPerformed(ActionEvent e) {
        paletteButtonAction(3,true);
    }

    private void shinyPaletteButton4ActionPerformed(ActionEvent e) {
        paletteButtonAction(4,true);
    }

    private void shinyPaletteButton5ActionPerformed(ActionEvent e) {
        paletteButtonAction(5,true);
    }

    private void shinyPaletteButton6ActionPerformed(ActionEvent e) {
        paletteButtonAction(6,true);
    }

    private void shinyPaletteButton7ActionPerformed(ActionEvent e) {
        paletteButtonAction(7,true);
    }

    private void shinyPaletteButton8ActionPerformed(ActionEvent e) {
        paletteButtonAction(8,true);
    }

    private void shinyPaletteButton9ActionPerformed(ActionEvent e) {
        paletteButtonAction(9,true);
    }

    private void shinyPaletteButton10ActionPerformed(ActionEvent e) {
        paletteButtonAction(10,true);
    }

    private void shinyPaletteButton11ActionPerformed(ActionEvent e) {
        paletteButtonAction(11,true);
    }

    private void shinyPaletteButton12ActionPerformed(ActionEvent e) {
        paletteButtonAction(12,true);
    }

    private void shinyPaletteButton13ActionPerformed(ActionEvent e) {
        paletteButtonAction(13,true);
    }

    private void shinyPaletteButton14ActionPerformed(ActionEvent e) {
        paletteButtonAction(14,true);
    }

    private void shinyPaletteButton15ActionPerformed(ActionEvent e) {
        paletteButtonAction(15,true);
    }
    
    private void paletteButtonAction(int num, boolean shiny)
    {
        int frameNum= frameToggleButton.isSelected() ? 1 : 0;

        Color original= shiny ? shinyPalette[num] : palette[num];
        JColorChooser colorChooser= new JColorChooser(original);


        PreviewPanel previewPanel= new PreviewPanel(colorChooser,
                resizeImage(shiny ? sprites.getShinyMaleFront()[frameNum] : sprites.getMaleFront()[frameNum],160,160),
                resizeImage(shiny ? sprites.getShinyMaleBack()[frameNum] : sprites.getMaleBack()[frameNum],160,160),
                resizeImage(shiny ? sprites.getShinyFemaleFront()[frameNum] : sprites.getFemaleFront()[frameNum],160,160),
                resizeImage(shiny ? sprites.getShinyFemaleBack()[frameNum] : sprites.getFemaleBack()[frameNum],160,160));

        final Color[] toApply = new Color[1];
        ColorSelectionModel model = colorChooser.getSelectionModel();
        model.addChangeListener(e ->
        {
            ColorSelectionModel model1 = (ColorSelectionModel) e.getSource();
            Color newColor= model1.getSelectedColor();
            int r= newColor.getRed() - newColor.getRed() % 8;
            r= Math.min(r,248);
            int g= newColor.getGreen() - newColor.getGreen()%8;
            g= Math.min(g,248);
            int b= newColor.getBlue() - newColor.getBlue()%8;
            b= Math.min(b,248);
            newColor= new Color(r,g,b);

            for(int row= 0; row < previewPanel.maleFront.getHeight(); row++)
            {
                for(int col= 0; col < previewPanel.maleFront.getWidth(); col++)
                {
                    if(previewPanel.maleFrontMatches[row][col])
                        previewPanel.maleFront.setRGB(col,row,newColor.getRGB());
                    if(previewPanel.maleBackMatches[row][col])
                        previewPanel.maleBack.setRGB(col,row,newColor.getRGB());
                    if(previewPanel.femaleFrontMatches[row][col])
                        previewPanel.femaleFront.setRGB(col,row,newColor.getRGB());
                    if(previewPanel.femaleBackMatches[row][col])
                        previewPanel.femaleBack.setRGB(col,row,newColor.getRGB());
                }
            }
            previewPanel.currentColor= newColor;

            colorChooser.setColor(newColor);
            toApply[0]= newColor;
        });

        colorChooser.setPreviewPanel(previewPanel);
        AbstractColorChooserPanel[] chooserPanels= colorChooser.getChooserPanels();
        colorChooser.setChooserPanels(new AbstractColorChooserPanel[] {chooserPanels[3], chooserPanels[0]});
        colorChooser.setPreferredSize(new Dimension(700,480));


        final BufferedImage[][] shinyFemaleBack = {new BufferedImage[2]};
        final BufferedImage[][] shinyFemaleFront = {new BufferedImage[2]};

        final BufferedImage[][] shinyMaleBack = {new BufferedImage[2]};
        final BufferedImage[][] shinyMaleFront = {new BufferedImage[2]};

        final BufferedImage[][] femaleBack = {new BufferedImage[2]};
        final BufferedImage[][] femaleFront = {new BufferedImage[2]};

        final BufferedImage[][] maleBack = {new BufferedImage[2]};
        final BufferedImage[][] maleFront = {new BufferedImage[2]};

        JDialog dialog= JColorChooser.createDialog(this, "Species " + speciesChooserComboBox.getSelectedIndex() + " - " + (shiny ? "Shiny" : "Normal") + " Palette - Color " + num, true, colorChooser,
                e -> //OK
                {
                    Color finalColor= colorChooser.getColor();
                    System.out.println("Original color: " + original.toString());
                    System.out.println("Final color: " + finalColor.toString());

                    if(shiny)
                    {
                        shinyPaletteButtons[num].setIcon(new ImageIcon(getPaletteImage(finalColor)));
                        shinyPalette[num]= finalColor;


                        shinyFemaleBack[0] = new BufferedImage[] {updateColor(sprites.getShinyFemaleBack()[0],original,finalColor),updateColor(sprites.getShinyFemaleBack()[1],original,finalColor)};
                        shinyFemaleFront[0] = new BufferedImage[] {updateColor(sprites.getShinyFemaleFront()[0],original,finalColor),updateColor(sprites.getShinyFemaleFront()[1],original,finalColor)};

                        shinyMaleBack[0]= new BufferedImage[] {updateColor(sprites.getShinyMaleBack()[0],original,finalColor),updateColor(sprites.getShinyMaleBack()[1],original,finalColor)};
                        shinyMaleFront[0]= new BufferedImage[] {updateColor(sprites.getShinyMaleFront()[0],original,finalColor),updateColor(sprites.getShinyMaleFront()[1],original,finalColor)};


                        femaleBack[0]= Arrays.copyOf(sprites.getFemaleBack(),2);
                        femaleFront[0]= Arrays.copyOf(sprites.getFemaleFront(),2);
                        maleBack[0]= Arrays.copyOf(sprites.getMaleBack(),2);
                        maleFront[0]= Arrays.copyOf(sprites.getMaleFront(),2);

                    }
                    else
                    {
                        normalPaletteButtons[num].setIcon(new ImageIcon(getPaletteImage(finalColor)));
                        palette[num]= finalColor;

                        femaleBack[0]= new BufferedImage[] {updateColor(sprites.getFemaleBack()[0],original,finalColor),updateColor(sprites.getFemaleBack()[1],original,finalColor)};
                        femaleFront[0]= new BufferedImage[] {updateColor(sprites.getFemaleFront()[0],original,finalColor),updateColor(sprites.getFemaleFront()[1],original,finalColor)};

                        maleBack[0]= new BufferedImage[] {updateColor(sprites.getMaleBack()[0],original,finalColor),updateColor(sprites.getMaleBack()[1],original,finalColor)};
                        maleFront[0]= new BufferedImage[] {updateColor(sprites.getMaleFront()[0],original,finalColor),updateColor(sprites.getMaleFront()[1],original,finalColor)};


                        shinyFemaleBack[0]= Arrays.copyOf(sprites.getShinyFemaleBack(),2);
                        shinyFemaleFront[0]= Arrays.copyOf(sprites.getShinyFemaleFront(),2);
                        shinyMaleBack[0]= Arrays.copyOf(sprites.getShinyMaleBack(),2);
                        shinyMaleFront[0]= Arrays.copyOf(sprites.getShinyMaleFront(),2);
                    }

                    sprites= new PokemonSprites()
                    {
                        @Override
                        public BufferedImage[] getFemaleBack()
                        {
                            return femaleBack[0];
                        }

                        @Override
                        public BufferedImage[] getShinyFemaleBack()
                        {
                            return shinyFemaleBack[0];
                        }

                        @Override
                        public BufferedImage[] getMaleBack()
                        {
                            return maleBack[0];
                        }

                        @Override
                        public BufferedImage[] getShinyMaleBack()
                        {
                            return shinyMaleBack[0];
                        }

                        @Override
                        public BufferedImage[] getFemaleFront()
                        {
                            return femaleFront[0];
                        }

                        @Override
                        public BufferedImage[] getShinyFemaleFront()
                        {
                            return shinyFemaleFront[0];
                        }

                        @Override
                        public BufferedImage[] getMaleFront()
                        {
                            return maleFront[0];
                        }

                        @Override
                        public BufferedImage[] getShinyMaleFront()
                        {
                            return shinyMaleFront[0];
                        }

                        @Override
                        public Color[] getPalette()
                        {
                            return palette;
                        }

                        @Override
                        public Color[] getShinyPalette()
                        {
                            return shinyPalette;
                        }
                    };

                    normalFemaleFrontButton.setIcon(new ImageIcon(sprites.getFemaleFront()[0]));
                    normalFemaleBackButton.setIcon(new ImageIcon(sprites.getFemaleBack()[0]));
                    shinyFemaleFrontButton.setIcon(new ImageIcon(sprites.getShinyFemaleFront()[0]));
                    shinyFemaleBackButton.setIcon(new ImageIcon(sprites.getShinyFemaleBack()[0]));

                    normalMaleFrontButton.setIcon(new ImageIcon(sprites.getMaleFront()[0]));
                    normalMaleBackButton.setIcon(new ImageIcon(sprites.getMaleBack()[0]));
                    shinyMaleFrontButton.setIcon(new ImageIcon(sprites.getShinyMaleFront()[0]));
                    shinyMaleBackButton.setIcon(new ImageIcon(sprites.getShinyMaleBack()[0]));


                    for(int i= 0; i < normalPaletteButtons.length; i++)
                    {
                        normalPaletteButtons[i].setIcon(new ImageIcon(getPaletteImage(palette[i])));
                        shinyPaletteButtons[i].setIcon(new ImageIcon(getPaletteImage(shinyPalette[i])));
                    }

                    byte frontModifier;
                    byte backModifier;
                    if(femaleToggleButton.isSelected())
                    {
                        frontModifier= SpriteDataProcessor.getHeightModifier(project,speciesChooserComboBox.getSelectedIndex(), SpriteDataProcessor.SpriteType.Female_Front);
                        backModifier= SpriteDataProcessor.getHeightModifier(project,speciesChooserComboBox.getSelectedIndex(), SpriteDataProcessor.SpriteType.Female_Back);
                    }
                    else
                    {
                        frontModifier= SpriteDataProcessor.getHeightModifier(project, speciesChooserComboBox.getSelectedIndex(), SpriteDataProcessor.SpriteType.Male_Front);
                        backModifier= SpriteDataProcessor.getHeightModifier(project, speciesChooserComboBox.getSelectedIndex(), SpriteDataProcessor.SpriteType.Male_Back);
                    }

                    int frameValue= frameToggleButton.isSelected() ? 1 : 0;
                    int femaleValue= femaleToggleButton.isSelected() ? 1 : 0;
                    System.out.println(speciesChooserComboBox.getSelectedItem());
                    mockupPanel.setSprites(femaleValue == 1 ? sprites.getFemaleFront()[frameValue] : sprites.getMaleFront()[frameValue], femaleValue == 1 ? sprites.getFemaleBack()[frameValue] : sprites.getMaleBack()[frameValue],palette[0],spriteData[speciesChooserComboBox.getSelectedIndex()],frontModifier,backModifier);
                },

                e -> //Cancel
                {
                    System.out.println("Not applying colors");
                });

        dialog.setVisible(true);
    }

    private void saveChangesButtonActionPerformed(ActionEvent e)
    {
        BufferedImage[] femaleBack= Arrays.copyOf(sprites.getFemaleBack(),2);
        BufferedImage[] shinyFemaleBack= Arrays.copyOf(sprites.getShinyFemaleBack(),2);
        BufferedImage[] femaleFront= Arrays.copyOf(sprites.getFemaleFront(),2);
        BufferedImage[] shinyFemaleFront= Arrays.copyOf(sprites.getShinyFemaleFront(),2);

        BufferedImage[] maleBack= Arrays.copyOf(sprites.getMaleBack(),2);
        BufferedImage[] shinyMaleBack= Arrays.copyOf(sprites.getShinyMaleBack(),2);
        BufferedImage[] maleFront= Arrays.copyOf(sprites.getMaleFront(),2);
        BufferedImage[] shinyMaleFront= Arrays.copyOf(sprites.getShinyMaleFront(),2);

        palette= Arrays.copyOf(sprites.getPalette(),16);
        shinyPalette= Arrays.copyOf(sprites.getShinyPalette(),16);

        String dataPath= project.getDataPath();
        int selected= speciesChooserComboBox.getSelectedIndex()*6;

        boolean primary= Project.isPrimary(project);

        File narcPath= new File(dataPath + (Project.isDPPT(project) ? project.getBaseRom() == Game.Platinum ? File.separator + "poketool" + File.separator + "pokegra" + File.separator + "pl_pokegra.narc" : File.separator + "poketool" + File.separator + "pokegra" + File.separator + "pokegra.narc" : "/a" + File.separator + "0" + File.separator + "0" + File.separator + "4"));
        File pokegraPath= new File(dataPath + (Project.isDPPT(project) ? project.getBaseRom() == Game.Platinum ? File.separator + "poketool" + File.separator + "pokegra" + File.separator + "pl_pokegra" : File.separator + "poketool" + File.separator + "pokegra" + File.separator + "pokegra" : "/a" + File.separator + "0" + File.separator + "0" + File.separator + "4_"));

        try
        {
            ImageEncrypter.saveSprites(pokegraPath.getAbsolutePath(), sprites,selected,primary);
        }
        catch (IOException exception)
        {
            System.err.println("Sprite Save Error");
            exception.printStackTrace();
            return;
        }

        try
        {
            Narctowl narctowl = new Narctowl(true);
            narctowl.pack(pokegraPath.getAbsolutePath(),"",narcPath.getAbsolutePath());
        }
        catch (IOException ignored)
        {
        }
    }

    private void revertChangesButtonActionPerformed(ActionEvent e)
    {
        speciesChooserComboBoxActionPerformed(e);
    }

    private void normalMaleFrontButtonActionPerformed(ActionEvent e)
    {
        JOptionPane.showOptionDialog(this,"Choose an option","PokEditor",JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE,null,new String[] {"Import","Export"},0);
    }

    private void normalMaleBackButtonActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void shinyMaleFrontButtonActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void shinyMaleBackButtonActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void normalBackButtonActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void shinyFrontButtonActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void shinyBackButtonActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void normalFrontButtonActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void speciesChooserComboBoxActionPerformed(ActionEvent e)
    {
        frameToggleButton.setSelected(false);
        String dataPath= project.getDataPath();
        int selected= speciesChooserComboBox.getSelectedIndex()*6;

        boolean primary= false;

        File pokegraNarcPath= new File(dataPath + (Project.isDPPT(project) ? project.getBaseRom() == Game.Platinum ? File.separator + "poketool" + File.separator + "pokegra" + File.separator + "pl_pokegra.narc" : File.separator + "poketool" + File.separator + "pokegra" + File.separator + "pokegra.narc" :  File.separator + "a" + File.separator + "0" + File.separator + "0" + File.separator + "4"));
        File pokegraDirPath= new File(dataPath + (Project.isDPPT(project) ? project.getBaseRom() == Game.Platinum ? File.separator + "poketool" + File.separator + "pokegra" + File.separator + "pl_pokegra" : File.separator + "poketool" + File.separator + "pokegra" + File.separator + "pokegra" : File.separator + "a" + File.separator + "0" + File.separator + "0" + File.separator + "4_"));

        primary= Project.isPrimary(project);

        try
        {
            Narctowl narctowl = new Narctowl(true);
            if(!pokegraDirPath.exists())
            {
                narctowl.unpack(pokegraNarcPath.getAbsolutePath(), pokegraDirPath.getAbsolutePath());
            }
            pokegraDirPath.deleteOnExit();

            spriteData = SpriteDataProcessor.getPositionData(project);
        }
        catch (IOException ignored)
        {
        }

        toDelete.add(pokegraDirPath);
        sprites= ImageDecrypter.getSprites(pokegraDirPath.getAbsolutePath(),selected,primary);

        palette= Arrays.copyOf(sprites.getPalette(),16);
        shinyPalette= Arrays.copyOf(sprites.getShinyPalette(),16);


        normalFemaleFrontButton.setIcon(new ImageIcon(sprites.getFemaleFront()[0]));
        normalFemaleBackButton.setIcon(new ImageIcon(sprites.getFemaleBack()[0]));
        shinyFemaleFrontButton.setIcon(new ImageIcon(sprites.getShinyFemaleFront()[0]));
        shinyFemaleBackButton.setIcon(new ImageIcon(sprites.getShinyFemaleBack()[0]));

        normalMaleFrontButton.setIcon(new ImageIcon(sprites.getMaleFront()[0]));
        normalMaleBackButton.setIcon(new ImageIcon(sprites.getMaleBack()[0]));
        shinyMaleFrontButton.setIcon(new ImageIcon(sprites.getShinyMaleFront()[0]));
        shinyMaleBackButton.setIcon(new ImageIcon(sprites.getShinyMaleBack()[0]));


        byte frontModifier;
        byte backModifier;
        if(femaleToggleButton.isSelected())
        {
            frontModifier= SpriteDataProcessor.getHeightModifier(project,speciesChooserComboBox.getSelectedIndex(), SpriteDataProcessor.SpriteType.Female_Front);
            backModifier= SpriteDataProcessor.getHeightModifier(project,speciesChooserComboBox.getSelectedIndex(), SpriteDataProcessor.SpriteType.Female_Back);
        }
        else
        {
            frontModifier= SpriteDataProcessor.getHeightModifier(project, speciesChooserComboBox.getSelectedIndex(), SpriteDataProcessor.SpriteType.Male_Front);
            backModifier= SpriteDataProcessor.getHeightModifier(project, speciesChooserComboBox.getSelectedIndex(), SpriteDataProcessor.SpriteType.Male_Back);
        }

        int frameValue= frameToggleButton.isSelected() ? 1 : 0;
        int femaleValue= femaleToggleButton.isSelected() ? 1 : 0;
        System.out.println(speciesChooserComboBox.getSelectedItem());
        mockupPanel.setSprites(femaleValue == 1 ? sprites.getFemaleFront()[frameValue] : sprites.getMaleFront()[frameValue], femaleValue == 1 ? sprites.getFemaleBack()[frameValue] : sprites.getMaleBack()[frameValue],palette[0],spriteData[speciesChooserComboBox.getSelectedIndex()],frontModifier,backModifier);

        for(int i= 0; i < normalPaletteButtons.length; i++)
        {
            normalPaletteButtons[i].setIcon(new ImageIcon(getPaletteImage(palette[i])));
            shinyPaletteButtons[i].setIcon(new ImageIcon(getPaletteImage(shinyPalette[i])));
        }

        //        ImageIO.write(image,"png",new File(System.getProperty("user.dir") + File.separator + "cynthia.png"));

    }

    private void frameToggleButtonActionPerformed(ActionEvent e)
    {
        int frameValue= frameToggleButton.isSelected() ? 1 : 0;
        frameToggleLabel.setText("" + frameValue);


        normalFemaleFrontButton.setIcon(new ImageIcon(sprites.getFemaleFront()[frameValue]));
        normalFemaleBackButton.setIcon(new ImageIcon(sprites.getFemaleBack()[frameValue]));
        shinyFemaleFrontButton.setIcon(new ImageIcon(sprites.getShinyFemaleFront()[frameValue]));
        shinyFemaleBackButton.setIcon(new ImageIcon(sprites.getShinyFemaleBack()[frameValue]));

        normalMaleFrontButton.setIcon(new ImageIcon(sprites.getMaleFront()[frameValue]));
        normalMaleBackButton.setIcon(new ImageIcon(sprites.getMaleBack()[frameValue]));
        shinyMaleFrontButton.setIcon(new ImageIcon(sprites.getShinyMaleFront()[frameValue]));
        shinyMaleBackButton.setIcon(new ImageIcon(sprites.getShinyMaleBack()[frameValue]));

        byte frontModifier;
        byte backModifier;

        if(femaleToggleButton.isSelected())
        {
            frontModifier= SpriteDataProcessor.getHeightModifier(project,speciesChooserComboBox.getSelectedIndex(), SpriteDataProcessor.SpriteType.Female_Front);
            backModifier= SpriteDataProcessor.getHeightModifier(project,speciesChooserComboBox.getSelectedIndex(), SpriteDataProcessor.SpriteType.Female_Back);
        }
        else
        {
            frontModifier= SpriteDataProcessor.getHeightModifier(project, speciesChooserComboBox.getSelectedIndex(), SpriteDataProcessor.SpriteType.Male_Front);
            backModifier= SpriteDataProcessor.getHeightModifier(project, speciesChooserComboBox.getSelectedIndex(), SpriteDataProcessor.SpriteType.Male_Back);
        }

        int femaleValue= femaleToggleButton.isSelected() ? 1 : 0;
        System.out.println(speciesChooserComboBox.getSelectedItem());
        mockupPanel.setSprites(femaleValue == 1 ? sprites.getFemaleFront()[frameValue] : sprites.getMaleFront()[frameValue], femaleValue == 1 ? sprites.getFemaleBack()[frameValue] : sprites.getMaleBack()[frameValue],palette[0],spriteData[speciesChooserComboBox.getSelectedIndex()],frontModifier,backModifier);
    }

    private void importSpriteButtonActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void femaleToggleButtonActionPerformed(ActionEvent e)
    {
        int frameValue= frameToggleButton.isSelected() ? 1 : 0;

        byte frontModifier;
        byte backModifier;
        if(femaleToggleButton.isSelected())
        {
            frontModifier= SpriteDataProcessor.getHeightModifier(project,speciesChooserComboBox.getSelectedIndex(), SpriteDataProcessor.SpriteType.Female_Front);
            backModifier= SpriteDataProcessor.getHeightModifier(project,speciesChooserComboBox.getSelectedIndex(), SpriteDataProcessor.SpriteType.Female_Back);
        }
        else
        {
            frontModifier= SpriteDataProcessor.getHeightModifier(project, speciesChooserComboBox.getSelectedIndex(), SpriteDataProcessor.SpriteType.Male_Front);
            backModifier= SpriteDataProcessor.getHeightModifier(project, speciesChooserComboBox.getSelectedIndex(), SpriteDataProcessor.SpriteType.Male_Back);
        }

        int femaleValue= femaleToggleButton.isSelected() ? 1 : 0;
        System.out.println(speciesChooserComboBox.getSelectedItem());
        mockupPanel.setSprites(femaleValue == 1 ? sprites.getFemaleFront()[frameValue] : sprites.getMaleFront()[frameValue], femaleValue == 1 ? sprites.getFemaleBack()[frameValue] : sprites.getMaleBack()[frameValue],palette[0],spriteData[speciesChooserComboBox.getSelectedIndex()],frontModifier,backModifier);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        speciesChooserComboBox = new JComboBox();
        saveChangesButton = new JButton();
        revertChangesButton = new JButton();
        battleSpritePanel = new JPanel();
        importSpriteButton = new JButton();
        frameToggleLabel = new JLabel();
        frameToggleButton = new JToggleButton();
        normalLabel = new JLabel();
        shinyLabel = new JLabel();
        maleLabel = new JLabel();
        normalMaleFrontButton = new JButton();
        normalMaleBackButton = new JButton();
        shinyMaleFrontButton = new JButton();
        shinyMaleBackButton = new JButton();
        femaleLabel = new JLabel();
        normalFemaleFrontButton = new JButton();
        normalFemaleBackButton = new JButton();
        shinyFemaleFrontButton = new JButton();
        shinyFemaleBackButton = new JButton();
        normalFrontLabel = new JLabel();
        normalBackLabel = new JLabel();
        shinyFrontLabel = new JLabel();
        shinyBackLabel = new JLabel();
        palettePanel = new JPanel();
        label5 = new JLabel();
        label6 = new JLabel();
        normalPaletteButton0 = new JButton();
        normalPaletteButton1 = new JButton();
        normalPaletteButton2 = new JButton();
        normalPaletteButton3 = new JButton();
        normalPaletteButton4 = new JButton();
        normalPaletteButton5 = new JButton();
        normalPaletteButton6 = new JButton();
        normalPaletteButton7 = new JButton();
        normalPaletteButton8 = new JButton();
        normalPaletteButton9 = new JButton();
        normalPaletteButton10 = new JButton();
        normalPaletteButton11 = new JButton();
        normalPaletteButton12 = new JButton();
        normalPaletteButton13 = new JButton();
        normalPaletteButton14 = new JButton();
        normalPaletteButton15 = new JButton();
        hSpacer1 = new JPanel(null);
        separator1 = new JSeparator();
        hSpacer2 = new JPanel(null);
        shinyPaletteButton0 = new JButton();
        shinyPaletteButton1 = new JButton();
        shinyPaletteButton2 = new JButton();
        shinyPaletteButton3 = new JButton();
        shinyPaletteButton4 = new JButton();
        shinyPaletteButton5 = new JButton();
        shinyPaletteButton6 = new JButton();
        shinyPaletteButton7 = new JButton();
        shinyPaletteButton8 = new JButton();
        shinyPaletteButton9 = new JButton();
        shinyPaletteButton10 = new JButton();
        shinyPaletteButton11 = new JButton();
        shinyPaletteButton12 = new JButton();
        shinyPaletteButton13 = new JButton();
        shinyPaletteButton14 = new JButton();
        shinyPaletteButton15 = new JButton();
        battlePanel = new JPanel();
        femaleToggleButton = new JToggleButton();
        mockupPanel = new PokemonSpritePanel.BattleMockupPanel();

        //======== this ========
        setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[fill]" +
            "[fill]" +
            "[92,fill]",
            // rows
            "[]" +
            "[]" +
            "[]" +
            "[]"));

        //---- speciesChooserComboBox ----
        speciesChooserComboBox.setAutoscrolls(true);
        speciesChooserComboBox.addActionListener(e -> speciesChooserComboBoxActionPerformed(e));
        add(speciesChooserComboBox, "cell 0 0");

        //---- saveChangesButton ----
        saveChangesButton.setText("Save");
        saveChangesButton.addActionListener(e -> saveChangesButtonActionPerformed(e));
        add(saveChangesButton, "cell 1 0");

        //---- revertChangesButton ----
        revertChangesButton.setText("Revert");
        revertChangesButton.addActionListener(e -> revertChangesButtonActionPerformed(e));
        add(revertChangesButton, "cell 2 0");

        //======== battleSpritePanel ========
        {
            battleSpritePanel.setBorder(new TitledBorder(""));
            battleSpritePanel.setLayout(new MigLayout(
                "hidemode 3",
                // columns
                "[fill]" +
                "[grow,fill]" +
                "[grow,fill]" +
                "[grow,fill]" +
                "[grow,fill]",
                // rows
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]"));

            //---- importSpriteButton ----
            importSpriteButton.setText("Import");
            importSpriteButton.addActionListener(e -> importSpriteButtonActionPerformed(e));
            battleSpritePanel.add(importSpriteButton, "cell 1 0 4 1");

            //---- frameToggleLabel ----
            frameToggleLabel.setText("0");
            battleSpritePanel.add(frameToggleLabel, "cell 0 1,alignx right,growx 0");

            //---- frameToggleButton ----
            frameToggleButton.setText("Toggle Frame");
            frameToggleButton.addActionListener(e -> frameToggleButtonActionPerformed(e));
            battleSpritePanel.add(frameToggleButton, "cell 1 1 4 1");

            //---- normalLabel ----
            normalLabel.setText("Normal");
            battleSpritePanel.add(normalLabel, "cell 1 2 2 1,alignx center,growx 0");

            //---- shinyLabel ----
            shinyLabel.setText("Shiny");
            battleSpritePanel.add(shinyLabel, "cell 3 2 2 1,alignx center,growx 0");

            //---- maleLabel ----
            maleLabel.setText("Male");
            battleSpritePanel.add(maleLabel, "cell 0 3,alignx center,growx 0");

            //---- normalMaleFrontButton ----
            normalMaleFrontButton.setToolTipText("Male Front Sprite");
            normalMaleFrontButton.addActionListener(e -> normalFrontButtonActionPerformed(e));
            battleSpritePanel.add(normalMaleFrontButton, "cell 1 3,alignx center,growx 0");

            //---- normalMaleBackButton ----
            normalMaleBackButton.setToolTipText("Male Back Sprite");
            normalMaleBackButton.addActionListener(e -> normalBackButtonActionPerformed(e));
            battleSpritePanel.add(normalMaleBackButton, "cell 2 3,alignx center,growx 0");

            //---- shinyMaleFrontButton ----
            shinyMaleFrontButton.setToolTipText("Shiny Male Front Sprite");
            shinyMaleFrontButton.addActionListener(e -> shinyFrontButtonActionPerformed(e));
            battleSpritePanel.add(shinyMaleFrontButton, "cell 3 3,alignx center,growx 0");

            //---- shinyMaleBackButton ----
            shinyMaleBackButton.setToolTipText("Shiny Male Back Sprite");
            shinyMaleBackButton.addActionListener(e -> shinyBackButtonActionPerformed(e));
            battleSpritePanel.add(shinyMaleBackButton, "cell 4 3,alignx center,growx 0");

            //---- femaleLabel ----
            femaleLabel.setText("Female");
            battleSpritePanel.add(femaleLabel, "cell 0 4,alignx center,growx 0");

            //---- normalFemaleFrontButton ----
            normalFemaleFrontButton.setToolTipText("Female Front Sprite");
            battleSpritePanel.add(normalFemaleFrontButton, "cell 1 4,alignx center,growx 0");

            //---- normalFemaleBackButton ----
            normalFemaleBackButton.setToolTipText("Female Back Sprite");
            battleSpritePanel.add(normalFemaleBackButton, "cell 2 4,alignx center,growx 0");

            //---- shinyFemaleFrontButton ----
            shinyFemaleFrontButton.setToolTipText("Shiny Female Front Sprite");
            battleSpritePanel.add(shinyFemaleFrontButton, "cell 3 4,alignx center,growx 0");

            //---- shinyFemaleBackButton ----
            shinyFemaleBackButton.setToolTipText("Shiny Female Back Sprite");
            battleSpritePanel.add(shinyFemaleBackButton, "cell 4 4,alignx center,growx 0");

            //---- normalFrontLabel ----
            normalFrontLabel.setText("Front");
            battleSpritePanel.add(normalFrontLabel, "cell 1 5,alignx center,growx 0");

            //---- normalBackLabel ----
            normalBackLabel.setText("Back");
            battleSpritePanel.add(normalBackLabel, "cell 2 5,alignx center,growx 0");

            //---- shinyFrontLabel ----
            shinyFrontLabel.setText("Front");
            battleSpritePanel.add(shinyFrontLabel, "cell 3 5,alignx center,growx 0");

            //---- shinyBackLabel ----
            shinyBackLabel.setText("Back");
            battleSpritePanel.add(shinyBackLabel, "cell 4 5,alignx center,growx 0");

            //======== palettePanel ========
            {
                palettePanel.setBorder(new TitledBorder("Palettes"));
                palettePanel.setLayout(new MigLayout(
                    "hidemode 3",
                    // columns
                    "[grow,fill]" +
                    "[grow,fill]" +
                    "[fill]" +
                    "[grow,fill]" +
                    "[fill]" +
                    "[grow,fill]" +
                    "[grow,fill]",
                    // rows
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
                label5.setText("Normal");
                palettePanel.add(label5, "cell 0 0 2 1,alignx center,growx 0");

                //---- label6 ----
                label6.setText("Shiny");
                palettePanel.add(label6, "cell 5 0 2 1,alignx center,growx 0");

                //---- normalPaletteButton0 ----
                normalPaletteButton0.addActionListener(e -> normalPaletteButton0ActionPerformed(e));
                palettePanel.add(normalPaletteButton0, "cell 0 1");

                //---- normalPaletteButton1 ----
                normalPaletteButton1.addActionListener(e -> normalPaletteButton1ActionPerformed(e));
                palettePanel.add(normalPaletteButton1, "cell 1 1");

                //---- normalPaletteButton2 ----
                normalPaletteButton2.addActionListener(e -> normalPaletteButton2ActionPerformed(e));
                palettePanel.add(normalPaletteButton2, "cell 0 2");

                //---- normalPaletteButton3 ----
                normalPaletteButton3.addActionListener(e -> normalPaletteButton3ActionPerformed(e));
                palettePanel.add(normalPaletteButton3, "cell 1 2");

                //---- normalPaletteButton4 ----
                normalPaletteButton4.addActionListener(e -> normalPaletteButton4ActionPerformed(e));
                palettePanel.add(normalPaletteButton4, "cell 0 3");

                //---- normalPaletteButton5 ----
                normalPaletteButton5.addActionListener(e -> normalPaletteButton5ActionPerformed(e));
                palettePanel.add(normalPaletteButton5, "cell 1 3");

                //---- normalPaletteButton6 ----
                normalPaletteButton6.addActionListener(e -> normalPaletteButton6ActionPerformed(e));
                palettePanel.add(normalPaletteButton6, "cell 0 4");

                //---- normalPaletteButton7 ----
                normalPaletteButton7.addActionListener(e -> normalPaletteButton7ActionPerformed(e));
                palettePanel.add(normalPaletteButton7, "cell 1 4");

                //---- normalPaletteButton8 ----
                normalPaletteButton8.addActionListener(e -> normalPaletteButton8ActionPerformed(e));
                palettePanel.add(normalPaletteButton8, "cell 0 5");

                //---- normalPaletteButton9 ----
                normalPaletteButton9.addActionListener(e -> normalPaletteButton9ActionPerformed(e));
                palettePanel.add(normalPaletteButton9, "cell 1 5");

                //---- normalPaletteButton10 ----
                normalPaletteButton10.addActionListener(e -> normalPaletteButton10ActionPerformed(e));
                palettePanel.add(normalPaletteButton10, "cell 0 6");

                //---- normalPaletteButton11 ----
                normalPaletteButton11.addActionListener(e -> normalPaletteButton11ActionPerformed(e));
                palettePanel.add(normalPaletteButton11, "cell 1 6");

                //---- normalPaletteButton12 ----
                normalPaletteButton12.addActionListener(e -> normalPaletteButton12ActionPerformed(e));
                palettePanel.add(normalPaletteButton12, "cell 0 7");

                //---- normalPaletteButton13 ----
                normalPaletteButton13.addActionListener(e -> normalPaletteButton13ActionPerformed(e));
                palettePanel.add(normalPaletteButton13, "cell 1 7");

                //---- normalPaletteButton14 ----
                normalPaletteButton14.addActionListener(e -> normalPaletteButton14ActionPerformed(e));
                palettePanel.add(normalPaletteButton14, "cell 0 8");

                //---- normalPaletteButton15 ----
                normalPaletteButton15.addActionListener(e -> normalPaletteButton15ActionPerformed(e));
                palettePanel.add(normalPaletteButton15, "cell 1 8");
                palettePanel.add(hSpacer1, "cell 2 1");

                //---- separator1 ----
                separator1.setOrientation(SwingConstants.VERTICAL);
                palettePanel.add(separator1, "cell 3 1 1 8");
                palettePanel.add(hSpacer2, "cell 4 1");

                //---- shinyPaletteButton0 ----
                shinyPaletteButton0.addActionListener(e -> shinyPaletteButton0ActionPerformed(e));
                palettePanel.add(shinyPaletteButton0, "cell 5 1");

                //---- shinyPaletteButton1 ----
                shinyPaletteButton1.addActionListener(e -> shinyPaletteButton1ActionPerformed(e));
                palettePanel.add(shinyPaletteButton1, "cell 6 1");

                //---- shinyPaletteButton2 ----
                shinyPaletteButton2.addActionListener(e -> shinyPaletteButton2ActionPerformed(e));
                palettePanel.add(shinyPaletteButton2, "cell 5 2");

                //---- shinyPaletteButton3 ----
                shinyPaletteButton3.addActionListener(e -> shinyPaletteButton3ActionPerformed(e));
                palettePanel.add(shinyPaletteButton3, "cell 6 2");

                //---- shinyPaletteButton4 ----
                shinyPaletteButton4.addActionListener(e -> shinyPaletteButton4ActionPerformed(e));
                palettePanel.add(shinyPaletteButton4, "cell 5 3");

                //---- shinyPaletteButton5 ----
                shinyPaletteButton5.addActionListener(e -> shinyPaletteButton5ActionPerformed(e));
                palettePanel.add(shinyPaletteButton5, "cell 6 3");

                //---- shinyPaletteButton6 ----
                shinyPaletteButton6.addActionListener(e -> shinyPaletteButton6ActionPerformed(e));
                palettePanel.add(shinyPaletteButton6, "cell 5 4");

                //---- shinyPaletteButton7 ----
                shinyPaletteButton7.addActionListener(e -> shinyPaletteButton7ActionPerformed(e));
                palettePanel.add(shinyPaletteButton7, "cell 6 4");

                //---- shinyPaletteButton8 ----
                shinyPaletteButton8.addActionListener(e -> shinyPaletteButton8ActionPerformed(e));
                palettePanel.add(shinyPaletteButton8, "cell 5 5");

                //---- shinyPaletteButton9 ----
                shinyPaletteButton9.addActionListener(e -> shinyPaletteButton9ActionPerformed(e));
                palettePanel.add(shinyPaletteButton9, "cell 6 5");

                //---- shinyPaletteButton10 ----
                shinyPaletteButton10.addActionListener(e -> shinyPaletteButton10ActionPerformed(e));
                palettePanel.add(shinyPaletteButton10, "cell 5 6");

                //---- shinyPaletteButton11 ----
                shinyPaletteButton11.addActionListener(e -> shinyPaletteButton11ActionPerformed(e));
                palettePanel.add(shinyPaletteButton11, "cell 6 6");

                //---- shinyPaletteButton12 ----
                shinyPaletteButton12.addActionListener(e -> shinyPaletteButton12ActionPerformed(e));
                palettePanel.add(shinyPaletteButton12, "cell 5 7");

                //---- shinyPaletteButton13 ----
                shinyPaletteButton13.addActionListener(e -> shinyPaletteButton13ActionPerformed(e));
                palettePanel.add(shinyPaletteButton13, "cell 6 7");

                //---- shinyPaletteButton14 ----
                shinyPaletteButton14.addActionListener(e -> shinyPaletteButton14ActionPerformed(e));
                palettePanel.add(shinyPaletteButton14, "cell 5 8");

                //---- shinyPaletteButton15 ----
                shinyPaletteButton15.addActionListener(e -> shinyPaletteButton15ActionPerformed(e));
                palettePanel.add(shinyPaletteButton15, "cell 6 8");
            }
            battleSpritePanel.add(palettePanel, "cell 1 6 4 1,growx");
        }
        add(battleSpritePanel, "cell 0 1");

        //======== battlePanel ========
        {
            battlePanel.setBorder(new TitledBorder(""));
            battlePanel.setLayout(new MigLayout(
                "hidemode 3",
                // columns
                "[grow,fill]" +
                "[grow,fill]",
                // rows
                "[]" +
                "[]" +
                "[]" +
                "[]"));

            //---- femaleToggleButton ----
            femaleToggleButton.setText("Female");
            femaleToggleButton.addActionListener(e -> femaleToggleButtonActionPerformed(e));
            battlePanel.add(femaleToggleButton, "cell 0 0 2 1");

            //---- mockupPanel ----
            mockupPanel.setBorder(new TitledBorder("text"));
            mockupPanel.setMinimumSize(new Dimension(256, 192));
            battlePanel.add(mockupPanel, "cell 0 1 2 1,grow");
        }
        add(battlePanel, "cell 1 1 2 1,grow");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JComboBox speciesChooserComboBox;
    private JButton saveChangesButton;
    private JButton revertChangesButton;
    private JPanel battleSpritePanel;
    private JButton importSpriteButton;
    private JLabel frameToggleLabel;
    private JToggleButton frameToggleButton;
    private JLabel normalLabel;
    private JLabel shinyLabel;
    private JLabel maleLabel;
    private JButton normalMaleFrontButton;
    private JButton normalMaleBackButton;
    private JButton shinyMaleFrontButton;
    private JButton shinyMaleBackButton;
    private JLabel femaleLabel;
    private JButton normalFemaleFrontButton;
    private JButton normalFemaleBackButton;
    private JButton shinyFemaleFrontButton;
    private JButton shinyFemaleBackButton;
    private JLabel normalFrontLabel;
    private JLabel normalBackLabel;
    private JLabel shinyFrontLabel;
    private JLabel shinyBackLabel;
    private JPanel palettePanel;
    private JLabel label5;
    private JLabel label6;
    private JButton normalPaletteButton0;
    private JButton normalPaletteButton1;
    private JButton normalPaletteButton2;
    private JButton normalPaletteButton3;
    private JButton normalPaletteButton4;
    private JButton normalPaletteButton5;
    private JButton normalPaletteButton6;
    private JButton normalPaletteButton7;
    private JButton normalPaletteButton8;
    private JButton normalPaletteButton9;
    private JButton normalPaletteButton10;
    private JButton normalPaletteButton11;
    private JButton normalPaletteButton12;
    private JButton normalPaletteButton13;
    private JButton normalPaletteButton14;
    private JButton normalPaletteButton15;
    private JPanel hSpacer1;
    private JSeparator separator1;
    private JPanel hSpacer2;
    private JButton shinyPaletteButton0;
    private JButton shinyPaletteButton1;
    private JButton shinyPaletteButton2;
    private JButton shinyPaletteButton3;
    private JButton shinyPaletteButton4;
    private JButton shinyPaletteButton5;
    private JButton shinyPaletteButton6;
    private JButton shinyPaletteButton7;
    private JButton shinyPaletteButton8;
    private JButton shinyPaletteButton9;
    private JButton shinyPaletteButton10;
    private JButton shinyPaletteButton11;
    private JButton shinyPaletteButton12;
    private JButton shinyPaletteButton13;
    private JButton shinyPaletteButton14;
    private JButton shinyPaletteButton15;
    private JPanel battlePanel;
    private JToggleButton femaleToggleButton;
    private PokemonSpritePanel.BattleMockupPanel mockupPanel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public void setProject(Project project) throws IOException
    {
        this.project= project;

        String[] nameData;
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


        speciesChooserComboBox.removeAllItems();
        for(String s : nameData)
        {
            speciesChooserComboBox.addItem(new ComboBoxItem(s));
        }

        speciesChooserComboBox.setSelectedIndex(1);
        speciesChooserComboBoxActionPerformed(null);
    }

    public BufferedImage getPaletteImage(Color color)
    {
        BufferedImage image= new BufferedImage(40,16,BufferedImage.TYPE_INT_RGB);
        for(int row= 0; row < image.getHeight(); row++)
        {
            for(int col= 0; col < image.getWidth(); col++)
            {
                image.setRGB(col,row,color.getRGB());
            }
        }
        return image;
    }

    public static BufferedImage copyOfImage(BufferedImage image)
    {
        BufferedImage newImage= new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_INT_RGB);
        for(int row= 0; row < image.getHeight(); row++)
        {
            for(int col= 0; col < image.getWidth(); col++)
            {
                newImage.setRGB(col,row,image.getRGB(col,row));
            }
        }
        return newImage;
    }

    public static void clearDirs(File folder)
    {
        for(File f : Objects.requireNonNull(folder.listFiles()))
        {
            if(f.isDirectory())
                clearDirs(f);
            else
                f.delete();
        }
        folder.delete();
    }

    public BufferedImage updateColor(BufferedImage image, Color originalColor, Color newColor)
    {
        BufferedImage newImage= new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_INT_RGB);

        for(int row= 0; row < image.getHeight(); row++)
        {
            for(int col= 0; col < image.getWidth(); col++)
            {
                if(image.getRGB(col,row) == originalColor.getRGB())
                    newImage.setRGB(col,row,newColor.getRGB());
                else
                    newImage.setRGB(col,row,image.getRGB(col,row));
            }
        }

        return newImage;
    }

    static class PreviewPanel extends JComponent
    {
        Color currentColor;
        BufferedImage maleFront;
        BufferedImage femaleFront;
        BufferedImage maleBack;
        BufferedImage femaleBack;
        boolean[][] maleFrontMatches;
        boolean[][] femaleFrontMatches;
        boolean[][] maleBackMatches;
        boolean[][] femaleBackMatches;

        public PreviewPanel(JColorChooser chooser, BufferedImage maleFront, BufferedImage maleBack, BufferedImage femaleFront, BufferedImage femaleBack)
        {
            currentColor= chooser.getColor();
            this.maleFront= maleFront;
            this.maleBack= maleBack;
            this.femaleFront = femaleFront;
            this.femaleBack= femaleBack;
            setPreferredSize(new Dimension(700,200));

            maleFrontMatches = new boolean[maleFront.getHeight()][maleFront.getWidth()];
            maleBackMatches = new boolean[maleFront.getHeight()][maleFront.getWidth()];
            femaleFrontMatches = new boolean[femaleFront.getHeight()][femaleFront.getWidth()];
            femaleBackMatches = new boolean[femaleFront.getHeight()][femaleFront.getWidth()];
            for(int row= 0; row < maleFrontMatches.length; row++)
            {
                for(int col= 0; col < maleFrontMatches[row].length; col++)
                {
                    if(maleFront.getRGB(col,row) == currentColor.getRGB())
                        maleFrontMatches[row][col]= true;
                    if(femaleFront.getRGB(col,row) == currentColor.getRGB())
                        femaleFrontMatches[row][col]= true;
                    if(maleBack.getRGB(col,row) == currentColor.getRGB())
                        maleBackMatches[row][col]= true;
                    if(femaleBack.getRGB(col,row) == currentColor.getRGB())
                        femaleBackMatches[row][col]= true;
                }
            }
        }

        @Override
        public void paint(Graphics g) {
            g.setColor(Color.RED);
            g.drawString("RGB values must be a multiple of 8. Maximum value allowed is 248.", getWidth()/4 - 40,10);
            g.drawString("Male",getWidth()/4 - 15,195);
            g.drawString("Female",(getWidth()/4)*3 - 20,195);
            g.setColor(currentColor);
            g.drawImage(maleFront,getWidth()/2 - maleFront.getWidth()*2 - 20,20,this);
            g.drawImage(maleBack, getWidth()/2 - maleBack.getWidth() - 10,20,this);
            g.drawImage(femaleFront,getWidth()/2 + 10,20,this);
            g.drawImage(femaleBack, getWidth()/2 + femaleBack.getWidth() + 20,20,this);
        }
    }


    static class BattleMockupPanel extends JPanel
    {
        static Image textBar;
        static Image background;
        static Image playerPlatform;
        static Image enemyPlatform;
        static Image playerHealth;
        static Image enemyHealth;

        static BufferedImage frontSprite;
        static BufferedImage backSprite;

        static Image shadow;


        static SpriteData spriteData;
        static int frontModifier;
        static int backModifier;

        public BattleMockupPanel()
        {
            super();
            textBar= new ImageIcon(BattleMockupPanel.class.getResource("/icons/text_bar.png")).getImage();
            background= new ImageIcon(BattleMockupPanel.class.getResource("/icons/background.png")).getImage();
            playerPlatform= new ImageIcon(BattleMockupPanel.class.getResource("/icons/platform_you.png")).getImage();
            enemyPlatform= new ImageIcon(BattleMockupPanel.class.getResource("/icons/platform_opponent.png")).getImage();
            playerHealth= new ImageIcon(BattleMockupPanel.class.getResource("/icons/health_you.png")).getImage();
            enemyHealth= new ImageIcon(BattleMockupPanel.class.getResource("/icons/health_opponent.png")).getImage();

            Dimension dimension= new Dimension(256,192);
            setPreferredSize(dimension);
            setMinimumSize(dimension);
            setMaximumSize(dimension);
            setVisible(true);
        }

        @Override
        public void paint(Graphics g)
        {
            //The origin (top left) is 0, 0

            Graphics2D g2d= (Graphics2D) g;
            g2d.drawImage(background,0,0, null);
            g2d.drawImage(enemyPlatform,129,72,null);
            g2d.drawImage(playerPlatform,-42,122,null);

            if(spriteData.getShadowType() == SpriteDataProcessor.ShadowType.Small)
            {
                g2d.drawImage(shadow,179 + spriteData.getShadowXOffset(), 83, null);
            }
            else if(spriteData.getShadowType() == SpriteDataProcessor.ShadowType.Medium)
            {
                g2d.drawImage(shadow,174 + spriteData.getShadowXOffset(), 83, null);
            }
            else if(spriteData.getShadowType() == SpriteDataProcessor.ShadowType.Large)
            {
                g2d.drawImage(shadow,167 + spriteData.getShadowXOffset(), 82, null);
            }

            if(frontSprite != null)
            {
                g2d.drawImage(frontSprite,152,10 - spriteData.getSpriteYOffset() + frontModifier,null);
                g2d.drawImage(backSprite,23,72 + backModifier,null);
            }

            g2d.drawImage(playerHealth,129,95, null);
            g2d.drawImage(enemyHealth,0,18, null);

            g2d.drawImage(textBar,0,getHeight()-48, null);
            g2d.dispose();
        }

        public void setSprites(BufferedImage newFront, BufferedImage newBack, Color background, SpriteData newPositionData, int frontMod, int backMod)
        {
            frontSprite = makeTransparent(newFront, background);
            backSprite = makeTransparent(newBack, background);
            spriteData = newPositionData;
            frontModifier= frontMod & 0xff;
            backModifier= backMod & 0xff;

            switch (spriteData.getShadowType())
            {
                case None:
                    shadow= null;
                    break;

                case Small:
                    shadow= new ImageIcon(BattleMockupPanel.class.getResource("/icons/shadow_small.png")).getImage();
                    break;

                case Medium:
                    shadow= new ImageIcon(BattleMockupPanel.class.getResource("/icons/shadow_medium.png")).getImage();
                    break;

                case Large:
                    shadow= new ImageIcon(BattleMockupPanel.class.getResource("/icons/shadow_large.png")).getImage();
                    break;
            }


            System.out.println("pl_poke_data.narc Front Mod: " + spriteData.getSpriteYOffset());
            System.out.println("Height.narc Front Mod: " + frontModifier);
            System.out.println("Height.narc Back Mod: " + backModifier);
            System.out.println("Movement: " + spriteData.getMovement() + "\n");

            repaint();
        }

        private BufferedImage makeTransparent(BufferedImage sprite, Color background)
        {
            BufferedImage newSprite= new BufferedImage(sprite.getWidth(),sprite.getHeight(),BufferedImage.TYPE_INT_ARGB);
            for(int row= 0; row < sprite.getHeight(); row++)
            {
                for(int col= 0; col < sprite.getWidth(); col++)
                {
                    if(sprite.getRGB(col,row) != background.getRGB())
                        newSprite.setRGB(col,row,sprite.getRGB(col,row));
                }
            }

            return newSprite;
        }

    }




    private static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }




//    private static void testIndexedColor() throws IOException {
//        File in = new File("test.png");
//        File out = new File("test_result.png");
//
//        try (ImageInputStream input = ImageIO.createImageInputStream(in);
//             ImageOutputStream output = ImageIO.createImageOutputStream(out)) {
//            ImageReader reader = ImageIO.getImageReaders(input).next(); // Will fail if no reader
//            reader.setInput(input);
//
//            ImageWriter writer = ImageIO.getImageWriter(reader); // Will obtain a writer that understands the metadata from the reader
//            writer.setOutput(output);  // Will fail if no writer
//
//            // Now, the important part, we'll read the pixel AND metadata all in one go
//            IIOImage image = reader.readAll(0, null); // PNGs only have a single image, so index 0 is safe
//
//            // You can now access and modify the image data using:
//            BufferedImage bi = (BufferedImage) image.getRenderedImage();
//            FastBitmap fb = new FastBitmap(bi);
//
//            // ...do stuff...
//
//            Graphics2D g = bi.createGraphics();
//            try {
//                g.drawImage(fb.toBufferedImage(), 0, 0, null);
//            }
//            finally {
//                g.dispose();
//            }
//
//            // Write pixel and metadata back
//            writer.write(null, image, writer.getDefaultWriteParam());
//        }
//    }
}
