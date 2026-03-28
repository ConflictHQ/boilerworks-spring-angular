package com.boilerworks.api.dto;

import com.boilerworks.api.model.AppUser;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

  private UUID id;
  private String email;
  private String firstName;
  private String lastName;
  private boolean active;
  private boolean staff;
  private List<String> groups;
  private List<String> permissions;

  public static UserDto from(AppUser user) {
    UserDto dto = new UserDto();
    dto.setId(user.getId());
    dto.setEmail(user.getEmail());
    dto.setFirstName(user.getFirstName());
    dto.setLastName(user.getLastName());
    dto.setActive(user.isActive());
    dto.setStaff(user.isStaff());
    dto.setGroups(user.getGroups().stream().map(g -> g.getName()).toList());
    dto.setPermissions(
        user.getGroups().stream()
            .flatMap(g -> g.getPermissions().stream())
            .map(p -> p.getCodename())
            .distinct()
            .toList());
    return dto;
  }
}
