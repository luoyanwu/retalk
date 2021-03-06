package com.jeffrey.retalk.web;

import com.jeffrey.retalk.dto.ArchiveGroup;
import com.jeffrey.retalk.entity.Article;
import com.jeffrey.retalk.entity.Tag;
import com.jeffrey.retalk.service.IArticleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.*;


@Controller
public class ArticleController {
    @Autowired
    private IArticleService articleService;


    public ArticleController(IArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/article")
    public String index(ModelMap modelMap, Principal principal) {
        long articlesCount = articleService.getArticlesCount("");

        List<Article> articles = articleService.getArticlesOfOnePage("", 1,15);

        modelMap.addAttribute("articles", articles);
        modelMap.addAttribute("articlesCount", articlesCount);
        modelMap.addAttribute("isIndex", true);

        return "article/index";
    }

    @PostMapping(value = "/article/add")
    public Long newArticle(@RequestBody Article article) {
        article.setCreatedTime(new Date());
        article.setModifiedTime(new Date());
        article.setUserName("");
        Long id = articleService.insertArticle(article);
        return article.getId();
    }

    @GetMapping("/article/new")
    public Article newArticlePage(ModelMap modelMap,Article article) {

        modelMap.addAttribute("article", article);

        return article;
    }

    @GetMapping(value = "/article/{articleId}")
    public String getArticle(@PathVariable long articleId, ModelMap modelMap, Principal principal) {

        Article article = articleService.getArticleById(articleId, "");

        modelMap.addAttribute("article", article);

        return "article/display";
    }

    @GetMapping(value = "/article/modify/{articleId}")
    public String updateArticle(@PathVariable long articleId, ModelMap modelMap, Principal principal) {
        Article article = articleService.getArticleById(articleId, "");
        List<Tag> tags = articleService.getAllTagsOfOneArticle(articleId);

        modelMap.addAttribute("article", article);
        modelMap.addAttribute("tags", tags);

        return "article/modify";
    }

    @PostMapping(value = "/page/{page}")
    @ResponseBody
    public List<Article> pagination(@PathVariable int page, Principal principal) {
        return articleService.getArticlesOfOnePage("", page,15);
    }


    @PostMapping(value = "/article/delete/{articleId}")
    @ResponseBody
    public String deleteArticle(@PathVariable String articleId, Principal principal) {
        articleService.deleteArticleById(Integer.parseInt(articleId));
        return "0";
    }

    @ResponseBody
    @PostMapping(value = "/article/update")
    public Article updateArticle(@ModelAttribute("article") Article article) {
        //int page = articleService.getArticlePage("", article.getId());
        articleService.updateArticle(article);

        return article;
    }


    @GetMapping("/article/archive")
    public String archive(ModelMap modelMap,
                        HttpSession session) {

        List<ArchiveGroup> archiveGroups = articleService.groupByYearMonth("");
        Map<ArchiveGroup,List<Article>> archives = new LinkedHashMap<ArchiveGroup,List<Article>>();
        for (ArchiveGroup archiveGroup : archiveGroups)
        {
            if(StringUtils.isNotBlank(archiveGroup.getTime()))
            {
                List article = articleService.getArticlesByYearMonth("",archiveGroup.getTime());
                archives.put(archiveGroup,article);
            }
        }
        modelMap.addAttribute("archives",archives);
        return "/article/archive";
    }
}
