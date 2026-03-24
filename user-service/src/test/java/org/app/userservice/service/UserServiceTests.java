package org.app.userservice.service;


import org.app.userservice.dto.UserResponse;
import org.app.userservice.dto.UserUpdateDto;
import org.app.userservice.entity.User;
import org.app.userservice.mapper.UserMapper;
import org.app.userservice.repository.UserRepository;
import org.app.userservice.security.SecurityService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {


    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityService  securityService;


    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImp userService;




    @Test
    public void UserService_GetUserById_ReturnsUserDto(){

        //Arrange
        UUID uuid1 = UUID.randomUUID();
        User user=User.builder()
                .id(uuid1)
                .cin("D000000")
                .email("userTest@gmail.com")
                .firstName("john")
                .lastName("doe")
                .build();

        when(userRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(user));
        when(securityService.canAccessUser((UUID) Mockito.any())).thenReturn(true);

        UserResponse byId = userService.findById(uuid1);

        Assertions.assertThat(byId).isNotNull();


    }


    @Test
    public void UserService_UpdateUser_ReturnUserDto(){

        //Arrange
        UUID uuid1 = UUID.randomUUID();
        User user=User.builder()
                .id(uuid1)
                .cin("D000000")
                .email("userTest@gmail.com")
                .firstName("john")
                .lastName("doe")
                .build();


        UserUpdateDto userUpdateDto=UserUpdateDto.builder()
                .cin("D000000")
                .phone("123456789")
                .address("address")
                .build();

        when(userRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(user));
//        when(userMapper.updateEntity(Mockito.any()).thenReturn(true);
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        UserResponse byId = userService.updateProfile(uuid1,userUpdateDto);

        Assertions.assertThat(byId).isNotNull();


    }


    @Test
    public void UserService_DeleteUser_ReturnsUserIEmpty(){

        //Arrange
        UUID uuid1 = UUID.randomUUID();
        User user=User.builder()
                .id(uuid1)
                .cin("D000000")
                .email("userTest@gmail.com")
                .firstName("john")
                .lastName("doe")
                .build();


        when(userRepository.existsById(Mockito.any())).thenReturn(true);

        assertAll(()->userService.deleteById(uuid1));


    }



}
