package com.se231.onlineedu.Repository;


import com.se231.onlineedu.model.User;
import com.se231.onlineedu.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author liu
 * @date 2019/07/17
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void whenFindByCourseId_thenReturnForum() throws Exception {
        User user = new User();
        user.setUsername("haha");
        user.setPassword("hahahha");
        user.setEmail("ajvksl");
        user.setTel(46135L);
        entityManager.persist(user);
        entityManager.flush();

        User found = userRepository.findByUsername("haha").orElseThrow(() -> new Exception("haha"));

        assertThat(found.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    public void existByUsername() {
        User user = new User();
        user.setUsername("haha1");
        user.setPassword("hahahha1");
        user.setEmail("ajvksl1");
        user.setTel(461351L);
        entityManager.persist(user);
        entityManager.flush();

        Boolean flag = userRepository.existsByUsername("haha1");

        assertThat(flag).isEqualTo(true);
    }

    @Test
    public void existByEmail() {
        User user = new User();
        user.setUsername("haha2");
        user.setPassword("hahahha2");
        user.setEmail("ajvks21");
        user.setTel(4611L);
        entityManager.persist(user);
        entityManager.flush();

        Boolean flag = userRepository.existsByEmail("ajvks21");

        assertThat(flag).isEqualTo(true);
    }

    @Test
    public void existByTel() {
        User user = new User();
        user.setUsername("haha3");
        user.setPassword("hahahaha2");
        user.setEmail("ajvks2d1");
        user.setTel(46114L);
        entityManager.persist(user);
        entityManager.flush();
        Boolean flag = userRepository.existsByTel(46114L);
        assertThat(flag).isEqualTo(true);
    }

    @Test
    public void sav(){
        String a = "123\0\r4567893\0\r456cd6";
        String[] strings = a.split("\0\r");
        for(String string: strings){
            System.out.println(string);
        }
    }
}
