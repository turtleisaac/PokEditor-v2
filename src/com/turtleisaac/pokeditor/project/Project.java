package com.turtleisaac.pokeditor.project;

import com.turtleisaac.pokeditor.framework.XmlReader;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

public class Project implements Serializable
{
    private String name;
    private File projectPath;
    private int generation;
    private Game baseRom;
    private String baseRomGameCode;
    private String language;
    private String program;
    private boolean backups;


    public Project(String name, String path, String program)
    {
        this.name= name;
        projectPath= new File(path);
        this.program= program;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public File getProjectPath()
    {
        return projectPath;
    }

    public void setProjectPath(File projectPath)
    {
        this.projectPath = projectPath;
    }

    public int getGeneration()
    {
        return generation;
    }

    public void setGeneration(int generation)
    {
        this.generation = generation;
    }

    public Game getBaseRom()
    {
        return baseRom;
    }

    public void setBaseRom(Game baseRom)
    {
        this.baseRom = baseRom;
    }

    public String getBaseRomGameCode()
    {
        return baseRomGameCode;
    }

    public void setBaseRomGameCode(String baseRomGameCode)
    {
        this.baseRomGameCode = baseRomGameCode;
    }

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }

    public String getProgram()
    {
        return program;
    }

    public void setProgram(String program)
    {
        this.program = program;
    }

    public boolean isBackups()
    {
        return backups;
    }

    public void setBackups(boolean backups)
    {
        this.backups = backups;
    }

    public String getDataPath()
    {
        return new File(getProjectPath().getAbsolutePath() + File.separator + getName() + File.separator + "data").getAbsolutePath();
    }

    public String getXml()
    {
        String ret = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n";
        ret += "<" + program.toLowerCase() + ">\n";
        ret += "  <name>" + name + "<\\name>\n";
        ret += "  <baseromname>" + baseRom + "<\\baseromname>\n";
        ret += "  <baseromgamecode>" + baseRomGameCode + "<\\baseromgamecode>\n";
        ret += "  <language>" + language.toUpperCase() + "<\\language>\n";
        ret += "  <backups>" + backups + "<\\backups>\n";
        ret += "<\\" + program.toLowerCase() + ">";

        return ret;
    }

    @Override
    public String toString()
    {
        return "Project{" +
                "name='" + name + '\'' +
                ", projectPath=" + projectPath +
                ", generation=" + generation +
                ", baseRomName='" + baseRom + '\'' +
                ", baseRomGameCode='" + baseRomGameCode + '\'' +
                ", language='" + language + '\'' +
                ", program='" + program + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return generation == project.generation &&
                Objects.equals(name, project.name) &&
                Objects.equals(projectPath, project.projectPath) &&
                Objects.equals(baseRom, project.baseRom) &&
                Objects.equals(baseRomGameCode, project.baseRomGameCode) &&
                Objects.equals(language, project.language) &&
                Objects.equals(program, project.program);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, projectPath, generation, baseRom, baseRomGameCode, language, program);
    }

    public static Project readFromXml(String path) throws IOException
    {
        XmlReader xmlReader= new XmlReader(path);

        HashMap<String,String> data = xmlReader.readFile();

//        System.out.println(data.toString());

        Project project= new Project(data.get("name"),path.substring(0,path.lastIndexOf(File.separator)),data.get("project"));
        project.setBaseRom(parseBaseRom(data.get("baseromgamecode")));
        project.setBaseRomGameCode(data.get("baseromgamecode"));
        project.setLanguage(data.get("language"));
        project.setProgram(data.get("program"));
        if (data.containsKey("backups"))
        {
            project.setBackups(Boolean.parseBoolean(data.get("backups")));
        }
        else
        {
            project.setBackups(false);
        }


        return project;
    }

    public static Game parseBaseRom(String baseRomGameCode)
    {
        switch (baseRomGameCode.substring(0,3))
        {
            case "ADA":
                return Game.Diamond;

            case "APA":
                return Game.Pearl;

            case "CPU":
                return Game.Platinum;

            case "IPK":
                return Game.HeartGold;

            case "IPG":
                return Game.SoulSilver;

            case "IRB":
                return Game.Black;

            case "IRA":
                return Game.White;

            case "IRO":
                return Game.White2;

            case "IRD":
                return Game.Black2;

            default:
                throw new RuntimeException("Invalid game");
        }
    }

    public String getBaseTitle()
    {
        switch (baseRomGameCode.substring(0,3))
        {
            case "ADA":
                return "Pokemon Diamond";

            case "APA":
                return "Pokemon Pearl";

            case "CPU":
                return "Pokemon Platinum";

            case "IPK":
                return "Pokemon HeartGold";

            case "IPG":
                return "Pokemon SoulSilver";

            case "IRB":
                return "POKEMON B";

            case "IRA":
                return "POKEMON W";

            case "IRO":
                return "POKEMON B2";

            case "IRD":
                return "POKEMON W2";

            default:
                throw new RuntimeException("Invalid game");
        }
    }

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

    public static boolean isPrimary(Project project)
    {
        return project.getBaseRom() == Game.Platinum || project.getBaseRom() == Game.HeartGold || project.getBaseRom() == Game.SoulSilver;
    }

    public static boolean isHGSS(Project project)
    {
        return project.getBaseRom() == Game.HeartGold || project.getBaseRom() == Game.SoulSilver;
    }

    public static boolean isDPPT(Project project)
    {
        return project.getBaseRom() == Game.Diamond || project.getBaseRom() == Game.Pearl || project.getBaseRom() == Game.Platinum;
    }

    public static boolean isPlatinum(Project project)
    {
        return project.getBaseRom() == Game.Platinum;
    }

    public static void saveProject(Project project, String path)
    {

    }
}
