package org.jenkinsci.plugins.marketfeaturereport;
import hudson.Launcher;
import hudson.Extension;
import hudson.model.*;
import hudson.tasks.*;
import hudson.util.FormValidation;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.QueryParameter;

import javax.servlet.ServletException;
import java.io.IOException;

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
        // This is where you 'build' the project.
        // Since this is a dummy, we just say 'hello world' and call that a build.

         //This also shows how you can consult the global configuration of the builder
        listener.getLogger().println("Performing Post build task..." + name);



        return true;
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)super.getDescriptor();
    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @Extension
    public static final  class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        @Override public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        @Override public String getDisplayName() {
            return "Market Feature 2.0";
        }
    }
}

