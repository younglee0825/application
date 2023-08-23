package carrot.app.Map;

import carrot.app.User.UserVo;
import carrot.app.mapper.MapMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MapService {

    @Autowired
    private final MapMapper mapMapper;

    @Transactional
    public void saveMapData (MapVo mapVo){
        System.out.println("mapVo : "+mapVo);
        mapMapper.saveMapData(mapVo);
    }
    public List<MapVo> fetchDataForDate(String selectedDate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserVo userVo = (UserVo) authentication.getPrincipal();

        List<MapVo> mylist = mapMapper.findByMdatetime(selectedDate);
        List<MapVo> nicknameList  = new ArrayList<>();
        // 선택한 날짜에 해당하는 데이터를 데이터베이스에서 조회
        for (MapVo mapVo : mylist) {
            String nickname = mapVo.getUser_nick();
            if(nickname.equals(userVo.getUnick())){
                nicknameList.add(mapVo);
            }
        }
        return nicknameList;
    }
}

