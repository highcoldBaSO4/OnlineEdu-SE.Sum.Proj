package com.se231.onlineedu.Repository;

import com.se231.onlineedu.model.*;
import com.se231.onlineedu.repository.ApplyRepository;
import com.se231.onlineedu.repository.CoursePrototypeRepository;
import com.se231.onlineedu.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class ApplyRepositoryTest {
    @Autowired
    private ApplyRepository applyRepository;

    @Autowired
    private CoursePrototypeRepository coursePrototypeRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findAppliesByPrototypeId(){
        CoursePrototype coursePrototype = new CoursePrototype();
        coursePrototype.setId(1L);
        coursePrototype.setTitle("ads");
        coursePrototype.setState(CoursePrototypeState.USING);
        coursePrototypeRepository.save(coursePrototype);

        User user = new User();
        user.setId(1L);
        user.setEmail("asdf");
        user.setUsername("ghjhk");
        user.setTel(631L);
        user.setPassword("asdfg");
        userRepository.save(user);


        Apply apply = new Apply();
        ApplyPrimaryKey applyPrimaryKey = new ApplyPrimaryKey();
        applyPrimaryKey.setCoursePrototype(coursePrototype);
        applyPrimaryKey.setTeachingAdmin(user);
        apply.setApplicationForCoursePK(applyPrimaryKey);
        apply.setApplyState(ApplyState.APPROVAL);

        applyRepository.save(apply);

        List<Apply> founds = applyRepository.findAppliesByPrototypeId(1L);
        assertThat(founds.size()).isEqualTo(1);

    }
}
