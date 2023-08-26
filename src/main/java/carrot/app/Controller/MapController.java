package carrot.app.Controller;

import carrot.app.Map.Map;
import carrot.app.Map.MapService;
import carrot.app.Map.MapVo;
import carrot.app.User.UserService;
import carrot.app.User.UserVo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/map")
public class MapController {

    @Autowired
    private MapService mapService;
    private UserService userService;
    private int currentCalories = 0; // 현재 칼로리 값을 저장할 변수

    @GetMapping
    public String map(Model model){
        //회원정보를 잘가져왔는지 확인용(40-43) 끝나면 지울 예정
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserVo userVo = (UserVo) authentication.getPrincipal();
        model.addAttribute("nickname", userVo.getUnick() + "님");
        return "map";
    }


    //json형태
    @PostMapping("/jsonInfo")
    @ResponseBody
    public ResponseEntity<String> saveMeasurementData(@RequestBody String measurementData) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserVo userVo = (UserVo) authentication.getPrincipal();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(measurementData);
            int elapsedTimeInSeconds = jsonNode.get("mtime").asInt();
            double calculatedCalories = jsonNode.get("mykcals").asDouble();

            // 이제 실제 GPS 데이터를 사용하여 이동거리 계산
            // 예시로 임의의 값을 사용하도록 하겠습니다.
            double gpsDistance = 5.0; // 실제 GPS 데이터로 대체

            // 이동거리를 기반으로 칼로리 계산
            double gpsCalories = gpsDistance * 100; // 간단한 칼로리 계산식

            // LocalDateTime을 포맷에 맞게 변환
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy. M. d. HH:mm");
            LocalDateTime mdatetime = LocalDateTime.parse(jsonNode.get("mdatetime").asText(), formatter);

            // Create a MapVo object to store data
            MapVo mapVo = new MapVo();
            mapVo.setMtime(new Time(elapsedTimeInSeconds * 1000)); // 밀리초 단위로 변환
            mapVo.setMdistance(gpsDistance);
            mapVo.setMykcals(gpsCalories);
            mapVo.setMdatetime(mdatetime);
            mapVo.setUser_nick(userVo.getUnick());
            mapVo.setUser_num(userVo.getUnum());

            // Call the service to save the data
            mapService.saveMapData(mapVo);

            // Return response
            return ResponseEntity.ok("{\"result\": \"Measurement data saved\"}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to process measurement data");
        }
    }


    @GetMapping("/fetchDataForDate")
    public ResponseEntity<HashMap<String,Object>> fetchData(
            @RequestParam("year") int year,
            @RequestParam("month") int month,
            @RequestParam("day") int day) {

        String selectedDate = String.format("%04d-%02d-%02d", year, month, day);
        System.out.println(selectedDate);

        try {
            List<MapVo> list = mapService.fetchDataForDate(selectedDate);
            HashMap<String,Object> responseData = new HashMap<>();
            responseData.put("result", list);
            System.out.println(responseData);
            return ResponseEntity.status(HttpStatus.OK).body(responseData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}
