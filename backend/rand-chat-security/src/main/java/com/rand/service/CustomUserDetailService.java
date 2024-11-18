package com.rand.service;

import com.rand.entity.CustomUserDetails;
import com.rand.member.model.Members;
import com.rand.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub


        Members members = memberRepository.findByUsername(username);



        if(members!=null) {


            return new CustomUserDetails(members);
        }
        //없을경우 null 리턴
        return null;
    }

}