package com.vmcomms.ptemagic.web.rest.vm;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserVM {

	private Long id;

    private String fullName;

    private String email;

    private boolean activated = false;

    private String password;

    private String phonenumber;
}
