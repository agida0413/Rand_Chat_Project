package rand.api.domain.member.repository;

import rand.api.domain.member.entity.EmailAuth;

public interface MemberRepository {

    //이메일 중복체크
    public int emailDuplicateCheck(EmailAuth emailAuth);
}
