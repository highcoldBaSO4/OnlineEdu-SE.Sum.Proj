package com.se231.onlineedu.serviceimpl;

import javax.persistence.EntityManager;
import com.se231.onlineedu.exception.NotFoundException;
import com.se231.onlineedu.exception.NotMatchException;
import com.se231.onlineedu.exception.ValidationException;
import com.se231.onlineedu.message.request.TitleAndDes;
import com.se231.onlineedu.model.*;
import com.se231.onlineedu.repository.PaperRepository;
import com.se231.onlineedu.repository.ResourceRepository;
import com.se231.onlineedu.repository.SectionBranchRepository;
import com.se231.onlineedu.repository.SectionRepository;
import com.se231.onlineedu.service.CourseService;
import com.se231.onlineedu.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Section Service Implementation Class
 *
 * Contains service logic related to section.
 *
 * @author Zhe Li
 * @date 2019/07/19
 */
@Service
public class SectionServiceImpl implements SectionService {
    @Autowired
    CourseService courseService;

    @Autowired
    SectionRepository sectionRepository;

    @Autowired
    SectionBranchRepository sectionBranchRepository;

    @Autowired
    PaperRepository paperRepository;

    @Autowired
    ResourceRepository resourceRepository;

    @Autowired
    EntityManager entityManager;

    @Override
    public Section createSection(Long courseId, Integer secNo, String title) {
        Course course = courseService.getCourseInfo(courseId);
        if(secNo>sectionRepository.currentSecNo(courseId).orElse(0)||secNo<0){
            throw new ValidationException("Invalid section number.");
        }
        int secId = sectionRepository.currentSecId(courseId).orElse(0);
        sectionRepository.updateSecNo(courseId,secNo);
        Section section = new Section();
        section.setTitle(title);
        SectionPrimaryKey sectionPrimaryKey = new SectionPrimaryKey(course,secId+1);
        section.setSecNo(secNo+1);
        section.setSectionPrimaryKey(sectionPrimaryKey);
        return sectionRepository.save(section);
    }

    @Override
    public SectionBranches createBranch(Long courseId, int secId,int branchNo, TitleAndDes form) {
        Course course = courseService.getCourseInfo(courseId);
        SectionPrimaryKey sectionPrimaryKey = new SectionPrimaryKey(course,secId);
        Section section = sectionRepository.findById(sectionPrimaryKey).orElseThrow(()->new NotFoundException("章节不存在"));
        if(branchNo>sectionBranchRepository.currentBranchNo(courseId,secId).orElse(0)||branchNo<0){
            throw new ValidationException("Invalid branch number.");
        }
        int branchId = sectionBranchRepository.currentBranchId(courseId,secId).orElse(0);
        sectionBranchRepository.updateSecBranchNo(courseId, secId, branchNo);
        SectionBranchesPrimaryKey sectionBranchesPrimaryKey = new SectionBranchesPrimaryKey(section,branchId+1);
        SectionBranches sectionBranches = new SectionBranches();
        sectionBranches.setTitle(form.getTitle());
        sectionBranches.setDescription(form.getDescription());
        sectionBranches.setBranchNo(branchNo+1);
        sectionBranches.setSectionBranchesPrimaryKey(sectionBranchesPrimaryKey);
        return sectionBranchRepository.save(sectionBranches);
    }

    @Override
    public Section issuePaper(Long courseId, int secId, int branchId, Long paperId) {
        Course course = courseService.getCourseInfo(courseId);
        Section section = sectionRepository.findById(new SectionPrimaryKey(course,secId))
                .orElseThrow(()->new NotFoundException("No corresponding question"));
        Paper paper = paperRepository.findById(paperId)
                .orElseThrow(()->new NotFoundException("No corresponding paper"));
        if(!paper.getCourse().equals(course)){
            throw new NotMatchException("该试卷不在课程中");
        }
        section.getPapers().add(paper);
        SectionBranches sectionBranches = sectionBranchRepository.findById(new SectionBranchesPrimaryKey(section,branchId))
                .orElseThrow(()->new NotFoundException("小节不存在"));
        sectionBranches.getPapers().add(paper);
        sectionBranchRepository.save(sectionBranches);
        return sectionRepository.save(section);
    }

    @Override
    public Section issueResource(Long courseId, int secId, int branchId, Long resourceId) {
        Course course = courseService.getCourseInfo(courseId);
        Section section = sectionRepository.findById(new SectionPrimaryKey(course,secId))
                .orElseThrow(()->new NotFoundException("No corresponding section"));
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(()->new NotFoundException("No corresponding paper"));
        if(section.getResources().contains(resource)){
            throw new ValidationException("This resource has already been issued.");
        }
        section.getResources().add(resource);
        if(!course.getCoursePrototype().getResources().contains(resource)){
            throw new NotMatchException("该课程原型中无该资源");
        }
        SectionBranches sectionBranches = sectionBranchRepository.findById(new SectionBranchesPrimaryKey(section,branchId))
                .orElseThrow(()->new NotFoundException("小节不存在"));
        sectionBranches.getResources().add(resource);
        sectionBranchRepository.save(sectionBranches);
        return sectionRepository.save(section);
    }
}
