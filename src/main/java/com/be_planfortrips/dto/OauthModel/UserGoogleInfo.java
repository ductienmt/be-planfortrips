package com.be_planfortrips.dto.OauthModel;

public record UserGoogleInfo (
        String sub,
        String name,
        String give_name,
        String family_name,
        String picture,
        String email,
        boolean email_verified,
        String local
){
}
