package com.atguigu.srb.core.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.srb.core.listener.ExcelDictDTOListener;
import com.atguigu.srb.core.pojo.dto.ExcelDictDTO;
import com.atguigu.srb.core.pojo.entity.Dict;
import com.atguigu.srb.core.mapper.DictMapper;
import com.atguigu.srb.core.service.DictService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 数据字典 服务实现类
 * </p>
 *
 * @author Zlw
 * @since 2023-03-15
 */
@Service
@Slf4j
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Resource
    //RedisConfig配置类@Bean注入方法返回的是RedisTemplate<String,Object>
    private RedisTemplate<String,Object> redisTemplate;

    //实现业务层I/O流数据的导入方法
    @Override
    @Transactional(rollbackFor = {Exception.class}) //添加事务回滚
    public void importData(InputStream inputStream) {
        //这儿的baseMapper ---> dictMapper(DictServiceImpl下的dictMapper默认@Resource)
        //new ExcelDictDTOListener(baseMapper)) ---> 插入数据库中
        EasyExcel.read(inputStream, ExcelDictDTO.class,new ExcelDictDTOListener(baseMapper)).sheet().doRead();
        log.info("importData finished");
    }

    /*
    * Excel导出函数
    * */
    @Override
    public List<ExcelDictDTO> listDictData() {
        List<Dict> dictList = baseMapper.selectList(null);
        List<ExcelDictDTO> excelDictDTOList = new ArrayList<>(dictList.size());
        dictList.forEach(dict->{
            ExcelDictDTO excelDictDTO = new ExcelDictDTO();
            //将dictList中的元素类型(Dict)转为ExcelDictDTO数据类型
            BeanUtils.copyProperties(dict, excelDictDTO);
            excelDictDTOList.add(excelDictDTO);
        });

        return excelDictDTOList;
    }

    /*
    * 借款信息填写的下拉框(根据父类dict_code)
    * */
    @Override
    public List<Dict> findByDictCode(String dictCode) {
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("dict_code", dictCode);
        Dict dict = baseMapper.selectOne(dictQueryWrapper);
        return this.listByParentId(dict.getId());
    }

    /*
    * 根据父类的parent_id和子类的值value来得到name
    * */
    @Override
    public String getNameByParentDictCodeAndValue(String dictCode, Integer value) {

        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<Dict>();
        dictQueryWrapper.eq("dict_code", dictCode);
        Dict parentDict = baseMapper.selectOne(dictQueryWrapper);

        if(parentDict == null) {
            return "";
        }

        dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper
                .eq("parent_id", parentDict.getId())
                .eq("value", value);
        Dict dict = baseMapper.selectOne(dictQueryWrapper);

        if(dict == null) {
            return "";
        }

        return dict.getName();
    }

    /*
    * 数据字典展示
    * */
    @Override
    public List<Dict> listByParentId(Long parentId) {

        List<Dict> dictList = null;
        //先从Redis缓存中拿值
        try {
            dictList = (List<Dict>) redisTemplate.opsForValue().get("srb:core:dictList:"+parentId);
            if(dictList != null){
                log.info("从Redis中取值");
                return dictList;
            }
        } catch (Exception e) {
            log.error("redis服务器异常1："+ ExceptionUtils.getStackTrace(e));
        }

        //如果Redis缓存中没有值则从数据库中拿值
        log.info("从数据库中拿值");
        dictList = baseMapper.selectList(new QueryWrapper<Dict>().eq("parent_id",parentId));
        dictList.forEach(dict -> {
            //如果有子节点则为非子节点
            boolean hasChildren = this.hasChildren(dict.getId());
            dict.setHasChildren(hasChildren);
        });

        //从数据库中拿值后将数据存入Redis缓存中(设置缓存时间为5分钟),防止存入失败则用try-catch包住
        try {
            redisTemplate.opsForValue().set("srb:core:dictList:"+parentId,dictList,5, TimeUnit.MINUTES);
            log.info("数据存入Redis中");
        } catch (Exception e) {
            log.error("redis服务器异常2："+ ExceptionUtils.getStackTrace(e));
        }

        //返回数据列表
        return dictList;
    }

    /*
    * 判断该节点是否有子节点
    * */
    private boolean hasChildren(Long id){
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<Dict>().eq("parent_id",id);
        Integer count = baseMapper.selectCount(queryWrapper);
        System.out.println(count.intValue());
        return count.intValue()>0;
    }
}
