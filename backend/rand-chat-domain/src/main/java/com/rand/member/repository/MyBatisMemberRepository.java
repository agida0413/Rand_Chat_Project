package com.rand.member.repository;

import com.rand.config.rds.ReadOnly;
import com.rand.config.rds.WriteOnly;
import com.rand.member.mapper.MemberMapper;
import com.rand.member.model.Members;
import com.rand.member.model.cons.MembersState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.rand.member.dto.response.ResFindIdDTO;

@Repository
@RequiredArgsConstructor
public class MyBatisMemberRepository implements MemberRepository{
    private final MemberMapper memberMapper;


    @Override
    @ReadOnly
    public Members findByEmail(Members members) {
        return memberMapper.findByEmail(members);
    }

    @Override
    @ReadOnly
    public Members findByUsername(String username) {
        return memberMapper.findByUsername(username);
    }

    @Override
    @WriteOnly
    public void activationMem(Members members) {
        memberMapper.activationMem(members);
    }

    @Override
    @ReadOnly
    public int emailDuplicateCheck(Members members) {
        return memberMapper.emailDuplicateCheck(members);
    }

    @Override
    @ReadOnly
    public Members findByUsrId(Members members) {
        return memberMapper.findByUsrId(members);
    }

    @Override
    @ReadOnly
    public Members findByUsrIdWithLock(Members members) {
        return memberMapper.findByUsrIdWithLock(members);
    }

    @Override
    @Transactional
    @WriteOnly
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
    @ReadOnly
    public ResFindIdDTO findId(Members members) {
        return memberMapper.findId(members);
    }

    @Override
    @ReadOnly
    public Members findByUsrAndEmail(Members members) {
        return memberMapper.findByUsrAndEmail(members);
    }

    @Override
    @WriteOnly
    public void resetNewPassword(Members members) {
        memberMapper.resetNewPassword(members);
    }

    @Override
    @ReadOnly
    public int findByNnAndEmail(Members members) {
        return memberMapper.findByNnAndEmail(members);
    }

    @Override
    @WriteOnly
    public void join(Members members) {
        memberMapper.join(members);
    }

    @Override
    @ReadOnly
    public int nickNameDuplicateCheck(Members members) {
        return memberMapper.nickNameDuplicateCheck(members);
    }

    @Override
    @ReadOnly
    public int userNameDuplicateCheck(Members members) {
        return memberMapper.userNameDuplicateCheck(members);
    }

    @Override
    @WriteOnly
    public void updatePwd(Members members){
        memberMapper.updatePwd(members);
    }

    @Override
    @WriteOnly
    public void memberDel(Members members){
        memberMapper.memberDel(members);
    }
    @Override
    @WriteOnly
    public void updateProfileImg(Members members){
        memberMapper.updateProfileImg(members);
    }
    @Override
    @ReadOnly
    public Members selectMemberInfo(Members members){
        return memberMapper.selectMemberInfo(members);
    }
}
