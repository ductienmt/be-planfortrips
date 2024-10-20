package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.response.AuthResponse;

import java.io.IOException;

public interface IAuth2Service {

    String getGoogleUrl();

    AuthResponse loginGoogle(String code) throws IOException;
}
