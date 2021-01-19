//package com.turtleisaac.pokeditor.editors;
//
//import com.turtleisaac.pokeditor.editors.babies.BabyFormEditor;
//import com.turtleisaac.pokeditor.editors.encounters.johto.EncounterEditor;
//import com.turtleisaac.pokeditor.editors.encounters.sinnoh.SinnohEncounterEditor;
//import com.turtleisaac.pokeditor.editors.evolutions.gen4.EvolutionEditor;
//import com.turtleisaac.pokeditor.editors.evolutions.gen5.EvolutionEditorGen5;
//import com.turtleisaac.pokeditor.framework.*;
//import com.turtleisaac.pokeditor.framework.BLZCoder;
//import com.turtleisaac.pokeditor.framework.Buffer;
//import com.turtleisaac.pokeditor.editors.growth.GrowthEditor;
//import com.turtleisaac.pokeditor.editors.intro.IntroEditorGen4;
//import com.turtleisaac.pokeditor.editors.items.ItemEditorGen4;
//import com.turtleisaac.pokeditor.editors.items.ItemEditorGen5;
//import com.turtleisaac.pokeditor.editors.learnsets.LearnsetEditor;
//import com.turtleisaac.pokeditor.editors.moves.gen4.MoveEditorGen4;
//import com.turtleisaac.pokeditor.editors.moves.gen5.MoveEditorGen5;
//import com.turtleisaac.pokeditor.editors.narctowl.Narctowl;
//import com.turtleisaac.pokeditor.editors.opening.OpeningEditorGen4;
//import com.turtleisaac.pokeditor.editors.overlays.OverlayData;
//import com.turtleisaac.pokeditor.editors.personal.gen4.PersonalEditor;
//import com.turtleisaac.pokeditor.editors.personal.gen5.Gen5PersonalEditor2;
//import com.turtleisaac.pokeditor.editors.randomizer.FileRandomizer;
//import com.turtleisaac.pokeditor.editors.starters.gen4.StarterEditorGen4;
//import com.turtleisaac.pokeditor.editors.tutors.sinnoh.TutorMoveListEditor;
//
//import java.io.*;
//import java.security.NoSuchAlgorithmException;
//import java.util.*;
//import java.net.*;
//
//public class DsRomReader
//{
//
//    public static void main(String[] args) throws Exception
//    {
//        System.setIn(new FilterInputStream(System.in){
//            @Override
//            public void close(){
//                throw new RuntimeException("System.in closed!");
//            }
//        } );
//
//        /**
//         * Update Notifications
//         */
//        String path= System.getProperty("user.dir") + File.separator;
//
//        System.out.println("PokEditor is a tool written by Turtleisaac. All unauthorized or uncredited uses of this tool should be reported immediately. If you are using an authorized version of this tool, enjoy! (If you aren't using an authorized version, I am deeply disappointed)");
//
//        URL versionUrl= new URL("https://raw.githubusercontent.com/turtleisaac/PokEditor/master/Program%20Files/version");
//        BufferedReader onlineVersionReader;
//        try
//        {
//            onlineVersionReader= new BufferedReader(new InputStreamReader(versionUrl.openStream()));
//        }
//        catch (IOException e)
//        {
//            onlineVersionReader= new BufferedReader(new FileReader(path + "Program Files" + File.separator + "version"));
//        }
//        String onlineVersion= onlineVersionReader.readLine().toLowerCase();
//
//
//        BufferedReader localVersionReader= new BufferedReader(new FileReader(path + "Program Files" + File.separator + "version"));
//        String localVersion= localVersionReader.readLine().toLowerCase();
//        Scanner scanner= new Scanner(System.in);
//
//        if(!onlineVersion.equals(localVersion))
//        {
//
//            String ans;
//
//            System.out.println("\nThere is a new version of PokEditor (v" + onlineVersion + ") available. You are currently running v" + localVersion + ". Do you wish to update? (y/N)\n");
//
//            System.out.println("v" + onlineVersion + " Changelog:");
//            String line;
//            while((line= onlineVersionReader.readLine()) != null)
//            {
//                System.out.println("    " + line);
//            }
//
//            ans= scanner.nextLine().toLowerCase();
//
//
//
//            if(ans.equals("y"))
//            {
//                System.out.println("\nPlease go update PokEditor using the releases tab on the official GitHub: https://github.com/turtleisaac/PokEditor/releases");
//                System.exit(0);
//            }
//            onlineVersionReader.close();
//            localVersionReader.close();
//        }
//
//
//        /**
//         * Help Command
//         */
//        if(args.length == 0)
//        {
//            args= new String[] {"help"};
//        }
//        if(args[0].equalsIgnoreCase("help"))
//        {
//            System.out.print("\nHelp: ");
//            if(args.length == 1)
//            {
//                System.out.println("\n\nThe following entries are the different editors/ tools you can run in PokEditor\nTo view information on any of these editors/ tools, please either type in their names now, or for future reference, add them as a second parameter when running the help command. For example, run \"java -jar PokEditor.jar help moves\" to see information on the move editor");
//                System.out.println("\nSpreadsheet-Based Editors:\n* PokEditor\n* Personal\n* Learnsets\n* Encounters\n* Evolutions\n* Growth\n* Items\n* Moves\n* Tutors\n* Babies\n");
//                System.out.println("\nCMD-Based Editors:\n* Starters\n* Intro\n* Opening\n");
//                System.out.println("\nOthers:\n* Narc\n* Random\n* ARM9\n* ARM7");
//                args= new String[] {args[0],scanner.nextLine()};
//                System.out.print("\nHelp: ");
//            }
//
//            switch (args[1].toLowerCase())
//            {
//                case "com/turtleisaac/pokeditor/editors/personal":
//                    System.out.println("Personal Data Editor\n");
//                    System.out.println("The Personal Data narc is used to store a large chunk of the information needed for each species of Pokémon, such as their base stats, types, abilities, ev yields, and much more!");
//                    System.out.println("To run the Personal Data Editor, you need to run PokEditor using the argument \"personal\" (without the quotes), followed by the name of your rom (including .nds) (which needs to be inside of the PokEditor folder)\n");
//                    System.out.println("The spreadsheets that are generated by running this command (and therefore need to be exported from the templates when you are done editing) are \"personalData.csv\" and \"tmLearnsetData.csv\"");
//                    System.out.println("Make sure to append the file names with \"Recompile\" prior to the file extension (don't add any spaces)");
//                    System.out.println("This editor currently works for all games, both Gen 4 and 5");
//                    break;
//
//                case "com/turtleisaac/pokeditor/editors/learnsets":
//                    System.out.println("Level-Up Learnsets Editor\n");
//                    System.out.println("The Level-Up Learnsets narc, as the name suggests, is used to store the level-up learnsets of each species of Pokémon. Each Pokémon can have a maximum of 20 moves in Gen 4, and a maximum of 30 in Gen 5");
//                    System.out.println("To run the Level-Up Learnsets Editor, you need to run PokEditor using the argument \"learnsets\" (without the quotes), followed by the name of your rom (including .nds) (which needs to be inside of the PokEditor folder)\n");
//                    System.out.println("The spreadsheet that is generated by running this command (and therefore needs to be exported from the templates when you are done editing) is \"Learnset.csv\"");
//                    System.out.println("Make sure to append the file names with \"Recompile\" prior to the file extension (don't add any spaces)");
//                    System.out.println("This editor currently works for all games, both Gen 4 and 5");
//                    break;
//
//                case "com/turtleisaac/pokeditor/editors/encounters":
//                    System.out.println("Wild Encounters Editor\n");
//                    System.out.println("The Encounters narc, as the name suggests, is used to store the different types of encounter data for each area in the game");
//                    System.out.println("To run the Wild Encounters Editor, you need to run PokEditor using the argument \"encounters\" (without the quotes), followed by the name of your rom (including .nds) (which needs to be inside of the PokEditor folder)\n");
//                    System.out.println("The spreadsheets that are generated by running this command (and therefore need to be exported from the templates when you are done editing) can be found in the \"encounters\" folder that is generated by the program. The spreadsheets that are generated differ depending on what game or Gen you are editing");
//                    System.out.println("Make sure to append the file names with \"Recompile\" prior to the file extension (don't add any spaces)");
//                    System.out.println("This editor currently works for all Gen 4 games");
//                    break;
//
//                case "com/turtleisaac/pokeditor/editors/evolutions":
//                    System.out.println("Evolutions Editor\n");
//                    System.out.println("The Evolutions narc, as the name suggests, is used to store the different evolutions of each species of Pokémon. Each species can only have a maximum of seven evolutions");
//                    System.out.println("To run the Evolutions Editor, you need to run PokEditor using the argument \"evolutions\" (without the quotes), followed by the name of your rom (including .nds) (which needs to be inside of the PokEditor folder)\n");
//                    System.out.println("The spreadsheet that is generated by running this command (and therefore needs to be exported from the templates when you are done editing) is \"evolutionData.csv\"");
//                    System.out.println("Make sure to append the file names with \"Recompile\" prior to the file extension (don't add any spaces)");
//                    System.out.println("This editor currently works for all games, both Gen 4 and 5");
//                    break;
//
//                case "com/turtleisaac/pokeditor/editors/growth":
//                    System.out.println("Growth Table Editor\n");
//                    System.out.println("The Growth table narc is used to store the amount of exp required for each level for each growth type (the growth type used be changed for each pokemon using the Personal editor)");
//                    System.out.println("To run the Growth Table Editor, you need to run PokEditor using the argument \"growth\" (without the quotes), followed by the name of your rom (including .nds) (which needs to be inside of the PokEditor folder)\n");
//                    System.out.println("The spreadsheet that is generated by running this command (and therefore needs to be exported from the templates when you are done editing) is \"growthTable.csv\"");
//                    System.out.println("Make sure to append the file names with \"Recompile\" prior to the file extension (don't add any spaces)");
//                    System.out.println("This editor currently works for all Gen 4 games");
//                    break;
//
//                case "com/turtleisaac/pokeditor/editors/items":
//                    System.out.println("Item Editor\n");
//                    System.out.println("The Items narc, as the name suggests, is used to store the data required to define every single item in the game");
//                    System.out.println("To run the Item Editor, you need to run PokEditor using the argument \"items\" (without the quotes), followed by the name of your rom (including .nds) (which needs to be inside of the PokEditor folder)\n");
//                    System.out.println("The spreadsheet that is generated by running this command (and therefore needs to be exported from the templates when you are done editing) is \"Items.csv\"");
//                    System.out.println("Make sure to append the file names with \"Recompile\" prior to the file extension (don't add any spaces)");
//                    System.out.println("This editor currently works for all Gen 4 games");
//                    break;
//
//                case "com/turtleisaac/pokeditor/editors/moves":
//                    System.out.println("Move Editor\n");
//                    System.out.println("The Moves narc, as the name suggests, is used to store the data required to define every single move in the game");
//                    System.out.println("To run the Move Editor, you need to run PokEditor using the argument \"moves\" (without the quotes), followed by the name of your rom (including .nds) (which needs to be inside of the PokEditor folder)\n");
//                    System.out.println("The spreadsheet that is generated by running this command (and therefore needs to be exported from the templates when you are done editing) is \"moveData.csv\"");
//                    System.out.println("Make sure to append the file names with \"Recompile\" prior to the file extension (don't add any spaces)");
//                    System.out.println("This editor currently works for all games, both Gen 4 and 5");
//                    break;
//
//                case "narc" :
//                    System.out.println("Narctowl\n");
//                    System.out.println("This program has a built-in narc unpacking/ packing tool, called Narctowl. It is used for many of the internal functions of PokEditor, but it can be accessed separately for your own usage.");
//                    System.out.println("To access it, use the arguments \"narc\" (without the quotes), and then either \"pack\" or \"unpack\" (depending on which operation you want to do) (once again, without the quotes)");
//                    System.out.println("You will then be prompted for different information such as the name of the narc to unpack or the name of the folder to pack into a narc");
//                    break;
//
//                case "pokeditor" :
//                    System.out.println("PokEditor\n");
//                    System.out.println("\nWelcome to PokEditor! PokEditor is a tool written by Turtleisaac which is meant to replace many features of the previously used, old programs such as PPRE or SDSME");
//                    System.out.println("PokEditor functions by reading game data straight from a rom, processing it into an editable spreadsheet, then using an edited spreadsheet to modify the existing data in a rom");
//                    System.out.println("Unlike many of the old tools for Nintendo DS Pokémon hacking, PokEditor will work with your rom, regardless of how crazily it has been modified from the original. It also works on others' hacks, such as");
//                    System.out.println("PokEditor currently has many different editors across both Gen 4 and 5, with many more editors planned for future versions. Please stay tuned!");
//                    System.out.println("The easiest way to edit data is to use the PokEditor templates on Google Sheets. They have plenty of formatting, data validation/ error correction, and detailed descriptions of what each value does");
//                    System.out.println("This is the link to the PokEditor Google Sheets templates: https://drive.google.com/drive/folders/1hlKiP7V31Ddj4WmKnjK7lfhT88yPjB55?usp=sharing");
//                    System.out.println("This is the link to the PokEditor readme: https://github.com/turtleisaac/PokEditor/blob/master/README.md");
//                    System.out.println("If you have any issues with this tool, feel free to contact me for help! You can use the \"Issues\" tab on GitHub, or join the following Discord server: https://discord.gg/cTKQq5Y");
//                    break;
//
//                case "com/turtleisaac/pokeditor/editors/starters":
//                    System.out.println("Starter Editor\n");
//                    System.out.println("This editor, as the name suggests, allows you to change the three starter Pokemon you are able to choose from at the start of the game");
//                    System.out.println("To run the Starter Editor, you need to run PokEditor using the argument \"starters\" (without the quotes), followed by the name of your rom (including .nds) (which needs to be inside of the PokEditor folder)\n");
//                    System.out.println("Unlike most editors in this tool, you make the edits through the command line instead of a spreadsheet for this editor");
//                    System.out.println("It must be noted that for now, the sprites of the starters you can choose from remain as the defaults, but the cry played and the pokemon you receive match what you changed them to. Future updates will fix this and (optionally) automatically adjust the rival's teams");
//                    System.out.println("This editor currently works for only the Sinnoh Gen 4 games (Diamond, Pearl, Platinum)");
//                    break;
//
//                case "com/turtleisaac/pokeditor/editors/intro":
//                    System.out.println("Intro Editor\n");
//                    System.out.println("This editor, as the name suggests, allows you to change the Pokemon that Professor Rowan shows you during the intro at the start of the game");
//                    System.out.println("To run the Intro Editor, you need to run PokEditor using the argument \"intro\" (without the quotes), followed by the name of your rom (including .nds) (which needs to be inside of the PokEditor folder)\n");
//                    System.out.println("Unlike most editors in this tool, you make the edits through the command line instead of a spreadsheet for this editor");
//                    System.out.println("This editor currently works for only the Sinnoh Gen 4 games (Diamond, Pearl, Platinum)");
//                    break;
//
//                case "com/turtleisaac/pokeditor/editors/opening":
//                    System.out.println("Opening Cinematic Cutscene Editor\n");
//                    System.out.println("This editor, as the name suggests, allows you to change the Pokemon that can appear during the opening cinematic cutscene before you open your save file");
//                    System.out.println("To run the Opening Cinematic Cutscene Editor, you need to run PokEditor using the argument \"opening\" (without the quotes), followed by the name of your rom (including .nds) (which needs to be inside of the PokEditor folder)\n");
//                    System.out.println("Unlike most editors in this tool, you make the edits through the command line instead of a spreadsheet for this editor");
//                    System.out.println("This editor currently works for only the Sinnoh Gen 4 games (Diamond, Pearl, Platinum)");
//                    break;
//
//                case "random" :
//                    System.out.println("File Randomizer\n");
//                    System.out.println("This tool can randomize the order of your rom's personal, learnsets, evolutions, etc... files so the data for every species ends up inside of a different sprite, dex entry, cry, etc...");
//                    System.out.println("For example, you could get all of the attributes of Origin Forme Giratina inside of the sprite and cry of an Igglybuff. Fun!");
//                    System.out.println("The resulting files are not guaranteed to permit a possible run. Extreme bad luck could make it so that progression-essential HM's can't be learned by any available species at the start of the game");
//                    System.out.println("To run the File Randomizer on multiple files at once, you need to run PokEditor using the argument \"random\" (without the quotes), the names of every data type you want randomized (separated by spaces), then the name of your rom (including .nds) (which needs to be inside of the PokEditor folder)");
//                    System.out.println("Alternatively, to run the randomizer on an individual data type, you add \"random\" (without the quotes) in between the editor type and the rom name when typing the arguments for another editor (ex: personal random rom.nds)");
//                    System.out.println("This feature only works with the following set of file types: personal (includes stats and tm learnsets), learnsets, growth, moves, encounters (does all types together), items (not recommended at all, makes game impossible), and evolutions");
//                    System.out.println("One thing that must be noted is that unlike all of the other tools/ editors for editing the data in a rom, the File Randomizer WILL overwrite the rom that you supply. Make sure to have a copy of your rom stored elsewhere before randomizing");
//                    System.out.println("This tool currently works for all games, both Gen 4 and 5 (however, some randomization types will not work due to how the data is stored)");
//                    break;
//
//                case "com/turtleisaac/pokeditor/editors/tutors":
//                    System.out.println("Tutor Editor\n");
//                    System.out.println("This editor, as the name suggests, allows you to change the moves taught by the move tutors in the game and every species' compatibility with those moves");
//                    System.out.println("To run the Tutor Editor, you need to run PokEditor using the argument \"tutors\" (without the quotes), followed by the name of your rom (including .nds) (which needs to be inside of the PokEditor folder)\n");
//                    System.out.println("The spreadsheets that are generated by running this command (and therefore need to be exported from the templates when you are done editing) are \"tutorMoveData.csv\" and \"tutorCompatibilityData.csv\"");
//                    System.out.println("This editor currently works for only the Sinnoh Gen 4 games (Diamond, Pearl, Platinum)");
//                    break;
//
//                case "com/turtleisaac/pokeditor/editors/babies":
//                    System.out.println("Baby Form Editor\n");
//                    System.out.println("This editor, as the name suggests, allows you to change species of Pokemon created when hatching an egg created by a female member of a species (note: incense babies/ held-item dependent babies are not part of this editor)");
//                    System.out.println("To run the Baby Form Editor, you need to run PokEditor using the argument \"babies\" (without the quotes), followed by the name of your rom (including .nds) (which needs to be inside of the PokEditor folder)\n");
//                    System.out.println("The spreadsheet that is generated by running this command (and therefore needs to be exported from the templates when you are done editing) is \"babyFormsData.csv\"");
//                    System.out.println("This editor currently works for all Gen 4 games");
//                    break;
//
//                case "arm9" :
//                    System.out.println("ARM9 Extractor");
//                    System.out.println("This tool, as the name suggests, can extract the arm9 of a rom");
//                    System.out.println("To run the ARM9 Extractor, you need to run PokEditor using the argument \"arm9\" (without the quotes), followed by the name of your rom (including .nds) (which needs to be inside of the PokEditor folder)\n");
//                    System.out.println("This tool currently works for all games, both Gen 4 and 5");
//                    break;
//
//                case "arm7" :
//                    System.out.println("ARM7 Extractor");
//                    System.out.println("This tool, as the name suggests, can extract the arm7 of a rom");
//                    System.out.println("To run the ARM7 Extractor, you need to run PokEditor using the argument \"arm7\" (without the quotes), followed by the name of your rom (including .nds) (which needs to be inside of the PokEditor folder)\n");
//                    System.out.println("This tool currently works for all games, both Gen 4 and 5");
//                    break;
//
////                case "babies_sp" :
////                    System.out.println("Special Baby Form Editor\n");
////                    System.out.println("This editor, as the name suggests, allows you to change species of Pokemon created when hatching an egg created by a female member of a species under special conditions (such as having a held item)");
////                    System.out.println("To run the Special Baby Form Editor, you need to run PokEditor using the argument \"babies_sp\" (without the quotes), followed by the name of your rom (including .nds) (which needs to be inside of the PokEditor folder)\n");
////                    System.out.println("The spreadsheet that is generated by running this command (and therefore needs to be exported from the templates when you are done editing) is \"specialBabyFormsData.csv\"");
////                    System.out.println("This editor currently works for only the Sinnoh Gen 4 games (Diamond, Pearl, Platinum)");
////                    break;
//
//                case "help" :
//                    System.out.println("Help Command\n");
//                    System.out.println("Why the heck are you using the help command to figure out how to use the help command? That's like using an index file to look up the location of the index file!");
//                    System.out.println("Stop digging around the tool for easter eggs and just use the darn thing!");
//                    break;
//
//                case "turtleisaac" :
//                    System.out.println("Turtleisaac\n");
//                    System.out.println("An awesome dude who creates awesome tools");
//                    break;
//
//                default:
//                    System.out.println("\n\nYou did not specify one of the above editors/ tools. Please run the program and try again");
//                    break;
//            }
//            System.exit(0);
//        }
//
//        /**
//         * Narctowl Access
//         */
//        if (args[0].equalsIgnoreCase("narc"))
//        {
//            Narctowl narctowl= new Narctowl(true);
//            switch (args[1].toLowerCase())
//            {
//                case "unpack" :
//                    System.out.println("Name of the narc to unpack? (include .narc)");
////                    narctowl.unpack(scanner.nextLine());
//                    break;
//
//                case "pack" :
//                    System.out.println("Name of the folder to pack?");
//                    String dirName= scanner.nextLine();
//                    System.out.println("Name to give to output narc? (include .narc)");
//                    String outputName= scanner.nextLine();
//                    narctowl.pack("extracted" + File.separator + dirName,outputName,"");
//                    break;
//
//                default:
//                    throw new RuntimeException("Invalid arguments. Valid arguments for accessing Narctowl are either \"narc unpack\" or \"narc pack\"");
//            }
//            System.exit(0);
//        }
//
//        /**
//         * Compression/ Decompression
//         */
//        if(args.length == 2 && (args[0].equalsIgnoreCase("compress") || args[0].equalsIgnoreCase("decompress")))
//        {
//            System.out.println("\n----------------------------LZ Compressor/ Decompressor----------------------------\n");
//
//            BLZCoder coder= new BLZCoder(null);
//            if(!new File(path + args[1]).isDirectory())
//            {
//                System.out.println("Enter a name for the output file (you should probably use the same file extension as the original file)");
//                System.out.println("\nThe file will be output in the same directory as the original file, but the original file needs to be somewhere within the PokEditor folder\n");
//                String name= scanner.nextLine();
//                Buffer buffer= new Buffer(path + args[1]);
//                byte[] contents= buffer.readRemainder();
//                int lastIndex= args[1].lastIndexOf(File.separator) != -1 ? args[1].lastIndexOf(File.separator) : 0;
//                BinaryWriter writer= new BinaryWriter(path + args[1].substring(0,lastIndex) + name);
//
//                if(args[0].equalsIgnoreCase("compress"))
//                {
//                    System.out.println("\nIs this an arm9? (y/N)\n");
//                    boolean arm9= scanner.nextLine().trim().equalsIgnoreCase("y");
//                    writer.write(coder.BLZ_EncodePub(contents,arm9,true,name));
//                }
//                else if(args[0].equalsIgnoreCase("decompress"))
//                {
//                    writer.write(coder.BLZ_DecodePub(contents,name));
//                }
//                writer.close();
//                buffer.close();
//                System.out.println("Process complete. Output can be found at: " + path + args[1].substring(0,lastIndex) + name);
//            }
//            else
//            {
//                System.out.print("\n\nThe specified path is a directory and not a file. Continuing will produce a folder containing ");
//                String newDir= path + args[1];
//                boolean compress= false;
//                if(args[0].equalsIgnoreCase("compress"))
//                {
//                    System.out.print("compressed");
//                    newDir+= "_compressed";
//                    compress= true;
//                }
//                else if(args[0].equalsIgnoreCase("decompress"))
//                {
//                    System.out.print("decompressed");
//                    newDir+= "_decompressed";
//                }
//                System.out.println(" versions of all of its contents. For this to work, you can't have any folders within the specified folder.\n");
//                System.out.println("Would you like to continue? (y/N)\n");
//
//                if(!scanner.nextLine().equalsIgnoreCase("y"))
//                {
//                    if(!new File(newDir).exists() && !new File(newDir).mkdir())
//                    {
//                        throw new RuntimeException("Unable to create output directory. Check write perms");
//                    }
//
//                    newDir+= File.separator;
//
//                    List<File> fileList = new ArrayList<>(Arrays.asList(Objects.requireNonNull(new File(path + args[1]).listFiles())));
//                    fileList.removeIf(File::isHidden);
//                    File[] files = fileList.toArray(new File[0]);
//                    Buffer buffer;
//                    BinaryWriter writer;
//                    byte[] contents;
//
//                    for(File file : files)
//                    {
//                        if(file.isDirectory())
//                            throw new RuntimeException("You can't have a folder in the folder of files to be compressed.");
//                        buffer= new Buffer(file.toString());
//                        contents= buffer.readRemainder();
//                        writer= new BinaryWriter(newDir + file.getName());
//                        if(compress)
//                        {
//                            writer.write(coder.BLZ_EncodePub(contents,false,true,file.getName()));
//                        }
//                        else
//                        {
//                            writer.write(coder.BLZ_DecodePub(contents,file.getName()));
//                        }
//                        writer.close();
//                        buffer.close();
//                    }
//                    System.out.println("Process complete. Output directory can be found at: " + newDir);
//                }
//
//            }
//            System.exit(0);
//        }
//
//        /**
//         * Running DsRomReader/ PokEditor
//         */
//
//        DsRomReader reader;
//        if(args[0].equalsIgnoreCase("random"))
//        {
//            String[] randomizerEditors= new String[args.length-2];
//            System.arraycopy(args,1,randomizerEditors,0,args.length-2);
//            if(randomizerEditors.length != 1)
//            {
//                for (String randomizerEditor : randomizerEditors)
//                {
//                    System.out.println("Randomizing: " + randomizerEditor);
//                    reader = new DsRomReader(scanner, true);
//                    reader.readRom(new String[]{randomizerEditor, "random", args[args.length - 1]});
//                }
//            }
//            else
//            {
//                reader = new DsRomReader(scanner, false);
//                reader.readRom(new String[]{randomizerEditors[0], "random", args[args.length - 1]});
//            }
//        }
//        else
//        {
//            reader= new DsRomReader(scanner,false);
//            reader.readRom(args);
//        }
//    }
//
//    private String path= System.getProperty("user.dir") + File.separator;
//    private String[] romCapacities= new String[13];
//    private String rom;
//    private String tempPath= "temp" + File.separator;
//    private String tempPathUnpack;
////    private String outputPath= tempPath + "rom";
//    private Buffer buffer;
//    private RomData romData;
//    private ArrayList<FimgEntry> fimgEntries;
//    private ArrayList<FimgEntry> sortedEntries;
//    private ArrayList<OverlayData> overlayEntries;
//    private int fileOffset;
//    private int length;
//    private int fileID;
//    private int overlayID;
//    private int newFileLength;
//    private String type;
//    private ArrayList<Long> rootContents= new ArrayList<>();
//    private static String[] nameData;
//    private static boolean isNarc;
//    private boolean isSpreadsheet;
//    private Scanner scanner;
//    private String gameCode;
//    private boolean multiRun;
//    private boolean compressedArm9;
//
//    private HashMap<String,HashMap<String,Integer>> shortcutMap; //Format: Hashmap<Game code, Hashmap<Shortcut name, File offset>>
//
//
//    public DsRomReader(Scanner scanner, boolean multiRun) throws IOException, ClassNotFoundException
//    {
//        this.scanner= scanner;
//        this.multiRun= multiRun;
//
//        Arrays.fill(romCapacities,"");
//        romCapacities[6]= "8MB";
//        romCapacities[7]= "16MB";
//        romCapacities[8]= "32MB";
//        romCapacities[9]= "64MB";
//        romCapacities[10]= "128MB";
//        romCapacities[11]= "256MB";
//        romCapacities[12]= "512MB";
//
////        new File(outputPath).mkdirs();
//        if(new File(path + "Program Files" + File.separator + "Shortcuts.ser").exists())
//        {
//            ObjectInputStream objIn= new ObjectInputStream(new FileInputStream(path + "Program Files" + File.separator + "Shortcuts.ser"));
//            shortcutMap= (HashMap<String,HashMap<String,Integer>>) objIn.readObject();
//        }
//        else
//        {
//            shortcutMap= new HashMap<>();
//            HashMap<String,Integer> platinum= new HashMap<>();
//            platinum.put("com/turtleisaac/pokeditor/editors/personal",0x1a5);
//            platinum.put("com/turtleisaac/pokeditor/editors/learnsets",0x1a7);
//            platinum.put("com/turtleisaac/pokeditor/editors/evolutions",0x1a1);
//            platinum.put("com/turtleisaac/pokeditor/editors/growth",0x1a4);
//            platinum.put("com/turtleisaac/pokeditor/editors/encounters",0x14a);
//            platinum.put("com/turtleisaac/pokeditor/editors/items",0x192);
//            platinum.put("com/turtleisaac/pokeditor/editors/moves",0x1bd);
//            platinum.put("com/turtleisaac/pokeditor/editors/babies",0x1a6);
//
//            HashMap<String,Integer> diamond= new HashMap<>();
//            diamond.put("com/turtleisaac/pokeditor/editors/personal",0x146);
//            diamond.put("com/turtleisaac/pokeditor/editors/learnsets",0x148);
//            diamond.put("com/turtleisaac/pokeditor/editors/evolutions",0x144);
//            diamond.put("com/turtleisaac/pokeditor/editors/growth",0x145);
//            diamond.put("com/turtleisaac/pokeditor/editors/encounters",0x107);
//            diamond.put("com/turtleisaac/pokeditor/editors/items",0x13a);
//            diamond.put("com/turtleisaac/pokeditor/editors/moves",0x158);
//            diamond.put("com/turtleisaac/pokeditor/editors/babies",0x147);
//
//            HashMap<String,Integer> pearl= new HashMap<>();
//            pearl.put("com/turtleisaac/pokeditor/editors/personal",0x146);
//            pearl.put("com/turtleisaac/pokeditor/editors/learnsets",0x148);
//            pearl.put("com/turtleisaac/pokeditor/editors/evolutions",0x144);
//            pearl.put("com/turtleisaac/pokeditor/editors/growth",0x145);
//            pearl.put("com/turtleisaac/pokeditor/editors/encounters",0x108);
//            pearl.put("com/turtleisaac/pokeditor/editors/items",0x13a);
//            pearl.put("com/turtleisaac/pokeditor/editors/moves",0x158);
//            pearl.put("com/turtleisaac/pokeditor/editors/babies",0x147);
//
//            HashMap<String,Integer> heartGold= new HashMap<>();
//            heartGold.put("com/turtleisaac/pokeditor/editors/personal",0x83);
//            heartGold.put("com/turtleisaac/pokeditor/editors/learnsets",0xa2);
//            heartGold.put("com/turtleisaac/pokeditor/editors/evolutions",0xa3);
//            heartGold.put("com/turtleisaac/pokeditor/editors/growth",0x84);
//            heartGold.put("com/turtleisaac/pokeditor/editors/encounters",0xa6);
//            heartGold.put("com/turtleisaac/pokeditor/editors/items",0x92);
//            heartGold.put("com/turtleisaac/pokeditor/editors/moves",0x8c);
//            heartGold.put("com/turtleisaac/pokeditor/editors/babies",0x1ff);
//
//            HashMap<String,Integer> soulSilver= new HashMap<>();
//            soulSilver.put("com/turtleisaac/pokeditor/editors/personal",0x83);
//            soulSilver.put("com/turtleisaac/pokeditor/editors/learnsets",0xa2);
//            soulSilver.put("com/turtleisaac/pokeditor/editors/evolutions",0xa3);
//            soulSilver.put("com/turtleisaac/pokeditor/editors/growth",0x84);
//            soulSilver.put("com/turtleisaac/pokeditor/editors/encounters",0xa6);
//            soulSilver.put("com/turtleisaac/pokeditor/editors/items",0x92);
//            soulSilver.put("com/turtleisaac/pokeditor/editors/moves",0x8c);
//            soulSilver.put("com/turtleisaac/pokeditor/editors/babies",0x1ff);
//
//            HashMap<String,Integer> blackWhite= new HashMap<>();
//            blackWhite.put("com/turtleisaac/pokeditor/editors/personal",0x102);
//            blackWhite.put("com/turtleisaac/pokeditor/editors/learnsets",0x104);
//            blackWhite.put("com/turtleisaac/pokeditor/editors/evolutions",0x105);
//            blackWhite.put("com/turtleisaac/pokeditor/editors/growth",0x103);
//            blackWhite.put("com/turtleisaac/pokeditor/editors/encounters",0x170);
//            blackWhite.put("com/turtleisaac/pokeditor/editors/items",0x0);
//            blackWhite.put("com/turtleisaac/pokeditor/editors/moves",0x158);
//
//            HashMap<String,Integer> black2White2= new HashMap<>();
//            black2White2.put("com/turtleisaac/pokeditor/editors/personal",0x102);
//            black2White2.put("com/turtleisaac/pokeditor/editors/learnsets",0x104);
//            black2White2.put("com/turtleisaac/pokeditor/editors/evolutions",0x105);
//            black2White2.put("com/turtleisaac/pokeditor/editors/growth",0x103);
//            black2White2.put("com/turtleisaac/pokeditor/editors/encounters",0x170);
//            black2White2.put("com/turtleisaac/pokeditor/editors/items",0x0);
//            black2White2.put("com/turtleisaac/pokeditor/editors/moves",0x158);
//
//            shortcutMap.put("Platinum",platinum);
//            shortcutMap.put("Diamond",diamond);
//            shortcutMap.put("Pearl",pearl);
//            shortcutMap.put("HeartGold",heartGold);
//            shortcutMap.put("SoulSilver",soulSilver);
//            shortcutMap.put("BlackWhite",blackWhite);
//            shortcutMap.put("Black2White2",black2White2);
//
//
//        }
//
//    }
//
//    private void readRom(String[] args) throws Exception
//    {
////        if(args[0].equalsIgnoreCase("starters2"))
////        {
////            throw new RuntimeException("Do not run PokEditor with the argument \"starters2\", as that is an internal parameter only. To run the Starter Editor, please use the argument \"starters\"");
////        }
//
//
//        if(new File(path + "temp").exists() && (!args[1].equalsIgnoreCase("random") || !args[0].equalsIgnoreCase("random")))
//        {
//            clearDirectory(new File(path + "temp"));
//        }
//        String rom= args[args.length-1];
//        this.rom= path + rom;
//        buffer= new Buffer(rom);
//        String title= readHeader();
//
//        if(args[0].equalsIgnoreCase("arm9"))
//        {
//            readArm9();
//            if(compressedArm9)
//            {
//                System.out.println("Decompress the arm9? (Y/n)");
//                String ans= scanner.nextLine();
//                if(!ans.equalsIgnoreCase("n"))
//                {
//                    Buffer arm9Buffer= new Buffer(path + "arm9.bin");
//                    byte[] arm9= arm9Buffer.readRemainder();
//                    BLZCoder blzCoder= new BLZCoder(null);
//                    BinaryWriter writer= new BinaryWriter(path + "arm9.bin");
//                    writer.write(blzCoder.BLZ_DecodePub(arm9,"arm9"));
//                    writer.close();
//                }
//            }
//            System.out.println("Process completed. Output can be found at: " + path + "arm9.bin");
//
//            System.exit(0);
//        }
//        else if(args[0].equalsIgnoreCase("arm7"))
//        {
//            readArm7();
//            System.out.println("Process completed. Output can be found at: " + path + "arm7.bin");
//            System.exit(0);
//        }
//        else if(args[0].equalsIgnoreCase("find") && args.length >= 2)
//        {
//            readArm9();
//            if(compressedArm9)
//            {
//                Buffer arm9Buffer= new Buffer(path + "arm9.bin");
//                byte[] arm9= arm9Buffer.readRemainder();
//                BLZCoder blzCoder= new BLZCoder(null);
//                BinaryWriter writer= new BinaryWriter(path + "arm9.bin");
//                writer.write(blzCoder.BLZ_DecodePub(arm9,"arm9"));
//                writer.close();
//            }
//            emptySpaceFinder(Integer.parseInt(args[1]));
//            System.exit(0);
//        }
//
//
//
////        readFntb();
//
//
//        readFatb();
//
//        if(args[0].equalsIgnoreCase("extract"))
//        {
//            System.out.println("\n----------------------------File Extractor----------------------------\n");
//            if(args.length == 3)
//            {
//                try
//                {
//                    extractFile(Integer.parseInt(args[1]));
//                }
//                catch (NumberFormatException e)
//                {
////                    extractFile(args[1]);
//                }
//            }
//            else if(args.length == 2)
//            {
//                System.out.println("Enter the name (or ID in base 10) of the file to be extracted");
//                String str= scanner.nextLine();
//                System.out.println();
//                try
//                {
//                    extractFile(Integer.parseInt(str));
//                }
//                catch (NumberFormatException e)
//                {
////                    extractFile(str);
//                }
//            }
//
//        }
//
//
//
//        grabFile(args, title, null,false);
//
//
////        if(args[0].equalsIgnoreCase("starters"))
////        {
////            startersHasRan= true;
////            args= new String[] {"starters2", "temp" + File.separator + "rom1.nds"};
////            buffer.close();
////
////            rom= args[args.length-1];
////            this.rom= path + rom;
////            buffer= new Buffer(rom);
////            title= readHeader();
////            readFatb();
////            grabFile(args,title);
////        }
//
//        if(!args[1].equalsIgnoreCase("random") && new File(path + "temp" + File.separator + "random.ser").exists())
//        {
//            System.out.println("Do you wish to delete the temp folder? (Y/n)");
//            String ans= scanner.nextLine();
//            if(!ans.equalsIgnoreCase("n"))
//            {
//                clearDirectory(new File(path + "temp"));
//            }
//        }
//    }
//
//    protected String readHeader() throws IOException
//    {
//        String title= buffer.readString(12).trim();
//        String gameCode= buffer.readString(4);
//        String makerCode= buffer.readString(2);
//        int deviceCode= buffer.readByte();
//        int encryptionSeed= buffer.readByte();
//        byte romChipCapacity= buffer.readBytes(1)[0];
//        byte[] reserved1= buffer.readBytes(7);
//        byte reserved2= (byte)buffer.readByte();
//        int systemRegion= buffer.readByte();
//        int romVersion= buffer.readByte();
//        int autoStartFlag= buffer.readByte();
//
//        int arm9Offset= buffer.readInt();
//        if(arm9Offset < 0x4000) {
//            throw new RuntimeException("Invalid ROM Header: ARM9 Offset");
//        }
//        int arm9EntryAddress= buffer.readInt();
//        if(!(arm9EntryAddress >= 0x2000000 && arm9EntryAddress <= 0x23BFE00)) {
//            throw new RuntimeException("Invalid ROM Header: ARM9 Entry Address");
//        }
//        int arm9LoadAddress= buffer.readInt();
//        if(!(arm9LoadAddress >= 0x2000000 && arm9LoadAddress <= 0x23BFE00)) {
//            throw new RuntimeException("Invalid ROM Header: ARM9 RAM Address");
//        }
//        int arm9Length= buffer.readInt();
//        if(arm9Length > 0x3BFE00) {
//            throw new RuntimeException("Invalid ROM Header: ARM9 Size");
//        }
//        int arm7Offset= buffer.readInt();
//        if(arm7Offset < 0x8000) {
//            throw new RuntimeException("Invalid ROM Header: ARM7 Offset");
//        }
//        int arm7EntryAddress= buffer.readInt();
//        if(!((arm7EntryAddress >= 0x2000000 && arm7EntryAddress <= 0x23BFE00) || (arm7EntryAddress >= 0x37F8000 && arm7EntryAddress <= 0x3807E00))) {
//            throw new RuntimeException("Invalid ROM Header: ARM7 Entry Address");
//        }
//        int arm7LoadAddress= buffer.readInt();
//        if(!((arm7LoadAddress >= 0x2000000 && arm7LoadAddress <= 0x23BFE00) || (arm7LoadAddress >= 0x37F8000 && arm7LoadAddress <= 0x3807E00))) {
//            throw new RuntimeException("Invalid ROM Header: ARM7 Load Address");
//        }
//        int arm7Length= buffer.readInt();
//        if(arm7Length > 0x3BFE0) {
//            throw new RuntimeException("Invalid ROM Header: ARM7 Size");
//        }
//
//        int fntbOffset= buffer.readInt();
//        int fntbLength= buffer.readInt();
//
//        int fatbOffset= buffer.readInt();
//        int fatbLength= buffer.readInt();
//
//        int arm9OverlayOffset= buffer.readInt();
//        int amr9OverlayLength= buffer.readInt();
//
//        int arm7OverlayOffset= buffer.readInt();
//        int arm7OverlayLength= buffer.readInt();
//
//        int normalCardControlRegisterSettings= buffer.readInt();
//        int secureCardControlRegisterSettings= buffer.readInt();
//
//        int iconBannerOffset= buffer.readInt();
//        short secureAreaCrc= buffer.readShort();
//        short secureTransferTimeout= buffer.readShort();
//        int arm9Autoload= buffer.readInt();
//        int arm7Autoload= buffer.readInt();
//        byte[] secureDisable= buffer.readBytes(8);
//
//        int romLength= buffer.readInt();
//        int headerLength= buffer.readInt();
//
//        byte[] reserved3= buffer.readBytes(212);
//        byte[] reserved4= buffer.readBytes(16);
//        byte[] nintendoLogo= buffer.readBytes(0x156);
//        short nintendoLogoCrc= buffer.readShort();
//        short headerCrc= buffer.readShort();
//        int debugRomOffset= buffer.readInt();
//        int debugLength= buffer.readInt();
//        int debugRamOffset= buffer.readInt();
//        int reserved5= buffer.readInt();
//        byte[] reserved6= buffer.readBytes(144);
//
//        romData= new RomData() {
//            @Override
//            public String getTitle() {
//                return title;
//            }
//
//            @Override
//            public String getGameCode() {
//                return gameCode;
//            }
//
//            @Override
//            public String getMakerCode() {
//                return makerCode;
//            }
//
//            @Override
//            public int getDeviceCode() {
//                return deviceCode;
//            }
//
//            @Override
//            public int getEncryptionSeed() {
//                return encryptionSeed;
//            }
//
//            @Override
//            public byte getDeviceCapacity() {
//                return romChipCapacity;
//            }
//
//            @Override
//            public byte[] getReserved() {
//                return reserved1;
//            }
//
//            @Override
//            public byte getReserved2() {
//                return reserved2;
//            }
//
//            @Override
//            public int getSystemRegion() {
//                return systemRegion;
//            }
//
//            @Override
//            public int getRomVersion() {
//                return romVersion;
//            }
//
//            @Override
//            public int getAutoStartFlag() {
//                return autoStartFlag;
//            }
//
//            @Override
//            public int getArm9Offset() {
//                return arm9Offset;
//            }
//
//            @Override
//            public int getArm9EntryAddress() {
//                return arm9EntryAddress;
//            }
//
//            @Override
//            public int getArm9LoadAddress() {
//                return arm9LoadAddress;
//            }
//
//            @Override
//            public int getArm9Length() {
//                return arm9Length;
//            }
//
//            @Override
//            public int getArm7Offset() {
//                return arm7Offset;
//            }
//
//            @Override
//            public int getArm7EntryAddress() {
//                return arm7EntryAddress;
//            }
//
//            @Override
//            public int getArm7LoadAddress() {
//                return arm7LoadAddress;
//            }
//
//            @Override
//            public int getArm7Length() {
//                return arm7Length;
//            }
//
//            @Override
//            public int getFntbOffset() {
//                return fntbOffset;
//            }
//
//            @Override
//            public int getFntbLength() {
//                return fntbLength;
//            }
//
//            @Override
//            public int getFatbOffset() {
//                return fatbOffset;
//            }
//
//            @Override
//            public int getFatbLength() {
//                return fatbLength;
//            }
//
//            @Override
//            public int getArm9OverlayOffset() {
//                return arm9OverlayOffset;
//            }
//
//            @Override
//            public int getArm9OverlayLength() {
//                return amr9OverlayLength;
//            }
//
//            @Override
//            public int getArm7OverlayOffset() {
//                return arm7OverlayOffset;
//            }
//
//            @Override
//            public int getArm7OverlayLength() {
//                return arm7OverlayLength;
//            }
//
//            @Override
//            public int getNormalCardControlRegisterSettings() {
//                return normalCardControlRegisterSettings;
//            }
//
//            @Override
//            public int getSecureCardControlRegisterSettings() {
//                return secureCardControlRegisterSettings;
//            }
//
//            @Override
//            public int getIconBannerOffset() {
//                return iconBannerOffset;
//            }
//
//            @Override
//            public int getSecureAreaCrc() {
//                return secureAreaCrc;
//            }
//
//            @Override
//            public short getSecureTransferTimeout() {
//                return secureTransferTimeout;
//            }
//
//            @Override
//            public int getArm9Autoload() {
//                return arm9Autoload;
//            }
//
//            @Override
//            public int getArm7Autoload() {
//                return arm7Autoload;
//            }
//
//            @Override
//            public byte[] getSecureDisable() {
//                return secureDisable;
//            }
//
//            @Override
//            public int getRomLength() {
//                return romLength;
//            }
//
//            @Override
//            public int getHeaderLength() {
//                return headerLength;
//            }
//
//            @Override
//            public byte[] getReserved3() {
//                return reserved3;
//            }
//
//            @Override
//            public byte[] getReserved4() {
//                return reserved4;
//            }
//
//            @Override
//            public byte[] getNintendoLogo() {
//                return nintendoLogo;
//            }
//
//            @Override
//            public short getNintendoLogoCrc() {
//                return nintendoLogoCrc;
//            }
//
//            @Override
//            public short getHeaderCrc() {
//                return headerCrc;
//            }
//
//            @Override
//            public int getDebugRomOffset() {
//                return debugRomOffset;
//            }
//
//            @Override
//            public int getDebugLength() {
//                return debugLength;
//            }
//
//            @Override
//            public int getDebugRamOffset() {
//                return debugRamOffset;
//            }
//
//            @Override
//            public int getReserved5() {
//                return reserved5;
//            }
//
//            @Override
//            public byte[] getReserved6() {
//                return reserved6;
//            }
//        };
//
//        System.out.println("Title: " + romData.getTitle());
//        System.out.println("Game Code: " + romData.getGameCode());
//        System.out.println("Maker Code: " + romData.getMakerCode());
//        System.out.println("Device Code: " + romData.getDeviceCode());
//        System.out.println("Encryption Seed: " + romData.getEncryptionSeed());
//        System.out.println("File Length: " + romCapacities[romData.getDeviceCapacity()]);
//        System.out.println("Reserved 1: " + Arrays.toString(romData.getReserved()));
//        System.out.println("Reserved 2: " + romData.getReserved2());
//        System.out.println("System Region: " + romData.getSystemRegion());
//        System.out.println("Rom Version: " + romData.getRomVersion());
//        System.out.println("Internal Flags: " + romData.getAutoStartFlag());
//        System.out.println("Arm9 Offset: " + romData.getArm9Offset());
//        System.out.println("Arm9 Entry Address: " + romData.getArm9EntryAddress());
//        System.out.println("Arm9 Load Address: " + romData.getArm9LoadAddress());
//        System.out.println("Arm9 Length: " + romData.getArm9Length());
//        System.out.println("Arm7 Offset: " + romData.getArm7Offset());
//        System.out.println("Arm7 Entry Address: " + romData.getArm7EntryAddress());
//        System.out.println("Arm7 Load Address" + romData.getArm7LoadAddress());
//        System.out.println("Arm7 Length: " + romData.getArm7Length());
//        System.out.println("Fntb Offset: " + romData.getFntbOffset());
//        System.out.println("Fntb Length: " + romData.getFntbLength());
//        System.out.println("Fatb Offset: " + romData.getFatbOffset());
//        System.out.println("Fatb Length: " + romData.getFatbLength());
//        System.out.println("Arm9 Overlay Offset: " + romData.getArm9OverlayOffset());
//        System.out.println("Arm9 Overlay Length: " + romData.getArm9OverlayLength());
//        System.out.println("Arm7 Overlay Offset: " + romData.getArm7OverlayOffset());
//        System.out.println("Arm7 Overlay Length: " + romData.getArm7OverlayLength());
//        System.out.println("Normal Card Control Register Settings: " + romData.getNormalCardControlRegisterSettings());
//        System.out.println("Secure Card Control Register Settings: " + romData.getSecureCardControlRegisterSettings());
//        System.out.println("Icon Banner Offset: " + romData.getIconBannerOffset());
//        System.out.println("Secure Area Crc: " + romData.getSecureAreaCrc());
//        System.out.println("Secure Transfer Timeout: " + romData.getSecureTransferTimeout());
//        System.out.println("Arm9 Autoload: " + romData.getArm9Autoload());
//        System.out.println("Arm7 Autoload: " + romData.getArm7Autoload());
//        System.out.println("Secure Disable: " + Arrays.toString(romData.getSecureDisable()));
//        System.out.println("Rom Length: " + romData.getRomLength());
//        System.out.println("Header Length: " + romData.getHeaderLength());
//        System.out.println("Reserved 3: " + Arrays.toString(romData.getReserved3()));
//        System.out.println("Reserved 4: " + Arrays.toString(romData.getReserved4()));
//        System.out.println("Nintendo Logo: " + Arrays.toString(romData.getNintendoLogo()));
//        System.out.println("Nintendo Logo Crc: " + romData.getNintendoLogoCrc());
//        System.out.println("Header Crc: " + romData.getHeaderCrc());
//        System.out.println("Debug Rom Offset: " + romData.getDebugRomOffset());
//        System.out.println("Debug Length: " + romData.getDebugLength());
//        System.out.println("Debug Ram Offset: " + romData.getDebugRamOffset());
//        System.out.println("Reserved 5: " + romData.getReserved5());
//        System.out.println("Reserved 6: " + Arrays.toString(romData.getReserved6()));
//
//        System.out.println("End of header: " + buffer.getPosition() + "\n");
//
//        String region= gameCode.substring(3);
//        String[] titles= new String[] {"POKEMON HG","POKEMON SS","POKEMON D","POKEMON P","POKEMON PL","POKEMON B","POKEMON W","POKEMON B2","POKEMON W2"};
//        String[] gameCodes= new String[] {"IPK" + region,"IPG" + region,"ADA" + region,"APA" + region,"CPU" + region,"IRB" + region,"IRW" + region,"IRE" + region,"IRD" + region};
//        String finalTitle= title;
//
//        boolean invalid= true;
//        for(String game : titles)
//        {
//            if (!finalTitle.trim().equalsIgnoreCase(game))
//            {
//                invalid= false;
//                break;
//            }
//        }
//
//        if(invalid)
//        {
//            System.out.println("Invalid rom header. Please specify what game this is using the following options (MAKE SURE TO SPELL CORRECTLY): Diamond, Pearl, Platinum, HeartGold, SoulSilver, Black, White, Black2, White2");
//            finalTitle= scanner.nextLine().toLowerCase();
//            for(int i= 0; i < titles.length; i++)
//            {
//                String game= titles[i];
//                if (finalTitle.trim().equalsIgnoreCase(game))
//                {
//                    this.gameCode= gameCodes[i];
//                }
//                else
//                {
//                    throw new RuntimeException("Invalid game: " + finalTitle);
//                }
//            }
//        }
//        else
//        {
//            this.gameCode= gameCode;
//        }
//
//        if(!gameCode.toLowerCase().contains("ada") && !gameCode.toLowerCase().contains("apa") && !gameCode.toLowerCase().contains("cpu"))
//        {
//            compressedArm9 = true;
//        }
//
//
//        return finalTitle;
//    }
//
////    public void readFntb() throws IOException
////    {
////        buffer.skipTo(romData.getFntbOffset());
////
////        int fntbOffset= buffer.getPosition();
////        int fntbEndOffset= buffer.getPosition() + romData.getFatbLength();
////
////        BinaryWriter writer= new BinaryWriter(path + "fntb.bin");
////        writer.write(buffer.readBytes(romData.getFntbLength()));
////        buffer.close();
////
////        buffer= new Buffer(rom);
////        buffer.skipTo(romData.getFntbOffset());
////
////        int subTableOffset= buffer.readInt();
////        int firstSubTableFileId= buffer.readUIntS();
////        int numDirs= buffer.readUIntS();
////
////        while(buffer.getPosition() != )
////        {
////
////        }
////
////        int fileId= firstSubTableFileId;
////        while(buffer.getPosition() != fntbEndOffset)
////        {
////            int identifier= buffer.readByte();
////            if(identifier == 0)
////            {
//////                System.out.println("End of sub-table");
////            }
////            else if(identifier <= 0x7f)
////            {
////                String name= buffer.readString(identifier);
////                System.out.println("File Name: " + name);
////
////                int id= fileId++;
////                System.out.println("File ID: 0x" + hexFormat(Integer.toHexString(id)));
////            }
////            else if (identifier >= 0x81)
////            {
////                identifier-= 0x80;
////                String name= buffer.readString(identifier);
////                System.out.println("Sub-Directory Name: " + name);
////
////                int subDirId= buffer.readUIntS();
////                System.out.println("Sub-Directory ID: 0x" + hexFormat(Integer.toHexString(subDirId)));
////            }
////            else
////            {
//////                System.out.println("Reserved");
////            }
////
////            System.out.println();
////        }
////    }
//
//    public void readArm9() throws IOException
//    {
//        buffer.skipTo(romData.getArm9Offset());
////        BinaryWriter writer= new BinaryWriter(outputPath + File.separator + "arm9.bin");
//        BinaryWriter writer= new BinaryWriter(path + "arm9.bin");
//        writer.write(buffer.readBytes(romData.getArm9Length()));
//        writer.close();
//    }
//
//    public void readArm7() throws IOException
//    {
//        buffer.skipTo(romData.getArm7Offset());
////        BinaryWriter writer= new BinaryWriter(outputPath + File.separator + "arm7.bin");
//        BinaryWriter writer= new BinaryWriter(path + "arm7.bin");
//        writer.write(buffer.readBytes(romData.getArm7Length()));
//        writer.close();
//    }
//
//    public void readFntb()
//    {
//        buffer.skipTo(romData.getFntbOffset());
//        System.out.println(buffer.getPosition() + "\n");
//        HashMap<String,String> map= new HashMap<>();
//
//        /** Main Table */
//        ArrayList<FntbEntry> fntbEntries= new ArrayList<>();
//        int fntbPos= 0;
//        long subTableOffset = buffer.readUIntI();
//        long subTableFirstFileId = buffer.readUIntS();
//        long numDirs = buffer.readUIntS();
//        System.out.println("ID: 0xf000");
//        System.out.println("Offset to Sub-table: 0x" + Long.toHexString(subTableOffset));
//        System.out.println("ID of first file in Sub-table: 0x" + Long.toHexString(subTableFirstFileId));
//        System.out.println("Number of files: 0x" + Long.toHexString(numDirs) + " (" + numDirs + ")\n");
//        fntbPos+= 8;
//        int id= 1;
////        directories.add(new Directory() {
////            @Override
////            public String getName() {
////                return "root";
////            }
////
////            @Override
////            public String getHexId() {
////                return "f000";
////            }
////
////            @Override
////            public String getParentDir() {
////                return new File(tempPath).getName();
////            }
////
////            @Override
////            public File getFile() {
////                return new File(tempPath + File.separator + "root");
////            }
////        });
////        System.out.println("Path: " + path + generatePath(directories.get(0)));
////        System.out.println("Path: " + generatePath(directories.get(0)));
//////        fntbPos+= 8;
////        System.out.println("ID: 0xf000");
////        System.out.println("Offset to Sub-table: 0x" + Long.toHexString(subTableOffset));
////        System.out.println("ID of first file in Sub-table: 0x" + Long.toHexString(subTableFirstFileId));
////        System.out.println("Number of files: 0x" + Long.toHexString(numDirs) + "\n");
////        String[] subTableOffsets= new String[(int)numDirs];
////        subTableOffsets[0]= Long.toHexString(subTableOffset);
////        for (int i = 0; i < numDirs - 1; i++)
////        {
////            int finalI = i;
////            long subTableOffset = buffer.readUIntI();
////            long firstFileID = buffer.readUIntS();
////            long thirdEntry = buffer.readUIntS();
////            File parentDir= new File(System.getProperty("user.dir"));
////
////            directories.add(new Directory() {
////                @Override
////                public String getName() {
////                    return null;
////                }
////
////                @Override
////                public String getHexId() {
////                    return hexFormat(Integer.toHexString(finalI + 1));
////                }
////
////                @Override
////                public String getParentDir() {
////                    return hexFormat(Long.toHexString(thirdEntry));
////                }
////
////                @Override
////                public File getFile() {
////                    return new File(directories.get(0).getFile().toString() + File.separator);
////                }
////            });
////            System.out.println("Path: " + tempPath + File.separator + generatePath(directories.get(i+1)));
////            System.out.println("Path: " + generatePath(directories.get(i+1)));
////            fntbPos+= 8;
////            System.out.println("ID: 0x" + hexFormat(Integer.toHexString(i + 1)));
////            System.out.println("Offset to Sub-table: 0x" + Long.toHexString(subTableOffset));
////            System.out.println("ID of first file in Sub-table: 0x" + Long.toHexString(firstFileID));
////            System.out.println("ID of parent directory: 0x" + hexFormat(Long.toHexString(thirdEntry)) + "\n");
////            subTableOffsets[i+1]= Long.toHexString(subTableOffset);
////        }
////        System.out.println(Arrays.toString(subTableOffsets));
////        System.out.println(Integer.toHexString(fntbPos));
////        System.out.println(buffer.getPosition());
////        System.out.println("\n");
//
//        for(int i= 0; i < numDirs-1; i++)
//        {
//            long subTableStartOffset= buffer.readUIntI();
//            long firstFileId= buffer.readUIntS();
//            long parentDir= buffer.readUIntS();
//            fntbPos+= 8;
//            System.out.println("ID: 0x" + hexFormat(Integer.toHexString(id++)));
//            System.out.println("Offset in FNTB: 0x" + Long.toHexString(subTableStartOffset));
//            System.out.println("ID of first file in directory: 0x" + Long.toHexString(firstFileId));
//            System.out.println("ID of parent directory: 0x" + hexFormat(Long.toHexString(parentDir)) + "\n");
//            map.put("0x" + hexFormat(Integer.toHexString(id-1)),"0x" + hexFormat(Long.toHexString(parentDir)));
//            fntbEntries.add(new FntbEntry()
//            {
//                @Override
//                public long getStartingOffset() {
//                    return subTableStartOffset;
//                }
//
//                @Override
//                public long getFirstFileId() {
//                    return firstFileId;
//                }
//
//                @Override
//                public long getParentDir() {
//                    return parentDir;
//                }
//            });
//        }
//
//        /** Name Table */
//        ArrayList<String> dirNames= new ArrayList<>();
//        HashMap<Integer,String> nameMap= new HashMap<>();
//        nameMap.put(0xf000,"root");
//        int subDirectoryIndex= 0;
//        int fileIdx = (int)subTableFirstFileId;
//        for (int i = 0; buffer.getPosition() < romData.getFntbOffset() + romData.getFntbLength(); i++)
//        {
//            long length = Integer.toUnsignedLong(buffer.readByte());
//            fntbPos+= 1;
//            boolean isFile = true;
//            if (length == 128 || length  == 0)
//            {
//                isFile = false;
//            }
//            if (length >= 129 && length <= 255)
//            {
//                length -= 128;
//                isFile = false;
//            }
//            String name = buffer.readString((int) length);
//            fntbPos+= length;
////            System.out.print(name);
//            short subDirectoryId;
//            if (!isFile)
//            {
//                if(length != 0)
//                {
//                    subDirectoryId = buffer.readShort();
//                    fntbPos+= 2;
////                    System.out.println(", Sub-directory ID 0x" + Integer.toHexString(subDirectoryId).substring(4));
//                    nameMap.put((int) subDirectoryId & 0xffff, name);
//
//                    if (subDirectoryIndex != 0)
//                    {
////                        System.out.println(dirNames.get(subDirectoryIndex-1));
//                        //new File(tempPath + File.separator + dirNames.get(subDirectoryIndex-1) + name).mkdir();
//                    }
//                    else
//                    {
//                        //new File(tempPath + File.separator +  name).mkdir();
//                    }
//                }
//                else
//                {
//                    subDirectoryIndex++;
//                    //dirNames.add(dirNamesContents);
//                    //dirNamesContents= new ArrayList<>();
////                    System.out.println();
//                }
//            }
//            else
//            {
//                System.out.println(name.trim().toLowerCase());
////                System.out.println(", File ID 0x" + Integer.toHexString(fileIdx++));
//            }
//        }
//
////        System.out.println(nameMap.toString());
//
//        String[] paths= generatePaths(map);
//        Arrays.sort(paths);
//        String lastPath= "                                                                       ";
//
//        for(int i= 0; i < nameMap.size(); i++)
//        {
//            String name= nameMap.get(i + 0xf000);
//            String dirId= "0x" + hexFormat(Integer.toHexString(i + 0xf000));
//            for(int x= 0; x < paths.length; x++)
//            {
//                String path= paths[x];
//                if(path.contains(dirId))
//                {
//                    path= path.replaceAll(dirId,name);
//                }
//                paths[x]= path;
//            }
//        }
//
//        for(String str : paths)
//        {
//            if(str.length() < lastPath.length())
//            {
//                System.out.println();
//            }
//            System.out.println(str);
//            lastPath= str;
//
//            String outputPath= path + File.separator + "temp" + File.separator + "rom";
//            new File(outputPath + str).mkdirs();
//        }
//        System.out.println();
//
//        System.out.println("\n\n\n\n\n");
//    }
//
//
//
//    protected void readFatb() throws IOException
//    {
//        int firstFileID= 0;
//        int SS_HG_FIRST_FILE = 0x81;
//        int D_P_PT_FIRST_FILE = 0x7A;
//        switch (romData.getTitle())
//        {
//            case "POKEMON SS" :
//
//            case "POKEMON HG" :
//                firstFileID= SS_HG_FIRST_FILE;
//                break;
//
//            case "POKEMON D" :
//
//            case "POKEMON P" :
//
//            case "POKEMON PL" :
//                firstFileID= D_P_PT_FIRST_FILE;
//                break;
//        }
////        System.out.println("First file ID: " + firstFileID + "\n");
//
//        int fatbPos= 0;
//        buffer.skipTo(romData.getFatbOffset());
////        System.out.println(buffer.getPosition() + "\n");
//        fimgEntries= new ArrayList<>();
////        System.out.println("Length: " + romData.getFatbLength()/8 + " files");
//        int lastEnd= 0;
//
//
//        for(int i= 0; i < romData.getFatbLength()/8; i++)
//        {
////            System.out.println("Fatb Offset: " + buffer.getPosition());
//            long startingOffset= buffer.readUIntI();
//            long endingOffset= buffer.readUIntI();
//            fatbPos+= 4;
////            System.out.println("ID: 0x" + Integer.toHexString(i));
////            System.out.println("Starting Offset: " +startingOffset);
////            System.out.println("Ending Offset: " + endingOffset);
////            System.out.println("Length: " + (endingOffset-startingOffset) + "\n");
//
//            long gap= 0;
//            if(i > firstFileID)
//            {
//                gap= startingOffset-lastEnd;
//            }
////            System.out.println(gap);
//
//            int finalI = i;
//            long finalDiff = gap;
//            fimgEntries.add(new FimgEntry() {
//                @Override
//                public int getId() {
//                    return finalI;
//                }
//
//                @Override
//                public long getStartingOffset() {
//                    return startingOffset;
//                }
//
//                @Override
//                public long getEndingOffset() {
//                    return endingOffset;
//                }
//
//                @Override
//                public long getGap() {
//                    return finalDiff;
//                }
//            });
//            lastEnd= (int) endingOffset;
//        }
////        System.out.println("Number of recorded entries: " + fimgEntries.size());
//
//
//    }
//
//
//    private static final int PERSONAL_J = 0x83;
//    private static final int LEARNSET_J = 0xA2;
//    private static final int EVOLUTION_J = 0xA3;
//    private static final int GROWTH_J = 0x84;
//    private static final int ENCOUNTER_SS = 0x109;
//    private static final int ENCOUNTER_HG = 0xA6;
//    private static final int ITEM_J = 0x92;
//    private static final int MOVE_J = 0x8C;
//    private static final int BABY_J= 0x1FF;
//
//
//    private static final int PERSONAL_PT= 0x1A5;
//    private static final int LEARNSET_PT = 0x1A7;
//    private static final int EVOLUTION_PT = 0x1A1;
//    private static final int GROWTH_PT = 0x1A4;
//    private static final int ENCOUNTER_PT = 0x14A;
//    private static final int ITEM_PT = 0x192;
//    private static final int MOVE_PT = 0x1BD;
//    private static final int STARTER_PT= 0x4E;
//    private static final int STARTER2_PT= 104;
//    private static final int INTRO_PT= 0x49;
//    private static final int OPENING_PT= 0x4D;
//    private static final int TUTOR_PT= 0x05;
//    private static final int BABY_PT= 0x1A6;
//
//    private static final int PERSONAL_DP= 0x146;
//    private static final int LEARNSET_DP = 0x148;
//    private static final int EVOLUTION_DP = 0x144;
//    private static final int GROWTH_DP = 0x145;
//    private static final int ENCOUNTER_DP = 0x108;
//    private static final int ITEM_DP = 0x13A;
//    private static final int MOVE_DP = 0x158;
//    private static final int STARTER_DP= 0x40;
//    private static final int STARTER2_DP= 63;
//    private static final int INTRO_DP= 0x3b;
//    private static final int OPENING_DP= 0x3F;
//    private static final int TUTOR_DP= 0x05;
//    private static final int BABY_DP= 0x147;
//
//
//    private static final int PERSONAL_B2W2= 0x16B;
//    private static final int LEARNSET_B2W2= 0x16D;
//    private static final int EVOLUTION_B2W2= 0x16E;
//    private static final int GROWTH_B2W2= 0x16C;
//    private static final int ENCOUNTER_B2W2= 0x1D9;
//    private static final int ITEM_B2W2 = 0x173;
//    private static final int MOVE_B2W2 = 0x170;
//
//    private static final int PERSONAL_BW= 0x102;
//    private static final int LEARNSET_BW= 0x104;
//    private static final int EVOLUTION_BW= 0x105;
//    private static final int GROWTH_BW= 0x103;
//    private static final int ENCOUNTER_BW= 0x170;
//    private static final int ITEM_BW = 0;
//    private static final int MOVE_BW = 0x158;
//
//
//    private void grabFile(String[] args, String title, String outputName, boolean preDefinedName) throws IOException, ClassNotFoundException, InterruptedException, NoSuchAlgorithmException
//    {
//        String type= args[0].toLowerCase();
//
//        isNarc= false;
//        tempPath= "temp" + File.separator + type;
//        tempPathUnpack= tempPath;
//        if(!type.equalsIgnoreCase("com/turtleisaac/pokeditor/editors/starters") && !type.equalsIgnoreCase("com/turtleisaac/pokeditor/editors/intro") && !type.equalsIgnoreCase("starters2") && !type.equalsIgnoreCase("com/turtleisaac/pokeditor/editors/opening") && !type.equalsIgnoreCase("com/turtleisaac/pokeditor/editors/tutors") && !type.equalsIgnoreCase("com/turtleisaac/pokeditor/editors/babies") && !type.equalsIgnoreCase("babies_sp"))
//        {
//            tempPath+= ".narc";
//            isNarc= true;
//        }
//        if(!type.equalsIgnoreCase("com/turtleisaac/pokeditor/editors/starters") && !type.equalsIgnoreCase("com/turtleisaac/pokeditor/editors/intro") && !type.equalsIgnoreCase("starters2") && !type.equalsIgnoreCase("com/turtleisaac/pokeditor/editors/opening"))
//        {
//            isSpreadsheet= true;
//        }
//
//        boolean randomize= args[1].equalsIgnoreCase("random");
//
//
//        fileOffset= 0;
//        length= 0;
//
//        this.type= type;
//
//
//        switch (title)
//        {
//            case "heartgold":
//            case "POKEMON HG":
//                switch(type) {
//                    case "com/turtleisaac/pokeditor/editors/personal":
//                        setFileData(PERSONAL_J);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/learnsets":
//                        setFileData(LEARNSET_J);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/evolutions":
//                        setFileData(EVOLUTION_J);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/growth":
//                        setFileData(GROWTH_J);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/encounters":
//                        setFileData(ENCOUNTER_HG);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/items":
//                        setFileData(ITEM_J);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/moves":
//                        setFileData(MOVE_J);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/babies":
//                        setFileData(BABY_J);
//                        break;
//                    default:
//                        throw new RuntimeException("Invalid arguments");
//                }
//            break;
//
//            case "soulsilver":
//            case "POKEMON SS":
//                switch(type) {
//                    case "com/turtleisaac/pokeditor/editors/personal":
//                        setFileData(PERSONAL_J);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/learnsets":
//                        setFileData(LEARNSET_J);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/evolutions":
//                        setFileData(EVOLUTION_J);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/growth":
//                        setFileData(GROWTH_J);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/encounters":
//                        setFileData(ENCOUNTER_SS);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/items":
//                        setFileData(ITEM_J);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/moves":
//                        setFileData(MOVE_J);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/babies":
//                        setFileData(BABY_J);
//                        break;
//                    default:
//                        throw new RuntimeException("Invalid arguments");
//                }
//                break;
//
//            case "platinum":
//            case "POKEMON PL":
//                switch(type) {
//                    case "com/turtleisaac/pokeditor/editors/personal":
//                        setFileData(PERSONAL_PT);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/learnsets":
//                        setFileData(LEARNSET_PT);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/evolutions":
//                        setFileData(EVOLUTION_PT);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/growth":
//                        setFileData(GROWTH_PT);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/encounters":
//                        setFileData(ENCOUNTER_PT);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/items":
//                        setFileData(ITEM_PT);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/moves":
//                        setFileData(MOVE_PT);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/starters":
//                        setFileData(STARTER_PT);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/opening":
//                        setFileData(OPENING_PT);
//                        break;
//                    case "starters2" :
//                        setFileData(STARTER2_PT);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/intro":
//                        setFileData(INTRO_PT);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/tutors":
//                        setFileData(TUTOR_PT);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/babies":
//                        setFileData(BABY_PT);
//                        break;
//                    default:
//                        throw new RuntimeException("Invalid arguments");
//                }
//                break;
//
//            case "pearl":
//            case "POKEMON P":
//                switch(type) {
//                    case "com/turtleisaac/pokeditor/editors/personal":
//                        setFileData(PERSONAL_DP);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/learnsets":
//                        setFileData(LEARNSET_DP);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/evolutions":
//                        setFileData(EVOLUTION_DP);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/growth":
//                        setFileData(GROWTH_DP);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/encounters":
//                        setFileData(ENCOUNTER_DP);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/items":
//                        setFileData(ITEM_DP);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/moves":
//                        setFileData(MOVE_DP);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/starters":
//                        setFileData(STARTER_DP);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/opening":
//                        setFileData(OPENING_DP);
//                        break;
//                    case "starters2" :
//                        setFileData(STARTER2_DP);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/intro":
//                        setFileData(INTRO_DP);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/tutors":
//                        setFileData(TUTOR_DP);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/babies":
//                        setFileData(BABY_DP);
//                        break;
//                    default:
//                        throw new RuntimeException("Invalid arguments");
//                }
//                break;
//
//            case "diamond":
//            case "POKEMON D":
//                switch(type) {
//                    case "com/turtleisaac/pokeditor/editors/personal":
//                        setFileData(PERSONAL_DP);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/learnsets":
//                        setFileData(LEARNSET_DP);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/evolutions":
//                        setFileData(EVOLUTION_DP);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/growth":
//                        setFileData(GROWTH_DP);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/encounters":
//                        setFileData(ENCOUNTER_DP-1);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/items":
//                        setFileData(ITEM_DP);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/moves":
//                        setFileData(MOVE_DP);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/starters":
//                        setFileData(STARTER_DP);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/opening":
//                        setFileData(OPENING_DP);
//                        break;
//                    case "starters2" :
//                        setFileData(STARTER2_DP);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/intro":
//                        setFileData(INTRO_DP);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/tutors":
//                        setFileData(TUTOR_DP);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/babies":
//                        setFileData(BABY_DP);
//                        break;
//                    default:
//                        throw new RuntimeException("Invalid arguments");
//                }
//                break;
//
//            case "black":
//            case "white":
//            case "POKEMON B" :
//            case "POKEMON W" :
//                switch(type) {
//                    case "com/turtleisaac/pokeditor/editors/personal":
//                        setFileData(PERSONAL_BW);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/learnsets":
//                        setFileData(LEARNSET_BW);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/evolutions":
//                        setFileData(EVOLUTION_BW);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/growth":
//                        setFileData(GROWTH_BW);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/encounters":
//                        setFileData(ENCOUNTER_BW);
//                        throw new RuntimeException("Encounters currently can't be edited for Gen 5");
////                        break;
//                    case "com/turtleisaac/pokeditor/editors/items":
//                        setFileData(ITEM_BW);
//                        throw new RuntimeException("Items currently can't be edited for Gen 5");
////                        break;
//                    case "com/turtleisaac/pokeditor/editors/moves":
//                        setFileData(MOVE_BW);
//                        break;
//                    default:
//                        throw new RuntimeException("Invalid arguments");
//                }
//                break;
//
//            case "black2":
//            case "white2":
//            case "POKEMON W2" :
//            case "POKEMON B2" :
//                switch(type) {
//                    case "com/turtleisaac/pokeditor/editors/personal":
//                        setFileData(PERSONAL_B2W2);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/learnsets":
//                        setFileData(LEARNSET_B2W2);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/evolutions":
//                        setFileData(EVOLUTION_B2W2);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/growth":
//                        setFileData(GROWTH_B2W2);
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/encounters":
//                        setFileData(ENCOUNTER_B2W2);
//                        throw new RuntimeException("Encounters currently can't be edited for Gen 5");
////                        break;
//                    case "com/turtleisaac/pokeditor/editors/items":
//                        setFileData(ITEM_B2W2);
//                        throw new RuntimeException("Items currently can't be edited for Gen 5");
////                        break;
//                    case "com/turtleisaac/pokeditor/editors/moves":
//                        setFileData(MOVE_B2W2);
//                        break;
//                    default:
//                        throw new RuntimeException("Invalid arguments");
//                }
//                break;
//
//            default:
//                throw new RuntimeException("Invalid game title");
//        }
//
//        buffer.close();
//        buffer= new Buffer(rom);
//
//        buffer.skipTo(fileOffset);
////        System.out.println("Current Location: " + buffer.getPosition());
//        if(!new File(path + "temp").exists() && !new File(path + "temp").mkdir())
//        {
//            throw new RuntimeException("Failed to create temp directory. Check write perms.");
//        }
//        BinaryWriter writer= new BinaryWriter(tempPath);
////        System.out.println("Read and wrote " + length + " bytes.");
//        writer.write(buffer.readBytes(length));
//        Narctowl narctowl= new Narctowl(false); //creates new narctowl.Narctowl object
//        if(isNarc)
//        {
//            narctowl.unpack(tempPath, null); //run narcs.Narctowl.unpack() with narc extracted from rom as parameter
//        }
//
//        if(randomize && isNarc)
//        {
//            FileRandomizer randomizer= new FileRandomizer(gameCode, false);
//            String ans;
//            if(multiRun)
//            {
//                if(args[0].equalsIgnoreCase("com/turtleisaac/pokeditor/editors/personal"))
//                    ans= "y";
//                else
//                    ans= "n";
//            }
//            else
//            {
//                System.out.println("Do you want to generate a new random order? (Y/n)");
//                ans= scanner.nextLine();
//            }
//
//
//            if(!ans.equalsIgnoreCase("n"))
//            {
//                randomizer.createOrder(Objects.requireNonNull(new File(tempPathUnpack).listFiles()).length);
//            }
//            randomizer.randomizeWithSet(tempPathUnpack);
//
//        }
//        else if(isNarc)
//        {
//            System.out.println("Do you want to generate new sheets? (Y/n)");
//            String ans= scanner.nextLine().toLowerCase();
//            if(!ans.equalsIgnoreCase("n"))
//            {
//                switch (args[0].toLowerCase()) {
//                    case "com/turtleisaac/pokeditor/editors/personal":
//                        if(romData.getTitle().equals("POKEMON B") || romData.getTitle().equals("POKEMON W") || title.equals("black") || title.equals("white") || romData.getTitle().equals("POKEMON B2") || romData.getTitle().equals("POKEMON W2") || title.equals("black2") || title.equals("white2"))
//                        {
//                            Gen5PersonalEditor2 personalEditor= new Gen5PersonalEditor2(gameCode);
//                            personalEditor.personalToCSV(tempPathUnpack);
//                        }
//                        else
//                        {
//                            PersonalEditor personalEditor = new PersonalEditor(gameCode);
//                            personalEditor.personalToSheet(tempPathUnpack);
//                        }
//
//                        break;
//
//                    case "com/turtleisaac/pokeditor/editors/learnsets":
//                        LearnsetEditor learnsetEditor = new LearnsetEditor(gameCode);
//                        learnsetEditor.learnsetToCsv(tempPathUnpack);
//
//                        break;
//
//                    case "com/turtleisaac/pokeditor/editors/evolutions":
//                        if(romData.getTitle().equals("POKEMON B") || romData.getTitle().equals("POKEMON W") || title.equals("black") || title.equals("white") || romData.getTitle().equals("POKEMON B2") || romData.getTitle().equals("POKEMON W2") || title.equals("black2") || title.equals("white2"))
//                        {
//                            EvolutionEditorGen5 evolutionEditor = new EvolutionEditorGen5();
//                            evolutionEditor.evolutionToCsv(tempPathUnpack, false);
//                        }
//                        else
//                        {
//                            EvolutionEditor evolutionEditor = new EvolutionEditor(gameCode);
//                            evolutionEditor.evolutionToCsv(tempPathUnpack, false);
//                        }
//
//                        break;
//
//                    case "com/turtleisaac/pokeditor/editors/growth":
//                        if(romData.getTitle().equals("POKEMON B") || romData.getTitle().equals("POKEMON W") || title.equals("black") || title.equals("white") || romData.getTitle().equals("POKEMON B2") || romData.getTitle().equals("POKEMON W2") || title.equals("black2") || title.equals("white2"))
//                        {
//                            throw new RuntimeException("The Growth Editor is currently not available for Gen 5");
//                        }
//                        else
//                        {
//                            GrowthEditor growthEditor = new GrowthEditor();
//                            growthEditor.growthToCsv(tempPathUnpack);
//                        }
//
//                        break;
//                    case "com/turtleisaac/pokeditor/editors/encounters":
//                        if(romData.getTitle().equals("POKEMON HG") || romData.getTitle().equals("POKEMON SS") || title.equals("soulsilver") || title.equals("heartgold"))
//                        {
//                            EncounterEditor encounterEditor= new EncounterEditor(gameCode);
//                            encounterEditor.encountersToCsv(tempPathUnpack);
//                        }
//                        else
//                        {
//                            SinnohEncounterEditor encounterEditor = new SinnohEncounterEditor();
//                            encounterEditor.encountersToCsv(tempPathUnpack);
//                        }
//                        break;
//
//                    case "com/turtleisaac/pokeditor/editors/items":
//                        if(romData.getTitle().equals("POKEMON B") || romData.getTitle().equals("POKEMON W") || title.equals("black") || title.equals("white") || romData.getTitle().equals("POKEMON B2") || romData.getTitle().equals("POKEMON W2") || title.equals("black2") || title.equals("white2"))
//                        {
//                            ItemEditorGen5 itemEditor = new ItemEditorGen5();
//                            itemEditor.itemsToCsv(tempPathUnpack);
//                        }
//                        else
//                        {
//                            ItemEditorGen4 itemEditor = new ItemEditorGen4(gameCode);
//                            itemEditor.itemsToCsv(tempPathUnpack);
//                        }
//                        break;
//
//                    case "com/turtleisaac/pokeditor/editors/moves":
//                        if(romData.getTitle().equals("POKEMON B") || romData.getTitle().equals("POKEMON W") || title.equals("black") || title.equals("white") || romData.getTitle().equals("POKEMON B2") || romData.getTitle().equals("POKEMON W2") || title.equals("black2") || title.equals("white2"))
//                        {
//                            MoveEditorGen5 moveEditor = new MoveEditorGen5();
//                            moveEditor.movesToCsv(tempPathUnpack);
//                        }
//                        else
//                        {
//                            MoveEditorGen4 moveEditor = new MoveEditorGen4();
//                            moveEditor.movesToCsv(tempPathUnpack);
//                        }
//                        break;
//
//                    default:
//                        throw new RuntimeException("Invalid arguments");
//                }
//
//                System.out.println("\nAfter making all edits to the csv file(s), export them with the same name(s) as they had originally, but with \"Recompile\" appended prior to the file extension. Place them in the same folder they were output in.\nPress Enter to continue.");
//                Thread.sleep(1000);
//                scanner.nextLine();
//                Thread.sleep(1000);
//            }
//
//
//            switch (args[0].toLowerCase()) {
//                case "com/turtleisaac/pokeditor/editors/personal":
//                    if(romData.getTitle().equals("POKEMON B") || romData.getTitle().equals("POKEMON W") || title.equals("black") || title.equals("white") || romData.getTitle().equals("POKEMON B2") || romData.getTitle().equals("POKEMON W2") || title.equals("black2") || title.equals("white2"))
//                    {
//                        Gen5PersonalEditor2 personalEditor = new Gen5PersonalEditor2(gameCode);
//                        personalEditor.csvToPersonal("personalDataRecompile.csv", "tmLearnsetDataRecompile.csv", type + "Recompile");
//                    }
//                    else
//                    {
//                        PersonalEditor personalEditor = new PersonalEditor(gameCode);
//                        personalEditor.csvToPersonal("personalDataRecompile.csv", "tmLearnsetDataRecompile.csv", type + "Recompile");
//                    }
//
//                    break;
//                case "com/turtleisaac/pokeditor/editors/learnsets":
//                    LearnsetEditor learnsetEditor = new LearnsetEditor(gameCode);
//                    learnsetEditor.csvToLearnsets("LearnsetRecompile.csv", type + "Recompile");
//
//                    break;
//                case "com/turtleisaac/pokeditor/editors/evolutions":
//                    if(romData.getTitle().equals("POKEMON B") || romData.getTitle().equals("POKEMON W") || title.equals("black") || title.equals("white") || romData.getTitle().equals("POKEMON B2") || romData.getTitle().equals("POKEMON W2") || title.equals("black2") || title.equals("white2"))
//                    {
//                        EvolutionEditorGen5 evolutionEditor = new EvolutionEditorGen5();
//                        evolutionEditor.csvToEvolutions("EvolutionDataRecompile.csv", type + "Recompile");
//                    }
//                    else
//                    {
//                        EvolutionEditor evolutionEditor = new EvolutionEditor(gameCode);
//                        evolutionEditor.csvToEvolutions("EvolutionDataRecompile.csv", type + "Recompile");
//                    }
//
//                    break;
//                case "com/turtleisaac/pokeditor/editors/growth":
//                    GrowthEditor growthEditor = new GrowthEditor();
//                    growthEditor.csvToGrowth("GrowthTableRecompile.csv", type + "Recompile");
//
//                    break;
//                case "com/turtleisaac/pokeditor/editors/encounters":
//                    if(romData.getTitle().equals("POKEMON HG") || romData.getTitle().equals("POKEMON SS"))
//                    {
//                        EncounterEditor encounterEditor = new EncounterEditor(gameCode);
//                        encounterEditor.csvToEncounters("com/turtleisaac/pokeditor/editors/encounters",type + "Recompile");
//                    }
//                    else
//                    {
//                        SinnohEncounterEditor encounterEditor = new SinnohEncounterEditor();
//                        encounterEditor.csvToEncounters("com/turtleisaac/pokeditor/editors/encounters",type + "Recompile");
//                    }
//
//                    break;
//                case "com/turtleisaac/pokeditor/editors/items":
//                    if(romData.getTitle().equals("POKEMON B") || romData.getTitle().equals("POKEMON W") || title.equals("black") || title.equals("white") || romData.getTitle().equals("POKEMON B2") || romData.getTitle().equals("POKEMON W2") || title.equals("black2") || title.equals("white2"))
//                    {
//                        ItemEditorGen5 itemEditor = new ItemEditorGen5();
//                        itemEditor.csvToItems("ItemsRecompile.csv",type + "Recompile");
//                    }
//                    else
//                    {
//                        ItemEditorGen4 itemEditor = new ItemEditorGen4(gameCode);
//                        itemEditor.csvToItems("ItemsRecompile.csv",type + "Recompile");
//                    }
//
//                    break;
//                case "com/turtleisaac/pokeditor/editors/moves":
//                    if(romData.getTitle().equals("POKEMON B") || romData.getTitle().equals("POKEMON W") || title.equals("black") || title.equals("white") || romData.getTitle().equals("POKEMON B2") || romData.getTitle().equals("POKEMON W2") || title.equals("black2") || title.equals("white2"))
//                    {
//                        MoveEditorGen5 moveEditor = new MoveEditorGen5();
//                        moveEditor.csvToMoves("MoveDataRecompile.csv", type + "Recompile");
//                    }
//                    else
//                    {
//                        MoveEditorGen4 moveEditor = new MoveEditorGen4();
//                        moveEditor.csvToMoves("MoveDataRecompile.csv", type + "Recompile");
//                    }
//
//                    break;
//                default:
//                    throw new RuntimeException("Invalid arguments");
//            }
//        }
//        else
//        {
//            String ans;
//            switch (args[0].toLowerCase())
//            {
//                case "com/turtleisaac/pokeditor/editors/starters":
//                    StarterEditorGen4 starterEditor= new StarterEditorGen4(gameCode);
//                    starterEditor.changeStarters(tempPathUnpack);
//                    break;
//
//                case "com/turtleisaac/pokeditor/editors/opening":
//                    OpeningEditorGen4 openingEditor= new OpeningEditorGen4(gameCode);
//                    openingEditor.changeOpening(tempPathUnpack);
//                    break;
//
//                case "com/turtleisaac/pokeditor/editors/intro":
//                    IntroEditorGen4 introEditor= new IntroEditorGen4(gameCode);
//                    introEditor.changeIntroPokemon(tempPathUnpack);
//                    break;
//
//                case "com/turtleisaac/pokeditor/editors/tutors":
//                    System.out.println("Do you want to generate new sheets? (Y/n)");
//                    ans= scanner.nextLine().toLowerCase();
//                    TutorMoveListEditor tutorEditor= new TutorMoveListEditor(gameCode,tempPathUnpack);
//                    if(!ans.equalsIgnoreCase("n"))
//                    {
//                        tutorEditor.moveListToCsv();
//
//                        System.out.println("\nAfter making all edits to the csv file(s), export them with the same name(s) as they had originally, but with \"Recompile\" appended prior to the file extension. Place them in the same folder they were output in.\nPress Enter to continue.");
//                        Thread.sleep(1000);
//                        scanner.nextLine();
//                        Thread.sleep(1000);
//                    }
//                    tutorEditor.csvToMoveList("tutorMoveDataRecompile.csv","tutorCompatibilityDataRecompile.csv", type + "Recompile");
//                    break;
//
//                case "com/turtleisaac/pokeditor/editors/babies":
//                    System.out.println("Do you want to generate new sheets? (Y/n)");
//                    ans= scanner.nextLine().toLowerCase();
//                    BabyFormEditor babyEditor= new BabyFormEditor();
//                    if(!ans.equalsIgnoreCase("n"))
//                    {
//                        babyEditor.babyFormsToCsv(tempPathUnpack);
//
//                        System.out.println("\nAfter making all edits to the csv file(s), export them with the same name(s) as they had originally, but with \"Recompile\" appended prior to the file extension. Place them in the same folder they were output in.\nPress Enter to continue.");
//                        Thread.sleep(1000);
//                        scanner.nextLine();
//                        Thread.sleep(1000);
//                    }
//                    babyEditor.csvToBabyForms("babyFormsDataRecompile.csv",type + "Recompile");
//                    break;
//
//                default:
//                    throw new RuntimeException("Invalid arguments");
//            }
//        }
//
//        if(isNarc)
//        {
//            narctowl.pack(tempPathUnpack + "Recompile",type + "Recompile.narc","");
//
//            Buffer narcBuffer= new Buffer(path + "temp" + File.separator + type + "Recompile.narc");
//            BinaryWriter narcWriter;
//
//            if(args[0].equalsIgnoreCase("com/turtleisaac/pokeditor/editors/personal") && (gameCode.substring(0,3).equalsIgnoreCase("ird") || gameCode.substring(0,3).equalsIgnoreCase("ire")) && !args[1].equalsIgnoreCase("com/turtleisaac/pokeditor/editors/personal"))
//            {
////                System.out.println("mooooooo1");
//                byte[] first8= narcBuffer.readBytes(8);
//                int length= narcBuffer.readInt() + 2;
//                byte[] narcConents= narcBuffer.readRemainder();
//                narcWriter= new BinaryWriter(path + "temp" + File.separator + type + "Recompile.narc");
//                narcWriter.write(first8);
//                narcWriter.writeInt(length);
//                narcWriter.write(narcConents);
//                narcWriter.writeBytes(0xff,0xff);
//            }
//
//            if(length != new File(path + "temp" + File.separator + type + "Recompile.narc").length())
//            {
//                System.out.println("The file you have recompiled is different in length from the original file. Recompiling roms using a file of different length is unsupported at the moment, so please use Tinke to insert the narc into your rom.\nUse this website to find out where the file is meant to go in Tinke: https://projectpokemon.org/rawdb/");
//
//                narcBuffer= new Buffer(path + "temp" + File.separator + type + "Recompile.narc");
//                narcWriter= new BinaryWriter(path + type + "Recompile.narc");
//                narcWriter.write(narcBuffer.readBytes((int)new File(path + "temp" + File.separator + type + "Recompile.narc").length()));
//                narcBuffer.close();
//                narcWriter.close();
////            clearDirectory(new File(path + "temp"));
//                System.exit(0);
//            }
//        }
//
//
//        replaceFile(args, preDefinedName, outputName);
//    }
//
//    private void replaceFile(String[] args, boolean preDefinedName, String outputName) throws IOException, NoSuchAlgorithmException
//    {
//
//        String name;
//        BinaryWriter writer;
//        Buffer romBuffer;
//        String tempRom;
//        if(!multiRun)
//        {
//            System.out.println("Please enter the name to be given to the output rom (include .nds)");
//            name= scanner.nextLine();
//        }
//        else
//        {
//            name= args[args.length-1];
//        }
//        tempRom= path + "temp" + File.separator + "rom.nds";
//
//        romBuffer= new Buffer(rom);
//        writer= new BinaryWriter(tempRom);
//
//
//
////        writer.write(romBuffer.readBytes(romData.getFatbOffset() + (fileID*8))); //copies all bytes from base rom between 0x00 and FATB entry for file that was extracted and edited
//
////        ArrayList<FimgEntry> newFimgEntries= new ArrayList<>();
////        for(int i= 0; i < fileID; i++)
////        {
////            newFimgEntries.add(fimgEntries.get(i));
////        }
////
////        newFileLength= (int) new File(path + "temp" + File.separator + type + "Recompile.narc").length(); //gets the length of the repacked narc
////        int diff= newFileLength-length; //the difference between the length of the new file and the length of the original
////        int start= romBuffer.readInt(); //the offset that the file starts at in the FIMG table
////        writer.writeInt(start); //copies start offset to new rom
////        int end= romBuffer.readInt()+diff; //the offset that the new file will end at in the FIMG table
////        writer.writeInt(end); //writes new ending offset to new rom
////        int finalStart = start; //stores a local, final copy of start
////        int finalEnd = end; //stores a local, final copy of end
////        newFimgEntries.add(new FimgEntry() { //changes the contents of the program's internal FATB table for the edited file
////            @Override
////            public int getId() {
////                return fileID;
////            }
////
////            @Override
////            public long getStartingOffset() {
////                return finalStart;
////            }
////
////            @Override
////            public long getEndingOffset() {
////                return finalEnd;
////            }
////
////            @Override
////            public long getGap() {
////                return fimgEntries.get(0).getGap();
////            }
////        });
////
////        start= romBuffer.readInt()+diff;
////        int gap= 0;
////        if(start % 4 != 0)
////        {
////            gap= 4-(start%4);
////        }
////        start+= gap;
////        writer.writeInt(start);
////        diff+= gap;
////        end= romBuffer.readInt()+diff;
////        writer.writeInt(end);
////        int finalStart2 = start;
////        int finalEnd2 = end;
////        int finalGap1 = gap;
////        newFimgEntries.add(new FimgEntry() {
////            @Override
////            public int getId() {
////                return fileID+1;
////            }
////
////            @Override
////            public long getStartingOffset() {
////                return finalStart2;
////            }
////
////            @Override
////            public long getEndingOffset() {
////                return finalEnd2;
////            }
////
////            @Override
////            public long getGap() {
////                return finalGap1;
////            }
////        });
////
////        for(int i= (fileID+2); i < romData.getFatbLength()/8; i++) //goes through the remainder of the FIMG and copies it to the new rom, with the necessary alterations
////        {
////            start= romBuffer.readInt()+diff;
////            writer.writeInt(start);
////            end= romBuffer.readInt()+diff;
////            writer.writeInt(end);
////            int finalIdx = i;
////            int finalStart1 = start;
////            int finalEnd1 = end;
////            int finalGap = gap;
////            newFimgEntries.add(new FimgEntry() {
////                @Override
////                public int getId() {
////                    return finalIdx;
////                }
////
////                @Override
////                public long getStartingOffset() {
////                    return finalStart1;
////                }
////
////                @Override
////                public long getEndingOffset() {
////                    return finalEnd1;
////                }
////
////                @Override
////                public long getGap() {
////                    return finalGap;
////                }
////            });
////        }
//
//
////        writer.write(romBuffer.readBytes(fileOffset-romData.getFatbOffset()+romData.getFatbLength())); //copies all bytes between the end of the FATB to the start of the file to be replaced in the FIMG from the base rom to the new rom
//
////        byte[] header= romBuffer.readBytes(0x15e);
////        System.out.println("Current position: " + romBuffer.getPosition());
////
////        short headerChecksum= calculateCRC16(header);
////        System.out.println("HEADER CHECKSUM CRC-16: " + headerChecksum + " (0x" + Integer.toHexString(headerChecksum) + ")");
//
//
//        writer.write(romBuffer.readBytes(fileOffset));
//        Buffer narcBuffer;
//        if(isNarc)
//        {
//            narcBuffer= new Buffer(path + "temp" + File.separator + type + "Recompile.narc"); //creates a new Framework.Buffer object to read through the repacked, modified narc
//        }
//        else
//        {
//            narcBuffer= new Buffer(path + "temp" + File.separator + type + "Recompile"); //creates a new Framework.Buffer object to read through the modified file
//        }
//
////        writer.write(narcBuffer.readBytes(newFileLength)); //writes the entire modified narc to the new rom
//        writer.write(narcBuffer.readBytes(length));
//        romBuffer.skipBytes(length); //skips past the original file in the Framework.Buffer reading the original rom
//        writer.write(romBuffer.readRemainder());
//        writer.close();
//
////        int b;
////        while((b= romBuffer.readByte()) == 0xff)
////        {
////            System.out.println("moo");
////            writer.writeByte((byte)0xff);
////        }
////        writer.writeByteNumTimes((byte) 0xff,gap);
////        writer.writeByte((byte)b);
////        writer.write(romBuffer.readBytes((int) (new File(rom).length()-romBuffer.getPosition())));
//
////        boolean startersHasRan = false;
////        if(startersHasRan)
////        {
////            tempRom= path + "temp" + File.separator + "rom1.nds";
////        }
//
//        writer= new BinaryWriter(path + name);
//        romBuffer= new Buffer(tempRom);
//        writer.write(romBuffer.readBytes((int) new File(tempRom).length()));
//        System.out.println("\nProcess completed. Output file can be found at: " + path + name);
//
//        //writer.write(romBuffer.readBytes((int) (new File(rom).length()-fimgEntries.get(fileID+1).getStartingOffset()))); //writes all bytes from the starting offset of the file after the modified file to the end of the rom
//////        writer.write(romBuffer.readBytes((int) (new File(rom).length())-romBuffer.getPosition()));
////        for(int i= fileOffset + newFileLength; i < fimgEntries.get(fileID+1).getStartingOffset(); i++)
////        {
////            writer.writeByte((byte) 0xff);
////        }
////        writer.close();
////        romBuffer.close();
//        }
//
//
//
//    private void arm9Jump(String[] args)
//    {
//
//    }
//
//    public void readOverlays()
//    {
//        overlayEntries= new ArrayList<>();
//        buffer.skipTo(romData.getArm9OverlayOffset());
//
//        int numOverlays= romData.getArm9OverlayLength()/32;
//        System.out.println("Number of Overlays: " + numOverlays + "\n");
//
//        while(buffer.getPosition() != romData.getArm9OverlayOffset() + romData.getArm9OverlayLength())
//        {
//            long overlayId= buffer.readUIntI();
//
//            long ramAddress= buffer.readUIntI();
//            long ramSize= buffer.readUIntI();
//
//            long bssSize= buffer.readUIntI();
//            long staticInitStart= buffer.readUIntI();
//            long staticInitEnd= buffer.readUIntI();
//            long fileId= buffer.readUIntI();
//            int reserved= buffer.readInt();
//
//            System.out.println("Overlay ID: " + overlayId);
//            System.out.println("Ram Address: " + ramAddress);
//            System.out.println("Ram Size: " + ramSize);
//            System.out.println("BSS Size: " + bssSize);
//            System.out.println("Static Initializer Start Address: " + staticInitStart);
//            System.out.println("Static Initializer End Address: " + staticInitStart);
//            System.out.println("File ID: " + fileId + "\n");
//
//            if(fileId != overlayId)
//            {
//                System.out.println("MOOO");
//            }
//
//            overlayEntries.add(new OverlayData()
//            {
//                @Override
//                public long getOverlayId()
//                {
//                    return overlayId;
//                }
//
//                @Override
//                public long getRamAddress()
//                {
//                    return ramAddress;
//                }
//
//                @Override
//                public long getRamSize()
//                {
//                    return ramSize;
//                }
//
//                @Override
//                public long getBssSize()
//                {
//                    return bssSize;
//                }
//
//                @Override
//                public long getStaticInitStart()
//                {
//                    return staticInitStart;
//                }
//
//                @Override
//                public long getStaticInitEnd()
//                {
//                    return staticInitEnd;
//                }
//
//                @Override
//                public long getFileId()
//                {
//                    return fileId;
//                }
//
//                @Override
//                public int getReserved()
//                {
//                    return reserved;
//                }
//            });
//        }
//
//    }
//
//
//
//    public void editOverlays(String[] args, String title) throws IOException
//    {
////        OverlayData overlayData;
////        args[0]= args[0].toLowerCase();
////
////        switch (title)
////        {
////            case "heartgold":
////            case "POKEMON HG":
////            case "soulsilver":
////            case "POKEMON SS":
////                switch(args[0]) {
////                    case "starters" :
////                        overlayID= STARTER_J;
////                        throw new RuntimeException("Not supported yet");
//////                        break;
////                    default:
////                        throw new RuntimeException("Invalid arguments");
////                }
//////                break;
////
////            case "platinum":
////            case "POKEMON PL":
////                switch(args[0]) {
////                    case "starters" :
////                        overlayID= STARTER_PT;
////                        break;
////                    default:
////                        throw new RuntimeException("Invalid arguments");
////                }
////                break;
////
////            case "pearl":
////            case "POKEMON P":
////            case "diamond":
////            case "POKEMON D":
////                switch(args[0]) {
////                    case "starters" :
////                        overlayID= STARTER_DP;
////                        break;
////                    default:
////                        throw new RuntimeException("Invalid arguments");
////                }
////                break;
////
////            case "black":
////            case "white":
////            case "POKEMON B" :
////            case "POKEMON W" :
////                switch(args[0]) {
////                    case "starters" :
////                        overlayID= STARTER_BW;
////                        throw new RuntimeException("Not supported yet");
//////                        break;
////                    default:
////                        throw new RuntimeException("Invalid arguments");
////                }
//////                break;
////
////            case "black2":
////            case "white2":
////            case "POKEMON W2" :
////            case "POKEMON B2" :
////                switch(args[0]) {
////                    case "starters" :
////                        overlayID= STARTER_B2W2;
////                        throw new RuntimeException("Not supported yet");
//////                        break;
////                    default:
////                        throw new RuntimeException("Invalid arguments");
////                }
//////                break;
////
////            default:
////                throw new RuntimeException("Invalid game title");
////        }
////
////        overlayData= overlayEntries.get(overlayID);
////
////        BinaryWriter writer= new BinaryWriter(path + "temp" + File.separator + "rom.nds");
////        Buffer romBuffer= new Buffer(rom);
////
////
////        writer.write(romBuffer.readBytes((int) overlayData.getStaticInitStart()));
////
////        switch (args[0])
////        {
////            case "starters" :
////                String ans;
////                int pokemonId;
////
////                switch (title)
////                {
////                    case "heartgold":
////                    case "POKEMON HG":
////                    case "soulsilver":
////                    case "POKEMON SS":
////                        break;
////
////                    case "platinum":
////                    case "POKEMON PL":
////                        writer.write(romBuffer.readBytes(0x1bc0));
////                        for(int i= 0; i < 3; i++)
////                        {
////                            pokemonId= romBuffer.readInt();
////                            System.out.println("Starter " + (i + 1) + " is currently: " + nameData[pokemonId] + ". Do you want to change it? (y/N)\n");
////                            ans= scanner.nextLine();
////                            System.out.println("\n");
////
////                            if(ans.equalsIgnoreCase("y"))
////                            {
////                                System.out.println("Please enter the name of the Pokemon you wish to replace it with\n");
////                                pokemonId= getPokemon(scanner.nextLine());
////                                System.out.println("\nStarter has been replaced with " + nameData[pokemonId] + "\n");
////                            }
////                            writer.writeInt(pokemonId);
////                            romBuffer.skipBytes(4);
////                        }
////                        break;
////
////                    case "pearl":
////                    case "POKEMON P":
////                    case "diamond":
////                    case "POKEMON D":
////                        writer.write(romBuffer.readBytes(0x1b88));
////                        for(int i= 0; i < 3; i++)
////                        {
////                            pokemonId= romBuffer.readInt();
////                            System.out.println("Starter " + (i + 1) + " is currently: " + nameData[pokemonId] + ". Do you want to change it? (y/N)\n");
////                            ans= scanner.nextLine();
////                            System.out.println("\n");
////
////                            if(ans.equalsIgnoreCase("y"))
////                            {
////                                System.out.println("Please enter the name of the Pokemon you wish to replace it with\n");
////                                pokemonId= getPokemon(scanner.nextLine());
////                                System.out.println("\nStarter has been replaced with " + nameData[pokemonId] + "\n");
////                            }
////                            writer.writeInt(pokemonId);
////                            romBuffer.skipBytes(4);
////                        }
////                        break;
////
////                    case "black":
////                    case "white":
////                    case "POKEMON B" :
////                    case "POKEMON W" :
////                        break;
////
////                    case "black2":
////                    case "white2":
////                    case "POKEMON W2" :
////                    case "POKEMON B2" :
////                        break;
////                }
////                writer.write(buffer.readRemainder());
////                writer.close();
////                break;
////            default:
////                System.out.println("Invalid arguments. Please try re-running the program with different arguments");
////                System.exit(0);
////        }
////
////        System.out.println("Please enter the name to be given to the output rom (include .nds)");
////        String name= scanner.nextLine();
////
////        writer= new BinaryWriter(path + name);
////        romBuffer= new Buffer(path + "temp" + File.separator + "rom.nds");
////        writer.write(romBuffer.readBytes((int) new File(path + "temp" + File.separator + "rom.nds").length()));
////        System.out.println("\nProcess completed. Output file can be found at: " + path + name);
////        writer.close();
//    }
//
//
//
//    public void emptySpaceFinder(int numBytes) throws IOException
//    {
//        Buffer arm9Buffer= new Buffer(path + "arm9.bin");
//        long originalLength= new File(path + "arm9.bin").length();
//
//        if(arm9Buffer.getLength() < 0x3BFE00)
//        {
//            byte[] arm9= arm9Buffer.readRemainder();
//            BinaryWriter writer= new BinaryWriter(path + "arm9.bin");
//            writer.write(arm9);
//            writer.writeByteNumTimes((byte)0xff,0x3BFE00-arm9.length);
//            writer.close();
//        }
//
//        System.out.println("\n----------------------------ARM9 Empty Space Finder----------------------------\n");
//
//        System.out.println("Looking for empty spaces with a length greater than or equal to: " + numBytes + "\n");
//
//        System.out.println("Game: " + romData.getTitle() + " (" + romData.getGameCode() + ")\n");
//
//        arm9Buffer= new Buffer(path + "arm9.bin");
//        int idx= 0;
//        ArrayList<Byte> byteList;
//
//        boolean foundPerfect= false;
//        String bestLocation= "";
//        int closestLength= Integer.MAX_VALUE;
//
//        while(arm9Buffer.getPosition() != arm9Buffer.getLength())
//        {
//            byteList= new ArrayList<>();
//            byte b;
//            int offset= 0;
//            boolean first= true;
//            int numSinceNonFF= 0;
//            boolean atEnd= false;
//
//            while((b= (byte)arm9Buffer.readByte()) == (byte)0xff)
//            {
//                if(first)
//                {
//                    offset= arm9Buffer.getPosition()-1;
//                    first= false;
//                }
//                numSinceNonFF++;
//                byteList.add(b);
//
//                if(numSinceNonFF > numBytes + 200)
//                {
//                    atEnd= true;
//                    break;
//                }
//            }
//
//            if(byteList.size() >= numBytes)
//            {
//                if(!atEnd)
//                {
//                    if(byteList.size() < closestLength)
//                    {
//                        closestLength= byteList.size();
//                        bestLocation= "\nOffset: " + offset + " (0x" + Integer.toHexString(offset) + ")\nLength: " + byteList.size() + " (0x" + Integer.toHexString(byteList.size()) + ")";
//                    }
//                    else if (byteList.size() == closestLength)
//                    {
//                        bestLocation+= "\n\nOffset: " + offset + " (0x" + Integer.toHexString(offset) + ")\nLength: " + byteList.size() + " (0x" + Integer.toHexString(byteList.size()) + ")";
//                    }
//
//                    System.out.println("Offset: " + offset + " (0x" + Integer.toHexString(offset) + ")");
//                    System.out.println("Length: " + byteList.size() + " (0x" + Integer.toHexString(byteList.size()) + ")");
//                    System.out.print("Opinion: ");
//
//                    if(byteList.size() >= numBytes + 10)
//                    {
//                        System.out.println("This empty section might be too large for your code (" + (byteList.size()-numBytes) + " extra empty bytes). If other smaller sections are found, you may want to consider using those instead to save this section for larger code later on.");
//                    }
//                    else if(byteList.size() != numBytes)
//                    {
//                        System.out.println("This empty section may be a good location for your code, as there are only " + (byteList.size()-numBytes) + " extra empty bytes. Make sure to check if there are any smaller sections before choosing this one.");
//                    }
//                    else
//                    {
//                        System.out.println("This empty section may be a perfect location for your code, as there are no extra empty bytes at this offset.");
//                        foundPerfect= true;
//                    }
//                    System.out.print("\n\n");
//                    idx++;
//                }
//                else if(!foundPerfect)
//                {
//                    System.out.println("Offset: " + offset + " (0x" + Integer.toHexString(offset) + ")");
//                    System.out.println("Length: " + (0x3BFE00 - offset) + " (0x" + Integer.toHexString(0x3BFE00 - offset) +  ")");
//                    System.out.println("Opinion: This area may ideal for usage, as it is at the end of the arm9. However, depending on how good of a fit there might be elsewhere in the arm9, you may not want to use this area.\n\n");
//                    idx++;
//                    break;
//                }
//                else
//                {
//                    break;
//                }
//            }
//
//        }
//
//        System.out.println("\nTotal number of usable spaces found: " + idx + "\n");
//        System.out.println("Best candidate(s):\n" + bestLocation);
//        new File(path + "arm9.bin").deleteOnExit();
//    }
//
//
//    public void extractFile(String fileName, boolean shortcut) throws IOException
//    {
//        if(!shortcut)
//        {
//            String game;
//            switch(gameCode.substring(0,3).toLowerCase().trim())
//            {
//                case "cpu":
//                    game= "Pt.txt";
//                    break;
//
//                case "ada":
//                case "apa":
//                    game= "DP.txt";
//                    break;
//
//                case "ipk":
//                case "ipg":
//                    game= "HGSS.txt";
//                    break;
//
//                case "irb":
//                case "irw":
//                    game= "BW.txt";
//                    break;
//
//                case "ird":
//                case "ire":
//                    game= "B2W2.txt";
//                    break;
//
//                default:
//                    throw new RuntimeException("Invalid game: " + romData.getTitle() + " (" + romData.getGameCode() + ")");
//            }
//
//
//            BufferedReader reader= new BufferedReader(new FileReader(path + "Program Files" + File.separator + "Filesystems" + File.separator + game));
//            String line;
//            int i= 0;
//            while((line= reader.readLine()) != null)
//            {
//                if(fileName.toLowerCase().trim().equalsIgnoreCase(line.toLowerCase().trim()))
//                {
//                    System.out.println("File " + fileName + " found at offset: " + i + " (0x" + Integer.toHexString(i) + ")");
//                    extractFile(i);
//                }
//                i++;
//            }
//
//            throw new RuntimeException("File not found");
//        }
//        else
//        {
//            String game;
//            switch(gameCode.substring(0,3).toLowerCase().trim())
//            {
//                case "cpu":
//                    game= "Platinum";
//                    break;
//
//                case "ada":
//                    game= "Diamond";
//                    break;
//
//                case "apa":
//                    game= "Pearl";
//                    break;
//
//                case "ipk":
//                    game= "HeartGold";
//                    break;
//
//                case "ipg":
//                    game= "SoulSilver";
//                    break;
//
//                case "irb":
//                case "irw":
//                    game= "BlackWhite";
//                    break;
//
//                case "ird":
//                case "ire":
//                    game= "Black2White2";
//                    break;
//
//                default:
//                    throw new RuntimeException("Invalid game: " + romData.getTitle() + " (" + romData.getGameCode() + ")");
//            }
//
//            HashMap<String,Integer> map= shortcutMap.get(game);
//
//            if(map.containsKey(fileName))
//            {
//                extractFile(map.get(fileName));
//            }
//            else
//            {
//                System.out.println("There is no shortcut with this name for the specified game. Would you like to add one? (y/N)");
//                String ans= scanner.nextLine().trim();
//                if(ans.equalsIgnoreCase("y"))
//                {
//
//
//                    System.out.println("\nThese are the shortcuts that currently exist for this game:\n");
//                    String[] shortcutNames= map.keySet().toArray(new String[0]);
//                    Integer[] shortcutValues= map.values().toArray(new Integer[0]);
//                    for(int i= 0; i < shortcutNames.length; i++)
//                    {
////                        System.out.println("Shortcut Name: " + shortcutNames[i]);
////                        System.out.println("    File ID: " + shortcutValues[i] + " (" + Integer.toHexString(shortcutValues[i]) + ")");
////                        System.out.println("    File name: " + );
//                    }
//                }
//                else
//                {
//                    System.out.println("Process aborted due to user response");
//                    System.exit(0);
//                }
//            }
//        }
//
//    }
//    /**
//     * Not used for normal PokEditor operations, see grabFile() instead. This is instead for the separate user-accessible file access/ extraction
//     * @param index an integer value representing the index to be pulled from the FATB table and subsequently the FIMG entries
//     * @throws IOException because reasons
//     */
//    public void extractFile(int index) throws IOException
//    {
//        buffer= new Buffer(rom);
//        FimgEntry file= fimgEntries.get(index);
//
//        buffer.skipTo((int)file.getStartingOffset());
//        if(!new File(path + "extracted").exists() && !new File(path + "extracted").mkdir())
//        {
//            throw new RuntimeException("Failed to create extracted directory. Check write perms.");
//        }
//
//        System.out.println("\nEnter the name for the output file");
//        String name= scanner.nextLine().trim();
//        BinaryWriter writer= new BinaryWriter(path + "extracted" + File.separator + name);
//        writer.write(buffer.readBytes((int) (file.getEndingOffset()-file.getStartingOffset())));
//        System.out.println("\nProcess completed. Output can be found at: " + path + "extracted" + File.separator + name);
//
////        Narctowl narctowl= new Narctowl(false); //creates new narctowl.Narctowl object
////        if(scanner.nextLine().trim().equalsIgnoreCase("y"))
////        {
////            narctowl.unpack(tempPath); //run narcs.Narctowl.unpack() with narc extracted from rom as parameter
////        }
//        System.exit(0);
//    }
//
//
//    private void clearDirectory(File directory)
//    {
////        if(!directory.isDirectory())
////        {
////            throw new RuntimeException(directory.getName() + " is not a directory");
////        }
////
////        List<File> fileList = new ArrayList<>(Arrays.asList(new File(path + "temp").listFiles())); //creates a List of File objects representing every file in specified parameter directory
////        fileList.removeIf(File::isHidden); //removes all File objects from List that are hidden
////
////        File[] files = fileList.toArray(new File[0]); //creates an array of File objects using the contents of the modified List
////        Arrays.sort(files); //sorts files
////        File file;
////
////        for(int i= 0; i < files.length; i++)
////        {
////            file= files[i];
////            if(directory.isDirectory())
////            {
////                clearDirectory(file);
////            }
////            else
////            {
////                if(!file.delete())
////                {
////                    throw new RuntimeException("Unable to delete file " + file.getName() + ". Check write perms");
////                }
////            }
////        }
////        if(!directory.delete())
////        {
////            throw new RuntimeException("Unable to delete directory " + directory.getName() + ". Check write perms.");
////        }
//        for(File subfile : Objects.requireNonNull(directory.listFiles()))
//        {
//            if(subfile.isDirectory())
//            {
//                clearDirectory(subfile);
//            }
//            else
//            {
//                subfile.delete();
//            }
//        }
//        directory.delete();
//    }
//
//    private String hexFormat(String hexVal)
//    {
//        assert hexVal.length() <= 4;
//        StringBuilder hexValBuilder = new StringBuilder(hexVal);
//        while(hexValBuilder.length() != 4)
//        {
//            if(hexValBuilder.length() != 3)
//            {
//                hexValBuilder.insert(0, "0");
//            }
//            else
//            {
//                hexValBuilder.insert(0, "f");
//            }
//        }
//        hexVal = hexValBuilder.toString();
//        return hexVal;
//    }
//
//    private boolean compareDirs(File dir1, File dir2)
//    {
//        File[] dir1List= dir1.listFiles();
//        File[] dir2List= dir2.listFiles();
//        assert dir1List != null;
//        assert dir2List != null;
//        if(dir1List.length != dir2List.length)
//        {
//            return false;
//        }
//
//        File dir1File;
//        File dir2File;
//        for(int i= 0; i < dir1List.length; i++)
//        {
//            dir1File= dir1List[i];
//            dir2File= dir2List[i];
////            if(!dir1File.equals(dir2File))
////            {
////                //throw new RuntimeException("File \"" + dir1File.getName() + "\" in dir1 and file \"" + dir2File.getName() + "\" in dir2 do not match");
////                System.out.println("File \"" + dir1File.getName() + "\" in dir1 and file \"" + dir2File.getName() + "\" in dir2 do not match");
////            }
//            if(dir1File.equals(dir2File))
//            {
//                System.out.println("File \"" + dir1File.getName() + "\" in dir1 and file \"" + dir2File.getName() + "\" in dir2 match");
//            }
//        }
//        return true;
//    }
//
//    private void sort (File[] arr)
//    {
//        Arrays.sort(arr, Comparator.comparingInt(DsRomReader::fileToInt));
//    }
//
//    private static int fileToInt (File f)
//    {
//        return Integer.parseInt(f.getName().split("\\.")[0]);
//    }
//
//    private void setFileData(int fileId)
//    {
//        fileOffset= (int) fimgEntries.get(fileId).getStartingOffset();
//        length= (int) fimgEntries.get(fileId).getEndingOffset()-fileOffset;
//        fileID= fileId;
//    }
//
//    private String[] generatePaths(HashMap<String,String> map)
//    {
//        ArrayList<String> pathList= new ArrayList<>();
//        StringBuilder path;
//        for(String dirId : map.keySet())
//        {
//            path= new StringBuilder();
//            path.append("/").append(dirId);
//            String parentDir= map.get(dirId);
//            path.insert(0,"/" + parentDir);
//            while(!parentDir.equals("0xf000"))
//            {
//                parentDir= map.get(parentDir);
//                path.insert(0,"/" + parentDir);
//            }
////            if(!map.containsValue(dirId))
////            {
//                pathList.add(path.toString());
////            }
//
//        }
//        return pathList.toArray(new String[0]);
//    }
//
//    private static boolean containsObj(Object[] arr, Object value)
//    {
//        for (Object obj : arr)
//        {
//            if (obj.equals(value))
//                return true;
//        }
//        return false;
//    }
//
//    private String byteArrToString(byte... arr)
//    {
//        StringBuilder str= new StringBuilder();
//        for(byte b : arr) {
//            str.append((b + "").length() == 2 ? b : "0" + b);
//        }
//        return str.toString();
//    }
//
//
//    public short calculateCRC16(byte... arr)
//    {
//        CRC16 crc16= new CRC16();
//
//        for(byte b : arr){
//            crc16.update(b);
//        }
//        return (short) crc16.getValue();
//    }
//
//}
//
