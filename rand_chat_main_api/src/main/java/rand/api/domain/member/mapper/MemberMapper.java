package rand.api.domain.member.mapper;


import org.apache.ibatis.annotations.Mapper;
import rand.api.domain.member.entity.EmailAuth;

@Mapper
public interface MemberMapper {
    //이메일 중복체크
    public int emailDuplicateCheck(EmailAuth emailAuth);

}
