package com.geek.geekstudio.service.impl;

import com.geek.geekstudio.exception.ParameterError;
import com.geek.geekstudio.mapper.CourseMapper;
import com.geek.geekstudio.mapper.DirectionMapper;
import com.geek.geekstudio.model.po.CoursePO;
import com.geek.geekstudio.model.vo.DirectionVO;
import com.geek.geekstudio.model.vo.RestInfo;
import com.geek.geekstudio.service.CourseService;
import com.geek.geekstudio.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@CacheConfig(cacheNames = "course")
@Service
@Slf4j
public class CourseServiceImpl implements CourseService {
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    DirectionMapper directionMapper;

    /**
     *添加课程
     */
    @Override
    public RestInfo addCourse(int courseId, String courseName) throws ParameterError {
        CoursePO coursePO=courseMapper.queryCourseByCourseId(courseId);
        if(coursePO!=null){
            throw new ParameterError("课程号已存在，不能再添加");
        }
        String addTime= DateUtil.creatDate();
        courseMapper.addCourse(courseId,courseName,addTime);
        return RestInfo.success("添加课程成功！",null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestInfo delCourse(int courseId) throws ParameterError {
        CoursePO coursePO=courseMapper.queryCourseByCourseId(courseId);
        if(coursePO==null){
            throw new ParameterError("课程号不存在，不能删除");
        }
        //(删除课程应该也要删除课程相关的数据-->删除有学生选择了这门课程的记录)
        courseMapper.deleteCourseByCourseId(courseId);
        return RestInfo.success("删除课程成功！",null);
    }

    /**
     * 查询所有开设课程  设置缓存
     * 1.使用注解形式：(case:同一个类中，方法间的调用并不会通过spring代理类去调用，而是直接调用。
     * 所以本类中方法调用的其他带注解的方法，注解不生效)
     * 2.直接使用RedisTemplate操作
     */
    @Cacheable(key = "'AllCourse'")
    @Override
    public RestInfo queryCourse() {
        log.info("查询所有的课程");
        List<CoursePO> coursePOList=courseMapper.queryCourse();
        return RestInfo.success("所有课程数据",coursePOList);
    }

    /**
     *查询自己的选择方向
     */
    @Override
    public RestInfo queryMyCourse(String userId) {
        List<DirectionVO> directionVOList=directionMapper.queryMyCourse(userId);
        for(DirectionVO directionVO:directionVOList){
            CoursePO coursePO=courseMapper.queryCourseByCourseId(directionVO.getCourseId());
            directionVO.setCourseName(coursePO.getCourseName());
        }
        return RestInfo.success("个人所选方向数据",directionVOList);
    }

    /**
     * 更新所有课程的缓存
     */
    @CachePut(key = "'AllCourse'")
    public RestInfo updateCourse() {
        List<CoursePO> coursePOList=courseMapper.queryCourse();
        log.info("更新了所有课程的缓存");
        return RestInfo.success("所有课程数据",coursePOList);
    }
}
