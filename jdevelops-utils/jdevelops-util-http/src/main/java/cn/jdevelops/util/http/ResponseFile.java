package cn.jdevelops.util.http;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 文件下载头设置
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/8/11 10:53
 */
public class ResponseFile {




    /**
     * 设置xlsx下载头
     * ps： 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
     *
     * @param response HttpServletResponse
     * @param title    表名
     * @return HttpServletResponse
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     */
    public static HttpServletResponse xlsxResponse(HttpServletResponse response, String title) throws UnsupportedEncodingException {
        return customResponse(response, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", title, ".xlsx");
    }


    /**
     * 设置json下载头
     *
     * @param response HttpServletResponse
     * @param title    json名
     * @return HttpServletResponse
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     */
    public static HttpServletResponse jsonResponse(HttpServletResponse response, String title) throws UnsupportedEncodingException {
        // 设置响应头，指定响应类型为JSON文件
        return customResponse(response, "application/json", title, ".json");
    }


    /**
     * 设置zip下载头
     *
     * @param response HttpServletResponse
     * @param title    json名
     * @return HttpServletResponse
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     */
    public static HttpServletResponse zipResponse(HttpServletResponse response, String title) throws UnsupportedEncodingException {
        // 设置响应头，指定响应类型为zip文件
        return customResponse(response, "application/zip", title, ".zip");
    }

    /**
     * 设置csv下载头
     *
     * @param response HttpServletResponse
     * @param title    json名
     * @return HttpServletResponse
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     */
    public static HttpServletResponse csvResponse(HttpServletResponse response, String title) throws UnsupportedEncodingException {
        // 设置响应头，指定响应类型为csv文件
        return customResponse(response, "text/csv;charset=utf-8", title, ".csv");
    }


    /**
     * 自定义文件下载头
     *
     * @param response HttpServletResponse
     * @param file     文件
     * @return HttpServletResponse
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     */
    public static HttpServletResponse fileResponse(HttpServletResponse response, File file) throws IOException {
        String contentType = Files.probeContentType(Paths.get(file.getAbsolutePath())) + ";charset=utf-8";
        HttpServletResponse fileResponseBase = customResponse(response, contentType, file.getName());
        //设置文件大小
        fileResponseBase.setHeader("Content-Length", String.valueOf(file.length()));
        return fileResponseBase;
    }

    /**
     * 自定义文件下载头
     *
     * @param response    HttpServletResponse
     * @param contentType response.setContentType
     * @param title       文件名
     * @param fileFormat  文件类型
     * @return HttpServletResponse
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     */
    public static HttpServletResponse customResponse(HttpServletResponse response, String contentType, String title, String fileFormat) throws UnsupportedEncodingException {
        return customResponse(response, contentType, title + fileFormat);
    }


    /**
     * 自定义文件下载头
     *
     * @param response    HttpServletResponse
     * @param contentType response.setContentType
     * @param filename    文件名 (title+fileFormat)
     * @return HttpServletResponse
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     */
    public static HttpServletResponse customResponse(HttpServletResponse response, String contentType, String filename) throws UnsupportedEncodingException {
        // 设置响应头，指定响应类型
        response.reset();//避免空行
        response.setContentType(contentType);
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        String fileName = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        return response;
    }
}
