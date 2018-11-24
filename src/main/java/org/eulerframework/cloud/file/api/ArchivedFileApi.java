package org.eulerframework.cloud.file.api;

import org.eulerframework.cloud.file.dto.ArchivedFileDTO;
import org.eulerframework.cloud.file.entity.ArchivedFile;
import org.eulerframework.cloud.file.exception.ArchivedFileNotFoundException;
import org.eulerframework.cloud.file.exception.FileArchiveException;
import org.eulerframework.cloud.file.service.ArchivedFileService;
import org.eulerframework.cloud.file.util.PojoConvertor;
import org.eulerframework.cloud.file.vo.ArchivedFileVO;
import org.eulerframework.cloud.security.EulerCloudUserContext;
import org.eulerframework.common.util.io.file.FileUtils;
import org.eulerframework.web.core.base.controller.ApiSupportWebController;
import org.eulerframework.web.core.base.response.ErrorResponse;
import org.eulerframework.web.core.exception.web.SystemWebError;
import org.eulerframework.web.core.exception.web.WebException;
import org.eulerframework.web.util.ServletUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping(path = "archived")
public class ArchivedFileApi extends ApiSupportWebController {

    @Autowired
    private ArchivedFileService archivedFileService;

    /**
     * 归档一个文件
     * @param file 待归档的文件
     * @return 归档文件信息
     * @throws IOException IO异常
     */
    @PostMapping
    public ArchivedFileVO uploadFile(@RequestParam MultipartFile file) throws IOException {
        String currentUserId = EulerCloudUserContext.getCurrentUserId();
        return PojoConvertor.toVO(this.archivedFileService.saveMultipartFile(currentUserId, file));
    }

    /**
     * 下载归档文件
     * @param archivedFileId 归档文件ID, 如带文件扩展名, 会检查文件扩展名是否匹配
     * @throws IOException 除文件不存在之外的其他IO异常
     */
    @GetMapping("{archivedFileId}")
    public void downloadFile(@PathVariable String archivedFileId) throws IOException {
        String extensions = FileUtils.extractFileExtension(archivedFileId);
        archivedFileId = FileUtils.extractFileNameWithoutExtension(archivedFileId);
        ArchivedFileDTO archivedFileDTO = this.archivedFileService.findArchivedFileDTO(archivedFileId, extensions);
        try {
            this.writeFile(archivedFileDTO.getOriginalFileName(), archivedFileDTO.getFile());
        } catch (FileNotFoundException e) {
            throw new ArchivedFileNotFoundException(archivedFileId, extensions);
        }
    }

    /**
     * 获取归档文件信息
     * @param archivedFileId 归档文件ID, 如带文件扩展名, 会检查文件扩展名是否匹配
     */
    @GetMapping("info/{archivedFileId}")
    public ArchivedFileVO findArchivedFile(@PathVariable String archivedFileId) {
        String extensions = FileUtils.extractFileExtension(archivedFileId);
        archivedFileId = FileUtils.extractFileNameWithoutExtension(archivedFileId);
        return PojoConvertor.toVO(this.archivedFileService.findArchivedFile(archivedFileId, extensions));
    }

    /**
     * 用于在程序发生{@link ArchivedFileNotFoundException}异常时统一返回错误信息
     *
     * @return 包含错误信息的Ajax响应体
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ArchivedFileNotFoundException.class)
    public Object archivedFileNotFoundException(ArchivedFileNotFoundException e) {
        return new ErrorResponse(new WebException(e.getMessage(), SystemWebError.RESOURCE_NOT_FOUND, e));
    }
}
