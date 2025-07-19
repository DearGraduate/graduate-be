package com.example.graduate.global.apiPayload.exception.handler;

import com.example.graduate.global.apiPayload.code.BaseErrorCode;
import com.example.graduate.global.apiPayload.exception.GeneralException;

public class UserHandler extends GeneralException {

  public UserHandler(BaseErrorCode errorCode) {
    super(errorCode);
  }
}
