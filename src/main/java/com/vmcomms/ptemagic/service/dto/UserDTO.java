package com.vmcomms.ptemagic.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.vmcomms.ptemagic.config.Constants;

import com.vmcomms.ptemagic.domain.Authority;
import com.vmcomms.ptemagic.domain.User;
import com.vmcomms.ptemagic.domain.enumeration.UserType;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.validation.constraints.*;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO representing a user, with his authorities.
 */
@JsonInclude(Include.NON_EMPTY)
public class UserDTO {

    private Long id;

    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login;

    @Email
    @Size(min = 5, max = 100)
    private String email;

    private boolean activated = false;

    @JsonIgnore
    @Size(min = 2, max = 6)
    private String langKey;

    @JsonIgnore
    private String createdBy;

    private Instant createdDate;

//    @JsonIgnore
    private String lastModifiedBy;
    
    private String fullName;
    
    private String phonenumber;

//    @JsonIgnore
    private Instant lastModifiedDate;

    private Set<String> authorities;
    
    private Integer remainDays;
    
    public UserDTO() {
        // Empty constructor needed for Jackson.
    }

    public UserDTO(User user) {
        this(user.getId(), user.getLogin(), 
            user.getEmail(), user.getActivated(), user.getLangKey(),
            user.getCreatedBy(), user.getCreatedDate(), user.getLastModifiedBy(), user.getLastModifiedDate(),
            user.getAuthorities().stream().map(Authority::getName)
                .collect(Collectors.toSet()), user.getFullName(), user.getPhonenumber(), user.getRemainDays());
    }

    public UserDTO(Long id, String login, 
        String email, boolean activated, String langKey,
        String createdBy, Instant createdDate, String lastModifiedBy, Instant lastModifiedDate,
        Set<String> authorities, String fullName, String phonenumber, Integer remainDays) {

        this.id = id;
        this.login = login;
        this.email = email;
        this.activated = activated;
        this.langKey = langKey;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDate = lastModifiedDate;
        this.authorities = authorities;
        this.fullName = fullName;
        this.phonenumber = phonenumber;
        this.remainDays = remainDays;
    }
    
//    public UserType getUserType() {
//		return userType;
//	}
//
//	public void setUserType(UserType userType) {
//		this.userType = userType;
//	}

	public Integer getRemainDays() {
		return remainDays;
	}

	public void setRemainDays(Integer remainDays) {
		this.remainDays = remainDays;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public boolean isActivated() {
        return activated;
    }

    public String getLangKey() {
        return langKey;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
            "login='" + login + '\'' +
            ", email='" + email + '\'' +
            ", activated=" + activated +
            ", langKey='" + langKey + '\'' +
            ", createdBy=" + createdBy +
            ", createdDate=" + createdDate +
            ", lastModifiedBy='" + lastModifiedBy + '\'' +
            ", lastModifiedDate=" + lastModifiedDate +
            ", authorities=" + authorities +
            "}";
    }
}
