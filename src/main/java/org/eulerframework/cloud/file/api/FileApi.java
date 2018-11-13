package org.eulerframework.cloud.file.api;

import org.eulerframework.cloud.file.dto.ArchivedFileDTO;
import org.eulerframework.cloud.file.entity.ArchivedFile;
import org.eulerframework.cloud.file.exception.ArchivedFileNotFoundException;
import org.eulerframework.cloud.file.exception.FileArchiveException;
import org.eulerframework.cloud.file.service.ArchivedFileService;
import org.eulerframework.common.util.io.file.FileUtils;
import org.eulerframework.web.core.base.controller.ApiSupportWebController;
import org.eulerframework.web.util.ServletUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping(path = "/")
public class FileApi extends ApiSupportWebController {

    @Autowired
    private ArchivedFileService archivedFileService;

    @PostMapping
    public ArchivedFile uploadFile(MultipartFile file) throws FileArchiveException {
        return this.archivedFileService.saveMultipartFile(file);
    }

    @GetMapping("{archivedFileId}")
    public void downloadFile(@PathVariable String archivedFileId) throws IOException {
        System.out.println(ServletUtils.getRequest().getRequestURI());
        String extensions = FileUtils.extractFileExtension(archivedFileId);
        archivedFileId = FileUtils.extractFileNameWithoutExtension(archivedFileId);
        ArchivedFileDTO archivedFileDTO = this.archivedFileService.findArchivedFile(archivedFileId, extensions);
        try {
            this.writeFile(archivedFileDTO.getOriginalFileName(), archivedFileDTO.getFile());
        } catch (FileNotFoundException e) {
            throw new ArchivedFileNotFoundException(archivedFileId, extensions);
        }
    }
}
