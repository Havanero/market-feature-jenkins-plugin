package org.jenkinsci.plugins.marketfeaturereport;

import hudson.model.AbstractBuild;
import hudson.model.Action;
import org.jenkinsci.plugins.marketfeaturereport.report.Report;
import org.kohsuke.stapler.StaplerProxy;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;


public class MarketFeatureBuildAction implements Action,
        Serializable, StaplerProxy {

    private static final long serialVersionUID = 1L;

    private AbstractBuild<?, ?> build;
    private String result;
    private Report report;
    private ArrayList<ArrayList<String>> fileError;


    public MarketFeatureBuildAction(final AbstractBuild<?, ?> build,
            final ArrayList<String> files) throws InterruptedException, URISyntaxException,
            IOException {
        this.build = build;
        this.report = new Report();
        this.fileError = new ArrayList<>();

        for (String currentReport : files) {

            String path = build.getArtifactsDir().getAbsolutePath()
                    + File.separatorChar + currentReport;

            ParseCss parseCss = new ParseCss();
            if (parseCss.parse(path)) {
                parseCss.ReadResults();
                this.report.addSection(parseCss.result());
            } else {
                ArrayList<String> list = new ArrayList<>();
                list.add("#FF0000");
                list.add("Market Feature Error");
                list.add("#000000");
                String str = build.getArtifactsDir().getName();
                str = str.replace("archive", build.getNumber() + "/artifact");

                list.add(str + File.separatorChar + currentReport);
                str = build.getArtifactsDir().getName();
                str = str.replace("archive", "artifact");

                list.add(str + File.separatorChar + currentReport);
                list.add("Parsing");
                fileError.add(list);
            }
        }

    }


    public Report getReport() {
        return report;
    }

    public ArrayList<ArrayList<String>> getFileError() {
        return fileError;
    }


    public String getSummary() {
        StringBuilder builder = new StringBuilder();
        return builder.toString();
    }


    public String getDetails() {
        StringBuilder builder = new StringBuilder();
        return builder.toString();
    }


    public String getResult() {
        return this.result;
    }


    AbstractBuild<?, ?> getBuild() {
        return this.build;
    }

    public Object getTarget() {
        return this.result;
    }

    /**
     * Get Previous result.
     */
    String getPreviousResult() {
        MarketFeatureBuildAction previousAction = this.getPreviousAction();
        String previousResult = null;
        if (previousAction != null) {
            previousResult = previousAction.getResult();
        }
        return previousResult;
    }


    MarketFeatureBuildAction getPreviousAction() {
        AbstractBuild<?, ?> previousBuild = this.build.getPreviousBuild();
        if (previousBuild != null) {
            return previousBuild.getAction(MarketFeatureBuildAction.class);
        }
        return null;
    }


    public String getProjectName() {
        String str = build.getParent().getName();
        str = str.replace(".", "dot");
        return str;
    }


    public int getBuildNumber() {
        return build.getNumber();
    }

    /**
     * The three functions getIconFileName,getDisplayName,getUrlName create a
     * link to a new page with url : http://{root}/job/{job name}/URL_NAME for
     * the page of the build.
     */
    public String getIconFileName() {
        // return "/plugin/summary_report/icons/summary_report.png";
        return null;
    }

    public String getDisplayName() {
        // return "Build Action";
        return null;
    }

    public String getUrlName() {
        // return URL_NAME;
        return null;
    }
}
