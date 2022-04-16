package com.shiji.blog.service;

import com.shiji.blog.NotFoundException;
import com.shiji.blog.dao.TagRepository;
import com.shiji.blog.po.Blog;
import com.shiji.blog.po.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService{
    @Autowired
    private TagRepository tagRepository;

    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        tagRepository.save(tag);
        return tag;
    }

    @Transactional
    @Override
    public Tag getTag(Long id) {
        return tagRepository.findById(id).get();
    }

    @Transactional
    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }


    @Transactional
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public List<Tag> listTag() {
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> listTagTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"blogs.size");
        Pageable pageable = PageRequest.of(0,size,sort);
        return tagRepository.findTop(pageable);
    }

    @Transactional
    @Override
    public List<Tag> listTag(String ids){
        List<Long> list = new ArrayList<>();
        if(ids!=null && !"".equals(ids)){
            String[] arr = ids.split(",");
            for(int i = 0; i < arr.length; i++){
                list.add(new Long(arr[i]));
            }
        }
        return tagRepository.findAllById(list);
    };

    @Transactional
    @Override
    public List<Tag> listTag(Integer size){
        Sort sort =  Sort.by(Sort.Order.desc("blogs.size"));
        Pageable pageable = PageRequest.of(0, size, sort);
        return tagRepository.findTop(pageable);
    }

    @Transactional
    @Override
    public List<Tag> listTagPublishNumber() {
        List<Tag> tags = listTag();
        for(Tag tag : tags){
            int i = 0;
            List<Blog> blogs = tag.getBlogs();
            for(Blog blog: blogs){
                if(blog.isPublished()) i++;
            }
            tag.setPublishedBlogsN(i);
        }
        return tags;
    }

    @Transactional
    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag t = tagRepository.getOne(id);
        if(t==null){
            throw new NotFoundException("没有找到标签！");
        }
        BeanUtils.copyProperties(tag, t);
        return tagRepository.save(t);
    }

    @Transactional
    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);

    }
}