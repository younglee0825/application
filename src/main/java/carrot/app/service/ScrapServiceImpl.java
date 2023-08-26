package carrot.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import carrot.app.mapper.ScrapMapper;

@Service("scrapService")
public class ScrapServiceImpl implements ScrapService {

	@Autowired
	private ScrapMapper mapper;

}
