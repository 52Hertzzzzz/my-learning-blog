package com.framework.filter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/***
 * Body输入流复制装饰器
 */
public class BodyDuplicateWrapper extends HttpServletRequestWrapper {

    private byte[] bodyCache = null;

    public BodyDuplicateWrapper(HttpServletRequest request) {
        super(request);
        try {
            ServletInputStream inputStream = request.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) > -1) {
                //3参数:     byte b[]              int off                    int len
                //        buffer字节数组，buffer数组写入位置（偏移量），写入字节数（一般等于读取字节数）
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();

            //复制入本地变量
            bodyCache = outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * 重复获取输入流方法
     * @return
     * @throws IOException
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bodyCache);
        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
    }

    /***
     * 重复获取reader方法
     * @return
     * @throws IOException
     */
    @Override
    public BufferedReader getReader() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bodyCache);
        return new BufferedReader(new InputStreamReader(inputStream));
    }
}
