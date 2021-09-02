package com.foundernest.screener.domain.crud.core.ports.outgoing;

import com.foundernest.screener.domain.crud.core.model.User;

public interface UserOutgoing {
    String createUser(User user);
    User getUserDetails(String name);
    String updateUserDetails(User user);
    String deleteUser(String name);
}
