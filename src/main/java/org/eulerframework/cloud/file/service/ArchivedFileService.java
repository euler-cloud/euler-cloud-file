package org.eulerframework.cloud.file.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.eulerframework.cloud.config.EulerCloudConfig;
import org.eulerframework.cloud.file.dto.ArchivedFileDTO;
import org.eulerframework.cloud.file.entity.ArchivedFile;
import org.eulerframework.cloud.file.exception.ArchivedFileNotFoundException;
import org.eulerframework.cloud.file.repository.ArchivedFileRepository;
import org.eulerframework.common.base.log.LogSupport;
import org.eulerframework.common.util.Assert;
import org.eulerframework.common.util.CommonUtils;
import org.eulerframework.common.util.DateUtils;
import org.eulerframework.common.util.StringUtils;
import org.eulerframework.common.util.io.file.FileUtils;
import org.eulerframework.common.util.io.file.SimpleFileIOUtils;
import org.eulerframework.web.core.base.response.ErrorResponse;
import org.eulerframework.web.core.exception.web.SystemWebError;
import org.eulerframework.web.core.exception.web.WebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

@Service
public class ArchivedFileService extends LogSupport {

    @Autowired
    private EulerCloudConfig eulerCloudConfig;

    @Autowired
    private ArchivedFileRepository archivedFileRepository;

    private ArchivedFile saveFileInfo(String originalFilename, String archivedPathSuffix, File archivedFile)
            throws IOException {
        InputStream inputStream = new FileInputStream(archivedFile);
        String md5 = DigestUtils.md5Hex(inputStream);
        long fileSize = archivedFile.length();
        String archivedFilename = archivedFile.getName();

        ArchivedFile af = new ArchivedFile();

        af.setOriginalFilename(originalFilename);
        af.setArchivedPathSuffix(archivedPathSuffix);
        af.setArchivedFilename(archivedFilename);
        af.setExtension(FileUtils.extractFileExtension(originalFilename));
        af.setFileByteSize(fileSize);
        af.setMd5(md5);
        af.setUploadedAt(new Date());
        af.setUploadedUserId("anonymousUser");//TODO:记录上传者ID

        this.archivedFileRepository.save(af);

        return af;
    }

    private String getFileArchivePath() {
        String runtimePath = eulerCloudConfig.getRuntimePath();
        return CommonUtils.convertDirToUnixFormat(runtimePath, false) + "/archived/file";
    }

    public ArchivedFile saveMultipartFile(MultipartFile multipartFile) throws IOException {
        String archiveFilePath = this.getFileArchivePath();
        String archivedPathSuffix = DateUtils.formatDate(new Date(), "yyyy-MM-dd");

        String originalFilename = multipartFile.getOriginalFilename();
        String targetFilename = UUID.randomUUID().toString();

        File targetFile = new File(archiveFilePath + "/" + archivedPathSuffix, targetFilename);

        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }

        try {
            multipartFile.transferTo(targetFile);

            this.logger.info("已保存文件: " + targetFile.getPath());

            return this.saveFileInfo(originalFilename, archivedPathSuffix, targetFile);
        } catch (IllegalStateException | IOException e) {
            if (targetFile.exists())
                SimpleFileIOUtils.deleteFile(targetFile);

            throw e;
        }
    }

    public ArchivedFile findArchivedFile(String archivedFileId, String extensions) {
        Assert.isFalse(StringUtils.isNull(archivedFileId), "archivedFileId can not be empty");

        ArchivedFile archivedFile = this.archivedFileRepository.findArchivedFileById(archivedFileId);

        if(archivedFile == null) {
            throw new ArchivedFileNotFoundException(archivedFileId, extensions);
        }

        if(StringUtils.hasText(extensions) && !extensions.equalsIgnoreCase(archivedFile.getExtension())) {
            throw new ArchivedFileNotFoundException(archivedFileId, extensions);
        }

        return archivedFile;
    }

    public ArchivedFileDTO findArchivedFileDTO(String archivedFileId, String extensions){
        ArchivedFile archivedFile = this.findArchivedFile(archivedFileId, extensions);

        String archivedFilePath = this.getFileArchivePath();

        if(archivedFile.getArchivedPathSuffix() != null)
            archivedFilePath = archivedFilePath + "/" + archivedFile.getArchivedPathSuffix();

        File file = new File(archivedFilePath, archivedFile.getArchivedFilename());

        ArchivedFileDTO archivedFileDTO = new ArchivedFileDTO();
        archivedFileDTO.setFile(file);
        archivedFileDTO.setOriginalFileName(archivedFile.getOriginalFilename());
        return archivedFileDTO;
    }

    /**
     * 用于在程序发生{@link AccessDeniedException}异常时统一返回错误信息
     *
     * @return 包含错误信息的Ajax响应体
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(FileNotFoundException.class)
    public Object fileNotFoundException(FileNotFoundException e) {
        return new ErrorResponse(new WebException(e.getMessage(), SystemWebError.ACCESS_DENIED, e));
    }
}
