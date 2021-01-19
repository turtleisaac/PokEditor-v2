package com.turtleisaac.pokeditor;

import com.turtleisaac.pokeditor.project.Project;
import com.turtleisaac.pokeditor.gui.Theme;

import java.io.Serializable;
import java.util.ArrayList;

public class Config implements Serializable
{
    private ArrayList<Project> projects;
    private Theme theme;

    public Config()
    {
        projects= new ArrayList<>();
        theme= Theme.Darcula;
    }

    public Config(Theme theme)
    {
        projects= new ArrayList<>();
        this.theme= theme;
    }

    private Config(ArrayList<Project> projects, Theme theme)
    {
        this.projects= projects;
        this.theme= theme;
    }

    public ArrayList<Project> getProjects()
    {
        return projects;
    }

    public Project getProject(int i)
    {
        return projects.get(i);
    }

    public Theme getTheme()
    {
        return theme;
    }

    public void setProjects(ArrayList<Project> projects)
    {
        this.projects = projects;
    }

    public void addProject(Project project)
    {
        projects.add(project);
    }

    public void addProject(Project project, int i)
    {
        projects.add(i, project);
    }

    public void setProject(Project project, int i)
    {
        projects.set(i, project);
    }

    public void removeProject(Project project)
    {
        projects.remove(project);
    }

    public void removeProject(int i)
    {
        projects.remove(i);
    }

    public void setTheme(Theme theme)
    {
        this.theme = theme;
    }

    public static Config copyOf(Config config)
    {
        return new Config(config.getProjects(), config.getTheme());
    }

}
