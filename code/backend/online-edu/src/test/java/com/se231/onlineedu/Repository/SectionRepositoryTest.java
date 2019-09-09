package com.se231.onlineedu.Repository;

import com.se231.onlineedu.BaseTest;
import com.se231.onlineedu.model.Section;
import com.se231.onlineedu.repository.SectionBranchRepository;
import com.se231.onlineedu.repository.SectionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Zhe Li
 * @date 2019/07/22
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class SectionRepositoryTest extends BaseTest {
    @Autowired
    SectionRepository sectionRepository;

    @Autowired
    SectionBranchRepository sectionBranchRepository;

    @Test
    public void getMaxNumberTest(){
        int currentSecNo = sectionRepository.currentSecNo(course.getId()).orElseThrow();
        int currentSecId = sectionRepository.currentSecId(course.getId()).orElseThrow();
        assertThat(currentSecId).isEqualTo(1);
        assertThat(currentSecNo).isEqualTo(1);

        int currentBranchNo = sectionBranchRepository.currentBranchNo(course.getId(),section.getSectionPrimaryKey().getSecId()).orElseThrow();
        int currentBranchId = sectionBranchRepository.currentBranchId(course.getId(),section.getSectionPrimaryKey().getSecId()).orElseThrow();
        assertThat(currentBranchId).isEqualTo(1);
        assertThat(currentBranchNo).isEqualTo(1);

        Section section2 = createSection("sec_2",course,2,2);
        createSectionBranches(2,"branch_2",2,section2);
        int currentSecNo2 = sectionRepository.currentSecNo(course.getId()).orElseThrow();
        int currentSecId2 = sectionRepository.currentSecId(course.getId()).orElseThrow();
        int currentBranchNo2 = sectionBranchRepository.currentBranchNo(course.getId(),section2.getSectionPrimaryKey().getSecId()).orElseThrow();
        int currentBranchId2 = sectionBranchRepository.currentBranchId(course.getId(),section2.getSectionPrimaryKey().getSecId()).orElseThrow();
        assertThat(currentSecId2).isEqualTo(2);
        assertThat(currentSecNo2).isEqualTo(2);
        assertThat(currentBranchId2).isEqualTo(2);
        assertThat(currentBranchNo2).isEqualTo(2);
    }

    @Test
    public void updateTest(){
        createSection("sec_2",course,2,2);
        createSection("sec_3",course,3,3);
        int currentSecNo1 = sectionRepository.currentSecNo(course.getId()).orElseThrow();
        sectionRepository.updateSecNo(course.getId(),2);
        int currentSecNo2 =sectionRepository.currentSecNo(course.getId()).orElseThrow();
        assertThat(currentSecNo1).isEqualTo(3);
        assertThat(currentSecNo2).isEqualTo(4);

        createSectionBranches(2,"branch_2",2,section);
        createSectionBranches(3,"branch_3",3,section);
        int currentBranchNo1 = sectionBranchRepository.currentBranchNo(course.getId(),section.getSectionPrimaryKey().getSecId()).orElseThrow();
        sectionBranchRepository.updateSecBranchNo(course.getId(),section.getSectionPrimaryKey().getSecId(),2);
        int currentBranchNo2 =sectionBranchRepository.currentBranchNo(course.getId(),section.getSectionPrimaryKey().getSecId()).orElseThrow();
        assertThat(currentBranchNo1).isEqualTo(3);
        assertThat(currentBranchNo2).isEqualTo(4);
    }
}