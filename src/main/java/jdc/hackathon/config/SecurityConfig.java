package jdc.hackathon.config;

import jdc.hackathon.jwt.JwtAuthenticationFilter;
import jdc.hackathon.security.CustomAuthenticationEntryPoint;
import jdc.hackathon.security.oauth.CustomOAuth2UserService;
import jdc.hackathon.security.oauth.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService oauth2UserService;
    private final OAuth2AuthenticationSuccessHandler successHandler;
    private final JwtAuthenticationFilter jwtFilter;
    private final CustomAuthenticationEntryPoint entryPoint;
    private final Environment env;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CSRF 비활성화 + 세션 Stateless + CORS 설정
        http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .sessionManagement(sm -> sm
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .cors(cors -> cors
                        .configurationSource(corsConfig())
                );

        // --- local 프로필: 모든 요청 열기 ---
        if (Arrays.asList(env.getActiveProfiles()).contains("local")) {
            http.authorizeHttpRequests(auth -> auth
                    .requestMatchers("/h2-console/**", "/h2-console/*", "/login.do").permitAll()
                    .anyRequest().permitAll());
            return http.build();
        }

        // --- prod/staging 프로필: 실제 보안 설정 ---
        http.authorizeHttpRequests(auth -> auth
                        // 공개 엔드포인트
                        .requestMatchers(
                                "/api/token/**",
                                "/oauth2/**",
                                "/actuator/health"
                        ).permitAll()

                        // 공개 GET: 글·유저 조회
                        .requestMatchers(HttpMethod.GET,
                                "/api/posts/**",
                                "/api/users/**"
                        ).permitAll()

                        // 그 외 요청은 인증 필요
                        .anyRequest().authenticated()
                )
                // 인증 실패 핸들러
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(entryPoint)
                )
                // OAuth2 로그인 설정
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(ui -> ui
                                .userService(oauth2UserService)
                        )
                        .successHandler(successHandler)
                )
                // JWT 필터 등록
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfig() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173",
                "http://hackathon-alb-463254656.ap-northeast-2.elb.amazonaws.com",
                "https://hackathon-alb-463254656.ap-northeast-2.elb.amazonaws.com",
                "http://www.hackathon-alb-463254656.ap-northeast-2.elb.amazonaws.com"
        ));
        cfg.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
        cfg.setAllowedHeaders(Arrays.asList("*"));
        cfg.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/**", cfg);
        return src;
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173",
                "http://hackathon-alb-463254656.ap-northeast-2.elb.amazonaws.com",
                "https://hackathon-alb-463254656.ap-northeast-2.elb.amazonaws.com",
                "http://www.hackathon-alb-463254656.ap-northeast-2.elb.amazonaws.com"
        ));
        cfg.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
        cfg.setAllowedHeaders(Arrays.asList("*"));
        cfg.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/**", cfg);
        return new CorsFilter(src);
    }
}
