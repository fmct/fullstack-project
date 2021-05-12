package application.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Collection;

public class UserDto {

    private final String username;
    private final Long id;

    //need default constructor for JSON Parsing
    public UserDto(String username, Long id)
    {
        this.username = username;
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

}
