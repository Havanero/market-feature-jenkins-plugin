package org.jenkinsci.plugins.marketfeaturereport;

import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.BuildListener;

import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Recorder;
import java.io.File;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.Project;

import org.kohsuke.stapler.DataBoundConstructor;
import org.xml.sax.SAXException;


public class MarketFeaturePublisher extends Recorder {

    /* The list name of the files to parse */
    private String name;

    /**
     * Whether the reports are shown on the project page or not.
     * By default, the reports are shown on the project page as
     * well as on the results page. When the reports are very long
     * the user can chose to display them only on the results page.
     */
    private boolean shownOnProjectPage = true;

    /**
     * Set the name of the publisher.
     * @param name
     * 		The name of the published report
     * @param shownOnProjectPage
     * 		Whether or not to display the report on the Project page
     */
    @DataBoundConstructor
    public MarketFeaturePublisher(final String name,
            final boolean shownOnProjectPage) {
        this.name = name;
        this.shownOnProjectPage = shownOnProjectPage;
    }

    /**
     * Get the name of the publisher.
     * @return string
     * 		The name of the published report
     */
    public String getName() {
        return name;
    }

    /**
     * Get whether or not to display the report on the Project page.
     * @return boolean
     *      Whether or not to display the report on the Project page
     */
    public boolean isShownOnProjectPage() {
        return shownOnProjectPage;
    }

    /**
     * Get the action associated to the publisher.
     * @param project
     * 		Project on which to apply publication
     */
    @Override
    public Action getProjectAction(final AbstractProject<?, ?> project) {
        return new MarketFeatureProjectAction(project);
    }

    /**
     * Perform the publication.
     * @param build
     * 		Build on which to apply publication
     * @param launcher
     * 		Unused
     * @param listener
     * 		Unused
     * @return boolean
     * 		true if the publishing successfully complete
     * 		true if the publishing could not complete
     * @throws IOException
     * 		In case of file IO mismatch
     * @throws InterruptedException
     * 		In case of interuption
     */
    @Override
    public boolean perform(
            final AbstractBuild<?, ?> build,
            final Launcher launcher,
            final BuildListener listener)
            throws IOException, InterruptedException {

        /**
         * Define if we must parse multiple file by searching for ','
         * in the name var.
         */
        String[] files = name.split(",");

        ArrayList<String> filesToParse = new ArrayList<String>();
        for (int i = 0; i < files.length; i++) {
            FileSet fileSet = new FileSet();
            File workspace = build.getArtifactsDir();
            fileSet.setDir(workspace);
            fileSet.setIncludes(files[i].trim());
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
            for (int j = 0; j < tmpFiles.length; j++) {
                filesToParse.add(tmpFiles[j]);
            }

        }

        MarketFeatureBuildAction buildAction;
        try {
            buildAction = new MarketFeatureBuildAction(build, filesToParse);
            build.addAction(buildAction);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * Method that returns the status of the service required.
     */
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.BUILD;
    }
}