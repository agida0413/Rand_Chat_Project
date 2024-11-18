package com.rand.entity;

import com.rand.member.model.Members;
import com.rand.member.model.cons.MembersSex;
import com.rand.member.model.cons.MembersState;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final Members members;

    public CustomUserDetails(Members members) {

        this.members = members;
    }

    //패스워드
    @Override
    public String getPassword() {

        return members.getPassword();
    }

    //고유번호 리턴
    public int getUsrId()
    {
        return members.getUsrId();
    }

    //이메일을 사용하니 이메일 리턴
    @Override
    public String getUsername() {

        return members.getUsername();
    }
    //닉네임
    public String getNickname() {
        return members.getNickName();
    }
    public MembersState getState() {
        return members.getState();
    }
    public MembersSex getSex(){
        return members.getSex();
    }
    public LocalDate getBirth(){
        return  members.getBirth();
    }
    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    @Override
    public boolean isAccountNonLocked() {

        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    @Override
    public boolean isEnabled() {

        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        return null;
    }
}
