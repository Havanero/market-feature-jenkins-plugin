package org.jenkinsci.plugins.marketfeaturereport;

import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Recorder;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;


public class MarketFeaturePublisher extends Recorder {

    private String name;

    private boolean shownOnProjectPage = true;

    @DataBoundConstructor
    public MarketFeaturePublisher(final String name,
            final boolean shownOnProjectPage) {
        this.name = name;
        this.shownOnProjectPage = shownOnProjectPage;
    }


    public String getName() {
        return name;
    }

    public boolean isShownOnProjectPage() {
        return shownOnProjectPage;
    }


    @Override
    public Action getProjectAction(final AbstractProject<?, ?> project) {
        return new MarketFeatureProjectAction(project);
    }

    @Override
    public boolean perform(
            final AbstractBuild<?, ?> build,
            final Launcher launcher,
            final BuildListener listener)
            throws IOException, InterruptedException {

        String files = name;

        ArrayList<String> filesToParse = new ArrayList<>();
            FileSet fileSet = new FileSet();
            File workspace = build.getArtifactsDir();
            fileSet.setDir(workspace);
            fileSet.setIncludes(files);
            Project antProject = new Project();
            fileSet.setProject(antProject);
            String[] tmpFiles;
            try {
                tmpFiles = fileSet.getDirectoryScanner(antProject)
                        .getIncludedFiles();
            } catch (Exception ex) {
                listener.getLogger().println(ex.toString());
                return false;
            }
        Collections.addAll(filesToParse, tmpFiles);



        MarketFeatureBuildAction buildAction;
        try {
            buildAction = new MarketFeatureBuildAction(build, filesToParse);
            build.addAction(buildAction);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return true;
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.BUILD;
    }
}