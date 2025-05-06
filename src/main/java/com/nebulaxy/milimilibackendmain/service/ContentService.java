package com.nebulaxy.milimilibackendmain.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nebulaxy.milimilibackendmain.entity.*;
import com.nebulaxy.milimilibackendmain.exception.CustomerException;
import com.nebulaxy.milimilibackendmain.mapper.ContentMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContentService {

    @Resource
    ContentMapper contentMapper;

    @Autowired
    private TimetableService timetableService;

    // 查询全部文件
    public List<Content> selectAllFiles() {
        return contentMapper.selectAllFiles();
    }

    // 查询文件,
    public Content selectFiles(Integer vid) {
        return contentMapper.selectFiles(vid);
    }

    // 新增文件
    public void addFiles(Content content) {
        // 根据新账号查询数据库，是否存在同样的账号数据
        Content dbfiles = contentMapper.selectFilesByTitle(content.getTitle());
        if (dbfiles != null) {
            throw new CustomerException("文件重复");
        }
        contentMapper.insertFiles(content);
    }

    // 分页查询资料
    public PageInfo<Content> fileSelectPage(Integer pageNum, Integer pageSize, Integer mcId, String title,
            Integer status, Integer scId, String topic) {
        // 开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<Content> list = contentMapper.selectFiles3(mcId, title, status, scId, topic);
        return PageInfo.of(list);
    }

    // 分页查询视频
    public PageInfo<Content> videoSelectPage(Integer pageNum, Integer pageSize, Integer mcId, String title,
            Integer state, String scId) {
        // 开启分页查询video
        PageHelper.startPage(pageNum, pageSize);
        List<Content> list = contentMapper.selectFiles1(mcId, title, state,scId);
        return PageInfo.of(list);
    }

    // 分页查询帖子
    public PageInfo<Post> postSelectPage(Integer pageNum, Integer pageSize, String title, Integer status, Integer uid,
            Integer topic) {
        // 开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<Post> list = contentMapper.selectFiles2(title, status, uid, topic);
        return PageInfo.of(list);
    }

    // 新增帖子
    public void posting(Post post) {
        contentMapper.insertPost(post);
    }

    // 查询所有帖子
    public List<Post> selectAllPost() {
        return contentMapper.selectAllPost();
    }

    // 审核帖子
    public void moderation(Content content) {
        contentMapper.moderation(content.getVid(), content.getStatus());
    }

    public void moderationPost(Post post) {
        contentMapper.moderationPost(post.getPostid(), post.getStatus());
    }

    public Post selectPost(Integer postid) {
        return contentMapper.selectPost(postid);
    }

    // （取消）点赞帖子
    public void likePost(Integer postid) {
        contentMapper.likePost(postid);
    }

    public void unlikePost(Integer postid) {
        contentMapper.unlikePost(postid);
    }

    // 查询帖子数据

    // 主页推荐部分
    // 无参数，取热度前4
    // 帖子
    public List<Post> homePost() {
        return contentMapper.hotPost();
    }

    // 文件
    public List<Content> homeFiles() {
        return contentMapper.hotFiles();
    }

    // 视频
    public List<Content> homeVideos() {
        return contentMapper.hotVideos();
    }

    //个性化推荐
    //如果存在课程表时触发
    // 查询课程表并分词
    private List<String> keywords(Integer uid){
        // 获取用户的课程表
        List<Timetable> dbTimetable = timetableService.getCoursesByUid(uid);
        if (dbTimetable == null || dbTimetable.isEmpty()) {
            return List.of(); // 如果没有课程表，返回空列表
        }

        // 提取课程名称
        StringBuilder courseNames = new StringBuilder();
        for (Timetable timetable : dbTimetable) {
            courseNames.append(timetable.getCourse_name()).append(" ");
        }

        // 使用 IK 分词器进行分词
        List<String> keywords = new ArrayList<>();
        try (IKAnalyzer analyzer = new IKAnalyzer(true)) {
            TokenStream tokenStream = analyzer.tokenStream("", courseNames.toString());
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                CharTermAttribute charTermAttribute = tokenStream.getAttribute(CharTermAttribute.class);
                keywords.add(charTermAttribute.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // 如果分词失败，返回空列表
        }
        return keywords;
    }

    // 帖子
    public List<Post> personalizationRecommendationPost(Integer uid) {
      List<String> keywords = keywords(uid);
        // 根据分词结果查询帖子
        List<Post> recommendedPosts = new ArrayList<>();
        for (String keyword : keywords) {
            List<Post> posts = contentMapper.selectPostsByTitleKeyword(keyword);
            recommendedPosts.addAll(posts);
        }
        return recommendedPosts;
    }
    // 视频课程
    public List<Content> personalizationRecommendationLessons(Integer uid) {
        List<String> keywords = keywords(uid);
        // 根据分词结果查询视频
        List<Content> recommendedVideos = new ArrayList<>();
        for (String keyword : keywords) {
            List<Content> videos = contentMapper.selectLessonsByTitleKeyword(keyword);
            recommendedVideos.addAll(videos);
        }
        return recommendedVideos;
    }
    // 文件
    public List<Content> personalizationRecommendationFiles(Integer uid) {
        List<String> keywords = keywords(uid);
        // 根据分词结果查询文件
        List<Content> recommendedFiles = new ArrayList<>();
        for (String keyword : keywords) {
            List<Content> files = contentMapper.selectFilesByTitleKeyword(keyword);
            recommendedFiles.addAll(files);
        }
        return recommendedFiles;
    }

    // 收藏
    public void favorites(Favorites favorites) {
        Favorites dbInfo = contentMapper.selectFavorites(favorites.getUid(), favorites.getVid());
        if (dbInfo != null) {
            contentMapper.unFavorites(favorites.getUid(), favorites.getVid());
        } else {
            contentMapper.favorites(favorites.getUid(), favorites.getVid());
        }
    }

    public List<Favorites> selectFavorites(Integer uid, Integer vid) {
        return contentMapper.selectAllFavorites(uid, vid);
    }

    // 文件下载计数
    public void downloadCount(Content content) {
        Download dbinfo = contentMapper.selectDownloadCount(content.getVid());
        if (dbinfo != null) {
            contentMapper.downloadCount(content.getVid());
        } else {
            contentMapper.insertDownloadCount(content.getVid());
        }

    }

    // 文件下载数查询
    public Download selectDownloadCount(String vid) {
        return contentMapper.selectDownloadCount(vid);
    }

    public List<Content> findAllFiles() {
        return contentMapper.findAllFiles();
    }

    // 确保默认
    public Integer insertContent(Content content) {
        // 设置默认值
        if (content.getStatus() == null)
            content.setStatus(1);
        if (content.getUploadDate() == null) {
            content.setUploadDate(new Timestamp(System.currentTimeMillis()));
        }
        if (content.getScId() == null)
            content.setScId("");
        if (content.getTopic() == null)
            content.setTopic("");
        if (content.getDuration() == null)
            content.setDuration(0.0);

        contentMapper.insertFiles(content);
        return content.getUid();
    }

    public Integer getLatestVidByUid(Integer uid) {
        return contentMapper.getLatestVidByUid(uid);
    }



}
