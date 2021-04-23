package com.se231.onlineedu.service;

import com.se231.onlineedu.message.request.TitleAndDes;
import com.se231.onlineedu.model.Section;
import com.se231.onlineedu.model.SectionBranches;

/**
 * Section Service Interface
 *
 * interface of service related to section
 *
 * @author Zhe Li
 * @date 2019/07/19
 */
public interface SectionService {
    /**
     * this service allow teacher to create a new section
     * @param courseId  id of course
     * @param secNo section number of current section
     * @param title  title  of new section
     * @return  section info
     */
    Section createSection(Long courseId, Integer secNo, String title);

    /**
     * this service allow teacher to append a new branch after current branch in section
     * @param courseId  id of course
     * @param secNo number of section
     * @param branchNo number of current branch
     * @param form title and description of branch
     * @return  branch info
     */
    SectionBranches createBranch(Long courseId,int secNo,int branchNo,TitleAndDes form);

    /**
     * this service allow teacher to issue a existing paper to a section
     * @param courseId  id of the course
     * @param secNo number of the section
     * @param branchNo number of branch
     * @param paperId id of issued paper
     * @return  the section which is modified
     * @throws Exception    mainly contain not found exception
     */
    Section issuePaper(Long courseId,int secNo,int branchNo,Long paperId)throws Exception;

    /**
     * this service allow teacher to issue a existing paper to a section
     * @param courseId  id of the course
     * @param secNo number of the section
     * @param branchNo number of branch
     * @param resourceId id of issued resource
     * @return  the section which is modified
     * @throws Exception    mainly contain not found exception
     */
    Section issueResource(Long courseId,int secNo,int branchNo,Long resourceId);
}
