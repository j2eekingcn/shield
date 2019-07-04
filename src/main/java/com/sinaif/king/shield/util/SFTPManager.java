package com.sinaif.king.shield.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;


/**
 * @author : dlx
 * @version : 1.0 Create Date : 2017年12月26日 上午11:17:43
 * @Description : FTP下载
 * @Copyright : Sinaif Software Co.,Ltd.Rights Reserved
 * @Company : 海南新浪爱问普惠科技有限公司
 */
public class SFTPManager {

    private ChannelSftp sftp = null;
    private Session sshSession = null;

    protected Logger logger = LoggerFactory.getLogger(SFTPManager.class);

    /*
     * 连接
     */
    public void connect(String username, String password, String host, int port) throws JSchException {
        try {

            JSch jsch = new JSch();

            jsch.getSession(username, host, port);
            sshSession = jsch.getSession(username, host, port);

            System.out.println("Session created.");

            sshSession.setPassword(password);

            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();

            System.out.println("Session connected.");
            System.out.println("Opening Channel.");

            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
            logger.info("Connected to " + host + ".");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    /*
     * 关闭
     */
    public void disconnect() {
        if (this.sftp != null) {
            if (this.sftp.isConnected()) {
                this.sftp.disconnect();
                System.out.println("sftp is closed already");
            }
        }

        if (this.sshSession != null) {
            if (this.sshSession.isConnected()) {
                this.sshSession.disconnect();
                System.out.println("sshSession is closed already");
            }
        }
    }

    /**
     * 将字符串按照指定的字符编码上传到sftp
     *
     * @param directory    上传到sftp目录
     * @param sftpFileName 文件在sftp端的命名
     * @param dataStr      待上传的数据
     * @param charsetName  sftp上的文件，按该字符编码保存
     * @throws UnsupportedEncodingException
     * @throws SftpException
     * @throws Exception
     */
    public void upload(String directory, String sftpFileName, String dataStr, String charsetName) throws UnsupportedEncodingException, SftpException {
        upload(directory, sftpFileName, new ByteArrayInputStream(dataStr.getBytes(charsetName)));
    }

    /**
     * 上传单个文件
     *
     * @param directory  上传到sftp目录
     * @param uploadFile 要上传的文件,包括路径
     * @throws FileNotFoundException
     * @throws SftpException
     * @throws Exception
     */
    public void upload(String directory, String sftpFileName, String uploadFile) throws FileNotFoundException, SftpException {
        File file = new File(uploadFile);
        upload(directory, sftpFileName, new FileInputStream(file));
    }

    /**
     * 将输入流的数据上传到sftp作为文件
     *
     * @param directory    上传到该目录
     * @param sftpFileName sftp端文件名
     * @param input           输入流
     * @throws SftpException
     * @throws Exception
     */
    public void upload(String directory, String sftpFileName, InputStream input) throws SftpException {
        try {
            sftp.cd(directory);
            sftp.put(input, sftpFileName);
            logger.info("file:{} is upload successful" , sftpFileName);
        } catch (SftpException e) {
            logger.warn("directory is not exist");
            logger.error(e.getMessage(), e);
            sftp.mkdir(directory);
            sftp.cd(directory);
        } finally {
            this.disconnect();
        }
    }

    /*
     * 批量下载
     */
    public boolean batchDownLoadFile(String remotPath, String localPath, String fileFormat, boolean del) {
        try {
            Vector v = listFiles(remotPath);
            if (v.size() > 0) {

                Iterator it = v.iterator();
                while (it.hasNext()) {
                    LsEntry entry = (LsEntry) it.next();
                    String filename = entry.getFilename();
                    SftpATTRS attrs = entry.getAttrs();
                    if (!attrs.isDir()) {
                        if (fileFormat != null && !"".equals(fileFormat.trim())) {
                            if (filename.startsWith(fileFormat)) {
                                if (this.downloadSingleFile(remotPath, filename, localPath, filename) && del) {
                                    deleteSFTP(remotPath, filename);
                                }
                            }
                        } else {
                            if (this.downloadSingleFile(remotPath, filename, localPath, filename) && del) {
                                deleteSFTP(remotPath, filename);
                            }
                        }
                    }
                }
            }
        } catch (SftpException e) {
            e.printStackTrace();
        } finally {
            this.disconnect();
        }
        return false;
    }

    public boolean removeToTargetPath(String remotePath) {
        try {
            sftp.cd(remotePath);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /*
     * 一次下载单个文件
     */
    public boolean downloadSingleFile(String remotePath, String remoteFileName, String localPath,
                                      String localFileName) {
        try {
            logger.info(String.format("FTP下载：remotePath:%s,remoteFileName:%s,localPath:%s,localFileName:%s", remotePath,
                    remoteFileName, localPath, localFileName));
            if (!"".equals(remotePath)) {
                sftp.cd(remotePath);
            }
            File file = new File(localPath + localFileName);
            if (file.exists()) {
                logger.info(String.format("FTP下载，文件已经存在：%s", (localPath + localFileName)));
            }
            mkdirs(localPath + localFileName);
            sftp.get(remoteFileName, new FileOutputStream(file));
            return true;// 下载成功
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(String.format("程序异常：FTP下载"), e);
        }
        return false;// 下载失败
    }

    /*
     * 删除文件
     */
    public boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        }

        if (!file.isFile()) {
            return false;
        }

        return file.delete();
    }

    /*
     * 创建目录
     */
    public boolean createDir(String createpath) {
        try {
            if (isDirExist(createpath)) {
                this.sftp.cd(createpath);
                return true;
            }
            String pathArry[] = createpath.split("/");
            StringBuffer filePath = new StringBuffer("/");
            for (String path : pathArry) {
                if (path.equals("")) {
                    continue;
                }
                filePath.append(path + "/");
                if (isDirExist(filePath.toString())) {
                    sftp.cd(filePath.toString());
                } else {
                    // 建立目录
                    sftp.mkdir(filePath.toString());
                    // 进入并设置为当前目录
                    sftp.cd(filePath.toString());
                }

            }
            this.sftp.cd(createpath);
            return true;
        } catch (SftpException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断目录是否存在
     */
    public boolean isDirExist(String directory) {
        boolean isDirExistFlag = false;
        try {
            SftpATTRS sftpATTRS = sftp.lstat(directory);
            isDirExistFlag = true;
            return sftpATTRS.isDir();
        } catch (Exception e) {
            if (e.getMessage().toLowerCase().equals("no such file")) {
                isDirExistFlag = false;
            }
        }
        return isDirExistFlag;
    }

    /**
     * 删除stfp文件
     */
    public void deleteSFTP(String directory, String deleteFile) {
        try {
            sftp.cd(directory);
            sftp.rm(deleteFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 如果目录不存在就创建目录
     *
     * @param path
     */
    public void mkdirs(String path) {
        File f = new File(path);

        String fs = f.getParent();

        f = new File(fs);

        if (!f.exists()) {
            f.mkdirs();
        }
    }

    /*
     * 列出目录下的文件
     */
    public Vector listFiles(String directory) throws SftpException {
        return sftp.ls(directory);
    }

    public static void main(String[] args) throws SftpException, IOException {
        SFTPManager sftpManager = new SFTPManager();
        try {
            File f = new File("E:\\1.jpg");
            String fileName = f.getName();
            String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
            System.out.println(prefix);
//			sftpManager.connect("810002","cib@810002","117.144.184.152",22);  
//			String path = DateUtils.dateToString(new Date(),DateUtils.DATE_FORMAT_12);
//			sftpManager.createDir("/facecompare/"+path);
//			sftpManager.upload("/facecompare/"+path,"QDSINA"+HNUtil.getId()+"_0199_01.jpg", "E:\\d3ebf22d8d938.jpg");

//			sftpManager.createDir("/images/"+path);
//			sftpManager.upload("/images/"+path,"QDSINA152818131075359903.gz", "E:\\QDSINA152818131075359903.gz");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sftpManager.disconnect();
        }
    }

}
