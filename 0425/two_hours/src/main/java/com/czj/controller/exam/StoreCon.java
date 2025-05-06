package com.czj.controller.exam;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czj.controller.util.DEA;
import com.czj.pojo.exam.Exam;
import com.czj.pojo.exam.Store;
import com.czj.pojo.util.SearchForm;
import com.czj.service.exam.ExamService;
import com.czj.service.exam.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@RestController
@RequestMapping("/Store")
public class StoreCon {

    @Autowired
    private StoreService storeService;

    /**
     * 分页条件查询考试
     * @return 数据集
     */
    @PostMapping("/getExam")
    public DEA getExam(@RequestBody SearchForm searchForm, HttpServletRequest request){
        // 获取课程id
        HttpSession session = request.getSession();
        int courseId = (int) session.getAttribute("courseId");
        String title = searchForm.title;
        // 封装查询类
        QueryWrapper<Store> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("title", title);
        queryWrapper.eq("course_id", courseId);
        queryWrapper.eq("type", Store.TExam);
        queryWrapper.orderByAsc("begin_date");
        // 查询页面总数
        int count = storeService.count(queryWrapper);
        int currentPage = searchForm.currentPage;
        int pageSize = searchForm.pageSize;
        // 超界检测-默认归零
        if(count < (currentPage - 1) * pageSize){
            currentPage = 1;
        }
        // 封装分页信息
        IPage<Store> page = new Page<>(currentPage,pageSize);
        return  new DEA(storeService.page(page,queryWrapper));
    }

    /**
     * 修改信息
     * @param store 数据表单
     * @return DEA
     */
    @RequestMapping("/updateInfo")
    @PostMapping
    public DEA updateInfo(@RequestBody Store store) {
        UpdateWrapper<Store> updateWrapper = new UpdateWrapper();
        // 设置查询类
        updateWrapper.eq("id", store.getId());
        // 更新数据
        return new DEA(storeService.update(store,updateWrapper));
    }

    /**
     * 创建一条考试
     * @param store 包含考试的名称，描述，起止时间，类型，有序
     * @return 成功标记
     */
    @PostMapping("/createExam")
    public DEA createExam(@RequestBody Store store, HttpServletRequest request){
        // 获取发布者，课程ID，及创建时间
        store.setAuthorId((String) request.getSession().getAttribute("userId"));
        store.setCourseId((Integer) request.getSession().getAttribute("courseId"));
        store.setCreateTime(new Date());
        return new DEA(storeService.save(store));
    }

    /**
     * 更新一条考试
     * @param store 包含考试的id，名称，描述，起止时间，类型，有序
     * @return 成功标记
     */
    @PostMapping("/updateExam")
    public DEA updateExam(@RequestBody Store store){
        // 获取发布者，课程ID，及创建时间
        store.setCreateTime(new Date());
        return new DEA(storeService.updateById(store));
    }

    /**
     * 获取一条考试信息
     * @param store 包含id
     * @return 讨论信息
     */
    @PostMapping("/getExamById")
    public DEA getExamById(@RequestBody Store store){
        return new DEA(storeService.getById(store.getId()));
    }

    /**
     * 删除考试 todo 相关数据清理工作未完成
     * @param store 包含id
     */
    @PostMapping("/deleteExamById")
    public DEA deleteExamById(@RequestBody Store store){
        // todo todo todo
        // todo 删除考试前应当清理掉考试相关的全部数据
        // todo todo todo
        return new DEA(storeService.removeById(store.getId()));
    }
}
