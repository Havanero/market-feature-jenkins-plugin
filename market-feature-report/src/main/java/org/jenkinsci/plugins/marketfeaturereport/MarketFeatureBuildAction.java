package org.jenkinsci.plugins.marketfeaturereport;

import hudson.model.AbstractBuild;
import hudson.model.Action;
import org.kohsuke.stapler.StaplerProxy;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class MarketFeatureBuildAction implements Action,
        Serializable, StaplerProxy {

    private static final long serialVersionUID = 1L;
    public static final String URL_NAME = "aciResult";
    private AbstractBuild<?, ?> build;
    private String result;
    private Report report;
    private ArrayList<ArrayList<String>> fileError;

    public MarketFeatureBuildAction(final AbstractBuild<?, ?> build,
            final String files) throws InterruptedException,
            IOException {
        this.build = build;
        this.report = new Report();
        String currentReport = files;
        String file_path = build.getArtifactsDir().getAbsolutePath()
                + File.separatorChar + currentReport;

        System.out.println("Running Features Builder...." + file_path);
        this.report.addSection("testing ");
        this.result = build.getDisplayName();
        System.out.println("result " + this.result);

    }

    @Override public String getIconFileName() {
        return null;
    }

    @Override
    public String getDisplayName() {
        System.out.println("calling display");
        return "get display name";
    }

    @Override public String getUrlName() {
        return "get URL name";
    }

    @Override public Object getTarget() {
        return null;
    }

    public Report getReport() {
        return report;
    }

    AbstractBuild<?, ?> getBuild() {
        return this.build;
    }

}
