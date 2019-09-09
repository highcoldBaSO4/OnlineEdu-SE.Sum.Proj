package com.se231.onlineedu.Repository;


import com.se231.onlineedu.model.Role;
import com.se231.onlineedu.model.RoleType;
import com.se231.onlineedu.repository.RoleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void findByRole() throws Exception {
        Role role = new Role();
        role.setRole(RoleType.ROLE_USER);
        roleRepository.save(role);
        Role found = roleRepository.findByRole(RoleType.ROLE_USER).orElseThrow(() -> new Exception("jaja"));

        assertThat(found.getRole()).isEqualTo(RoleType.ROLE_USER);
    }

}
