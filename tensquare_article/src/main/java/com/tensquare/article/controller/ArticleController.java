package com.tensquare.article.controller;

import com.tensquare.article.pojo.Article;
import com.tensquare.article.service.ArticleService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @RequestMapping(value = "/examine/{articleId}", method = RequestMethod.PUT)
    public Result examine(@PathVariable String articleId) {
        articleService.updateState(articleId);
        return new Result(true, StatusCode.OK, "Examine Success");
    }

    @RequestMapping(value = "/thumbup/{articleId}", method = RequestMethod.PUT)
    public Result thumbup(@PathVariable String articleId) {
        articleService.addThumbup(articleId);
        return new Result(true, StatusCode.OK, "Like Success");
    }

    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "Query Success", articleService.findAll());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "Query Success", articleService.findById(id));
    }

    @RequestMapping(value = "/search/{page}/{size}", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap, @PathVariable int page, @PathVariable int size) {
        Page<Article> pageList = articleService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, "Query Success", new PageResult<Article>(pageList.getTotalElements(), pageList.getContent()));
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, "Query Success", articleService.findSearch(searchMap));
    }

    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Article article) {
        articleService.add(article);
        return new Result(true, StatusCode.OK, "Create Success");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody Article article, @PathVariable String id) {
        article.setId(id);
        articleService.update(article);
        return new Result(true, StatusCode.OK, "Update Success");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        articleService.deleteById(id);
        return new Result(true, StatusCode.OK, "Delete Success");
    }
}
