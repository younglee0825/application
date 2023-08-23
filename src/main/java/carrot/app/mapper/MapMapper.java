package carrot.app.mapper;

import carrot.app.Map.MapService;
import carrot.app.Map.MapVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper
public interface MapMapper {
    // SaveJsonData
    void saveMapData(MapVo mapvo);
    List<MapVo> findByMdatetime(@Param("date") String selectedDate);



}
