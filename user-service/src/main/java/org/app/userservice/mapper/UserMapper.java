package org.app.userservice.mapper;

import org.app.userservice.dto.UserResponse;
import org.app.userservice.dto.UserUpdateDto;
import org.app.userservice.entity.User;
import org.springframework.beans.BeanUtils;

public final class UserMapper {

    public static UserResponse toResponse(User u) {
        if (u == null) return null;
        UserResponse response = new UserResponse();
        BeanUtils.copyProperties(u,response);
        return response;

    }

    public static void updateEntity(User user, UserUpdateDto dto) {
        if (dto == null || user == null) return;

        if(dto.getAddress()!=null ){
            user.setAddress(dto.getAddress());
        }
        if (dto.getCin()!=null ){
            user.setCin(dto.getCin());
        }

        if (dto.getPhone()!=null ){
            user.setPhoneNumber(dto.getPhone());
        }

        if (dto.getAddress()!=null  && dto.getCin()!=null && dto.getPhone()!=null){
            user.setProfileCompleted(true);
        }

    }



}