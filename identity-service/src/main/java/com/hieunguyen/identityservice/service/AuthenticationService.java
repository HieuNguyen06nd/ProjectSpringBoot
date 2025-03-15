package com.hieunguyen.identityservice.service;

import com.hieunguyen.identityservice.dto.request.AuthenticationRequest;
import com.hieunguyen.identityservice.dto.request.IntrospectRequest;
import com.hieunguyen.identityservice.dto.response.AuthenticationResponse;
import com.hieunguyen.identityservice.dto.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AuthenticationService {

    AuthenticationResponse authenticate(AuthenticationRequest request);

    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;
}
