package storemanagementtool.store.dto;

import lombok.*;
import storemanagementtool.store.model.Role;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String username;
    private String password;
    private Role role;
}