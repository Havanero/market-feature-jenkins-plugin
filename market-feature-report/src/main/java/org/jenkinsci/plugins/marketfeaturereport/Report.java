package org.jenkinsci.plugins.marketfeaturereport;

import java.util.ArrayList;

public class Report {

    private ArrayList reportSection;

    /**
     * Initialize the section.
     */
    public Report() {
        reportSection = new ArrayList();
    }

    /**
     * Return the current list of section.
     *
     * @return section
     * 		the current section list
     */
    public ArrayList getSection() {
        return reportSection;
    }

    /**
     * Assign a section list to the report.
     *
     * @param section
     * 		the section list to set
     */
    public void setSection(final ArrayList section) {
        this.reportSection = section;
    }

    /**
     * Add a new section to the report.
     *
     * @param section
     * 		The section to add in the list of sections
     */
    public void addSection(final String section) {
        this.reportSection.add(section);
    }

}
