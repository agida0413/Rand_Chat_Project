package rand.api.domain.member.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import rand.api.domain.member.entity.EmailAuth;
import rand.api.domain.member.mapper.MemberMapper;

@Repository
@RequiredArgsConstructor
public class MyBatisMemberRepository implements MemberRepository{
    private final MemberMapper memberMapper;

    //이메일 중복 체크
    @Override
    public int emailDuplicateCheck(EmailAuth emailAuth) {
        return memberMapper.emailDuplicateCheck(emailAuth);
    }
}
