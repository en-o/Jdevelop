/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.detabes.spring.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Local DataSource configuration.
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "detabes.database")
public class DataBaseProperties {

    /**
     * 数据库类型 默认mysql
     */
    private String dialect="mysql";

    /**
     * 创库脚本 （如果没写 (根据spring.datasource.url上的库名创建,且为 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci ;)）
     *  脚本路径：META-INF/schema.mysql.sql
     *  eg. initScript=META-INF/schema.mysql.sql
     *  schema.mysql.sql中切记只能有建库语句,如果多个库一定要用分号隔开语句，不建议有建表语句
     */
    private String initScript;

    /**
     *  是否启用 默认true
     */
    private Boolean initEnable=true;

}
