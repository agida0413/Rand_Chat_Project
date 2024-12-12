package com.rand.common.service;

import com.rand.config.rds.ReadOnly;
import com.rand.config.var.RedisKey;
import com.rand.member.model.Members;
import com.rand.member.model.cons.MembersSex;
import com.rand.member.repository.MemberRepository;
import com.rand.redis.InMemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.TimeUnit;
@Service
@RequiredArgsConstructor

public class MemberService implements CommonMemberService{
    private final InMemRepository inMemRepository;
    private final MemberRepository memberRepository;
    @Override

    public Members memberGetInfoMethod(int usrId) {
        //캐시 및


            //캐시 회원정보 조회
            Map<Object,Object> cachedMemberInfo = inMemRepository.getHashValueEntry(RedisKey.MEMBER_INFO_KEY+usrId);

            //있을 시
            if (cachedMemberInfo != null && !cachedMemberInfo.isEmpty()) {

                //레디스에서 조회
                Members redisMembers = memberInfoCacheGet(cachedMemberInfo,usrId);


                //응답
                return  redisMembers;


            }
            else{
                //없을 시 rdb 조회
                Members members = new Members();
                members.setUsrId(usrId);

                Members resultMembers = memberRepository.selectMemberInfo(members);

                //레디스 캐시 세이브(1분 ttl)
                memberInfoCacheSave(usrId,resultMembers);
                return resultMembers;
            }

    }
    private void memberInfoCacheSave(int usrId,Members resultMembers){
        inMemRepository.hashSave(RedisKey.MEMBER_INFO_KEY+usrId,"name",resultMembers.getName(),60, TimeUnit.SECONDS);
        inMemRepository.hashSave(RedisKey.MEMBER_INFO_KEY+usrId,"email",resultMembers.getEmail(),60,TimeUnit.SECONDS);
        inMemRepository.hashSave(RedisKey.MEMBER_INFO_KEY+usrId,"username",resultMembers.getUsername(),60,TimeUnit.SECONDS);
        inMemRepository.hashSave(RedisKey.MEMBER_INFO_KEY+usrId,"nickname",resultMembers.getNickName(),60,TimeUnit.SECONDS);
        inMemRepository.hashSave(RedisKey.MEMBER_INFO_KEY+usrId,"profileImg",resultMembers.getProfileImg(),60,TimeUnit.SECONDS);
        inMemRepository.hashSave(RedisKey.MEMBER_INFO_KEY+usrId,"birth",resultMembers.getBirth(),60,TimeUnit.SECONDS);
        inMemRepository.hashSave(RedisKey.MEMBER_INFO_KEY+usrId,"sex",resultMembers.getSex(),60,TimeUnit.SECONDS);
    }

    private Members memberInfoCacheGet(Map<Object,Object> cachedMemberInfo, int usrId){
        // Redis에 캐시가 있는 경우 DTO로 변환
        String name = (String) cachedMemberInfo.get("name");
        String email = (String) cachedMemberInfo.get("email");
        String username = (String) cachedMemberInfo.get("username");
        String nickname = (String) cachedMemberInfo.get("nickname");
        String profileImg = (String) cachedMemberInfo.get("profileImg");
        String sex = (String) cachedMemberInfo.get("sex");


        LocalDate birth = LocalDate.parse((String) cachedMemberInfo.get("birth"));

        Members redisMembers = new Members();
        redisMembers.setProfileImg(profileImg);
        redisMembers.setName(name);
        redisMembers.setEmail(email);
        redisMembers.setUsername(username);
        redisMembers.setNickName(nickname);
        redisMembers.setBirth(birth);

        redisMembers.setUsrId(usrId);
        if(sex.equals("MAN")){
            redisMembers.setSex(MembersSex.MAN);

        }else{
            redisMembers.setSex(MembersSex.FEMAIL);
        }

        return  redisMembers;
    }
}
