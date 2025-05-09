package jdc.hackathon.security.oauth;

import jdc.hackathon.domain.entity.User;
import jdc.hackathon.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        var oAuth2User = super.loadUser(userRequest);

        String provider     = userRequest.getClientRegistration().getRegistrationId();
        String oauthId      = oAuth2User.getName();
        String baseNick     = (String) oAuth2User.getAttributes().get("name");
        String profileImage = (String) oAuth2User.getAttributes().get("picture");

        User user = userRepository.findByProviderAndOauthId(provider, oauthId)
                .orElseGet(() -> {
                    String nickname = makeUniqueNickname(baseNick);
                    return userRepository.save(User.builder()
                            .provider(provider)
                            .oauthId(oauthId)
                            .nickname(nickname)
                            .profileImage(profileImage)
                            .build());
                });

        return new CustomOAuth2User(user, oAuth2User.getAttributes());
    }

    private String makeUniqueNickname(String base) {
        String nick = base;
        while (userRepository.existsByNickname(nick)) {
            nick = base + "_" + RandomStringUtils.randomAlphanumeric(5);
        }
        return nick;
    }
}
