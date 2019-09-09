package com.se231.onlineedu.repository;

import java.util.List;
import com.se231.onlineedu.model.Course;
import com.se231.onlineedu.model.StudyRecord;
import com.se231.onlineedu.model.StudyRecordPrimaryKey;
import com.se231.onlineedu.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Zhe Li
 * @date 2019/09/06
 */
public interface StudyRecordRepository extends JpaRepository<StudyRecord, StudyRecordPrimaryKey> {
    List<StudyRecord> findAllByStudyRecordPrimaryKey_UserAndStudyRecordPrimaryKey_Course(User user, Course course);
}
