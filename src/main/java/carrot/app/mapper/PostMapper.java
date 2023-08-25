package carrot.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import carrot.app.dto.CommentDTO;
import carrot.app.dto.PostDTO;

@Mapper
public interface PostMapper {
	List<PostDTO> postList(int pageNo, int pageSize);

	PostDTO selectpost(PostDTO postDto);

	int addPost(PostDTO postDto);

	int updatepost(PostDTO postDto);

	int deletepost(PostDTO postDto);

	List<CommentDTO> commentList(int pnum);

	int addComment(CommentDTO commentDto);

	int deleteComment(int cnum);
	
	int addlikes(PostDTO postDto);
	
	int selectlikes(PostDTO postDto);
	
	int countlikes(PostDTO postDto);
	
	int ismypost(PostDTO postDto);

}
