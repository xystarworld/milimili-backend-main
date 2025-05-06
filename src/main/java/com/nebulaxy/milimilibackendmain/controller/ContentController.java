package com.nebulaxy.milimilibackendmain.controller;

import cn.hutool.core.io.FileUtil;
import com.github.pagehelper.PageInfo;
import com.nebulaxy.milimilibackendmain.common.Result;
import com.nebulaxy.milimilibackendmain.entity.Content;
import com.nebulaxy.milimilibackendmain.entity.Download;
import com.nebulaxy.milimilibackendmain.entity.Favorites;
import com.nebulaxy.milimilibackendmain.entity.Post;
import com.nebulaxy.milimilibackendmain.exception.CustomerException;
import com.nebulaxy.milimilibackendmain.service.ContentService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * 处理文件上传下载接口
 */
@RestController
@RequestMapping("/content")
public class ContentController {

    @Resource
    ContentService contentService;

    private static final Logger logger = LoggerFactory.getLogger(ContentController.class);

    /**
     * 文件上传（图片上传）
     */
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) throws Exception {
        // 文件存放路径
        String filePath = "D:/curriculum design/files/avatar/";
        if (!FileUtil.exist(filePath)) {
            FileUtil.mkdir(filePath);
        }
        byte[] bytes = file.getBytes();
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename(); // 上传的毫秒时加上文件的原始名称
        // 写入文件
        FileUtil.writeBytes(bytes, filePath + fileName);
        String url = "http://localhost:9090/content/download/" + fileName;
        return Result.success(url);
    }

    /**
     * 文件上传（文件）
     */
    @PostMapping("/filesUpload")
    public Result filesUpload(@RequestParam("file") MultipartFile file) throws Exception {
        // 文件存放路径
        String filePath = "D:/curriculum design/files/files/";
        if (!FileUtil.exist(filePath)) {
            FileUtil.mkdir(filePath);
        }
        byte[] bytes = file.getBytes();
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename(); // 上传的毫秒时加上文件的原始名称
        // 写入文件
        FileUtil.writeBytes(bytes, filePath + fileName);
        String url = "http://localhost:9090/content/filesDownload/" + fileName;
        return Result.success(url);
    }


    /**
     * 文件分片上传（每片50MB）
     */
    @PostMapping("/chunkUpload")
    public Result chunkUpload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("chunkNumber") int chunkNumber,
            @RequestParam("totalChunks") int totalChunks,
            @RequestParam("identifier") String identifier,
            @RequestParam("filename") String originalFilename) throws Exception {

        // 文件存放路径
        String chunkPath = "D:/curriculum design/files/temp/" + identifier + "/";
        if (!FileUtil.exist(chunkPath)) {
            FileUtil.mkdir(chunkPath);
        }

        // 保存分片文件，命名为chunkNumber.tmp
        String chunkFileName = chunkNumber + ".tmp";
        FileUtil.writeBytes(file.getBytes(), chunkPath + chunkFileName);

        // 计算并打印上传进度
        double progress = (double) chunkNumber / totalChunks * 100;
        System.out.printf("上传进度: %.2f%% (分片 %d/%d)%n", progress, chunkNumber, totalChunks);

        // 如果是最后一个分片，则合并文件
        if (chunkNumber == totalChunks) {
            return mergeFiles(identifier, originalFilename);
        }

        return Result.success("分片上传成功");
    }

    /**
     * 合并分片文件
     */
    private Result mergeFiles(String identifier, String originalFilename) throws IOException {
        String chunkPath = "D:/curriculum design/files/temp/" + identifier + "/";
        String finalPath = "D:/curriculum design/files/files/";

        if (!FileUtil.exist(finalPath)) {
            FileUtil.mkdir(finalPath);
        }

        // 最终文件名：时间戳+原始文件名
        String fileName = System.currentTimeMillis() + "_" + originalFilename;

        try (FileOutputStream fos = new FileOutputStream(finalPath + fileName)) {
            // 获取所有分片文件并按序号排序
            File[] chunks = new File(chunkPath).listFiles();
            if (chunks != null) {
                Arrays.sort(chunks, Comparator.comparingInt(f -> Integer.parseInt(f.getName().split("\\.")[0])));

                // 合并文件
                for (File chunk : chunks) {
                    Files.copy(chunk.toPath(), fos);
                }
            }
        }

        // 合并完成后删除临时分片目录
        FileUtil.del(chunkPath);

        String url = "http://localhost:9090/content/filesDownload/" + fileName;
        return Result.success(url);
    }

    /**
     * 检查分片是否存在（用于前端上传前的检查）
     */
    @GetMapping("/checkChunk")
    public Result checkChunk(
            @RequestParam("identifier") String identifier,
            @RequestParam("chunkNumber") int chunkNumber) {

        String chunkPath = "D:/curriculum design/files/temp/" + identifier + "/";
        String chunkFileName = chunkPath + chunkNumber + ".tmp";

        if (FileUtil.exist(chunkFileName)) {
            return Result.success("分片已存在");
        }
        return Result.success("分片不存在");
    }


    /**
     * 文件下载接口（图片）
     * 下载路径为 http://localhost:9090/content/download/**
     */
    @GetMapping("/download/{contentName}")
    public void download(@PathVariable String contentName, HttpServletResponse response) throws Exception {
        // 找到文件的位置
        String filePath = "D:/curriculum design/files/avatar/";
        String realPath = filePath + contentName;
        boolean exist = FileUtil.exist(realPath);
        if (!exist) {
            throw new CustomerException("文件不存在");
        }
        // 读取文件字节流
        byte[] bytes = FileUtil.readBytes(realPath);
        ServletOutputStream os = response.getOutputStream();
        // 输出流对象，把文件写出到客户端
        os.write(bytes);
        os.flush();
        os.close();
    }

    /**
     * 文件下载接口（文件）
     * 下载路径为 http://localhost:9090/content/filesDownload/**
     */
    @GetMapping("/filesDownload/{contentName}")
    public void filesDownload(@PathVariable String contentName, HttpServletResponse response) throws Exception {
        // 找到文件的位置
        String filePath = "D:/curriculum design/files/files/";
        String realPath = filePath + contentName;
        boolean exist = FileUtil.exist(realPath);
        if (!exist) {
            throw new CustomerException("文件不存在");
        }
        // 读取文件字节流
        byte[] bytes = FileUtil.readBytes(realPath);
        ServletOutputStream os = response.getOutputStream();
        // 输出流对象，把文件写出到客户端
        os.write(bytes);
        os.flush();
        os.close();
    }

    /**
     * 文件下载计数接口
     */
    @PostMapping("/downloadCount")
    public Result downloadCount(@RequestBody Content content) {
        contentService.downloadCount(content);
        return Result.success();
    }

    /**
     * 查询文件下载数
     */
    @GetMapping("/downloadCountFinder")
    public Result downloadCountFinder(@RequestParam String vid) {
        Download download = contentService.selectDownloadCount(vid);
        return Result.success(download);
    }

    /**
     * 文件查询
     */
    @GetMapping("/selectAllFiles")
    public Result selectAllFiles() {
        List<Content> contentsList = contentService.selectAllFiles();
        return Result.success(contentsList);
    }

    /**
     * 文件查询（文件）
     */
    @GetMapping("/findAllFiles")
    public Result findAllFiles() {
        List<Content> contentsList = contentService.findAllFiles();
        return Result.success(contentsList);
    }

    /**
     * 文件查询
     */
    @GetMapping("/selectFiles")
    public Result selectFiles(@RequestParam Integer vid) {
        Content contentsList = contentService.selectFiles(vid);
        return Result.success(contentsList);
    }

    /**
     * 新增文件
     */
    @PostMapping("/filesAdd")
    public Result addFiles(@RequestBody Content content) {
        // @RequestBody 接收前端传来的 json 数据
        contentService.addFiles(content);
        return Result.success();
    }

    /**
     * 新增帖子
     */
    @PostMapping("/posting")
    public Result posting(@RequestBody Post post) {
        contentService.posting(post);
        return Result.success();
    }

    /**
     * 文件审核
     */
    @PutMapping("/moderation")
    public Result moderation(@RequestBody Content content) {
        contentService.moderation(content);
        return Result.success();
    }

    /**
     * 帖子审核
     */
    @PutMapping("/moderationPost")
    public Result moderationPost(@RequestBody Post post) {
        contentService.moderationPost(post);
        return Result.success();
    }

    /**
     * 分页查询文件
     * pageNum 当前页码
     * pageSize 每页展示数量
     */
    @GetMapping("/fileSelectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "12") Integer pageSize,
            @RequestParam Integer mcId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer scId,
            @RequestParam(required = false) String topic) {
        PageInfo<Content> pageInfo = contentService.fileSelectPage(pageNum, pageSize, mcId, title, status, scId, topic);
        return Result.success(pageInfo);
    }

    /**
     * 分页查询帖子
     * pageNum 当前页码
     * pageSize 每页展示数量
     */
    @GetMapping("/postSelectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "12") Integer pageSize,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer uid,
            @RequestParam(required = false) Integer topic) {
        PageInfo<Post> pageInfo = contentService.postSelectPage(pageNum, pageSize, title, status, uid, topic);
        return Result.success(pageInfo);
    }

    /**
     * 分页查询视频
     * pageNum 当前页码
     * pageSize 每页展示数量
     */
    @GetMapping("/list")
    public Result selectlist(@RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "12") Integer pageSize,
            @RequestParam Integer mcId,
            @RequestParam(required = false)Integer state,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String scId) {
        PageInfo<Content> pageInfo = contentService.videoSelectPage(pageNum, pageSize, mcId, title, state,scId);
        return Result.success(pageInfo);
    }

    /**
     * 帖子查询
     */
    @GetMapping("/selectPost")
    public Result selectPost(@RequestParam Integer postid) {
        Post postInfo = contentService.selectPost(postid);
        return Result.success(postInfo);
    }

    /**
     * 帖子查询（所有）
     */
    @GetMapping("/selectAllPost")
    public Result selectAllPost() {
        List<Post> postList = contentService.selectAllPost();
        return Result.success(postList);
    }

    /**
     * 帖子点赞
     */
    @PutMapping("/likePost/{postid}")
    public Result likePost(@PathVariable Integer postid) {
        contentService.likePost(postid);
        return Result.success();
    }

    /**
     * 帖子取消点赞
     */
    @PutMapping("/unlikePost/{postid}")
    public Result unlikePost(@PathVariable Integer postid) {
        contentService.unlikePost(postid);
        return Result.success();
    }

    /**
     * 收藏
     */
    @PostMapping("/favorites")
    public Result favorites(@RequestBody Favorites favorites) {
        contentService.favorites(favorites);
        return Result.success();
    }

    /**
     * 查询收藏
     */
    @GetMapping("/selectFavorites")
    public Result selectFavorites(@RequestParam(required = false) Integer uid,
            @RequestParam(required = false) Integer vid) {
        List<Favorites> dbInfo = contentService.selectFavorites(uid, vid);
        return Result.success(dbInfo);
    }

    /**
     * 主页推荐（普通）
     */
    @GetMapping("/homeRecommendPost")
    public Result homeRecommend() {
        List<Post> postList = contentService.homePost();
        return Result.success(postList);
    }

    @GetMapping("/homeRecommendFiles")
    public Result homeRecommendFiles() {
        List<Content> contentList = contentService.homeFiles();
        return Result.success(contentList);
    }

    @GetMapping("/homeRecommendVideos")
    public Result homeRecommendVideos() {
        List<Content> contentList = contentService.homeVideos();
        return Result.success(contentList);
    }


    /**
     * 主页推荐（个性化）
     */
    @GetMapping("/personalizationRecommendationPost")
    public Result personalizationRecommendationPost(@RequestParam Integer uid) {
        List<Post> postList = contentService.personalizationRecommendationPost(uid);
        return Result.success(postList);
    }

    @GetMapping("/personalizationRecommendationVideo")
    public Result personalizationRecommendationFiles(@RequestParam Integer uid) {
        List<Content> contentList = contentService.personalizationRecommendationLessons(uid);
        return Result.success(contentList);
    }

    @GetMapping("/personalizationRecommendationFiles")
    public Result personalizationRecommendationVideo(@RequestParam Integer uid) {
        List<Content> contentList = contentService.personalizationRecommendationFiles(uid);
        return Result.success(contentList);
    }

    /**
     * 创建视频记录
     */
    @PostMapping("/create")
    public Result createContent(@RequestBody Content request) {
        contentService.insertContent(request);
        Integer latestVid = contentService.getLatestVidByUid(request.getUid());
        return Result.success(latestVid);
    }

    /**
     * 视频流
     */
    @GetMapping("/inlineVideo/{filename}")
    public void streamVideo(
            @PathVariable String filename,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        Path filePath = Paths.get("D:/curriculum design/files/files/", filename);
        if (!Files.exists(filePath)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "视频文件不存在");
            return;
        }

        // 设置基础响应头
        response.setHeader("Accept-Ranges", "bytes");
        response.setContentType(getMimeType(filename)); // 根据文件扩展名设置 MIME 类型

        long fileSize = Files.size(filePath);
        String rangeHeader = request.getHeader("Range");

        long start = 0;
        long end = fileSize - 1;
        int status = HttpServletResponse.SC_OK;

        // 处理 Range 请求
        if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
            String[] ranges = rangeHeader.substring(6).split("-");
            try {
                start = Long.parseLong(ranges[0]);
                if (ranges.length > 1) {
                    end = Long.parseLong(ranges[1]);
                } else {
                    end = fileSize - 1; // 处理类似 "bytes=1024-" 的请求
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Range Header");
                return;
            }

            // 验证范围有效性
            if (start < 0 || end >= fileSize || start > end) {
                response.setHeader("Content-Range", "bytes */" + fileSize);
                response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                return;
            }

            status = HttpServletResponse.SC_PARTIAL_CONTENT;
            response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + fileSize);
        }

        // 设置内容长度
        long contentLength = end - start + 1;
        response.setHeader("Content-Length", String.valueOf(contentLength));
        response.setStatus(status);

        // 流传输
        try (RandomAccessFile file = new RandomAccessFile(filePath.toFile(), "r");
             OutputStream os = response.getOutputStream()) {

            file.seek(start);
            byte[] buffer = new byte[8192 * 4]; // 增大缓冲区提升性能
            long remaining = contentLength;

            while (remaining > 0) {
                int readSize = (int) Math.min(buffer.length, remaining);
                int read = file.read(buffer, 0, readSize);
                if (read == -1) break;

                try {
                    os.write(buffer, 0, read);
                    remaining -= read;
                } catch (IOException e) {
                    // 客户端中断处理
                    if (e instanceof ClientAbortException) {
                        logger.debug("客户端中止下载: {}", filename);
                        return; // 直接退出，无需抛出异常
                    }
                    throw e; // 其他 IO 异常继续抛出
                }
            }
        } catch (ClientAbortException e) {
            logger.debug("客户端中止下载: {}", filename); // 静默记录
        } catch (IOException e) {
            logger.error("视频流传输失败: {}", filename, e);
            throw new CustomerException("视频流传输失败", e);
        }
    }

    // 获取 MIME 类型
    private String getMimeType(String filename) {
        String contentType = "application/octet-stream";
        try {
            contentType = Files.probeContentType(Paths.get(filename));
        } catch (IOException ignored) {
        }
        return contentType != null ? contentType : "video/mp4"; // 默认 fallback
    }




}