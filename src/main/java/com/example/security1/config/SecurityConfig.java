package com.example.security1.config;

import com.example.security1.config.auth.PrincipalDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//bean 등록? : 스프링 컨테이너에서 객체를 관리할 수 있게 하는 것
//bean 등록(IoC가 관리)
@Configuration
//스프링 시큐리티 필터(SecurityConfig)가 스프링 필터체인에 등록된다 = 시큐리티 필터 추가 = 시큐리티 필터가 등록이 된다.
//=> 스프링 시큐리티는 이미 활성화는 되어 있는데 어떤 설정들을 해당 파일에서 하겠다는 것
@EnableWebSecurity
//secured 어노테이션 활성화, preAuthorize,postAuthorize 두 개의 어노테이션 활성화
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PrincipalDetailsService principalDetailsService;

    @Bean   //해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.
    public BCryptPasswordEncoder encodePwd(){
        return new BCryptPasswordEncoder();
    }

    //시큐리티가 대신 로그인해주는데 password를 가로채기를 하는데
    //해당 password가 뭘로 해쉬 되어 회원가입이 되었는지 알아야
    //같은 해쉬로 암호화해서 DB에 있는 해쉬랑 비교할 수 있음.
    //이걸 안하면 패스워드 비교를 못해서 꼭 해줘야 한다?!
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(principalDetailsService).passwordEncoder(encodePwd());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()   //csrf 토큰 비활성화
            .authorizeRequests()
                .antMatchers("/user/**").authenticated()    //인증만 되면 들어갈 수 있는 주소
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .anyRequest().permitAll()
            .and()
                .formLogin()
                .loginPage("/loginForm")
                .loginProcessingUrl("/login")  // /login 주소 호출 되면 시큐리티가 낚아채서 대신 로그인 진행해줌.
                .defaultSuccessUrl("/");
    }
}