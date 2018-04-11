<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="${model.namespace}">
    <select id="selectByCondition" resultMap="BaseResultMap" parameterType="java.util.Map">
        SELECT
        <include refid="Base_Column_List"/>
    <#if isBlob == 1>
        ,
        <include refid="Blob_Column_List"/>
    </#if>
        FROM ${model.tableName}
        WHERE 1 = 1
    ${conditionStr}
        <include refid="custom_condition"/>
        <if test="start != null and rows != null">
            LIMIT <#noparse>#{start}, #{rows}</#noparse>
        </if>
    </select>
    <select id="selectByConditionGetCount" resultType="java.lang.Integer" parameterType="java.util.Map">
        SELECT
        count(*)
        FROM ${model.tableName}
        WHERE 1 = 1
    ${conditionStr}
        <include refid="custom_condition"/>
        <if test="start != null and rows != null">
            LIMIT <#noparse>#{start}, #{rows}</#noparse>
        </if>
    </select>
    <delete id="deleteByIdsList" parameterType="java.util.Map">
        delete from ${model.tableName}
        where ${model.idColumn} in
        <foreach collection="list" item="item" index="index" open="(" close=")" separator=",">
        <#noparse>#{item}</#noparse>
        </foreach>
    </delete>
    <select id="selectByIdsList" resultMap="BaseResultMap" parameterType="java.util.Map">
        select
        <include refid="Base_Column_List"/>
    <#if isBlob == 1>
        ,
        <include refid="Blob_Column_List"/>
    </#if>
        from ${model.tableName}
        where ${model.idColumn} in
        <foreach collection="list" item="item" index="index" open="(" close=")" separator=",">
        <#noparse>#{item}</#noparse>
        </foreach>
    </select>
    <select id="insertBatch" parameterType="java.util.Map">
        insert into ${model.tableName}
        (${FieldStr}) values
        <foreach collection="list" item="item" index="index" separator=",">
            (${valueStr})
        </foreach>
    </select>
</mapper>