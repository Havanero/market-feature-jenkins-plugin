package org.jenkinsci.plugins.marketfeaturereport;

import hudson.model.AbstractBuild;
import hudson.model.Action;
import org.kohsuke.stapler.StaplerProxy;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class MarketFeatureBuildAction implements Action,
        Serializable, StaplerProxy {

    public static final String URL_NAME = "aciResult";
    private AbstractBuild<?, ?> build;
    private String result;
    private Report report;
    private ArrayList<ArrayList<String>> fileError;

    public MarketFeatureBuildAction(final AbstractBuild<?, ?> build,
            final ArrayList<String> files) throws InterruptedException,
            ParserConfigurationException, SAXException, URISyntaxException,
            IOException {
        this.build = build;
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

    @Override public Object getTarget() {
        return null;
    }

    public Report getReport() {
        return report;
    }
}
