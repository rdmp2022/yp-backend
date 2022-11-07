package com.walker.ypbackend.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = -329010018852733051L;

    private String userAccount;

    private String userPassword;


}
