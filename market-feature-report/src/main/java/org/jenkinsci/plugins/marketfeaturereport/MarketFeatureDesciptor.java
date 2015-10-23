package org.jenkinsci.plugins.marketfeaturereport;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;

@Extension
public class MarketFeatureDesciptor  extends BuildStepDescriptor<Publisher> {

    public MarketFeatureDesciptor(){
        super(MarketFeaturePublisher.class);
        load();
    }

    public boolean isApplicable(Class<? extends AbstractProject> aClass) {
        return true;
    }

    @Override public String getDisplayName() {
        return "Market Features";
    }
}
