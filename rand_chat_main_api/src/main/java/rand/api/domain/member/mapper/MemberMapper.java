package rand.api.domain.member.mapper;


import org.apache.ibatis.annotations.Mapper;
import rand.api.domain.member.model.Members;
import rand.api.web.dto.member.response.ResFindIdDTO;

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
}
