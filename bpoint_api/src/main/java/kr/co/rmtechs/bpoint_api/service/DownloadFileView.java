package kr.co.rmtechs.bpoint_api.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

@Component("downloadFileView")
public class DownloadFileView extends AbstractView{
	
	public DownloadFileView() {
        // TODO Auto-generated constructor stub
        setContentType("application/download; ccharset=utf-8");
    }
    
    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // TODO Auto-generated method stub
        File file = (File) model.get("downloadFile");
        String downloadFileName = (String) model.get("downloadFileName");
        
        response.setContentType(getContentType());
        response.setContentLength((int)file.length());
        setResponse(request,response,downloadFileName);
        response.setHeader("Content-Transfer-Encoding", "binary");
         
        OutputStream out = response.getOutputStream();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            FileCopyUtils.copy(fis, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) { try { fis.close(); } catch (Exception e2) {}}
        }
        out.flush();
    }
    
    private void setResponse(HttpServletRequest request, HttpServletResponse response, String fileName) throws UnsupportedEncodingException{
        String userAgent = request.getHeader("User-Agent");
        if (userAgent.indexOf("MSIE 5.5") > -1) { // MS IE 5.5 이하
            response.setHeader("Content-Disposition", "filename=" + URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "\\ ") + ";");
        } else if (userAgent.indexOf("MSIE") > -1) { // MS IE (보통은 6.x 이상 가정)
            response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "\\ ") + ";");
        } else if (userAgent.indexOf("Trident") > -1) { // MS IE 11
            response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "\\ ") + ";");
        } else { // 모질라나 오페라
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("euc-kr"), "latin1").replaceAll("\\+", "\\ ") + ";");
        }
    }

}
