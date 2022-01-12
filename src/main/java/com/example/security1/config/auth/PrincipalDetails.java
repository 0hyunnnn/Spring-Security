package com.example.security1.config.auth;

//시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
//로그인 진행 완료되면 시큐리티가 갖는 session 에 만들어줌. (Security ContextHolder)
//오브젝트가 정해져있음 => Authentication 타입 객체
//Authentication 안에 User 정보가 있어야 됨.
//User 오브젝트 타입은 UserDetails 타입 객체.

//Security Session => Authentication = UserDetails(PrincipalDetails)
//Security Session에 들어갈 수 있는 객체는 Authentication인데
//그 안에 User 정보가 있어야 되잖아? UserDetails 라는 타입에 User 정보를 입력해야되

import com.example.security1.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

//implements UserDetails 하면 PrincipalDetails UserDetails 같은 타입이 되는 거야
//그렇게되면 PrincipalDetails Authenticaion 객체 안에 넣을수가 있어
public class PrincipalDetails implements UserDetails {
    private User user;  //콤포지션 : 클래스가 객체를 품고있는 것, 상속이랑은 다름?

    public PrincipalDetails(User user){
        this.user = user;
    }

    //계정이 갖고있는 권한 목록을 리턴한다. (권한이 여러개 있을 수 있어서 루프를 돌아야 하는데 우리는 한개만 있어서)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();

        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        //위의 collect.add를 람다식으로 하면
        //collect.add(()->{return user.getRole();});
        //위 식이 될 수 있는 이유는 .add 함수 안에 들어올 수 있는 파라메터는 하나밖에 없어서 .add가 알고 있고
        //그리고 GrantedAuthority()안에 함수는 하나 밖에 없다. 그러므로 다 지우면 저것만 남는다.

        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    //계정이 만료되지 않았는지 리턴한다. (true : 만료안됨)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정이 잠겨있지 않았는지 리턴한다. (true : 잠기지 않음)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 비밀번호가 만료되지 않았는지 리턴한다. (true : 만료안됨)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정이 활성화(사용가능)인지 리턴한다. (true : 활성화)
    @Override
    public boolean isEnabled() {
        //만약 우리 사이트에서 1년동안 회원이 로그인을 안하면 => 휴면 계정으로
        //현재시간 - 로그인시간 => 1년 초과하면 return false;
        return true;
    }
}