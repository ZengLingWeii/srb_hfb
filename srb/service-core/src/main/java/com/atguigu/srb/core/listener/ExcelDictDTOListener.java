package com.atguigu.srb.core.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.srb.core.mapper.DictMapper;
import com.atguigu.srb.core.pojo.dto.ExcelDictDTO;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;

//ExcelDict导入的监听器
@Slf4j
@NoArgsConstructor
public class ExcelDictDTOListener extends AnalysisEventListener<ExcelDictDTO> {

    /*
    *  每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
    * */
    private static final int BATCH_COUNT = 5;
    List<ExcelDictDTO> list = new ArrayList();

    //dictMapper操作数据库
    private DictMapper dictMapper;

    //构造器中传入mapper对象
    public ExcelDictDTOListener(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

    /*
    * 遍历每一行的记录
    * */
    @Override
    public void invoke(ExcelDictDTO data, AnalysisContext context) {
        log.info("解析到一条记录: {}", data);
        log.info("ExcelDictDTO的id: {}",data.getId());
        log.info("ExcelDictDTO的parent_id: {}",data.getParentId());
        list.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            list.clear();
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据(size()<5的最后几条信息)也存储到数据库
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", list.size());
        //批量插入数据库
        dictMapper.insertBatch(list);
        log.info("存储数据库成功！");
    }
}
