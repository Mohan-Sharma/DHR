package org.nthdimenzion.security.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.nthdimenzion.ddd.domain.annotations.ValueObject;
import org.nthdimenzion.object.utils.EqualsFacilitator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.dao.SystemWideSaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.util.ObjectUtils;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

@ValueObject
@Embeddable
public class Credentials {

    private String username;
    private String password;
    private PasswordEncoder passwordEncoder;
    private SystemWideSaltSource saltSource;


    Credentials() {
    }

    Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Credentials(Builder builder) {
        this.username = builder.username;
        this.password= builder.password;
        this.passwordEncoder = builder.passwordEncoder;
        this.saltSource = builder.saltSource;
    }

    public String getUsername() {
        return new String(username.intern());
    }

    void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return new String(password);
    }

    void setPassword(String password) {
        this.password = password;
    }

    public void changePassword(String newPassword){
        password = passwordEncoder.encodePassword(newPassword, saltSource.getSystemWideSalt());
    }

    public String getEncodedPassword(String password){
        return passwordEncoder.encodePassword(password, saltSource.getSystemWideSalt());
    }

    @Transient
    public void assignEncoder(PasswordEncoder passwordEncoder,SystemWideSaltSource saltSource) {
        this.saltSource = saltSource;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean equals(Object o) {
        if (EqualsFacilitator.initialChecksPass(o, this)) {
            Credentials obj = (Credentials) o;
            return new EqualsBuilder().reflectionEquals(obj, this);
        }
        return EqualsFacilitator.initialChecksPass(o, this);
    }

    @Override
    public int hashCode() {
        return ObjectUtils.nullSafeHashCode(new Object[]{username, password});
    }

    @Override
    public String toString() {
        return username;
    }

    public static class Builder {
        private String username;
        private String password;
        private PasswordEncoder passwordEncoder;
        private SystemWideSaltSource saltSource;

        Builder(String username, String password) {
            this.username = username;
            this.password = password;
        }

        Builder passwordEncoder(PasswordEncoder passwordEncoder) {
            this.passwordEncoder = passwordEncoder;
            return this;
        }

        Builder saltSource(SystemWideSaltSource saltSource) {
            this.saltSource = saltSource;
            return this;
        }

        Credentials build() {
            password = passwordEncoder.encodePassword(password, saltSource.getSystemWideSalt());
            return new Credentials(this);
        }

    }


}

