package com.thoughtworks.bbs.service.impl;

import com.thoughtworks.bbs.mappers.UserMapper;
import com.thoughtworks.bbs.mappers.UserRoleMapper;
import com.thoughtworks.bbs.model.User;
import com.thoughtworks.bbs.model.UserRole;
import com.thoughtworks.bbs.model.UserValidator;
import com.thoughtworks.bbs.service.ServiceResult;
import com.thoughtworks.bbs.service.UserService;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {
    private UserValidator validator;
    private SqlSessionFactory factory;

    public UserServiceImpl(SqlSessionFactory factory) {
        this.factory = factory;
        validator = new UserValidator();
    }

    @Override
    public User get(long id) {
        return null;
    }

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public ServiceResult<User> save(User user) {
        Map<String, String> errors = validator.validate(user);
        SqlSession session = factory.openSession();

        if(errors.isEmpty()) {
            try{
                UserMapper userMapper = session.getMapper(UserMapper.class);
                UserRoleMapper userRoleMapper = session.getMapper(UserRoleMapper.class);

                userMapper.insert(user);

                UserRole userRole = new UserRole();
                userRole.setUserId(user.getId());
                userRole.setRoleName("ROLE_REGULAR");

                userRoleMapper.insert(userRole);

                session.commit();
            } finally {
                session.close();
            }
        }

        return new ServiceResult<User>(errors, user);
    }

    @Override
    public ServiceResult<User> update(User user) {
        Map<String, String> errors = validator.validate(user);
        SqlSession session = factory.openSession();

        if(errors.isEmpty()) {
            try{
                UserMapper userMapper = session.getMapper(UserMapper.class);

                userMapper.update(user);

                session.commit();
            } finally {
                session.close();
            }
        }

        return new ServiceResult<User>(errors, user);
    }

    @Override
    public String changePassword(User user, String oldPassword, String newPassword, String confirmPassword){
        String result = new String();
        if(null != validator.passwordValidate(newPassword)){
            result = "failed";
        }
        else if(user.getPasswordHash().equals(oldPassword) && newPassword.equals(confirmPassword))
        {
            user.setPasswordHash(newPassword);
            update(user);
            result = "success";
        }
        else
        {   result = "failed" ;
        }

        return result;
    }

    @Override
    public void delete(User user) {

    }

    @Override
    public User getByUsername(String username) {
        SqlSession session = factory.openSession();
        User user = null;

        try{
            UserMapper mapper = session.getMapper(UserMapper.class);
            user = mapper.findByUsername(username);
        } finally {
            session.close();
        }

        return user;
    }



    public boolean verify(User user, String oldPassWord) {
        //To change body of created methods use File | Settings | File Templates.
        return false;
    }

}
