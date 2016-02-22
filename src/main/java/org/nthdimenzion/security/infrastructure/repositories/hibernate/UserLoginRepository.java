package org.nthdimenzion.security.infrastructure.repositories.hibernate;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.nthdimenzion.ddd.domain.annotations.DomainRepositoryImpl;
import org.nthdimenzion.ddd.infrastructure.hibernate.GenericHibernateRepository;
import org.nthdimenzion.ddd.infrastructure.hibernate.IHibernateDaoOperations;
import org.nthdimenzion.security.domain.IUserLoginRepository;
import org.nthdimenzion.security.domain.UserLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.SystemWideSaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@DomainRepositoryImpl
@Transactional
public class UserLoginRepository extends GenericHibernateRepository<UserLogin, Long> implements IUserLoginRepository {

    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private SystemWideSaltSource saltSource;

    protected UserLoginRepository(){

    }

    @Autowired
    public UserLoginRepository(IHibernateDaoOperations hibernateDaoOperations) {
        super(hibernateDaoOperations);
    }

    @Override
    public UserLogin findUserLoginWithUserName(String username) {
        DetachedCriteria userLoginWithUserNameCriteria = DetachedCriteria.forClass(UserLogin.class);
        userLoginWithUserNameCriteria.add(Restrictions.eq("credentials.username", username));
        if(hibernateDaoOperations.findByCriteria(userLoginWithUserNameCriteria).size()>0) {
            UserLogin userLogin = (UserLogin) hibernateDaoOperations.findByCriteria(userLoginWithUserNameCriteria).get(0);
            userLogin.assignEncoder(passwordEncoder, saltSource);
            return userLogin;
        }
        else
            return null;
    }

    @Override
    public void disableUser(String username) {
        UserLogin userLogin = findUserLoginWithUserName(username);
        hibernateDaoOperations.delete(userLogin);
    }
}
