package rand.api.domain.member.repository;

public interface MemberInMemRepository {
    public void emailAuthCodeSave(String Key , String certificationNumber,Long ttl);
}
