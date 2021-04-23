package com.se231.onlineedu.repository;

import com.se231.onlineedu.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author liu
 */
public interface ResourceRepository extends JpaRepository<Resource,Long> {
}
