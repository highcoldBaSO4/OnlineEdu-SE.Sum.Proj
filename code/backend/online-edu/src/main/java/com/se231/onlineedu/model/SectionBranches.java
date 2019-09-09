package com.se231.onlineedu.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * Section Branches Entity Class
 *
 * section branches under a section to divide the section into smaller parts.
 *
 * @author Zhe Li
 * @date 2019/07/15
 */
@Entity
public class SectionBranches {
    @EmbeddedId
    private SectionBranchesPrimaryKey sectionBranchesPrimaryKey;

    private String title;

    private String description;

    @ManyToMany
    private List<Paper> papers;

    private int branchNo;

    @ManyToMany
    private List<Resource> resources;

    public SectionBranches() {
        papers = new ArrayList<>();
        resources = new ArrayList<>();
    }

    public SectionBranches(SectionBranchesPrimaryKey sectionBranchesPrimaryKey, String title, List<Paper> papers, List<Resource> resources) {
        this.sectionBranchesPrimaryKey = sectionBranchesPrimaryKey;
        this.title = title;
        this.papers = papers;
        this.resources = resources;
    }

    public SectionBranchesPrimaryKey getSectionBranchesPrimaryKey() {
        return sectionBranchesPrimaryKey;
    }

    public void setSectionBranchesPrimaryKey(SectionBranchesPrimaryKey sectionBranchesPrimaryKey) {
        this.sectionBranchesPrimaryKey = sectionBranchesPrimaryKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Paper> getPapers() {
        return papers;
    }

    public void setPapers(List<Paper> papers) {
        this.papers = papers;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    public int getBranchNo() {
        return branchNo;
    }

    public void setBranchNo(int branchNo) {
        this.branchNo = branchNo;
    }
}
