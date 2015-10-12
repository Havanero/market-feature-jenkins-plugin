package org.jenkinsci.plugins.marketfeaturereport;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;

import java.io.Serializable;

public class MarketFeatureProjectAction implements Action, Serializable {

    private static final long serialVersionUID = 1L;

    private AbstractProject<?, ?> project;

    public MarketFeatureProjectAction(final AbstractProject<?, ?> project) {
        this.project = project;
    }

    @Override public String getIconFileName() {
        return null;
    }

    @Override public String getDisplayName() {
        return null;
    }

    @Override public String getUrlName() {
        return null;
    }
    public AbstractBuild<?, ?> getLastFinishedBuild() {
        AbstractBuild<?, ?> lastBuild = project.getLastBuild();
        while (lastBuild != null && (lastBuild.isBuilding()
                || lastBuild.getAction(MarketFeatureBuildAction.class) == null)) {
            lastBuild = lastBuild.getPreviousBuild();
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
}
