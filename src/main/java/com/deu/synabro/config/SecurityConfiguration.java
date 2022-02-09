package com.deu.synabro.config;

import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@Configuration  // Bean 관리하는 어노테이션
@EnableWebSecurity  // Spring Security를 활성화하는 어노테이션
// 해당 클래스가 Spring Security 설정 파일로 역할을 하기 위해선 WebSecurityConfigurerAdapter 클래스를 상속해야 함
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtProvider jwtProvider;

//    // 암호화에 필요한 PasswordEncoder 를 Bean 등록합니다.
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    }
//
//    // authenticationManager를 Bean 등록합니다.
//    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .authorizeHttpRequests()
                    .antMatchers("*" ,"/home/*", "/*/signin", "/*/signup").permitAll()  //누구나 접근 가능
                    .anyRequest().permitAll();
//                    .antMatchers("/user/**").hasRole("USER")
//                    .antMatchers("/admin/**").hasRole("ADMIN")
//                    .anyRequest().permitAll()  // 나머지는 권한이 있기만 하면 접근 가능
//                    .and()
//                    .addFilterBefore(new JwtAuthenticationFilter(jwtProvider),
//                            UsernamePasswordAuthenticationFilter.class);
            // JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 전에 넣는다
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
