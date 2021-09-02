package com.foundernest.screener.domain.crud.core;

import com.foundernest.screener.domain.crud.core.model.User;
import com.foundernest.screener.domain.crud.core.ports.incoming.UserIncoming;
import com.foundernest.screener.domain.crud.core.ports.outgoing.UserOutgoing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CrudFacade implements UserIncoming {
    @Autowired
    private UserOutgoing userOutgoing;

    @Override
    public String createUser(User user) {
        return userOutgoing.createUser(user);
    }

    @Override
    public User getUserDetails(String name) {
        return userOutgoing.getUserDetails(name);
    }

    @Override
    public String updateUserDetails(User user) {
        return userOutgoing.updateUserDetails(user);
    }

    @Override
    public String deleteUser(String name) {
        return userOutgoing.deleteUser(name);
    }
}
