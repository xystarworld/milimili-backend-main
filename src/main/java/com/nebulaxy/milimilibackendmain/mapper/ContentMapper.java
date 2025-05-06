package com.nebulaxy.milimilibackendmain.mapper;

import com.nebulaxy.milimilibackendmain.entity.Content;
import com.nebulaxy.milimilibackendmain.entity.Download;
import com.nebulaxy.milimilibackendmain.entity.Favorites;
import com.nebulaxy.milimilibackendmain.entity.Post;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;

public interface ContentMapper {

    List<Content> selectAllFiles();

    Content selectFiles(Integer vid);

    Content selectFilesByTitle(String title);

    void insertFiles(Content content);

    List<Content> selectFiles3(Integer mcId, String title, Integer status, Integer scId, String topic);

    List<Post> selectFiles2(String title, Integer status, Integer uid, Integer topic);

    Post selectPost(Integer postid);

    void insertPost(Post post);

    void moderation(String vid, Integer status);

    void moderationPost(Integer postid, Integer status);

    void likePost(Integer postid);

    void unlikePost(Integer postid);

    List<Post> hotPost();

    List<Content> hotFiles();

    List<Content> hotVideos();

    void favorites(Integer uid, Integer vid);

    Favorites selectFavorites(Integer uid, Integer vid);

    void unFavorites(Integer uid, Integer vid);

    List<Favorites> selectAllFavorites(Integer uid, Integer vid);

    Download selectDownloadCount(String vid);

    void insertDownloadCount(String vid);

    void downloadCount(String vid);

    List<Post> selectAllPost();

    List<Content> findAllFiles();

    @Select("SELECT vid FROM content WHERE uid = #{uid} and status = 1 order by vid desc limit 1")
    Integer getLatestVidByUid(Integer uid);

    List<Content> selectFiles1(Integer mcId, String title, Integer state,String scId);

    List<Post> selectPostsByTitleKeyword(@Param("keyword") String keyword);

    List<Content> selectLessonsByTitleKeyword(@Param("keyword") String keyword);

    List<Content> selectFilesByTitleKeyword(@Param("keyword") String keyword);
}
