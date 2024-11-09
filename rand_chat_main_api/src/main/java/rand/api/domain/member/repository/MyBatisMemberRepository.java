package rand.api.domain.member.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import rand.api.domain.member.entity.EmailAuthSend;
import rand.api.domain.member.entity.Members;
import rand.api.domain.member.mapper.MemberMapper;

@Repository
@RequiredArgsConstructor
public class MyBatisMemberRepository implements MemberRepository{
    private final MemberMapper memberMapper;


    @Override
    public int emailDuplicateCheckInJoinForm(Members members) {
        return memberMapper.emailDuplicateCheckInJoinForm(members);
    }

    @Override
    public void join(Members members) {
         memberMapper.join(members);
    }

    @Override
    public int nickNameDuplicateCheck(Members members) {
        return memberMapper.nickNameDuplicateCheck(members);
    }

    @Override
    public int userNameDuplicateCheck(Members members) {
        return memberMapper.userNameDuplicateCheck(members);
    }

    //이메일 중복 체크
    @Override
    public int emailDuplicateCheck(EmailAuthSend emailAuth) {
        return memberMapper.emailDuplicateCheck(emailAuth);
    }
}
