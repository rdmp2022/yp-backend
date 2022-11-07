package com.walker.ypbackend.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 3949323957522581064L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;
}
