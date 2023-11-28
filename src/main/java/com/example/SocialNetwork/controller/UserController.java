package com.example.SocialNetwork.controller;

import com.example.SocialNetwork.dtos.LoginRequest;
import com.example.SocialNetwork.dtos.LoginResponse;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.service.UserService;
import com.example.SocialNetwork.service.UserServiceImpl;
import com.example.SocialNetwork.utils.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final UserServiceImpl userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(new LoginResponse(jwtUtil.generateToken(userService.findUserByEmail(loginRequest.getEmail()))));
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello Levi9 konferencijska sala uvek radi";
    }

    @PostMapping("/")
    public String saveUser(@RequestBody User user) {
        userService.saveUser(user);
        return "Bravo";
    }

    @PutMapping("/{id}")
    public String updateUser(@PathVariable Long id, @RequestBody User user) {
        userService.updateUser(id, user);
        return "Bravo";
    }

    // NECE RADITI DOK SE NE SREDI SOCIALGROUP I NE POPUNI BAREM 1 RED U GROUPMEMBER TABELI
    @GetMapping("/")
    public List<User> showAllUsers() {
        return userService.getAllUsers();
    }

    // NECE RADITI DOK SE NE SREDI SOCIALGROUP I NE POPUNI BAREM 1 RED U GROUPMEMBER TABELI
    @DeleteMapping("/{id}")
    public String deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);

        return "Bravo";
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        User user = userService.findByID(id);
        return user;
    }
}
