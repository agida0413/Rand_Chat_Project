package rand.api.domain.member.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import rand.api.domain.member.model.Members;
import rand.api.domain.member.mapper.MemberMapper;
import rand.api.web.dto.member.response.ResFindIdDTO;

@Repository
@RequiredArgsConstructor
public class MyBatisMemberRepository implements MemberRepository{
    private final MemberMapper memberMapper;


    @Override
    public int emailDuplicateCheck(Members members) {
        return memberMapper.emailDuplicateCheck(members);
    }

    @Override
    public ResFindIdDTO findId(Members members) {
        return memberMapper.findId(members);
    }

    @Override
    public void resetNewPassword(Members members) {
        memberMapper.resetNewPassword(members);
    }

    @Override
    public int findByNnAndEmail(Members members) {
        return memberMapper.findByNnAndEmail(members);
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


}
