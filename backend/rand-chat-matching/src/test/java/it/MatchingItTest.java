package it;


import com.rand.MatchingApp;
import com.rand.member.dto.request.CurLocationDTO;
import com.rand.member.model.Members;
import com.rand.member.model.cons.MembersSex;
import com.rand.member.repository.MemberRepository;

import com.rand.redis.InMemRepository;
import com.rand.service.MatchAcceptService;
import com.rand.service.MatchService;
import net.bytebuddy.utility.dispatcher.JavaDispatcher;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@SpringBootTest(classes = MatchingApp.class)
@Transactional
public class MatchingItTest {

    private static final double MIN_LAT = 37.0; // 최소 위도 (경기도 남부)
    private static final double MAX_LAT = 37.8; // 최대 위도 (경기도 북부, 서울과 경계)
    private static final double MIN_LON = 126.6; // 최소 경도 (경기도 서부)
    private static final double MAX_LON = 127.5; // 최대 경도 (경기도 동부)
    private static Map<String , Object > map = new HashMap<>();

    @Autowired
    private MatchService matchService;

    @Autowired
    private MatchAcceptService matchAcceptService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private InMemRepository inMemRepository;


    @Test
    public void matchingLogic(){

        //임의 회원 10000명의 위치정보 저장
        for(int i =0; i<2; i++){
            Random random = new Random();
            CurLocationDTO dto = new CurLocationDTO();
            double lat = MIN_LAT + (MAX_LAT - MIN_LAT) * random.nextDouble();
            double lon = MIN_LON + (MAX_LON - MIN_LON) * random.nextDouble();
            dto.setLocaleLat(lat);
            dto.setLocaleLon(lon);

            map.put(String.valueOf(i),dto);
            Members members = new Members(dto);
            inMemRepository.saveLoc(String.valueOf(i),lat,lon);

        }




    }


}
