package carrot.app.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import carrot.app.dto.RecruitDTO;

@Mapper
public interface RecruitMapper {
	List<RecruitDTO> recruitList(Map<String, Object> recruitMap);

	RecruitDTO selectRecruit(RecruitDTO recruitDto);

	int addRecruit(RecruitDTO recruitDto);

	int updateRecruit(RecruitDTO recruitDto);

	int deleteRecruit(RecruitDTO recruitDto);
	
	int countJoin(RecruitDTO recruitDto);
	
	int insertJoin(RecruitDTO recruitDto);
	
	int countRecruitJoin(RecruitDTO recruitDto);
	
	int countScrap(RecruitDTO recruitDto);
	
	int insertScrap(RecruitDTO recruitDto);
}
