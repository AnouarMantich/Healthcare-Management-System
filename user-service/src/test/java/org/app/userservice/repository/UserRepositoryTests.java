package org.app.userservice.repository;


import lombok.AllArgsConstructor;
import org.app.userservice.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;


    private UUID uuid ;
    private User user;

    private UUID uuid1 ;
    private User user1;

    private UUID uuid2 ;
    private User user2;


    @BeforeEach
    public void init(){
        uuid = UUID.randomUUID();
        user=User.builder()
                .id(uuid)
                .cin("D000000")
                .email("userTest@gmail.com")
                .firstName("john")
                .lastName("doe")
                .build();


         uuid = UUID.randomUUID();
         user=User.builder()
                .id(uuid)
                .cin("D000000")
                .email("userTest@gmail.com")
                .firstName("john")
                .lastName("doe")
                .build();


         uuid2 = UUID.randomUUID();
         user2=User.builder()
                .id(uuid2)
                .cin("D000002")
                .email("user2Test@gmail.com")
                .firstName("jane")
                .lastName("doe")
                .build();

    }

    @Test
    public void UserRepository_SaveAll_ReturnSavedUser(){

        User savedUser = userRepository.save(user);

        //Assert

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isEqualTo(uuid);

    }

    @Test
    public void UserRepository_GetAll_ReturnMoreThanOneUser(){



        //Act

        User savedUser = userRepository.save(user);
        User savedUser2 = userRepository.save(user2);


        List<User> all = userRepository.findAll();

        //Assert

        Assertions.assertThat(all).isNotNull();
        Assertions.assertThat(all.size()).isEqualTo(2);

    }


    @Test
    public void UserRepository_FindById_ReturnPokemon(){



        //Act

        User savedUser = userRepository.save(user);
        User byId = userRepository.findById(savedUser.getId()).get();

        //Assert

        Assertions.assertThat(byId).isNotNull();
        Assertions.assertThat(byId.getId()).isEqualTo(uuid);

    }


    @Test
    public void UserRepository_UpdateUser_ReturnUpdatedUser(){

        //Arrange
        UUID uuid = UUID.randomUUID();
        User user=User.builder()
                .id(uuid)
                .cin("D000000")
                .email("userTest@gmail.com")
                .firstName("john")
                .lastName("doe")
                .build();

        //Act

        User savedUser = userRepository.save(user);
        savedUser.setCin("D111111");
        savedUser.setFirstName("mark");
        User updated = userRepository.save(savedUser);

        //Assert

        Assertions.assertThat(updated).isNotNull();
        Assertions.assertThat(savedUser.getId()).isEqualTo(uuid);
        Assertions.assertThat(savedUser.getCin()).isEqualTo("D111111");
        Assertions.assertThat(savedUser.getFirstName()).isEqualTo("mark");

    }


    @Test
    public void UserRepository_DeleteUser_ReturnUserIsEmpty(){

        //Arrange
        UUID uuid = UUID.randomUUID();
        User user=User.builder()
                .id(uuid)
                .cin("D000000")
                .email("userTest@gmail.com")
                .firstName("john")
                .lastName("doe")
                .build();

        //Act

        User savedUser = userRepository.save(user);
        userRepository.delete(savedUser);
        Optional<User> byId = userRepository.findById(savedUser.getId());

        //Assert

        Assertions.assertThat(byId).isEmpty();

    }



}
