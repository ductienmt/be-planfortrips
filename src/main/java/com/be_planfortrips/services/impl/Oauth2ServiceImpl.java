package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.OauthModel.TypeLogin;
import com.be_planfortrips.dto.OauthModel.UserGoogleInfo;
import com.be_planfortrips.dto.response.AuthResponse;
import com.be_planfortrips.entity.Image;
import com.be_planfortrips.entity.Role;
import com.be_planfortrips.entity.User;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.repositories.RoleRepository;
import com.be_planfortrips.repositories.UserRepository;
import com.be_planfortrips.security.jwt.JwtProvider;
import com.be_planfortrips.services.interfaces.IAuth2Service;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.*;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class Oauth2ServiceImpl implements IAuth2Service {
    private final RoleRepository roleRepository;
    UserRepository userRepository;

    @NonFinal
    @Value("${google.clientId}")
    String clientId;

    @NonFinal
    @Value("${google.clientSecret}")
    String clientSecret;

    WebClient webClient;

    JwtProvider jwtProvider;

    @Override
    public String getGoogleUrl() {
        String url = new GoogleAuthorizationCodeRequestUrl(
                clientId,
                "http://localhost:5050/login",
                Arrays.asList("email","profile","openid")).build();
        return url;
    }

    @Override
    public AuthResponse loginGoogle(String code) throws IOException {
        String accessToken = new GoogleAuthorizationCodeTokenRequest(
                new NetHttpTransport(),
                new GsonFactory(),
                clientId,
                clientSecret,
                code,
                "http://localhost:5050/login"
        ).execute().getAccessToken();

        try {
            UserGoogleInfo userGoogleInfo = webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/oauth2/v3/userinfo").queryParam("access_token", accessToken)
                            .build())
                    .retrieve()
                    .bodyToMono(UserGoogleInfo.class).block();
            System.out.println(userGoogleInfo);
            assert userGoogleInfo != null;
            return getUserGoogle(userGoogleInfo);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Gap loi");
            return null;
        }
    }

    private AuthResponse getUserGoogle(UserGoogleInfo userGoogleInfo) {
        // Kiểm tra UserGoogleInfor có tồn tại hay chưa
        Optional<User> user = userRepository.findUSerByGoogleId(userGoogleInfo.sub());
        if (user.isPresent()) {
            // Nếu User tồn tại
            User userExist = user.get();
            String token = jwtProvider.createToken(userExist.getUserName(), userExist.getRole().getName(), TypeLogin.LOGIN_GOOGLE);
            return AuthResponse.builder()
                    .userName(userExist.getUserName())
                    .token(token)
                    .role(userExist.getRole().getName())
                    .build();
        }
        // Neu User (GoogleID) chua ton tai
        Role role = roleRepository.findByName("ROLE_USER").orElseThrow(
                () -> new AppException(ErrorType.roleNameNotFound)
        );
//        Image mediaFile = Image.
//                .(UUID.randomUUID().toString())
//                .mediaFilePath(userGoogleInfo.picture())
//                .build();
//        UserProfile userProfile = UserProfile.builder()
//                .profileEmail(userGoogleInfo.email())
//                .profileFullName(userGoogleInfo.name())
//                .mediaFile(mediaFile)
//                .build();
        User userGoogle = User.builder()
                .role(role)
                .fullName(userGoogleInfo.name())
                .googleId(userGoogleInfo.sub())
                .build();

        // Sau đó, cập nhật đối tượng user cho userProfile nếu cần thiết
        userRepository.save(userGoogle);
        String token = jwtProvider.createToken(userGoogle.getUserName(), userGoogle.getRole().getName(),TypeLogin.LOGIN_GOOGLE);
        return AuthResponse.builder()
                .token(token)
                .firstOauth2(true)
                .userName(userGoogle.getUserName())
                .role(userGoogle.getRole().getName())
                .build();
    }


}
