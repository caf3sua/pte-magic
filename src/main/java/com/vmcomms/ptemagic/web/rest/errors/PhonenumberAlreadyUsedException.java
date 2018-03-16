package com.vmcomms.ptemagic.web.rest.errors;

public class PhonenumberAlreadyUsedException extends BadRequestAlertException {

    public PhonenumberAlreadyUsedException() {
        super(ErrorConstants.PHONENUMBER_ALREADY_USED_TYPE, "Phonenumber already in use", "userManagement", "phonenumberexists");
    }
}
