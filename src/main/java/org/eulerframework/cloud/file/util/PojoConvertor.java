/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.eulerframework.cloud.file.util;

import org.eulerframework.cloud.file.entity.ArchivedFile;
import org.eulerframework.cloud.file.vo.ArchivedFileVO;

public class PojoConvertor {

    public static ArchivedFileVO toVO(ArchivedFile archivedFile) {
        if(archivedFile == null) {
            return null;
        }

        ArchivedFileVO archivedFileVO = new ArchivedFileVO();
        archivedFileVO.setExtension(archivedFile.getExtension());
        archivedFileVO.setFileByteSize(archivedFile.getFileByteSize());
        archivedFileVO.setId(archivedFile.getId());
        archivedFileVO.setMd5(archivedFile.getMd5());
        archivedFileVO.setOriginalFilename(archivedFile.getOriginalFilename());
        archivedFileVO.setUploadedAt(archivedFile.getUploadedAt());
        archivedFileVO.setUploadedBy(archivedFile.getUploadedBy());
        return archivedFileVO;
    }
}
