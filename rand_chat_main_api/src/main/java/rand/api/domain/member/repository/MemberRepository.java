package rand.api.domain.member.repository;

import rand.api.domain.member.model.Members;
import rand.api.web.dto.member.response.ResFindIdDTO;

public interface MemberRepository {


    public int emailDuplicateCheck(Members members);
    public int userNameDuplicateCheck(Members members);
    public int nickNameDuplicateCheck(Members members);
    public void join(Members members);
    public ResFindIdDTO findId(Members members);
    public int findByNnAndEmail(Members members);
    public void resetNewPassword(Members members);
}
