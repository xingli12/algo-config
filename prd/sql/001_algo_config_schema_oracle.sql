-- 算法配置管理系统数据库脚本
-- Oracle 12c+

-- ============================================
-- 1. 算法配置表 (algo_config)
-- ============================================
CREATE TABLE algo_config (
  id                 NUMBER(19) NOT NULL,
  recipe_id          VARCHAR2(64 CHAR) NOT NULL,
  eqp_type           VARCHAR2(64 CHAR) NOT NULL,
  eqp_id             VARCHAR2(64 CHAR),
  product_id         VARCHAR2(64 CHAR),
  case_name          VARCHAR2(128 CHAR),
  python_version     VARCHAR2(32 CHAR),
  algorithm_name     VARCHAR2(128 CHAR) NOT NULL,
  algorithm_version  VARCHAR2(64 CHAR) NOT NULL,
  algorithm_desc     CLOB,
  algorithm_file_path VARCHAR2(512 CHAR),
  algorithm_config_json  CLOB,
  aggregate_enabled  NUMBER(1) DEFAULT 0 NOT NULL,
  dc_config_json     CLOB,
  send_kov           NUMBER(1) DEFAULT 0 NOT NULL,
  send_lis           NUMBER(1) DEFAULT 0 NOT NULL,
  send_lithops       NUMBER(1) DEFAULT 0 NOT NULL,
  external_system_json CLOB,
  enabled            NUMBER(1) DEFAULT 1 NOT NULL,
  created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  created_by         VARCHAR2(64 CHAR),
  updated_by         VARCHAR2(64 CHAR),
  CONSTRAINT pk_algo_config PRIMARY KEY (id),
  CONSTRAINT uk_algo_config_recipe_id UNIQUE (recipe_id)
);

COMMENT ON TABLE algo_config IS '算法配置表';
COMMENT ON COLUMN algo_config.id IS '主键ID';
COMMENT ON COLUMN algo_config.recipe_id IS 'Recipe ID（唯一）';
COMMENT ON COLUMN algo_config.eqp_type IS '设备类型';
COMMENT ON COLUMN algo_config.eqp_id IS '设备ID';
COMMENT ON COLUMN algo_config.product_id IS '产品ID';
COMMENT ON COLUMN algo_config.case_name IS 'Case名称';
COMMENT ON COLUMN algo_config.python_version IS 'Python版本';
COMMENT ON COLUMN algo_config.algorithm_name IS '算法名称';
COMMENT ON COLUMN algo_config.algorithm_version IS '算法版本';
COMMENT ON COLUMN algo_config.algorithm_desc IS '算法描述';
COMMENT ON COLUMN algo_config.algorithm_file_path IS '算法文件路径';
COMMENT ON COLUMN algo_config.algorithm_config_json IS '算法配置JSON';
COMMENT ON COLUMN algo_config.aggregate_enabled IS '是否启用聚合（0否1是）';
COMMENT ON COLUMN algo_config.dc_config_json IS 'DC配置JSON';
COMMENT ON COLUMN algo_config.send_kov IS '是否发送KOV（0否1是）';
COMMENT ON COLUMN algo_config.send_lis IS '是否发送LIS（0否1是）';
COMMENT ON COLUMN algo_config.send_lithops IS '是否发送Lithops（0否1是）';
COMMENT ON COLUMN algo_config.external_system_json IS '外部系统配置JSON';
COMMENT ON COLUMN algo_config.enabled IS '启用状态（0停用1启用）';
COMMENT ON COLUMN algo_config.created_at IS '创建时间';
COMMENT ON COLUMN algo_config.updated_at IS '更新时间';
COMMENT ON COLUMN algo_config.created_by IS '创建人';
COMMENT ON COLUMN algo_config.updated_by IS '更新人';

-- 创建索引
CREATE INDEX idx_algo_config_algorithm_name ON algo_config(algorithm_name);
CREATE INDEX idx_algo_config_updated_at ON algo_config(updated_at);

-- 创建序列
CREATE SEQUENCE seq_algo_config START WITH 1 INCREMENT BY 1 NOCACHE;

-- 创建触发器（自增ID + 自动更新 updated_at）
CREATE OR REPLACE TRIGGER trg_algo_config_bi
BEFORE INSERT ON algo_config
FOR EACH ROW
BEGIN
  IF :NEW.id IS NULL THEN
    SELECT seq_algo_config.NEXTVAL INTO :NEW.id FROM DUAL;
  END IF;
  :NEW.created_at := CURRENT_TIMESTAMP;
  :NEW.updated_at := CURRENT_TIMESTAMP;
END;
/

CREATE OR REPLACE TRIGGER trg_algo_config_bu
BEFORE UPDATE ON algo_config
FOR EACH ROW
BEGIN
  :NEW.updated_at := CURRENT_TIMESTAMP;
END;
/

-- ============================================
-- 2. 镜像配置表 (image_config)
-- ============================================
CREATE TABLE image_config (
  id            NUMBER(19) NOT NULL,
  image_code    VARCHAR2(64 CHAR),
  image_name    VARCHAR2(128 CHAR) NOT NULL,
  image_tag     VARCHAR2(64 CHAR) NOT NULL,
  registry_url  VARCHAR2(256 CHAR) NOT NULL,
  image_size    VARCHAR2(32 CHAR),
  image_desc    CLOB,
  status        VARCHAR2(16 CHAR) DEFAULT 'ENABLED' NOT NULL,
  created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  CONSTRAINT pk_image_config PRIMARY KEY (id),
  CONSTRAINT uk_image_config_code UNIQUE (image_code),
  CONSTRAINT uk_image_config_name_tag UNIQUE (image_name, image_tag)
);

COMMENT ON TABLE image_config IS '镜像配置表';
COMMENT ON COLUMN image_config.id IS '主键ID';
COMMENT ON COLUMN image_config.image_code IS '镜像编码（如img_001）';
COMMENT ON COLUMN image_config.image_name IS '镜像名称';
COMMENT ON COLUMN image_config.image_tag IS '镜像标签';
COMMENT ON COLUMN image_config.registry_url IS '仓库地址';
COMMENT ON COLUMN image_config.image_size IS '镜像大小';
COMMENT ON COLUMN image_config.image_desc IS '镜像描述';
COMMENT ON COLUMN image_config.status IS '状态（ENABLED/DISABLED）';
COMMENT ON COLUMN image_config.created_at IS '创建时间';
COMMENT ON COLUMN image_config.updated_at IS '更新时间';

-- 创建索引
CREATE INDEX idx_image_config_status ON image_config(status);
CREATE INDEX idx_image_config_created_at ON image_config(created_at);

-- 创建序列
CREATE SEQUENCE seq_image_config START WITH 1 INCREMENT BY 1 NOCACHE;

-- 创建触发器
CREATE OR REPLACE TRIGGER trg_image_config_bi
BEFORE INSERT ON image_config
FOR EACH ROW
BEGIN
  IF :NEW.id IS NULL THEN
    SELECT seq_image_config.NEXTVAL INTO :NEW.id FROM DUAL;
  END IF;
  :NEW.created_at := CURRENT_TIMESTAMP;
  :NEW.updated_at := CURRENT_TIMESTAMP;
END;
/

CREATE OR REPLACE TRIGGER trg_image_config_bu
BEFORE UPDATE ON image_config
FOR EACH ROW
BEGIN
  :NEW.updated_at := CURRENT_TIMESTAMP;
END;
/

-- ============================================
-- 3. 镜像与算法关联关系表 (image_algo_rel)
-- ============================================
CREATE TABLE image_algo_rel (
  id            NUMBER(19) NOT NULL,
  image_id      NUMBER(19) NOT NULL,
  algorithm_name VARCHAR2(128 CHAR) NOT NULL,
  CONSTRAINT pk_image_algo_rel PRIMARY KEY (id),
  CONSTRAINT uk_image_algo_rel UNIQUE (image_id, algorithm_name),
  CONSTRAINT fk_image_algo_rel_image_id FOREIGN KEY (image_id)
    REFERENCES image_config(id) ON DELETE CASCADE
);

COMMENT ON TABLE image_algo_rel IS '镜像与算法关联关系表';
COMMENT ON COLUMN image_algo_rel.id IS '主键ID';
COMMENT ON COLUMN image_algo_rel.image_id IS '镜像ID';
COMMENT ON COLUMN image_algo_rel.algorithm_name IS '算法名称';

-- 创建索引
CREATE INDEX idx_image_algo_rel_algorithm_name ON image_algo_rel(algorithm_name);

-- 创建序列
CREATE SEQUENCE seq_image_algo_rel START WITH 1 INCREMENT BY 1 NOCACHE;

-- 创建触发器
CREATE OR REPLACE TRIGGER trg_image_algo_rel_bi
BEFORE INSERT ON image_algo_rel
FOR EACH ROW
BEGIN
  IF :NEW.id IS NULL THEN
    SELECT seq_image_algo_rel.NEXTVAL INTO :NEW.id FROM DUAL;
  END IF;
END;
/

-- ============================================
-- 4. 资源配置表 (resource_config)
-- ============================================
CREATE TABLE resource_config (
  id             NUMBER(19) NOT NULL,
  algorithm_name VARCHAR2(128 CHAR) NOT NULL,
  image_id       NUMBER(19) NOT NULL,
  image_version  VARCHAR2(64 CHAR),
  current_version VARCHAR2(64 CHAR),
  nas_id         VARCHAR2(64 CHAR),
  cpu_limit      VARCHAR2(32 CHAR),
  mem_limit      VARCHAR2(32 CHAR),
  cpu_request    VARCHAR2(32 CHAR),
  mem_request    VARCHAR2(32 CHAR),
  status         VARCHAR2(16 CHAR) DEFAULT 'ACTIVE' NOT NULL,
  updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  CONSTRAINT pk_resource_config PRIMARY KEY (id),
  CONSTRAINT fk_resource_config_image_id FOREIGN KEY (image_id)
    REFERENCES image_config(id) ON DELETE RESTRICT
);

COMMENT ON TABLE resource_config IS '资源配置表';
COMMENT ON COLUMN resource_config.id IS '主键ID';
COMMENT ON COLUMN resource_config.algorithm_name IS '算法名称';
COMMENT ON COLUMN resource_config.image_id IS '镜像ID';
COMMENT ON COLUMN resource_config.image_version IS '镜像版本';
COMMENT ON COLUMN resource_config.current_version IS '当前版本';
COMMENT ON COLUMN resource_config.nas_id IS 'NAS ID';
COMMENT ON COLUMN resource_config.cpu_limit IS 'CPU限制（如2000m）';
COMMENT ON COLUMN resource_config.mem_limit IS '内存限制（如4Gi）';
COMMENT ON COLUMN resource_config.cpu_request IS 'CPU请求（如1000m）';
COMMENT ON COLUMN resource_config.mem_request IS '内存请求（如2Gi）';
COMMENT ON COLUMN resource_config.status IS '状态（ACTIVE/INACTIVE）';
COMMENT ON COLUMN resource_config.updated_at IS '更新时间';

-- 创建索引
CREATE INDEX idx_resource_config_algorithm_name ON resource_config(algorithm_name);
CREATE INDEX idx_resource_config_status ON resource_config(status);
CREATE INDEX idx_resource_config_updated_at ON resource_config(updated_at);

-- 创建序列
CREATE SEQUENCE seq_resource_config START WITH 1 INCREMENT BY 1 NOCACHE;

-- 创建触发器
CREATE OR REPLACE TRIGGER trg_resource_config_bi
BEFORE INSERT ON resource_config
FOR EACH ROW
BEGIN
  IF :NEW.id IS NULL THEN
    SELECT seq_resource_config.NEXTVAL INTO :NEW.id FROM DUAL;
  END IF;
  :NEW.updated_at := CURRENT_TIMESTAMP;
END;
/

CREATE OR REPLACE TRIGGER trg_resource_config_bu
BEFORE UPDATE ON resource_config
FOR EACH ROW
BEGIN
  :NEW.updated_at := CURRENT_TIMESTAMP;
END;
/

-- ============================================
-- 5. 操作日志表 (operation_log)
-- ============================================
CREATE TABLE operation_log (
  id        NUMBER(19) NOT NULL,
  module    VARCHAR2(32 CHAR) NOT NULL,
  operation VARCHAR2(32 CHAR) NOT NULL,
  biz_id    VARCHAR2(64 CHAR),
  operator  VARCHAR2(64 CHAR),
  content   CLOB,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  CONSTRAINT pk_operation_log PRIMARY KEY (id)
);

COMMENT ON TABLE operation_log IS '操作日志表';
COMMENT ON COLUMN operation_log.id IS '主键ID';
COMMENT ON COLUMN operation_log.module IS '模块（ALGO/RESOURCE/IMAGE）';
COMMENT ON COLUMN operation_log.operation IS '操作类型（CREATE/UPDATE/DELETE/ENABLE/DISABLE/DEPLOY/ROLLBACK）';
COMMENT ON COLUMN operation_log.biz_id IS '业务ID';
COMMENT ON COLUMN operation_log.operator IS '操作人';
COMMENT ON COLUMN operation_log.content IS '操作内容（JSON格式）';
COMMENT ON COLUMN operation_log.created_at IS '创建时间';

-- 创建索引
CREATE INDEX idx_operation_log_module_operation ON operation_log(module, operation);
CREATE INDEX idx_operation_log_created_at ON operation_log(created_at);

-- 创建序列
CREATE SEQUENCE seq_operation_log START WITH 1 INCREMENT BY 1 NOCACHE;

-- 创建触发器
CREATE OR REPLACE TRIGGER trg_operation_log_bi
BEFORE INSERT ON operation_log
FOR EACH ROW
BEGIN
  IF :NEW.id IS NULL THEN
    SELECT seq_operation_log.NEXTVAL INTO :NEW.id FROM DUAL;
  END IF;
END;
/

-- ============================================
-- 初始化数据（可选）
-- ============================================

-- Python 版本字典
-- INSERT INTO python_version_dict (version, description) VALUES ('3.8', 'Python 3.8');
-- INSERT INTO python_version_dict (version, description) VALUES ('3.9', 'Python 3.9');
-- INSERT INTO python_version_dict (version, description) VALUES ('3.10', 'Python 3.10');
-- INSERT INTO python_version_dict (version, description) VALUES ('3.11', 'Python 3.11');

COMMIT;
