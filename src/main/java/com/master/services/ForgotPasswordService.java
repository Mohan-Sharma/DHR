package com.master.services;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.security.authentication.dao.SystemWideSaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

/**
 * Created by Administrator on 2/23/2015.
 */
@Service
public class ForgotPasswordService {
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private PasswordEncoder passwordEncoder;
    private SystemWideSaltSource saltSource;
    private static final String QUERY_TO_UPDATE_PASSWORD = "UPDATE `user_login` u SET u.password=:password, u.password_reset_pending='1' WHERE u.username = :userName ";

    @Autowired
    public ForgotPasswordService(DataSource dataSource, PasswordEncoder passwordEncoder, SystemWideSaltSource saltSource){
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.passwordEncoder = passwordEncoder;
        this.saltSource = saltSource;
    }

    public boolean changePasswordAfterSecurityVerified(String userName, String password){
        boolean result = false;
        String encodedPassword = encodePassword(password);
        SqlParameterSource parameterSource = new MapSqlParameterSource("userName",userName).addValue("password",encodedPassword);
        int success = namedParameterJdbcTemplate.update(QUERY_TO_UPDATE_PASSWORD, parameterSource);
        if(success>0){
            result = true;
        }
        return result;
    }

    private String encodePassword(String password){
        Preconditions.checkNotNull(password);
        return passwordEncoder.encodePassword(password, saltSource.getSystemWideSalt());
    }
}
