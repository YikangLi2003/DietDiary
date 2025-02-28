package com.github.yikangli2003;

import com.github.yikangli2003.database.DataAccessor;
import com.github.yikangli2003.database.entity.*;
import com.github.yikangli2003.database.exception.*;
import com.github.yikangli2003.util.*;

import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        /*DataAccessor.signUpNewUser(
                "giao@email.com",
                PasswordUtil.hashPassword("12345"),
                "Giao Giao",
                LocalDateTime.now());

         */


        //User user = DataAccessor.getUserByAccount("giao@email.com");
        //System.out.println(user.getId());
        //System.out.println(user.getHashedPassword());
        //System.out.println(user.getName());

        /*user.setHashedPassword(PasswordUtil.hashPassword("4151123"));
        user.setName("Gee Gee");
        DataAccessor.saveUserPropertyChange(user);*/

        User user = new User("giao@email.com",
                PasswordUtil.hashPassword("12345"),
                "Giao Giao",
                LocalDateTime.now());

        DataAccessor.saveUserPropertyChange(user);

        DataAccessor.closeEntityManagerFactory();
    }
}