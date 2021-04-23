package com.se231.onlineedu.repository;

import java.util.Optional;
import com.se231.onlineedu.model.SectionBranches;
import com.se231.onlineedu.model.SectionBranchesPrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Zhe Li
 * @date 2019/07/19
 */
public interface SectionBranchRepository extends JpaRepository<SectionBranches, SectionBranchesPrimaryKey> {
    /**
     * update section number of a course
     * @param courseId course ID
     * @param secId current section id
     * @param branchNo current branch
     */
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "update section_branches set branch_no= branch_no+1 where section_course_id = ?1 and section_sec_id=?2 and branch_no>?3",nativeQuery = true)
    void updateSecBranchNo(Long courseId,int secId,int branchNo);

    /**
     * get max branch number of a section now
     * @param courseId course ID
     * @param secId id of section
     * @return current section number if exists
     */
    @Query(value = "select max(branch_no) from section_branches where section_course_id = ?1 and section_sec_id=?2",nativeQuery = true)
    Optional<Integer> currentBranchNo(Long courseId,int secId);

    /**
     * get max branch id of a section now
     * @param courseId course ID
     * @param secId number of section
     * @return current section number if exists
     */
    @Query(value = "select max(branch_id) from section_branches where section_course_id = ?1 and section_sec_id=?2",nativeQuery = true)
    Optional<Integer> currentBranchId(Long courseId,int secId);
}
