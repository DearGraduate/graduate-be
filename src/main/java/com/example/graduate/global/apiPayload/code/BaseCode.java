package com.example.graduate.global.apiPayload.code;

import com.example.graduate.global.apiPayload.dto.ReasonDTO;

public interface BaseCode {
    ReasonDTO getReason();

    ReasonDTO getReasonHttpStatus();
}
