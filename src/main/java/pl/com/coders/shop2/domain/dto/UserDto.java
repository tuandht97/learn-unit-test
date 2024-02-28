package pl.com.coders.shop2.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserDto {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
}
