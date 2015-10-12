package org.jenkinsci.plugins.marketfeaturereport;
import hudson.Launcher;
import hudson.Extension;
import hudson.model.*;
import hudson.tasks.*;
import hudson.util.FormValidation;
import net.sf.json.JSONObject;
import org.apache.tools.ant.types.FileSet;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.QueryParameter;

import javax.servlet.ServletException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.tools.ant.Project;
import org.xml.sax.SAXException;

/**
        * Post build tasks added as {@link Recorder}.
        *
        * @author Caleb Carvalho
        */
public class MarketFeatureBuilder extends Recorder {

    private final String name;

    // Fields in config.jelly must match the parameter names in the "DataBoundConstructor"
    @DataBoundConstructor
    public MarketFeatureBuilder(String name) {
        this.name = name;
    }

    /**
     * We'll use this from the <tt>config.jelly</tt>.
     */
    public String getName() {
        return name;
    }

    @Override
    public Action getProjectAction(final AbstractProject<?, ?> project) {
        return new MarketFeatureProjectAction(project);
    }

    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener)
            throws IOException {


        listener.getLogger().println("Performing Post build task..." + name);
        File f = new File(name);
        if (f.exists()) {
            listener.getLogger().println("Found file under " + f.getPath());
        }
//        FileSet fileSet = new FileSet();
//        File workspace = build.getArtifactsDir();
//        fileSet.setDir(workspace);
//        fileSet.setIncludes(name);
//        Project antProject = new Project();
//        fileSet.setProject(antProject);
//        String[] tmpFiles
//                = fileSet.getDirectoryScanner(antProject)
//                .getIncludedFiles();

        try {
            MarketFeatureBuildAction buildAction = new MarketFeatureBuildAction(build, name);
            build.addAction(buildAction);

        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return false;
        } catch (SAXException e) {
            e.printStackTrace();
            return false;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)super.getDescriptor();
    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.BUILD;
    }

    @Extension
    public static final  class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        public DescriptorImpl(){
            super(MarketFeatureBuilder.class);
            load();
        }

        @Override public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        @Override public String getDisplayName() {
            return "Market Feature 2.0";
        }
    }
}

