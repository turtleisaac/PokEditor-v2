//import encounters.johto.EncounterEditor;
//import evolutions.gen4.EvolutionEditor;
//import growth.GrowthEditor;
//import learnsets.LearnsetEditor;
//import personal.gen4.PersonalEditor;
//
//import java.io.*;
//
//public class Editor
//{
//    public static void main(String[] args) throws IOException
//    {
//        switch (args[0]) {
//            case "personal":
//                PersonalEditor personalEditor = new PersonalEditor();
//                if (args[1].equals("toCsv")) {
//                    personalEditor.personalToCSV(args[2]);
//                } else if (args[1].equals("toPersonal")) {
//                    personalEditor.csvToPersonal(args[2], args[3], args[4]);
//                } else {
//                    throw new RuntimeException("Invalid arguments");
//                }
//                break;
//            case "learnsets":
//                LearnsetEditor learnsetEditor = new LearnsetEditor();
//                if (args[1].equals("toCsv")) {
//                    learnsetEditor.learnsetToCsv(args[2]);
//                } else if (args[1].equals("toLearnsets")) {
//                    learnsetEditor.csvToLearnsets(args[2], args[3]);
//                } else {
//                    throw new RuntimeException("Invalid arguments");
//                }
//
//                break;
//            case "evolutions":
//                EvolutionEditor evolutionEditor = new EvolutionEditor();
//                if (args[1].equals("toCsv")) {
//                    evolutionEditor.evolutionToCsv(args[2], false);
//                } else if (args[1].equals("toEvolutions")) {
//                    evolutionEditor.csvToEvolutions(args[2], args[3]);
//                } else {
//                    throw new RuntimeException("Invalid arguments");
//                }
//
//                break;
//            case "growth":
//                GrowthEditor growthEditor = new GrowthEditor();
//                if (args[1].equals("toCsv")) {
//                    growthEditor.growthToCsv(args[2]);
//                } else if (args[1].equals("toGrowth")) {
//                    growthEditor.csvToGrowth(args[2], args[3]);
//                } else {
//                    throw new RuntimeException("Invalid arguments");
//                }
//
//                break;
//            case "encounters":
//                EncounterEditor encounterEditor = new EncounterEditor();
//                if (args[1].equals("toCsv")) {
//                    encounterEditor.encountersToCsv(args[2]);
//                } else if (args[1].equals("toEncounters")) {
//                    encounterEditor.csvToEncounters(args[2],args[3]);
//                } else {
//                    throw new RuntimeException("Invalid arguments");
//                }
//
//                break;
//
////            case "headbutt":
////                Encounters.Johto.HeadbuttEncounterEditor headbuttEditor= new Encounters.Johto.HeadbuttEncounterEditor();
////                if(args[1].equals("toCsv")) {
////                    headbuttEditor.headbuttToCsv("a252SS");
////                } else if (args[1].equals("toHeadbutt")) {
////                    headbuttEditor.csvToHeadbutt("headbuttEncounters.csv","headbuttEncounters Recompile");
////                } else {
////                    throw new RuntimeException("Invalid arguments");
////                }
////                break;
//            default:
//                throw new RuntimeException("Invalid arguments");
//        }
//    }
//}
