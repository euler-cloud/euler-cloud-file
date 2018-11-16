CREATE DATABASE euler_cloud_file
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_bin;

USE euler_cloud_file;

-- 上传文件信息表
CREATE TABLE archived_file
(
  id                   VARCHAR(36)   NOT NULL
  COMMENT '上传文件ID',
  archived_filename    VARCHAR(100)  NOT NULL
  COMMENT '上传后文件名',
  archived_path_suffix VARCHAR(255) COMMENT '上传保存路径前缀',
  extension            VARCHAR(255) COMMENT '文件扩展名',
  file_byte_size       BIGINT        NOT NULL
  COMMENT '文件大小',
  md5                  VARCHAR(255)  NOT NULL
  COMMENT '文件MD5',
  original_filename    VARCHAR(2000) NOT NULL
  COMMENT '原始文件名',
  uploaded_at          DATETIME(3)   NOT NULL
  COMMENT '上传时刻',
  uploaded_by          VARCHAR(255)  NOT NULL
  COMMENT '上传用户ID',
  PRIMARY KEY (id),
  CONSTRAINT UK_ARCHIVED_FILE_AF UNIQUE (archived_filename)
)
  ENGINE = InnoDB
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_bin
  COMMENT = '上传文件记录表';