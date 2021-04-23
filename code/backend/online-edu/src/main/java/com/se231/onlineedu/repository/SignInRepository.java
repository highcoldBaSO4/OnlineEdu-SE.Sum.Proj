package com.se231.onlineedu.repository;

import com.se231.onlineedu.model.SignIn;
import com.se231.onlineedu.model.SignInPrimaryKey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author liu
 */
public interface SignInRepository extends CrudRepository<SignIn, SignInPrimaryKey> {
    /**
     * get max section id of a course now
     * @param courseId course ID
     * @return current section number if exists
     */
    @Query(value = "select max(sign_in_no) from sign_in where course_id = ?1",nativeQuery = true)
    Optional<Integer> currentSignInNo(Long courseId);

    List<SignIn> findBySignInPrimaryKey_Course_Id(Long courseId);
}
