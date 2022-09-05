import com.jackhack96.jNdstool.main.JNdstool;
import com.turtleisaac.pokeditor.editors.items.ItemEditorGen4;
import com.turtleisaac.pokeditor.editors.items.ItemTableEntry;
import com.turtleisaac.pokeditor.editors.items.ItemTableParser;
import com.turtleisaac.pokeditor.editors.learnsets.LearnsetEditor;
import com.turtleisaac.pokeditor.editors.moves.gen4.MoveEditorGen4;
import com.turtleisaac.pokeditor.editors.personal.gen4.PersonalEditor;
import com.turtleisaac.pokeditor.editors.personal.gen4.PersonalReturnGen4;
import com.turtleisaac.pokeditor.editors.text.TextBank;
import com.turtleisaac.pokeditor.editors.text.TextEditor;
import com.turtleisaac.pokeditor.editors.trainers.gen4.TrainerEditorGen4;
import com.turtleisaac.pokeditor.editors.trainers.gen4.TrainerReturnGen4;
import com.turtleisaac.pokeditor.framework.Buffer;
import com.turtleisaac.pokeditor.framework.narctowl.Narctowl;
import com.turtleisaac.pokeditor.gui.projects.projectwindow.ProjectWindow;
import com.turtleisaac.pokeditor.gui.projects.projectwindow.sheets.FormatGenerator;
import com.turtleisaac.pokeditor.gui.projects.projectwindow.sheets.RomApplier;
import com.turtleisaac.pokeditor.project.Project;
import com.turtleisaac.pokeditor.utilities.TableLocator;
import com.turtleisaac.pokeditor.utilities.TablePointers;
import junit.framework.*;
import turtleisaac.GoogleSheetsAPI;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class GameTest extends TestCase
{
    protected String game;
    protected String gameCode;
    protected String romPath;

    protected String testProjectPath;
    protected String testProjectName;
    protected Project testProject;
    protected GoogleSheetsAPI testAPI;
    protected String testDataPath;
    protected String testUnpackedPath;

    protected String controlProjectPath;
    protected String controlProjectName;
    protected Project controlProject;
    protected GoogleSheetsAPI controlAPI;
    protected String controlDataPath;
    protected String controlUnpackedPath;

    protected static String[] itemNames;
    protected static ArrayList<ItemTableEntry> itemTableData;

    protected String personalFilePath;
    protected String personalDirName;

    protected String movesFilePath;
    protected String movesDirName;

    protected String learnsetsFilePath;
    protected String learnsetsDirName;

    protected String itemsFilePath;
    protected String itemsDirName;

    protected String trainersFilePath;
    protected String trainersDirName;
    protected String trainerTeamsFilePath;
    protected String trainerTeamsDirName;

    protected TextBank itemNamesBank;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        testProjectName = game + "Test";
        testProjectPath = "src" + File.separator + "testing" + File.separator + game + File.separator + testProjectName;

        controlProjectName = game + "Control";
        controlProjectPath = "src" + File.separator + "testing" + File.separator + game + File.separator + controlProjectName;

        createTestProject();
        createControlProject();

        String unpackedFolderPath = "src" + File.separator + "testing" + File.separator + game + File.separator + "unpacked" + File.separator;

        testDataPath = testProject.getDataPath();
        controlDataPath = controlProject.getDataPath();
        testUnpackedPath = unpackedFolderPath + "Test" + File.separator;
        controlUnpackedPath = unpackedFolderPath + "Control" + File.separator;

        clearDirs(new File(unpackedFolderPath));
    }

    protected void createControlProject()
    {
        createProject(ProjectTypes.CONTROL, controlProjectPath, controlProjectName);
    }

    protected void createTestProject()
    {
        createProject(ProjectTypes.TEST, testProjectPath, testProjectName);
    }

    protected void createProject(ProjectTypes projectType, String projectPath, String projectName)
    {
        clearDirs(new File(projectPath));
        File projectDir = new File(projectPath);
//        System.out.println(projectDir.getAbsolutePath());
        if (!projectDir.mkdirs())
        {
            System.err.println("Project directory could not be created");
        }

        String xmlOutput;

        if (projectType == ProjectTypes.CONTROL)
        {
            controlProject = new Project(projectName, projectPath, "pokeditor");
            controlProject.setLanguage("ENG");
            controlProject.setBaseRom(Project.parseBaseRom(gameCode));
            controlProject.setBaseRomGameCode(gameCode);
            xmlOutput = controlProject.getXml();
        }
        else
        {
            testProject = new Project(projectName, projectPath, "pokeditor");
            testProject.setLanguage("ENG");
            testProject.setBaseRom(Project.parseBaseRom(gameCode));
            testProject.setBaseRomGameCode(gameCode);
            xmlOutput = testProject.getXml();
        }


        try //unpack rom
        {
            JNdstool.main("-x", romPath, projectPath + File.separator + projectName);
        } catch (IOException exception)
        {
            exception.printStackTrace();
            return;
        }

        try //write out project file
        {
            BufferedWriter writer= new BufferedWriter(new FileWriter(projectPath + File.separator + projectName + ".pokeditor"));
            writer.write(xmlOutput);
            writer.close();
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }

        try //unpack program files zip
        {
            File resourceDir = new File(projectPath + File.separator + "Program Files");
            if (!resourceDir.exists())
            {
                InputStream in = ProjectWindow.class.getResourceAsStream("/Program Files.zip");
                byte[] buffer = new byte[1024];
                ZipInputStream zipIn = new ZipInputStream(in);
                ZipEntry zipEntry = zipIn.getNextEntry();

                while (zipEntry != null)
                {
                    if (!zipEntry.getName().contains("__"))
                    {
                        File outputFile = newFile(resourceDir.getParentFile(),zipEntry);
                        if (zipEntry.isDirectory())
                        {
                            if (!outputFile.isDirectory() && !outputFile.mkdirs())
                            {
                                throw new IOException("Failed to create directory " + outputFile);
                            }
                        }
                        else
                        {
                            // fix for Windows-created archives
                            File parent = outputFile.getParentFile();
                            if (!parent.isDirectory() && !parent.mkdirs())
                            {
                                throw new IOException("Failed to create directory " + parent);
                            }

                            // write file content
                            if (!outputFile.getName().contains("._"))
                            {
                                FileOutputStream outputStream = new FileOutputStream(outputFile);
                                int len;
                                while ((len = zipIn.read(buffer)) > 0)
                                {
                                    outputStream.write(buffer, 0, len);
                                }
                                outputStream.close();
                            }
                        }
                    }
                    zipEntry = zipIn.getNextEntry();
                }
                zipIn.closeEntry();
                zipIn.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        try
        {
            initializeItemsTable(projectType);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        try //create local API instance
        {
            initializeAPI(projectType, projectPath);
        }
        catch(GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    protected void initializeAPI(ProjectTypes projectType, String projectPath) throws IOException, GeneralSecurityException
    {
        Project project;
        if (projectType == ProjectTypes.CONTROL)
        {
            project = controlProject;
        }
        else
        {
            project = testProject;
        }

        GoogleSheetsAPI api = new GoogleSheetsAPI(null, projectPath, true);

        if (projectType == ProjectTypes.CONTROL)
        {
            controlAPI = api;
        }
        else
        {
            testAPI = api;
        }

        ObjectOutputStream out= new ObjectOutputStream(new FileOutputStream(projectPath + File.separator + "api.ser"));
        out.writeObject(api.getSPREADSHEET_LINK());
        out.close();


        if (projectType == ProjectTypes.TEST)
        {
            RomApplier editApplier = new RomApplier(project, projectPath, api, null, false, itemTableData, false);
            editApplier.getCheckboxTree().checkRoot();
            editApplier.applyButtonActionPerformed(null);
            editApplier.dispose();
        }
    }

    private void initializeItemsTable(ProjectTypes projectType) throws IOException
    {
        Project project;
        if (projectType == ProjectTypes.CONTROL)
        {
            project = controlProject;
        }
        else
        {
            project = testProject;
        }

        //TODO finish
        TableLocator tableLocator= new TableLocator(project);
        TablePointers.TablePointer itemTablePointer;
        itemTablePointer = TablePointers.getPointers().get(project.getBaseRomGameCode()).get("items");

        switch(project.getBaseRom())
        {
            case Platinum:
                itemNames = TextEditor.getBank(project, TextBank.PLAT_ITEM_NAMES);
                break;

            case HeartGold:
            case SoulSilver:
                itemNames = TextEditor.getBank(project, TextBank.HGSS_ITEM_NAMES);
                break;
        }

        byte[] itemTable;
        itemTable= tableLocator.obtainTableArr(itemTablePointer, itemNames.length, 8);
        itemTableData = ItemTableParser.parseTable(itemTable, itemNames.length);
    }

     // Below are test methods

    public void testIdenticalBases()
    {
        assertEquals(controlProject.getBaseRom(), testProject.getBaseRom());
        assertEquals(controlProject.getBaseRomGameCode(), testProject.getBaseRomGameCode());
    }

    public void testPersonalEditor() throws IOException
    {
        System.out.println("Testing personal editor");
        Narctowl narctowl = new Narctowl(true);
        testAPI.updateSheet("Formatting (DO NOT TOUCH)", FormatGenerator.updateFormatSheet(testProject));

        //generate test sheets

        PersonalEditor editor= new PersonalEditor(testDataPath, testProject);
        narctowl.unpack(testDataPath + File.separator + personalFilePath,testUnpackedPath + personalDirName);
        PersonalReturnGen4 testReturn= editor.personalToSheet(testUnpackedPath + personalDirName);
        testAPI.updateSheet("Personal", testReturn.getPersonalData());
        testAPI.updateSheet("TM Learnsets", testReturn.getTMData());

        //test sheets to files

        clearDirs(new File(testUnpackedPath + personalDirName));
        editor.csvToPersonal(testAPI.getSpecifiedSheetArr("Personal"), testAPI.getSpecifiedSheetArr("TM Learnsets"), testUnpackedPath + personalDirName, testDataPath + File.separator + personalFilePath);

        //unpack control narc

        clearDirs(new File(controlUnpackedPath + personalDirName));
        narctowl.unpack(controlDataPath + File.separator + personalFilePath,controlUnpackedPath + personalDirName);

        //compare test files to control files

        File controlDir = new File(controlUnpackedPath + personalDirName);
        File testDir = new File(testUnpackedPath + personalDirName);

        File[] controlFiles = controlDir.listFiles();
        File[] testFiles = testDir.listFiles();

        sort(controlFiles);
        sort(testFiles);

        assertEquals(controlFiles.length, testFiles.length);
        for(int i = 0; i < controlFiles.length; i++)
        {
            byte[] control = Buffer.readFile(controlFiles[i].getAbsolutePath());
            byte[] test = Buffer.readFile(testFiles[i].getAbsolutePath());
            assertEquals(control.length, test.length);
            if (control.length == test.length)
            {
                for (int j = 0; j < control.length; j++)
                {
                    assertEquals(control[j], test[j]);
                }
            }
        }
    }

    public void testMovesEditor() throws IOException
    {
        System.out.println("Testing moves editor");
        Narctowl narctowl = new Narctowl(true);
        testAPI.updateSheet("Formatting (DO NOT TOUCH)", FormatGenerator.updateFormatSheet(testProject));

        //generate test sheets

        MoveEditorGen4 editor= new MoveEditorGen4(testProject,testDataPath);
        narctowl.unpack(testDataPath + File.separator + movesFilePath,testUnpackedPath + movesDirName);
        testAPI.updateSheet("Moves",editor.movesToSheet(testUnpackedPath + movesDirName));

        //test sheets to files

        clearDirs(new File(testUnpackedPath + movesDirName));
        editor.sheetToMoves(testAPI.getSpecifiedSheetArr("Moves"), testUnpackedPath + movesDirName, testDataPath + File.separator + movesFilePath);

        //unpack control narc

        clearDirs(new File(controlUnpackedPath + movesDirName));
        narctowl.unpack(controlDataPath + File.separator + movesFilePath,controlUnpackedPath + movesDirName);


        //compare test files to control files

        File controlDir = new File(controlUnpackedPath + movesDirName);
        File testDir = new File(testUnpackedPath + movesDirName);

        File[] controlFiles = controlDir.listFiles();
        File[] testFiles = testDir.listFiles();

        sort(controlFiles);
        sort(testFiles);

        assertEquals(controlFiles.length, testFiles.length);
        for(int i = 0; i < controlFiles.length; i++)
        {
            byte[] control = Buffer.readFile(controlFiles[i].getAbsolutePath());
            byte[] test = Buffer.readFile(testFiles[i].getAbsolutePath());
            assertEquals(control.length, test.length);
            if (control.length == test.length)
            {
                for (int j = 0; j < control.length; j++)
                {
                    System.out.printf("Move %d byte %d: %d vs. %d\n", i, j, control[j], test[j]);
                    assertEquals(control[j], test[j]);
                }
            }
        }
    }

    public void testLearnsetsEditor() throws IOException
    {
        System.out.println("Testing learnset editor");
        Narctowl narctowl = new Narctowl(true);
        testAPI.updateSheet("Formatting (DO NOT TOUCH)", FormatGenerator.updateFormatSheet(testProject));

        //generate test sheets

        LearnsetEditor editor= new LearnsetEditor(testDataPath, testProject);
        narctowl.unpack(testDataPath + File.separator + learnsetsFilePath,testUnpackedPath + learnsetsDirName);
        testAPI.updateSheet("Level-Up Learnsets",editor.learnsetToSheet(testUnpackedPath + learnsetsDirName));

        //test sheets to files

        clearDirs(new File(testUnpackedPath + learnsetsDirName));
        editor.sheetToLearnsets(testAPI.getSpecifiedSheetArr("Level-Up Learnsets"),testUnpackedPath + learnsetsDirName);

        //unpack control narc

        clearDirs(new File(controlUnpackedPath + learnsetsDirName));
        narctowl.unpack(controlDataPath  + File.separator + learnsetsFilePath,controlUnpackedPath + learnsetsDirName);


        //compare test files to control files

        File controlDir = new File(controlUnpackedPath + learnsetsDirName);
        File testDir = new File(testUnpackedPath + learnsetsDirName);

        File[] controlFiles = controlDir.listFiles();
        File[] testFiles = testDir.listFiles();

        sort(controlFiles);
        sort(testFiles);

        assertEquals(controlFiles.length, testFiles.length);
        for(int i = 0; i < controlFiles.length; i++)
        {
            byte[] control = Buffer.readFile(controlFiles[i].getAbsolutePath());
            byte[] test = Buffer.readFile(testFiles[i].getAbsolutePath());
            assertEquals(control.length, test.length);
            if (control.length == test.length)
            {
                for (int j = 0; j < control.length; j++)
                {
                    System.out.printf("Learnset %d byte %d: %d vs. %d\n", i, j, control[j], test[j]);
                    if (i != 354)
                        assertEquals(control[j], test[j]);
                }
            }
        }
    }

    public void testItemsEditor() throws IOException
    {
        System.out.println("Testing items editor");
        Narctowl narctowl = new Narctowl(true);
        testAPI.updateSheet("Formatting (DO NOT TOUCH)", FormatGenerator.updateFormatSheet(testProject));

        //generate test sheets

        ItemEditorGen4 editor= new ItemEditorGen4(testDataPath, testProject, itemTableData);
        narctowl.unpack(testDataPath + File.separator + itemsFilePath,testUnpackedPath + itemsDirName);
        testAPI.updateSheet("Items",editor.itemsToSheet(testUnpackedPath + itemsDirName));

        //test sheets to files

        clearDirs(new File(testUnpackedPath + itemsDirName));
        editor.sheetToItems(testAPI.getSpecifiedSheetArr("Items"),testAPI.getSpecifiedSheetArr("Formatting (DO NOT TOUCH)"), testUnpackedPath + itemsDirName, testDataPath + File.separator + itemsFilePath);

        //unpack control narc

        clearDirs(new File(controlUnpackedPath + itemsDirName));
        narctowl.unpack(controlDataPath  + File.separator + itemsFilePath,controlUnpackedPath + itemsDirName);


        //compare test files to control files

        File controlDir = new File(controlUnpackedPath + itemsDirName);
        File testDir = new File(testUnpackedPath + itemsDirName);

        File[] controlFiles = controlDir.listFiles();
        File[] testFiles = testDir.listFiles();

        sort(controlFiles);
        sort(testFiles);

        assertEquals(controlFiles.length, testFiles.length);
        for(int i = 0; i < controlFiles.length; i++)
        {
            byte[] control = Buffer.readFile(controlFiles[i].getAbsolutePath());
            byte[] test = Buffer.readFile(testFiles[i].getAbsolutePath());
            assertEquals(control.length, test.length);
            if (control.length == test.length)
            {
                for (int j = 0; j < control.length; j++)
                {
                    System.out.printf("Item %d byte %d: %d vs. %d\n", i, j, control[j], test[j]);
                    assertEquals(control[j], test[j]);
                }
            }
        }
    }

    public void testTrainerEditor() throws IOException
    {
        System.out.println("Testing trainer editor");
        Narctowl narctowl = new Narctowl(true);
        testAPI.updateSheet("Formatting (DO NOT TOUCH)", FormatGenerator.updateFormatSheet(testProject));

        //generate test sheets

        TrainerEditorGen4 editor= new TrainerEditorGen4(testProject,testDataPath,testProject.getBaseRom());
        narctowl.unpack(testDataPath + File.separator + trainersFilePath,testUnpackedPath + trainersDirName);
        narctowl.unpack(testDataPath + File.separator + trainerTeamsFilePath,testUnpackedPath + trainerTeamsDirName);
        TrainerReturnGen4 trainerReturn= editor.trainersToSheets(testUnpackedPath + trainersDirName,testUnpackedPath + trainerTeamsDirName);
        testAPI.updateSheet("Trainer Data",trainerReturn.getTrainerData());
        testAPI.updateSheet("Trainer Pokemon",trainerReturn.getTrainerPokemon());

        //test sheets to files

        clearDirs(new File(testUnpackedPath + trainersDirName));
        clearDirs(new File(testUnpackedPath + trainerTeamsDirName));
        editor.sheetsToTrainers(testAPI.getSpecifiedSheetArr("Trainer Data"),testAPI.getSpecifiedSheetArr("Trainer Pokemon"),testUnpackedPath);

        //unpack control narc

        clearDirs(new File(controlUnpackedPath + trainersDirName));
        clearDirs(new File(controlUnpackedPath + trainerTeamsDirName));
        narctowl.unpack(controlDataPath + File.separator + trainersFilePath,controlUnpackedPath + trainersDirName);
        narctowl.unpack(controlDataPath + File.separator + trainerTeamsFilePath,controlUnpackedPath + trainerTeamsDirName);

        //compare test files to control files (TRDATA)

        File controlDir = new File(controlUnpackedPath + trainersDirName);
        File testDir = new File(testUnpackedPath + trainersDirName);

        File[] controlFiles = controlDir.listFiles();
        File[] testFiles = testDir.listFiles();

        sort(controlFiles);
        sort(testFiles);

        assertEquals(controlFiles.length, testFiles.length);
        for(int i = 0; i < controlFiles.length; i++)
        {
            byte[] control = Buffer.readFile(controlFiles[i].getAbsolutePath());
            byte[] test = Buffer.readFile(testFiles[i].getAbsolutePath());
            assertEquals(control.length, test.length);
            if (control.length == test.length)
            {
                for (int j = 0; j < control.length; j++)
                {
                    System.out.printf("Trainer %d byte %d: %d vs. %d\n", i, j, control[j], test[j]);
                    assertEquals(control[j], test[j]);
                }
            }
        }

        //compare test files to control files (TRPOKE)

        controlDir = new File(controlUnpackedPath + trainerTeamsDirName);
        testDir = new File(testUnpackedPath + trainerTeamsDirName);

        controlFiles = controlDir.listFiles();
        testFiles = testDir.listFiles();

        sort(controlFiles);
        sort(testFiles);

        assertEquals(controlFiles.length, testFiles.length);
        for(int i = 0; i < controlFiles.length; i++)
        {
            byte[] control = Buffer.readFile(controlFiles[i].getAbsolutePath());
            byte[] test = Buffer.readFile(testFiles[i].getAbsolutePath());
            assertEquals(control.length, test.length);
            if (control.length == test.length)
            {
                for (int j = 0; j < control.length; j++)
                {
                    System.out.printf("Trainer team %d byte %d: %d vs. %d\n", i, j, control[j], test[j]);
                    assertEquals(control[j], test[j]);
                }
            }
        }
    }

    public void testItemNames() throws IOException
    {
        testItemsEditor();
        textTester(itemNamesBank);
    }

    protected void textTester(TextBank textBank) throws IOException
    {
        String[] controlText = TextEditor.getBank(controlProject, textBank);
        String[] testText = TextEditor.getBank(testProject, textBank);

//        assertEquals(controlText.length, testText.length);
        for (int i = 0; i < controlText.length; i++)
        {
            System.out.printf("%s vs. %s\n", controlText[i], testText[i]);
            assertEquals(controlText[i], testText[i]);
        }
    }



     // Below are helper methods

    private void sort (File arr[])
    {
        Arrays.sort(arr, Comparator.comparingInt(GameTest::fileToInt));
    }

    private static int fileToInt (File f)
    {
        return Integer.parseInt(f.getName().split("\\.")[0]);
    }

    private void clearDirs(File file)
    {
        if(file.exists())
        {
            if (file.isDirectory())
            {
                for (File f : Objects.requireNonNull(file.listFiles()))
                {
                    clearDirs(f);
                }

                if (!file.delete())
                {
                    System.err.println("Directory \"" + file.getAbsolutePath() + "\" not deleted successfully");
                }
            }
            else
            {
                if (!file.delete())
                {
                    System.err.println("File \"" + file.getAbsolutePath() + "\" not deleted successfully");
                }
            }
        }
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

    // Below are subclasses

    public enum ProjectTypes {
        CONTROL,
        TEST
    }
}
