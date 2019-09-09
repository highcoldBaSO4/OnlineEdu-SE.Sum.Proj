package com.se231.onlineedu.repository;

import com.se231.onlineedu.model.LearnPrimaryKey;
import com.se231.onlineedu.model.StudyTempRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Zhe Li
 * @date 2019/09/06
 */
public interface StudyTempRecordRepository extends JpaRepository<StudyTempRecord, LearnPrimaryKey> {
}
