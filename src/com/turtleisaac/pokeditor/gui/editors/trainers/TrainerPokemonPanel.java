/*
 * Created by JFormDesigner on Thu Jan 21 15:19:22 EST 2021
 */

package com.turtleisaac.pokeditor.gui.editors.trainers;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import com.jidesoft.swing.ComboBoxSearchable;
import com.turtleisaac.pokeditor.gui.EditorComboBox;
import com.turtleisaac.pokeditor.gui.ComboBoxItem;
import com.turtleisaac.pokeditor.utilities.TrainerPersonalityCalculator;
import com.turtleisaac.pokeditor.editors.trainers.gen4.TrainerPokemonData;
import net.miginfocom.swing.*;

/**
 * @author turtleisaac
 */
public class TrainerPokemonPanel extends JPanel
{
    private TrainerPokemonData pokemonData;
    private static final String[] natures= {"Hardy","Lonely","Brave","Adamant","Naughty","Bold","Docile","Relaxed","Impish","Lax","Timid","Hasty","Serious","Jolly","Naive","Modest","Mild","Quiet","Bashful","Rash","Calm","Gentle","Sassy","Careful","Quirky"};
    private static int[] formNumberData;
    private final TrainerPanel parent;
    private final String projectPath;

    private boolean pokemonChangesStored = true;

    public TrainerPokemonPanel(TrainerPanel parent, TrainerPokemonData pokemon, boolean moves, boolean item)
    {
        initComponents();
        pokemonData= pokemon;
        this.parent= parent;
        projectPath= parent.getProjectPath();


        ComboBoxSearchable natureSearchable= new ComboBoxSearchable(superCustomNatureComboBox);
        ComboBoxSearchable sealSearchable= new ComboBoxSearchable(sealSelectionComboBox);

        setMovesEnabled(moves);
        setItemEnabled(item);
        pidPane.setBorder(new TitledBorder("PID Generation"));

        enableParentData();

        if(pokemon != null)
        {
            loadPokemonData();
        }

        if(parent.isAbilityFunctional())
        {
            superCustomAbilitySpinner.setEnabled(true);
            oldMethodAbilitySpinner.setEnabled(true);
        }
    }

    public void enableParentData()
    {
        for(String species : parent.getSpeciesList())
        {
            speciesComboBox.addItem(new ComboBoxItem(species));
        }

        for(String heldItem : parent.getItemList())
        {
            heldItemComboBox.addItem(new ComboBoxItem(heldItem));
        }

        for(String move : parent.getMoveData())
        {
            move1ComboBox.addItem(new ComboBoxItem(move));
            move2ComboBox.addItem(new ComboBoxItem(move));
            move3ComboBox.addItem(new ComboBoxItem(move));
            move4ComboBox.addItem(new ComboBoxItem(move));
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

    private void loadPokemonData()
    {
        speciesComboBox.setSelectedIndex(pokemonData.getPokemon());
        heldItemComboBox.setSelectedIndex(pokemonData.getItem());
        move1ComboBox.setSelectedIndex(pokemonData.getMove1());
        move2ComboBox.setSelectedIndex(pokemonData.getMove2());
        move3ComboBox.setSelectedIndex(pokemonData.getMove3());
        move4ComboBox.setSelectedIndex(pokemonData.getMove4());
        sealSelectionComboBox.setSelectedIndex(pokemonData.getBallCapsule());

        int pidValue= TrainerPersonalityCalculator.generatePid(parent.getTrainerFileIndex(),parent.getSelectedClassIndex(),parent.getSelectedClassGender(),speciesComboBox.getSelectedIndex(),pokemonData.getLevel(),pokemonData.getIvs(), pokemonData.getAbility(), parent.isAbilityFunctional());

        superCustomLevelSpinner.setValue(pokemonData.getLevel());
        superCustomIvSpinner.setValue(pokemonData.getIvs()*31/255);
        superCustomAbilitySpinner.setValue((int) pokemonData.getAbility());
        superCustomFormSlider.setValue(pokemonData.getAltForm());
        superCustomNatureComboBox.setSelectedIndex((pidValue%100)%25);

        targetPidLevelSpinner.setValue(pokemonData.getLevel());
        targetPidFormSpinner.setValue(pokemonData.getAltForm());
        targetPidTextField.setText("0x" + fixHexString(Integer.toHexString(pidValue)));

        oldMethodLevelSpinner.setValue(pokemonData.getLevel());
        oldMethodFormSpinner.setValue(pokemonData.getAltForm());
        oldMethodDifficultySpinner.setValue((int) pokemonData.getIvs());
        oldMethodAbilitySpinner.setValue((int) pokemonData.getAbility());
        setVisibilityIndicator(false);
    }

    private void targetPidApplyButtonActionPerformed(ActionEvent e)
    {
        parent.setSaved(false);
        setVisibilityIndicator(false);
        int level= (int) targetPidLevelSpinner.getValue();;
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
            return;
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
                return (Integer) targetPidFormSpinner.getValue();
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
        loadPokemonData();
    }

    private void superCustomApplyButtonActionPerformed(ActionEvent e)
    {
        setVisibilityIndicator(false);
        parent.setSaved(false);
        int level= (int) superCustomLevelSpinner.getValue();
        int ivValue= (int) superCustomIvSpinner.getValue();
        int ability= (int) superCustomAbilitySpinner.getValue();
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
//        System.out.println("Starting value: " + min);
        for(int i= min; i*31/255 == ivValue && i <= 255; i++)
        {
//            for(int ability_num= 255; ability_num != 0; ability_num--)
//            {
//                if(ability_num % 2 == ability)
//                {
//
//                }
//            }
            pidValue= TrainerPersonalityCalculator.generatePid(parent.getTrainerFileIndex(),parent.getSelectedClassIndex(),parent.getSelectedClassGender(),speciesComboBox.getSelectedIndex(),level,i, ability, parent.isAbilityFunctional());
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
                        return formNumber;
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
                loadPokemonData();
                break;
            }
        }

        if(difficultyValue != -1)
        {
            JOptionPane.showMessageDialog(parent,"Success!\nPID: 00" + Integer.toHexString(pidValue) + "\nDifficulty Value: " + difficultyValue,"Success",JOptionPane.INFORMATION_MESSAGE);
        }
        else
        {
            JOptionPane.showMessageDialog(parent,"Error: No possible difficulty value can result in the specified target nature and ability with the selected level and IVs. Please adjust them and try again.","Error",JOptionPane.ERROR_MESSAGE);
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
//                for(int ability_num= 255; ability_num != 0; ability_num--)
//                {
                    int pid= TrainerPersonalityCalculator.generatePid(parent.getTrainerFileIndex(),parent.getSelectedClassIndex(),parent.getSelectedClassGender(),speciesComboBox.getSelectedIndex(),level,i, ability, parent.isAbilityFunctional());
                    area.append("\nIVs: " + i*31/255 + " (" + i + "), Nature: " + natures[(pid%100)%25]);
//                }
            }
            area.setEditable(false);
            scrollPane.setViewportView(area);
            frame.setContentPane(scrollPane);
            frame.pack();
            frame.setVisible(true);
            frame.toFront();
        }

    }

    private void speciesComboBoxActionPerformed(ActionEvent e)
    {
        setVisibilityIndicator(false);
        parent.setSaved(false);
        try
        {
//            superCustomFormSlider.setMaximum(formNumberData[speciesComboBox.getSelectedIndex()]-1);
        }
        catch(NullPointerException exception)
        {
//            superCustomFormSlider.setEnabled(false);
            return;
        }
        superCustomFormSlider.setEnabled(true);
        superCustomFormSlider.setMaximum(10);
    }

    private void oldMethodApplyButtonActionPerformed(ActionEvent e)
    {
        setVisibilityIndicator(false);
        parent.setSaved(false);

        int difficultyValue= (Integer) oldMethodDifficultySpinner.getValue();

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
                return (short) ((int) oldMethodAbilitySpinner.getValue());
            }

            @Override
            public int getLevel()
            {
                return (Integer) oldMethodLevelSpinner.getValue();
            }

            @Override
            public int getPokemon()
            {
                return speciesComboBox.getSelectedIndex();
            }

            @Override
            public int getAltForm()
            {
                return (Integer) oldMethodFormSpinner.getValue();
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

        loadPokemonData();

        JOptionPane.showMessageDialog(parent,"Success!", "Trainer Editor",JOptionPane.INFORMATION_MESSAGE);
    }

    private void move1ComboBoxActionPerformed(ActionEvent e) {
        setVisibilityIndicator(true);
    }

    private void move2ComboBoxActionPerformed(ActionEvent e) {
        setVisibilityIndicator(true);
    }

    private void move3ComboBoxActionPerformed(ActionEvent e) {
        setVisibilityIndicator(true);
    }

    private void move4ComboBoxActionPerformed(ActionEvent e) {
        setVisibilityIndicator(true);
    }

    private void heldItemComboBoxActionPerformed(ActionEvent e) {
        setVisibilityIndicator(true);
    }

    private void sealSelectionComboBoxActionPerformed(ActionEvent e) {
        setVisibilityIndicator(true);
    }

    private void superCustomFormSliderStateChanged(ChangeEvent e) {
        setVisibilityIndicator(true);
    }

    private void superCustomNatureComboBoxActionPerformed(ActionEvent e) {
        setVisibilityIndicator(true);
    }

    private void superCustomAbilitySpinnerStateChanged(ChangeEvent e) {
        setVisibilityIndicator(true);
    }

    private void superCustomIvSpinnerStateChanged(ChangeEvent e) {
        setVisibilityIndicator(true);
    }

    private void superCustomLevelSpinnerStateChanged(ChangeEvent e) {
        setVisibilityIndicator(true);
    }

    private void targetPidLevelSpinnerStateChanged(ChangeEvent e) {
        setVisibilityIndicator(true);
    }

    private void targetPidFormSpinnerStateChanged(ChangeEvent e) {
        setVisibilityIndicator(true);
    }

    private void targetPidTextFieldActionPerformed(ActionEvent e) {
        setVisibilityIndicator(true);
    }

    private void oldMethodLevelSpinnerStateChanged(ChangeEvent e) {
        setVisibilityIndicator(true);
    }

    private void oldMethodFormSpinnerStateChanged(ChangeEvent e) {
        setVisibilityIndicator(true);
    }

    private void oldMethodDifficultySpinnerStateChanged(ChangeEvent e) {
        setVisibilityIndicator(true);
    }

    private void oldMethodAbilitySpinnerStateChanged(ChangeEvent e) {
        setVisibilityIndicator(true);
    }

    private void setVisibilityIndicator(boolean needApply)
    {
//        if(needApply)
//        {
//            oldMethodApplyButton.setBackground(new Color(35, 132, 203, 255));
//            targetPidApplyButton.setBackground(new Color(35, 132, 203, 255));
//            superCustomApplyButton.setBackground(new Color(35, 132, 203, 255));
//        }
//        else
//        {
//            oldMethodApplyButton.setBackground(new Color(60, 63, 65, 255));
//            targetPidApplyButton.setBackground(new Color(60, 63, 65, 255));
//            superCustomApplyButton.setBackground(new Color(60, 63, 65, 255));
//        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        movesPanel = new JPanel();
        move1ComboBox = new EditorComboBox();
        move2ComboBox = new EditorComboBox();
        move3ComboBox = new EditorComboBox();
        move4ComboBox = new EditorComboBox();
        sealPanel = new JPanel();
        sealSelectionComboBox = new JComboBox<>();
        generalPanel = new JPanel();
        speciesLabel = new JLabel();
        speciesComboBox = new EditorComboBox();
        heldItemLabel = new JLabel();
        heldItemComboBox = new EditorComboBox();
        pidPane = new JTabbedPane();
        superCustomPanel = new JPanel();
        superCustomLevelLabel = new JLabel();
        superCustomIvLable = new JLabel();
        superCustomAbilityLabel = new JLabel();
        superCustomNatureLabel = new JLabel();
        superCustomFormLabel = new JLabel();
        superCustomLevelSpinner = new JSpinner();
        superCustomIvSpinner = new JSpinner();
        superCustomAbilitySpinner = new JSpinner();
        superCustomNatureComboBox = new JComboBox<>();
        superCustomFormSlider = new JSlider();
        superCustomApplyButton = new JButton();
        targetPidPanel = new JPanel();
        targetPidLevelLabel = new JLabel();
        targetPidFormLabel = new JLabel();
        targetPidLabel = new JLabel();
        targetPidTextField = new JTextField();
        targetPidApplyButton = new JButton();
        targetPidLevelSpinner = new JSpinner();
        targetPidFormSpinner = new JSpinner();
        oldMethodPanel = new JPanel();
        oldMethodLevelLabel = new JLabel();
        oldMethodFormLabel = new JLabel();
        oldMethodLevelSpinner = new JSpinner();
        oldMethodDifficultyLabel = new JLabel();
        oldMethodAbilityLabel = new JLabel();
        oldMethodFormSpinner = new JSpinner();
        oldMethodDifficultySpinner = new JSpinner();
        oldMethodAbilitySpinner = new JSpinner();
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

            //---- move1ComboBox ----
            move1ComboBox.addActionListener(e -> move1ComboBoxActionPerformed(e));
            movesPanel.add(move1ComboBox, "cell 0 0");

            //---- move2ComboBox ----
            move2ComboBox.addActionListener(e -> move2ComboBoxActionPerformed(e));
            movesPanel.add(move2ComboBox, "cell 1 0");

            //---- move3ComboBox ----
            move3ComboBox.addActionListener(e -> move3ComboBoxActionPerformed(e));
            movesPanel.add(move3ComboBox, "cell 0 1");

            //---- move4ComboBox ----
            move4ComboBox.addActionListener(e -> move4ComboBoxActionPerformed(e));
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
                "None",
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
            sealSelectionComboBox.addActionListener(e -> sealSelectionComboBoxActionPerformed(e));
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

            //---- heldItemComboBox ----
            heldItemComboBox.addActionListener(e -> heldItemComboBoxActionPerformed(e));
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

                //---- superCustomLevelSpinner ----
                superCustomLevelSpinner.setToolTipText("A value ranging from 1 to 100");
                superCustomLevelSpinner.setModel(new SpinnerNumberModel(1, 1, 100, 1));
                superCustomLevelSpinner.addChangeListener(e -> superCustomLevelSpinnerStateChanged(e));
                superCustomPanel.add(superCustomLevelSpinner, "cell 0 1");

                //---- superCustomIvSpinner ----
                superCustomIvSpinner.setToolTipText("A value ranging from 0 to 31");
                superCustomIvSpinner.setModel(new SpinnerNumberModel(0, 0, 31, 1));
                superCustomIvSpinner.addChangeListener(e -> superCustomIvSpinnerStateChanged(e));
                superCustomPanel.add(superCustomIvSpinner, "cell 1 1");

                //---- superCustomAbilitySpinner ----
                superCustomAbilitySpinner.setToolTipText("HGSS ONLY: Even numbers are ability 0, odd numbers are ability 1");
                superCustomAbilitySpinner.setEnabled(false);
                superCustomAbilitySpinner.setModel(new SpinnerNumberModel(0, 0, 255, 1));
                superCustomAbilitySpinner.addChangeListener(e -> superCustomAbilitySpinnerStateChanged(e));
                superCustomPanel.add(superCustomAbilitySpinner, "cell 2 1");

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
                superCustomNatureComboBox.addActionListener(e -> superCustomNatureComboBoxActionPerformed(e));
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

                //---- targetPidTextField ----
                targetPidTextField.addActionListener(e -> targetPidTextFieldActionPerformed(e));
                targetPidPanel.add(targetPidTextField, "cell 2 1");

                //---- targetPidApplyButton ----
                targetPidApplyButton.setText("Apply");
                targetPidApplyButton.addActionListener(e -> targetPidApplyButtonActionPerformed(e));
                targetPidPanel.add(targetPidApplyButton, "cell 3 0 1 2,growy");

                //---- targetPidLevelSpinner ----
                targetPidLevelSpinner.setToolTipText("A value ranging from 1 to 100");
                targetPidLevelSpinner.setModel(new SpinnerNumberModel(1, 1, 100, 1));
                targetPidLevelSpinner.addChangeListener(e -> targetPidLevelSpinnerStateChanged(e));
                targetPidPanel.add(targetPidLevelSpinner, "cell 0 1");

                //---- targetPidFormSpinner ----
                targetPidFormSpinner.setModel(new SpinnerNumberModel(0, 0, 0, 1));
                targetPidFormSpinner.addChangeListener(e -> targetPidFormSpinnerStateChanged(e));
                targetPidPanel.add(targetPidFormSpinner, "cell 1 1");
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

                //---- oldMethodLevelSpinner ----
                oldMethodLevelSpinner.setToolTipText("A value ranging from 1 to 100");
                oldMethodLevelSpinner.setModel(new SpinnerNumberModel(1, 1, 100, 1));
                oldMethodLevelSpinner.addChangeListener(e -> oldMethodLevelSpinnerStateChanged(e));
                oldMethodPanel.add(oldMethodLevelSpinner, "cell 0 1");

                //---- oldMethodDifficultyLabel ----
                oldMethodDifficultyLabel.setText("Difficulty Value");
                oldMethodPanel.add(oldMethodDifficultyLabel, "cell 2 0,alignx center,growx 0");

                //---- oldMethodAbilityLabel ----
                oldMethodAbilityLabel.setText("Ability No.");
                oldMethodPanel.add(oldMethodAbilityLabel, "cell 3 0,alignx center,growx 0");

                //---- oldMethodFormSpinner ----
                oldMethodFormSpinner.setModel(new SpinnerNumberModel(0, 0, 0, 1));
                oldMethodFormSpinner.addChangeListener(e -> oldMethodFormSpinnerStateChanged(e));
                oldMethodPanel.add(oldMethodFormSpinner, "cell 1 1");

                //---- oldMethodDifficultySpinner ----
                oldMethodDifficultySpinner.setToolTipText("A value ranging from 0 to 255");
                oldMethodDifficultySpinner.setModel(new SpinnerNumberModel(0, 0, 255, 1));
                oldMethodDifficultySpinner.addChangeListener(e -> oldMethodDifficultySpinnerStateChanged(e));
                oldMethodPanel.add(oldMethodDifficultySpinner, "cell 2 1");

                //---- oldMethodAbilitySpinner ----
                oldMethodAbilitySpinner.setToolTipText("Changes which ability posessed by this species is used (HGSS only)");
                oldMethodAbilitySpinner.setModel(new SpinnerNumberModel((short) 0, (short) 0, (short) 255, (short) 1));
                oldMethodAbilitySpinner.addChangeListener(e -> oldMethodAbilitySpinnerStateChanged(e));
                oldMethodPanel.add(oldMethodAbilitySpinner, "cell 3 1");

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
    // Generated using JFormDesigner non-commercial license
    private JPanel movesPanel;
    private EditorComboBox move1ComboBox;
    private EditorComboBox move2ComboBox;
    private EditorComboBox move3ComboBox;
    private EditorComboBox move4ComboBox;
    private JPanel sealPanel;
    private JComboBox<String> sealSelectionComboBox;
    private JPanel generalPanel;
    private JLabel speciesLabel;
    private EditorComboBox speciesComboBox;
    private JLabel heldItemLabel;
    private EditorComboBox heldItemComboBox;
    private JTabbedPane pidPane;
    private JPanel superCustomPanel;
    private JLabel superCustomLevelLabel;
    private JLabel superCustomIvLable;
    private JLabel superCustomAbilityLabel;
    private JLabel superCustomNatureLabel;
    private JLabel superCustomFormLabel;
    private JSpinner superCustomLevelSpinner;
    private JSpinner superCustomIvSpinner;
    private JSpinner superCustomAbilitySpinner;
    private JComboBox<String> superCustomNatureComboBox;
    private JSlider superCustomFormSlider;
    private JButton superCustomApplyButton;
    private JPanel targetPidPanel;
    private JLabel targetPidLevelLabel;
    private JLabel targetPidFormLabel;
    private JLabel targetPidLabel;
    private JTextField targetPidTextField;
    private JButton targetPidApplyButton;
    private JSpinner targetPidLevelSpinner;
    private JSpinner targetPidFormSpinner;
    private JPanel oldMethodPanel;
    private JLabel oldMethodLevelLabel;
    private JLabel oldMethodFormLabel;
    private JSpinner oldMethodLevelSpinner;
    private JLabel oldMethodDifficultyLabel;
    private JLabel oldMethodAbilityLabel;
    private JSpinner oldMethodFormSpinner;
    private JSpinner oldMethodDifficultySpinner;
    private JSpinner oldMethodAbilitySpinner;
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

    @Override
    public String toString()
    {
        String ret= speciesComboBox.getSelectedItem() + (heldItemComboBox.getSelectedIndex() != 0 ? " @ " + heldItemComboBox.getSelectedItem() : "")
                + "\nLevel: " + superCustomLevelSpinner.getValue().toString()
                + "\n" + superCustomNatureComboBox.getSelectedItem() + " Nature"
                + "\nIVs: " + superCustomIvSpinner.getValue().toString() + " HP / " + superCustomIvSpinner.getValue().toString() + " Atk / " + superCustomIvSpinner.getValue().toString() + " Def / "  + superCustomIvSpinner.getValue().toString() + " SpA / "  + superCustomIvSpinner.getValue().toString() + " SpD / " + superCustomIvSpinner.getValue().toString() + " Spe"
                + "\n- " + move1ComboBox.getSelectedItem()
                + "\n- " + move2ComboBox.getSelectedItem()
                + "\n- " + move3ComboBox.getSelectedItem()
                + "\n- " + move4ComboBox.getSelectedItem();

        return ret;
    }

    public void parseSmogon(String smogonSet)
    {

    }
}
