package pl.com.coders.shop2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pl.com.coders.shop2.domain.User;
import pl.com.coders.shop2.domain.dto.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto userToDto(User user);
    User dtoToUser(UserDto userDto);


}
