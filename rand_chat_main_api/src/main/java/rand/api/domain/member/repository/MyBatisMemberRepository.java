package rand.api.domain.member.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rand.api.domain.member.model.Members;
import rand.api.domain.member.mapper.MemberMapper;
import rand.api.domain.member.model.cons.MembersState;
import rand.api.web.dto.member.response.ResFindIdDTO;

@Repository
@RequiredArgsConstructor
public class MyBatisMemberRepository implements MemberRepository{
    private final MemberMapper memberMapper;


    @Override
    public Members findByEmail(Members members) {
        return memberMapper.findByEmail(members);
    }

    @Override
    public Members findByUsername(String username) {
        return memberMapper.findByUsername(username);
    }

    @Override
    public void activationMem(Members members) {
        memberMapper.activationMem(members);
    }

    @Override
    public int emailDuplicateCheck(Members members) {
        return memberMapper.emailDuplicateCheck(members);
    }

    @Override
    @Transactional
    public void pwdWrongUpdate(String username) {
        Members findMembers = findByUsername(username);
        if(findMembers!=null){
            memberMapper.pwdWrongUpdate(username);

            if(findMembers.getState().equals(MembersState.ACTIVE) && findMembers.getPwdWrong() >= 4){
                memberMapper.memberStateLock(findMembers.getUsername());
            }
        }
    }

    @Override
    public ResFindIdDTO findId(Members members) {
        return memberMapper.findId(members);
    }

    @Override
    public Members findByUsrAndEmail(Members members) {
        return memberMapper.findByUsrAndEmail(members);
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
