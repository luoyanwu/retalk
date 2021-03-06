package com.jeffrey.retalk.web;

import com.jeffrey.retalk.entity.Tag;
import com.jeffrey.retalk.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class TagController {

    private ITagService tagService;

    @Autowired
    public TagController(ITagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/tag")
    public String index(ModelMap modelMap, Principal principal) {
        List<Tag> tags = tagService.getAllTags("");

        modelMap.addAttribute("tags", tags);

        return "tag/index";
    }

    @GetMapping("/tag/{tagId}")
    public String getOneTag(@PathVariable("tagId") long tagId, ModelMap modelMap, Principal principal) {
        Tag tag = tagService.getTagById(tagId);
        List<Tag> tags = tagService.getAllTags("");
        modelMap.addAttribute("tag", tag);
        modelMap.addAttribute("tags", tags);
        return "tag/display";
    }

    @ResponseBody
    @PostMapping("/tag/{tagId}")
    public Tag getOneTag(@PathVariable("tagId") long tagId) {
        return tagService.getTagById(tagId);
    }

    @ResponseBody
    @PostMapping("/tag/new")
    public Tag newTag(@RequestParam("value") String tagName, Principal principal) {
        Tag isTag = tagService.getTagByName(tagName, "");
        if (isTag != null ) {
            return isTag;
        } else {
            Tag tag = new Tag();
            tag.setName(tagName);
            tag.setUserName("");
            tagService.insertTag(tag);
            return tag;
        }
    }

    @ResponseBody
    @PostMapping("/tag/all")
    public List<Tag> getAllTags(Principal principal) {
        return tagService.getAllTags("");
    }

    @PostMapping("/tag/delete/{tagId}")
    public String deleteOneTag(@PathVariable("tagId") long tagId) {
        tagService.deleteTagById(tagId);

        return "redirect:/";
    }

}
