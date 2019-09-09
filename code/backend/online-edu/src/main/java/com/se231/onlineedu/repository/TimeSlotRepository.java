package com.se231.onlineedu.repository;

import java.sql.Time;
import java.util.Optional;
import com.se231.onlineedu.model.TimeSlot;
import com.se231.onlineedu.model.WeekDay;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *Time Slot Repository Interface
 *
 * operate with database
 *
 * @author Zhe Li
 * @date 2019/07/15
 */
public interface TimeSlotRepository extends JpaRepository<TimeSlot,Long> {
    /**
     * check if the time slot has existed
     * @param day   week day
     * @param start start time
     * @param end   end time
     * @return  optional time slot
     */
    Optional<TimeSlot> findByDayAndStartAndEnd(WeekDay day,Time start,Time end);
}
