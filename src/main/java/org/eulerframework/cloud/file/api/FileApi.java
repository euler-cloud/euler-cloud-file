package org.eulerframework.cloud.file.api;

import org.eulerframework.web.core.base.controller.ApiSupportWebController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping(path = "/")
public class FileApi extends ApiSupportWebController {

    @PostMapping
    public void uploadFile(MultipartFile file) {

    }

    @GetMapping("{fileId}")
    public void downloadFile(@PathVariable String fileId) throws IOException {
        File file = new File("");
        this.writeFile("", file);
    }
}
