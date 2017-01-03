package org.jenkinsci.plugins.marketfeaturereport;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.Descriptor;
import hudson.tasks.Publisher;
import hudson.util.DescribableList;
import org.jenkinsci.plugins.marketfeaturereport.report.Report;

import java.io.Serializable;
import java.util.ArrayList;


public class MarketFeatureProjectAction implements Action, Serializable {


    private static final long serialVersionUID = 1L;


    public static final String URL_NAME = "aciResult";


    private AbstractProject<?, ?> project;


    public MarketFeatureProjectAction(final AbstractProject<?, ?> project) {
        this.project = project;
    }


    public String getIconFileName() {
        return null;
    }

    public String getDisplayName() {
        //        return "ProjectAction";
        return null;
    }

    public String getUrlName() {
        //        return URL_NAME;
        return null;
    }


    public AbstractBuild<?, ?> getLastFinishedBuild() {
        AbstractBuild<?, ?> lastBuild = project.getLastBuild();
        while (lastBuild != null && (lastBuild.isBuilding()
                || lastBuild.getAction(MarketFeatureBuildAction.class) == null)) {

            lastBuild = lastBuild.getPreviousCompletedBuild();
        }
        return lastBuild;
    }

    public Report getReport() {
        AbstractBuild<?, ?> build = getLastFinishedBuild();
        // When the project has not had a build yet, build is null
        if (build == null) {
            return null;
        }
        MarketFeatureBuildAction resultAction =
                build.getAction(MarketFeatureBuildAction.class);
        return resultAction.getReport();
    }


    public ArrayList< ArrayList<String> > getFileError() {
        AbstractBuild<?, ?> build = getLastFinishedBuild();
        // When the project has not had a build yet, build is null
        if (build == null) {
            return null;
        }
        MarketFeatureBuildAction resultAction =
                build.getAction(MarketFeatureBuildAction.class);
        return resultAction.getFileError();
    }


    public String getProjectName() {
        String str = project.getName();
        str = str.replace(".", "dot");
        return str;
    }


    public boolean isShownOnProjectPage() {
        DescribableList<Publisher, Descriptor<Publisher>> publishers =
                project.getPublishersList();
        MarketFeaturePublisher publisher =
                publishers.get(MarketFeaturePublisher.class);
        return publisher.isShownOnProjectPage();
    }
}
