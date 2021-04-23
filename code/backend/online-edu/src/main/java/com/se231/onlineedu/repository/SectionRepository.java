package com.se231.onlineedu.repository;

import java.util.List;
import java.util.Optional;
import com.se231.onlineedu.model.Section;
import com.se231.onlineedu.model.SectionPrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


/**
 * SectionRepository Repository Interface
 *
 * used to operate with database
 *
 * @author Zhe Li
 *
 * @date 2019/07/08
 */
public interface SectionRepository extends JpaRepository<Section, SectionPrimaryKey> {

    /**
     * get max section number of a course now
     * @param courseId course ID
     * @return current section number if exists
     */
    @Query(value = "select max(sec_no) from section where course_id = ?1",nativeQuery = true)
    Optional<Integer> currentSecNo(Long courseId);

    /**
     * get max section id of a course now
     * @param courseId course ID
     * @return current section number if exists
     */
    @Query(value = "select max(sec_id) from section where course_id = ?1",nativeQuery = true)
    Optional<Integer> currentSecId(Long courseId);

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "update section set sec_no = sec_no + 1 where course_id = ?1 and sec_no > ?2",nativeQuery = true)
    void updateSecNo(Long courseId,int secNo);
}
