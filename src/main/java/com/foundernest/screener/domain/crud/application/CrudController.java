package com.foundernest.screener.domain.crud.application;

import com.foundernest.screener.domain.crud.core.model.User;
import com.foundernest.screener.domain.crud.core.ports.incoming.UserIncoming;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CrudController {
    @Autowired
    private UserIncoming userIncoming;

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        String resp = userIncoming.createUser(user);
        if (resp == null) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Unable to save user: " + user.getName());
        }
        return ResponseEntity.status(HttpStatus.OK).body(userIncoming.createUser(user));
    }

    @GetMapping("/getUser")
    public ResponseEntity<?> getUser(@RequestParam String name) {
        User resp = userIncoming.getUserDetails(name);
        if (resp == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(name + " not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @PutMapping("/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        String resp = userIncoming.updateUserDetails(user);
        if (resp == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to update " + user.getName());
        }
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestParam String name) {
        return ResponseEntity.status(HttpStatus.OK).body(userIncoming.deleteUser(name));
    }
}
