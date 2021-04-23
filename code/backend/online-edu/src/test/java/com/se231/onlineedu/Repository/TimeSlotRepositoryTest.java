package com.se231.onlineedu.Repository;

import com.se231.onlineedu.model.TimeSlot;
import com.se231.onlineedu.model.WeekDay;
import com.se231.onlineedu.repository.TimeSlotRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Time;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author liu
 * @date 2019/07/17
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class TimeSlotRepositoryTest {
    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Test
    public void findByDateAndStartAndEnd() throws Exception {
        Time time = Time.valueOf("08:00:00");
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setDay(WeekDay.FRIDAY);
        timeSlot.setEnd(time);
        timeSlot.setStart(time);
        TimeSlot found = timeSlotRepository.save(timeSlot);
        TimeSlot found1 = timeSlotRepository.findByDayAndStartAndEnd(WeekDay.FRIDAY, time, time).orElseThrow(()->new Exception("hah"));
        assertThat(found1.getDay()).isEqualTo(WeekDay.FRIDAY);
    }
}
