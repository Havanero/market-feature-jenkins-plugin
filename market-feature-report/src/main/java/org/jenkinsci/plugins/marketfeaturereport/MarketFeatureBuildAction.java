/*
 * The MIT License
 *
 * Copyright (c) 2012, Thomas Deruyter, Raynald Briand
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jenkinsci.plugins.marketfeaturereport;

import hudson.model.AbstractBuild;
import hudson.model.Action;
import org.jenkinsci.plugins.marketfeaturereport.report.Report;
import org.kohsuke.stapler.StaplerProxy;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Class describing action performed on build page.
 */
public class MarketFeatureBuildAction implements Action,
        Serializable, StaplerProxy {

    /**
     * Id of the class.
     */
    private static final long serialVersionUID = 1L;
    /**
     * URL to access data.
     */
    public static final String URL_NAME = "aciResult";
    private AbstractBuild<?, ?> build;
    private String result;
    private Report report;
    private ArrayList<ArrayList<String>> fileError;

    /**
     * Constructor.
     *
     * @param build The current build
     * @param files The current files
     * @throws InterruptedException         Interruption
     * @throws ParserConfigurationException Exception in parser configuration
     * @throws SAXException                 Exception in XML parser
     * @throws URISyntaxException           Exception in URL
     * @throws IOException                  Exception with I/Os
     */
    public MarketFeatureBuildAction(final AbstractBuild<?, ?> build,
            final ArrayList<String> files) throws InterruptedException,
            ParserConfigurationException, SAXException, URISyntaxException,
            IOException {
        this.build = build;
        this.report = new Report();
        this.fileError = new ArrayList<>();

        for (int i = 0; i < files.size(); i++) {

            String currentReport = files.get(i);

            String path = build.getArtifactsDir().getAbsolutePath()
                    + File.separatorChar + currentReport;

            ParseCss parseCss = new ParseCss();
            if (parseCss.parse(path)) {
                parseCss.ReadResults();
                this.report.addSection(parseCss.result());
            } else {
                ArrayList<String> list = new ArrayList<>();
                list.add("#FF0000"); // titleColor
                list.add("Market Feature Error"); // fieldName
                list.add("#000000"); // detailColor

                String str = build.getArtifactsDir().getName();
                str = str.replace("archive", build.getNumber() + "/artifact");

                list.add(str + File.separatorChar + currentReport);
                // href project page

                str = build.getArtifactsDir().getName();
                str = str.replace("archive", "artifact");

                list.add(str + File.separatorChar + currentReport);
                // href build page
                list.add("Parsing"); // fieldValue
                fileError.add(list);
            }
        }

    }

    /**
     * Get Report.
     */
    public Report getReport() {
        return report;
    }

    /**
     * Get Error File.
     */
    public ArrayList<ArrayList<String>> getFileError() {
        return fileError;
    }

    /**
     * Get Summary.
     */
    public String getSummary() {
        StringBuilder builder = new StringBuilder();
        return builder.toString();
    }

    /**
     * Get Details.
     */
    public String getDetails() {
        StringBuilder builder = new StringBuilder();
        return builder.toString();
    }

    /**
     * Get Result.
     */
    public String getResult() {
        return this.result;
    }

    /**
     * Get current build.
     */
    AbstractBuild<?, ?> getBuild() {
        return this.build;
    }

    /**
     * Get Target.
     */
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

    /**
     * Get Previous action.
     */
    MarketFeatureBuildAction getPreviousAction() {
        AbstractBuild<?, ?> previousBuild = this.build.getPreviousBuild();
        if (previousBuild != null) {
            return previousBuild.getAction(MarketFeatureBuildAction.class);
        }
        return null;
    }

    /**
     * Get Project Name.
     */
    public String getProjectName() {
        String str = build.getParent().getName();
        str = str.replace(".", "dot");
        return str;
    }

    /**
     * Get Build Number.
     */
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

    /**
     * The three functions getIconFileName,getDisplayName,getUrlName create a
     * link to a new page with url : http://{root}/job/{job name}/URL_NAME for
     * the page of the build.
     */
    public String getDisplayName() {
        // return "ACIAction";
        return null;
    }

    /**
     * The three functions getIconFileName,getDisplayName,getUrlName create a
     * link to a new page with url : http://{root}/job/{job name}/URL_NAME for
     * the page of the build.
     */
    public String getUrlName() {
        // return URL_NAME;
        return null;
    }
}
