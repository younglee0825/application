package carrot.app.service;

import java.util.List;

import carrot.app.dto.PostDTO;

public interface PostService {

	public List<PostDTO> getPostList(int pageNo, int pageSize);
	
	public int addPost(PostDTO postDto);
	
	public PostDTO getPost(PostDTO postDto);
	
	public int addLikes(PostDTO postDto);
	
	public int selectLikes(PostDTO postDto);
	
	public int countlikes(PostDTO postDto);
	
	public int isMyPost(PostDTO postDto);
	
	public int modifyPost(PostDTO postDto);
	
	public int deletePost(PostDTO postDto);
}