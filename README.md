# ik-analyzer
基于IKAnalyzer，进行分词功能的完善与修订，同步支持lucene各发布版本版本，并引入新的特性，原始代码地址 http://code.google.com/p/ik-analyzer/.
该fork版本由[GeoHey](https://geohey.com/)公司进行维护。

### 依赖软件 (Dependecies)
- Lucene
- testng(for unit test)
- JDBC
- DBUtils
- bonecp

### 目标 (Goals)
- 将词库管理由文本文件迁移到数据库(***)
- 完成Lucene 5.x的支持(***)
- 支持简单的实体识别(*)
- 支持地名解析(**)
