package rand.api.domain.member.repository;

public interface MemberInMemRepository {
    public void emailAuthCodeSave(String redisKey , String certificationNumber,Long ttl);
}
