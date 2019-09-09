package com.se231.onlineedu.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * SectionRepository Entity Class
 *
 * a section is a teaching module in the convenience of teacher to manage resource.
 *
 * @author Yuxuan Liu
 *
 * @date 2019/7/4
 */
@Entity
public class Section {
    @EmbeddedId
    private SectionPrimaryKey sectionPrimaryKey;

    private String title;

    @JsonIgnore
    @ManyToMany
    private List<Paper> papers;

    private int secNo;

    @ManyToMany
    @JsonIgnore
    private List<Resource> resources;

    @JsonManagedReference
    @OneToMany(mappedBy = "sectionBranchesPrimaryKey.section")
    private List<SectionBranches> sectionBranchesList;


    public Section() {
        resources=new ArrayList<>();
        papers=new ArrayList<>();
    }

    public SectionPrimaryKey getSectionPrimaryKey() {
        return sectionPrimaryKey;
    }

    public void setSectionPrimaryKey(SectionPrimaryKey sectionPrimaryKey) {
        this.sectionPrimaryKey = sectionPrimaryKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public List<SectionBranches> getSectionBranchesList() {
        return sectionBranchesList;
    }

    public void setSectionBranchesList(List<SectionBranches> sectionBranchesList) {
        this.sectionBranchesList = sectionBranchesList;
    }

    public int getSecNo() {
        return secNo;
    }

    public void setSecNo(int secNo) {
        this.secNo = secNo;
    }
}
