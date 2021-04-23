package com.se231.onlineedu.repository;

import com.se231.onlineedu.model.Notice;
import com.se231.onlineedu.model.NoticePrimaryKey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * @author yuxuanLiu
 * @date 2019/07/26
 */
public interface NoticeRepository extends CrudRepository<Notice, NoticePrimaryKey> {
    /**
     * get max notice number of a course now
     * @param courseId course ID
     * @return current section number if exists
     */
    @Query(value = "select max(notice_no) from notice where course_id = ?1",nativeQuery = true)
    Optional<Integer> currentNoticeNo(Long courseId);
}
