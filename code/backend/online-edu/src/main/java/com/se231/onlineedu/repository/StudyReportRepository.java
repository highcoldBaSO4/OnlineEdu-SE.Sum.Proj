package com.se231.onlineedu.repository;

import com.se231.onlineedu.model.LearnPrimaryKey;
import com.se231.onlineedu.model.StudyReport;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Zhe Li
 * @date 2019/09/07
 */
public interface StudyReportRepository extends JpaRepository<StudyReport, LearnPrimaryKey> {
}
