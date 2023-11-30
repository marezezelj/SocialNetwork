package com.example.SocialNetwork.controller;

import com.example.SocialNetwork.dto.FriendRequestDTO;
import com.example.SocialNetwork.dto.UserDTO;
import com.example.SocialNetwork.dtos.LoginRequest;
import com.example.SocialNetwork.dtos.LoginResponse;
import com.example.SocialNetwork.dtos.PasswordDto;
import com.example.SocialNetwork.dtos.UserCreateDto;
import com.example.SocialNetwork.entities.FriendRequest;
import com.example.SocialNetwork.entities.Friends;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.repository.UserRepository;
import com.example.SocialNetwork.service.FriendRequestService;
import com.example.SocialNetwork.service.FriendsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.SocialNetwork.service.UserServiceImpl;
import com.example.SocialNetwork.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final UserServiceImpl userService;
    private final JwtUtil jwtUtil;
    private UserRepository userRepository;
    private FriendRequestService friendRequestService;
    private FriendsService friendsService;

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

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserCreateDto userCreateDto) {
        return ResponseEntity.ok(userService.createUser(userCreateDto));
    }

    @PostMapping("/activate-password/{id}")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordDto passwordDto, @PathVariable Long id) {
        userService.resetUserPassword(passwordDto, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        userService.forgotPassword(email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return new ResponseEntity<>("Hello Levi9 konferencijska sala uvek radi", HttpStatus.OK);

    }

    @PostMapping("/friendrequests")
    public ResponseEntity<Object> sendFriendRequest(@RequestBody FriendRequest friendRequest) {
        friendRequest.setId_user1(getCurrentUser().getId());
        return friendRequestService.sendFriendRequest(friendRequest);
    }

    @GetMapping("/friendrequests")
    public ResponseEntity<Object> getAllRequests() {
        Long uid = getUser();
        List<FriendRequestDTO> listOfFriends = friendRequestService.getAllRequests(uid);

        if (listOfFriends.isEmpty()) {
            return ResponseEntity.ok("You have no friend requests");
        } else {
            return ResponseEntity.ok(listOfFriends);
        }
    }

    @PutMapping("/friendrequests/respond")
    public ResponseEntity<Object> respondToRequest(@RequestParam(name = "id") Long id, @RequestParam(name = "status") Long status) {
        return friendRequestService.respondToRequest(id, status);
    }

    @GetMapping("/currentuser")
    public User getCurrentUser() {
        return userService.findCurrentUser();
    }
    @PostMapping("/")
    public ResponseEntity<String> saveUser(@RequestBody User user) {
        userService.saveUser(user);
        return new ResponseEntity<>("User saved", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @GetMapping("/")
    public ResponseEntity<?> showAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @DeleteMapping("/")
    public ResponseEntity<String> deleteUser() {
        Long id = getUser();
        return userService.deleteUserById(id);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        return userService.deleteUserById(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.findByID(id);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/friends")
    public ResponseEntity<?> getFriendsByUser() {
        List<UserDTO> friends = friendsService.getFriendsByUser();
        return new ResponseEntity<>(friends, HttpStatus.OK);
    }

    @GetMapping("/friends/search")
    public ResponseEntity<?> searchFriends(@RequestParam("search") String search) {
        return friendsService.searchFriends(search);
    }

    @DeleteMapping("/friends/admin_delete")
    public ResponseEntity<String> deleteFriendByUser(@RequestParam("user1Id") Long user1Id, @RequestParam("user2Id") Long user2Id) {
        return friendsService.deleteFriendByUser(user1Id, user2Id);
    }

    @DeleteMapping("/friends")
    public ResponseEntity<String> deleteFriend(@RequestParam("friendId") Long friendId) {
        return friendsService.deleteFriend(friendId);
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        invalidateSession(request);

        if(authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(null);
        }

        return new ResponseEntity<>("Logout success!" , HttpStatus.OK);
    }

    private void invalidateSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    @GetMapping("/dto/{id}")
    public UserDTO getUserDTOById(@PathVariable Long id) {
        UserDTO user = userService.findByIDDTO(id);
        return user;
    }

    private Long getUser() {
        Optional<User> user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Long uid = user.get().getId();
        return uid;
    }

}
