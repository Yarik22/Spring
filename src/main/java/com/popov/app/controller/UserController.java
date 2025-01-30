package com.popov.app.controller;

import com.popov.app.dto.auth.LoginRequestDTO;
import com.popov.app.dto.user.UserCreateDTO;
import com.popov.app.dto.user.UserResponseDTO;
import com.popov.app.dto.user.UserUpdateDTO;
import com.popov.app.model.User;
import com.popov.app.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponseDTO> userDtos = users.stream()
                .map(this::convertToUserResponseDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(u -> new ResponseEntity<>(convertToUserResponseDTO(u), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserCreateDTO userCreateDTO) {
        User user = convertToUser(userCreateDTO);
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(convertToUserResponseDTO(createdUser), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable UUID id,
            @RequestBody UserUpdateDTO userUpdateDTO) {
        User user = convertToUser(userUpdateDTO);
        user.setId(id);
        User updatedUser = userService.updateUser(id, user);
        return updatedUser != null ?
                new ResponseEntity<>(convertToUserResponseDTO(updatedUser), HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        return userService.deleteUser(id) ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PostMapping("/me")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@RequestBody LoginRequestDTO loginRequest) {
        Optional<User> userOptional = userService.findByEmail(loginRequest.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(loginRequest.getPassword())) {
                UserResponseDTO userResponseDTO = convertToUserResponseDTO(user);
                return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private User convertToUser(UserCreateDTO userCreateDTO) {
        User user = new User();
        user.setName(userCreateDTO.getName());
        user.setEmail(userCreateDTO.getEmail());
        user.setPassword(userCreateDTO.getPassword());
        return user;
    }

    private User convertToUser(UserUpdateDTO userUpdateDTO) {
        User user = new User();
        user.setName(userUpdateDTO.getName());
        user.setEmail(userUpdateDTO.getEmail());
        user.setPassword(userUpdateDTO.getPassword());
        return user;
    }

    private UserResponseDTO convertToUserResponseDTO(User user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setName(user.getName());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setCreatedAt(user.getCreatedAt());
        return userResponseDTO;
    }
}
