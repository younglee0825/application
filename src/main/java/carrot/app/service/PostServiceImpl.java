package carrot.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import carrot.app.dto.PostDTO;
import carrot.app.mapper.PostMapper;

@Service("postService")
public class PostServiceImpl implements PostService {

	@Autowired
	private PostMapper mapper;

	@Override
	public List<PostDTO> getPostList(int pageNo, int pageSize) {
		return mapper.postList(pageNo, pageSize);
	}

	@Override
	public int addPost(PostDTO postDto) {
		return mapper.addPost(postDto);
	}

	@Override
	public PostDTO getPost(PostDTO postDto) {
		return mapper.selectpost(postDto);
	}

	@Override
	public int addLikes(PostDTO postDto) {
		return mapper.addlikes(postDto);
	}

	@Override
	public int selectLikes(PostDTO postDto) {
		return mapper.selectlikes(postDto);
	}

	@Override
	public int countlikes(PostDTO postDto) {
		return mapper.countlikes(postDto);
	}

	@Override
	public int isMyPost(PostDTO postDto) {
		return mapper.ismypost(postDto);
	}

	@Override
	public int modifyPost(PostDTO postDto) {
		return mapper.updatepost(postDto);
	}

	@Override
	public int deletePost(PostDTO postDto) {
		return mapper.deletepost(postDto);
	}

}
