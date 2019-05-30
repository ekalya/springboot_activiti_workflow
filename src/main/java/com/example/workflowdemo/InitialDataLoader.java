/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.workflowdemo;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 *
 * @author exk
 */
@Component
public class InitialDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private UserDetailsRepository userRepository;

    @Autowired
    private UserRolesRepository roleRepository;

    @Autowired
    private GrantedAuthoritiesRepository privilegeRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup) {
            return;
        }
        GrantedAuthorityImpl readPrivilege
                = createPrivilegeIfNotFound("READ_PRIVILEGE");
        GrantedAuthorityImpl writePrivilege
                = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        List<GrantedAuthorityImpl> adminPrivileges = Arrays.asList(
                readPrivilege, writePrivilege);
        createRoleIfNotFound("ADMIN", adminPrivileges);
        createRoleIfNotFound("USER", Arrays.asList(readPrivilege));

        UserRole adminRole = roleRepository.findByName("ADMIN");
        UserDetailsImpl user = new UserDetailsImpl();
        user.setUsername("admin");
        user.setPassword(passwordencoder().encode("test"));
        user.setUserRoles(Arrays.asList(adminRole));
        user.setEnabled(true);
        userRepository.save(user);

        alreadySetup = true;
    }

    @Transactional
    private GrantedAuthorityImpl createPrivilegeIfNotFound(String name) {

        GrantedAuthorityImpl privilege = privilegeRepository.findByAuthority(name);
        if (privilege == null) {
            privilege = new GrantedAuthorityImpl(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    private UserRole createRoleIfNotFound(
            String name, Collection<GrantedAuthorityImpl> privileges) {

        UserRole role = roleRepository.findByName(name);
        if (role == null) {
            role = new UserRole(name);
            role.setAuthorities(privileges);
            roleRepository.save(role);
        }
        return role;
    }

    private PasswordEncoder passwordencoder() {
        return new BCryptPasswordEncoder();
    }
}
