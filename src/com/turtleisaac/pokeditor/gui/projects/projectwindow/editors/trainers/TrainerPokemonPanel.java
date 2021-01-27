/*
 * Created by JFormDesigner on Thu Jan 21 15:19:22 EST 2021
 */

package com.turtleisaac.pokeditor.gui.projects.projectwindow.editors.trainers;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import com.jidesoft.swing.ComboBoxSearchable;
import com.turtleisaac.pokeditor.calculations.TrainerPersonalityCalculator;
import com.turtleisaac.pokeditor.editors.trainers.gen4.TrainerPokemonData;
import net.miginfocom.swing.*;

/**
 * @author Truck
 */
public class TrainerPokemonPanel extends JPanel
{
    private TrainerPokemonData pokemonData;
    private static final String[] natures= {"Hardy","Lonely","Brave","Adamant","Naughty","Bold","Docile","Relaxed","Impish","Lax","Timid","Hasty","Serious","Jolly","Naive","Modest","Mild","Quiet","Bashful","Rash","Calm","Gentle","Sassy","Careful","Quirky"};
    private static int[] formNumberData;
    private final TrainerPanel parent;
    private final String projectPath;

    public TrainerPokemonPanel(TrainerPanel parent, TrainerPokemonData pokemon, boolean moves, boolean item)
    {
        initComponents();
        pokemonData= pokemon;
        this.parent= parent;
        projectPath= parent.getProjectPath();


//        sealSelectionComboBox= getLargeComboBox();

        ComboBoxSearchable speciesSearchable= new ComboBoxSearchable(speciesComboBox);
        ComboBoxSearchable heldItemSearchable= new ComboBoxSearchable(heldItemComboBox);
        ComboBoxSearchable move1Searchable= new ComboBoxSearchable(move1ComboBox);
        ComboBoxSearchable move2Searchable= new ComboBoxSearchable(move2ComboBox);
        ComboBoxSearchable move3Searchable= new ComboBoxSearchable(move3ComboBox);
        ComboBoxSearchable move4Searchable= new ComboBoxSearchable(move4ComboBox);
        ComboBoxSearchable natureSearchable= new ComboBoxSearchable(superCustomNatureComboBox);
        ComboBoxSearchable sealSearchable= new ComboBoxSearchable(sealSelectionComboBox);

        setMovesEnabled(moves);
        setItemEnabled(item);
        pidPane.setBorder(new TitledBorder("PID Generation"));

        enableParentData();

        if(pokemon != null)
        {
            speciesComboBox.setSelectedIndex(pokemon.getPokemon());
            heldItemComboBox.setSelectedIndex(pokemon.getItem());
            move1ComboBox.setSelectedIndex(pokemon.getMove1());
            move2ComboBox.setSelectedIndex(pokemon.getMove2());
            move3ComboBox.setSelectedIndex(pokemon.getMove3());
            move4ComboBox.setSelectedIndex(pokemon.getMove4());
            sealSelectionComboBox.setSelectedIndex(pokemon.getBallCapsule());

            int pidValue= TrainerPersonalityCalculator.generatePid(parent.getTrainerFileIndex(),parent.getSelectedClassIndex(),parent.getSelectedClassGender(),speciesComboBox.getSelectedIndex(),pokemon.getLevel(),pokemon.getIvs());

            superCustomLevelTextField.setText("" + pokemon.getLevel());
            superCustomIvTextField.setText("" + pokemon.getIvs()*31/255);
            superCustomAbilityTextField.setText("" + pokemon.getAbility());
            superCustomFormSlider.setValue(pokemon.getAltForm());
            superCustomNatureComboBox.setSelectedIndex((pidValue%100)%25);

            targetPidLevelTextField.setText("" + pokemon.getLevel());
            targetPidFormTextField.setText("" + pokemon.getAltForm());
            targetPidFormTextField.setText("0x" + fixHexString(Integer.toHexString(pidValue)));

            oldMethodLevelTextField.setText("" + pokemon.getLevel());
            oldMethodFormTextField.setText("" + pokemon.getAltForm());
            oldMethodDifficultyTextField.setText("" + pokemon.getIvs());
            oldMethodAbilityTextField.setText("" + pokemon.getAbility());
        }
    }

    public void enableParentData()
    {
        for(String species : parent.getSpeciesList())
        {
            speciesComboBox.addItem(species);
        }

        for(String heldItem : parent.getItemList())
        {
            heldItemComboBox.addItem(heldItem);
        }

        for(String move : parent.getMoveData())
        {
            move1ComboBox.addItem(move);
            move2ComboBox.addItem(move);
            move3ComboBox.addItem(move);
            move4ComboBox.addItem(move);
        }

        formNumberData= parent.getFormNumberData();
    }

    public void setMovesEnabled(boolean bool)
    {
        move1ComboBox.setEnabled(bool);
        move2ComboBox.setEnabled(bool);
        move3ComboBox.setEnabled(bool);
        move4ComboBox.setEnabled(bool);
    }

    public void setItemEnabled(boolean bool)
    {
        heldItemComboBox.setEnabled(bool);
    }

    private void targetPidApplyButtonActionPerformed(ActionEvent e)
    {
        parent.setSaved(false);
        int level= Integer.parseInt(targetPidLevelTextField.getText());
        String pid= targetPidTextField.getText();

        if(pid.contains(" "))
        {
            StringBuilder stringBuilder= new StringBuilder();
            for(String s : pid.split(" "))
            {
                stringBuilder.append(s);
            }
            pid= stringBuilder.toString();
        }

        if(pid.length() != 8)
        {
            JOptionPane.showMessageDialog(parent,"Invalid PID: Needs to be a four byte hexadecimal value.","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        else if(!pid.startsWith("00"))
        {
            JOptionPane.showMessageDialog(parent,"Invalid PID: A target PID must have 0x00 as the first byte","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        else if(!pid.endsWith("88") && !pid.endsWith("78") && !pid.endsWith("89") && pid.endsWith("79"))
        {
            JOptionPane.showMessageDialog(parent,"Invalid PID: A target PID must have either 0x88 or 0x78 as the last byte.\n(HGSS can also have 0x89 or 0x79 as the last byte)","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }

        int pidValue= Integer.parseInt(pid,16);

        int difficultyValue= TrainerPersonalityCalculator.bruteForcePid(pidValue,parent.getTrainerFileIndex(),parent.getSelectedClassIndex(),parent.getSelectedClassGender(),speciesComboBox.getSelectedIndex(),level);

        if(difficultyValue != -1)
            JOptionPane.showMessageDialog(parent,"Success!\nDifficulty Value: " + difficultyValue,"Success",JOptionPane.INFORMATION_MESSAGE);
        else
        {
            JOptionPane.showMessageDialog(parent,"Error: No possible combination of values can result in the target PID for this trainer file number, trainer class, species, and level.","Error",JOptionPane.ERROR_MESSAGE);
        }

        pokemonData= new TrainerPokemonData()
        {
            @Override
            public short getIvs()
            {
                return (short) difficultyValue;
            }

            @Override
            public short getAbility()
            {
                return pokemonData.getAbility();
            }

            @Override
            public int getLevel()
            {
                return level;
            }

            @Override
            public int getPokemon()
            {
                return speciesComboBox.getSelectedIndex();
            }

            @Override
            public int getAltForm()
            {
                return Integer.parseInt(targetPidFormTextField.getText());
            }

            @Override
            public int getItem()
            {
                return heldItemComboBox.getSelectedIndex();
            }

            @Override
            public int getMove1()
            {
                return move1ComboBox.getSelectedIndex();
            }

            @Override
            public int getMove2()
            {
                return move2ComboBox.getSelectedIndex();
            }

            @Override
            public int getMove3()
            {
                return move3ComboBox.getSelectedIndex();
            }

            @Override
            public int getMove4()
            {
                return move4ComboBox.getSelectedIndex();
            }

            @Override
            public short getBallCapsule()
            {
                return (short) sealSelectionComboBox.getSelectedIndex();
            }
        };
    }

    private void superCustomApplyButtonActionPerformed(ActionEvent e)
    {
        parent.setSaved(false);
        int level= Integer.parseInt(superCustomLevelTextField.getText());
        int ivValue= Integer.parseInt(superCustomIvTextField.getText());
        int ability= Integer.parseInt(superCustomAbilityTextField.getText());
        int nature= superCustomNatureComboBox.getSelectedIndex();
        int formNumber= superCustomFormSlider.getValue();

        int min= -1;

        for(int i= 0; i <= 255; i++)
        {
            if(i*31/255 == ivValue)
            {
                min= i;
                break;
            }
        }

        int pidValue= -1;
        int difficultyValue= -1;
        System.out.println("Starting value: " + min);
        for(int i= min; i*31/255 == ivValue && i <= 255; i++)
        {
            pidValue= TrainerPersonalityCalculator.generatePid(parent.getTrainerFileIndex(),parent.getSelectedClassIndex(),parent.getSelectedClassGender(),speciesComboBox.getSelectedIndex(),level,i);
//            System.out.println("PID: 0x00" + Integer.toHexString(pidValue));
            if((pidValue%100)%25 == nature)
            {
                difficultyValue= i;
                int finalDifficultyValue = difficultyValue;
                pokemonData= new TrainerPokemonData()
                {
                    @Override
                    public short getIvs()
                    {
                        return (short) finalDifficultyValue;
                    }

                    @Override
                    public short getAbility()
                    {
                        return (short) ability;
                    }

                    @Override
                    public int getLevel()
                    {
                        return level;
                    }

                    @Override
                    public int getPokemon()
                    {
                        return speciesComboBox.getSelectedIndex();
                    }

                    @Override
                    public int getAltForm()
                    {
                        return superCustomFormSlider.getValue();
                    }

                    @Override
                    public int getItem()
                    {
                        return heldItemComboBox.getSelectedIndex();
                    }

                    @Override
                    public int getMove1()
                    {
                        return move1ComboBox.getSelectedIndex();
                    }

                    @Override
                    public int getMove2()
                    {
                        return move2ComboBox.getSelectedIndex();
                    }

                    @Override
                    public int getMove3()
                    {
                        return move3ComboBox.getSelectedIndex();
                    }

                    @Override
                    public int getMove4()
                    {
                        return move4ComboBox.getSelectedIndex();
                    }

                    @Override
                    public short getBallCapsule()
                    {
                        return (short) sealSelectionComboBox.getSelectedIndex();
                    }
                };
                break;
            }
        }

        if(difficultyValue != -1)
        {
            JOptionPane.showMessageDialog(parent,"Success!\nPID: 00" + Integer.toHexString(pidValue) + "\nDifficulty Value: " + difficultyValue,"Success",JOptionPane.INFORMATION_MESSAGE);
        }
        else
        {
            JOptionPane.showMessageDialog(parent,"Error: No possible difficulty value can result in the specified target nature with the selected level and IVs. Please adjust them and try again.","Error",JOptionPane.ERROR_MESSAGE);
            JFrame frame= new JFrame();
            frame.setTitle("Possible IVs and Natures");
            frame.setMinimumSize(new Dimension(500,500));
            frame.setPreferredSize(frame.getMinimumSize());
            frame.setLocationRelativeTo(parent);

            JScrollPane scrollPane= new JScrollPane();

            JTextArea area= new JTextArea();
            area.append("Level " + level + " " + speciesComboBox.getSelectedItem() + " in trainer file " + parent.getTrainerFileIndex() + " with trainer class " + parent.getSelectedClassIndex());

            for(int i= 255; i != 0; i--)
            {
                int pid= TrainerPersonalityCalculator.generatePid(parent.getTrainerFileIndex(),parent.getSelectedClassIndex(),parent.getSelectedClassGender(),speciesComboBox.getSelectedIndex(),level,i);
                area.append("\nIVs: " + i*31/255 + ", Nature: " + natures[(pid%100)%25]);
            }
            area.setEditable(false);
            scrollPane.setViewportView(area);
            frame.setContentPane(scrollPane);
            frame.pack();
            frame.setVisible(true);
        }

    }

    private void superCustomFormSliderStateChanged(ChangeEvent e)
    {
        parent.setSaved(false);
        // TODO add your code here
    }

    private void speciesComboBoxActionPerformed(ActionEvent e)
    {
        parent.setSaved(false);
        try
        {
            superCustomFormSlider.setMaximum(formNumberData[speciesComboBox.getSelectedIndex()]-1);
        }
        catch(NullPointerException exception)
        {
            superCustomFormSlider.setEnabled(false);
            return;
        }
        superCustomFormSlider.setEnabled(true);
    }

    private void oldMethodApplyButtonActionPerformed(ActionEvent e)
    {
        parent.setSaved(false);
        pokemonData= new TrainerPokemonData()
        {
            @Override
            public short getIvs()
            {
                return (short) Integer.parseInt(oldMethodDifficultyTextField.getText());
            }

            @Override
            public short getAbility()
            {
                return (short) Integer.parseInt(oldMethodAbilityTextField.getText());
            }

            @Override
            public int getLevel()
            {
                return Integer.parseInt(oldMethodLevelTextField.getText());
            }

            @Override
            public int getPokemon()
            {
                return speciesComboBox.getSelectedIndex();
            }

            @Override
            public int getAltForm()
            {
                return Integer.parseInt(oldMethodFormTextField.getText());
            }

            @Override
            public int getItem()
            {
                return heldItemComboBox.getSelectedIndex();
            }

            @Override
            public int getMove1()
            {
                return move1ComboBox.getSelectedIndex();
            }

            @Override
            public int getMove2()
            {
                return move2ComboBox.getSelectedIndex();
            }

            @Override
            public int getMove3()
            {
                return move3ComboBox.getSelectedIndex();
            }

            @Override
            public int getMove4()
            {
                return move4ComboBox.getSelectedIndex();
            }

            @Override
            public short getBallCapsule()
            {
                return (short) sealSelectionComboBox.getSelectedIndex();
            }
        };
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        movesPanel = new JPanel();
        move1ComboBox = new JComboBox();
        move2ComboBox = new JComboBox();
        move3ComboBox = new JComboBox();
        move4ComboBox = new JComboBox();
        sealPanel = new JPanel();
        sealSelectionComboBox = new JComboBox<>();
        generalPanel = new JPanel();
        speciesLabel = new JLabel();
        speciesComboBox = new JComboBox();
        heldItemLabel = new JLabel();
        heldItemComboBox = new JComboBox();
        pidPane = new JTabbedPane();
        superCustomPanel = new JPanel();
        superCustomLevelLabel = new JLabel();
        superCustomIvLable = new JLabel();
        superCustomAbilityLabel = new JLabel();
        superCustomNatureLabel = new JLabel();
        superCustomFormLabel = new JLabel();
        superCustomLevelTextField = new JTextField();
        superCustomIvTextField = new JTextField();
        superCustomAbilityTextField = new JTextField();
        superCustomNatureComboBox = new JComboBox<>();
        superCustomFormSlider = new JSlider();
        superCustomApplyButton = new JButton();
        targetPidPanel = new JPanel();
        targetPidLevelLabel = new JLabel();
        targetPidFormLabel = new JLabel();
        targetPidLabel = new JLabel();
        targetPidTextField = new JTextField();
        targetPidApplyButton = new JButton();
        targetPidLevelTextField = new JTextField();
        targetPidFormTextField = new JTextField();
        oldMethodPanel = new JPanel();
        oldMethodLevelLabel = new JLabel();
        oldMethodFormLabel = new JLabel();
        oldMethodLevelTextField = new JTextField();
        oldMethodDifficultyLabel = new JLabel();
        oldMethodAbilityLabel = new JLabel();
        oldMethodFormTextField = new JTextField();
        oldMethodDifficultyTextField = new JTextField();
        oldMethodAbilityTextField = new JTextField();
        oldMethodApplyButton = new JButton();

        //======== this ========
        setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[grow,fill]" +
            "[338,grow,fill]" +
            "[fill]",
            // rows
            "[]" +
            "[grow]"));

        //======== movesPanel ========
        {
            movesPanel.setBorder(new TitledBorder("Moves"));
            movesPanel.setLayout(new MigLayout(
                "hidemode 3",
                // columns
                "[grow,fill]" +
                "[grow,fill]",
                // rows
                "[grow]" +
                "[grow]"));
            movesPanel.add(move1ComboBox, "cell 0 0");
            movesPanel.add(move2ComboBox, "cell 1 0");
            movesPanel.add(move3ComboBox, "cell 0 1");
            movesPanel.add(move4ComboBox, "cell 1 1");
        }
        add(movesPanel, "cell 1 0,grow");

        //======== sealPanel ========
        {
            sealPanel.setBorder(new TitledBorder("Pok\u00e9 Ball Seals"));
            sealPanel.setLayout(new FlowLayout());

            //---- sealSelectionComboBox ----
            sealSelectionComboBox.setFont(new Font(".SF NS Text", Font.PLAIN, 13));
            sealSelectionComboBox.setModel(new DefaultComboBoxModel<>(new String[] {
                "Red Petals",
                "Music Notes",
                "Confetti",
                "Lightning Bolts",
                "Black Smoke",
                "Hearts & Stars",
                "Red Hearts",
                "Blue Bubbles",
                "Pink Bubbles",
                "Yellow Stars",
                "Cyan & Yellow Stars",
                "Black & White Smoke",
                "Red Flames",
                "Blue Flames",
                "Pink & Blue Bubbles",
                "Various 1",
                "Various 2",
                "Music and Lightning",
                "Red Petals 2",
                "Petals & Confetti",
                "Petal Spirals",
                "Small Confetti",
                "Blue Stars",
                "Blue & Yellow Stars",
                "Black Smoke 2",
                "Purple Petal Spirals",
                "Red Petals 3"
            }));
            sealPanel.add(sealSelectionComboBox);
        }
        add(sealPanel, "cell 2 0,growy");

        //======== generalPanel ========
        {
            generalPanel.setBorder(new TitledBorder("General"));
            generalPanel.setLayout(new MigLayout(
                "hidemode 3",
                // columns
                "[grow,fill]",
                // rows
                "[]" +
                "[]" +
                "[]" +
                "[]"));

            //---- speciesLabel ----
            speciesLabel.setText("Species");
            generalPanel.add(speciesLabel, "cell 0 0,grow");

            //---- speciesComboBox ----
            speciesComboBox.addActionListener(e -> speciesComboBoxActionPerformed(e));
            generalPanel.add(speciesComboBox, "cell 0 1,grow");

            //---- heldItemLabel ----
            heldItemLabel.setText("Held Item");
            generalPanel.add(heldItemLabel, "cell 0 2,grow");
            generalPanel.add(heldItemComboBox, "cell 0 3,grow");
        }
        add(generalPanel, "cell 0 0,growy");

        //======== pidPane ========
        {
            pidPane.setBorder(new TitledBorder("text"));
            pidPane.setName("PID Calculation");

            //======== superCustomPanel ========
            {
                superCustomPanel.setLayout(new MigLayout(
                    "hidemode 3",
                    // columns
                    "[grow,fill]" +
                    "[grow,fill]" +
                    "[grow,fill]" +
                    "[grow,fill]" +
                    "[grow,fill]" +
                    "[grow,fill]" +
                    "[fill]",
                    // rows
                    "[]" +
                    "[]"));

                //---- superCustomLevelLabel ----
                superCustomLevelLabel.setText("Level");
                superCustomPanel.add(superCustomLevelLabel, "cell 0 0,alignx center,growx 0");

                //---- superCustomIvLable ----
                superCustomIvLable.setText("IV Value");
                superCustomPanel.add(superCustomIvLable, "cell 1 0,alignx center,growx 0");

                //---- superCustomAbilityLabel ----
                superCustomAbilityLabel.setText("Ability");
                superCustomPanel.add(superCustomAbilityLabel, "cell 2 0,alignx center,growx 0");

                //---- superCustomNatureLabel ----
                superCustomNatureLabel.setText("Nature");
                superCustomPanel.add(superCustomNatureLabel, "cell 3 0 2 1,alignx center,growx 0");

                //---- superCustomFormLabel ----
                superCustomFormLabel.setText("Form No.");
                superCustomPanel.add(superCustomFormLabel, "cell 5 0,alignx center,growx 0");

                //---- superCustomLevelTextField ----
                superCustomLevelTextField.setToolTipText("A value ranging from 1 to 100");
                superCustomPanel.add(superCustomLevelTextField, "cell 0 1");

                //---- superCustomIvTextField ----
                superCustomIvTextField.setToolTipText("A value ranging from 0 to 31");
                superCustomPanel.add(superCustomIvTextField, "cell 1 1");

                //---- superCustomAbilityTextField ----
                superCustomAbilityTextField.setToolTipText("Changes which ability posessed by this species is used (HGSS only)");
                superCustomAbilityTextField.setText("0");
                superCustomAbilityTextField.setEditable(false);
                superCustomAbilityTextField.setEnabled(false);
                superCustomPanel.add(superCustomAbilityTextField, "cell 2 1");

                //---- superCustomNatureComboBox ----
                superCustomNatureComboBox.setModel(new DefaultComboBoxModel<>(new String[] {
                    "Hardy",
                    "Lonely",
                    "Brave",
                    "Adamant",
                    "Naughty",
                    "Bold",
                    "Docile",
                    "Relaxed",
                    "Impish",
                    "Lax",
                    "Timid",
                    "Hasty",
                    "Serious",
                    "Jolly",
                    "Naive",
                    "Modest",
                    "Mild",
                    "Quiet",
                    "Bashful",
                    "Rash",
                    "Calm",
                    "Gentle",
                    "Sassy",
                    "Careful",
                    "Quirky"
                }));
                superCustomPanel.add(superCustomNatureComboBox, "cell 3 1 2 1");

                //---- superCustomFormSlider ----
                superCustomFormSlider.setPaintLabels(true);
                superCustomFormSlider.setPaintTicks(true);
                superCustomFormSlider.setSnapToTicks(true);
                superCustomFormSlider.setValue(0);
                superCustomFormSlider.setMaximum(1);
                superCustomFormSlider.setMajorTickSpacing(1);
                superCustomFormSlider.addChangeListener(e -> superCustomFormSliderStateChanged(e));
                superCustomPanel.add(superCustomFormSlider, "cell 5 1");

                //---- superCustomApplyButton ----
                superCustomApplyButton.setText("Apply");
                superCustomApplyButton.addActionListener(e -> superCustomApplyButtonActionPerformed(e));
                superCustomPanel.add(superCustomApplyButton, "cell 6 0 1 2,grow");
            }
            pidPane.addTab("Method 1", superCustomPanel);

            //======== targetPidPanel ========
            {
                targetPidPanel.setToolTipText("This method will brute force for a specific PID. Useful for things like Spinda patterns");
                targetPidPanel.setLayout(new MigLayout(
                    "hidemode 3",
                    // columns
                    "[fill]" +
                    "[fill]" +
                    "[grow,fill]" +
                    "[fill]",
                    // rows
                    "[]" +
                    "[grow]"));

                //---- targetPidLevelLabel ----
                targetPidLevelLabel.setText("Level");
                targetPidPanel.add(targetPidLevelLabel, "cell 0 0,alignx center,growx 0");

                //---- targetPidFormLabel ----
                targetPidFormLabel.setText("Form No.");
                targetPidPanel.add(targetPidFormLabel, "cell 1 0,alignx center,growx 0");

                //---- targetPidLabel ----
                targetPidLabel.setText("Target PID (Hexadecimal)");
                targetPidPanel.add(targetPidLabel, "cell 2 0,alignx center,growx 0");
                targetPidPanel.add(targetPidTextField, "cell 2 1");

                //---- targetPidApplyButton ----
                targetPidApplyButton.setText("Apply");
                targetPidApplyButton.addActionListener(e -> targetPidApplyButtonActionPerformed(e));
                targetPidPanel.add(targetPidApplyButton, "cell 3 0 1 2,growy");

                //---- targetPidLevelTextField ----
                targetPidLevelTextField.setToolTipText("A value ranging from 1 to 100");
                targetPidPanel.add(targetPidLevelTextField, "cell 0 1");
                targetPidPanel.add(targetPidFormTextField, "cell 1 1");
            }
            pidPane.addTab("Method 2", targetPidPanel);

            //======== oldMethodPanel ========
            {
                oldMethodPanel.setToolTipText("The old method for setting trainer data, used in tools such as SDSME");
                oldMethodPanel.setLayout(new MigLayout(
                    "hidemode 3",
                    // columns
                    "[grow,fill]" +
                    "[grow,fill]" +
                    "[grow,fill]" +
                    "[grow,fill]" +
                    "[fill]",
                    // rows
                    "[]" +
                    "[grow]"));

                //---- oldMethodLevelLabel ----
                oldMethodLevelLabel.setText("Level");
                oldMethodPanel.add(oldMethodLevelLabel, "cell 0 0,alignx center,growx 0");

                //---- oldMethodFormLabel ----
                oldMethodFormLabel.setText("Form No.");
                oldMethodPanel.add(oldMethodFormLabel, "cell 1 0,alignx center,growx 0");

                //---- oldMethodLevelTextField ----
                oldMethodLevelTextField.setToolTipText("A value ranging from 1 to 100");
                oldMethodPanel.add(oldMethodLevelTextField, "cell 0 1");

                //---- oldMethodDifficultyLabel ----
                oldMethodDifficultyLabel.setText("Difficulty Value");
                oldMethodPanel.add(oldMethodDifficultyLabel, "cell 2 0,alignx center,growx 0");

                //---- oldMethodAbilityLabel ----
                oldMethodAbilityLabel.setText("Ability No.");
                oldMethodPanel.add(oldMethodAbilityLabel, "cell 3 0,alignx center,growx 0");
                oldMethodPanel.add(oldMethodFormTextField, "cell 1 1");

                //---- oldMethodDifficultyTextField ----
                oldMethodDifficultyTextField.setToolTipText("A value ranging from 0 to 255");
                oldMethodPanel.add(oldMethodDifficultyTextField, "cell 2 1");

                //---- oldMethodAbilityTextField ----
                oldMethodAbilityTextField.setToolTipText("Changes which ability posessed by this species is used (HGSS only)");
                oldMethodPanel.add(oldMethodAbilityTextField, "cell 3 1");

                //---- oldMethodApplyButton ----
                oldMethodApplyButton.setText("Apply");
                oldMethodApplyButton.addActionListener(e -> oldMethodApplyButtonActionPerformed(e));
                oldMethodPanel.add(oldMethodApplyButton, "cell 4 0 1 2,grow");
            }
            pidPane.addTab("Method 3", oldMethodPanel);
        }
        add(pidPane, "cell 0 1 3 1");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel movesPanel;
    private JComboBox move1ComboBox;
    private JComboBox move2ComboBox;
    private JComboBox move3ComboBox;
    private JComboBox move4ComboBox;
    private JPanel sealPanel;
    private JComboBox<String> sealSelectionComboBox;
    private JPanel generalPanel;
    private JLabel speciesLabel;
    private JComboBox speciesComboBox;
    private JLabel heldItemLabel;
    private JComboBox heldItemComboBox;
    private JTabbedPane pidPane;
    private JPanel superCustomPanel;
    private JLabel superCustomLevelLabel;
    private JLabel superCustomIvLable;
    private JLabel superCustomAbilityLabel;
    private JLabel superCustomNatureLabel;
    private JLabel superCustomFormLabel;
    private JTextField superCustomLevelTextField;
    private JTextField superCustomIvTextField;
    private JTextField superCustomAbilityTextField;
    private JComboBox<String> superCustomNatureComboBox;
    private JSlider superCustomFormSlider;
    private JButton superCustomApplyButton;
    private JPanel targetPidPanel;
    private JLabel targetPidLevelLabel;
    private JLabel targetPidFormLabel;
    private JLabel targetPidLabel;
    private JTextField targetPidTextField;
    private JButton targetPidApplyButton;
    private JTextField targetPidLevelTextField;
    private JTextField targetPidFormTextField;
    private JPanel oldMethodPanel;
    private JLabel oldMethodLevelLabel;
    private JLabel oldMethodFormLabel;
    private JTextField oldMethodLevelTextField;
    private JLabel oldMethodDifficultyLabel;
    private JLabel oldMethodAbilityLabel;
    private JTextField oldMethodFormTextField;
    private JTextField oldMethodDifficultyTextField;
    private JTextField oldMethodAbilityTextField;
    private JButton oldMethodApplyButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public TrainerPokemonData getPokemonData()
    {
        return pokemonData;
    }

    private String fixHexString(String str)
    {
        StringBuilder stringBuilder= new StringBuilder(str);
        while(stringBuilder.length() != 8)
        {
            stringBuilder.insert(0,"0");
        }
        return stringBuilder.toString();
    }
}
