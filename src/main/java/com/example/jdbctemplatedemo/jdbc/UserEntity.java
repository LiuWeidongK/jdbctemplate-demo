package com.example.jdbctemplatedemo.jdbc;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author lwd
 * Created on 2023/10/17
 */
@Getter
@SuperBuilder
@NoArgsConstructor
public class UserEntity implements JDBCTemplateEntity {

    @ID
    private long id;

    private String userName;
    private Integer age;
    // ...
}
