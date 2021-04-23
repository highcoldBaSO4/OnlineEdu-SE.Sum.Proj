package com.se231.onlineedu.model;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * Section Branches' Primary Key
 *
 * a section branch should be identified by a section and a branch number.
 *
 * @author Zhe Li
 * @date 2019/07/15
 */
@Embeddable
public class SectionBranchesPrimaryKey implements Serializable {
    private static final Long serialVersionUID = 1L;

    @ManyToOne
    @JsonBackReference
    private Section section;

    private int branchId;

    public SectionBranchesPrimaryKey(Section section, int branchId) {
        this.section = section;
        this.branchId = branchId;
    }

    public SectionBranchesPrimaryKey() {
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchNo(int branchId) {
        this.branchId = branchId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SectionBranchesPrimaryKey that = (SectionBranchesPrimaryKey) o;
        return branchId == that.branchId &&
                Objects.equals(section, that.section);
    }

    @Override
    public int hashCode() {
        return Objects.hash(section, branchId);
    }
}
