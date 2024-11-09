package rand.api.domain.member.repository;

import rand.api.domain.member.model.EmailAuthSend;
import rand.api.domain.member.model.FindId;
import rand.api.domain.member.model.Members;
import rand.api.web.dto.member.response.ResFindIdDTO;

public interface MemberRepository {

    //이메일 중복체크
    public int emailDuplicateCheck(EmailAuthSend emailAuth);
    public int emailDuplicateCheckInJoinForm(Members members);
    public int userNameDuplicateCheck(Members members);
    public int nickNameDuplicateCheck(Members members);
    public void join(Members members);
    public ResFindIdDTO findId(FindId findId);
}
