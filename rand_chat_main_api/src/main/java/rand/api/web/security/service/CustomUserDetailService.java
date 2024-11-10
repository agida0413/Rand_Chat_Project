package rand.api.web.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rand.api.domain.member.model.Members;
import rand.api.domain.member.repository.MemberRepository;
import rand.api.web.security.entity.CustomUserDetails;

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