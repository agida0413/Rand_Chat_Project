package com.rand.member.mapper;


import com.rand.member.model.Members;
import org.apache.ibatis.annotations.Mapper;

import com.rand.member.dto.response.ResFindIdDTO;

@Mapper
public interface MemberMapper {

    public int emailDuplicateCheck(Members members);
    public int userNameDuplicateCheck(Members members);
    public int nickNameDuplicateCheck(Members members);
    public void join(Members members);
    public ResFindIdDTO findId(Members members);
    public int findByNnAndEmail(Members members);
    public void resetNewPassword(Members members);
    public Members findByUsrAndEmail(Members members);
    public Members findByEmail(Members members);
    public void activationMem(Members members);
    public Members findByUsername(String username);
    public void pwdWrongUpdate(String username);
    public void memberStateLock(String username);
    public Members findByUsrIdWithLock(Members members);
    public Members findByUsrId(Members members);
    public void updatePwd(Members members);
    public void memberDel(Members members);
    public void updateProfileImg(Members members);
    public Members selectMemberInfo(Members members);
}
