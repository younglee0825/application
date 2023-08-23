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
            double distance = jsonNode.get("mdistance").asDouble();
            double calculatedCalories = jsonNode.get("mykcals").asDouble();
            //LocalDateTime mdatetime = LocalDateTime.parse(jsonNode.get("mdatetime").asText(), DateTimeFormatter.ISO_DATE_TIME);
            String dateTimeString = jsonNode.get("mdatetime").asText();

            //dateTimeString = dateTimeString.replace("오후", "PM").replaceAll("오후|오전", "");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy. M. d. HH:mm");
            LocalDateTime mdatetime = LocalDateTime.parse(dateTimeString, formatter);

            //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy. M. d.", Locale.US);
            //LocalDate localDate = LocalDate.parse(jsonNode.get("mdatetime").asText(), formatter);
            //LocalDateTime mdatetime = localDate.atStartOfDay();
            // Convert elapsedTimeInSeconds to Time type
            Time mtime = Time.valueOf(LocalTime.ofSecondOfDay(elapsedTimeInSeconds));

            // Create a MapVo object to store data
            MapVo mapVo = new MapVo();
            mapVo.setMtime(mtime);
            mapVo.setMdistance(distance);
            mapVo.setMykcals(calculatedCalories);
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
