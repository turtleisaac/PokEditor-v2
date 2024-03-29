/*
 * Created by JFormDesigner on Tue Feb 02 16:47:18 EST 2021
 */

package com.turtleisaac.pokeditor.gui.editors.sprites.pokemon;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.*;

import com.jidesoft.swing.ComboBoxSearchable;
import com.turtleisaac.pokeditor.framework.narctowl.Narctowl;
import com.turtleisaac.pokeditor.editors.spritepositions.SpriteData;
import com.turtleisaac.pokeditor.editors.spritepositions.SpriteDataProcessor;
import com.turtleisaac.pokeditor.editors.text.TextEditor;
import com.turtleisaac.pokeditor.gui.*;
import com.turtleisaac.pokeditor.gui.ComboBoxItem;
import com.turtleisaac.pokeditor.gui.MyFilter;
import com.turtleisaac.pokeditor.project.Game;
import com.turtleisaac.pokeditor.project.Project;
import com.turtleisaac.pokeditor.utilities.images.*;
import net.miginfocom.swing.*;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
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

    private SpriteDataProcessor.ShadowType shadowType;
    private int femaleBackMod;
    private int femaleFrontMod;
    private int maleBackMod;
    private int maleFrontMod;

    private boolean canAdjust= false;
    boolean frameToggled;

    private boolean spriteEditDisabled= false;

    private static String unpackedFolderPath;


    public PokemonSpritePanel()
    {
        initComponents();
        normalPaletteButtons = new JButton[] {normalPaletteButton0,normalPaletteButton1,normalPaletteButton2,normalPaletteButton3,normalPaletteButton4,normalPaletteButton5,normalPaletteButton6,normalPaletteButton7,normalPaletteButton8,normalPaletteButton9,normalPaletteButton10,normalPaletteButton11,normalPaletteButton12,normalPaletteButton13,normalPaletteButton14,normalPaletteButton15};
        shinyPaletteButtons = new JButton[] {shinyPaletteButton0,shinyPaletteButton1,shinyPaletteButton2,shinyPaletteButton3,shinyPaletteButton4,shinyPaletteButton5,shinyPaletteButton6,shinyPaletteButton7,shinyPaletteButton8,shinyPaletteButton9,shinyPaletteButton10,shinyPaletteButton11,shinyPaletteButton12,shinyPaletteButton13,shinyPaletteButton14,shinyPaletteButton15};
        toDelete= new ArrayList<>();
        mockupPanel.setVisible(true);

        ComboBoxSearchable speciesSearchable= new ComboBoxSearchable(speciesChooserComboBox);
        speciesChooserComboBox.setMaximumRowCount(8);

        shadowSizeComboBox.addItem(SpriteDataProcessor.ShadowType.None);
        shadowSizeComboBox.addItem(SpriteDataProcessor.ShadowType.Small);
        shadowSizeComboBox.addItem(SpriteDataProcessor.ShadowType.Medium);
        shadowSizeComboBox.addItem(SpriteDataProcessor.ShadowType.Large);



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
        int frameNum= frameToggled ? 1 : 0;

        Color original= shiny ? shinyPalette[num] : palette[num];
        JColorChooser colorChooser= new JColorChooser(original);


        PreviewPanel previewPanel= new PreviewPanel(colorChooser,
                shiny ? sprites.getShinyMaleFront()[frameNum] : sprites.getMaleFront()[frameNum],
                shiny ? sprites.getShinyMaleBack()[frameNum] : sprites.getMaleBack()[frameNum],
                shiny ? sprites.getShinyFemaleFront()[frameNum] : sprites.getFemaleFront()[frameNum],
                shiny ? sprites.getShinyFemaleBack()[frameNum] : sprites.getFemaleBack()[frameNum]);

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

            previewPanel.femaleBack.updateColor(num,newColor);
            previewPanel.maleBack.updateColor(num,newColor);
            previewPanel.femaleFront.updateColor(num,newColor);
            previewPanel.maleFront.updateColor(num,newColor);
            previewPanel.repaint();

            previewPanel.currentColor= newColor;

            colorChooser.setColor(newColor);
            toApply[0]= newColor;
        });

        colorChooser.setPreviewPanel(previewPanel);
        AbstractColorChooserPanel[] chooserPanels= colorChooser.getChooserPanels();
        colorChooser.setChooserPanels(new AbstractColorChooserPanel[] {chooserPanels[3], chooserPanels[0]});
        colorChooser.setPreferredSize(new Dimension(700,480));


        final SpriteImage[][] shinyFemaleBack = {new SpriteImage[2]};
        final SpriteImage[][] shinyFemaleFront = {new SpriteImage[2]};

        final SpriteImage[][] shinyMaleBack = {new SpriteImage[2]};
        final SpriteImage[][] shinyMaleFront = {new SpriteImage[2]};

        final SpriteImage[][] femaleBack = {new SpriteImage[2]};
        final SpriteImage[][] femaleFront = {new SpriteImage[2]};

        final SpriteImage[][] maleBack = {new SpriteImage[2]};
        final SpriteImage[][] maleFront = {new SpriteImage[2]};

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


                        shinyFemaleBack[0] = new SpriteImage[] {sprites.getShinyFemaleBack()[0].updateColor(num,finalColor),sprites.getShinyFemaleBack()[1].updateColor(num,finalColor)};
                        shinyFemaleFront[0] = new SpriteImage[] {sprites.getShinyFemaleFront()[0].updateColor(num,finalColor),sprites.getShinyFemaleFront()[1].updateColor(num,finalColor)};

                        shinyMaleBack[0]= new SpriteImage[] {sprites.getShinyMaleBack()[0].updateColor(num,finalColor),sprites.getShinyMaleBack()[1].updateColor(num,finalColor)};
                        shinyMaleFront[0]= new SpriteImage[] {sprites.getShinyMaleFront()[0].updateColor(num,finalColor),sprites.getShinyMaleFront()[1].updateColor(num,finalColor)};


                        femaleBack[0]= Arrays.copyOf(sprites.getFemaleBack(),2);
                        femaleFront[0]= Arrays.copyOf(sprites.getFemaleFront(),2);
                        maleBack[0]= Arrays.copyOf(sprites.getMaleBack(),2);
                        maleFront[0]= Arrays.copyOf(sprites.getMaleFront(),2);

                    }
                    else
                    {
                        normalPaletteButtons[num].setIcon(new ImageIcon(getPaletteImage(finalColor)));
                        palette[num]= finalColor;

                        femaleBack[0]= new SpriteImage[] {sprites.getFemaleBack()[0].updateColor(num,finalColor),sprites.getFemaleBack()[1].updateColor(num,finalColor)};
                        femaleFront[0]= new SpriteImage[] {sprites.getFemaleFront()[0].updateColor(num,finalColor),sprites.getFemaleFront()[1].updateColor(num,finalColor)};

                        maleBack[0]= new SpriteImage[] {sprites.getMaleBack()[0].updateColor(num,finalColor),sprites.getMaleBack()[1].updateColor(num,finalColor)};
                        maleFront[0]= new SpriteImage[] {sprites.getMaleFront()[0].updateColor(num,finalColor),sprites.getMaleFront()[1].updateColor(num,finalColor)};


                        shinyFemaleBack[0]= Arrays.copyOf(sprites.getShinyFemaleBack(),2);
                        shinyFemaleFront[0]= Arrays.copyOf(sprites.getShinyFemaleFront(),2);
                        shinyMaleBack[0]= Arrays.copyOf(sprites.getShinyMaleBack(),2);
                        shinyMaleFront[0]= Arrays.copyOf(sprites.getShinyMaleFront(),2);
                    }

                    sprites= new PokemonSprites()
                    {
                        @Override
                        public SpriteImage[] getFemaleBack()
                        {
                            return femaleBack[0];
                        }

                        @Override
                        public SpriteImage[] getShinyFemaleBack()
                        {
                            return shinyFemaleBack[0];
                        }

                        @Override
                        public SpriteImage[] getMaleBack()
                        {
                            return maleBack[0];
                        }

                        @Override
                        public SpriteImage[] getShinyMaleBack()
                        {
                            return shinyMaleBack[0];
                        }

                        @Override
                        public SpriteImage[] getFemaleFront()
                        {
                            return femaleFront[0];
                        }

                        @Override
                        public SpriteImage[] getShinyFemaleFront()
                        {
                            return shinyFemaleFront[0];
                        }

                        @Override
                        public SpriteImage[] getMaleFront()
                        {
                            return maleFront[0];
                        }

                        @Override
                        public SpriteImage[] getShinyMaleFront()
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
                    positionAdjustmentMade();
                    updateIcons();
                },

                e -> //Cancel
                {
                    System.out.println("Not applying colors");
                });

        dialog.setVisible(true);
    }

    private void saveChangesButtonActionPerformed(ActionEvent e)
    {
        palette= Arrays.copyOf(sprites.getPalette(),16);
        shinyPalette= Arrays.copyOf(sprites.getShinyPalette(),16);

        unpackedFolderPath = project.getProjectPath().toString() + File.separator + "temp" + File.separator;

        String dataPath= project.getDataPath();
        int selected= speciesChooserComboBox.getSelectedIndex()*6;

        boolean primary= Project.isPrimary(project);

        File pokegraNarcPath= new File(dataPath + (Project.isDPPT(project) ? project.getBaseRom() == Game.Platinum ? File.separator + "poketool" + File.separator + "pokegra" + File.separator + "pl_pokegra.narc" : File.separator + "poketool" + File.separator + "pokegra" + File.separator + "pokegra.narc" : "/a" + File.separator + "0" + File.separator + "0" + File.separator + "4"));
        File pokegraDirPath= new File(unpackedFolderPath + "pokegra");

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

        try
        {
            ImageEncrypter.saveSprites(pokegraDirPath.getAbsolutePath(), sprites,selected,primary);
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
            narctowl.pack(pokegraDirPath.getAbsolutePath(),"",pokegraNarcPath.getAbsolutePath());
        }
        catch (IOException ignored)
        {
        }
    }

    private void revertChangesButtonActionPerformed(ActionEvent e)
    {
        speciesChooserComboBoxActionPerformed(e);
    }

    private void speciesChooserComboBoxActionPerformed(ActionEvent e)
    {
        System.out.println("Selected: " + speciesChooserComboBox.getSelectedIndex());
        canAdjust= false;
        frameToggleButton.setSelected(false);
        genderToggleButton.setSelected(false);
        shinyToggleButton.setSelected(false);
        String dataPath= project.getDataPath();
        int selected= speciesChooserComboBox.getSelectedIndex()*6;

        unpackedFolderPath = project.getProjectPath().toString() + File.separator + "temp" + File.separator;

        File pokegraNarcPath= new File(dataPath + (Project.isDPPT(project) ? project.getBaseRom() == Game.Platinum ? File.separator + "poketool" + File.separator + "pokegra" + File.separator + "pl_pokegra.narc" : File.separator + "poketool" + File.separator + "pokegra" + File.separator + "pokegra.narc" :  File.separator + "a" + File.separator + "0" + File.separator + "0" + File.separator + "4"));
        File pokegraDirPath= new File(unpackedFolderPath + "pokegra");

        boolean primary= Project.isPrimary(project);

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
        if(!ImageDecrypter.spriteExists(pokegraDirPath.getAbsolutePath(),selected))
        {
            JOptionPane.showMessageDialog(this,"The selected sprite does not exist.","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        sprites= ImageDecrypter.getSprites(pokegraDirPath.getAbsolutePath(),selected,primary);

        palette= Arrays.copyOf(sprites.getPalette(),16);
        shinyPalette= Arrays.copyOf(sprites.getShinyPalette(),16);

        femaleBackMod= SpriteDataProcessor.getHeightModifier(project,speciesChooserComboBox.getSelectedIndex(), SpriteDataProcessor.SpriteType.Female_Back);
        femaleFrontMod= SpriteDataProcessor.getHeightModifier(project,speciesChooserComboBox.getSelectedIndex(), SpriteDataProcessor.SpriteType.Female_Front);
        maleBackMod= SpriteDataProcessor.getHeightModifier(project, speciesChooserComboBox.getSelectedIndex(), SpriteDataProcessor.SpriteType.Male_Back);
        maleFrontMod= SpriteDataProcessor.getHeightModifier(project, speciesChooserComboBox.getSelectedIndex(), SpriteDataProcessor.SpriteType.Male_Front);


        SpriteData spriteData1= spriteData[speciesChooserComboBox.getSelectedIndex()];
        globalFrontYSpinner.setValue((int) spriteData1.getSpriteYOffset());
        shadowXSpinner.setValue((int) spriteData1.getShadowXOffset());
        movementEffectSpinner.setValue(spriteData1.getMovement());
        backMovementEffectSpinner.setValue(spriteData1.getBackMovement());
        shadowSizeComboBox.setSelectedItem(spriteData1.getShadowType());
        femaleBackYSpinner.setValue(femaleBackMod);
        maleBackYSpinner.setValue(maleBackMod);
        femaleFrontYSpinner.setValue(femaleFrontMod);
        maleFrontYSpinner.setValue(maleFrontMod);

        canAdjust= true;

        positionAdjustmentMade();
        updateIcons();
    }

    private void frameToggleButtonActionPerformed(ActionEvent e)
    {
        frameToggled= ((JToggleButton) e.getSource()).isSelected();

        if((e.getSource()) != frameToggleButton)
            frameToggleButton.setSelected(frameToggled);
        else
            frameToggleButton2.setSelected(frameToggled);

        int frameValue= frameToggled ? 1 : 0;
        frameToggleLabel.setText("" + frameValue);


        normalFemaleFrontButton.setIcon(new ImageIcon(sprites.getFemaleFront()[frameValue].getImage()));
        normalFemaleBackButton.setIcon(new ImageIcon(sprites.getFemaleBack()[frameValue].getImage()));
        shinyFemaleFrontButton.setIcon(new ImageIcon(sprites.getShinyFemaleFront()[frameValue].getImage()));
        shinyFemaleBackButton.setIcon(new ImageIcon(sprites.getShinyFemaleBack()[frameValue].getImage()));

        normalMaleFrontButton.setIcon(new ImageIcon(sprites.getMaleFront()[frameValue].getImage()));
        normalMaleBackButton.setIcon(new ImageIcon(sprites.getMaleBack()[frameValue].getImage()));
        shinyMaleFrontButton.setIcon(new ImageIcon(sprites.getShinyMaleFront()[frameValue].getImage()));
        shinyMaleBackButton.setIcon(new ImageIcon(sprites.getShinyMaleBack()[frameValue].getImage()));

        positionAdjustmentMade();
    }

    private void femaleToggleButtonActionPerformed(ActionEvent e) {
        positionAdjustmentMade();
    }

    private void shinyToggleButtonActionPerformed(ActionEvent e) {
        positionAdjustmentMade();
    }

    private void globalFrontYSpinnerStateChanged(ChangeEvent e) {
        positionAdjustmentMade();
    }

    private void shadowXSpinnerStateChanged(ChangeEvent e) {
        positionAdjustmentMade();
    }

    private void femaleBackYSpinnerStateChanged(ChangeEvent e) {
        positionAdjustmentMade();
    }

    private void maleBackYSpinnerStateChanged(ChangeEvent e) {
        positionAdjustmentMade();
    }

    private void femaleFrontYSpinnerStateChanged(ChangeEvent e) {
        positionAdjustmentMade();
    }

    private void maleFrontYSpinnerStateChanged(ChangeEvent e) {
        positionAdjustmentMade();
    }

    private void movementEffectSpinnerStateChanged(ChangeEvent e) {
        positionAdjustmentMade();
    }

    private void backMovementEffectSpinnerStateChanged(ChangeEvent e) {
        positionAdjustmentMade();
    }

    private void shadowSizeComboBoxActionPerformed(ActionEvent e) {
        positionAdjustmentMade();
    }

    private void positionAdjustmentMade()
    {
        if(canAdjust)
        {
            int frameValue= frameToggled ? 1 : 0;
            int femaleValue= genderToggleButton.isSelected() ? 1 : 0;
            boolean isShiny= shinyToggleButton.isSelected();

            femaleBackMod= (Integer) femaleBackYSpinner.getValue();
            maleBackMod= (Integer) maleBackYSpinner.getValue();
            femaleFrontMod= (Integer) femaleFrontYSpinner.getValue();
            maleFrontMod= (Integer) maleFrontYSpinner.getValue();
            shadowType= (SpriteDataProcessor.ShadowType) shadowSizeComboBox.getSelectedItem();

            if(shadowType == null)
            {
                shadowSizeComboBox.removeAllItems();
                shadowSizeComboBox.addItem(SpriteDataProcessor.ShadowType.None);
                shadowSizeComboBox.addItem(SpriteDataProcessor.ShadowType.Small);
                shadowSizeComboBox.addItem(SpriteDataProcessor.ShadowType.Medium);
                shadowSizeComboBox.addItem(SpriteDataProcessor.ShadowType.Large);
                shadowType= SpriteDataProcessor.ShadowType.None;
            }


            int frontModifier;
            int backModifier;
            if(genderToggleButton.isSelected())
            {
                backModifier= femaleBackMod;
                frontModifier= femaleFrontMod;
            }
            else
            {
                backModifier= maleBackMod;
                frontModifier= maleFrontMod;
            }

            SpriteImage front;
            try
            {
                front= isShiny ? femaleValue == 1 ? sprites.getShinyFemaleFront()[frameValue] : sprites.getShinyMaleFront()[frameValue] : femaleValue == 1 ? sprites.getFemaleFront()[frameValue] : sprites.getMaleFront()[frameValue];
            }
            catch (NullPointerException exception)
            {
                front= new SpriteImage(palette);
            }


            SpriteImage back;
            try
            {
                back= isShiny ? femaleValue == 1 ? sprites.getShinyFemaleBack()[frameValue] : sprites.getShinyMaleBack()[frameValue] : femaleValue == 1 ? sprites.getFemaleBack()[frameValue] : sprites.getMaleBack()[frameValue];
            }
            catch (NullPointerException exception)
            {
                back= new SpriteImage(palette);
            }

            int globalYValue= (Integer) globalFrontYSpinner.getValue();
            int shadowXValue= (Integer) shadowXSpinner.getValue();

            mockupPanel.setSprites(front,back,(byte) (globalYValue & 0xff),(byte) (shadowXValue & 0xff),shadowType,frontModifier,backModifier,genderToggleButton.isSelected());
        }
    }

    private void savePositionChangesButtonActionPerformed(ActionEvent e)
    {
        int unknownByte= spriteData[speciesChooserComboBox.getSelectedIndex()].getUnknownByte();
        byte[] unknownSection1= spriteData[speciesChooserComboBox.getSelectedIndex()].getUnknownSection1();
        byte[] unknownSection2= spriteData[speciesChooserComboBox.getSelectedIndex()].getUnknownSection2();

        spriteData[speciesChooserComboBox.getSelectedIndex()]= new SpriteData()
        {
            @Override
            public int getUnknownByte()
            {
                return unknownByte;
            }

            @Override
            public int getMovement()
            {
                return (int) movementEffectSpinner.getValue();
            }

            @Override
            public byte[] getUnknownSection1()
            {
                return unknownSection1;
            }

            @Override
            public int getBackMovement()
            {
                return (int) backMovementEffectSpinner.getValue();
            }

            @Override
            public byte[] getUnknownSection2()
            {
                return unknownSection2;
            }

            @Override
            public byte getSpriteYOffset()
            {
                return (byte) ((int) globalFrontYSpinner.getValue());
            }

            @Override
            public byte getShadowXOffset()
            {
                return (byte) ((int) shadowXSpinner.getValue());
            }

            @Override
            public SpriteDataProcessor.ShadowType getShadowType()
            {
                return (SpriteDataProcessor.ShadowType) shadowSizeComboBox.getSelectedItem();
            }
        };

        try
        {
            SpriteDataProcessor.applyPositionChanges(project,spriteData);
            SpriteDataProcessor.applyHeightChanges(project,speciesChooserComboBox.getSelectedIndex(),femaleBackMod,maleBackMod,femaleFrontMod,maleFrontMod);
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }

    }

    private void movementRadioButtonActionPerformed(ActionEvent e)
    {
        if(movementRadioButton.isSelected())
        {
            try
            {
                Desktop.getDesktop().browse(new URI("https://imgur.com/a/SK8yitv"));
            } catch (URISyntaxException | IOException exception)
            {
                exception.printStackTrace();
            }
            movementRadioButton.setSelected(false);
        }

    }

    private void normalFemaleFrontButtonActionPerformed(ActionEvent e)
    {
        spriteImportExportAction(sprites.getFemaleFront()[frameToggled ? 1 : 0],false,true,false);
    }

    private void normalFemaleBackButtonActionPerformed(ActionEvent e)
    {
        spriteImportExportAction(sprites.getFemaleBack()[frameToggled ? 1 : 0],false,false,false);
    }

    private void normalMaleFrontButtonActionPerformed(ActionEvent e)
    {
        spriteImportExportAction(sprites.getMaleFront()[frameToggled ? 1 : 0],true,true,false);
    }

    private void normalMaleBackButtonActionPerformed(ActionEvent e)
    {
        spriteImportExportAction(sprites.getMaleBack()[frameToggled ? 1 : 0],true,false,false);
    }


    private void shinyFemaleFrontButtonActionPerformed(ActionEvent e)
    {
        spriteImportExportAction(sprites.getShinyFemaleFront()[frameToggled ? 1 : 0],false,true,true);
    }

    private void shinyFemaleBackButtonActionPerformed(ActionEvent e)
    {
        spriteImportExportAction(sprites.getShinyFemaleBack()[frameToggled ? 1 : 0],false,false,true);
    }

    private void shinyMaleFrontButtonActionPerformed(ActionEvent e)
    {
        spriteImportExportAction(sprites.getShinyMaleFront()[frameToggled ? 1 : 0],true,true,true);
    }

    private void shinyMaleBackButtonActionPerformed(ActionEvent e)
    {
        spriteImportExportAction(sprites.getShinyMaleBack()[frameToggled ? 1 : 0],true,false,true);
    }

    private void updateIcons()
    {
        normalFemaleFrontButton.setIcon(new ImageIcon(sprites.getFemaleFront()[frameToggled ? 1 : 0].getImage()));
        normalFemaleBackButton.setIcon(new ImageIcon(sprites.getFemaleBack()[frameToggled ? 1 : 0].getImage()));
        shinyFemaleFrontButton.setIcon(new ImageIcon(sprites.getShinyFemaleFront()[frameToggled ? 1 : 0].getImage()));
        shinyFemaleBackButton.setIcon(new ImageIcon(sprites.getShinyFemaleBack()[frameToggled ? 1 : 0].getImage()));

        normalMaleFrontButton.setIcon(new ImageIcon(sprites.getMaleFront()[frameToggled ? 1 : 0].getImage()));
        normalMaleBackButton.setIcon(new ImageIcon(sprites.getMaleBack()[frameToggled ? 1 : 0].getImage()));
        shinyMaleFrontButton.setIcon(new ImageIcon(sprites.getShinyMaleFront()[frameToggled ? 1 : 0].getImage()));
        shinyMaleBackButton.setIcon(new ImageIcon(sprites.getShinyMaleBack()[frameToggled ? 1 : 0].getImage()));

        Color[] palette= sprites.getPalette();
        Color[] shinyPalette= sprites.getShinyPalette();

        for(int i= 0; i < normalPaletteButtons.length; i++)
        {
            normalPaletteButtons[i].setIcon(new ImageIcon(getPaletteImage(palette[i])));
            shinyPaletteButtons[i].setIcon(new ImageIcon(getPaletteImage(shinyPalette[i])));
        }

        this.palette= palette;
        this.shinyPalette= shinyPalette;

        positionAdjustmentMade();
    }

    private void spriteImportExportAction(SpriteImage image, boolean male, boolean front, boolean shiny)
    {
        if(spriteEditDisabled)
        {
            JOptionPane.showMessageDialog(this,"Image importing/ exporting/ editing is currently disabled","PokEditor",JOptionPane.ERROR_MESSAGE);
            return;
        }

        int optionValue= JOptionPane.showOptionDialog(this,"Choose an option","PokEditor",JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE,null,new String[] {"Edit","Import","Export"},0);

        if(optionValue == 2) //Export
        {
            exportSpriteAction(image);
        }
        else if(optionValue == 1) //Import
        {
            importSpriteAction(male,front,shiny);
        }
        else if(optionValue == 0) //Edit
        {
//            SpriteEditor spriteEditor= new SpriteEditor(image);
            JOptionPane.showMessageDialog(this,"Internal image editing is currently disabled. Please use the Export feature instead.","PokEditor",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportSpriteAction(SpriteImage image)
    {
//        File spriteEditor= new File(project.getProjectPath() + File.separator + "sprite_editor.ser");
//
//        if(!spriteEditor.exists())
//        {
//            JOptionPane.showMessageDialog(this,"You will now have to find and select the executable for the sprite editor that you use.\nIf you are a Mac user, applications will appear as folders but with the extension\".app\"","Sprite Exporter",JOptionPane.INFORMATION_MESSAGE);
//            JFileChooser fc= new JFileChooser(System.getProperty("user.dir"));
//            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
//            fc.setDialogTitle("Choose your default Sprite Editor");
//            int returnVal = fc.showOpenDialog(this);
//
//            if(returnVal == JFileChooser.APPROVE_OPTION)
//            {
//                try
//                {
//                    ObjectOutputStream objectOutputStream= new ObjectOutputStream(new FileOutputStream(spriteEditor));
//                    objectOutputStream.writeObject(fc.getSelectedFile());
//                }
//                catch (IOException fileNotFoundException)
//                {
//                    fileNotFoundException.printStackTrace();
//                }
//            }
//            else
//            {
//                return;
//            }
//        }


        JFileChooser fc= new JFileChooser(project.getProjectPath());
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.addChoosableFileFilter(new MyFilter("PNG File",".png"));
        fc.setAcceptAllFileFilterUsed(false);
        fc.setDialogTitle("Write PNG");
        int returnVal = fc.showOpenDialog(this);

        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            String path= fc.getSelectedFile().getAbsolutePath();
            if(!path.toLowerCase().endsWith(".png"))
                path+= ".png";

            try
            {
                ImageExporter.exportImage(path,image);
            } catch (IOException exception)
            {
                exception.printStackTrace();
            }

        }
        else if(returnVal == JFileChooser.CANCEL_OPTION)
        {
            return;
        }

//        Runtime runtime= Runtime.getRuntime();
//        Process process;
//        try
//        {
//            ObjectInputStream objectInputStream= new ObjectInputStream(new FileInputStream(spriteEditor));
//            spriteEditor= (File) objectInputStream.readObject();
//
//            File spriteTemp= File.createTempFile("temp_sprite_pokeditor",".png");
//            spriteTemp.deleteOnExit();
//            ImageIO.write(image,"png",spriteTemp);
//
//            if(System.getProperty("os.name").toLowerCase().contains("mac"))
//            {
//                System.out.println(spriteEditor.getAbsolutePath());
//                System.out.println(spriteTemp.getAbsolutePath());
//                runtime.exec(spriteEditor.getAbsolutePath() + "/Contents/MacOS/gimp " + spriteTemp.getAbsolutePath());
//            }
//            else if(System.getProperty("os.name").toLowerCase().contains("windows"))
//            {
//                process= runtime.exec(spriteEditor.getAbsolutePath() + " " + spriteTemp.getAbsolutePath());
//            }
//
//            CopyImageToClipboard clipboard= new CopyImageToClipboard();
//            clipboard.copyImage(image);
//        }
//        catch (IOException | ClassNotFoundException fileNotFoundException)
//        {
//            fileNotFoundException.printStackTrace();
//        }
    }

    private void importSpriteAction(boolean male, boolean front, boolean shiny)
    {
        SpriteImage newImage= null;

        JFileChooser fc= new JFileChooser(project.getProjectPath());
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.addChoosableFileFilter(new MyFilter("PNG File",".png"));
        fc.setAcceptAllFileFilterUsed(false);
        fc.setDialogTitle("Import PNG");
        int returnVal = fc.showOpenDialog(this);

        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            try
            {
                newImage= ImageImporter.importImage(fc.getSelectedFile().getAbsolutePath());
            }
            catch (IOException exception)
            {
                exception.printStackTrace();
            }
        }
        else if(returnVal == JFileChooser.CANCEL_OPTION)
        {
            return;
        }

        if(newImage == null)
        {
            return;
        }

        SpriteImage[] femaleBack= sprites.getFemaleBack();
        SpriteImage[] maleBack= sprites.getMaleBack();
        SpriteImage[] femaleFront= sprites.getFemaleFront();
        SpriteImage[] maleFront= sprites.getMaleFront();

        SpriteImage[] shinyFemaleBack= sprites.getShinyFemaleBack();
        SpriteImage[] shinyMaleBack= sprites.getShinyMaleBack();
        SpriteImage[] shinyFemaleFront= sprites.getShinyFemaleFront();
        SpriteImage[] shinyMaleFront= sprites.getShinyMaleFront();

        if(!shiny)
        {
            palette= newImage.getPalette();
            shinyPalette= sprites.getShinyPalette();
        }
        else
        {
            palette= sprites.getPalette();
            shinyPalette= newImage.getPalette();
        }



        if(!shiny)
        {
            femaleBack[0].setPalette(palette);
            femaleBack[1].setPalette(palette);
            maleBack[0].setPalette(palette);
            maleBack[1].setPalette(palette);
            femaleFront[0].setPalette(palette);
            femaleFront[1].setPalette(palette);
            maleFront[0].setPalette(palette);
            maleFront[1].setPalette(palette);
        }
        else
        {
            shinyFemaleBack[0].setPalette(palette);
            shinyFemaleBack[1].setPalette(palette);
            shinyMaleBack[0].setPalette(palette);
            shinyMaleBack[1].setPalette(palette);
            shinyFemaleFront[0].setPalette(palette);
            shinyFemaleFront[1].setPalette(palette);
            shinyMaleFront[0].setPalette(palette);
            shinyMaleFront[1].setPalette(palette);
        }

        if(male)
        {
            if(front)
            {
                if(shiny)
                {
                    shinyMaleFront[frameToggled ? 1 : 0]= newImage;
                    maleFront[frameToggled ? 1 : 0]= maleFront[frameToggled ? 1 : 0].createCopyWithPalette(newImage);
                }
                else
                {
                    maleFront[frameToggled ? 1 : 0]= newImage;
                    shinyMaleFront[frameToggled ? 1 : 0]= shinyMaleFront[frameToggled ? 1 : 0].createCopyWithPalette(newImage);
                }
            }
            else
            {
                if(shiny)
                {
                    shinyMaleBack[frameToggled ? 1 : 0]= newImage;
                    maleBack[frameToggled ? 1 : 0]= maleBack[frameToggled ? 1 : 0].createCopyWithPalette(newImage);
                }
                else
                {
                    maleBack[frameToggled ? 1 : 0]= newImage;
                    shinyMaleBack[frameToggled ? 1 : 0]= shinyMaleBack[frameToggled ? 1 : 0].createCopyWithPalette(newImage);
                }
            }
        }
        else
        {
            if(front)
            {
                if(shiny)
                {
                    shinyFemaleFront[frameToggled ? 1 : 0]= newImage;
                    femaleFront[frameToggled ? 1 : 0]= femaleFront[frameToggled ? 1 : 0].createCopyWithPalette(newImage);
                }
                else
                {
                    femaleFront[frameToggled ? 1 : 0]= newImage;
                    shinyFemaleFront[frameToggled ? 1 : 0]= shinyFemaleFront[frameToggled ? 1 : 0].createCopyWithPalette(newImage);
                }
            }
            else
            {
                if(shiny)
                {
                    shinyFemaleBack[frameToggled ? 1 : 0]= newImage;
                    femaleBack[frameToggled ? 1 : 0]= femaleBack[frameToggled ? 1 : 0].createCopyWithPalette(newImage);
                }
                else
                {
                    femaleBack[frameToggled ? 1 : 0]= newImage;
                    shinyFemaleBack[frameToggled ? 1 : 0]= shinyFemaleBack[frameToggled ? 1 : 0].createCopyWithPalette(newImage);
                }
            }
        }


        sprites= new PokemonSprites()
        {
            @Override
            public SpriteImage[] getFemaleBack()
            {
                return femaleBack;
            }

            @Override
            public SpriteImage[] getShinyFemaleBack()
            {
                return shinyFemaleBack;
            }

            @Override
            public SpriteImage[] getMaleBack()
            {
                return maleBack;
            }

            @Override
            public SpriteImage[] getShinyMaleBack()
            {
                return shinyMaleBack;
            }

            @Override
            public SpriteImage[] getFemaleFront()
            {
                return femaleFront;
            }

            @Override
            public SpriteImage[] getShinyFemaleFront()
            {
                return shinyFemaleFront;
            }

            @Override
            public SpriteImage[] getMaleFront()
            {
                return maleFront;
            }

            @Override
            public SpriteImage[] getShinyMaleFront()
            {
                return shinyMaleFront;
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

        updateIcons();
    }

    private void normalToShinyButtonActionPerformed(ActionEvent e)
    {
        SpriteImage[] shinyFemaleBack= sprites.getShinyFemaleBack();
        SpriteImage[] shinyMaleBack= sprites.getShinyMaleBack();
        SpriteImage[] shinyFemaleFront= sprites.getShinyFemaleFront();
        SpriteImage[] shinyMaleFront= sprites.getShinyMaleFront();

        Color[] shinyPalette= sprites.getShinyPalette();

        sprites= new PokemonSprites()
        {
            @Override
            public SpriteImage[] getFemaleBack()
            {
                return shinyFemaleBack;
            }

            @Override
            public SpriteImage[] getShinyFemaleBack()
            {
                return shinyFemaleBack;
            }

            @Override
            public SpriteImage[] getMaleBack()
            {
                return shinyMaleBack;
            }

            @Override
            public SpriteImage[] getShinyMaleBack()
            {
                return shinyMaleBack;
            }

            @Override
            public SpriteImage[] getFemaleFront()
            {
                return shinyFemaleFront;
            }

            @Override
            public SpriteImage[] getShinyFemaleFront()
            {
                return shinyFemaleFront;
            }

            @Override
            public SpriteImage[] getMaleFront()
            {
                return shinyMaleFront;
            }

            @Override
            public SpriteImage[] getShinyMaleFront()
            {
                return shinyMaleFront;
            }

            @Override
            public Color[] getPalette()
            {
                return shinyPalette;
            }

            @Override
            public Color[] getShinyPalette()
            {
                return shinyPalette;
            }
        };

        updateIcons();
    }

    private void shinyToNormalButtonActionPerformed(ActionEvent e)
    {
        SpriteImage[] femaleBack= sprites.getFemaleBack();
        SpriteImage[] maleBack= sprites.getMaleBack();
        SpriteImage[] femaleFront= sprites.getFemaleFront();
        SpriteImage[] maleFront= sprites.getMaleFront();

        Color[] palette= sprites.getPalette();

        sprites= new PokemonSprites()
        {
            @Override
            public SpriteImage[] getFemaleBack()
            {
                return femaleBack;
            }

            @Override
            public SpriteImage[] getShinyFemaleBack()
            {
                return femaleBack;
            }

            @Override
            public SpriteImage[] getMaleBack()
            {
                return maleBack;
            }

            @Override
            public SpriteImage[] getShinyMaleBack()
            {
                return maleBack;
            }

            @Override
            public SpriteImage[] getFemaleFront()
            {
                return femaleFront;
            }

            @Override
            public SpriteImage[] getShinyFemaleFront()
            {
                return femaleFront;
            }

            @Override
            public SpriteImage[] getMaleFront()
            {
                return maleFront;
            }

            @Override
            public SpriteImage[] getShinyMaleFront()
            {
                return maleFront;
            }

            @Override
            public Color[] getPalette()
            {
                return palette;
            }

            @Override
            public Color[] getShinyPalette()
            {
                return palette;
            }
        };

        updateIcons();
    }

    private void swapPalettesButtonActionPerformed(ActionEvent e)
    {
        SpriteImage[] femaleBack= sprites.getFemaleBack();
        SpriteImage[] maleBack= sprites.getMaleBack();
        SpriteImage[] femaleFront= sprites.getFemaleFront();
        SpriteImage[] maleFront= sprites.getMaleFront();

        SpriteImage[] shinyFemaleBack= sprites.getShinyFemaleBack();
        SpriteImage[] shinyMaleBack= sprites.getShinyMaleBack();
        SpriteImage[] shinyFemaleFront= sprites.getShinyFemaleFront();
        SpriteImage[] shinyMaleFront= sprites.getShinyMaleFront();

        Color[] palette= sprites.getPalette();
        Color[] shinyPalette= sprites.getShinyPalette();

        sprites= new PokemonSprites()
        {
            @Override
            public SpriteImage[] getFemaleBack()
            {
                return shinyFemaleBack;
            }

            @Override
            public SpriteImage[] getShinyFemaleBack()
            {
                return femaleBack;
            }

            @Override
            public SpriteImage[] getMaleBack()
            {
                return shinyMaleBack;
            }

            @Override
            public SpriteImage[] getShinyMaleBack()
            {
                return maleBack;
            }

            @Override
            public SpriteImage[] getFemaleFront()
            {
                return shinyFemaleFront;
            }

            @Override
            public SpriteImage[] getShinyFemaleFront()
            {
                return femaleFront;
            }

            @Override
            public SpriteImage[] getMaleFront()
            {
                return shinyMaleFront;
            }

            @Override
            public SpriteImage[] getShinyMaleFront()
            {
                return maleFront;
            }

            @Override
            public Color[] getPalette()
            {
                return shinyPalette;
            }

            @Override
            public Color[] getShinyPalette()
            {
                return palette;
            }
        };

        updateIcons();
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        speciesChooserComboBox = new EditorComboBox();
        saveChangesButton = new JButton();
        revertChangesButton = new JButton();
        button1 = new JButton();
        battleSpritePanel = new JPanel();
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
        normalToShinyButton = new JButton();
        shinyToNormalButton = new JButton();
        swapPalettesButton = new JButton();
        battlePanel = new JPanel();
        mockupPanel = new PokemonSpritePanel.BattleMockupPanel();
        genderToggleButton = new JToggleButton();
        shinyToggleButton = new JToggleButton();
        frameToggleButton2 = new JToggleButton();
        panel1 = new JPanel();
        separator4 = new JSeparator();
        globalFrontYLabel = new JLabel();
        globalFrontYSpinner = new JSpinner();
        shadowXLabel = new JLabel();
        shadowXSpinner = new JSpinner();
        separator2 = new JSeparator();
        femaleBackYLabel = new JLabel();
        femaleBackYSpinner = new JSpinner();
        maleBackYLabel = new JLabel();
        maleBackYSpinner = new JSpinner();
        femaleFrontYLabel = new JLabel();
        femaleFrontYSpinner = new JSpinner();
        maleFrontYLabel = new JLabel();
        maleFrontYSpinner = new JSpinner();
        separator3 = new JSeparator();
        movementEffectLabel = new JLabel();
        movementRadioButton = new JRadioButton();
        movementEffectSpinner = new JSpinner();
        backMovementEffectLabel = new JLabel();
        backMovementEffectRadioButton = new JRadioButton();
        backMovementEffectSpinner = new JSpinner();
        shadowSizeTextField = new JLabel();
        shadowSizeComboBox = new JComboBox<>();
        separator5 = new JSeparator();
        savePositionChangesButton = new JButton();

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

        //---- button1 ----
        button1.setText("New Sprite");
        button1.setEnabled(false);
        add(button1, "cell 2 0");

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
            normalMaleFrontButton.addActionListener(e -> normalMaleFrontButtonActionPerformed(e));
            battleSpritePanel.add(normalMaleFrontButton, "cell 1 3,alignx center,growx 0");

            //---- normalMaleBackButton ----
            normalMaleBackButton.setToolTipText("Male Back Sprite");
            normalMaleBackButton.addActionListener(e -> normalMaleBackButtonActionPerformed(e));
            battleSpritePanel.add(normalMaleBackButton, "cell 2 3,alignx center,growx 0");

            //---- shinyMaleFrontButton ----
            shinyMaleFrontButton.setToolTipText("Shiny Male Front Sprite");
            shinyMaleFrontButton.addActionListener(e -> shinyMaleFrontButtonActionPerformed(e));
            battleSpritePanel.add(shinyMaleFrontButton, "cell 3 3,alignx center,growx 0");

            //---- shinyMaleBackButton ----
            shinyMaleBackButton.setToolTipText("Shiny Male Back Sprite");
            shinyMaleBackButton.addActionListener(e -> shinyMaleBackButtonActionPerformed(e));
            battleSpritePanel.add(shinyMaleBackButton, "cell 4 3,alignx center,growx 0");

            //---- femaleLabel ----
            femaleLabel.setText("Female");
            battleSpritePanel.add(femaleLabel, "cell 0 4,alignx center,growx 0");

            //---- normalFemaleFrontButton ----
            normalFemaleFrontButton.setToolTipText("Female Front Sprite");
            normalFemaleFrontButton.addActionListener(e -> normalFemaleFrontButtonActionPerformed(e));
            battleSpritePanel.add(normalFemaleFrontButton, "cell 1 4,alignx center,growx 0");

            //---- normalFemaleBackButton ----
            normalFemaleBackButton.setToolTipText("Female Back Sprite");
            normalFemaleBackButton.addActionListener(e -> normalFemaleBackButtonActionPerformed(e));
            battleSpritePanel.add(normalFemaleBackButton, "cell 2 4,alignx center,growx 0");

            //---- shinyFemaleFrontButton ----
            shinyFemaleFrontButton.setToolTipText("Shiny Female Front Sprite");
            shinyFemaleFrontButton.addActionListener(e -> shinyFemaleFrontButtonActionPerformed(e));
            battleSpritePanel.add(shinyFemaleFrontButton, "cell 3 4,alignx center,growx 0");

            //---- shinyFemaleBackButton ----
            shinyFemaleBackButton.setToolTipText("Shiny Female Back Sprite");
            shinyFemaleBackButton.addActionListener(e -> shinyFemaleBackButtonActionPerformed(e));
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

                //---- normalToShinyButton ----
                normalToShinyButton.setText("Set to Shiny");
                normalToShinyButton.addActionListener(e -> normalToShinyButtonActionPerformed(e));
                palettePanel.add(normalToShinyButton, "cell 0 9 2 1");

                //---- shinyToNormalButton ----
                shinyToNormalButton.setText("Set to Normal");
                shinyToNormalButton.addActionListener(e -> shinyToNormalButtonActionPerformed(e));
                palettePanel.add(shinyToNormalButton, "cell 5 9 2 1");

                //---- swapPalettesButton ----
                swapPalettesButton.setText("Swap Palettes");
                swapPalettesButton.addActionListener(e -> swapPalettesButtonActionPerformed(e));
                palettePanel.add(swapPalettesButton, "cell 0 10 7 1");
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
                "[]" +
                "[grow]" +
                "[grow]"));

            //---- mockupPanel ----
            mockupPanel.setBorder(new TitledBorder("Battle Mockup"));
            mockupPanel.setMinimumSize(new Dimension(256, 192));
            battlePanel.add(mockupPanel, "cell 0 0 2 1,grow");

            //---- genderToggleButton ----
            genderToggleButton.setText("Toggle Gender");
            genderToggleButton.addActionListener(e -> femaleToggleButtonActionPerformed(e));
            battlePanel.add(genderToggleButton, "cell 0 1 2 1,growy");

            //---- shinyToggleButton ----
            shinyToggleButton.setText("Toggle Shiny");
            shinyToggleButton.setMinimumSize(new Dimension(124, 30));
            shinyToggleButton.addActionListener(e -> shinyToggleButtonActionPerformed(e));
            battlePanel.add(shinyToggleButton, "cell 0 1 2 1,growy");

            //---- frameToggleButton2 ----
            frameToggleButton2.setText("Toggle Frame");
            frameToggleButton2.setMinimumSize(new Dimension(124, 30));
            frameToggleButton2.addActionListener(e -> frameToggleButtonActionPerformed(e));
            battlePanel.add(frameToggleButton2, "cell 0 2 2 1");

            //======== panel1 ========
            {
                panel1.setLayout(new MigLayout(
                    "hidemode 3",
                    // columns
                    "[grow,fill]" +
                    "[fill]",
                    // rows
                    "[grow]" +
                    "[grow]" +
                    "[grow]" +
                    "[grow]" +
                    "[grow]" +
                    "[grow]" +
                    "[grow]" +
                    "[grow]" +
                    "[grow]" +
                    "[]" +
                    "[]" +
                    "[]" +
                    "[]"));
                panel1.add(separator4, "cell 0 0 2 1");

                //---- globalFrontYLabel ----
                globalFrontYLabel.setText("Global Front Y");
                panel1.add(globalFrontYLabel, "cell 0 1");

                //---- globalFrontYSpinner ----
                globalFrontYSpinner.setModel(new SpinnerNumberModel(0, -128, 127, 1));
                globalFrontYSpinner.addChangeListener(e -> globalFrontYSpinnerStateChanged(e));
                panel1.add(globalFrontYSpinner, "cell 1 1");

                //---- shadowXLabel ----
                shadowXLabel.setText("Shadow X");
                panel1.add(shadowXLabel, "cell 0 2");

                //---- shadowXSpinner ----
                shadowXSpinner.setModel(new SpinnerNumberModel(0, -128, 127, 1));
                shadowXSpinner.addChangeListener(e -> shadowXSpinnerStateChanged(e));
                panel1.add(shadowXSpinner, "cell 1 2");
                panel1.add(separator2, "cell 0 3 2 1");

                //---- femaleBackYLabel ----
                femaleBackYLabel.setText("Female Back -Y");
                panel1.add(femaleBackYLabel, "cell 0 4");

                //---- femaleBackYSpinner ----
                femaleBackYSpinner.setModel(new SpinnerNumberModel(0, -255, 0, 1));
                femaleBackYSpinner.addChangeListener(e -> femaleBackYSpinnerStateChanged(e));
                panel1.add(femaleBackYSpinner, "cell 1 4");

                //---- maleBackYLabel ----
                maleBackYLabel.setText("Male Back -Y");
                panel1.add(maleBackYLabel, "cell 0 5");

                //---- maleBackYSpinner ----
                maleBackYSpinner.setModel(new SpinnerNumberModel(0, -255, 0, 1));
                maleBackYSpinner.addChangeListener(e -> maleBackYSpinnerStateChanged(e));
                panel1.add(maleBackYSpinner, "cell 1 5");

                //---- femaleFrontYLabel ----
                femaleFrontYLabel.setText("Female Front -Y");
                panel1.add(femaleFrontYLabel, "cell 0 6");

                //---- femaleFrontYSpinner ----
                femaleFrontYSpinner.setModel(new SpinnerNumberModel(0, -255, 0, 1));
                femaleFrontYSpinner.addChangeListener(e -> femaleFrontYSpinnerStateChanged(e));
                panel1.add(femaleFrontYSpinner, "cell 1 6");

                //---- maleFrontYLabel ----
                maleFrontYLabel.setText("Male Front -Y");
                panel1.add(maleFrontYLabel, "cell 0 7");

                //---- maleFrontYSpinner ----
                maleFrontYSpinner.setModel(new SpinnerNumberModel(0, -255, 0, 1));
                maleFrontYSpinner.addChangeListener(e -> maleFrontYSpinnerStateChanged(e));
                panel1.add(maleFrontYSpinner, "cell 1 7");
                panel1.add(separator3, "cell 0 8 2 1");

                //---- movementEffectLabel ----
                movementEffectLabel.setText("Front Movement");
                panel1.add(movementEffectLabel, "cell 0 9");

                //---- movementRadioButton ----
                movementRadioButton.addActionListener(e -> movementRadioButtonActionPerformed(e));
                panel1.add(movementRadioButton, "cell 0 9");

                //---- movementEffectSpinner ----
                movementEffectSpinner.setModel(new SpinnerNumberModel(0, 0, null, 1));
                movementEffectSpinner.addChangeListener(e -> movementEffectSpinnerStateChanged(e));
                panel1.add(movementEffectSpinner, "cell 1 9");

                //---- backMovementEffectLabel ----
                backMovementEffectLabel.setText("Back Movement");
                panel1.add(backMovementEffectLabel, "cell 0 10");
                panel1.add(backMovementEffectRadioButton, "cell 0 10");

                //---- backMovementEffectSpinner ----
                backMovementEffectSpinner.setModel(new SpinnerNumberModel(0, 0, null, 1));
                backMovementEffectSpinner.addChangeListener(e -> backMovementEffectSpinnerStateChanged(e));
                panel1.add(backMovementEffectSpinner, "cell 1 10");

                //---- shadowSizeTextField ----
                shadowSizeTextField.setText("Shadow Size");
                panel1.add(shadowSizeTextField, "cell 0 11");

                //---- shadowSizeComboBox ----
                shadowSizeComboBox.addActionListener(e -> shadowSizeComboBoxActionPerformed(e));
                panel1.add(shadowSizeComboBox, "cell 1 11");
                panel1.add(separator5, "cell 0 12 2 1");
            }
            battlePanel.add(panel1, "cell 0 3 2 2,growy");

            //---- savePositionChangesButton ----
            savePositionChangesButton.setText("Save Position Changes");
            savePositionChangesButton.addActionListener(e -> savePositionChangesButtonActionPerformed(e));
            battlePanel.add(savePositionChangesButton, "cell 0 5 2 1,aligny bottom,growy 0");
        }
        add(battlePanel, "cell 1 1 2 1,grow");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private EditorComboBox speciesChooserComboBox;
    private JButton saveChangesButton;
    private JButton revertChangesButton;
    private JButton button1;
    private JPanel battleSpritePanel;
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
    private JButton normalToShinyButton;
    private JButton shinyToNormalButton;
    private JButton swapPalettesButton;
    private JPanel battlePanel;
    private PokemonSpritePanel.BattleMockupPanel mockupPanel;
    private JToggleButton genderToggleButton;
    private JToggleButton shinyToggleButton;
    private JToggleButton frameToggleButton2;
    private JPanel panel1;
    private JSeparator separator4;
    private JLabel globalFrontYLabel;
    private JSpinner globalFrontYSpinner;
    private JLabel shadowXLabel;
    private JSpinner shadowXSpinner;
    private JSeparator separator2;
    private JLabel femaleBackYLabel;
    private JSpinner femaleBackYSpinner;
    private JLabel maleBackYLabel;
    private JSpinner maleBackYSpinner;
    private JLabel femaleFrontYLabel;
    private JSpinner femaleFrontYSpinner;
    private JLabel maleFrontYLabel;
    private JSpinner maleFrontYSpinner;
    private JSeparator separator3;
    private JLabel movementEffectLabel;
    private JRadioButton movementRadioButton;
    private JSpinner movementEffectSpinner;
    private JLabel backMovementEffectLabel;
    private JRadioButton backMovementEffectRadioButton;
    private JSpinner backMovementEffectSpinner;
    private JLabel shadowSizeTextField;
    private JComboBox<SpriteDataProcessor.ShadowType> shadowSizeComboBox;
    private JSeparator separator5;
    private JButton savePositionChangesButton;
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

    static class PreviewPanel extends JPanel
    {
        Color currentColor;
        SpriteImage maleFront;
        SpriteImage femaleFront;
        SpriteImage maleBack;
        SpriteImage femaleBack;

        public PreviewPanel(JColorChooser chooser, SpriteImage maleFront, SpriteImage maleBack, SpriteImage femaleFront, SpriteImage femaleBack)
        {
            currentColor= chooser.getColor();
            this.maleFront= maleFront.copyOfSelf();
            this.maleBack= maleBack.copyOfSelf();
            this.femaleFront = femaleFront.copyOfSelf();
            this.femaleBack= femaleBack.copyOfSelf();
            setPreferredSize(new Dimension(700,200));
        }

        @Override
        public void paint(Graphics g) {
            g.setColor(Color.RED);
            g.drawString("RGB values must be a multiple of 8. Maximum value allowed is 248.", getWidth()/4 - 40,10);
            g.drawString("Male",getWidth()/4 - 15,195);
            g.drawString("Female",(getWidth()/4)*3 - 20,195);
            g.setColor(currentColor);
            g.drawImage(maleFront.getResizedImage(),getWidth()/2 - maleFront.getResizedImage().getWidth()*2 - 20,20,this);
            g.drawImage(maleBack.getResizedImage(), getWidth()/2 - maleBack.getResizedImage().getWidth() - 10,20,this);
            g.drawImage(femaleFront.getResizedImage(),getWidth()/2 + 10,20,this);
            g.drawImage(femaleBack.getResizedImage(), getWidth()/2 + femaleBack.getResizedImage().getWidth() + 20,20,this);
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
        static Image genderIcon;


        static byte frontYOffset;
        static byte shadowXOffset;
        static SpriteDataProcessor.ShadowType shadowType;


        static int frontModifier;
        static int backModifier;

        static boolean isFemale;

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
            try
            {
                //The origin (top left) is 0, 0
                Graphics2D g2d= (Graphics2D) g;
                g2d.drawImage(background,0,0, null);
                g2d.drawImage(enemyPlatform,129,72,null);
                g2d.drawImage(playerPlatform,-42,122,null);


                if(shadowType == SpriteDataProcessor.ShadowType.Small)
                {
                    g2d.drawImage(shadow,179 + shadowXOffset, 83, null);
                }
                else if(shadowType == SpriteDataProcessor.ShadowType.Medium)
                {
                    g2d.drawImage(shadow,174 + shadowXOffset, 83, null);
                }
                else if(shadowType == SpriteDataProcessor.ShadowType.Large)
                {
                    g2d.drawImage(shadow,167 + shadowXOffset, 82, null);
                }


                if(frontSprite != null)
                {
                    g2d.drawImage(frontSprite,152,10 - frontYOffset - frontModifier,null);
                    g2d.drawImage(backSprite,23,72 - backModifier,null);
                }

                g2d.drawImage(playerHealth,129,95, null);
                g2d.drawImage(enemyHealth,0,18, null);

                g2d.drawImage(genderIcon,217,103,null);
                g2d.drawImage(genderIcon,65,25, null);

                g2d.drawImage(textBar,0,getHeight()-48, null);
                g2d.dispose();
            }
            catch (NullPointerException exception)
            {
                exception.printStackTrace();
            }

        }

        public void setSprites(SpriteImage newFront, SpriteImage newBack, byte newFrontYOffset, byte newShadowXOffset, SpriteDataProcessor.ShadowType newShadowType, int frontMod, int backMod, boolean female)
        {
            frontSprite= newFront.getTransparentImage();
            backSprite= newBack.getTransparentImage();
            frontYOffset= newFrontYOffset;
            shadowXOffset= newShadowXOffset;
            shadowType= newShadowType;

            frontModifier= frontMod;
            backModifier= backMod;

            switch (newShadowType)
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

            isFemale= female;

            if(female)
            {
                genderIcon= new ImageIcon(BattleMockupPanel.class.getResource("/icons/symbol_female.png")).getImage();
            }
            else
            {
                genderIcon= new ImageIcon(BattleMockupPanel.class.getResource("/icons/symbol_male.png")).getImage();
            }
            repaint();
        }
    }

    static class CopyImageToClipboard implements ClipboardOwner
    {
        public CopyImageToClipboard()
        {
        }

        public void copyImage(BufferedImage image)
        {
            TransferableImage trans= new TransferableImage(image);
            Clipboard c= Toolkit.getDefaultToolkit().getSystemClipboard();
            c.setContents(trans,this);
        }


        @Override
        public void lostOwnership(Clipboard clipboard, Transferable contents)
        {

        }

        private static class TransferableImage implements Transferable
        {
            Image i;

            public TransferableImage(Image i)
            {
                this.i= i;
            }

            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException
            {
                if(flavor.equals(DataFlavor.imageFlavor) && i != null)
                {
                    return i;
                }
                else
                {
                    throw new UnsupportedFlavorException(flavor);
                }
            }

            public DataFlavor[] getTransferDataFlavors()
            {
                DataFlavor[] flavors= new DataFlavor[1];
                flavors[0]= DataFlavor.imageFlavor;
                return flavors;
            }

            public boolean isDataFlavorSupported(DataFlavor flavor)
            {
                DataFlavor[] flavors= getTransferDataFlavors();
                for (DataFlavor dataFlavor : flavors)
                {
                    if (flavor.equals(dataFlavor))
                    {
                        return true;
                    }
                }

                return false;
            }
        }
    }

    public SpriteImage getImageFromClipboard()
    {
        System.out.println("Getting image");
        Transferable transferable= Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        if(transferable != null && transferable.isDataFlavorSupported(DataFlavor.imageFlavor))
        {
            try
            {
                return new SpriteImage((Image) transferable.getTransferData(DataFlavor.imageFlavor),this);
            }
            catch (UnsupportedFlavorException | IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            System.err.println("Image from clipboard: wasn't an image!");
        }

        return new SpriteImage(new BufferedImage(80,80,BufferedImage.TYPE_INT_RGB),this);
    }


}
