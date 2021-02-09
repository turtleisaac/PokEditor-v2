/*
 * Created by JFormDesigner on Tue Feb 02 16:47:18 EST 2021
 */

package com.turtleisaac.pokeditor.gui.projects.projectwindow.editors.sprites.pokemon;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.*;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.*;

import com.turtleisaac.pokeditor.editors.narctowl.Narctowl;
import com.turtleisaac.pokeditor.project.Game;
import com.turtleisaac.pokeditor.project.Project;
import com.turtleisaac.pokeditor.utilities.images.ImageDecrypter;
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
    ArrayList<File> toDelete;

    private PokemonSprites sprites;
    private PokemonSprites backupSprites;

    private Project project;
    private Color[] palette;
    private Color[] shinyPalette;

    public PokemonSpritePanel()
    {

        initComponents();
        toDelete= new ArrayList<>();
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

        JFrame colorFrame= new JFrame();
        colorFrame.setLocationRelativeTo(this);
        JColorChooser colorChooser= new JColorChooser(shiny ? shinyPalette[num] : palette[num]);

        PreviewPanel previewPanel= new PreviewPanel(colorChooser,
                shiny ? sprites.getShinyMaleFront()[frameNum] : sprites.getMaleFront()[frameNum],
                shiny ? sprites.getShinyMaleBack()[frameNum] : sprites.getMaleBack()[frameNum],
                shiny ? sprites.getShinyFemaleFront()[frameNum] : sprites.getFemaleFront()[frameNum],
                shiny ? sprites.getShinyFemaleBack()[frameNum] : sprites.getFemaleBack()[frameNum]);

        ColorSelectionModel model = colorChooser.getSelectionModel();
        model.addChangeListener(evt ->
        {
            ColorSelectionModel model1 = (ColorSelectionModel) evt.getSource();
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
        });

        colorChooser.setPreviewPanel(previewPanel);
        AbstractColorChooserPanel[] chooserPanels= colorChooser.getChooserPanels();
        colorChooser.setChooserPanels(new AbstractColorChooserPanel[] {chooserPanels[3], chooserPanels[0]});

        colorFrame.setContentPane(colorChooser);
        colorFrame.setPreferredSize(new Dimension(660,400));
        colorFrame.setTitle("Species " + speciesChooserComboBox.getValue() + " - " + (shiny ? "Shiny" : "Normal") + " Palette - Color " + num);
        colorFrame.pack();
        colorFrame.setVisible(true);
        colorFrame.toFront();

//        colorFrame.addWindowListener(new WindowAdapter()
//        {
//            @Override
//            public void windowClosing(WindowEvent e)
//            {
//                if(JOptionPane.showConfirmDialog(colorFrame,"Are you sure?","PokEditor",JOptionPane.YES_NO_OPTION) == 0)
//                {
//                    if(shiny)
//                    {
//                        shinyMaleFrontButton.setIcon(new ImageIcon(previewPanel.maleImage));
//                        shinyMaleBackButton.setIcon(new ImageIcon(previewPanel.maleImage));
//
//                        switch (num)
//                        {
//                            case 0:
//                                shinyPaletteButton0.setIcon(new ImageIcon(getPaletteImage(newColor)));
//                                break;
//                            case 1:
//                                shinyPaletteButton1.setIcon(new ImageIcon(getPaletteImage(newColor)));
//                                break;
//                            case 2:
//                                shinyPaletteButton2.setIcon(new ImageIcon(getPaletteImage(newColor)));
//                                break;
//                            case 3:
//                                shinyPaletteButton3.setIcon(new ImageIcon(getPaletteImage(newColor)));
//                                break;
//                            case 4:
//                                shinyPaletteButton4.setIcon(new ImageIcon(getPaletteImage(newColor)));
//                                break;
//                            case 5:
//                                shinyPaletteButton5.setIcon(new ImageIcon(getPaletteImage(newColor)));
//                                break;
//                            case 6:
//                                shinyPaletteButton6.setIcon(new ImageIcon(getPaletteImage(newColor)));
//                                break;
//                            case 7:
//                                shinyPaletteButton7.setIcon(new ImageIcon(getPaletteImage(newColor)));
//                                break;
//                            case 8:
//                                shinyPaletteButton8.setIcon(new ImageIcon(getPaletteImage(newColor)));
//                                break;
//                            case 9:
//                                shinyPaletteButton9.setIcon(new ImageIcon(getPaletteImage(newColor)));
//                                break;
//                            case 10:
//                                shinyPaletteButton10.setIcon(new ImageIcon(getPaletteImage(newColor)));
//                                break;
//                            case 11:
//                                shinyPaletteButton11.setIcon(new ImageIcon(getPaletteImage(newColor)));
//                                break;
//                            case 12:
//                                shinyPaletteButton12.setIcon(new ImageIcon(getPaletteImage(newColor)));
//                                break;
//                            case 13:
//                                shinyPaletteButton13.setIcon(new ImageIcon(getPaletteImage(newColor)));
//                                break;
//                            case 14:
//                                shinyPaletteButton14.setIcon(new ImageIcon(getPaletteImage(newColor)));
//                                break;
//                            case 15:
//                                shinyPaletteButton15.setIcon(new ImageIcon(getPaletteImage(newColor)));
//                                break;
//                        }
//
//                        shinyPalette[num]= newColor;
//                    }
//                    else
//                    {
//                        normalMaleFrontButton.setIcon(new ImageIcon(previewPanel.maleImage));
//                        normalMaleBackButton.setIcon(new ImageIcon(previewPanel.maleImage));
//
//                        switch (num)
//                        {
//                            case 0:
//                                normalPaletteButton0.setIcon(new ImageIcon(getPaletteImage(newColor)));
//                                break;
//                            case 1:
//                                normalPaletteButton1.setIcon(new ImageIcon(getPaletteImage(newColor)));
//                                break;
//                            case 2:
//                                normalPaletteButton2.setIcon(new ImageIcon(getPaletteImage(newColor)));
//                                break;
//                            case 3:
//                                normalPaletteButton3.setIcon(new ImageIcon(getPaletteImage(newColor)));
//                                break;
//                            case 4:
//                                normalPaletteButton4.setIcon(new ImageIcon(getPaletteImage(newColor)));
//                                break;
//                            case 5:
//                                normalPaletteButton5.setIcon(new ImageIcon(getPaletteImage(newColor)));
//                                break;
//                            case 6:
//                                normalPaletteButton6.setIcon(new ImageIcon(getPaletteImage(newColor)));
//                                break;
//                            case 7:
//                                normalPaletteButton7.setIcon(new ImageIcon(getPaletteImage(newColor)));
//                                break;
//                            case 8:
//                                normalPaletteButton8.setIcon(new ImageIcon(getPaletteImage(newColor)));
//                                break;
//                            case 9:
//                                normalPaletteButton9.setIcon(new ImageIcon(getPaletteImage(newColor)));
//                                break;
//                            case 10:
//                                normalPaletteButton10.setIcon(new ImageIcon(getPaletteImage(newColor)));
//                                break;
//                            case 11:
//                                normalPaletteButton11.setIcon(new ImageIcon(getPaletteImage(newColor)));
//                                break;
//                            case 12:
//                                normalPaletteButton12.setIcon(new ImageIcon(getPaletteImage(newColor)));
//                                break;
//                            case 13:
//                                normalPaletteButton13.setIcon(new ImageIcon(getPaletteImage(newColor)));
//                                break;
//                            case 14:
//                                normalPaletteButton14.setIcon(new ImageIcon(getPaletteImage(newColor)));
//                                break;
//                            case 15:
//                                normalPaletteButton15.setIcon(new ImageIcon(getPaletteImage(newColor)));
//                                break;
//                        }
//
//                        palette[num]= newColor;
//                    }
//                }
//            }
//        });

    }

    private void saveChangesButtonActionPerformed(ActionEvent e) {
//        normalMaleFrontBackup = copyOfImage((BufferedImage) normalMaleFront);
//        normalMaleBackBackup = copyOfImage((BufferedImage) normalMaleBack);
//        shinyMaleFrontBackup = copyOfImage((BufferedImage) shinyMaleFront);
//        shinyMaleBackBackup = copyOfImage((BufferedImage) shinyMaleBack);
    }

    private void revertChangesButtonActionPerformed(ActionEvent e)
    {
        frameToggleButton.setSelected(false);

        BufferedImage[] femaleBack= Arrays.copyOf(backupSprites.getFemaleBack(),2);
        BufferedImage[] shinyFemaleBack= Arrays.copyOf(backupSprites.getShinyFemaleBack(),2);
        BufferedImage[] femaleFront= Arrays.copyOf(backupSprites.getFemaleFront(),2);
        BufferedImage[] shinyFemaleFront= Arrays.copyOf(backupSprites.getShinyFemaleFront(),2);

        BufferedImage[] maleBack= Arrays.copyOf(backupSprites.getMaleBack(),2);
        BufferedImage[] shinyMaleBack= Arrays.copyOf(backupSprites.getShinyMaleBack(),2);
        BufferedImage[] maleFront= Arrays.copyOf(backupSprites.getMaleFront(),2);
        BufferedImage[] shinyMaleFront= Arrays.copyOf(backupSprites.getShinyMaleFront(),2);

        palette= Arrays.copyOf(backupSprites.getPalette(),16);
        shinyPalette= Arrays.copyOf(backupSprites.getShinyPalette(),16);

        backupSprites= new PokemonSprites()
        {
            @Override
            public BufferedImage[] getFemaleBack()
            {
                return femaleBack;
            }

            @Override
            public BufferedImage[] getShinyFemaleBack()
            {
                return shinyFemaleBack;
            }

            @Override
            public BufferedImage[] getMaleBack()
            {
                return maleBack;
            }

            @Override
            public BufferedImage[] getShinyMaleBack()
            {
                return shinyMaleBack;
            }

            @Override
            public BufferedImage[] getFemaleFront()
            {
                return femaleFront;
            }

            @Override
            public BufferedImage[] getShinyFemaleFront()
            {
                return shinyFemaleFront;
            }

            @Override
            public BufferedImage[] getMaleFront()
            {
                return maleFront;
            }

            @Override
            public BufferedImage[] getShinyMaleFront()
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

        normalFemaleFrontButton.setIcon(new ImageIcon(sprites.getFemaleFront()[0]));
        normalFemaleBackButton.setIcon(new ImageIcon(sprites.getFemaleBack()[0]));
        shinyFemaleFrontButton.setIcon(new ImageIcon(sprites.getShinyFemaleFront()[0]));
        shinyFemaleBackButton.setIcon(new ImageIcon(sprites.getShinyFemaleBack()[0]));

        normalMaleFrontButton.setIcon(new ImageIcon(sprites.getMaleFront()[0]));
        normalMaleBackButton.setIcon(new ImageIcon(sprites.getMaleBack()[0]));
        shinyMaleFrontButton.setIcon(new ImageIcon(sprites.getShinyMaleFront()[0]));
        shinyMaleBackButton.setIcon(new ImageIcon(sprites.getShinyMaleBack()[0]));


        normalPaletteButton0.setIcon(new ImageIcon(getPaletteImage(palette[0])));
        normalPaletteButton1.setIcon(new ImageIcon(getPaletteImage(palette[1])));
        normalPaletteButton2.setIcon(new ImageIcon(getPaletteImage(palette[2])));
        normalPaletteButton3.setIcon(new ImageIcon(getPaletteImage(palette[3])));
        normalPaletteButton4.setIcon(new ImageIcon(getPaletteImage(palette[4])));
        normalPaletteButton5.setIcon(new ImageIcon(getPaletteImage(palette[5])));
        normalPaletteButton6.setIcon(new ImageIcon(getPaletteImage(palette[6])));
        normalPaletteButton7.setIcon(new ImageIcon(getPaletteImage(palette[7])));
        normalPaletteButton8.setIcon(new ImageIcon(getPaletteImage(palette[8])));
        normalPaletteButton9.setIcon(new ImageIcon(getPaletteImage(palette[9])));
        normalPaletteButton10.setIcon(new ImageIcon(getPaletteImage(palette[10])));
        normalPaletteButton11.setIcon(new ImageIcon(getPaletteImage(palette[11])));
        normalPaletteButton12.setIcon(new ImageIcon(getPaletteImage(palette[12])));
        normalPaletteButton13.setIcon(new ImageIcon(getPaletteImage(palette[13])));
        normalPaletteButton14.setIcon(new ImageIcon(getPaletteImage(palette[14])));
        normalPaletteButton15.setIcon(new ImageIcon(getPaletteImage(palette[15])));

        shinyPaletteButton0.setIcon(new ImageIcon(getPaletteImage(shinyPalette[0])));
        shinyPaletteButton1.setIcon(new ImageIcon(getPaletteImage(shinyPalette[1])));
        shinyPaletteButton2.setIcon(new ImageIcon(getPaletteImage(shinyPalette[2])));
        shinyPaletteButton3.setIcon(new ImageIcon(getPaletteImage(shinyPalette[3])));
        shinyPaletteButton4.setIcon(new ImageIcon(getPaletteImage(shinyPalette[4])));
        shinyPaletteButton5.setIcon(new ImageIcon(getPaletteImage(shinyPalette[5])));
        shinyPaletteButton6.setIcon(new ImageIcon(getPaletteImage(shinyPalette[6])));
        shinyPaletteButton7.setIcon(new ImageIcon(getPaletteImage(shinyPalette[7])));
        shinyPaletteButton8.setIcon(new ImageIcon(getPaletteImage(shinyPalette[8])));
        shinyPaletteButton9.setIcon(new ImageIcon(getPaletteImage(shinyPalette[9])));
        shinyPaletteButton10.setIcon(new ImageIcon(getPaletteImage(shinyPalette[10])));
        shinyPaletteButton11.setIcon(new ImageIcon(getPaletteImage(shinyPalette[11])));
        shinyPaletteButton12.setIcon(new ImageIcon(getPaletteImage(shinyPalette[12])));
        shinyPaletteButton13.setIcon(new ImageIcon(getPaletteImage(shinyPalette[13])));
        shinyPaletteButton14.setIcon(new ImageIcon(getPaletteImage(shinyPalette[14])));
        shinyPaletteButton15.setIcon(new ImageIcon(getPaletteImage(shinyPalette[15])));
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
        String dataPath= project.getProjectPath().getAbsolutePath() + File.separator + project.getName() + "/data";
        int selected= (int)(speciesChooserComboBox.getValue())*6;

        boolean primary= false;

        File narcPath= new File(dataPath + (Project.isDPPT(project) ? project.getBaseRom() == Game.Platinum ? "/poketool/pokegra/pl_pokegra.narc" : "/poketool/pokegra/pokegra.narc" : "/a/0/0/4"));
        File pokegraPath= new File(dataPath + (Project.isDPPT(project) ? project.getBaseRom() == Game.Platinum ? "/poketool/pokegra/pl_pokegra" : "/poketool/pokegra/pokegra" : "/a/0/0/4_"));

        primary= Project.isPrimary(project);

        try
        {
            if(!pokegraPath.exists())
            {
                Narctowl narctowl = new Narctowl(true);
                narctowl.unpack(narcPath.getAbsolutePath(), pokegraPath.getAbsolutePath());
            }
            toDelete.add(pokegraPath);
            pokegraPath.deleteOnExit();
        }
        catch (IOException ignored)
        {
        }

        sprites= ImageDecrypter.getSprites(pokegraPath.getAbsolutePath(),selected,primary);

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

        backupSprites= new PokemonSprites()
        {
            @Override
            public BufferedImage[] getFemaleBack()
            {
                return femaleBack;
            }

            @Override
            public BufferedImage[] getShinyFemaleBack()
            {
                return shinyFemaleBack;
            }

            @Override
            public BufferedImage[] getMaleBack()
            {
                return maleBack;
            }

            @Override
            public BufferedImage[] getShinyMaleBack()
            {
                return shinyMaleBack;
            }

            @Override
            public BufferedImage[] getFemaleFront()
            {
                return femaleFront;
            }

            @Override
            public BufferedImage[] getShinyFemaleFront()
            {
                return shinyFemaleFront;
            }

            @Override
            public BufferedImage[] getMaleFront()
            {
                return maleFront;
            }

            @Override
            public BufferedImage[] getShinyMaleFront()
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




        normalFemaleFrontButton.setIcon(new ImageIcon(sprites.getFemaleFront()[0]));
        normalFemaleBackButton.setIcon(new ImageIcon(sprites.getFemaleBack()[0]));
        shinyFemaleFrontButton.setIcon(new ImageIcon(sprites.getShinyFemaleFront()[0]));
        shinyFemaleBackButton.setIcon(new ImageIcon(sprites.getShinyFemaleBack()[0]));

        normalMaleFrontButton.setIcon(new ImageIcon(sprites.getMaleFront()[0]));
        normalMaleBackButton.setIcon(new ImageIcon(sprites.getMaleBack()[0]));
        shinyMaleFrontButton.setIcon(new ImageIcon(sprites.getShinyMaleFront()[0]));
        shinyMaleBackButton.setIcon(new ImageIcon(sprites.getShinyMaleBack()[0]));


        normalPaletteButton0.setIcon(new ImageIcon(getPaletteImage(palette[0])));
        normalPaletteButton1.setIcon(new ImageIcon(getPaletteImage(palette[1])));
        normalPaletteButton2.setIcon(new ImageIcon(getPaletteImage(palette[2])));
        normalPaletteButton3.setIcon(new ImageIcon(getPaletteImage(palette[3])));
        normalPaletteButton4.setIcon(new ImageIcon(getPaletteImage(palette[4])));
        normalPaletteButton5.setIcon(new ImageIcon(getPaletteImage(palette[5])));
        normalPaletteButton6.setIcon(new ImageIcon(getPaletteImage(palette[6])));
        normalPaletteButton7.setIcon(new ImageIcon(getPaletteImage(palette[7])));
        normalPaletteButton8.setIcon(new ImageIcon(getPaletteImage(palette[8])));
        normalPaletteButton9.setIcon(new ImageIcon(getPaletteImage(palette[9])));
        normalPaletteButton10.setIcon(new ImageIcon(getPaletteImage(palette[10])));
        normalPaletteButton11.setIcon(new ImageIcon(getPaletteImage(palette[11])));
        normalPaletteButton12.setIcon(new ImageIcon(getPaletteImage(palette[12])));
        normalPaletteButton13.setIcon(new ImageIcon(getPaletteImage(palette[13])));
        normalPaletteButton14.setIcon(new ImageIcon(getPaletteImage(palette[14])));
        normalPaletteButton15.setIcon(new ImageIcon(getPaletteImage(palette[15])));

        shinyPaletteButton0.setIcon(new ImageIcon(getPaletteImage(shinyPalette[0])));
        shinyPaletteButton1.setIcon(new ImageIcon(getPaletteImage(shinyPalette[1])));
        shinyPaletteButton2.setIcon(new ImageIcon(getPaletteImage(shinyPalette[2])));
        shinyPaletteButton3.setIcon(new ImageIcon(getPaletteImage(shinyPalette[3])));
        shinyPaletteButton4.setIcon(new ImageIcon(getPaletteImage(shinyPalette[4])));
        shinyPaletteButton5.setIcon(new ImageIcon(getPaletteImage(shinyPalette[5])));
        shinyPaletteButton6.setIcon(new ImageIcon(getPaletteImage(shinyPalette[6])));
        shinyPaletteButton7.setIcon(new ImageIcon(getPaletteImage(shinyPalette[7])));
        shinyPaletteButton8.setIcon(new ImageIcon(getPaletteImage(shinyPalette[8])));
        shinyPaletteButton9.setIcon(new ImageIcon(getPaletteImage(shinyPalette[9])));
        shinyPaletteButton10.setIcon(new ImageIcon(getPaletteImage(shinyPalette[10])));
        shinyPaletteButton11.setIcon(new ImageIcon(getPaletteImage(shinyPalette[11])));
        shinyPaletteButton12.setIcon(new ImageIcon(getPaletteImage(shinyPalette[12])));
        shinyPaletteButton13.setIcon(new ImageIcon(getPaletteImage(shinyPalette[13])));
        shinyPaletteButton14.setIcon(new ImageIcon(getPaletteImage(shinyPalette[14])));
        shinyPaletteButton15.setIcon(new ImageIcon(getPaletteImage(shinyPalette[15])));

        //        ImageIO.write(image,"png",new File(System.getProperty("user.dir") + File.separator + "cynthia.png"));

    }

    private void frameToggleButtonActionPerformed(ActionEvent e)
    {
        int value= frameToggleButton.isSelected() ? 1 : 0;
        frameToggleLabel.setText("" + value);


        normalFemaleFrontButton.setIcon(new ImageIcon(sprites.getFemaleFront()[value]));
        normalFemaleBackButton.setIcon(new ImageIcon(sprites.getFemaleBack()[value]));
        shinyFemaleFrontButton.setIcon(new ImageIcon(sprites.getShinyFemaleFront()[value]));
        shinyFemaleBackButton.setIcon(new ImageIcon(sprites.getShinyFemaleBack()[value]));

        normalMaleFrontButton.setIcon(new ImageIcon(sprites.getMaleFront()[value]));
        normalMaleBackButton.setIcon(new ImageIcon(sprites.getMaleBack()[value]));
        shinyMaleFrontButton.setIcon(new ImageIcon(sprites.getShinyMaleFront()[value]));
        shinyMaleBackButton.setIcon(new ImageIcon(sprites.getShinyMaleBack()[value]));
    }

    private void speciesChooserComboBoxStateChanged(ChangeEvent e) {
        speciesChooserComboBoxActionPerformed(null);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        speciesChooserComboBox = new JSpinner();
        saveChangesButton = new JButton();
        revertChangesButton = new JButton();
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
        panel1 = new JPanel();
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
        panel2 = new JPanel();

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
        speciesChooserComboBox.setModel(new SpinnerNumberModel(0, 0, 493, 1));
        speciesChooserComboBox.addChangeListener(e -> speciesChooserComboBoxStateChanged(e));
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
                "[]"));

            //---- frameToggleLabel ----
            frameToggleLabel.setText("0");
            battleSpritePanel.add(frameToggleLabel, "cell 0 0,alignx right,growx 0");

            //---- frameToggleButton ----
            frameToggleButton.setText("Toggle Frame");
            frameToggleButton.addActionListener(e -> frameToggleButtonActionPerformed(e));
            battleSpritePanel.add(frameToggleButton, "cell 1 0 4 1");

            //---- normalLabel ----
            normalLabel.setText("Normal");
            battleSpritePanel.add(normalLabel, "cell 1 1 2 1,alignx center,growx 0");

            //---- shinyLabel ----
            shinyLabel.setText("Shiny");
            battleSpritePanel.add(shinyLabel, "cell 3 1 2 1,alignx center,growx 0");

            //---- maleLabel ----
            maleLabel.setText("Male");
            battleSpritePanel.add(maleLabel, "cell 0 2,alignx center,growx 0");

            //---- normalMaleFrontButton ----
            normalMaleFrontButton.setToolTipText("Male Front Sprite");
            normalMaleFrontButton.addActionListener(e -> normalFrontButtonActionPerformed(e));
            battleSpritePanel.add(normalMaleFrontButton, "cell 1 2,alignx center,growx 0");

            //---- normalMaleBackButton ----
            normalMaleBackButton.setToolTipText("Male Back Sprite");
            normalMaleBackButton.addActionListener(e -> normalBackButtonActionPerformed(e));
            battleSpritePanel.add(normalMaleBackButton, "cell 2 2,alignx center,growx 0");

            //---- shinyMaleFrontButton ----
            shinyMaleFrontButton.setToolTipText("Shiny Male Front Sprite");
            shinyMaleFrontButton.addActionListener(e -> shinyFrontButtonActionPerformed(e));
            battleSpritePanel.add(shinyMaleFrontButton, "cell 3 2,alignx center,growx 0");

            //---- shinyMaleBackButton ----
            shinyMaleBackButton.setToolTipText("Shiny Male Back Sprite");
            shinyMaleBackButton.addActionListener(e -> shinyBackButtonActionPerformed(e));
            battleSpritePanel.add(shinyMaleBackButton, "cell 4 2,alignx center,growx 0");

            //---- femaleLabel ----
            femaleLabel.setText("Female");
            battleSpritePanel.add(femaleLabel, "cell 0 3,alignx center,growx 0");

            //---- normalFemaleFrontButton ----
            normalFemaleFrontButton.setToolTipText("Female Front Sprite");
            battleSpritePanel.add(normalFemaleFrontButton, "cell 1 3,alignx center,growx 0");

            //---- normalFemaleBackButton ----
            normalFemaleBackButton.setToolTipText("Female Back Sprite");
            battleSpritePanel.add(normalFemaleBackButton, "cell 2 3,alignx center,growx 0");

            //---- shinyFemaleFrontButton ----
            shinyFemaleFrontButton.setToolTipText("Shiny Female Front Sprite");
            battleSpritePanel.add(shinyFemaleFrontButton, "cell 3 3,alignx center,growx 0");

            //---- shinyFemaleBackButton ----
            shinyFemaleBackButton.setToolTipText("Shiny Female Back Sprite");
            battleSpritePanel.add(shinyFemaleBackButton, "cell 4 3,alignx center,growx 0");

            //---- normalFrontLabel ----
            normalFrontLabel.setText("Front");
            battleSpritePanel.add(normalFrontLabel, "cell 1 4,alignx center,growx 0");

            //---- normalBackLabel ----
            normalBackLabel.setText("Back");
            battleSpritePanel.add(normalBackLabel, "cell 2 4,alignx center,growx 0");

            //---- shinyFrontLabel ----
            shinyFrontLabel.setText("Front");
            battleSpritePanel.add(shinyFrontLabel, "cell 3 4,alignx center,growx 0");

            //---- shinyBackLabel ----
            shinyBackLabel.setText("Back");
            battleSpritePanel.add(shinyBackLabel, "cell 4 4,alignx center,growx 0");

            //======== panel1 ========
            {
                panel1.setBorder(new TitledBorder("Palettes"));
                panel1.setLayout(new MigLayout(
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
                panel1.add(label5, "cell 0 0 2 1,alignx center,growx 0");

                //---- label6 ----
                label6.setText("Shiny");
                panel1.add(label6, "cell 5 0 2 1,alignx center,growx 0");

                //---- normalPaletteButton0 ----
                normalPaletteButton0.addActionListener(e -> normalPaletteButton0ActionPerformed(e));
                panel1.add(normalPaletteButton0, "cell 0 1");

                //---- normalPaletteButton1 ----
                normalPaletteButton1.addActionListener(e -> normalPaletteButton1ActionPerformed(e));
                panel1.add(normalPaletteButton1, "cell 1 1");

                //---- normalPaletteButton2 ----
                normalPaletteButton2.addActionListener(e -> normalPaletteButton2ActionPerformed(e));
                panel1.add(normalPaletteButton2, "cell 0 2");

                //---- normalPaletteButton3 ----
                normalPaletteButton3.addActionListener(e -> normalPaletteButton3ActionPerformed(e));
                panel1.add(normalPaletteButton3, "cell 1 2");

                //---- normalPaletteButton4 ----
                normalPaletteButton4.addActionListener(e -> normalPaletteButton4ActionPerformed(e));
                panel1.add(normalPaletteButton4, "cell 0 3");

                //---- normalPaletteButton5 ----
                normalPaletteButton5.addActionListener(e -> normalPaletteButton5ActionPerformed(e));
                panel1.add(normalPaletteButton5, "cell 1 3");

                //---- normalPaletteButton6 ----
                normalPaletteButton6.addActionListener(e -> normalPaletteButton6ActionPerformed(e));
                panel1.add(normalPaletteButton6, "cell 0 4");

                //---- normalPaletteButton7 ----
                normalPaletteButton7.addActionListener(e -> normalPaletteButton7ActionPerformed(e));
                panel1.add(normalPaletteButton7, "cell 1 4");

                //---- normalPaletteButton8 ----
                normalPaletteButton8.addActionListener(e -> normalPaletteButton8ActionPerformed(e));
                panel1.add(normalPaletteButton8, "cell 0 5");

                //---- normalPaletteButton9 ----
                normalPaletteButton9.addActionListener(e -> normalPaletteButton9ActionPerformed(e));
                panel1.add(normalPaletteButton9, "cell 1 5");

                //---- normalPaletteButton10 ----
                normalPaletteButton10.addActionListener(e -> normalPaletteButton10ActionPerformed(e));
                panel1.add(normalPaletteButton10, "cell 0 6");

                //---- normalPaletteButton11 ----
                normalPaletteButton11.addActionListener(e -> normalPaletteButton11ActionPerformed(e));
                panel1.add(normalPaletteButton11, "cell 1 6");

                //---- normalPaletteButton12 ----
                normalPaletteButton12.addActionListener(e -> normalPaletteButton12ActionPerformed(e));
                panel1.add(normalPaletteButton12, "cell 0 7");

                //---- normalPaletteButton13 ----
                normalPaletteButton13.addActionListener(e -> normalPaletteButton13ActionPerformed(e));
                panel1.add(normalPaletteButton13, "cell 1 7");

                //---- normalPaletteButton14 ----
                normalPaletteButton14.addActionListener(e -> normalPaletteButton14ActionPerformed(e));
                panel1.add(normalPaletteButton14, "cell 0 8");

                //---- normalPaletteButton15 ----
                normalPaletteButton15.addActionListener(e -> normalPaletteButton15ActionPerformed(e));
                panel1.add(normalPaletteButton15, "cell 1 8");
                panel1.add(hSpacer1, "cell 2 1");

                //---- separator1 ----
                separator1.setOrientation(SwingConstants.VERTICAL);
                panel1.add(separator1, "cell 3 1 1 8");
                panel1.add(hSpacer2, "cell 4 1");

                //---- shinyPaletteButton0 ----
                shinyPaletteButton0.addActionListener(e -> shinyPaletteButton0ActionPerformed(e));
                panel1.add(shinyPaletteButton0, "cell 5 1");

                //---- shinyPaletteButton1 ----
                shinyPaletteButton1.addActionListener(e -> shinyPaletteButton1ActionPerformed(e));
                panel1.add(shinyPaletteButton1, "cell 6 1");

                //---- shinyPaletteButton2 ----
                shinyPaletteButton2.addActionListener(e -> shinyPaletteButton2ActionPerformed(e));
                panel1.add(shinyPaletteButton2, "cell 5 2");

                //---- shinyPaletteButton3 ----
                shinyPaletteButton3.addActionListener(e -> shinyPaletteButton3ActionPerformed(e));
                panel1.add(shinyPaletteButton3, "cell 6 2");

                //---- shinyPaletteButton4 ----
                shinyPaletteButton4.addActionListener(e -> shinyPaletteButton4ActionPerformed(e));
                panel1.add(shinyPaletteButton4, "cell 5 3");

                //---- shinyPaletteButton5 ----
                shinyPaletteButton5.addActionListener(e -> shinyPaletteButton5ActionPerformed(e));
                panel1.add(shinyPaletteButton5, "cell 6 3");

                //---- shinyPaletteButton6 ----
                shinyPaletteButton6.addActionListener(e -> shinyPaletteButton6ActionPerformed(e));
                panel1.add(shinyPaletteButton6, "cell 5 4");

                //---- shinyPaletteButton7 ----
                shinyPaletteButton7.addActionListener(e -> shinyPaletteButton7ActionPerformed(e));
                panel1.add(shinyPaletteButton7, "cell 6 4");

                //---- shinyPaletteButton8 ----
                shinyPaletteButton8.addActionListener(e -> shinyPaletteButton8ActionPerformed(e));
                panel1.add(shinyPaletteButton8, "cell 5 5");

                //---- shinyPaletteButton9 ----
                shinyPaletteButton9.addActionListener(e -> shinyPaletteButton9ActionPerformed(e));
                panel1.add(shinyPaletteButton9, "cell 6 5");

                //---- shinyPaletteButton10 ----
                shinyPaletteButton10.addActionListener(e -> shinyPaletteButton10ActionPerformed(e));
                panel1.add(shinyPaletteButton10, "cell 5 6");

                //---- shinyPaletteButton11 ----
                shinyPaletteButton11.addActionListener(e -> shinyPaletteButton11ActionPerformed(e));
                panel1.add(shinyPaletteButton11, "cell 6 6");

                //---- shinyPaletteButton12 ----
                shinyPaletteButton12.addActionListener(e -> shinyPaletteButton12ActionPerformed(e));
                panel1.add(shinyPaletteButton12, "cell 5 7");

                //---- shinyPaletteButton13 ----
                shinyPaletteButton13.addActionListener(e -> shinyPaletteButton13ActionPerformed(e));
                panel1.add(shinyPaletteButton13, "cell 6 7");

                //---- shinyPaletteButton14 ----
                shinyPaletteButton14.addActionListener(e -> shinyPaletteButton14ActionPerformed(e));
                panel1.add(shinyPaletteButton14, "cell 5 8");

                //---- shinyPaletteButton15 ----
                shinyPaletteButton15.addActionListener(e -> shinyPaletteButton15ActionPerformed(e));
                panel1.add(shinyPaletteButton15, "cell 6 8");
            }
            battleSpritePanel.add(panel1, "cell 1 5 4 1,growx");
        }
        add(battleSpritePanel, "cell 0 1");

        //======== panel2 ========
        {
            panel2.setBorder(new TitledBorder(""));
            panel2.setLayout(new MigLayout(
                "hidemode 3",
                // columns
                "[fill]" +
                "[fill]",
                // rows
                "[]" +
                "[]" +
                "[]"));
        }
        add(panel2, "cell 1 1 2 1,grow");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JSpinner speciesChooserComboBox;
    private JButton saveChangesButton;
    private JButton revertChangesButton;
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
    private JPanel panel1;
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
    private JPanel panel2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public void setProject(Project project) throws IOException
    {
        this.project= project;
        speciesChooserComboBox.setValue(1);
    }

    public Image getPaletteImage(Color color)
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

    public void updateColor(BufferedImage image, Color color)
    {
        for(int row= 0; row < image.getHeight(); row++)
        {
            for(int col= 0; col < image.getWidth(); col++)
            {
                if(image.getRGB(col,row) == color.getRGB())
                    image.setRGB(col,row,color.getRGB());
            }
        }
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
            this.maleFront= copyOfImage(maleFront);
            this.maleBack= copyOfImage(maleBack);
            this.femaleFront = copyOfImage(femaleFront);
            this.femaleBack= copyOfImage(femaleBack);
            setPreferredSize(new Dimension(420,100));

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
            g.drawString("RGB values must be a multiple of 8. Maximum value allowed is 248.", 0,10);
            g.setColor(currentColor);
            g.drawImage(maleFront,getWidth()/2 - 180,20,this);
            g.drawImage(maleBack, getWidth()/2 - 90,20,this);
            g.drawImage(femaleFront,getWidth()/2 + 10,20,this);
            g.drawImage(femaleBack, getWidth() / 2 + 100,20,this);
        }
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
