package rand.api.domain.member.repository;

import rand.api.domain.member.entity.EmailAuthSend;
import rand.api.domain.member.entity.Members;

public interface MemberRepository {

    //이메일 중복체크
    public int emailDuplicateCheck(EmailAuthSend emailAuth);
    public int emailDuplicateCheckInJoinForm(Members members);
    public int userNameDuplicateCheck(Members members);
    public int nickNameDuplicateCheck(Members members);
    public void join(Members members);
}
